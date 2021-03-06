package project.onlinestore.web.controllers.shop;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.service.CategoryService;
import project.onlinestore.service.PagesViewCountService;
import project.onlinestore.service.ProductService;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final PagesViewCountService pagesViewCountService;

    public ShopController(CategoryService categoryService, ProductService productService, ModelMapper modelMapper, PagesViewCountService pagesViewCountService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.pagesViewCountService = pagesViewCountService;
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
        ProductServiceModel product = this.productService.findProductByNameLatin(nameLatin);
        model.addAttribute("product", this.modelMapper.map(product, ProductViewModel.class));
        model.addAttribute("stats", pagesViewCountService.getPageViews("/shop/article/" + nameLatin));
        model.addAttribute("similarProducts", this.categoryService.getSimilarProducts(product.getCategory().getNameLatin(), nameLatin));
        return "/shop/details";
    }



}
