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
        List<Category> categoryList = listCategories();
        Category category = new Category(name);
        categoryList.add(category);
        categoryData.save(categoryList);
    }

    public List<Category> listCategories() {
        return categoryData.load();
    }

    public Category findByName(String name) {
        List<Category> categories = categoryData.load();
        for (Category category : categories) {
            if (category.getCategoryName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

}
