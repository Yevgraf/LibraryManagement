package Data;

import Model.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookData {
    private static final String FILENAME = "Book.ser";

    public static void save(List<Book> bookList) {
        try {
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            File file = new File(FILENAME);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Ficheiro de livros criado.");
            }

            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(bookList);
            System.out.println("Livros guardados no ficheiro.");
            out.close();
        } catch (IOException e) {
            System.err.println("Erro ao guardar livros no ficheiro: " + e.getMessage());
        }
    }

    public static List<Book> load() {
        List<Book> bookList = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
            bookList = (List<Book>) in.readObject();
            System.out.println("Livros carregados do ficheiro.");
        } catch (FileNotFoundException e) {
            System.out.println("Não foram encontrados livros guardados.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar livros do ficheiro: " + e.getMessage());
        }
        return bookList;
    }


}