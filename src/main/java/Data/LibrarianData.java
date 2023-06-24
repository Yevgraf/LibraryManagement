package Data;

import Model.Librarian;
import Model.Member;
import Model.Reservation;
import Model.User;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibrarianData {


    public static void save(List<? extends User> userList) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement userStatement = connection.prepareStatement("INSERT INTO LibraryUser (name, address, birthDate, phone, email) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement librarianStatement = connection.prepareStatement("INSERT INTO Librarian (userId, password, name, address, birthDate, phone, email) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            for (User user : userList) {
                //verifica se user ou librarian já existem
                if (isUserExists(connection, user.getEmail()) || isLibrarianExists(connection, user.getEmail())) {
                    System.out.println("Utilizador já existe: " + user.getEmail());
                    continue; // skip este user e avança para o próximo
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
                    if (user instanceof Librarian) {
                        Librarian librarian = (Librarian) user;
                        librarianStatement.setInt(1, userId);
                        librarianStatement.setString(2, librarian.getPassword());
                        librarianStatement.setString(3, librarian.getName());
                        librarianStatement.setString(4, librarian.getAddress());
                        librarianStatement.setDate(5, Date.valueOf(librarian.getBirthDate()));
                        librarianStatement.setString(6, librarian.getPhone());
                        librarianStatement.setString(7, librarian.getEmail());
                        librarianStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao guardar utilizadores na base de dados: " + e.getMessage());
        }
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


    public static List<Librarian> load() {
        List<Librarian> userList = new ArrayList<>();
        try (Connection connection = DBconn.getConn();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT LibraryUser.*, Librarian.password FROM LibraryUser JOIN Librarian ON LibraryUser.id = Librarian.userId");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                LocalDate birthDate = resultSet.getDate("birthDate").toLocalDate();
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                Librarian librarian = new Librarian(name, address, birthDate, phone, email, password);
                librarian.setId(id);
                userList.add(librarian);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar utilizadores da base de dados: " + e.getMessage());
        }
        return userList;
    }

    public static void deleteLibrarian(String email) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement librarianStatement = connection.prepareStatement("DELETE FROM Librarian WHERE email = ?");
             PreparedStatement userStatement = connection.prepareStatement("DELETE FROM LibraryUser WHERE email = ?")) {

            librarianStatement.setString(1, email);
            int librarianRowsDeleted = librarianStatement.executeUpdate();

            userStatement.setString(1, email);
            int userRowsDeleted = userStatement.executeUpdate();

            if (librarianRowsDeleted > 0 && userRowsDeleted > 0) {
                System.out.println("Bibliotecário removido com sucesso: " + email);
            } else {
                System.out.println("Bibliotecário não encontrado: " + email);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover o bibliotecário: " + e.getMessage());
        }
    }


    public static List<User> loadUsers() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = DBconn.getConn();
             Statement statement = connection.createStatement()) {
            // Load Librarians
            ResultSet librarianResultSet = statement.executeQuery("SELECT LibraryUser.*, Librarian.password FROM LibraryUser JOIN Librarian ON LibraryUser.id = Librarian.userId");
            while (librarianResultSet.next()) {
                int id = librarianResultSet.getInt("id");
                String name = librarianResultSet.getString("name");
                String address = librarianResultSet.getString("address");
                LocalDate birthDate = librarianResultSet.getDate("birthDate").toLocalDate();
                String phone = librarianResultSet.getString("phone");
                String email = librarianResultSet.getString("email");
                String password = librarianResultSet.getString("password");

                Librarian librarian = new Librarian(name, address, birthDate, phone, email, password);
                librarian.setId(id);
                userList.add(librarian);
            }

            // Load Members
            ResultSet memberResultSet = statement.executeQuery("SELECT LibraryUser.*, Member.maxBorrowedBooks, Member.password FROM LibraryUser JOIN Member ON LibraryUser.id = Member.userId");
            while (memberResultSet.next()) {
                int id = memberResultSet.getInt("id");
                String name = memberResultSet.getString("name");
                String address = memberResultSet.getString("address");
                LocalDate birthDate = memberResultSet.getDate("birthDate").toLocalDate();
                String phone = memberResultSet.getString("phone");
                String email = memberResultSet.getString("email");
                int maxBorrowedBooks = memberResultSet.getInt("maxBorrowedBooks");
                String password = memberResultSet.getString("password");

                Member member = new Member(name, address, birthDate, phone, email, maxBorrowedBooks, password);
                member.setId(id);
                userList.add(member);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar utilizadores da base de dados: " + e.getMessage());
        }
        return userList;
    }


}   