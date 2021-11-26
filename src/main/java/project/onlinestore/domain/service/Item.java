package project.onlinestore.domain.service;

import project.onlinestore.domain.view.ProductViewModel;

import java.math.BigDecimal;

public class Item {

    private ProductViewModel product;
    private int qty;
    private BigDecimal totalSum;

    public Item() {
    }

    public Item(ProductViewModel product, int qty) {
        this.product = product;
        this.qty = qty;
    }

    public ProductViewModel getProduct() {
        return product;
    }

    public void setProduct(ProductViewModel product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getTotalSum() {
        return product.getProductPrice().multiply(BigDecimal.valueOf(qty));
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

}
