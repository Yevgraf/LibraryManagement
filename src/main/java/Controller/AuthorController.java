package Controller;

import Model.Author;
import Data.AuthorData;
import Model.Librarian;

import java.time.LocalDate;
import java.util.List;

public class AuthorController {
    private AuthorData authorData;

    public AuthorController(AuthorData authorData) {
        this.authorData = authorData;
    }

    public void createAuthor(String name, String address, LocalDate birthDate) {
        List<Author> authorList = authorData.load();
        Author author = new Author(name, address, birthDate);
        authorList.add(author);
        AuthorData.save(authorList);

    }


    public List<Author> getAllAuthors() {
        return authorData.load();
    }
}
