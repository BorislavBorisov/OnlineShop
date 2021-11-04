package project.onlinestore.web.controllers.moderator;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.onlinestore.domain.binding.CategoryAddBindingModel;
import project.onlinestore.domain.service.CategoryServiceModel;
import project.onlinestore.domain.view.CategoryViewModel;
import project.onlinestore.service.CategoryService;

import javax.validation.Valid;

@Controller
@RequestMapping("/moderator")
public class CategoriesController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoriesController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String allCategories(Model model) {
        model.addAttribute("allCategories", this.categoryService.getAllCategories());
        return "/moderator/categories/all-categories";
    }

    @GetMapping("/categories/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String addCategory() {
        return "/moderator/categories/add-category";
    }

    @PostMapping("/categories/add")
    public String addCategoryConfirm(@Valid CategoryAddBindingModel categoryAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("categoryAddBindingModel", categoryAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryAddBindingModel", bindingResult);

            return "redirect:add";
        }

        this.categoryService.addCategory(this.modelMapper.map(categoryAddBindingModel, CategoryServiceModel.class));
        return "redirect:/moderator/categories";
    }

    @GetMapping("/categories/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", this.modelMapper.map(this.categoryService.findCategoryById(id), CategoryViewModel.class));
        return "/moderator/categories/edit-category";
    }

    @PostMapping("/categories/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String editCategoryConfirm(@PathVariable Long id, @Valid CategoryAddBindingModel categoryAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("categoryAddBindingModel", categoryAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryAddBindingModel", bindingResult);

            return "redirect:" + id;
        }
        CategoryServiceModel map = this.modelMapper.map(categoryAddBindingModel, CategoryServiceModel.class);
        this.categoryService.editCategory(id, map);
        return "redirect:/moderator/categories";
    }

    @GetMapping("categories/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String deleteCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", this.modelMapper.map(this.categoryService.findCategoryById(id), CategoryViewModel.class));
        return "/moderator/categories/delete-category";
    }

    @PostMapping("categories/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String deleteCategoryConfirm(@PathVariable Long id) {
        this.categoryService.deleteCategory(id);
        return "redirect:/moderator/categories";
    }


    @ModelAttribute
    public CategoryViewModel categoryViewModel() {
        return new CategoryViewModel();
    }

    @ModelAttribute
    public CategoryAddBindingModel categoryAddBindingModel() {
        return new CategoryAddBindingModel();
    }
}
