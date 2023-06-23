package Data;

import Model.Artist;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArtistData {

    /**
     * Saves the given list of artists to the database.
     *
     * @param artistList The list of artists to be saved.
     */
    public static void save(List<Artist> artistList) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Artist (name) VALUES (?)")) {

            for (Artist artist : artistList) {
                if (!isArtistExists(connection, artist)) {
                    statement.setString(1, artist.getName());
                    statement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao guardar artistas na base de dados: " + e.getMessage());
        }
    }
    /**
     * Loads all artists from the database.
     *
     * @return A list of artists loaded from the database.
     */
    public static List<Artist> load() {
        List<Artist> artistList = new ArrayList<>();
        try (Connection connection = DBconn.getConn();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Artist")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Artist artist = new Artist(id, name);
                artistList.add(artist);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar artistas da base de dados: " + e.getMessage());
        }
        return artistList;
    }

    /**
     * Checks if an artist with the same name already exists in the database.
     *
     * @param connection The database connection.
     * @param artist     The artist to check for existence.
     * @return true if the artist exists, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    private static boolean isArtistExists(Connection connection, Artist artist) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Artist WHERE name = ?");
        statement.setString(1, artist.getName());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
        return false;
    }

    /**
     * Retrieves a list of all artists from the database.
     *
     * @return A list of all artists.
     */
    public List<Artist> listArtists() {
        return load();
    }
}
