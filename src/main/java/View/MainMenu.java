package View;

import Controller.*;
import Data.*;
import Model.*;

import java.util.Scanner;

public class MainMenu {
    private Scanner scanner;
    private AuthorMenu authorMenu;
    private LibrarianMenu librarianMenu;
    private MemberMenu memberMenu;
    private CardController cardController;
    private PublisherMenu publisherMenu;
    private CategoryMenu categoryMenu;
    private AgeRangeMenu ageRangeMenu;
    private BookMenu bookMenu;
    private ReservationMenu reservationMenu;
    private CDMenu cdMenu;
    private CreateArtistView createArtistView;

    public MainMenu() {
        scanner = new Scanner(System.in);
        MemberData memberData = new MemberData();
        CardData cardData = new CardData();
        BookData bookData = new BookData();
        AuthorData authorData = new AuthorData();
        AgeRangeData ageRangeData = new AgeRangeData();
        CategoryData categoryData = new CategoryData();
        PublisherData publisherData = new PublisherData();
        CDData cdData = new CDData();
        CardController cardController = new CardController(cardData);
        ReservationData reservationData = new ReservationData();
        ReservationController reservationController = new ReservationController(reservationData, memberData, bookData, cdData); // Pass CDData instance
        MemberController memberController = new MemberController(memberData, scanner);
        BookController bookController = new BookController(bookData, authorData, ageRangeData, categoryData, publisherData, scanner);

        ArtistController artistController = new ArtistController();
        createArtistView = new CreateArtistView(artistController);
        authorMenu = new AuthorMenu(this);
        librarianMenu = new LibrarianMenu(this);
        memberMenu = new MemberMenu(this, cardController);
        publisherMenu = new PublisherMenu(this);
        categoryMenu = new CategoryMenu(this);
        ageRangeMenu = new AgeRangeMenu(this);
        bookMenu = new BookMenu(this);
        reservationMenu = new ReservationMenu(this, memberController, bookController, reservationController);
        cdMenu = new CDMenu(this, new CDController(cdData, categoryData), scanner); // Initialize CDMenu with CDController and scanner
    }


    public void displayMenu() {
        System.out.println("===================================");
        System.out.println("| Bem-vindo à biblioteca!        |");
        System.out.println("| Selecione uma opção:           |");
        System.out.println("===================================");
        System.out.println("| 1 - Gestão de reservas         |");
        System.out.println("| 2 - Gestão de autores          |");
        System.out.println("| 3 - Gestão de artistas         |");
        System.out.println("| 4 - Gestão de bibliotecários   |");
        System.out.println("| 5 - Gestão de membros          |");
        System.out.println("| 6 - Gestão de editoras         |");
        System.out.println("| 7 - Gestão de categorias       |");
        System.out.println("| 8 - Gestão de faixa etária     |");
        System.out.println("| 9 - Gestão de livros           |");
        System.out.println("| 10 - Gestão de CDs             |"); // Add CD menu option
        System.out.println("| 0 - Sair                       |");
        System.out.println("===================================");

        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                reservationMenu.start();
                break;
            case 2:
                authorMenu.start();
                break;
            case 3:
                createArtistView.start();

            case 4:
                librarianMenu.start();
                break;
            case 5:
                memberMenu.start();
                break;
            case 6:
                publisherMenu.start();
                break;
            case 7:
                categoryMenu.start();
                break;
            case 8:
                ageRangeMenu.start();
                break;
            case 9:
                bookMenu.displayMenu();
                break;
            case 10:
                cdMenu.displayMenu(); // Call displayMenu() from CDMenu
                break;
            case 0:
                System.out.println("Até logo!");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
                break;
        }

        displayMenu();
    }
}
