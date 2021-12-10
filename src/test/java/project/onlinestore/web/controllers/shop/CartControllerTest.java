package project.onlinestore.web.controllers.shop;

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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.binding.UserRegisterBindingModel;
import project.onlinestore.domain.entities.RoleEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.UserServiceModel;
import project.onlinestore.repository.CartItemRepository;
import project.onlinestore.repository.RoleRepository;
import project.onlinestore.repository.UserRepository;
import project.onlinestore.service.UserService;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;

    UserEntity rootUser;
    UserEntity clientUser;

    RoleEntity roleRoot;
    RoleEntity roleClient;

    @Before
    public void setup() {
        this.userRepository.deleteAll();
        this.roleRepository.deleteAll();

        roleRoot = new RoleEntity();
        roleRoot.setAuthority("ROLE_ROOT");

        roleClient = new RoleEntity();
        roleClient.setAuthority("ROLE_CLIENT");
        roleRepository.saveAll(List.of(roleRoot, roleClient));

        rootUser = new UserEntity();
        rootUser.setUsername("root")
                .setFullName("Root Root")
                .setEmail("root@root.bg")
                .setPassword("1234")
                .setAuthorities(Set.of(roleRoot));

        clientUser = new UserEntity();
        clientUser.setUsername("test")
                .setFullName("test testov")
                .setEmail("test@test.bg")
                .setPassword("1234")
                .setAuthorities(Set.of(roleClient));
    }

    @After
    public void clearAll() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void test_ViewCart() throws Exception {
        authenticate();

        this.mockMvc
                .perform(get("/cart"))
                .andExpect(handler().methodName("getCart"))
                .andExpect(status().isOk())
                .andExpect(view().name("/shop/cart"));
    }

    public void authenticate() {
        UserRegisterBindingModel userRegisterBindingModel = new UserRegisterBindingModel();
        userRegisterBindingModel.setUsername("bobi");
        userRegisterBindingModel.setFullName("Borislav");
        userRegisterBindingModel.setEmail("bobi@bobi.bg");
        userRegisterBindingModel.setPassword("1234");
        userRegisterBindingModel.setConfirmPassword("1234");

        if (!this.userService.usernameCheck("bobi")
                && !this.userService.emailCheck("bobi@bobi.bg")) {
            this.userService.registerUser(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));
        }

        UserServiceModel userServiceModel = new UserServiceModel();
        userServiceModel.setUsername("bobi");
        userServiceModel.setPassword("1234");
        this.userService.authenticate(userServiceModel);
    }
}