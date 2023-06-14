package Data;

import Model.Artist;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArtistData {

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

    public Artist findByName(String name) {
        List<Artist> artistList = load();
        return artistList.stream()
                .filter(artist -> artist.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<Artist> listArtists() {
        return load();
    }
}
