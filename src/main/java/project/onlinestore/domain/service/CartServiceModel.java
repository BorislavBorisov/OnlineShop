package project.onlinestore.domain.service;

import java.math.BigDecimal;
import java.util.List;

public class CartServiceModel {

    private List<ProductServiceModel> products;
    private Integer count;
    private BigDecimal totalPrice;

    public CartServiceModel() {
    }

    public List<ProductServiceModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductServiceModel> products) {
        this.products = products;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
