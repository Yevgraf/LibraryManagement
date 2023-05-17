package Data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                System.out.println("Ficheiro de membro criado.");
            }

            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(memberList);
            System.out.println("Membros foram gravados no ficheiro.");
            out.close();
        } catch (IOException e) {
            System.err.println("Erro ao gravar membros no ficheiro: " + e.getMessage());
        }
    }

    public static List<Member> load() {
        List<Member> memberList = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
            memberList = (List<Member>) in.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("Não foram encontrados membros gravados");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar membros do ficheiro: " + e.getMessage());
        }
        return memberList;
    }

    public Optional<Member> findMemberByName(String memberName) {
        List<Member> memberList = load();
        return memberList.stream()
                .filter(m -> m.getName().equalsIgnoreCase(memberName))
                .findFirst();
    }

}
