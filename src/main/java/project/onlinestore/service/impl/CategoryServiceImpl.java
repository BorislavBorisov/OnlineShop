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
            category.setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638364042/empty_kk164n.jpg");
            category.setNameLatin(translate(categoryServiceModel.getName()).toLowerCase());
            return this.modelMapper.map(this.categoryRepository.save(category), CategoryServiceModel.class);
        }

        throw new IllegalArgumentException("Категория със същото име вече съществува!");

    }

    @Override
    public CategoryServiceModel findCategoryById(Long id) {
        return this.modelMapper.map(this.categoryRepository.findById(id)
                .orElse(null), CategoryServiceModel.class);
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

    @Override
    public List<ProductViewModel> getSimilarProducts(String categoryNameLatin, String nameLatin) {
        CategoryEntity category = this.categoryRepository.findByNameLatin(categoryNameLatin)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));

        return category.getProducts()
                .stream()
                .limit(4)
                .filter(p -> !p.getProductNameLatin().equals(nameLatin))
                .map(p -> this.modelMapper.map(p, ProductViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void seedCategories() {
        if (this.categoryRepository.count() == 0) {
            CategoryEntity categoryEntity = new CategoryEntity()
                    .setName("Компютри");
            categoryEntity.setNameLatin(translate(categoryEntity.getName()))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1637345647/myyqzjq54fapoaxiq0a8.png")
                    .setPosition(1)
                    .setRegistered(Instant.now());

            CategoryEntity categoryEntity1 = new CategoryEntity()
                    .setName("Лаптопи");
            categoryEntity1.setNameLatin(translate(categoryEntity1.getName()))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1637347030/uqn1y3zewdokxe6ht6h5.png")
                    .setPosition(2)
                    .setRegistered(Instant.now());

            CategoryEntity categoryEntity2 = new CategoryEntity()
                    .setName("Телефони");
            categoryEntity2.setNameLatin(translate(categoryEntity2.getName()))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1637425061/bgz1sadmgfjovg6erjov.png")
                    .setPosition(3)
                    .setRegistered(Instant.now());

            CategoryEntity categoryEntity3 = new CategoryEntity()
                    .setName("Мишки");
            categoryEntity3.setNameLatin(translate(categoryEntity3.getName()))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638465290/mouse_he4rra.png")
                    .setPosition(4)
                    .setRegistered(Instant.now());

            CategoryEntity categoryEntity4 = new CategoryEntity()
                    .setName("Клавиатури");
            categoryEntity4.setNameLatin(translate(categoryEntity4.getName()))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1637349593/abwrd71uxhqs9ryuhbil.png")
                    .setPosition(5)
                    .setRegistered(Instant.now());

            CategoryEntity categoryEntity5 = new CategoryEntity();
            categoryEntity5.setName("Слушалки");
            categoryEntity5.setNameLatin(translate(categoryEntity5.getName()))
                    .setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1637348578/tufwwqpkiz8psi08upkt.png")
                    .setPosition(6)
                    .setRegistered(Instant.now());

            this.categoryRepository.saveAll(
                    List.of(categoryEntity, categoryEntity1, categoryEntity2, categoryEntity3, categoryEntity4, categoryEntity5)
            );
        }
    }

    @Override
    public CategoryEntity findCategoryByName(String name) {
        return this.categoryRepository.findByName(name).get();
    }
}
