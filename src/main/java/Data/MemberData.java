package Data;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Model.Book;
import Model.CD;
import Model.Member;
import Model.User;

public class MemberData {


    public static void save(List<? extends User> userList) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement userStatement = connection.prepareStatement("INSERT INTO LibraryUser (name, address, birthDate, phone, email) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement memberStatement = connection.prepareStatement("INSERT INTO Member (userId, name, address, birthDate, phone, email, maxBorrowedBooks) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            for (User user : userList) {
                // Check if user, librarian, or member already exists
                if (isUserExists(connection, user.getEmail()) || isLibrarianExists(connection, user.getEmail()) || isMemberExists(connection, user.getEmail())) {

                    continue; // Skip this user and proceed to the next
                }

                userStatement.setString(1, user.getName());
                userStatement.setString(2, user.getAddress());
                userStatement.setDate(3, Date.valueOf(user.getBirthDate()));
                userStatement.setString(4, user.getPhone());
                userStatement.setString(5, user.getEmail());
                userStatement.executeUpdate();

                ResultSet generatedKeys = userStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    if (user instanceof Member) {
                        Member member = (Member) user;
                        memberStatement.setInt(1, userId);
                        memberStatement.setString(2, member.getName());
                        memberStatement.setString(3, member.getAddress());
                        memberStatement.setDate(4, Date.valueOf(member.getBirthDate()));
                        memberStatement.setString(5, member.getPhone());
                        memberStatement.setString(6, member.getEmail());
                        memberStatement.setInt(7, member.getMaxBorrowedBooks());
                        memberStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao guardar utilizadores na base de dados: " + e.getMessage());
        }
    }


    private static boolean isMemberExists(Connection connection, String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM Member JOIN LibraryUser ON Member.id = LibraryUser.id WHERE LibraryUser.email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    private static boolean isLibrarianExists(Connection connection, String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM Librarian WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    private static boolean isUserExists(Connection connection, String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM LibraryUser WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public static List<Member> load() {
        List<Member> memberList = new ArrayList<>();

        try (Connection connection = DBconn.getConn();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Member")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                LocalDate birthDate = resultSet.getDate("birthDate").toLocalDate();
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                int maxBorrowedBooks = resultSet.getInt("maxBorrowedBooks");

                Member member = new Member(id, name, address, birthDate, phone, email, maxBorrowedBooks);

                // Load reserved books for the member
                PreparedStatement reservedBooksStatement = connection.prepareStatement(
                        "SELECT p.id, p.title FROM Reservation r " +
                                "INNER JOIN ReservationProduct rp ON r.id = rp.reservationId " +
                                "INNER JOIN Product p ON rp.productId = p.id " +
                                "WHERE r.memberId = ? AND r.state = 'RESERVADO' AND p.type = 'book'"
                );
                reservedBooksStatement.setInt(1, id);
                ResultSet reservedBooksResultSet = reservedBooksStatement.executeQuery();

                while (reservedBooksResultSet.next()) {
                    int bookId = reservedBooksResultSet.getInt("id");
                    String bookTitle = reservedBooksResultSet.getString("title");

                    Book book = new Book(bookId, bookTitle);
                    member.addBorrowedBook(book);
                }

                // Load reserved CDs for the member
                PreparedStatement reservedCDsStatement = connection.prepareStatement(
                        "SELECT p.id, p.title FROM Reservation r " +
                                "INNER JOIN ReservationProduct rp ON r.id = rp.reservationId " +
                                "INNER JOIN Product p ON rp.productId = p.id " +
                                "WHERE r.memberId = ? AND r.state = 'RESERVADO' AND p.type = 'cd'"
                );
                reservedCDsStatement.setInt(1, id);
                ResultSet reservedCDsResultSet = reservedCDsStatement.executeQuery();

                while (reservedCDsResultSet.next()) {
                    int cdId = reservedCDsResultSet.getInt("id");
                    String cdTitle = reservedCDsResultSet.getString("title");

                    CD cd = new CD(cdId, cdTitle);
                    member.addBorrowedCD(cd);
                }

                memberList.add(member);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar membros da base de dados: " + e.getMessage());
        }

        return memberList;
    }



    public static Member getById(int memberId) {
        Member member = null;

        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Member WHERE id = ?")) {

            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    LocalDate birthDate = resultSet.getDate("birthDate").toLocalDate();
                    String phone = resultSet.getString("phone");
                    String email = resultSet.getString("email");
                    int maxBorrowedBooks = resultSet.getInt("maxBorrowedBooks");

                    member = new Member(name, address, birthDate, phone, email);
                    member.setId(memberId);
                    member.setMaxBorrowedBooks(maxBorrowedBooks);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving member by ID: " + e.getMessage());
        }

        return member;
    }




    public int getNumberOfBorrowedItems(int memberId) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement countStatement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM (SELECT productId FROM ReservationProduct WHERE reservationId IN (SELECT id FROM Reservation WHERE memberId = ? AND state = 'RESERVADO')) AS BorrowedItems")) {

            countStatement.setInt(1, memberId);

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




}
