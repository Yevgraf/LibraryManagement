package Controller;

import Model.Author;
import Data.AuthorData;

import java.time.LocalDate;
import java.util.List;
import Data.AuthorData;
import Model.Author;

public class AuthorController {
    private AuthorData authorData;

    public AuthorController(AuthorData authorData) {
        this.authorData = authorData;
    }

    public void createAuthor(String name, String address, LocalDate birthDate) {
         // Validations
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do autor inválido.");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço do autor inválido.");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }

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



    public List<Author> listAuthors() {
        return authorData.load();
    }
}
