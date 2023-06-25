package View;

import Controller.*;
import Data.*;
import Model.CD;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CDMenu {
    private Scanner scanner;
    private CDController cdController;
    private CreateCDView createCDView;

    private ArtistController artistController;
    private MainMenu mainMenu;
    private DateTimeFormatter dateFormatter;
    private AuthorController authorController;
    private AgeRangeController ageRangeController;
    private CategoryController categoryController;
    private PublisherController publisherController;
    private ReservationController reservationController;

    public CDMenu(MainMenu mainMenu, CDController cdController, Scanner scanner) {
        this.mainMenu = mainMenu;
        this.cdController = cdController;
        this.scanner = scanner;
        this.artistController = new ArtistController();
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        CDData cdData = new CDData();
        BookData bookData = new BookData();
        AuthorData authorData = new AuthorData();
        AgeRangeData ageRangeData = new AgeRangeData();
        CategoryData categoryData = new CategoryData();
        PublisherData publisherData = new PublisherData();
        ReservationData reservationData = new ReservationData();
        MemberData memberData = new MemberData();
        authorController = new AuthorController(authorData);
        ageRangeController = new AgeRangeController(ageRangeData);
        categoryController = new CategoryController(categoryData);
        publisherController = new PublisherController(publisherData);
        this.cdController = new  CDController(cdData, categoryData);
        createCDView = new CreateCDView(cdController, artistController, scanner);
        reservationController = new ReservationController(reservationData, memberData, bookData, cdData);
    }

    /**
     * Displays the CD menu and handles user input.
     */
    public void displayMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("==== CDs ====");
            System.out.println("1. Adicionar CD");
            System.out.println("2. Listar CDs");
            System.out.println("3. Remover CD");
            System.out.println("4. Procurar CD");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createCDView.createCD();
                    break;
                case 2:
                    listCDs();
                    break;
                case 3:
                    removeCD();
                    break;
                case 4:
                    SearchCDsView searchCDsView = new SearchCDsView(cdController, scanner);
                    searchCDsView.searchCDs();
                    break;
                case 0:
                    mainMenu.displayMenu();
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    private void listCDs() {
        List<CD> cdList = cdController.listCDs();
        if (cdList.isEmpty()) {
            System.out.println("Nenhum CD encontrado.");
        } else {
            System.out.println("==== Lista de CDs ====");
            for (CD cd : cdList) {
                System.out.println(cd.toString());
            }
        }
    }

    public void removeCD() {
        List<CD> cdList = cdController.listCDs();

        if (cdList.isEmpty()) {
            System.out.println("Nenhum CD encontrado na base de dados.");
            return;
        }

        displayCDList(cdList);

        int selectedIndex = getSelectedCDIndex();
        if (selectedIndex >= 1 && selectedIndex <= cdList.size()) {
            CD selectedCD = cdList.get(selectedIndex - 1);
            boolean removed = cdController.removeCD(selectedCD);
            if (removed) {
                showSuccessMessage(selectedCD.getTitle());
            } else {
                showFailureMessage(selectedCD.getTitle());
            }
        } else {
            System.out.println("Índice inválido. Operação cancelada.");
        }
    }

    public void displayCDList(List<CD> cdList) {
        if (cdList.isEmpty()) {
            System.out.println("Nenhum CD encontrado na base de dados.");
            return;
        }

        System.out.println("Lista de CDs:");
        for (int i = 0; i < cdList.size(); i++) {
            CD cd = cdList.get(i);
            System.out.println((i + 1) + ". " + cd.getTitle());
        }
    }

    public int getSelectedCDIndex() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insira o índice do CD: ");

        int selectedIndex = -1;
        while (selectedIndex < 1) {
            if (scanner.hasNextInt()) {
                selectedIndex = scanner.nextInt();
                if (selectedIndex < 1) {
                    System.out.println("Índice inválido. Insira um valor maior que zero.");
                }
            } else {
                System.out.println("Entrada inválida. Insira um número inteiro correspondente ao índice do CD.");
                scanner.next();
            }
        }
        return selectedIndex;
    }

    public void showSuccessMessage(String cdTitle) {
        System.out.println("O CD \"" + cdTitle + "\" foi removido com sucesso.");
    }

    public void showFailureMessage(String cdTitle) {
        System.out.println("Falha ao remover o CD \"" + cdTitle + "\".");
    }
}