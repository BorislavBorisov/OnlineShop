package project.onlinestore.domain.view;

import java.math.BigDecimal;

public class CartViewModel {

    private Long id;
    private ProductViewModel product;
    private Integer qty;
    private BigDecimal sumForProduct;

    public CartViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductViewModel getProduct() {
        return product;
    }

    public void setProduct(ProductViewModel product) {
        this.product = product;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getSumForProduct() {
        return sumForProduct = this.product.getProductPrice().multiply(BigDecimal.valueOf(this.qty));
    }
}
