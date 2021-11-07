package project.onlinestore.domain.binding;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserEditBindingModel {

    private String username;
    private String fullName;
    private String email;
    private String firstAddress;
    private String phoneNumber;
    private String country;
    private String city;
    private MultipartFile image;

    public UserEditBindingModel() {
    }

    @NotNull
    @Size(min = 3, max = 25, message = "Потребителското име трябва да бъде между 3 и 25 символа!")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull
    @Size(min = 3, max = 25, message = "Паролата трябва да бъде между 3 и 25 символа!")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @NotNull
    @Email(message = "Въведете валиден имейл адрес!")
    @Size(min = 3, max = 25, message = "Имейлът трябва да бъде между 3 и 25 символа!")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Size(max = 60, message = "Само 60 символа са позволени!")
    public String getFirstAddress() {
        return firstAddress;
    }

    public void setFirstAddress(String firstAddress) {
        this.firstAddress = firstAddress;
    }

    @Size(max = 10, message = "Само 10 символа са позволени!")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Size(max = 10, message = "Само 20 символа са позволени!")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Size(max = 10, message = "Само 20 символа са позволени!")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
