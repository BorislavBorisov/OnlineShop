package project.onlinestore.web.controllers.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.onlinestore.service.CategoryService;

@Controller
@RequestMapping("/shop")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String category(Model model) {
        model.addAttribute("categories", this.categoryService.getCategoriesAllByPosition());
        return "categories";
    }
}
