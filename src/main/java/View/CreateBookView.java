package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import Controller.*;
import Model.AgeRange;
import Model.Author;
import Model.Category;
import Model.Publisher;

public class CreateBookView {
    private BookController bookController;
    private AuthorController authorController;
    private AgeRangeController ageRangeController;
    private CategoryController categoryController;
    private PublisherController publisherController;
    private Scanner scanner;

    /**
     * Cria uma instância da classe CreateBookView.
     *
     * @param bookController      O controlador de livros.
     * @param authorController    O controlador de autores.
     * @param ageRangeController  O controlador de faixas etárias.
     * @param categoryController  O controlador de categorias.
     * @param publisherController O controlador de editoras.
     * @param scanner             O objeto Scanner para entrada do usuário.
     */
    public CreateBookView(BookController bookController, AuthorController authorController, AgeRangeController ageRangeController, CategoryController categoryController, PublisherController publisherController, Scanner scanner) {
        this.bookController = bookController;
        this.authorController = authorController;
        this.ageRangeController = ageRangeController;
        this.categoryController = categoryController;
        this.publisherController = publisherController;
        this.scanner = scanner;
    }

    /**
     * Permite a criação de um novo livro.
     * Solicita ao usuário todas as informações necessárias para criar um livro.
     * Chama o controlador para criar o livro correspondente.
     */
    public void createBook() {
        System.out.println("Criação de novo livro:");

        String title = getTitle();
        String subtitle = getSubtitle();
        Author author = getAuthor();
        int numPages = getNumPages();
        Category category = getCategory();
        LocalDate publicationDate = getPublicationDate();
        AgeRange ageRange = getAgeRange();
        Publisher publisher = getPublisher();
        String isbn = getIsbn();
        int quantity = getQuantity();

        bookController.createBook(title, subtitle, author.getName(), numPages, category.getCategoryName(), publicationDate, ageRange.getDescription(), publisher.getName(), isbn, quantity);
    }

    /**
     * Obtém a entrada do usuário para o título do livro.
     *
     * @return O título do livro inserido pelo usuário.
     */
    private String getTitle() {
        System.out.print("Título: ");
        return scanner.nextLine();
    }

    /**
     * Obtém a entrada do usuário para o subtítulo do livro.
     *
     * @return O subtítulo do livro inserido pelo usuário.
     */
    private String getSubtitle() {
        System.out.print("Subtítulo: ");
        return scanner.nextLine();
    }

    /**
     * Obtém a seleção do usuário para o autor do livro.
     *
     * @return O autor selecionado pelo usuário.
     */
    public Author getAuthor() {
        List<Author> authorList = authorController.listAuthors();
        System.out.println("Lista de autores:");
        for (int i = 0; i < authorList.size(); i++) {
            System.out.println((i + 1) + ". " + authorList.get(i).getName());
        }
        System.out.print("Digite o número do autor desejado: ");
        int selection = scanner.nextInt();
        scanner.nextLine(); // consome o caractere de nova linha restante
        if (selection < 1 || selection > authorList.size()) {
            System.out.println("Seleção inválida!");
            return null;
        }
        return authorList.get(selection - 1);
    }

    /**
     * Obtém a entrada do usuário para o número de páginas do livro.
     *
     * @return O número de páginas do livro inserido pelo usuário.
     */
    private int getNumPages() {
        System.out.print("Número de páginas: ");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Obtém a entrada do usuário para a quantidade de livros.
     *
     * @return A quantidade de livros inserida pelo usuário.
     */
    private int getQuantity() {
        System.out.print("Número de livros: ");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Obtém a seleção do usuário para a categoria do livro.
     *
     * @return A categoria selecionada pelo usuário.
     */
    public Category getCategory() {
        List<Category> categoryList = categoryController.listCategories();
        System.out.println("Lista de categorias:");
        for (int i = 0; i < categoryList.size(); i++) {
            System.out.println((i + 1) + ". " + categoryList.get(i).getCategoryName());
        }
        System.out.print("Digite o número da categoria desejada: ");
        int selection = scanner.nextInt();
        scanner.nextLine(); // consome o caractere de nova linha restante
        if (selection < 1 || selection > categoryList.size()) {
            System.out.println("Seleção inválida!");
            return null;
        }
        return categoryList.get(selection - 1);
    }

    /**
     * Obtém a entrada do usuário para a data de publicação do livro.
     *
     * @return A data de publicação do livro inserida pelo usuário.
     */
    private LocalDate getPublicationDate() {
        System.out.print("Data de publicação (dd/mm/aaaa): ");
        String publicationDateStr = scanner.nextLine();
        return LocalDate.parse(publicationDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Obtém a seleção do usuário para a faixa etária do livro.
     *
     * @return A faixa etária selecionada pelo usuário.
     */
    public AgeRange getAgeRange() {
        List<AgeRange> ageRangeList = ageRangeController.listAgeRanges();
        System.out.println("Lista de faixas etárias:");
        for (int i = 0; i < ageRangeList.size(); i++) {
            System.out.println((i + 1) + ". " + ageRangeList.get(i).getDescription());
        }
        System.out.print("Digite o número da faixa etária desejada: ");
        int selection = scanner.nextInt();
        scanner.nextLine();
        if (selection < 1 || selection > ageRangeList.size()) {
            System.out.println("Seleção inválida!");
            return null;
        }
        return ageRangeList.get(selection - 1);
    }

    /**
     * Obtém a seleção do usuário para a editora do livro.
     *
     * @return A editora selecionada pelo usuário.
     */
    public Publisher getPublisher() {
        List<Publisher> publisherList = publisherController.listPublishers();
        System.out.println("Lista de editoras:");
        for (int i = 0; i < publisherList.size(); i++) {
            System.out.println((i + 1) + ". " + publisherList.get(i).getName());
        }
        System.out.print("Digite o número da editora desejada: ");
        int selection = scanner.nextInt();
        scanner.nextLine();
        if (selection < 1 || selection > publisherList.size()) {
            System.out.println("Seleção inválida!");
            return null;
        }
        return publisherList.get(selection - 1);
    }

    /**
     * Obtém a entrada do usuário para o ISBN do livro.
     * Valida o ISBN inserido pelo usuário.
     *
     * @return O ISBN do livro inserido pelo usuário.
     */
    private String getIsbn() {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        try {
            validateIsbn(isbn);
        } catch (IllegalArgumentException e) {
            System.out.println("Entrada inválida: " + e.getMessage());
            return getIsbn(); // solicita novamente se a validação falhar
        }

        return isbn;
    }

    /**
     * Valida o ISBN fornecido.
     *
     * @param isbn O ISBN a ser validado.
     * @throws IllegalArgumentException Se o ISBN não tiver 13 dígitos ou não for um número válido.
     */
    private void validateIsbn(String isbn) {
        if (isbn.length() != 13) {
            throw new IllegalArgumentException("O ISBN deve ter 13 dígitos.");
        }

        try {
            Long.parseLong(isbn);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("O ISBN deve ser um número válido.");
        }
    }
}
