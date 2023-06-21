package View;

import Controller.BookController;
import Model.Author;
import Model.Book;

import java.util.List;
import java.util.Scanner;

/**
 * A classe SearchBooksView representa a interface de busca de livros.
 */
public class SearchBooksView {
    private final BookController bookController;
    private final Scanner scanner;

    /**
     * Construtor da classe SearchBooksView.
     * @param bookController O controlador de livros.
     * @param scanner O scanner para leitura de entrada do usuário.
     */
    public SearchBooksView(BookController bookController, Scanner scanner) {
        this.bookController = bookController;
        this.scanner = scanner;
    }

    /**
     * Inicia a interface de busca de livros.
     * Exibe as opções disponíveis e processa a escolha do usuário.
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
            scanner.nextLine(); // consome o caractere de nova linha

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
     * Realiza a busca de um livro por ID ou nome.
     * Solicita ao usuário um termo de busca e exibe o resultado da busca na tela.
     */
    private void searchBookByIdOrName() {
        System.out.println("Digite o título ou ISBN do livro para procurar:");
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
     * Realiza a busca de livros por categoria.
     * Exibe as categorias disponíveis e exibe o resultado da busca na tela.
     */
    private void searchBooksByCategory() {
        System.out.println("Categorias:");

        List<Book> result = bookController.searchBooksByCategory();

        if (result.isEmpty()) {
            System.out.println("Nenhum livro encontrado nesta categoria.");
        }
    }

    /**
     * Realiza a busca de livros por autor.
     * Exibe os autores disponíveis e exibe o resultado da busca na tela.
     */
    private void searchBooksByAuthor() {
        System.out.println("Autores:");

        List<Book> result = bookController.searchBooksByAuthor();

        if (result.isEmpty()) {
            System.out.println("Nenhum livro encontrado para este autor.");
        }
    }
}
