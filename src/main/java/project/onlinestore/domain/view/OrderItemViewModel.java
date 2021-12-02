package project.onlinestore.domain.view;

import java.math.BigDecimal;

public class OrderItemViewModel {

    private Long productId;
    private String productName;
    private String productNameLatin;
    private String productImg;
    private BigDecimal productPrice;
    private BigDecimal totalPrice;
    private Integer qty;

    public OrderItemViewModel() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameLatin() {
        return productNameLatin;
    }

    public void setProductNameLatin(String productNameLatin) {
        this.productNameLatin = productNameLatin;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
