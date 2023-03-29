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
        List<AgeRange> ageRangeList = listAgeRanges();
        AgeRange ageRange = new AgeRange(description);
        ageRangeList.add(ageRange);
        ageRangeData.save(ageRangeList);
        System.out.println("Faixa etária criada com sucesso.");
    }

    public List<AgeRange> listAgeRanges() {
        return ageRangeData.load();
    }
}
