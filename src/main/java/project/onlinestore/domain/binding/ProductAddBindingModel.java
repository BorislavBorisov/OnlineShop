package project.onlinestore.domain.binding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductAddBindingModel {

    private String productCode;
    private String productName;
    private BigDecimal productPrice;
    private String description;
    private String supplier;
    private String category;

    public ProductAddBindingModel() {
    }

    @NotNull
    @NotBlank(message = "Трябва да има някаква стойност")
    @Size(min = 1, max = 55, message = "Кодът на продукта не може да бъде по-малък от 1 и по-голям от 35 символа!")
    public String getProductCode() {
        return productCode;
    }

    public ProductAddBindingModel setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    @NotNull
    @NotBlank(message = "Трябва да има някаква стойност")
    @Size(min = 1, max = 499, message = "Името на продукта не може да бъде по-малко от 1 и по-голямо от 499 символа!")
    public String getProductName() {
        return productName;
    }

    public ProductAddBindingModel setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    @Positive(message = "Цената на продукта не може да бъде отрицателно число")
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public ProductAddBindingModel setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    @Size(max = 1500)
    public String getDescription() {
        return description;
    }

    public ProductAddBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getSupplier() {
        return supplier;
    }

    public ProductAddBindingModel setSupplier(String supplier) {
        this.supplier = supplier;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ProductAddBindingModel setCategory(String category) {
        this.category = category;
        return this;
    }
}
