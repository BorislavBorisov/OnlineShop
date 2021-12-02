package project.onlinestore.domain.service;

import java.math.BigDecimal;
import java.util.List;

public class CartServiceModel {

    private Long id;
    private List<ProductServiceModel> products;
    private UserServiceModel user;
    private BigDecimal totalSum;

    public CartServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductServiceModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductServiceModel> products) {
        this.products = products;
    }

    public UserServiceModel getUser() {
        return user;
    }

    public void setUser(UserServiceModel user) {
        this.user = user;
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }
}
