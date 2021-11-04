package project.onlinestore.web.controllers.moderator;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/moderator")
public class ProductsController {

    @GetMapping("/products")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String allProducts() {
        return "/moderator/products/all-products";
    }

}
