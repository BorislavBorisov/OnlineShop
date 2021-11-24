package project.onlinestore.domain.service;

import java.math.BigDecimal;

public class ProductServiceModel extends BaseServiceModel {

    private String productCode;
    private String productName;
    private String productNameLatin;
    private String imgUrl;
    private Integer inStock;
    private BigDecimal productPrice;
    private String description;
    private SupplierServiceModel supplier;
    private CategoryServiceModel category;

    public ProductServiceModel() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SupplierServiceModel getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierServiceModel supplier) {
        this.supplier = supplier;
    }

    public CategoryServiceModel getCategory() {
        return category;
    }

    public void setCategory(CategoryServiceModel category) {
        this.category = category;
    }
}
