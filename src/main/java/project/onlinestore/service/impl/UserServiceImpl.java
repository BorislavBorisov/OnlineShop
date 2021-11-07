package project.onlinestore.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.UserServiceModel;
import project.onlinestore.repository.UserRepository;
import project.onlinestore.service.RoleService;
import project.onlinestore.service.UserService;

import java.time.Instant;
import java.util.LinkedHashSet;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {
        UserEntity username = this.userRepository.findByUsername(userServiceModel.getUsername())
                .orElse(null);

        if (username != null) {
            throw new IllegalArgumentException("Потребителско име е заето!");
        }

        UserEntity userEmail = this.userRepository.findByEmail(userServiceModel.getEmail())
                .orElse(null);

        if (userEmail != null) {
            throw new IllegalArgumentException("Вече съществува протебител със същият имейл адрес!");
        }

        if (userRepository.count() == 0) {
            userServiceModel.setAuthorities(this.roleService.findAllRoles());
        } else {
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_CLIENT"));
        }

        UserEntity user = this.modelMapper.map(userServiceModel, UserEntity.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));
        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel findUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .map(u -> this.modelMapper.map(u, UserServiceModel.class))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    @Override
    public UserServiceModel editUserProfile(UserServiceModel userServiceModel) {
        UserEntity user = this.userRepository.findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        user.setUsername(userServiceModel.getUsername())
                .setFullName(userServiceModel.getFullName())
                .setEmail(userServiceModel.getEmail())
                .setFirstAddress(userServiceModel.getFirstAddress())
                .setPhoneNumber(userServiceModel.getPhoneNumber())
                .setCountry(userServiceModel.getCountry())
                .setCity(userServiceModel.getCity())
                .setImgUrl(userServiceModel.getImgUrl())
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
        return false;
    }

}
