package Controller;

import java.time.LocalDate;
import java.util.List;

import Data.LibrarianData;
import Model.Librarian;
import View.CreateLibrarianView;

public class LibrarianController {
    private LibrarianData librarianData;

    public LibrarianController(LibrarianData librarianData) {
        this.librarianData = librarianData;
    }
    public void createLibrarian(String name, String address, LocalDate birthDate, String phone, String email, String password) {
        List<Librarian> librarianList = librarianData.load();
        Librarian librarian = new Librarian(name, address, birthDate, phone, email, password);
        librarianList.add(librarian);
        librarianData.save(librarianList);
    }


    public List<Librarian> listLibrarians() {
        return librarianData.load();
    }
}
