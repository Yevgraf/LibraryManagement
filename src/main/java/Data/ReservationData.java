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
                 PreparedStatement insertStatement = connection.prepareStatement(
                         "INSERT INTO dbo.Reservation (bookId, memberId, startDate, endDate, state, satisfactionRating, additionalComments) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

                insertStatement.setInt(1, reservation.getBook().getId());
                insertStatement.setInt(2, reservation.getMember().getId());
                insertStatement.setDate(3, java.sql.Date.valueOf(reservation.getStartDate()));
                insertStatement.setDate(4, java.sql.Date.valueOf(reservation.getEndDate()));
                insertStatement.setString(5, reservation.getState().toString());
                insertStatement.setInt(6, reservation.getSatisfactionRating());
                insertStatement.setString(7, reservation.getAdditionalComments());

                int affectedRows = insertStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Falha ao salvar a reserva, sem linhas afetadas.");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao salvar a reserva no banco de dados: " + e.getMessage());
            }
        }

        public List<Reservation> load() {
            List<Reservation> reservations = new ArrayList<>();
            try (Connection connection = DBconn.getConn();
                 PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM dbo.Reservation");
                 ResultSet resultSet = selectStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int bookId = resultSet.getInt("bookId");
                    int memberId = resultSet.getInt("memberId");
                    LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                    LocalDate endDate = resultSet.getDate("endDate").toLocalDate();
                    State state = State.valueOf(resultSet.getString("state"));
                    int satisfactionRating = resultSet.getInt("satisfactionRating");
                    String additionalComments = resultSet.getString("additionalComments");

                    // Load associated book and member objects

                    Book book = loadBook(bookId);
                    Member member = loadMemberById(memberId);

                    Reservation reservation = new Reservation(id, book, member, startDate, endDate, state, satisfactionRating, additionalComments);
                    reservations.add(reservation);
                }
            } catch (SQLException e) {
                System.err.println("Erro ao carregar reservas do banco de dados: " + e.getMessage());
            }
            return reservations;
        }
        private Book loadBook(int bookId) {
            Book book = null;
            try (Connection connection = DBconn.getConn();
                 PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM dbo.Book WHERE id = ?")) {

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

            try (Connection connection = DBconn.getConn();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.Member WHERE id = ?")) {

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

            try (Connection connection = DBconn.getConn();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.LibraryUser WHERE id = ?")) {

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

        public void updateState(Reservation reservation, State newState) {
            try (Connection connection = DBconn.getConn();
                 PreparedStatement updateStatement = connection.prepareStatement(
                         "UPDATE dbo.Reservation SET state = ? WHERE id = ?")) {

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



        public List<Reservation> loadReservedReservation() {
        List<Reservation> reservedReservations = new ArrayList<>();
        List<Reservation> reservations = load();
        for (Reservation reservation : reservations) {
            if (reservation.getState() == State.RESERVADO) {
                reservedReservations.add(reservation);
            }
        }
        return reservedReservations;
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

}
