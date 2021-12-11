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
import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.entities.SupplierEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.service.SupplierServiceModel;
import project.onlinestore.repository.CategoryRepository;
import project.onlinestore.repository.ProductRepository;
import project.onlinestore.repository.SupplierRepository;

import java.math.BigDecimal;
import java.util.Optional;

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
    ProductEntity product;

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

        product = new ProductEntity();
        product.setProductName("TestProduct")
                .setProductNameLatin("TestProduct")
                .setProductCode("test-1")
                .setProductPrice(BigDecimal.valueOf(500))
                .setImgUrl("random image")
                .setCategory(category)
                .setSupplier(supplier);
        productRepository.save(product);
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
    public void get_ProductAdd_ProductCodeExist() throws Exception {
        ProductAddBindingModel productAddBindingModel = new ProductAddBindingModel();
        productAddBindingModel.setProductCode("test-1")
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
                .andExpect(flash().attributeExists("productCodeExists"))
                .andExpect(redirectedUrl("/admin/products/add"));
    }

    @Test
    public void get_ProductAdd_ProductNameExist() throws Exception {
        ProductAddBindingModel productAddBindingModel = new ProductAddBindingModel();
        productAddBindingModel.setProductCode("test")
                .setProductName("TestProduct")
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
                .andExpect(flash().attributeExists("productNameExists"))
                .andExpect(redirectedUrl("/admin/products/add"));
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

    @Test
    public void get_ProductAdd_InvalidCategory() throws Exception {
        ProductAddBindingModel productAddBindingModel = new ProductAddBindingModel();
        productAddBindingModel.setProductCode("1232131")
                .setProductName("1")
                .setProductPrice(BigDecimal.valueOf(99))
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
                        .param("category", "--- Избери Категория ---")
                        .param("supplier", String.valueOf(map2.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("addProductConfirm"))
                .andExpect(flash().attributeExists("invalidCategory"))
                .andExpect(redirectedUrl("/admin/products/add"));
    }

    @Test
    public void get_ProductAdd_InvalidSupplier() throws Exception {
        ProductAddBindingModel productAddBindingModel = new ProductAddBindingModel();
        productAddBindingModel.setProductCode("1232131")
                .setProductName("1")
                .setProductPrice(BigDecimal.valueOf(99))
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
                        .param("supplier", "--- Избери Доставчик ---")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("addProductConfirm"))
                .andExpect(flash().attributeExists("invalidSupplier"))
                .andExpect(redirectedUrl("/admin/products/add"));
    }

    @Test
    public void get_EditProductPage_ReturnsOk() throws Exception {
        Assert.assertEquals(1, productRepository.count());

        Optional<ProductEntity> byProductCode = productRepository.findByProductCode(product.getProductCode());
        Assert.assertNotNull(byProductCode);

        mockMvc.perform(get("/admin/products/edit/" + byProductCode.get().getId()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("editProduct"))
                .andExpect(view().name("/admin/products/edit-product"));
    }

    @Test
    public void test_EditProduct() throws Exception {
        Assert.assertEquals(1, productRepository.count());

        Optional<ProductEntity> byProductCode = productRepository.findByProductCode(product.getProductCode());
        Assert.assertNotNull(byProductCode);

        ProductAddBindingModel productAddBindingModel = modelMapper.map(byProductCode.get(), ProductAddBindingModel.class);
        productAddBindingModel.setProductCode("new code")
                        .setProductName("new Name")
                                .setProductPrice(BigDecimal.valueOf(89898));

        mockMvc.perform(post("/admin/products/edit/" + byProductCode.get().getId())
                .sessionAttr("product", productAddBindingModel)
                .param("productCode", productAddBindingModel.getProductCode())
                .param("productName", productAddBindingModel.getProductName())
                .param("productPrice", String.valueOf(productAddBindingModel.getProductPrice()))
                .param("description", byProductCode.get().getDescription())
                .param("category", String.valueOf(byProductCode.get().getId()))
                .param("supplier", String.valueOf(byProductCode.get().getId()))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("editProductConfirm"))
                .andExpect(redirectedUrl("/admin/products"));

    }

    @Test
    public void test_EditProduct_InvalidInput() throws Exception {
        Assert.assertEquals(1, productRepository.count());

        Optional<ProductEntity> byProductCode = productRepository.findByProductCode(product.getProductCode());
        Assert.assertNotNull(byProductCode);

        ProductAddBindingModel productAddBindingModel = modelMapper.map(byProductCode.get(), ProductAddBindingModel.class);
        productAddBindingModel.setProductCode("n")
                .setProductName("")
                .setProductPrice(BigDecimal.valueOf(-89898));

        mockMvc.perform(post("/admin/products/edit/" + byProductCode.get().getId())
                        .sessionAttr("product", productAddBindingModel)
                        .param("productCode", productAddBindingModel.getProductCode())
                        .param("productName", productAddBindingModel.getProductName())
                        .param("productPrice", String.valueOf(productAddBindingModel.getProductPrice()))
                        .param("description", byProductCode.get().getDescription())
                        .param("category", String.valueOf(byProductCode.get().getId()))
                        .param("supplier", String.valueOf(byProductCode.get().getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("editProductConfirm"))
                .andExpect(flash().attributeExists("productAddBindingModel"))
                .andExpect(redirectedUrl("/admin/products/edit/" + byProductCode.get().getId()));

    }

    @Test
    public void get_DeleteProductPage() throws Exception {
        Assert.assertEquals(1, productRepository.count());

        Optional<ProductEntity> byProductCode = productRepository.findByProductCode(product.getProductCode());
        Assert.assertNotNull(byProductCode);

        mockMvc.perform(get("/admin/products/delete/" + byProductCode.get().getId()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteProduct"))
                .andExpect(view().name("/admin/products/delete-product"));
    }

    @Test
    public void get_DeleteProductsConfirm() throws Exception {
        Assert.assertEquals(1, productRepository.count());

        Optional<ProductEntity> byProductCode = productRepository.findByProductCode(product.getProductCode());
        Assert.assertNotNull(byProductCode);

        mockMvc.perform(post("/admin/products/delete/" + byProductCode.get().getId()))
                .andExpect(handler().methodName("deleteProductConfirm"))
                .andExpect(redirectedUrl("/admin/products"));
    }

}