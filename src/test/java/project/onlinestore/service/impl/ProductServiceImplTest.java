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
import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.entities.SupplierEntity;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.repository.CategoryRepository;
import project.onlinestore.repository.ProductRepository;
import project.onlinestore.repository.SupplierRepository;
import project.onlinestore.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductServiceImplTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ModelMapper modelMapper;

    ProductEntity product1;
    ProductEntity product2;

    CategoryEntity firstCategory;
    CategoryEntity secondCategory;
    CategoryEntity thirdCategory;

    SupplierEntity firstSupplier;
    SupplierEntity secondSupplier;
    SupplierEntity thirdSupplier;

    @Before
    public void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        supplierRepository.deleteAll();

        firstCategory = new CategoryEntity();
        firstCategory.setName("first")
                .setImgUrl("some address")
                .setNameLatin("first")
                .setPosition(1);

        secondCategory = new CategoryEntity();
        secondCategory.setName("secondCategory")
                .setImgUrl("some address")
                .setNameLatin("secondCategory")
                .setPosition(2);

        thirdCategory = new CategoryEntity();
        thirdCategory.setName("thirdCategory")
                .setImgUrl("some address")
                .setNameLatin("thirdCategory")
                .setPosition(3);
        categoryRepository.saveAll(List.of(firstCategory, secondCategory, thirdCategory));

        firstSupplier = new SupplierEntity();
        firstSupplier.setName("TestSupplier-1")
                .setEmail("TestSupplier@TestSupplier.bg")
                .setPhoneNumber("123456789")
                .setAddress("Somewhere")
                .setPerson("Gosho");

        secondSupplier = new SupplierEntity();
        secondSupplier.setName("secondSupplier")
                .setEmail("secondSupplier@secondSupplier.bg")
                .setPhoneNumber("123456789")
                .setAddress("Somewhere")
                .setPerson("Gosho");

        thirdSupplier = new SupplierEntity();
        thirdSupplier.setName("thirdSupplier-1")
                .setEmail("thirdSupplier@thirdSupplier.bg")
                .setPhoneNumber("123456789")
                .setAddress("Somewhere")
                .setPerson("Gosho");
        supplierRepository.saveAll(List.of(firstSupplier, secondSupplier, thirdSupplier));

        product1 = new ProductEntity();
        product1.setProductName("TestProduct")
                .setProductNameLatin("TestProduct")
                .setProductCode("test-1")
                .setProductPrice(BigDecimal.valueOf(500))
                .setImgUrl("random image")
                .setCategory(firstCategory)
                .setSupplier(firstSupplier);

        product2 = new ProductEntity();
        product2.setProductName("TestProduct-2")
                .setProductNameLatin("TestProduct-2")
                .setProductCode("test-2")
                .setProductPrice(BigDecimal.valueOf(800))
                .setImgUrl("random image")
                .setCategory(firstCategory)
                .setSupplier(firstSupplier);

    }

//    @Test
//    public void test_SeedInDb() {
//        Assert.assertEquals(0, productRepository.count());
//        productService.seedProducts();
//        Assert.assertEquals(13, productRepository.count());
//
//    }

    @Test
    public void test_FindAllProducts() {
        Assert.assertEquals(0, productRepository.count());
        productRepository.saveAll(List.of(product1, product2));
        Assert.assertEquals(2, productRepository.count());

        List<ProductViewModel> allProducts = productService.findAllProducts();
        Assert.assertEquals(2, allProducts.size());
    }

    @Test
    public void test_FindProductByName() {
        Assert.assertEquals(0, productRepository.count());
        productRepository.saveAll(List.of(product1, product2));
        Assert.assertEquals(2, productRepository.count());

        ProductEntity productByName = productService.findProductByName(product1.getProductName());
        Assert.assertNotNull(productByName);

        Assert.assertEquals(product1.getProductName(), productByName.getProductName());
    }

    @Test
    public void test_FindProductByNameLatin() {
        Assert.assertEquals(0, productRepository.count());
        productRepository.saveAll(List.of(product1, product2));
        Assert.assertEquals(2, productRepository.count());

        ProductServiceModel productByNameLatin = productService.findProductByNameLatin(product1.getProductNameLatin());
        Assert.assertNotNull(productByNameLatin);

        Assert.assertEquals(product1.getProductNameLatin(), productByNameLatin.getProductNameLatin());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_FindProductByName_ThrowsEx() {
        Assert.assertEquals(0, productRepository.count());
        productRepository.saveAll(List.of(product1, product2));
        Assert.assertEquals(2, productRepository.count());

        ProductServiceModel productByNameLatin = productService.findProductByNameLatin("product1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_AddProduct_ThrowsEx() {
        Assert.assertEquals(0, productRepository.count());
        productRepository.saveAll(List.of(product1, product2));
        Assert.assertEquals(2, productRepository.count());

        productService.addProduct(modelMapper.map(product1, ProductServiceModel.class));
    }

    @Test
    public void test_AddProduct() {
        categoryRepository.save(firstCategory);
        supplierRepository.save(firstSupplier);

        CategoryEntity category = categoryRepository.findByName(firstCategory.getName()).get();
        Assert.assertNotNull(category);
        SupplierEntity supplier = supplierRepository.findByName(firstSupplier.getName()).get();
        Assert.assertNotNull(supplier);

        product1.setCategory(category)
                .setSupplier(supplier);

        productService.addProduct(modelMapper.map(product1, ProductServiceModel.class));
        Assert.assertEquals(1, productRepository.count());
    }

    @Test()
    public void test_FindProductById() {
        Assert.assertEquals(0, productRepository.count());
        productRepository.saveAll(List.of(product1, product2));
        Assert.assertEquals(2, productRepository.count());

        ProductServiceModel productById = productService.findProductById(product1.getId());
        Assert.assertEquals(product1.getId(), productById.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_FindProductById_ThrowsEx() {
        Assert.assertEquals(0, productRepository.count());
        productRepository.saveAll(List.of(product1, product2));
        Assert.assertEquals(2, productRepository.count());

        productService.findProductById(10L);
    }

    @Test()
    public void test_EditProduct() {
        Assert.assertEquals(0, productRepository.count());
        productRepository.saveAll(List.of(product1, product2));
        Assert.assertEquals(2, productRepository.count());

        ProductServiceModel productById = productService.findProductById(product1.getId());
        Assert.assertEquals(product1.getId(), productById.getId());

        productById.setProductCode("newTestCode")
                .setProductName("newTestName")
                .setProductPrice(BigDecimal.valueOf(8000));
        productService.editProduct(productById.getId(), productById);

        ProductEntity productByName = productService.findProductByName(productById.getProductName());
        Assert.assertEquals(productByName.getProductName(), productById.getProductName());
    }

    @Test()
    public void test_EditProductImage() {
        Assert.assertEquals(0, productRepository.count());
        productRepository.saveAll(List.of(product1, product2));
        Assert.assertEquals(2, productRepository.count());

        ProductServiceModel productById = productService.findProductById(product1.getId());
        Assert.assertEquals(product1.getId(), productById.getId());

        productById.setImgUrl("newImage");
        productService.editProductImage(productById);

        ProductEntity productByName = productService.findProductByName(productById.getProductName());

        Assert.assertEquals(productByName.getImgUrl(), productById.getImgUrl());
    }

    @Test()
    public void test_DeleteProduct() {
        Assert.assertEquals(0, productRepository.count());
        productRepository.saveAll(List.of(product1, product2));
        Assert.assertEquals(2, productRepository.count());

        ProductServiceModel productById = productService.findProductById(product1.getId());
        Assert.assertEquals(product1.getId(), productById.getId());

        productService.deleteProduct(product1.getId());
        Assert.assertEquals(1, productRepository.count());
    }
}