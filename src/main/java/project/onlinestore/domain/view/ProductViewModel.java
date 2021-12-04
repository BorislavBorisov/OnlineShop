package project.onlinestore.domain.view;

import java.math.BigDecimal;

public class ProductViewModel {

    private Long id;
    private String productCode;
    private String productName;
    private String productNameLatin;
    private String imgUrl;
    private BigDecimal productPrice;
    private String description;
    private SupplierViewModel supplier;
    private CategoryViewModel category;

    public ProductViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getProductNameLatin() {
        return productNameLatin;
    }

    public void setProductNameLatin(String productNameLatin) {
        this.productNameLatin = productNameLatin;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public SupplierViewModel getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierViewModel supplier) {
        this.supplier = supplier;
    }

    public CategoryViewModel getCategory() {
        return category;
    }

    public void setCategory(CategoryViewModel category) {
        this.category = category;
    }
}
