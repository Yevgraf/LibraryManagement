package Data;

import Model.Category;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryData {

    private static final String FILENAME = "categories.ser";

    public static void save(List<Category> categories) {
        try {
            FileOutputStream fos = new FileOutputStream(FILENAME);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(categories);
            oos.close();
            System.out.println("Categories saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving categories to file: " + e.getMessage());
        }
    }

    public static List<Category> load() {
        List<Category> categories = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            categories = (List<Category>) ois.readObject();
            ois.close();
            System.out.println("Categories loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No categories saved to file.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading categories from file: " + e.getMessage());
        }
        return categories;
    }
}
