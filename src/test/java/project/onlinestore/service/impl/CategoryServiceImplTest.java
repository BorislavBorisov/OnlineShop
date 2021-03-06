package project.onlinestore.service.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.onlinestore.domain.entities.CategoryEntity;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.domain.view.CategoryViewModel;
import project.onlinestore.repository.CategoryRepository;
import project.onlinestore.service.CategoryService;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryServiceImplTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;

    CategoryEntity categoryEntity;
    CategoryEntity categoryEntity1;

    @Before
    public void setup() {
        categoryRepository.deleteAll();

        categoryEntity = new CategoryEntity();
        categoryEntity.setName("TestCategory")
                .setImgUrl("some address")
                .setNameLatin("TestCategory")
                .setPosition(1);

        categoryEntity1 = new CategoryEntity();
        categoryEntity1.setName("TestCategory-2")
                .setImgUrl("some address")
                .setNameLatin("TestCategory-2")
                .setPosition(2);

    }

    @After
    public void clearAll() {
        categoryRepository.deleteAll();
    }

    @Test
    public void test_SeedCategories() {
        categoryService.seedCategories();
        Assert.assertEquals(6, categoryRepository.count());
        categoryRepository.deleteAll();
    }

    @Test
    public void test_FindCategoryByName_Correct() {
        categoryRepository.save(categoryEntity);
        String name = categoryEntity.getName();
        CategoryEntity categoryByName = categoryService.findCategoryByName(name);
        Assert.assertEquals(name, categoryByName.getName());

    }

    @Test
    public void test_GetCategoryById() {
        categoryRepository.saveAll(List.of(categoryEntity1, categoryEntity));
        Assert.assertEquals(2, categoryRepository.count());

        CategoryServiceModel category1 = categoryService.findCategoryById(categoryEntity.getId());
        Assert.assertNotNull(category1);
        CategoryServiceModel category2 = categoryService.findCategoryById(categoryEntity1.getId());
        Assert.assertNotNull(category2);

        Assert.assertEquals(category1.getName(), categoryEntity.getName());
        Assert.assertEquals(category2.getName(), categoryEntity1.getName());
    }

    @Test
    public void test_AddCategory() {
        Assert.assertEquals(0, categoryRepository.count());
        categoryService.addCategory(modelMapper.map(categoryEntity, CategoryServiceModel.class));
        Assert.assertEquals(1, categoryRepository.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_AddCategory_ThrowsEx() {
        categoryRepository.save(categoryEntity);
        Assert.assertEquals(1, categoryRepository.count());
        categoryService.addCategory(modelMapper.map(categoryEntity, CategoryServiceModel.class));
    }

    @Test
    public void test_GetAllCategories() {
        categoryRepository.saveAll(List.of(categoryEntity1, categoryEntity));
        List<CategoryViewModel> allCategories = categoryService.getAllCategories();
        Assert.assertEquals(2, allCategories.size());
    }

    @Test
    public void test_GetAllCategoriesByPosition() {
        categoryRepository.saveAll(List.of(categoryEntity1, categoryEntity));
        List<CategoryViewModel> allCategories = categoryService.getCategoriesAllByPosition();
        CategoryEntity map = modelMapper.map(allCategories.get(0), CategoryEntity.class);
        Assert.assertEquals(map.getPosition(), categoryEntity.getPosition());
    }

    @Test
    public void test_EditCategory() {
        categoryRepository.save(categoryEntity);
        CategoryEntity category = categoryService.findCategoryByName(categoryEntity.getName());
        Assert.assertNotNull(category);
        category.setName("test")
                .setPosition(3);
        categoryService.editCategory(category.getId(), modelMapper.map(category, CategoryServiceModel.class));

        Assert.assertEquals("test", category.getName());
    }

    @Test
    public void test_DeleteCategory() {
        categoryRepository.save(categoryEntity);
        Assert.assertEquals(1, categoryRepository.count());
        CategoryEntity category = categoryService.findCategoryByName(categoryEntity.getName());
        Assert.assertNotNull(category);

        categoryService.deleteCategory(category.getId());
        Assert.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void test_EditCategoryImage() {
        categoryRepository.save(categoryEntity);
        Assert.assertEquals(1, categoryRepository.count());
        CategoryEntity category = categoryService.findCategoryByName(categoryEntity.getName());
        Assert.assertNotNull(category);

        category.setImgUrl("nova snimka");
        categoryService.editImageCategory(modelMapper.map(category, CategoryServiceModel.class));
    }
}