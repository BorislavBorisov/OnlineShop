package project.onlinestore.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.onlinestore.domain.binding.QuestionBindingModel;
import project.onlinestore.service.QuestionService;

import javax.validation.Valid;

@Controller
public class HomeController {

    private final QuestionService questionService;

    public HomeController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @PostMapping("/question")
    public String question(@Valid QuestionBindingModel question) {
        this.questionService.sendQuestion(question);
        return "redirect:/";
    }

    @ModelAttribute
    public QuestionBindingModel questionBindingModel() {
        return new QuestionBindingModel();
    }
}
