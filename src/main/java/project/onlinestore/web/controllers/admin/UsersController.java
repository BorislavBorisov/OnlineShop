package project.onlinestore.web.controllers.admin;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.onlinestore.domain.service.RoleServiceModel;
import project.onlinestore.domain.view.UserViewModel;
import project.onlinestore.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class UsersController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UsersController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String allUsers(Model model) {
        model.addAttribute("allUsers", this.userService.getAllUsers());
        return "/admin/users/all-users";
    }

    @PostMapping("/users/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String setAdmin(@PathVariable Long id) {
        this.userService.setUserRole(id, "admin");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String setModerator(@PathVariable Long id) {
        this.userService.setUserRole(id, "moderator");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/set-client/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String setClient(@PathVariable Long id) {
        this.userService.setUserRole(id, "client");
        return "redirect:/admin/users";
    }
}
