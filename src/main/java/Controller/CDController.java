package Controller;

import Data.CDData;
import Data.CategoryData;
import Model.Artist;
import Model.CD;
import Model.Category;

import java.util.List;

public class CDController {
    private CDData cdData;
    private CategoryData categoryData;

    public CDController(CDData cdData, CategoryData categoryData) {
        this.cdData = cdData;
        this.categoryData = categoryData;
    }

    public void createCD(String title, Artist artist, int releaseYear, int numTracks, Category category, int quantity) {
        // Validations
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Título do CD inválido.");
        }
        if (artist == null) {
            throw new IllegalArgumentException("Artista do CD inválido.");
        }
        if (releaseYear < 0) {
            throw new IllegalArgumentException("Ano de lançamento do CD inválido.");
        }
        if (numTracks < 0) {
            throw new IllegalArgumentException("Número de faixas do CD inválido.");
        }

        List<CD> cdList = cdData.load();
        // Assuming that the title and artist combination must be unique for CDs
        CD cdByTitleAndArtist = cdList.stream()
                .filter(cd -> cd.getTitle().equalsIgnoreCase(title) && cd.getArtist().equals(artist))
                .findFirst()
                .orElse(null);
        if (cdByTitleAndArtist != null) {
            throw new IllegalArgumentException("Já existe um CD com o mesmo título e artista.");
        }

        CD cd = new CD(title, artist, releaseYear, numTracks, category, quantity);
        cdData.save(cd);

        System.out.println("CD criado com sucesso.");
    }


    public List<Category> listCategories() {
        return categoryData.listCategories();
    }
}
