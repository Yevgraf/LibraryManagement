package Data;

import Model.AgeRange;
import Model.Category;
import View.AgeRangeMenu;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AgeRangeData {
    private static final String FILENAME = "FaixaEtaria.ser";

    public static void save(List<AgeRange> ageRanges) {
        try {
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            File file = new File(FILENAME);

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Ficheiro de faixas etárias criado.");
            }

            fos = new FileOutputStream(file);
            out = new ObjectOutputStream(fos);
            out.writeObject(ageRanges);
            System.out.println("Faixas etárias guardadas no ficheiro.");
            out.close();
        } catch (IOException e) {
            System.err.println("Erro ao guardar faixas etárias no ficheiro: " + e.getMessage());
        }
    }

    public static List<AgeRange> load() {
        List<AgeRange> ageRanges = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILENAME))) {
            ageRanges = (List<AgeRange>) in.readObject();
            System.out.println("Faixas etárias carregadas do ficheiro.");
        } catch (FileNotFoundException e) {
            System.out.println("Não foram encontradas faixas etárias guardadas.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar faixas etárias do ficheiro: " + e.getMessage());
        }
        return ageRanges;
    }

    public AgeRange findByName(String name) {
        List<AgeRange> ageRangeList = load();
        for (AgeRange ageRange : ageRangeList) {
            if (ageRange.getDescription().equals(name)) {
                return ageRange;
            }
        }
        return null;
    }

}
