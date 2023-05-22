package Data;

import Model.*;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookData {


    public void save(Book book) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement checkStatement = connection.prepareStatement("SELECT id FROM dbo.Book WHERE isbn = ?");
             PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO dbo.Book (title, subtitle, authorId, numPages, categoryId, publicationDate, ageRangeId, publisherId, isbn, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            // Check if the book already exists
            checkStatement.setString(1, book.getIsbn());
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                // Book already exists, update its details instead of inserting a new record
                int existingBookId = resultSet.getInt("id");
                book.setId(existingBookId);
                updateBookDetails(book);
                return;
            }

            // Book does not exist, insert a new record
            insertStatement.setString(1, book.getTitle());
            insertStatement.setString(2, book.getSubtitle());
            insertStatement.setInt(3, book.getAuthor().getId());
            insertStatement.setInt(4, book.getNumPages());
            insertStatement.setInt(5, book.getCategory().getCategoryId());
            insertStatement.setDate(6, java.sql.Date.valueOf(book.getPublicationDate()));
            insertStatement.setInt(7, book.getAgeRange().getId());
            insertStatement.setInt(8, book.getPublisher().getId());
            insertStatement.setString(9, book.getIsbn());
            insertStatement.setInt(10, book.getQuantity());

            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Criação de lviro falhou, sem linhas afetadas.");
            }

            ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Falha ao obter o ID gerado do livro.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao guardar livro na base dados: " + e.getMessage());
        }
    }

    private void updateBookDetails(Book book) {
        // Implement the code to update the details of an existing book in the database based on its ID
        // You can use a PreparedStatement with an UPDATE statement to perform the update operation
        // Make sure to handle any SQLExceptions appropriately
    }

    public List<Book> load() {
        List<Book> books = new ArrayList<>();

        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.Book");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int bookId = resultSet.getInt("id");
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

                // Retrieve the associated Author, Category, AgeRange, and Publisher objects
                Author author = loadAuthorById(authorId);
                Category category = loadCategoryById(categoryId);
                AgeRange ageRange = loadAgeRangeById(ageRangeId);
                Publisher publisher = loadPublisherById(publisherId);

                // Create a new Book object and add it to the list
                Book book = new Book(bookId, title, subtitle, author, numPages, category, publicationDate, ageRange, publisher, isbn, quantity);
                books.add(book);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar livros do banco de dados: " + e.getMessage());
        }

        return books;
    }

    // Helper methods to load associated objects (Author, Category, AgeRange, Publisher) by ID
    private Author loadAuthorById(int authorId) {
        Author author = null;

        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.Author WHERE id = ?")) {

            statement.setInt(1, authorId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                LocalDate birthDate = resultSet.getDate("birthDate").toLocalDate();

                author = new Author(authorId, name, address, birthDate);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar autor por ID: " + e.getMessage());
        }

        return author;
    }

    private Category loadCategoryById(int categoryId) {
        Category category = null;

        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.Category WHERE categoryId = ?")) {

            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String categoryName = resultSet.getString("categoryName");

                category = new Category(categoryId, categoryName);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar categoria por ID: " + e.getMessage());
        }

        return category;
    }


    private AgeRange loadAgeRangeById(int ageRangeId) {
        AgeRange ageRange = null;

        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.AgeRange WHERE id = ?")) {

            statement.setInt(1, ageRangeId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String description = resultSet.getString("description");

                ageRange = new AgeRange(ageRangeId, description);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar faixa etária por ID: " + e.getMessage());
        }

        return ageRange;
    }


    private Publisher loadPublisherById(int publisherId) {
        Publisher publisher = null;

        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.Publisher WHERE id = ?")) {

            statement.setInt(1, publisherId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");

                publisher = new Publisher(publisherId, name, address);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar editora por ID: " + e.getMessage());
        }

        return publisher;
    }
    public boolean deleteBook(int bookId) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM dbo.Book WHERE id = ?")) {

            statement.setInt(1, bookId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao remover livro da base de dados: " + e.getMessage());
            return false;
        }
    }


    public List<Book> listBooks() {
        return load();
    }
}