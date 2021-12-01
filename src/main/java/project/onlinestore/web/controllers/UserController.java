package project.onlinestore.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.onlinestore.domain.binding.ImageBindingModel;
import project.onlinestore.domain.binding.UserEditBindingModel;
import project.onlinestore.domain.binding.UserRegisterBindingModel;
import project.onlinestore.domain.service.UserServiceModel;
import project.onlinestore.service.CloudinaryService;
import project.onlinestore.service.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    public UserController(UserService userService, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("passwordsDontMatch", true);
            return "redirect:register";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel", bindingResult);

            return "redirect:register";
        }


        this.userService.registerUser(this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class));

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-error")
    public String failedLogin(@ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                                      String userName, RedirectAttributes attributes) {

        attributes.addFlashAttribute("bad_credentials", true);

        return "redirect:/users/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Principal principal, Model model) {
        model.addAttribute("user", this.modelMapper.map(
                this.userService.findUserByUsername(principal.getName()), UserServiceModel.class
        ));
        return "/users/user-profile";
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profileUpdateConfirm(@Valid UserEditBindingModel userEditBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        UserServiceModel userServiceModel = this.modelMapper.map(userEditBindingModel, UserServiceModel.class);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userEditBindingModel", userEditBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userEditBindingModel", bindingResult);

            return "redirect:profile";
        }

        this.userService.editUserProfile(userServiceModel);

        return "redirect:/shop/categories";
    }

    @PostMapping("/profile/edit-picture")
    @PreAuthorize("isAuthenticated()")
    public String profilePictureUpdateConfirm(Principal principal, ImageBindingModel imageBindingModel) throws IOException {
        if (!imageBindingModel.getImage().isEmpty()) {
            UserServiceModel user = this.userService.findUserByUsername(principal.getName());
            System.out.println();
            user.setImgUrl(this.cloudinaryService.uploadImage(imageBindingModel.getImage()));
            this.userService.changeProfilePicture(user);

            return "redirect:profile";
        }
        return "redirect:/shop/categories";
    }


    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @ModelAttribute
    public UserEditBindingModel userEditBindingModel() {
        return new UserEditBindingModel();
    }

}