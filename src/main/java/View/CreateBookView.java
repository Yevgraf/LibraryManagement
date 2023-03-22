package View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import Model.Book;

public class CreateBookView {

    public static Book createBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter book title:");
        String title = scanner.nextLine();

        System.out.println("Enter book author:");
        String author = scanner.nextLine();

        System.out.println("Enter book genre (Romance, Mystery, Science Fiction, Fantasy, Horror, Historical Fiction, Thriller, Biography, Autobiography, Memoir, Self-Help, Travel, Cookbooks, Poetry, Drama):");
        String genre = "";
        boolean isValidGenre = false;
        while (!isValidGenre) {
            genre = scanner.nextLine();
            isValidGenre = Book.isValidGenre(genre);

            if (!isValidGenre) {
                System.out.println("Invalid genre. Please try again.");
                System.out.println("Enter book genre (Romance, Mystery, Science Fiction, Fantasy, Horror, Historical Fiction, Thriller, Biography, Autobiography, Memoir, Self-Help, Travel, Cookbooks, Poetry, Drama):");
            }
        }

        System.out.println("Enter book publication date (dd/MM/yyyy):");
        String dateString = scanner.nextLine();

        Date publicationDate = null;
        boolean isValidDate = false;
        while (!isValidDate) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                publicationDate = dateFormat.parse(dateString);
                isValidDate = true;
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please try again.");
                System.out.println("Enter book publication date (dd/MM/yyyy):");
                dateString = scanner.nextLine();
            }
        }

        return new Book(title, author, genre, publicationDate);
    }
}