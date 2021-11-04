package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    private String productCode;
    private String productName;
    private BigDecimal productPrice;
    private SupplierEntity supplier;
    private CategoryEntity category;

    public ProductEntity() {
    }

    @Basic
    @Column(name = "product_code", nullable = false, unique = true, length = 55)
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Basic
    @Column(name = "product_name", nullable = false, unique = true, length = 121)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Basic
    @Column(name = "product_price", nullable = false)
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    public SupplierEntity getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierEntity supplier) {
        this.supplier = supplier;
    }

    @ManyToOne
    @JoinColumn(name = "categoty_id")
    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
