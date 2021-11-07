package project.onlinestore.domain.binding;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRegisterBindingModel {

    private String username;
    private String fullName;
    private String password;
    private String confirmPassword;
    private String email;

    public UserRegisterBindingModel() {

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
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull
    @Size(min = 3, max = 25, message = "Паролата трябва да бъде между 3 и 25 символа!")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    @NotNull
    @Size(min = 3, max = 25, message = "Името трябва да бъде между 3 и 25 символа!")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
