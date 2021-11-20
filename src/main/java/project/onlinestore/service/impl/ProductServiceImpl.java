package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.view.ProductViewModel;
import project.onlinestore.repository.ProductRepository;
import project.onlinestore.service.CategoryService;
import project.onlinestore.service.ProductService;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductViewModel> findAllProducts() {
        return this.productRepository
                .findAll()
                .stream()
                .map(p -> this.modelMapper.map(p, ProductViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductServiceModel addProduct(ProductServiceModel productServiceModel) {
        ProductEntity byName = this.productRepository.findByProductName(productServiceModel.getProductName())
                .orElse(null);

        ProductEntity byCode = this.productRepository.findByProductCode(productServiceModel.getProductName())
                .orElse(null);

        if (byName == null && byCode == null) {
            ProductEntity product = this.modelMapper.map(productServiceModel, ProductEntity.class);
            return this.modelMapper.map(this.productRepository.save(product), ProductServiceModel.class);
        }

        throw new IllegalArgumentException("Something went wrong!");
    }

    @Override
    public ProductServiceModel findProductById(Long id) {
        ProductEntity product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID!"));

        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public ProductServiceModel editProduct(Long id, ProductServiceModel productServiceModel) {
        ProductEntity product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID!"));

        product.setProductName(productServiceModel.getProductName())
                .setProductCode(productServiceModel.getProductCode())
                .setBarcode(productServiceModel.getBarcode())
                .setProductPrice(productServiceModel.getProductPrice())
                .setDescription(productServiceModel.getDescription())
                .setModified(Instant.now());

        return this.modelMapper.map(this.productRepository.save(product), ProductServiceModel.class);

    }

    @Override
    public boolean deleteProduct(Long id) {
        ProductEntity product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID!"));

        try {
            this.productRepository.delete(product);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public boolean editImageCategory(ProductServiceModel productServiceModel) {
        ProductEntity product = this.productRepository.findById(productServiceModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID!"));

        try {
            product.setImgUrl(productServiceModel.getImgUrl());
            product.setModified(Instant.now());
            this.productRepository.save(product);
            return true;
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid product ID!");
        }
    }
}
