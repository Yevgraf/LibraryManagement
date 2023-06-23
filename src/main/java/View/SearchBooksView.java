package View;

import Controller.BookController;
import Model.Author;
import Model.Book;

import java.util.List;
import java.util.Scanner;

public class SearchBooksView {
    private final BookController bookController;
    private final Scanner scanner;
    /**
     * Constructs a SearchBooksView object with the specified BookController and Scanner.
     *
     * @param bookController The BookController used to interact with book data.
     * @param scanner        The Scanner used to read user input.
     */
    public SearchBooksView(BookController bookController, Scanner scanner) {
        this.bookController = bookController;
        this.scanner = scanner;
    }

    /**
     * Displays the search options and handles the user's choice.
     */
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
    /**
     * Searches a book by ID or name entered by the user.
     * Displays the search result or a message if the book is not found.
     */
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
    /**
     * Searches books by category chosen by the user.
     * Displays the search result or a message if no books are found in the category.
     */
    private void searchBooksByCategory() {
        System.out.println("Categorias:");

        List<Book> result = bookController.searchBooksByCategory();

        if (result.isEmpty()) {
            System.out.println("Nenhum livro encontrado nesta categoria.");

        }
    }

    /**
     * Searches books by author chosen by the user.
     * Displays the search result or a message if no books are found for the author.
     */
    private void searchBooksByAuthor() {
        System.out.println("Autores:");

        List<Book> result = bookController.searchBooksByAuthor();

        if (result.isEmpty()) {
            System.out.println("Nenhum livro encontrado para este autor.");

        }
    }
}


