package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.CartEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.RoleServiceModel;
import project.onlinestore.domain.service.UserServiceModel;
import project.onlinestore.domain.view.UserViewModel;
import project.onlinestore.repository.UserRepository;
import project.onlinestore.service.RoleService;
import project.onlinestore.service.UserService;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {
//        UserEntity username = this.userRepository.findByUsername(userServiceModel.getUsername())
//                .orElse(null);
//
//        if (username != null) {
//            throw new IllegalArgumentException("Потребителско име е заето!");
//        }
//
//        UserEntity userEmail = this.userRepository.findByEmail(userServiceModel.getEmail())
//                .orElse(null);
//
//        if (userEmail != null) {
//            throw new IllegalArgumentException("Вече съществува протебител със същият имейл адрес!");
//        }

        if (userRepository.count() == 0) {
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_ROOT"));
        } else {
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_CLIENT"));
        }

        UserEntity user = this.modelMapper.map(userServiceModel, UserEntity.class);
        user.setImgUrl("https://res.cloudinary.com/foncho/image/upload/v1636206196/avataaars_dztnrw.svg");
        user.setPassword(new BCryptPasswordEncoder().encode(userServiceModel.getPassword()));
        user.setRegistered(Instant.now());
        return this.modelMapper.map(this.userRepository.save(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel findUserByUsername(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        return this.modelMapper.map(userEntity, UserServiceModel.class);

    }

    @Override
    public UserServiceModel editUserProfile(UserServiceModel userServiceModel) {
        UserEntity user = this.userRepository.findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        user.setFirstAddress(userServiceModel.getFirstAddress())
                .setPhoneNumber(userServiceModel.getPhoneNumber())
                .setCountry(userServiceModel.getCountry())
                .setCity(userServiceModel.getCity())
                .setModified(Instant.now());

        return this.modelMapper.map(this.userRepository.save(user), UserServiceModel.class);
    }

    @Override
    public boolean changeProfilePicture(UserServiceModel userServiceModel) {
        UserEntity user = this.userRepository.findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));

        user.setImgUrl(userServiceModel.getImgUrl())
                .setModified(Instant.now());

        this.userRepository.save(user);
        return true;
    }

    @Override
    public List<UserViewModel> getAllUsers() {
        List<UserServiceModel> collect = this.userRepository.findAll()
                .stream()
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());

        return collect
                .stream()
                .map(u -> {
                    UserViewModel user = this.modelMapper.map(u, UserViewModel.class);
                    user.setAuthorities(u.getAuthorities()
                            .stream()
                            .map(RoleServiceModel::getAuthority)
                            .collect(Collectors.toSet()));
                    return user;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void setUserRole(Long id, String role) {
        UserEntity user = this.userRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect id!"));

        UserServiceModel userServiceModel = this.modelMapper.map(user, UserServiceModel.class);
        userServiceModel.getAuthorities().clear();

        switch (role) {
            case "client":
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_CLIENT"));
                break;
            case "moderator":
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_MODERATOR"));
                break;
            case "admin":
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_ADMIN"));
                break;
        }

        this.userRepository.save(this.modelMapper.map(userServiceModel, UserEntity.class));
    }

    @Override
    public void saveUser(UserEntity user) {
        this.userRepository.save(user);
    }

    @Override
    public Long findCartByUsername(String username) {
        UserEntity user = this.userRepository.findByUsername(username)
                .orElse(null);
        return user == null ? null : user.getCartEntity().getId();
    }


}
