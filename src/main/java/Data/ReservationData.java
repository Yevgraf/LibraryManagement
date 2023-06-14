package Data;

import Model.*;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static Data.BookData.loadCategoryById;

public class ReservationData {

    public void save(Reservation reservation) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement selectStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM Reservation WHERE memberId = ?");
             PreparedStatement insertReservationStatement = connection.prepareStatement(
                     "INSERT INTO Reservation (memberId, startDate, endDate, state, satisfactionRating, additionalComments) " +
                             "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertReservationProductStatement = connection.prepareStatement(
                     "INSERT INTO ReservationProduct (reservationId, productId) VALUES (?, ?)")) {

            int maxReservations = 3;
            int memberId = reservation.getMember().getId();
            State state = reservation.getState();

            int totalReservations = getNumberOfReservations(memberId, state);
            int maxBorrowedItems = 3;
            MemberData memberData = new MemberData();
            int totalBorrowedItems = memberData.getNumberOfBorrowedItems(memberId);

            if (totalReservations >= maxReservations) {
                System.out.println("O membro já possui " + totalReservations + " reservas. Não é permitido ter mais de " + maxReservations + " reservas no total.");
                System.out.println("A reserva não foi adicionada.");
                return;
            }

            if (totalBorrowedItems + reservation.getItemsCount() > maxBorrowedItems) {
                System.out.println("O membro já possui " + totalBorrowedItems + " itens emprestados. Não é permitido ter mais de " + maxBorrowedItems + " itens emprestados no total.");
                System.out.println("A reserva não foi adicionada.");
                return;
            }

            if (reservationExists(reservation)) {
                System.out.println("A reserva já existe para o membro.");
                System.out.println("A reserva não foi adicionada.");
                return;
            }

            try {
                // Save the reservation
                insertReservationStatement.setInt(1, memberId);
                insertReservationStatement.setDate(2, java.sql.Date.valueOf(reservation.getStartDate()));
                insertReservationStatement.setDate(3, java.sql.Date.valueOf(reservation.getEndDate()));
                insertReservationStatement.setString(4, reservation.getState().toString());
                insertReservationStatement.setInt(5, reservation.getSatisfactionRating());
                insertReservationStatement.setString(6, reservation.getAdditionalComments());

                int affectedRows = insertReservationStatement.executeUpdate();
                if (affectedRows == 0) {
                    System.out.println("Falha ao salvar a reserva, sem linhas afetadas.");
                    System.out.println("A reserva não foi adicionada.");
                    return;
                }

                // Get the generated reservation ID
                ResultSet generatedKeys = insertReservationStatement.getGeneratedKeys();
                int reservationId;
                if (generatedKeys.next()) {
                    reservationId = generatedKeys.getInt(1);
                } else {
                    System.out.println("Falha ao obter o ID da reserva gerado.");
                    System.out.println("A reserva não foi adicionada.");
                    return;
                }

                // Save the associated products
                for (Book book : reservation.getBooks()) {
                    insertReservationProductStatement.setInt(1, reservationId);
                    insertReservationProductStatement.setInt(2, book.getId());
                    insertReservationProductStatement.addBatch();
                }

                for (CD cd : reservation.getCds()) {
                    insertReservationProductStatement.setInt(1, reservationId);
                    insertReservationProductStatement.setInt(2, cd.getId());
                    insertReservationProductStatement.addBatch();
                }

                // Execute the batch insert for reservation products
                insertReservationProductStatement.executeBatch();

                System.out.println("Reserva adicionada com sucesso.");
            } catch (SQLException e) {
                System.out.println("Erro ao salvar a reserva: " + e.getMessage());
                System.out.println("A reserva não foi adicionada.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao salvar a reserva: " + e.getMessage());
            System.out.println("A reserva não foi adicionada.");
        }
    }

    private boolean reservationExists(Reservation reservation) {
        try (Connection connection = DBconn.getConn()) {
            int memberId = reservation.getMember().getId();

            for (Book book : reservation.getBooks()) {
                int bookId = book.getId();
                try (PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) FROM ReservationProduct WHERE productId = ? AND memberId = ? AND state = 'RESERVADO'")) {
                    countStatement.setInt(1, bookId);
                    countStatement.setInt(2, memberId);

                    try (ResultSet resultSet = countStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int count = resultSet.getInt(1);
                            if (count > 0) {
                                return true; // Reservation already exists for this book and member
                            }
                        }
                    }
                }
            }

            for (CD cd : reservation.getCds()) {
                int cdId = cd.getId();
                try (PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) FROM ReservationProduct WHERE productId = ? AND memberId = ? AND state = 'RESERVADO'")) {
                    countStatement.setInt(1, cdId);
                    countStatement.setInt(2, memberId);

                    try (ResultSet resultSet = countStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int count = resultSet.getInt(1);
                            if (count > 0) {
                                return true;
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    private int getNumberOfReservations(int memberId, State state) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) FROM Reservation JOIN ReservationProduct ON Reservation.id = ReservationProduct.reservationId WHERE Reservation.memberId = ? AND Reservation.state = ?")) {

            countStatement.setInt(1, memberId);
            countStatement.setString(2, state.toString());

            try (ResultSet resultSet = countStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    private Book loadBook(int bookId) {
        Book book = null;
        try (Connection connection = DBconn.getConn(); PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM dbo.Product WHERE id = ?")) {

            selectStatement.setInt(1, bookId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String subtitle = resultSet.getString("subtitle");
                int authorId = resultSet.getInt("authorId");
                int numPages = resultSet.getInt("numPages");
                int categoryId = resultSet.getInt("categoryId");
                LocalDate publicationDate = resultSet.getDate("publicationDate") != null ? resultSet.getDate("publicationDate").toLocalDate() : null;
                int ageRangeId = resultSet.getInt("ageRangeId");
                int publisherId = resultSet.getInt("publisherId");
                String isbn = resultSet.getString("isbn");
                int quantity = resultSet.getInt("quantity");

                // Load the Author, Category, AgeRange, and Publisher objects
                Author author = BookData.loadAuthorById(authorId);
                Category category = loadCategoryById(categoryId);
                AgeRange ageRange = BookData.loadAgeRangeById(ageRangeId);
                Publisher publisher = BookData.loadPublisherById(publisherId);

                // Create a Book object
                book = new Book(title, subtitle, author, numPages, category, publicationDate, ageRange, publisher, isbn, quantity);
                book.setId(bookId);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar livro do banco de dados: " + e.getMessage());
        }
        return book;
    }

    private Member loadMemberById(int memberId) {
        Member member = null;

        try (Connection connection = DBconn.getConn(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.Member WHERE id = ?")) {

            statement.setInt(1, memberId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                LocalDate birthDate = resultSet.getDate("birthDate").toLocalDate();
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                int maxBorrowedBooks = resultSet.getInt("maxBorrowedBooks");

                // Assuming there is a LibraryUser class, create an instance of it using the userId
                User user = loadLibraryUserById(userId);

                member = new Member(memberId, user, name, address, birthDate, phone, email, maxBorrowedBooks);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar membro por ID: " + e.getMessage());
        }

        return member;
    }

    private User loadLibraryUserById(int userId) {
        User libraryUser = null;

        try (Connection connection = DBconn.getConn(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.LibraryUser WHERE id = ?")) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                LocalDate birthDate = resultSet.getDate("birthDate").toLocalDate();
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");

                libraryUser = new User(userId, name, address, birthDate, phone, email);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar usuário da biblioteca por ID: " + e.getMessage());
        }

        return libraryUser;
    }

    private Artist loadArtistById(int artistId) {
        Artist artist = null;
        try (Connection connection = DBconn.getConn(); PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM dbo.Artist WHERE id = ?")) {

            selectStatement.setInt(1, artistId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                // You can load other attributes of the artist if needed

                // Create an Artist object
                artist = new Artist(name);
                artist.setId(artistId);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar artista do banco de dados: " + e.getMessage());
        }
        return artist;
    }


    public void updateState(Reservation reservation, State newState) {
        try (Connection connection = DBconn.getConn(); PreparedStatement updateStatement = connection.prepareStatement("UPDATE dbo.Reservation SET state = ? WHERE id = ?")) {

            updateStatement.setString(1, newState.toString());
            updateStatement.setInt(2, reservation.getId());

            int affectedRows = updateStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao atualizar o estado da reserva, sem linhas afetadas.");
            }

            reservation.setState(newState); // Update the state of the reservation object

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o estado da reserva no banco de dados: " + e.getMessage());
        }
    }

    private CD loadCD(int cdId) {
        CD cd = null;
        try (Connection connection = DBconn.getConn();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM dbo.Product WHERE id = ?")) {

            selectStatement.setInt(1, cdId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                int artistId = resultSet.getInt("artistId");
                int releaseYear = resultSet.getInt("releaseYear");
                int numTracks = resultSet.getInt("numTracks");
                int categoryId = resultSet.getInt("categoryId");
                int quantity = resultSet.getInt("quantity");

                // Load the Artist object
                Artist artist = loadArtistById(artistId);

                // Load the Category object
                Category category = loadCategoryById(categoryId);

                // Create a CD object
                cd = new CD(title, artist, releaseYear, numTracks, category, quantity);


                cd.setId(cdId);
            }
        } catch (SQLException e) {
            System.err.println("Error while loading CD from the database: " + e.getMessage());
        }
        return cd;
    }


    public List<Reservation> getReservationsForMember(Member member) {
        List<Reservation> memberReservations = new ArrayList<>();
        List<Reservation> reservations = load();
        for (Reservation r : reservations) {
            if (r.getMember().equals(member)) {
                memberReservations.add(r);
            }
        }
        return memberReservations;
    }

    public void updateReservationAdditionalInfo(Reservation reservation, int satisfactionRating, String additionalComments) {
        try (Connection connection = DBconn.getConn(); PreparedStatement statement = connection.prepareStatement("UPDATE dbo.Reservation SET satisfactionRating = ?, additionalComments = ? WHERE id = ?")) {

            statement.setInt(1, satisfactionRating);
            statement.setString(2, additionalComments);
            statement.setInt(3, reservation.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Erro ao atualizar informação adicional da reserva.");
            }

            // Update the reservation object with the new values
            reservation.setSatisfactionRating(satisfactionRating);
            reservation.setAdditionalComments(additionalComments);
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar informação adicional da reserva: " + e.getMessage());
        }
    }

    public void updateReservationState(Reservation reservation, State newState) {
        try (Connection connection = DBconn.getConn(); PreparedStatement statement = connection.prepareStatement("UPDATE dbo.Reservation SET state = ? WHERE id = ?")) {

            statement.setString(1, newState.toString());
            statement.setInt(2, reservation.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao mudar estado reserva.");
            }


            reservation.setState(newState);
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar estado de reserva: " + e.getMessage());
        }
    }

    public List<Reservation> load() {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = DBconn.getConn();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT r.*, p.type " +
                     "FROM dbo.Reservation AS r " +
                     "INNER JOIN dbo.ReservationProduct AS rp ON r.id = rp.reservationId " +
                     "INNER JOIN dbo.Product AS p ON rp.productId = p.id");
             ResultSet resultSet = selectStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int memberId = resultSet.getInt("memberId");
                LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                LocalDate endDate = resultSet.getDate("endDate").toLocalDate();
                State state = State.valueOf(resultSet.getString("state"));
                int satisfactionRating = resultSet.getInt("satisfactionRating");
                String additionalComments = resultSet.getString("additionalComments");
                String productType = resultSet.getString("type");

                Member member = loadMemberById(memberId);

                Reservation reservation = new Reservation(member, startDate, endDate);

                reservation.setId(id);
                reservation.setState(state);
                reservation.setSatisfactionRating(satisfactionRating);
                reservation.setAdditionalComments(additionalComments);

                // Load associated products
                List<Book> books = new ArrayList<>();
                List<CD> cds = new ArrayList<>();

                try (PreparedStatement productStatement = connection.prepareStatement("SELECT * FROM dbo.ReservationProduct WHERE reservationId = ?")) {
                    productStatement.setInt(1, id);
                    try (ResultSet productResultSet = productStatement.executeQuery()) {
                        while (productResultSet.next()) {
                            int productId = productResultSet.getInt("productId");
                            if (productType.equals("book")) {
                                Book book = loadBook(productId);
                                if (book != null) {
                                    books.add(book);
                                }
                            } else if (productType.equals("cd")) {
                                CD cd = loadCD(productId);
                                if (cd != null) {
                                    cds.add(cd);
                                }
                            }
                        }
                    }
                }

                // Add the loaded books and CDs to the reservation
                reservation.setBooks(books);
                reservation.setCds(cds);

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error while loading reservations from the database: " + e.getMessage());
        }
        return reservations;
    }


}
