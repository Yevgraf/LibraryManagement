package View;

import Controller.LibrarianController;
import Data.LibrarianData;
import Data.MemberData;
import Model.Librarian;
import Model.Member;
import Model.User;

import java.util.List;
import java.util.Scanner;

public class LoginView {
    private final LibrarianController librarianController;

    public LoginView(LibrarianController librarianController) {
        this.librarianController = librarianController;
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;
        Member member = null; // Initialize the member variable

        while (!success) {
            System.out.println("Por favor, insira seu email: ");
            String email = scanner.nextLine();

            System.out.println("Por favor, insira sua senha: ");
            String password = scanner.nextLine();

            success = librarianController.login(email, password);

            if (success) {
                User user = getUserByEmail(email);

                if (user instanceof Librarian) {
                    System.out.println("Login bem sucedido como bibliotecário!");
                    MainMenu mainMenu = new MainMenu();
                    mainMenu.displayMenu();
                } else if (user instanceof Member) {
                    System.out.println("Login successful as a member!");
                    int userId = user.getId();
                    int memberId = MemberData.getMemberIdByUserId(userId);
                    member = (Member) user; // Cast User to Member
                    member.setId(memberId); // Set the memberId in the Member object
                    MemberMainMenu memberMainMenu = new MemberMainMenu(member);
                    memberMainMenu.displayMenu();
                }
            } else {
                System.out.println("Email ou senha incorretos. Por favor, tente novamente.");
            }
        }
    }


    private User getUserByEmail(String email) {
        List<User> userList = LibrarianData.loadUsers();

        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        return null;
    }

}