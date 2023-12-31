package Controller;

import java.util.List;

import Data.CategoryData;
import Model.Category;

public class CategoryController {

    private CategoryData categoryData;

    public CategoryController(CategoryData categoryData) {
        this.categoryData = categoryData;
    }

    public void createCategory(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria não pode ser vazio ou nulo.");
        }

        List<Category> categoryList = listCategories();
        Category category = new Category(name);
        categoryList.add(category);
        categoryData.save(categoryList);
    }

    public List<Category> listCategories() {
        return categoryData.load();
    }


}
