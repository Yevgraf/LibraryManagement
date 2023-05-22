package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Publisher {

    private int id;
    private String name;
    private String address;

    public Publisher(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Publisher() {

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

    @Override
    public String toString() {
        return String.format("Editora:\n" +
                        "\tNome: %s\n" +
                        "\tMorada: %s\n",
                name, address);
    }

}
