package Controller;

import Model.Author;
import View.CreateAuthorView;

import java.util.List;

public class AuthorController {
    private CreateAuthorView createAuthorView;
    private AuthorData authorData;

    public AuthorController(CreateAuthorView createAuthorView, AuthorData authorData) {
        this.createAuthorView = createAuthorView;
        this.authorData = authorData;
    }

    public Author createAuthor() {
        Author author = createAuthorView.createAuthor();
        List<Author> authorList = authorData.load();
        authorList.add(author);
        authorData.save(authorList);
        createAuthorView.displayAuthor(author);
        System.out.println("Autor criado e guardado com sucesso.");
        return author;
    }

    public void listAuthors() {
        List<Author> authorList = authorData.load();
        if (authorList.isEmpty() || authorList == null) {
            System.out.println("Não existem autores guardados.");
        } else {
            for (Author author : authorList) {
                System.out.println(author);
            }
        }
    }
}

