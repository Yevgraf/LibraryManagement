package Data;

import Model.Artist;
import Model.CD;
import Model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CDData {

    public void save(CD cd) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO dbo.Product (title, type, artistId, releaseYear, numTracks, categoryId, quantity) VALUES (?, 'cd', ?, ?, ?, ?, ?)")) {

            insertStatement.setString(1, cd.getTitle());
            insertStatement.setInt(2, cd.getArtist().getId()); // Use the artist's ID
            insertStatement.setInt(3, cd.getReleaseYear());
            insertStatement.setInt(4, cd.getNumTracks());
            insertStatement.setInt(5, cd.getCategory().getCategoryId());
            insertStatement.setInt(6, cd.getQuantity());

            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao guardar CDs.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao guardar o CD na base de dados: " + e.getMessage());
        }
    }



    public List<CD> load() {
        List<CD> cds = new ArrayList<>();

        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.Product WHERE type = 'cd'");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int cdId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int artistId = resultSet.getInt("artistId"); // Retrieve the artist ID
                int releaseYear = resultSet.getInt("releaseYear");
                int numTracks = resultSet.getInt("numTracks");
                int categoryId = resultSet.getInt("categoryId");
                int quantity = resultSet.getInt("quantity");

                // Load the Category and Artist objects
                Category category = loadCategoryById(categoryId);
                Artist artist = loadArtistById(artistId); // Load the Artist object using the artistId

                CD cd = new CD(cdId, title, artist, releaseYear, numTracks, category, quantity);
                cds.add(cd);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar CDs da base de dados: " + e.getMessage());
        }

        return cds;
    }

    private Artist loadArtistById(int artistId) {
        Artist artist = null;

        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.Artist WHERE id = ?")) {

            statement.setInt(1, artistId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                // Additional columns related to the Artist entity can be retrieved here

                // Create the Artist object
                artist = new Artist(artistId, name);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar artista do banco de dados: " + e.getMessage());
        }

        return artist;
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

    public void updateCDQuantityDecrease(int cdId) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("UPDATE dbo.Product SET quantity = quantity - 1 WHERE id = ?")) {
            statement.setInt(1, cdId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao atualizar a quantidade do CD.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar a quantidade do CD: " + e.getMessage());
        }
    }


    public void updateCDQuantityIncrease(int productId) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("UPDATE dbo.Product SET quantity = quantity + 1 WHERE id = ?")) {

            statement.setInt(1, productId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao atualizar a quantidade do CD.");
            } else {
                System.out.println("Quantidade do CD atualizada com sucesso.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar a quantidade do CD: " + e.getMessage());
        }
    }
    public boolean deleteCD(int cdId) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM dbo.CD WHERE id = ?")) {

            statement.setInt(1, cdId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao remover CD da base de dados: " + e.getMessage());
            return false;
        }
    }


}
