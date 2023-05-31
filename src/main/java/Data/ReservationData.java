package Data;

import Model.*;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationData {


    public void save(Reservation reservation) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement selectStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM dbo.Reservation WHERE memberId = ?");
             PreparedStatement insertStatement = connection.prepareStatement(
                     "INSERT INTO dbo.Reservation (bookId, memberId, startDate, endDate, state, satisfactionRating, additionalComments, cdId) VALUES (?, ?, ?, ?, ?, ?, ?, ?)\n")) {

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
                if (reservation.getBook() != null) {
                    insertStatement.setInt(1, reservation.getBook().getId());
                } else {
                    insertStatement.setNull(1, java.sql.Types.INTEGER);
                }


                insertStatement.setInt(2, memberId);
                insertStatement.setDate(3, java.sql.Date.valueOf(reservation.getStartDate()));
                insertStatement.setDate(4, java.sql.Date.valueOf(reservation.getEndDate()));
                insertStatement.setString(5, reservation.getState().toString());
                insertStatement.setInt(6, reservation.getSatisfactionRating());
                insertStatement.setString(7, reservation.getAdditionalComments());

                if (reservation.getCd() != null) {
                    insertStatement.setInt(8, reservation.getCd().getId());
                } else {
                    insertStatement.setNull(8, java.sql.Types.INTEGER);
                }

                int affectedRows = insertStatement.executeUpdate();
                if (affectedRows == 0) {
                    System.out.println("Falha ao salvar a reserva, sem linhas afetadas.");
                    System.out.println("A reserva não foi adicionada.");
                    return;
                }

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
        try (Connection connection = DBconn.getConn(); PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) FROM Reservation WHERE bookId = ? AND memberId = ?")) {
            int memberId = reservation.getMember().getId();
            int bookId = reservation.getBook() != null ? reservation.getBook().getId() : 0; //set bookId para 0 caso a reserva que esteja a ser feita não tenha livro (null)

            countStatement.setInt(1, bookId);
            countStatement.setInt(2, memberId);

            try (ResultSet resultSet = countStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }




    private int getNumberOfReservations(int memberId, State state) {
        try (Connection connection = DBconn.getConn(); PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) FROM Reservation WHERE memberId = ? AND state = ?")) {

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

//    public List<Reservation> load() {
//        List<Reservation> reservations = new ArrayList<>();
//        try (Connection connection = DBconn.getConn(); PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM dbo.Reservation"); ResultSet resultSet = selectStatement.executeQuery()) {
//
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                int bookId = resultSet.getInt("bookId");
//                int cdId = resultSet.getInt("cdId");
//                int memberId = resultSet.getInt("memberId");
//                LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
//                LocalDate endDate = resultSet.getDate("endDate").toLocalDate();
//                State state = State.valueOf(resultSet.getString("state"));
//                int satisfactionRating = resultSet.getInt("satisfactionRating");
//                String additionalComments = resultSet.getString("additionalComments");
//
//                // Load associated book, CD, and member objects
//                Book book = loadBook(bookId);
//                CD cd = loadCD(cdId);
//                Member member = loadMemberById(memberId);
//
//                Reservation reservation = new Reservation(id, book, cd, member, startDate, endDate, state, satisfactionRating, additionalComments);
//                reservations.add(reservation);
//            }
//        } catch (SQLException e) {
//            System.err.println("Erro ao carregar reservas do banco de dados: " + e.getMessage());
//        }
//        return reservations;
//    }


    private Book loadBook(int bookId) {
        Book book = null;
        try (Connection connection = DBconn.getConn(); PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM dbo.Book WHERE id = ?")) {

            selectStatement.setInt(1, bookId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String subtitle = resultSet.getString("subtitle");
                int authorId = resultSet.getInt("authorId");
                int numPages = resultSet.getInt("numPages");
                int categoryId = resultSet.getInt("categoryId");
                LocalDate publicationDate = resultSet.getDate("publicationDate").toLocalDate();
                int ageRangeId = resultSet.getInt("ageRangeId");
                int publisherId = resultSet.getInt("publisherId");
                String isbn = resultSet.getString("isbn");
                int quantity = resultSet.getInt("quantity");

                // Load the Author, Category, AgeRange, and Publisher objects
                Author author = BookData.loadAuthorById(authorId);
                Category category = BookData.loadCategoryById(categoryId);
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

    private CD loadCD(int cdId) {
        CD cd = null;
        try (Connection connection = DBconn.getConn(); PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM dbo.CD WHERE id = ?")) {

            selectStatement.setInt(1, cdId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                int releaseYear = resultSet.getInt("releaseYear");
                int numTracks = resultSet.getInt("numTracks");
                int categoryId = resultSet.getInt("categoryId");
                int quantity = resultSet.getInt("quantity");

                // Load the Category object
                Category category = BookData.loadCategoryById(categoryId);

                // Create a CD object
                cd = new CD(title, artist, releaseYear, numTracks, category, quantity);
                cd.setId(cdId);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar CD do banco de dados: " + e.getMessage());
        }
        return cd;
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
        try (Connection connection = DBconn.getConn(); PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM dbo.Reservation"); ResultSet resultSet = selectStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int bookId = resultSet.getInt("bookId");
                int cdId = resultSet.getInt("cdId");
                int memberId = resultSet.getInt("memberId");
                LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                LocalDate endDate = resultSet.getDate("endDate").toLocalDate();
                State state = State.valueOf(resultSet.getString("state"));
                int satisfactionRating = resultSet.getInt("satisfactionRating");
                String additionalComments = resultSet.getString("additionalComments");


                Book book = null;
                if (bookId != 0) {
                    book = loadBook(bookId);
                }

                CD cd = null;
                if (cdId != 0) {
                    cd = loadCD(cdId);
                }

                Member member = loadMemberById(memberId);

                Reservation reservation;
                if (book != null && cd != null) {
                    reservation = new Reservation(id, book, cd, member, startDate, endDate, state, satisfactionRating, additionalComments);
                } else if (book != null) {
                    reservation = new Reservation(id, book, member, startDate, endDate, state, satisfactionRating, additionalComments);
                } else if (cd != null) {
                    reservation = new Reservation(id, cd, member, startDate, endDate, state, satisfactionRating, additionalComments);
                } else {
                    continue; // Skip this reservation if both book and CD are null
                }

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar reservas do banco de dados: " + e.getMessage());
        }
        return reservations;
    }


}
