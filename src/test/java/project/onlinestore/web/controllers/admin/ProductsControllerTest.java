package project.onlinestore.web.controllers.admin;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.binding.ProductAddBindingModel;
import project.onlinestore.domain.entities.CategoryEntity;
import project.onlinestore.domain.entities.SupplierEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.repository.CategoryRepository;
import project.onlinestore.repository.ProductRepository;
import project.onlinestore.repository.SupplierRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser(username = "test", roles = {"ADMIN", "ROOT"})
public class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ModelMapper modelMapper;

    CategoryEntity category;
    SupplierEntity supplier;

    @Before
    public void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        supplierRepository.deleteAll();

        category = new CategoryEntity();
        category.setName("test")
                .setNameLatin("test")
                .setPosition(1)
                .setImgUrl("random");
        categoryRepository.save(category);

        supplier = new SupplierEntity();
        supplier.setEmail("random@random.bg")
                .setAddress("random address")
                .setPerson("random person")
                .setPhoneNumber("0101010101")
                .setName("random name");
        supplierRepository.save(supplier);
    }

    @Test
    public void get_AddProductPage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/products/add"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addProduct"))
                .andExpect(view().name("/admin/products/add-product"));
    }

    @Test
    public void get_AllProductPage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/products"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("allProducts"))
                .andExpect(model().attributeExists("allProducts"))
                .andExpect(view().name("/admin/products/all-products"));
    }

    @Test
    public void get_ProductAdd() throws Exception {
        ProductAddBindingModel productAddBindingModel = new ProductAddBindingModel();
        productAddBindingModel.setProductCode("test")
                .setProductName("test")
                .setProductPrice(BigDecimal.valueOf(500))
                .setDescription("description")
                .setCategory("test")
                .setSupplier("random name");

        CategoryServiceModel map1 = modelMapper.map(category, CategoryServiceModel.class);
        SupplierServiceModel map2 = modelMapper.map(supplier, SupplierServiceModel.class);
        ProductServiceModel map = modelMapper.map(productAddBindingModel, ProductServiceModel.class);
        map.setImgUrl("random")
                .setCategory(map1)
                .setSupplier(map2);

        mockMvc.perform(post("/admin/products/add")
                        .sessionAttr("product", map)
                        .param("productCode", map.getProductCode())
                        .param("productName", map.getProductName())
                        .param("productPrice", String.valueOf(map.getProductPrice()))
                        .param("description", map.getDescription())
                        .param("category", String.valueOf(map1.getId()))
                        .param("supplier", String.valueOf(map2.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("addProductConfirm"))
                .andExpect(redirectedUrl("/admin/products"));
    }

    @Test
    public void get_ProductAdd_InvalidInput() throws Exception {
        ProductAddBindingModel productAddBindingModel = new ProductAddBindingModel();
        productAddBindingModel.setProductCode("")
                .setProductName("1")
                .setProductPrice(BigDecimal.valueOf(-99))
                .setDescription("description")
                .setCategory("test")
                .setSupplier("random name");

        CategoryServiceModel map1 = modelMapper.map(category, CategoryServiceModel.class);
        SupplierServiceModel map2 = modelMapper.map(supplier, SupplierServiceModel.class);
        ProductServiceModel map = modelMapper.map(productAddBindingModel, ProductServiceModel.class);
        map.setImgUrl("random")
                .setCategory(map1)
                .setSupplier(map2);

        mockMvc.perform(post("/admin/products/add")
                        .sessionAttr("product", map)
                        .param("productCode", map.getProductCode())
                        .param("productName", map.getProductName())
                        .param("productPrice", String.valueOf(map.getProductPrice()))
                        .param("description", map.getDescription())
                        .param("category", String.valueOf(map1.getId()))
                        .param("supplier", String.valueOf(map2.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("addProductConfirm"))
                .andExpect(flash().attributeExists("productAddBindingModel"))
                .andExpect(redirectedUrl("/admin/products/add"));
    }

}