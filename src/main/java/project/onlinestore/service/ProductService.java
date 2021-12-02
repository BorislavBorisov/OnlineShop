package project.onlinestore.service;

import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.view.ProductViewModel;

import java.util.List;

public interface ProductService {

    List<ProductViewModel> findAllProducts();

    ProductServiceModel addProduct(ProductServiceModel productServiceModel);

    ProductServiceModel findProductById(Long id);

    ProductServiceModel editProduct(Long id, ProductServiceModel productServiceModel);

    boolean deleteProduct(Long id);

    boolean editImageCategory(ProductServiceModel productServiceModel);

    ProductServiceModel findProductByNameLatin(String nameLatin);

    ProductEntity findProductByName(String key);

    void seedProducts();
}
