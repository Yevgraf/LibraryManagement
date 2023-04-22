package Model;

import java.io.Serializable;

public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    private int counter = 0;
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

    @Override
    public String toString() {
        return categoryName;
    }


}
