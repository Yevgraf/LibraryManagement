package Model;

import java.io.Serializable;
import java.time.LocalDate;

public class Member extends User implements Serializable {
    private int maxBorrowedBooks;
    private Card card;

    public Member(String name, String address, LocalDate birthDate, String phone, String email) {
        super(name, address, birthDate, phone, email);
        this.maxBorrowedBooks = 3;

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

    public int getMaxBorrowedBooks() {
        return maxBorrowedBooks;
    }

    public void setMaxBorrowedBooks(int maxBorrowedBooks) {
        this.maxBorrowedBooks = maxBorrowedBooks;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}

