package Data;

import Model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserData {

    private static final String FILENAME = "users.ser";

    public static void save(List<User> userList) {
        try {
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            File file = new File(FILENAME);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Ficheiro de utilizadores criado.");
            }

            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(userList);
            System.out.println("Utilizadores guardados no ficheiro.");
            out.close();
        } catch (IOException e) {
            System.err.println("Erro ao guardar utilizadores no ficheiro: " + e.getMessage());
        }
    }


    public static List<User> load() {
        List<User> userList = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
            userList = (List<User>) in.readObject();
            System.out.println("Utilizadores carregados do ficheiro.");
        } catch (FileNotFoundException e) {
            System.out.println("Não foram encontrados utilizadores guardados.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar utilizadores do ficheiro: " + e.getMessage());
        }
        return userList;
    }
}