package project.onlinestore.domain.binding;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserLoginBindingModel {

    private String username;
    private String password;

    public UserLoginBindingModel() {
    }

    @NotNull
    @Size(min = 3, max = 25, message = "Потребителското име трябва да бъде между 3 и 25 символа!")
    public String getUsername() {
        return username;
    }

    public UserLoginBindingModel setUsername(String username) {
        this.username = username;
        return this;
    }

    @NotNull
    @Size(min = 3, max = 25, message = "Паролата трябва да бъде между 3 и 25 символа!")
    public String getPassword() {
        return password;
    }

    public UserLoginBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }
}
