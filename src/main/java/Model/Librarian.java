package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Librarian extends User {

    private String password;


    public Librarian(String name, String address, LocalDate birthDate, String phone, String email, String password) {
        super(name, address, birthDate, phone, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
    }

    public String getAddress() {
        return super.getAddress();
    }

    public void setAddress(String address) {
        super.setAddress(address);
    }

    public LocalDate getBirthDate() {
        return super.getBirthDate();
    }

    public void setBirthDate(LocalDate birthDate) {
        super.setBirthDate(birthDate);
    }

    public String getPhone() {
        return super.getPhone();
    }

    public void setPhone(String phone) {
        super.setPhone(phone);
    }

    public String getEmail() {
        return super.getEmail();
    }

    public void setEmail(String email) {
        super.setEmail(email);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bibliotecário:\n");
        sb.append("\tNome: ").append(getName()).append("\n");
        sb.append("\tMorada: ").append(getAddress()).append("\n");
        sb.append("\tData de Nascimento: ").append(getBirthDate()).append("\n");
        sb.append("\tTelefone: ").append(getPhone()).append("\n");
        sb.append("\tEmail: ").append(getEmail()).append("\n");
        sb.append("\tPalavra-passe: ").append(getPassword()).append("\n");
        return sb.toString();
    }


}
