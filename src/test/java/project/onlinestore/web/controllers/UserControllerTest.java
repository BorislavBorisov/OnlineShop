package project.onlinestore.web.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.repository.RoleRepository;
import project.onlinestore.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void roles() {
        initMockMvc();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }


    @Test
    void test_OpenLoginForm() throws Exception {
        mockMvc.
                perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void test_OpenRegisterForm() throws Exception {
        mockMvc.
                perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void test_RegisterUser_ReturnsOk() throws Exception {
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


    private void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

}