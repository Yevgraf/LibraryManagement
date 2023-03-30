package Data;

import Model.Category;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryData {
    private static final String FILENAME = "Category.ser";

    public static void save(List<Category> categoryList) {
        try {
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            File file = new File(FILENAME);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Ficheiro de categorias criado.");
            }

            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(categoryList);
            System.out.println("Categorias guardadas no ficheiro.");
            out.close();
        } catch (IOException e) {
            System.err.println("Erro ao guardar categorias no ficheiro: " + e.getMessage());
        }
    }

    public static List<Category> load() {
        List<Category> categoryList = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
            categoryList = (List<Category>) in.readObject();
            System.out.println("Categorias carregadas do ficheiro.");
        } catch (FileNotFoundException e) {
            System.out.println("Não foram encontradas categorias guardadas.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar categorias do ficheiro: " + e.getMessage());
        }
        return categoryList;
    }

    public static Category findByName(String name) {
        List<Category> categoryList = load();
        return categoryList.stream()
                .filter(category -> category.getCategoryName().equals(name))
                .findFirst()
                .orElse(null);
    }
}

