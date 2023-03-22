package Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Book {
    private String title;
    private String author;
    private String genre;
    private Date publicationDate;

    public Book(String title, String author, String genre, Date publicationDate) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public static boolean isValidGenre(String genre) {
        String[] validGenres = { "Romance", "Mystery", "Science Fiction", "Fantasy", "Horror", "Historical Fiction",
                "Thriller", "Biography", "Autobiography", "Memoir", "Self-Help", "Travel",
                "Cookbooks", "Poetry", "Drama" };
        for (String validGenre : validGenres) {
            if (validGenre.equalsIgnoreCase(genre)) {
                return true;
            }
        }
        return false;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public String toString() {
        String firstLetter = this.genre.substring(0, 1).toUpperCase();
        String restOfGenre = this.genre.substring(1).toLowerCase();
        String formattedGenre = firstLetter + restOfGenre;
        return "Title: " + this.title +
                ", Author: " + this.author +
                ", Genre: " + formattedGenre +
                ", Publication Date: " + dateFormat.format(this.publicationDate);
    }
}
