package project.onlinestore.web.controllers.admin;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.onlinestore.domain.binding.ImageBindingModel;
import project.onlinestore.domain.binding.ProductAddBindingModel;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.service.CategoryService;
import project.onlinestore.service.CloudinaryService;
import project.onlinestore.service.ProductService;
import project.onlinestore.service.SupplierService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class ProductsController {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final SupplierService supplierService;
    private final CategoryService categoryService;

    public ProductsController(ProductService productService, ModelMapper modelMapper,
                              CloudinaryService cloudinaryService, SupplierService supplierService,
                              CategoryService categoryService) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.supplierService = supplierService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String allProducts(Model model) {
        List<ProductViewModel> allProducts = this.productService.findAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "/admin/products/all-products";
    }

    @GetMapping("/products/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String addProduct() {
        return "/admin/products/add-product";
    }

    @PostMapping("/products/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String addProductConfirm(@Valid ProductAddBindingModel productAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel", bindingResult);
            return "redirect:/admin/products/add";
        }

        ProductServiceModel productServiceModel = this.modelMapper.map(productAddBindingModel, ProductServiceModel.class);
        productServiceModel.setCategory(
                this.categoryService.findCategoryById(Long.parseLong(productAddBindingModel.getCategory()))
        );
        productServiceModel.setSupplier(
                this.supplierService.findSupplierById(Long.parseLong(productAddBindingModel.getSupplier()))
        );

        this.productService.addProduct(productServiceModel);
        return "redirect:/admin/products";
    }


    @GetMapping("/products/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", this.modelMapper
                .map(this.productService.findProductById(id), ProductViewModel.class));

        return "/admin/products/edit-product";
    }


    @PostMapping("/products/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String editProductConfirm(@PathVariable Long id, @Valid ProductAddBindingModel productAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel", bindingResult);

            return "redirect:" + id;
        }
        ProductServiceModel map = this.modelMapper.map(productAddBindingModel, ProductServiceModel.class);
        this.productService.editProduct(id, map);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String deleteProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", this.modelMapper.map(this.productService.findProductById(id), ProductViewModel.class));
        return "/admin/products/delete-product";
    }

    @PostMapping("/products/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String deleteProductConfirm(@PathVariable Long id) {
        this.productService.deleteProduct(id);
        return "redirect:/admin/products";
    }


    @GetMapping("/products/edit/image/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String editProductImage(@PathVariable Long id, Model model) {
        model.addAttribute("product", this.modelMapper.map(this.productService.findProductById(id), ProductViewModel.class));
        return "/admin/products/edit-image-product";
    }

    @PostMapping("/products/edit/image/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ROOT')")
    public String editProductImageConfirm(@PathVariable Long id, ImageBindingModel imageBindingModel) throws IOException {
        ProductServiceModel product = this.productService.findProductById(id);
        product.setImgUrl(this.cloudinaryService.uploadImage(imageBindingModel.getImage()));
        this.productService.editImageCategory(product);
        return "redirect:/admin/products";
    }

    @ModelAttribute
    public ImageBindingModel imageBindingModel(){ return new ImageBindingModel(); }

    @ModelAttribute
    public ProductAddBindingModel productAddBindingModel() {
        return new ProductAddBindingModel();
    }
}
