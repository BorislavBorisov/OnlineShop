package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.CartEntity;
import project.onlinestore.domain.entities.ProductEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.CartServiceModel;
import project.onlinestore.domain.service.ProductServiceModel;
import project.onlinestore.domain.view.CartViewModel;
import project.onlinestore.repository.CartRepository;
import project.onlinestore.service.CartService;
import project.onlinestore.service.ProductService;
import project.onlinestore.service.UserService;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public CartServiceImpl(CartRepository cartRepository, ProductService productService, ModelMapper modelMapper, UserService userService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public CartServiceModel addProductToCart(String productNameLatin, Integer qty, String username) {
        ProductServiceModel product = this.productService.findProductByNameLatin(productNameLatin);
        CartEntity isExist = this.cartRepository.findCartEntityByUser(this.modelMapper.map(
                this.userService.findUserByUsername(username), UserEntity.class)
        );

        if (isExist == null) {
            CartEntity cartEntity = new CartEntity();
            cartEntity.getProducts().put(productNameLatin, qty);
            cartEntity.setUser(this.modelMapper.map(this.userService.findUserByUsername(username), UserEntity.class));
            cartEntity.setRegistered(Instant.now());
            cartEntity.setActive(true);
            cartEntity.setCount(cartEntity.getProducts()
                    .values()
                    .stream()
                    .reduce(0, Integer::sum));

//            BigDecimal sum = new BigDecimal(0);
//            for (String p : cartEntity.getProducts().keySet()) {
//                ProductServiceModel productByNameLatin = this.productService.findProductByNameLatin(p);
//                sum.add(productByNameLatin.getProductPrice());
//            }

            return this.modelMapper.map(this.cartRepository.save(cartEntity), CartServiceModel.class);
        }

//        isExist.getProducts().add(this.modelMapper.map(product, ProductEntity.class));
        return this.modelMapper.map(this.cartRepository.save(isExist), CartServiceModel.class);
    }

    @Override
    public CartViewModel getCartByUserName(String name) {
        return this.modelMapper.map(
                this.cartRepository.findCartByUserUsername(name), CartViewModel.class);
    }
}
