package Data;

import Model.Category;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryData {

    public static void save(List<Category> categoryList) {
        try (Connection connection = DBconn.getConn();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " + "Category" + " (categoryName) VALUES (?)")) {

            for (Category category : categoryList) {
                if (!isCategoryExists(connection, category)) {
                    statement.setString(1, category.getCategoryName());
                    statement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao guardar categorias na base de dados: " + e.getMessage());
        }
    }

    public static List<Category> load() {
        List<Category> categoryList = new ArrayList<>();
        try (Connection connection = DBconn.getConn();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + "Category")) {

            while (resultSet.next()) {
                int categoryId = resultSet.getInt("categoryId");
                String categoryName = resultSet.getString("categoryName");
                Category category = new Category(categoryName);
                category.setCategoryId(categoryId);
                categoryList.add(category);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar categorias da base de dados: " + e.getMessage());
        }
        return categoryList;
    }

    private static boolean isCategoryExists(Connection connection, Category category) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + "Category" + " WHERE categoryName = ?");
        statement.setString(1, category.getCategoryName());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
        return false;
    }

    public static Category findByName(String name) {
        List<Category> categoryList = load();
        return categoryList.stream()
                .filter(category -> category.getCategoryName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<Category> listCategories() {
        return load();
    }

}

