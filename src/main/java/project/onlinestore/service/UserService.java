package project.onlinestore.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.onlinestore.domain.service.UserServiceModel;

public interface UserService extends UserDetailsService {

    UserServiceModel registerUser(UserServiceModel userServiceModel);
}
