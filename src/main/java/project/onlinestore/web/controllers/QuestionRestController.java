package project.onlinestore.web.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.onlinestore.domain.view.QuestionViewModel;
import project.onlinestore.service.QuestionService;

import java.util.List;

@RestController
public class QuestionRestController {

    private final QuestionService questionService;

    public QuestionRestController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/api/questions")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public ResponseEntity<List<QuestionViewModel>> getAllQuestions() {

        List<QuestionViewModel> allQuestions = this.questionService.getAllQuestions();

        return ResponseEntity
                .ok()
                .body(allQuestions);
    }
}
