/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author franc
 */
public class Author {

    private int id;
    private String name;
    private String address;
    private LocalDate birthDate;

    public Author(String name, String address, LocalDate birthDate) {
        this.name = name;
        this.address = address;
        this.birthDate = birthDate;
    }

    public Author() {

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

    @Override
    public String toString() {
        return String.format("Autor:\n" +
                        "\tID: %d\n" +
                        "\tNome: %s\n" +
                        "\tMorada: %s\n" +
                        "\tData de Nascimento: %s\n",
                id, name, address, birthDate);
    }
}
