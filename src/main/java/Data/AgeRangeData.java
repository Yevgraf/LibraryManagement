package Data;

import Model.AgeRange;
import Model.Category;
import View.AgeRangeMenu;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AgeRangeData {

    public static void save(List<AgeRange> ageRanges) {
        try (Connection connection = DBconn.getConn()) {
            String query = "INSERT INTO AgeRange (description) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);

            for (AgeRange ageRange : ageRanges) {

                if (!isAgeRangeExists(connection, ageRange.getDescription())) {
                    statement.setString(1, ageRange.getDescription());
                    statement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao guardar faixas etárias na base de dados: " + e.getMessage());
        }
    }

    private static boolean isAgeRangeExists(Connection connection, String description) throws SQLException {
        String query = "SELECT COUNT(*) FROM AgeRange WHERE description = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, description);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);

        return count > 0;
    }


    public static List<AgeRange> load() {
        List<AgeRange> ageRanges = new ArrayList<>();
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("SELECT id, description FROM " + "AgeRange");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                AgeRange ageRange = new AgeRange(id, description);
                ageRanges.add(ageRange);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar faixas etárias do banco de dados: " + e.getMessage());
        }
        return ageRanges;
    }

    public AgeRange findByName(String name) {
        List<AgeRange> ageRangeList = load();
        for (AgeRange ageRange : ageRangeList) {
            if (ageRange.getDescription().equals(name)) {
                return ageRange;
            }
        }
        return null;
    }

}
