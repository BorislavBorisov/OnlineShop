package project.onlinestore.web.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.binding.ProductAddBindingModel;
import project.onlinestore.domain.entities.CategoryEntity;
import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.entities.SupplierEntity;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.repository.CategoryRepository;
import project.onlinestore.repository.ProductRepository;
import project.onlinestore.repository.SupplierRepository;
import project.onlinestore.service.CategoryService;
import project.onlinestore.service.SupplierService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test", roles = {"ADMIN", "ROOT"})
class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
        supplierRepository.deleteAll();
        categoryRepository.deleteAll();

        init();
    }


    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
        supplierRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void get_ProductPage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("allProducts"))
                .andExpect(model().attributeExists("allProducts"))
                .andExpect(view().name("/admin/products/all-products"));
    }

    @Test
    public void get_AddProductPage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/products/add"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addProduct"))
                .andExpect(view().name("/admin/products/add-product"));
    }

    @Test
    public void create_Product_InvalidInput() throws Exception {
        init();

        mockMvc.perform(post("/admin/products/add")
                        .param("productCode", "p")
                        .param("productName", "t")
                        .param("productPrice", "-1")
                        .param("description", "testetsttestest")
                        .param("supplier", "1")
                        .param("category", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("addProductConfirm"))
                .andExpect(flash().attributeExists("productAddBindingModel"))
                .andExpect(redirectedUrl("/admin/products/add"));
    }


    public void init() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setImgUrl("aaaa")
                .setName("testCategory")
                .setNameLatin("testCategory")
                .setPosition(1);
        categoryRepository.save(categoryEntity);

        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setName("testSupplier")
                .setPhoneNumber("0909090900")
                .setEmail("test@test.bg")
                .setAddress("testtestttew")
                .setPerson("testtest");
        supplierRepository.save(supplierEntity);
    }
}