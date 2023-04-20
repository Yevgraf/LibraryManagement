package Data;

import Model.Author;
import Model.Category;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorData {

    private static final String FILENAME = "Author.ser";

    public static void save(List<Author> authorList) {
        try {
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            File file = new File(FILENAME);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Ficheiro de autores criado.");
            }

            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(authorList);
            System.out.println("Autores guardados no ficheiro.");
            out.close();
        } catch (IOException e) {
            System.err.println("Erro ao guardar autores no ficheiro: " + e.getMessage());
        }
    }


    public static List<Author> load() {
        List<Author> authorList = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
            authorList = (List<Author>) in.readObject();
            System.out.println("Autores carregados do ficheiro.");
        } catch (FileNotFoundException e) {
            System.out.println("Não foram encontrados autores guardados.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar autores do ficheiro: " + e.getMessage());
        }
        return authorList;
    }
    public Author findByName(String name) {
        List<Author> authorList = load();
        return authorList.stream()
                .filter(author -> author.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    public List<Author> listAuthors() {
        return load();
    }
}
