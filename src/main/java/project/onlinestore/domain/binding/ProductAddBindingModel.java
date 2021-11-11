package project.onlinestore.domain.binding;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductAddBindingModel {

    private String productCode;
    private String productName;
    private String barcode;
    private MultipartFile image;
    private BigDecimal productPrice;
    private String supplier;
    private String category;

    public ProductAddBindingModel() {
    }

    @NotNull
    @NotBlank
    @Size(min = 1, max = 35, message = "Кодът на продукта не може да бъде по-малък от 1 и по-голям от 35 символа!")
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @NotNull
    @NotBlank
    @Size(min = 5, max = 155, message = "Името на продукта не може да бъде по-малко от 5 и по-голямо от 155 символа!")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Size(max = 50, message = "Баркода на продукта не може да бъде по-голям от 50 символа!")
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    @Positive(message = "Цената на продукта не може да бъде отрицателно число")
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
