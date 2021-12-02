package project.onlinestore.web.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.repository.UserRepository;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    void testOpenLoginForm() throws Exception {
        mockMvc.
                perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testOpenRegisterForm() throws Exception {
        mockMvc.
                perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    private final String TEST_USER_USERNAME = "test";
    private final String TEST_USER_EMAIL = "test@test.bg";
    private final String TEST_USER_FILL_NAME = "test test";

    @Test
    void testRegisterUser() throws Exception {
        mockMvc
                .perform(post("/users/register")
                .param("username", TEST_USER_USERNAME)
                .param("email", TEST_USER_EMAIL)
                .param("fullName", TEST_USER_FILL_NAME)
                .param("password", "1234")
                .param("confirmPassword", "1234")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(1, userRepository.count());
        Optional<UserEntity> newlyCreatedUserOpt = userRepository.findByEmail(TEST_USER_EMAIL);

        Assertions.assertTrue(newlyCreatedUserOpt.isPresent());
        UserEntity newlyCreatedUser = newlyCreatedUserOpt.get();
        Assertions.assertEquals(TEST_USER_EMAIL, newlyCreatedUser.getEmail());
    }

}