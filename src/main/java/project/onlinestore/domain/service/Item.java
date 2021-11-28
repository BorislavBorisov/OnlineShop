package project.onlinestore.domain.service;

import project.onlinestore.domain.view.ProductViewModel;

import java.math.BigDecimal;

public class Item {

    private ProductViewModel product;
    private int qty;
    private BigDecimal currentSumOfProduct;

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

    public BigDecimal getCurrentSumOfProduct() {
        return product.getProductPrice().multiply(BigDecimal.valueOf(qty));
    }

    public void setCurrentSumOfProduct(BigDecimal currentSumOfProduct) {
        this.currentSumOfProduct = currentSumOfProduct;
    }

}
