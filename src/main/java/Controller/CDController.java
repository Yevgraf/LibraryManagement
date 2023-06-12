package Controller;

import Data.CDData;
import Data.CategoryData;
import Model.CD;
import Model.Category;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class CDController {
    private CDData cdData;
    private CategoryData categoryData;
    private Scanner scanner;

    public CDController(CDData cdData, CategoryData categoryData) {
        this.cdData = cdData;
        this.categoryData = categoryData;
        this.scanner = scanner;
    }

    public void createCD(String title, String artist, int releaseYear, int numTracks, String categoryName, int quantity) {
        // Validations
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Título do CD inválido.");
        }
        if (artist == null || artist.trim().isEmpty()) {
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
                .filter(cd -> cd.getTitle().equalsIgnoreCase(title) && cd.getArtist().equalsIgnoreCase(artist))
                .findFirst()
                .orElse(null);
        if (cdByTitleAndArtist != null) {
            throw new IllegalArgumentException("Já existe um CD com o mesmo título e artista.");
        }

        Category category = categoryData.findByName(categoryName);
        if (category == null) {
            throw new IllegalArgumentException("Categoria não encontrada.");
        }

        CD cd = new CD(title, artist, releaseYear, numTracks, category, quantity);
        cdData.save(cd);

        System.out.println("CD criado com sucesso.");
    }

    public List<Category> listCategories() {
        return categoryData.listCategories();
    }


    public List<CD> listCDs() {
        return cdData.load();
    }

    public boolean removeCD(CD cd) {
        return cdData.deleteCD(cd.getId());
    }

    public CD findCDByIdOrTitle(String searchTerm) {
        List<CD> cds = listCDs();

        CD cdByTitle = null;
        for (CD cd : cds) {
            if (cd.getTitle().equalsIgnoreCase(searchTerm)) {
                cdByTitle = cd;
                break;
            }
        }

        if (cdByTitle != null) {
            return cdByTitle;
        }

        List<CD> cdsByTitle = cds.stream()
                .filter(cd -> cd.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        if (cdsByTitle.isEmpty()) {
            return null;
        }

        if (cdsByTitle.size() == 1) {
            return cdsByTitle.get(0);
        }

        System.out.println("Há mais de um CD com esse título, selecione o ID correto:");
        for (CD cd : cdsByTitle) {
            System.out.println("ID: " + cd.getId() + ", Título: " + cd.getTitle());
        }

        int selectedId = scanner.nextInt();
        scanner.nextLine();

        return cdsByTitle.stream()
                .filter(cd -> cd.getId() == selectedId)
                .findFirst()
                .orElse(null);
    }

    public List<CD> searchCDsByCategory() {
        List<Category> categories = categoryData.listCategories();

        if (categories.isEmpty()) {
            System.out.println("Não há categorias cadastradas.");
            return Collections.emptyList();
        }

        System.out.println("Selecione uma categoria:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }

        int selectedCategoryIndex = scanner.nextInt();
        scanner.nextLine();

        if (selectedCategoryIndex < 1 || selectedCategoryIndex > categories.size()) {
            System.out.println("Opção inválida.");
            return Collections.emptyList();
        }

        Category selectedCategory = categories.get(selectedCategoryIndex - 1);

        List<CD> cds = searchCDsByCategoryWithName(selectedCategory);

        if (cds.isEmpty()) {
            System.out.println("Não há CDs cadastrados nesta categoria.");
            return Collections.emptyList();
        }

        System.out.println("CDs da categoria " + selectedCategory.getCategoryName() + ":");
        for (CD cd : cds) {
            System.out.println(cd);
        }

        return cds;
    }

    private List<CD> searchCDsByCategoryWithName(Category category) {
        List<CD> allCDs = cdData.load();

        return allCDs.stream()
                .filter(cd -> cd.getCategory().getCategoryName() != null && cd.getCategory().getCategoryName().equals(category.getCategoryName()))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<CD> searchCDsByArtist() {
        List<CD> allCDs = cdData.load();

        if (allCDs.isEmpty()) {
            System.out.println("Não há CDs cadastrados.");
            return Collections.emptyList();
        }

        System.out.println("Selecione um artista:");
        Set<String> artists = allCDs.stream()
                .map(CD::getArtist)
                .collect(Collectors.toSet());
        int index = 1;
        for (String artist : artists) {
            System.out.println(index + ". " + artist);
            index++;
        }

        int selectedArtistIndex = scanner.nextInt();
        scanner.nextLine();

        if (selectedArtistIndex < 1 || selectedArtistIndex > artists.size()) {
            System.out.println("Opção inválida.");
            return Collections.emptyList();
        }

        String selectedArtist = (String) artists.toArray()[selectedArtistIndex - 1];

        List<CD> cds = searchCDsByArtistWithName(selectedArtist);

        if (cds.isEmpty()) {
            System.out.println("Não há CDs cadastrados para este artista.");
            return Collections.emptyList();
        }

        System.out.println("CDs do artista " + selectedArtist + ":");
        for (CD cd : cds) {
            System.out.println(cd);
        }

        return cds;
    }

    private List<CD> searchCDsByArtistWithName(String artistName) {
        List<CD> allCDs = cdData.load();

        return allCDs.stream()
                .filter(cd -> cd.getArtist() != null && cd.getArtist().equals(artistName))
                .distinct()
                .collect(Collectors.toList());
    }
}