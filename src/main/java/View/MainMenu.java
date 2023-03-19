package View;

import Controller.CardController;
import Controller.MemberController;
import Data.CardData;
import Data.MemberData;
import Model.Member;

import java.util.Scanner;

public class MainMenu {
    private Scanner scanner;
    private AuthorMenu authorMenu;
    private LibrarianMenu librarianMenu;
    private MemberMenu memberMenu;
    private CardController cardController;

    public MainMenu() {
        scanner = new Scanner(System.in);
        MemberData memberData = new MemberData();
        CardData cardData = new CardData();
        CardController cardController = new CardController(cardData);

        authorMenu = new AuthorMenu(this);
        librarianMenu = new LibrarianMenu(this);
        memberMenu = new MemberMenu(this, cardController);
    }



    public void displayMenu() {
        int option = 0;
        do {
            System.out.println("Bem-vindo à biblioteca!");
            System.out.println("Selecione uma opção:");
            System.out.println("1 - Gestão de autores");
            System.out.println("2 - Gestão de bibliotecários");
            System.out.println("3 - Gestão de membros");
            System.out.println("4 - Gestão de editoras");
            System.out.println("5 - Gestão de categorias");
            System.out.println("6 - Reserva de livros");
            System.out.println("0 - Sair");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    authorMenu.start();
                    break;
                case 2:
                    librarianMenu.start();
                    break;
                case 3:
                    memberMenu.start();
                    break;
                case 4:
                    // chamar gestao de editoras
                    break;
                case 5:
                    // chamar gestao categorias
                    break;
                case 6:
                    // chamar gestao reserva
                    break;
                case 0:
                    System.out.println("Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }

        } while (option != 0);
    }
}
