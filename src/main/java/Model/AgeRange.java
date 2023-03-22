package Model;

public class AgeRange {
    private static int counter = 0;
    private int id;
    private String description;

    public AgeRange(String description) {
        this.id = counter++;
        this.description = description;
    }

    public AgeRange(){

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

    public static AgeRange valueOf(String upperCase) {
        return null;
    }
}
