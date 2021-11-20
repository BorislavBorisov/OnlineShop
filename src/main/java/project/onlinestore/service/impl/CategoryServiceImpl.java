package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.CategoryEntity;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.domain.view.CategoryViewModel;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.repository.CategoryRepository;
import project.onlinestore.service.CategoryService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

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
    public String translate(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

    @Override
    public List<ProductViewModel> getAllProductsByCategoryName(String nameLatin) {
        CategoryEntity category = this.categoryRepository.findByNameLatin(nameLatin)
                .orElseThrow(() -> new IllegalArgumentException("Invalid caregory"));

        return category.getProducts()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductViewModel.class))
                .collect(Collectors.toList());
    }
}
