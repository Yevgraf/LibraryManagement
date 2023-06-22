/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author franc
 */
public class User {

    private int id;
    private String name;
    private String address;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(int id, String name, String address, LocalDate birthDate, String phone, String email, String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public User(String name, String address, LocalDate birthDate, String phone, String email) {
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;

    }

    public User(int id, String name, String address, LocalDate birthDate, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return String.format("Utilizador:\n" +
                        "\tNome: %s\n" +
                        "\tMorada: %s\n" +
                        "\tData de nascimento: %s\n" +
                        "\tTelefone: %s\n" +
                        "\tEmail: %s\n",
                name, address, birthDate, phone, email);
    }

}
