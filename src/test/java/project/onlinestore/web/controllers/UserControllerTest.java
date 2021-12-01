package project.onlinestore.web.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.entities.RoleEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.repository.RoleRepository;
import project.onlinestore.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testOpenRegisterForm() throws Exception {
        mockMvc
                .perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    private static final String TEST_USER_USERNAME = "test";
    private static final String TEST_USER_FULL_NAME = "test test";
    private static final String TEST_USER_EMAIL = "test@test.bg";

    @Test
    public void registerPasswordsDontMatch() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("email", TEST_USER_EMAIL)
                        .param("password", "123")
                        .param("confirmPassword", "456")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attribute("passwordsDontMatch", true));
    }

    @Test
    public void registerInvalidInput() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "T")
                        .param("email", "abc.com")
                        .param("fullName", "a1")
                        .param("password", "123")
                        .param("confirmPassword", "123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attributeExists("userRegisterBindingModel"));
    }

    @Test
    void testRegisterUser() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", TEST_USER_USERNAME)
                        .param("fullName", TEST_USER_FULL_NAME)
                        .param("email", TEST_USER_EMAIL)
                        .param("password", "1234")
                        .param("confirmPassword", "1234")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/users/login"));

        Assertions.assertEquals(1, userRepository.count());

        Optional<UserEntity> newlyCreatedUserOpt = userRepository.findByEmail(TEST_USER_EMAIL);

        Assertions.assertTrue(newlyCreatedUserOpt.isPresent());

        UserEntity newlyCreatedUser = newlyCreatedUserOpt.get();

        Assertions.assertEquals(TEST_USER_USERNAME, newlyCreatedUser.getUsername());
        Assertions.assertEquals(TEST_USER_EMAIL, newlyCreatedUser.getEmail());
        Assertions.assertEquals(TEST_USER_FULL_NAME, newlyCreatedUser.getFullName());
    }

    @Test
    void testOpenLoginForm() throws Exception {
        mockMvc
                .perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }



//    @Test
//    @WithMockUser(username = "bobi", password = "1234",authorities = {"ROLE_CLIENT"})
//    void testOpenProfile() throws Exception {
//        mockMvc
//                .perform(get("/users/profile"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("profile"));
//    }

}