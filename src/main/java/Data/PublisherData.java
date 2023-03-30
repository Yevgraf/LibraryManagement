package Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Model.AgeRange;
import Model.Publisher;

public class PublisherData {
    private static final String FILENAME = "Publisher.ser";

    public static void save(List<Publisher> publisherList) {
        try {
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            File file = new File(FILENAME);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Publisher file created.");
            }

            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(publisherList);
            System.out.println("Publishers saved to file.");
            out.close();
        } catch (IOException e) {
            System.err.println("Error saving publishers to file: " + e.getMessage());
        }
    }

    public static List<Publisher> load() {
        List<Publisher> publisherList = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
            publisherList = (List<Publisher>) in.readObject();
            System.out.println("Publishers loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved publishers found.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading publishers from file: " + e.getMessage());
        }
        return publisherList;
    }

    public static Publisher findByName(String name) {
        List<Publisher> publisherList = load();
        return publisherList.stream()
                .filter(publisher -> publisher.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

}
