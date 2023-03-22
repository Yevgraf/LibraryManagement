package Model;

public class Category {


    private static int counter = 0;
    private int categoryId;
    private String categoryName;


    public Category(String categoryName) {
        this.categoryId = counter++;
        this.categoryName = categoryName;
    }

    public Category() {

    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static Category valueOf(String input) {
        return null;
    }
}
