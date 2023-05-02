package Data;

import Model.Book;
import Model.Card;
import Model.Member;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CardData {

    private static final String FILENAME = "Cards.ser";

    public static void save(List<Card> cardList) {
        try {
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            File file = new File(FILENAME);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Ficheiro de cartões criado.");
            }

            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(cardList);
            System.out.println("Cartões guardados no ficheiro.");
            out.close();
        } catch (IOException e) {
            System.err.println("Erro ao guardar cartões no ficheiro: " + e.getMessage());
        }
    }


    public static List<Card> load() {
        List<Card> cardList = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
            cardList = (List<Card>) in.readObject();
            Card.resetIdCounter(cardList);
        } catch (FileNotFoundException e) {
            System.out.println("Não foram encontrados cartões guardados.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar cartões do ficheiro: " + e.getMessage());
        }
        return cardList;
    }


}
