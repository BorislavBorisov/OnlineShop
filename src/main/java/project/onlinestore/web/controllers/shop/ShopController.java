package project.onlinestore.web.controllers.shop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.service.CategoryService;
import project.onlinestore.service.ProductService;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public ShopController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/categories")
    public String category(Model model) {
        model.addAttribute("categories", this.categoryService.getCategoriesAllByPosition());
        return "/shop/all-categories";
    }

    @GetMapping("/{nameLatin}")
    public String currentCategory(@PathVariable String nameLatin, Model model) {
        model.addAttribute("products", this.categoryService.getAllProductsByCategoryName(nameLatin));
        return "/shop/all-products";
    }

    @GetMapping("/article/{nameLatin}")
    public String currentProduct(@PathVariable String nameLatin, Model model) {
        ProductViewModel product = this.productService.getProductByNameLatin(nameLatin);
        model.addAttribute("product", product);
        model.addAttribute("similarProducts", this.categoryService.getSimilarProducts(product.getCategory().getNameLatin(), nameLatin));
        return "/shop/details";
    }
}
