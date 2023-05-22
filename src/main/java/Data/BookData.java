package Data;

import Model.*;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookData {
    private static final String BOOK_TABLE = "Book";
    private static final String AUTHOR_TABLE = "Author";
    private static final String CATEGORY_TABLE = "Category";
    private static final String AGERANGE_TABLE = "AgeRange";
    private static final String PUBLISHER_TABLE = "Publisher";


    public static void save(List<Book> bookList) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement bookStatement = connection.prepareStatement("INSERT INTO Book (title, subtitle, numPages, publicationDate, isbn, quantity, authorId, categoryId, ageRangeId, publisherId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            for (Book book : bookList) {
                // Check if the author exists in the database, if not, save the author first
                Author author = book.getAuthor();
                int authorId = getAuthorIdByName(connection, author.getName());
                if (authorId == -1) {
                    authorId = saveAuthor(connection, author);
                    author.setId(authorId);
                } else {
                    author.setId(authorId);
                }

                // Check if the category exists in the database, if not, save the category first
                Category category = book.getCategory();
                int categoryId = getCategoryIdByName(connection, category.getCategoryName());
                if (categoryId == -1) {
                    categoryId = saveCategory(connection, category);
                    category.setCategoryId(categoryId);
                } else {
                    category.setCategoryId(categoryId);
                }

                // Check if the age range exists in the database, if not, save the age range first
                AgeRange ageRange = book.getAgeRange();
                int ageRangeId = getAgeRangeIdByName(connection, ageRange.getDescription());
                if (ageRangeId == -1) {
                    ageRangeId = saveAgeRange(connection, ageRange);
                    ageRange.setId(ageRangeId);
                } else {
                    ageRange.setId(ageRangeId);
                }

                // Check if the publisher exists in the database, if not, save the publisher first
                Publisher publisher = book.getPublisher();
                int publisherId = getPublisherIdByName(connection, publisher.getName());
                if (publisherId == -1) {
                    publisherId = savePublisher(connection, publisher);
                    publisher.setId(publisherId);
                } else {
                    publisher.setId(publisherId);
                }

                bookStatement.setString(1, book.getTitle());
                bookStatement.setString(2, book.getSubtitle());
                bookStatement.setInt(3, book.getNumPages());
                bookStatement.setDate(4, Date.valueOf(book.getPublicationDate()));
                bookStatement.setString(5, book.getIsbn());
                bookStatement.setInt(6, book.getQuantity());
                bookStatement.setInt(7, author.getId());
                bookStatement.setInt(8, category.getCategoryId());
                bookStatement.setInt(9, ageRange.getId());
                bookStatement.setInt(10, publisher.getId());
                bookStatement.executeUpdate();

                ResultSet generatedKeys = bookStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int bookId = generatedKeys.getInt(1);
                    book.setId(bookId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving books to the database: " + e.getMessage());
        }
    }




    public static List<Book> load() {
        List<Book> bookList = new ArrayList<>();

        try (Connection connection = DBconn.getConn();
             Statement statement = connection.createStatement()) {
            String query = "SELECT b.id AS bookId, b.title, b.subtitle, b.numPages, b.publicationDate, b.isbn, b.quantity, " +
                    "a.id AS authorId, a.name AS authorName, a.address AS authorAddress, a.birthDate AS authorBirthDate, " +
                    "c.id AS categoryId, c.categoryName, " +
                    "ar.id AS ageRangeId, ar.ageRangeName, " +
                    "p.id AS publisherId, p.name AS publisherName, p.address AS publisherAddress " +
                    "FROM Book b " +
                    "JOIN Author a ON b.authorId = a.id " +
                    "JOIN Category c ON b.categoryId = c.id " +
                    "JOIN AgeRange ar ON b.ageRangeId = ar.id " +
                    "JOIN Publisher p ON b.publisherId = p.id";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("bookId");  // Corrected column name
                String title = resultSet.getString("title");
                String subtitle = resultSet.getString("subtitle");
                int numPages = resultSet.getInt("numPages");
                LocalDate publicationDate = resultSet.getDate("publicationDate").toLocalDate();
                String isbn = resultSet.getString("isbn");
                int quantity = resultSet.getInt("quantity");

                int authorId = resultSet.getInt("authorId");
                String authorName = resultSet.getString("authorName");
                String authorAddress = resultSet.getString("authorAddress");
                LocalDate authorBirthDate = resultSet.getDate("authorBirthDate").toLocalDate();
                Author author = new Author(authorName, authorAddress, authorBirthDate);
                author.setId(authorId);

                int categoryId = resultSet.getInt("categoryId");
                String categoryName = resultSet.getString("categoryName");
                Category category = new Category(categoryName);
                category.setCategoryId(categoryId);

                int ageRangeId = resultSet.getInt("ageRangeId");
                String ageRangeName = resultSet.getString("ageRangeName");
                AgeRange ageRange = new AgeRange(ageRangeName);
                ageRange.setId(ageRangeId);

                int publisherId = resultSet.getInt("publisherId");
                String publisherName = resultSet.getString("publisherName");
                String publisherAddress = resultSet.getString("publisherAddress");
                Publisher publisher = new Publisher(publisherName, publisherAddress);
                publisher.setId(publisherId);

                Book book = new Book(title, subtitle, numPages, publicationDate, isbn, quantity, author, category, ageRange, publisher);
                book.setId(id);
                bookList.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error loading books from the database: " + e.getMessage());
        }

        return bookList;
    }


    // Get author ID by name
    private static int getAuthorIdByName(Connection connection, String authorName) throws SQLException {
        int authorId = -1;
        String query = "SELECT id FROM Author WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, authorName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    authorId = resultSet.getInt("id");
                }
            }
        }
        return authorId;
    }

    // Save author and return the generated ID
    private static int saveAuthor(Connection connection, Author author) throws SQLException {
        int authorId = -1;
        String query = "INSERT INTO Author (name, address, birthDate) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getAddress());
            statement.setDate(3, Date.valueOf(author.getBirthDate()));
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                authorId = generatedKeys.getInt(1);
            }
        }
        return authorId;
    }

    // Get category ID by name
    private static int getCategoryIdByName(Connection connection, String categoryName) throws SQLException {
        int categoryId = -1;
        String query = "SELECT categoryId FROM Category WHERE categoryName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, categoryName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    categoryId = resultSet.getInt("categoryId");
                }
            }
        }
        return categoryId;
    }

    // Save category and return the generated ID
    private static int saveCategory(Connection connection, Category category) throws SQLException {
        int categoryId = -1;
        String query = "INSERT INTO Category (categoryName) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getCategoryName());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                categoryId = generatedKeys.getInt(1);
            }
        }
        return categoryId;
    }

    // Get age range ID by name
    private static int getAgeRangeIdByName(Connection connection, String ageRangeName) throws SQLException {
        int ageRangeId = -1;
        String query = "SELECT ageRangeId FROM AgeRange WHERE ageRangeName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ageRangeName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ageRangeId = resultSet.getInt("ageRangeId");
                }
            }
        }
        return ageRangeId;
    }

    // Save age range and return the generated ID
    private static int saveAgeRange(Connection connection, AgeRange ageRange) throws SQLException {
        int ageRangeId = -1;
        String query = "INSERT INTO AgeRange (ageRangeName) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ageRange.getDescription());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ageRangeId = generatedKeys.getInt(1);
            }
        }
        return ageRangeId;
    }

    // Get publisher ID by name
    private static int getPublisherIdByName(Connection connection, String publisherName) throws SQLException {
        int publisherId = -1;
        String query = "SELECT id FROM Publisher WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, publisherName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    publisherId = resultSet.getInt("id");
                }
            }
        }
        return publisherId;
    }

    // Save publisher and return the generated ID
    private static int savePublisher(Connection connection, Publisher publisher) throws SQLException {
        int publisherId = -1;
        String query = "INSERT INTO Publisher (name, address) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, publisher.getName());
            statement.setString(2, publisher.getAddress());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                publisherId = generatedKeys.getInt(1);
            }
        }
        return publisherId;
    }

    public List<Book> listBooks() {
        return load();
    }
}