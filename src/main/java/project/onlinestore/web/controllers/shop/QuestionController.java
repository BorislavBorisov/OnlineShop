package project.onlinestore.web.controllers.shop;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestionController {

    @GetMapping("/questions")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT') or hasRole('ROLE_MODERATOR')")
    public String get(){
        return "questions";
    }

}
