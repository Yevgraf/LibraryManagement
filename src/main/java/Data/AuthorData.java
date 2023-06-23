package Data;

import Model.Author;
import Model.Book;
import Model.Category;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuthorData {
    /**
     * Saves the given list of authors to the database.
     *
     * @param authorList The list of authors to be saved.
     */
    public static void save(List<Author> authorList) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " + "Author" + " (name, address, birthDate) VALUES (?, ?, ?)")) {

            for (Author author : authorList) {
                if (!isAuthorExists(connection, author)) {
                    statement.setString(1, author.getName());
                    statement.setString(2, author.getAddress());
                    statement.setDate(3, Date.valueOf(author.getBirthDate()));
                    statement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao guardar autores na base de dados: " + e.getMessage());
        }
    }

    /**
     * Loads all authors from the database.
     *
     * @return A list of authors loaded from the database.
     */
    public static List<Author> load() {
        List<Author> authorList = new ArrayList<>();
        try (Connection connection = DBconn.getConn();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + "Author")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                LocalDate birthDate = resultSet.getDate("birthDate").toLocalDate();
                Author author = new Author(name, address, birthDate);
                author.setId(id);
                authorList.add(author);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar autores da base de dados: " + e.getMessage());
        }
        return authorList;
    }
    /**
     * Checks if an author with the same name, address, and birth date already exists in the database.
     *
     * @param connection The database connection.
     * @param author     The author to check for existence.
     * @return true if the author exists, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    private static boolean isAuthorExists(Connection connection, Author author) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + "Author" + " WHERE name = ? AND address = ? AND birthDate = ?");
        statement.setString(1, author.getName());
        statement.setString(2, author.getAddress());
        statement.setDate(3, Date.valueOf(author.getBirthDate()));
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
        return false;
    }

    /**
     * Retrieves an author with the specified name from the list of authors.
     *
     * @param name The name of the author to find.
     * @return The author with the specified name, or null if not found.
     */
    public Author findByName(String name) {
        List<Author> authorList = load();
        return authorList.stream()
                .filter(author -> author.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    /**
     * Retrieves a list of all authors from the database.
     *
     * @return A list of all authors.
     */
    public List<Author> listAuthors() {
        return load();
    }
}
