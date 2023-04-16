package Controller;

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
        authorData.save(authorList);
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
            System.out.println((i + 1) + ". " + authorList.get(i).getName());
        }
    }

    public List<Author> listAuthors() {
        return authorData.load();
    }
}
