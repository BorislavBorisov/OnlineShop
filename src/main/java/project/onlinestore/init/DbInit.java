package project.onlinestore.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.onlinestore.service.CategoryService;
import project.onlinestore.service.ProductService;
import project.onlinestore.service.RoleService;
import project.onlinestore.service.SupplierService;

@Component
public class DbInit implements CommandLineRunner {

    private final RoleService roleService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final ProductService productService;

    public DbInit(RoleService roleService, CategoryService categoryService,
                  SupplierService supplierService, ProductService productService) {
        this.roleService = roleService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
//        this.roleService.seedRolesInDb();
//        this.categoryService.seedCategories();
//        this.supplierService.seedSuppliers();
//        this.productService.seedProducts();
    }
}
