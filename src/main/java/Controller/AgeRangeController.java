package Controller;

import java.util.List;

import Data.AgeRangeData;
import Model.AgeRange;

public class AgeRangeController {
    private AgeRangeData ageRangeData;

    public AgeRangeController(AgeRangeData ageRangeData) {
        this.ageRangeData = ageRangeData;
    }

    public void createAgeRange(String description) {
         if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode ser vazia.");
        }
        if (description.length() < 3 || description.length() > 50) {
            throw new IllegalArgumentException("A descrição deve ter entre 3 e 50 caracteres.");
        }
        List<AgeRange> ageRangeList = listAgeRanges();
        AgeRange ageRange = new AgeRange(description);
        ageRangeList.add(ageRange);
        ageRangeData.save(ageRangeList);
        System.out.println("Faixa etária criada com sucesso.");
    }

    public List<AgeRange> listAgeRanges() {
        return ageRangeData.load();
    }

    public AgeRange findByDescription(String description) {
        List<AgeRange> ageRangeList = listAgeRanges();
        for (AgeRange ageRange : ageRangeList) {
            if (ageRange.getDescription().equalsIgnoreCase(description)) {
                return ageRange;
            }
        }
        return null;
    }


}
