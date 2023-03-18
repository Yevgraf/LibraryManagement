package View;

import java.util.InputMismatchException;
import java.util.Scanner;

import Model.Book;

public class CreateBookView {
    private Scanner scanner;

    public CreateBookView(Scanner scanner) {
        this.scanner = scanner;
    }

    public Book show() {
        System.out.println("Add a book");
        System.out.print("Title: ");
        String title = scanner.nextLine();
    
        System.out.print("Author: ");
        String author = scanner.nextLine();
    
        int publicationYear;
        while (true) {
            try {
                System.out.print("Publication year (YYYY): ");
                publicationYear = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid year in the format YYYY.");
                scanner.nextLine();
            }
        }
    
        return new Book(title, author, publicationYear);
    }
    
    
}
