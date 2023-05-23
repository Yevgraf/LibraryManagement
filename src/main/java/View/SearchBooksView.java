package View;

import Controller.BookController;
import Model.Author;
import Model.Book;

import java.util.List;
import java.util.Scanner;

public class SearchBooksView {
    private final BookController bookController;
    private final Scanner scanner;

    public SearchBooksView(BookController bookController, Scanner scanner) {
        this.bookController = bookController;
        this.scanner = scanner;
    }

    public void searchBooks() {
        int option = -1;
        while (option != 0) {
            System.out.println("Escolha uma opção de busca:");
            System.out.println("1 - Procurar por ID ou nome");
            System.out.println("2 - Procurar por categoria");
            System.out.println("3 - Procurar por autor");
            System.out.println("0 - Voltar");

            option = scanner.nextInt();
            scanner.nextLine(); // consume newline character

            switch (option) {
                case 1:
                    searchBookByIdOrName();
                    break;
                case 2:
                    searchBooksByCategory();
                    break;
                case 3:
                    searchBooksByAuthor();
                    break;
                case 0:
                    System.out.println("Voltar ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente");
                    break;
            }
        }
    }

    private void searchBookByIdOrName() {
        System.out.println("Digite o titulo ou ISBN do livro para procurar:");
        String searchTerm = scanner.nextLine();

        Book result = bookController.findBookByIdOrName(searchTerm);

        if (result == null) {
            System.out.println("Livro não encontrado");
        } else {
            System.out.println("Livro encontrado:");
            System.out.println(result);
        }
    }

    private void searchBooksByCategory() {
        System.out.println("Categorias:");

        List<Book> result = bookController.searchBooksByCategory();

        if (result.isEmpty()) {
            System.out.println("Nenhum livro encontrado nesta categoria.");

        }
    }

    private void searchBooksByAuthor() {
        System.out.println("Autores:");

        List<Book> result = bookController.searchBooksByAuthor();

        if (result.isEmpty()) {
            System.out.println("Nenhum livro encontrado para este autor.");

        }
    }
}


