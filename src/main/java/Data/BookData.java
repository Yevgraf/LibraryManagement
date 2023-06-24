package Data;

import Model.*;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookData {
    /**
     * Saves the given book to the database.
     *
     * @param book The book to be saved.
     */
    public void save(Book book) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement checkStatement = connection.prepareStatement("SELECT id FROM dbo.Product WHERE isbn = ? AND type = 'book'");
             PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO dbo.Product (title, type, authorId, subtitle, numPages, categoryId, publicationDate, ageRangeId, publisherId, isbn, quantity) VALUES (?, 'book', ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            // Check if the book already exists
            checkStatement.setString(1, book.getIsbn());
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                // Book already exists, update its details instead of inserting a new record
                int existingProductId = resultSet.getInt("id");
                book.setId(existingProductId);
                updateBookDetails(book);
                return;
            }

            // Book does not exist, insert a new record
            insertStatement.setString(1, book.getTitle());
            insertStatement.setInt(2, book.getAuthor().getId());
            insertStatement.setString(3, book.getSubtitle());
            insertStatement.setInt(4, book.getNumPages());
            insertStatement.setInt(5, book.getCategory().getCategoryId());
            insertStatement.setDate(6, java.sql.Date.valueOf(book.getPublicationDate()));
            insertStatement.setInt(7, book.getAgeRange().getId());
            insertStatement.setInt(8, book.getPublisher().getId());
            insertStatement.setString(9, book.getIsbn());
            insertStatement.setInt(10, book.getQuantity());

            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Criação de livro falhou, sem linhas afetadas.");
            }

            ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Falha ao obter o ID gerado do livro.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao guardar livro na base de dados: " + e.getMessage());
        }
    }

    /**
     * Updates the quantity of a book in the database by decreasing it by 1.
     *
     * @param bookId The ID of the book to update.
     */
    public void updateBookQuantityDecrease(int bookId) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("UPDATE dbo.Product SET quantity = quantity - 1 WHERE id = ?")) {
            statement.setInt(1, bookId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao atualizar a quantidade do livro, nenhum registro afetado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar a quantidade do livro no banco de dados: " + e.getMessage());
        }
    }
    /**
     * Updates the quantity of a book in the database by increasing it by 1.
     *
     * @param productId The ID of the product to update.
     */
    public void updateBookQuantityIncrease(int productId) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("UPDATE dbo.Product SET quantity = quantity + 1 WHERE id = ?")) {

            statement.setInt(1, productId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao atualizar a quantidade do produto.");
            } else {
                System.out.println("Quantidade do produto atualizada com sucesso.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar a quantidade do produto: " + e.getMessage());
        }
    }
    /**
     * Updates the details of a book in the database.
     *
     * @param book The book with updated details.
     */
    private void updateBookDetails(Book book) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("UPDATE Product SET title = ?, subtitle = ?, authorId = ?, numPages = ?, categoryId = ?, publicationDate = ?, ageRangeId = ?, publisherId = ?, isbn = ?, quantity = ? WHERE id = ?")) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getSubtitle());
            statement.setInt(3, book.getAuthor().getId());
            statement.setInt(4, book.getNumPages());
            statement.setInt(5, book.getCategory().getCategoryId());
            statement.setDate(6, java.sql.Date.valueOf(book.getPublicationDate()));
            statement.setInt(7, book.getAgeRange().getId());
            statement.setInt(8, book.getPublisher().getId());
            statement.setString(9, book.getIsbn());
            statement.setInt(10, book.getQuantity());
            statement.setInt(11, book.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Detalhes do livro atualizados com sucesso.");
            } else {
                System.out.println("Falha ao atualizar os detalhes do livro.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar os detalhes do livro no banco de dados: " + e.getMessage());
        }
    }



    /**
     * Loads all books from the database.
     *
     * @return A list of books loaded from the database.
     */
    public List<Book> load() {
        List<Book> books = new ArrayList<>();

        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Product WHERE type = 'book'");
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
    static Author loadAuthorById(int authorId) {
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

    static Category loadCategoryById(int categoryId) {
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


    static AgeRange loadAgeRangeById(int ageRangeId) {
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


    static Publisher loadPublisherById(int publisherId) {
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

    /**
     * Deletes a book from the database by ID.
     *
     * @param bookId The ID of the book to delete.
     * @return true if the book is successfully deleted, false otherwise.
     */
    public boolean deleteBook(int bookId) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Product WHERE id = ?")) {

            statement.setInt(1, bookId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao remover livro da base de dados: " + e.getMessage());
            return false;
        }
    }


    /**
     * Retrieves a list of all books from the database.
     *
     * @return A list of all books.
     */
    public List<Book> listBooks() {
        return load();
    }
}