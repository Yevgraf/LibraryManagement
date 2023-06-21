package View;

import java.util.List;
import java.util.Scanner;

import Controller.CardController;
import Controller.MemberController;
import Data.MemberData;
import Model.Card;

/**
 * A classe MemberMenu representa o menu de opções relacionadas aos membros da biblioteca.
 */
public class MemberMenu {
    private Scanner scanner;
    private MemberController memberController;
    private CardController cardController;
    private CreateMemberView createMemberView;
    private MainMenu mainMenu;

    /**
     * Construtor da classe MemberMenu.
     * Inicializa os controladores, a visualização de criação de membros e o menu principal.
     * @param mainMenu O menu principal da aplicação.
     * @param cardController O controlador de cartões.
     */
    public MemberMenu(MainMenu mainMenu, CardController cardController) {
        this.mainMenu = mainMenu;
        scanner = new Scanner(System.in);
        MemberData memberData = new MemberData();
        memberController = new MemberController(memberData, scanner);
        createMemberView = new CreateMemberView(memberController);
        this.cardController = cardController;
    }

    /**
     * Inicia o menu de membros.
     * Exibe as opções disponíveis e permite ao usuário interagir com elas.
     */
    public void start() {
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Criar membro");
            System.out.println("2. Listar membros");
            System.out.println("3. Listar cartões");
            System.out.println("4. Voltar");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createMemberView.createMember();
                    break;
                case 2:
                    createMemberView.listMembers();
                    break;
                case 3:
                    listCards();
                    break;
                case 4:
                    System.out.println("Voltar...");
                    mainMenu.displayMenu();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    /**
     * Lista os cartões cadastrados.
     */
    private void listCards() {
        List<Card> cards = cardController.getAllCards();
        System.out.println("Lista de cartões:");
        for (Card card : cards) {
            System.out.println(card.toString());
        }
    }
}
