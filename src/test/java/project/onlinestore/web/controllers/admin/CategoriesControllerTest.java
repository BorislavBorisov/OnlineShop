package project.onlinestore.web.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.binding.CategoryAddBindingModel;
import project.onlinestore.domain.entities.CategoryEntity;
import project.onlinestore.domain.entities.RoleEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.repository.CategoryRepository;
import project.onlinestore.repository.RoleRepository;
import project.onlinestore.repository.UserRepository;

import java.time.Instant;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser(username = "test", roles = {"ADMIN", "ROOT"})
public class CategoriesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Before
    public void setup() {
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity userEntity = initUser();
        userRepository.save(userEntity);

        CategoryEntity category = initCategory();
        categoryRepository.save(category);
    }

    @After
    public void tearDown() {
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void get_CategoryPage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("allCategories"))
                .andExpect(model().attributeExists("allCategories"))
                .andExpect(view().name("/admin/categories/all-categories"));
    }

    @Test
    public void get_AddCategoryPage() throws Exception {
        mockMvc.perform(get("/admin/categories/add"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addCategory"))
                .andExpect(view().name("/admin/categories/add-category"));
    }

    @Test
    public void addCategory_InvalidInput() throws Exception {
        CategoryServiceModel categoryServiceModel = new CategoryServiceModel();
        categoryServiceModel.setName("B");
        categoryServiceModel.setPosition(1);
        categoryServiceModel.setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638303120/eronloirc8bv4n7qna1a.jpg");

        mockMvc.perform(post("/admin/categories/add")
                        .content(objectMapper.writeValueAsString(categoryServiceModel))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("addCategoryConfirm"))
                .andExpect(flash().attributeExists("categoryAddBindingModel"))
                .andExpect(redirectedUrl("/admin/categories/add"));
    }

    @Test
    public void addCategory_CategoryNameExists() throws Exception {
        Assert.assertEquals(1,categoryRepository.count());
        CategoryAddBindingModel categoryAddBindingModel = new CategoryAddBindingModel();
        categoryAddBindingModel.setName("TestCategory");
        categoryAddBindingModel.setPosition(1);

        mockMvc.perform(post("/admin/categories/add")
                        .sessionAttr("category", categoryAddBindingModel)
                        .param("name", categoryAddBindingModel.getName())
                        .param("position", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("addCategoryConfirm"))
                .andExpect(flash().attributeExists("categoryNameExists"))
                .andExpect(redirectedUrl("/admin/categories/add"));
    }

    @Test
    public void addCategoryAdd_NewCategory() throws Exception {
        CategoryAddBindingModel categoryAddBindingModel = new CategoryAddBindingModel();
        categoryAddBindingModel.setName("TESTOVA");
        categoryAddBindingModel.setPosition(1);

        mockMvc.perform(post("/admin/categories/add")
                        .sessionAttr("category", categoryAddBindingModel)
                        .param("name", categoryAddBindingModel.getName())
                        .param("position", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("addCategoryConfirm"))
                .andExpect(redirectedUrl("/admin/categories"));

        Assert.assertEquals(2, categoryRepository.count());
    }

    @Test
    public void get_EditCategoryPage() throws Exception {
        CategoryEntity testCategory = categoryRepository.findByName("TestCategory").get();

        mockMvc.perform(get("/admin/categories/edit/" + testCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("editCategory"))
                .andExpect(view().name("/admin/categories/edit-category"));
    }

    @Test
    public void get_CategoryEditCategory_InvalidInput() throws Exception {
        CategoryEntity testCategory = categoryRepository.findByName("TestCategory").get();

        CategoryAddBindingModel categoryAddBindingModel = new CategoryAddBindingModel();
        categoryAddBindingModel.setName("q")
                .setPosition(-1);

        mockMvc.perform(post("/admin/categories/edit/" + testCategory.getId())
                        .sessionAttr("category", testCategory)
                        .param("name", categoryAddBindingModel.getName())
                        .param("position", "-1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("editCategoryConfirm"))
                .andExpect(flash().attributeExists("categoryAddBindingModel"))
                .andExpect(redirectedUrl("/admin/categories/edit/" + testCategory.getId()));
    }

    @Test
    public void categoryEditCategory() throws Exception {
        CategoryEntity testCategory = categoryRepository.findByName("TestCategory").get();
        CategoryAddBindingModel categoryAddBindingModel = new CategoryAddBindingModel();
        categoryAddBindingModel.setName("qwert")
                .setPosition(10);

        mockMvc.perform(post("/admin/categories/edit/" + testCategory.getId())
                        .sessionAttr("category", testCategory)
                        .param("name", categoryAddBindingModel.getName())
                        .param("position", "10")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("editCategoryConfirm"))
                .andExpect(redirectedUrl("/admin/categories"));
    }

    @Test
    public void getEditCategoryImagePage() throws Exception {
        CategoryEntity testCategory = categoryRepository.findByName("TestCategory").get();

        mockMvc.perform(get("/admin/categories/edit/image/" + testCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("editImageCategory"))
                .andExpect(view().name("/admin/categories/edit-image-category"));
    }

    @Test
    public void get_DeleteCategoryPage() throws Exception {
        CategoryEntity testCategory = categoryRepository.findByName("TestCategory").get();

        mockMvc.perform(get("/admin/categories/delete/" + testCategory.getId()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteCategory"))
                .andExpect(view().name("/admin/categories/delete-category"));
    }

    @Test
    public void get_DeleteCategoryConfirm() throws Exception {
        CategoryEntity testCategory = categoryRepository.findByName("TestCategory").get();

        mockMvc.perform(post("/admin/categories/delete/" + testCategory.getId()))
                .andExpect(handler().methodName("deleteCategoryConfirm"))
                .andExpect(redirectedUrl("/admin/categories"));
    }

    private CategoryEntity initCategory() {
        CategoryEntity category = new CategoryEntity();
        category.setName("TestCategory");
        category.setPosition(1);
        category.setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1638303120/eronloirc8bv4n7qna1a.jpg");
        return category;
    }

    public UserEntity initUser() {
        RoleEntity roleAdmin = new RoleEntity();
        roleAdmin.setAuthority("ROLE_ADMIN");
        roleRepository.save(roleAdmin);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@test.bg")
                .setFullName("test")
                .setPassword("1234")
                .setUsername("test")
                .setAuthorities(Set.of(roleAdmin))
                .setRegistered(Instant.now());
        return userEntity;
    }



}