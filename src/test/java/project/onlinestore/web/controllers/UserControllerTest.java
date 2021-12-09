package project.onlinestore.web.controllers;

import org.junit.runner.RunWith;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.binding.UserRegisterBindingModel;
import project.onlinestore.domain.entities.RoleEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.UserServiceModel;
import project.onlinestore.repository.RoleRepository;
import project.onlinestore.repository.UserRepository;
import project.onlinestore.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
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

    @Test
    @WithMockUser
    public void test_OpenLoginForm() throws Exception {
        this.mockMvc
                .perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void test_OpenRegisterForm() throws Exception {
        mockMvc.
                perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void test_RegisterUser_PasswordsDontMatch() throws Exception {
        mockMvc
                .perform(post("/users/register")
                        .param("fullName", "test testov")
                        .param("username", "test")
                        .param("email", "test@test.bg")
                        .param("password", "1234")
                        .param("confirmPassword", "12345")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("registerConfirm"))
                .andExpect(flash().attributeExists("passwordsDontMatch"))
                .andExpect(redirectedUrl("/users/register"));
    }

    @Test
    public void test_RegisterUser_UsernameExists() throws Exception {
        userRepository.save(clientUser);
        Assert.assertEquals(1, userRepository.count());

        mockMvc
                .perform(post("/users/register")
                        .param("fullName", "test testov")
                        .param("username", "test")
                        .param("email", "test@testtt.bg")
                        .param("password", "12345")
                        .param("confirmPassword", "12345")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("registerConfirm"))
                .andExpect(flash().attributeExists("usernameExists"))
                .andExpect(redirectedUrl("/users/register"));
    }

    @Test
    public void test_RegisterUser_EmailExists() throws Exception {
        userRepository.save(clientUser);
        mockMvc
                .perform(post("/users/register")
                        .param("fullName", "test testov")
                        .param("username", "testtest")
                        .param("email", "test@test.bg")
                        .param("password", "12345")
                        .param("confirmPassword", "12345")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("registerConfirm"))
                .andExpect(flash().attributeExists("emailExists"))
                .andExpect(redirectedUrl("/users/register"));
    }

    @Test
    public void test_RegisterUser_InvalidInputs() throws Exception {
        mockMvc
                .perform(post("/users/register")
                        .param("fullName", "te")
                        .param("username", "te")
                        .param("email", "testest.bg")
                        .param("password", "1")
                        .param("confirmPassword", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("registerConfirm"))
                .andExpect(flash().attributeExists("userRegisterBindingModel"))
                .andExpect(redirectedUrl("/users/register"));
    }

    @Test
    public void test_RegisterUser() throws Exception {
        mockMvc
                .perform(post("/users/register")
                        .param("fullName", "test testov")
                        .param("username", "test")
                        .param("email", "test@test.bg")
                        .param("password", "12345")
                        .param("confirmPassword", "12345")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("registerConfirm"))
                .andExpect(redirectedUrl("/users/login"));

        assertEquals(1, userRepository.count());

        Optional<UserEntity> newlyCreatedUserOpt = userRepository.findByUsername("test");
        assertTrue(newlyCreatedUserOpt.isPresent());

        UserEntity newlyCreatedUser = newlyCreatedUserOpt.get();
        assertEquals("test testov", newlyCreatedUser.getFullName());
        assertEquals("test@test.bg", newlyCreatedUser.getEmail());
        assertEquals("test", newlyCreatedUser.getUsername());
    }

    @Test
    @WithMockUser
    public void test_loadProfilePage() throws Exception {
        authenticate();
        this.mockMvc
                .perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("/users/user-profile"));
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
