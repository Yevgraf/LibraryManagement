package Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.AgeRange;
import Model.Publisher;

public class PublisherData {

    public static void save(List<Publisher> publisherList) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " + "Publisher" + " (name, address) VALUES (?, ?)")) {

            for (Publisher publisher : publisherList) {
                if (!isPublisherExists(connection, publisher)) {
                    statement.setString(1, publisher.getName());
                    statement.setString(2, publisher.getAddress());
                    statement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao guardar editores na base de dados: " + e.getMessage());
        }
    }

    public static List<Publisher> load() {
        List<Publisher> publisherList = new ArrayList<>();
        try (Connection connection = DBconn.getConn();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + "Publisher")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                Publisher publisher = new Publisher(name, address);
                publisher.setId(id);
                publisherList.add(publisher);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar editores da base de dados: " + e.getMessage());
        }
        return publisherList;
    }

    private static boolean isPublisherExists(Connection connection, Publisher publisher) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + "Publisher" + " WHERE name = ? AND address = ?");
        statement.setString(1, publisher.getName());
        statement.setString(2, publisher.getAddress());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
        return false;
    }

    public static Publisher findByName(String name) {
        List<Publisher> publisherList = load();
        return publisherList.stream()
                .filter(publisher -> publisher.getName().equals(name))
                .findFirst()
                .orElse(null);
    }


}
