package Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Model.Member;

public class MemberData {
    private static final String FILENAME = "Member.ser";

    public static void save(List<Member> memberList) {
        try {
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            File file = new File(FILENAME);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Member file created.");
            }

            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(memberList);
            System.out.println("Members saved to file.");
            out.close();
        } catch (IOException e) {
            System.err.println("Error saving members to file: " + e.getMessage());
        }
    }

    public static List<Member> load() {
        List<Member> memberList = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
            memberList = (List<Member>) in.readObject();
            System.out.println("Members loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved members found.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading members from file: " + e.getMessage());
        }
        return memberList;
    }
}
