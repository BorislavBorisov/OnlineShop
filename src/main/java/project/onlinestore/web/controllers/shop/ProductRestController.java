package project.onlinestore.web.controllers.shop;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.service.ProductService;

import java.util.List;

@RestController
public class ProductRestController {

    private final ProductService productService;


    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/fetch/products")
    public ResponseEntity<List<ProductViewModel>> findAll() {
        List<ProductViewModel> allProducts = productService.findAllProducts();

        return ResponseEntity
                .ok()
                .body(allProducts);
    }
}
