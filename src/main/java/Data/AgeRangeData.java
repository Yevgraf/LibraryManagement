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

    /**
     * Saves the given list of age ranges to the database.
     *
     * @param ageRanges The list of age ranges to be saved.
     */
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
    /**
     * Checks if an age range with the specified description already exists in the database.
     *
     * @param connection  The database connection.
     * @param description The description of the age range.
     * @return true if the age range exists, false otherwise.
     * @throws SQLException if a database access error occurs.
     */

    private static boolean isAgeRangeExists(Connection connection, String description) throws SQLException {
        String query = "SELECT COUNT(*) FROM AgeRange WHERE description = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, description);

        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);

        return count > 0;
    }

    /**
     * Loads all age ranges from the database.
     *
     * @return A list of age ranges loaded from the database.
     */
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

    /**
     * Finds an age range by its name.
     *
     * @param name The name of the age range to find.
     * @return The age range with the specified name, or null if not found.
     */
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
