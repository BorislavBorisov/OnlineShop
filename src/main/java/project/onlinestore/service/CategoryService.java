package project.onlinestore.service;


import project.onlinestore.domain.entities.CategoryEntity;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.domain.view.CategoryViewModel;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.service.impl.CategoryServiceImpl;

import java.util.List;

public interface CategoryService {

    List<CategoryViewModel> getAllCategories();

    List<CategoryViewModel> getCategoriesAllByPosition();

    CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel);

    CategoryServiceModel findCategoryById(Long id);

    CategoryServiceModel editCategory(Long id, CategoryServiceModel categoryServiceModel);

    boolean deleteCategory(Long id);

    boolean editImageCategory(CategoryServiceModel categoryServiceModel);

    List<ProductViewModel> getAllProductsByCategoryName(String nameLatin);


    List<ProductViewModel> getSimilarProducts(String categoryNameLatin, String nameLatin);

    void seedCategories();

    CategoryEntity findCategoryByName(String name);
}
