package Controller;

import Data.LibrarianData;
import Model.Librarian;
import View.CreateLibrarianView;

import java.util.List;

public class UserController {
    private CreateLibrarianView createLibrarianView;
    private LibrarianData librarianData;

    public UserController(CreateLibrarianView createLibrarianView, LibrarianData librarianData) {
        this.createLibrarianView = createLibrarianView;
        this.librarianData = librarianData;
    }

    public Librarian createLibrarian() {
        Librarian librarian = createLibrarianView.createUser();
        List<Librarian> userList = librarianData.load();
        userList.add(librarian);
        librarianData.save(userList);
        createLibrarianView.displayUser(librarian);
        System.out.println("Bibliotecário  criado e guardado com sucesso.");
    return librarian;
    }


    public void listUsers() {
        List<Librarian> userList = librarianData.load();
        if (userList.isEmpty()) {
            System.out.println("Não existem Bibliotecário  guardados.");
        } else {
            for (Librarian librarian : userList) {
                System.out.println(librarian);
            }
        }
    }
}
