package project.onlinestore.domain.service;

import java.math.BigDecimal;

public class ProductServiceModel extends BaseServiceModel {

    private String productCode;
    private String productName;
    private String productNameLatin;
    private String imgUrl;
    private BigDecimal productPrice;
    private String description;
    private SupplierServiceModel supplier;
    private CategoryServiceModel category;

    public ProductServiceModel() {
    }

    public String getProductCode() {
        return productCode;
    }

    public ProductServiceModel setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public ProductServiceModel setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductNameLatin() {
        return productNameLatin;
    }

    public ProductServiceModel setProductNameLatin(String productNameLatin) {
        this.productNameLatin = productNameLatin;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public ProductServiceModel setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public ProductServiceModel setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public SupplierServiceModel getSupplier() {
        return supplier;
    }

    public ProductServiceModel setSupplier(SupplierServiceModel supplier) {
        this.supplier = supplier;
        return this;
    }

    public CategoryServiceModel getCategory() {
        return category;
    }

    public ProductServiceModel setCategory(CategoryServiceModel category) {
        this.category = category;
        return this;
    }
}
