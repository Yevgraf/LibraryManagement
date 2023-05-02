package Model;

import java.io.Serializable;

public class AgeRange implements Serializable {
    private static final long serialVersionUID = 1L;
    private int counter = 0;
    private int id;
    private String description;

    public AgeRange(String description) {
        this.id = counter++;
        this.description = description;
    }

    public AgeRange() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
