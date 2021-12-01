package project.onlinestore.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.UserServiceModel;
import project.onlinestore.domain.view.UserViewModel;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserServiceModel registerUser(UserServiceModel userServiceModel);

    UserServiceModel findUserByUsername(String username);

    UserServiceModel editUserProfile(UserServiceModel userServiceModel);

    boolean changeProfilePicture(UserServiceModel userServiceModel);

    List<UserViewModel> getAllUsers();

    void setUserRole(Long id, String role);

    void saveUser(UserEntity user);
}
