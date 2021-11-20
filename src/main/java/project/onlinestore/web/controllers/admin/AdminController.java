package project.onlinestore.web.controllers.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String moderator() {
        return "admin/admin";
    }

}
