package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.CartEntity;
import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.repository.CartRepository;
import project.onlinestore.repository.ProductRepository;
import project.onlinestore.service.CartService;
import project.onlinestore.service.ProductService;
import project.onlinestore.service.UserService;

import java.time.Instant;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final UserService userService;
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(UserService userService, CartRepository cartRepository,
                           ProductService productService, ProductRepository productRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addProductToCart(Long id, UserDetails principal, Long cartId) {
        UserEntity user = this.modelMapper.map(
                this.userService.findUserByUsername(principal.getUsername()), UserEntity.class);

        if (cartId == null) {
            CartEntity cartEntity = new CartEntity();
            cartEntity.setUser(user);
            cartEntity.setRegistered(Instant.now());

            ProductEntity product = this.modelMapper.map(
                    this.productService.findProductById(id), ProductEntity.class
            );
            product.setCount(1);
            cartEntity.getProducts().add(product);

            this.cartRepository.save(cartEntity);
            user.setCartEntity(cartEntity);
            this.userService.saveUser(user);
        } else {
            CartEntity cartEntity = this.cartRepository.findById(cartId)
                    .orElse(null);

            ProductEntity product = this.modelMapper.map(
                    this.productService.findProductById(id), ProductEntity.class
            );

            if (cartEntity != null) {
                for (ProductEntity productEntity : cartEntity.getProducts()) {
                    if (productEntity.getId().equals(product.getId())) {
                        productEntity.setCount(productEntity.getCount() + 1);
                    }
                }
            } else {
                throw new IllegalArgumentException("Нещо стана!");
            }


        }
    }


}
