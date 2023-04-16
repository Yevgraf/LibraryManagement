package Controller;

import Model.Author;
import Data.AuthorData;

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
    public Author findByName(String name) {
        List<Author> authors = authorData.load();
        for (Author author : authors) {
            if (author.getName().equalsIgnoreCase(name)) {
                return author;
            }
        }
        return null;
    }


    public void listAuthorsView() {
        List<Author> authorList = authorData.load();
        System.out.println("Lista de autores:");
        for (int i = 0; i < authorList.size(); i++) {
            System.out.println((i+1) + ". " + authorList.get(i).getName());
        }
    }


    public List<Author> listAuthors() {
        return authorData.load();
    }
}
