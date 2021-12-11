package project.onlinestore.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.binding.QuestionBindingModel;
import project.onlinestore.repository.QuestionRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private QuestionRepository questionRepository;

    @Before
    public void tearDown() {
        questionRepository.deleteAll();
    }

    @Test
    public void get_HomePage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("index"))
                .andExpect(view().name("index"));
    }

    @Test
    public void get_AboutPage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("about"))
                .andExpect(view().name("about"));
    }

    @Test
    public void sendQuestion() throws Exception {
        QuestionBindingModel questionBindingModel = new QuestionBindingModel();
        questionBindingModel.setQuestion("Ima li testove");
        questionBindingModel.setEmail("test@test.bg");
        questionBindingModel.setPhoneNumber("0884596587");

        mockMvc.perform(post("/question")
                        .sessionAttr("question", questionBindingModel)
                        .param("question", questionBindingModel.getQuestion())
                        .param("email", questionBindingModel.getEmail())
                        .param("phoneNumber", questionBindingModel.getPhoneNumber())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("question"))
                .andExpect(redirectedUrl("/"));

        Assert.assertEquals(1, questionRepository.count());
    }

}