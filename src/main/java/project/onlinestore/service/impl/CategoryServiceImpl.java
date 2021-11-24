package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.CategoryEntity;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.domain.view.CategoryViewModel;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.repository.CategoryRepository;
import project.onlinestore.service.BaseService;
import project.onlinestore.service.CategoryService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends BaseService implements CategoryService {

    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<CategoryViewModel> getAllCategories() {
        return this.categoryRepository.findAll()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryViewModel> getCategoriesAllByPosition() {
        return this.categoryRepository.getAllByPosition()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel) {
        CategoryEntity categoryEntity = this.categoryRepository
                .findByName(categoryServiceModel.getName())
                .orElse(null);

        if (categoryEntity == null) {
            CategoryEntity category = this.modelMapper.map(categoryServiceModel, CategoryEntity.class);
            category.setNameLatin(translate(categoryServiceModel.getName()).toLowerCase());
            return this.modelMapper.map(this.categoryRepository.save(category), CategoryServiceModel.class);
        }

        throw new IllegalArgumentException("Категория със същото име вече съществува!");

    }

    @Override
    public CategoryServiceModel findCategoryById(Long id) {
        CategoryEntity category = this.categoryRepository
                .findById(id).orElse(null);

        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel editCategory(Long id, CategoryServiceModel categoryServiceModel) {
        CategoryEntity category = this.categoryRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID!"));

        category.setName(categoryServiceModel.getName());
        category.setNameLatin(translate(categoryServiceModel.getName()).toLowerCase());
        category.setPosition(categoryServiceModel.getPosition());
        category.setModified(Instant.now());
        this.categoryRepository.save(category);

        return this.modelMapper.map(category, CategoryServiceModel.class);

    }

    @Override
    public boolean deleteCategory(Long id) {
        CategoryEntity category =
                this.categoryRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid category ID!"));

        try {
            this.categoryRepository.delete(category);
            return true;
        } catch (Exception exception) {
            return false;
        }

    }

    @Override
    public boolean editImageCategory(CategoryServiceModel categoryServiceModel) {
        CategoryEntity category = this.categoryRepository.findById(categoryServiceModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID!"));

        try {
            category.setImgUrl(categoryServiceModel.getImgUrl());
            category.setModified(Instant.now());
            this.categoryRepository.save(category);
            return true;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid category ID!");
        }

    }


    @Override
    public List<ProductViewModel> getAllProductsByCategoryName(String nameLatin) {
        CategoryEntity category = this.categoryRepository.findByNameLatin(nameLatin)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));

        return category.getProducts()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductViewModel.class))
                .collect(Collectors.toList());
    }
}
