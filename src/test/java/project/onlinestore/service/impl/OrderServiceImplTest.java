//package project.onlinestore.service.impl;
//
//import javassist.tools.rmi.ObjectNotFoundException;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.MockMvc;
//import project.onlinestore.domain.entities.*;
//import project.onlinestore.domain.view.CartViewModel;
//import project.onlinestore.domain.view.ProductViewModel;
//import project.onlinestore.repository.*;
//import project.onlinestore.service.OrderService;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import project.onlinestore.repository.OrderRepository;
//import project.onlinestore.service.OrderService;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//
//public class OrderServiceImplTest {
//
//    @Autowired
//    private OrderService orderService;
//    @Autowired
//    private OrderRepository orderRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private RoleRepository roleRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private SupplierRepository supplierRepository;
//    @Autowired
//    private ModelMapper modelMapper;
//    @Autowired
//    private CartItemRepository cartItemRepository;
//
//
//    UserEntity rootUser;
//    RoleEntity rootRole;
//
//    ProductEntity product;
//
//    CategoryEntity category;
//
//    SupplierEntity supplier;
//
//    CartItemEntity cartItem;
//
//    @Before
//    public void setup() {
//        userRepository.deleteAll();
//        roleRepository.deleteAll();
//        productRepository.deleteAll();
//        categoryRepository.deleteAll();
//        supplierRepository.deleteAll();
//        cartItemRepository.deleteAll();
//
//        rootRole = new RoleEntity("ROLE_ROOT");
//
//        roleRepository.save(rootRole);
//
//        rootUser = new UserEntity();
//        rootUser.setUsername("Bobi")
//                .setEmail("bobi@bobi.bg")
//                .setFullName("Borislav")
//                .setPassword("1234")
//                .setAuthorities(Set.of(rootRole));
//        userRepository.save(rootUser);
//
//        category = new CategoryEntity();
//        category.setName("category")
//                .setImgUrl("some address")
//                .setNameLatin("category")
//                .setPosition(1);
//        categoryRepository.save(category);
//
//        supplier = new SupplierEntity();
//        supplier.setName("TestSupplier-1")
//                .setEmail("TestSupplier@TestSupplier.bg")
//                .setPhoneNumber("123456789")
//                .setAddress("Somewhere")
//                .setPerson("Gosho");
//        supplierRepository.save(supplier);
//
//        product = new ProductEntity();
//        product.setProductName("TestProduct")
//                .setProductNameLatin("TestProduct")
//                .setProductCode("test-1")
//                .setProductPrice(BigDecimal.valueOf(500))
//                .setImgUrl("random image")
//                .setCategory(category)
//                .setSupplier(supplier);
//        productRepository.save(product);
//
//        cartItem = new CartItemEntity();
//        cartItem.setQty(1);
//        cartItem.setUser(rootUser);
//        cartItem.setProduct(product);
//        cartItemRepository.save(cartItem);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void test_createOrder_ThrowsEx() throws ObjectNotFoundException {
//        userRepository.save(rootUser);
//        Assert.assertEquals(1, userRepository.count());
//
//        Optional<UserEntity> byUsername = userRepository.findByUsername(rootUser.getUsername());
//        Assert.assertNotNull(byUsername);
//
//        orderService.createOrder(byUsername.get().getUsername());
//    }
//
//    @Test
//    public void test_CreateOrder() throws ObjectNotFoundException {
//        Assert.assertEquals(1, userRepository.count());
//        Optional<UserEntity> user = userRepository.findByUsername(rootUser.getUsername());
//        Assert.assertNotNull(user);
//
//        Assert.assertEquals(1, productRepository.count());
//        Optional<ProductEntity> byProductCode = productRepository.findByProductCode(product.getProductCode());
//        Assert.assertNotNull(byProductCode);
//
//        Assert.assertEquals(1, cartItemRepository.count());
//        List<CartItemEntity> allByUserUsername = cartItemRepository.findAllByUserUsername(rootUser.getUsername());
//        Assert.assertEquals(1, allByUserUsername.size());
//
//        orderService.createOrder(rootUser.getUsername());
//
//    }
//
//}