package Model;

import java.io.Serializable;
import java.time.LocalDate;

public class Publisher implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int counter = 0;
    private int id;
    private String name;
    private String address;

    public Publisher(String name, String address) {
        this.id = counter++;
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
