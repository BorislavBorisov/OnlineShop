package project.onlinestore.web.controllers.admin;

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
import project.onlinestore.service.CloudinaryService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoriesController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    public CategoriesController(CategoryService categoryService, ModelMapper modelMapper, CloudinaryService cloudinaryService) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/categories")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ROOT')")
    public String allCategories(Model model) {
        model.addAttribute("allCategories", this.categoryService.getAllCategories());
        return "/admin/categories/all-categories";
    }

    @GetMapping("/categories/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String addCategory() {
        return "/admin/categories/add-category";
    }

    @PostMapping("/categories/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String addCategoryConfirm(@Valid CategoryAddBindingModel categoryAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("categoryAddBindingModel", categoryAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryAddBindingModel", bindingResult);

            return "redirect:add";
        }
        CategoryServiceModel categoryServiceModel = this.modelMapper.map(categoryAddBindingModel, CategoryServiceModel.class);
        categoryServiceModel.setImgUrl(this.cloudinaryService.uploadImage(categoryAddBindingModel.getImage()));
        this.categoryService.addCategory(categoryServiceModel);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", this.modelMapper.map(this.categoryService.findCategoryById(id), CategoryViewModel.class));
        return "/admin/categories/edit-category";
    }

    @PostMapping("/categories/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String editCategoryConfirm(@PathVariable Long id, @Valid CategoryAddBindingModel categoryAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("categoryAddBindingModel", categoryAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryAddBindingModel", bindingResult);

            return "redirect:" + id;
        }
        CategoryServiceModel map = this.modelMapper.map(categoryAddBindingModel, CategoryServiceModel.class);
        this.categoryService.editCategory(id, map);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/image/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String editImageCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", this.modelMapper.map(this.categoryService.findCategoryById(id), CategoryViewModel.class));
        return "/admin/categories/edit-image-category";
    }

    @PostMapping("/categories/edit/image/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String editImageCategoryConfirm(@PathVariable Long id, CategoryAddBindingModel categoryAddBindingModel) throws IOException {
        CategoryServiceModel category = this.categoryService.findCategoryById(id);
        category.setImgUrl(this.cloudinaryService.uploadImage(categoryAddBindingModel.getImage()));
        this.categoryService.editImageCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("categories/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String deleteCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", this.modelMapper.map(this.categoryService.findCategoryById(id), CategoryViewModel.class));
        return "/admin/categories/delete-category";
    }

    @PostMapping("categories/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String deleteCategoryConfirm(@PathVariable Long id) {
        this.categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    @ResponseBody
    public List<CategoryViewModel> fetchCategories() {
        return this.categoryService.getAllCategories();
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
