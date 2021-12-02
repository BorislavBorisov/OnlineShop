package project.onlinestore.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.onlinestore.domain.binding.QuestionBindingModel;

import javax.validation.Valid;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/question")
    public String question(@Valid QuestionBindingModel question) {
        System.out.println();
        return "redirect:/";
    }

    @ModelAttribute
    public QuestionBindingModel questionBindingModel() {
        return new QuestionBindingModel();
    }
}
