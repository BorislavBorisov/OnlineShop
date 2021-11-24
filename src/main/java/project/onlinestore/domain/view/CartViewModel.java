package project.onlinestore.domain.view;

import project.onlinestore.domain.service.ProductServiceModel;

import java.math.BigDecimal;
import java.util.List;

public class CartViewModel {

    private Long id;
    private List<ProductServiceModel> products;
    private Integer count;
    private BigDecimal totalPrice;

    public CartViewModel() {
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
