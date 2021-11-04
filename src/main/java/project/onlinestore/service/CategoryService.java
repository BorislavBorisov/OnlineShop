package project.onlinestore.service;


import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.domain.view.CategoryViewModel;

import java.util.List;

public interface CategoryService {

    List<CategoryViewModel> getAllCategories();

    List<CategoryViewModel> getCategoriesAllByPosition();

    CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel);

    CategoryServiceModel findCategoryById(Long id);

    CategoryServiceModel editCategory(Long id, CategoryServiceModel categoryServiceModel);

    boolean deleteCategory(Long id);
}
