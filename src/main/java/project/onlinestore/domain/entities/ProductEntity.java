package project.onlinestore.domain.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {

    private String productCode;
    private String productName;
    private String barcode;
    private String imgUrl;
    private Integer inStock;
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

    public ProductEntity setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    @Basic
    @Column(name = "product_name", nullable = false, unique = true, length = 121)
    public String getProductName() {
        return productName;
    }

    public ProductEntity setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    @Basic
    @Column(name = "product_barcode", length = 121)
    public String getBarcode() {
        return barcode;
    }

    public ProductEntity setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    @Basic
    @Column(name = "product_image", nullable = false)
    public String getImgUrl() {
        return imgUrl;
    }

    public ProductEntity setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    @Basic
    @Column(name = "in_stock")
    public Integer getInStock() {
        return inStock;
    }

    public ProductEntity setInStock(Integer inStock) {
        this.inStock = inStock;
        return this;
    }

    @Basic
    @Column(name = "product_price", nullable = false)
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public ProductEntity setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    public SupplierEntity getSupplier() {
        return supplier;
    }

    public ProductEntity setSupplier(SupplierEntity supplier) {
        this.supplier = supplier;
        return this;
    }

    @ManyToOne
    @JoinColumn(name = "categoty_id")
    public CategoryEntity getCategory() {
        return category;
    }

    public ProductEntity setCategory(CategoryEntity category) {
        this.category = category;
        return this;
    }
}
