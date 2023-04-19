package Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import Data.LibrarianData;
import Model.Librarian;
import View.CreateLibrarianView;

public class LibrarianController {
    private LibrarianData librarianData;

    public LibrarianController(LibrarianData librarianData) {
        this.librarianData = librarianData;
    }
    public void createLibrarian(String name, String address, LocalDate birthDate, String phone, String email, String password) {
        // Verificar se todos os campos obrigatórios foram preenchidos
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Endereço não pode ser nulo ou vazio");
        }
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Telefone não pode ser nulo ou vazio");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }

        // Verificar se o email tem formato válido
        if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
            throw new IllegalArgumentException("Email inválido");
        }

        // Verificar se a senha tem pelo menos 8 caracteres, uma letra minúscula, uma letra maiúscula, um número e um caractere especial
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", password)) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 8 caracteres, uma letra minúscula, uma letra maiúscula, um número e um caractere especial");
        }

        // Verificar se a data de nascimento não é nula e é anterior à data atual
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento inválida");
        }

        // Verificar se o telefone tem formato válido
        if (!Pattern.matches("\\(\\d{2}\\)\\d{4}-\\d{4}", phone)) {
            throw new IllegalArgumentException("Telefone inválido");
        }

       // Verificar se já existe um bibliotecário com o mesmo email antes de criar um novo bibliotecário com esse email
        List<Librarian> librarianList = (List<Librarian>) librarianData.load();
       if (librarianList.stream().anyMatch(librarian -> librarian.getEmail().equals(email))) {
            throw new IllegalArgumentException("Já existe um bibliotecário com esse email");
        }
        Librarian librarian = new Librarian(name, address, birthDate, phone, email, password);
        librarianList.add(librarian);
        librarianData.save(librarianList);
    }


    public List<Librarian> listLibrarians() {
        return (List<Librarian>) librarianData.load();
    }
}
