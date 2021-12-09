package project.onlinestore.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import project.onlinestore.domain.entities.RoleEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.UserServiceModel;
import project.onlinestore.domain.view.UserViewModel;
import project.onlinestore.repository.RoleRepository;
import project.onlinestore.repository.UserRepository;
import project.onlinestore.service.UserService;

import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;

    UserEntity rootUser;
    UserEntity clientUser;
    RoleEntity rootRole;
    RoleEntity clientRole;
    RoleEntity modRole;
    RoleEntity adminRole;


    @Before
    public void setup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        rootRole = new RoleEntity("ROLE_ROOT");
        modRole = new RoleEntity("ROLE_MODERATOR");
        adminRole = new RoleEntity("ROLE_ADMIN");
        clientRole = new RoleEntity("ROLE_CLIENT");
        roleRepository.saveAll(List.of(rootRole, clientRole, modRole, adminRole));

        rootUser = new UserEntity();
        rootUser.setUsername("Bobi")
                .setEmail("bobi@bobi.bg")
                .setFullName("Borislav")
                .setPassword("1234")
                .setAuthorities(Set.of(rootRole));

        clientUser = new UserEntity();
        clientUser.setUsername("Gosho")
                .setEmail("gosho@gosho.bg")
                .setFullName("Gosho")
                .setPassword("12345")
                .setAuthorities(Set.of(clientRole));
    }

    @Test
    public void test_RegisterUser() {
        UserServiceModel root = modelMapper.map(rootUser, UserServiceModel.class);

        userService.registerUser(root);
        Assert.assertEquals(1, this.userRepository.count());

        UserServiceModel client = modelMapper.map(clientUser, UserServiceModel.class);
        userService.registerUser(client);
        Assert.assertEquals(2, this.userRepository.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_RegisterUser_ThrowsEx_UsernameTaken() {
        userRepository.save(rootUser);
        Assert.assertEquals(1, this.userRepository.count());

        UserServiceModel root = modelMapper.map(rootUser, UserServiceModel.class);
        userService.registerUser(root);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_RegisterUser_ThrowsEx_EmailTaken() {
        rootUser.setUsername("newUsername");
        userRepository.save(rootUser);
        Assert.assertEquals(1, this.userRepository.count());

        UserServiceModel root = modelMapper.map(rootUser, UserServiceModel.class);
        userService.registerUser(root);
    }

    @Test
    public void test_loadUserByUsername() {
        userRepository.save(rootUser);
        Assert.assertEquals(1, this.userRepository.count());

        UserDetails userDetails = userService.loadUserByUsername(rootUser.getUsername());
        Assert.assertEquals(userDetails.getUsername(), rootUser.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void test_loadUserByUsername_ThrowsEx() {
        userRepository.save(rootUser);
        Assert.assertEquals(1, this.userRepository.count());

        userService.loadUserByUsername("TestovUser");
    }

    @Test
    public void test_findUserByUsername() {
        userRepository.save(rootUser);
        Assert.assertEquals(1, this.userRepository.count());

        UserServiceModel user = userService.findUserByUsername(rootUser.getUsername());
        Assert.assertEquals(rootUser.getUsername(), user.getUsername());
        Assert.assertEquals(rootUser.getEmail(), user.getEmail());
    }

    @Test
    public void test_editUserProfile() {
        userRepository.save(rootUser);
        Assert.assertEquals(1, this.userRepository.count());

        UserServiceModel user = userService.findUserByUsername(rootUser.getUsername());
        user.setCity("Sofia");
        user.setCountry("Bulgaria");
        user.setPhoneNumber("123456789");
        user.setAddress("Somewhere");

        UserServiceModel userServiceModel = userService.editUserProfile(modelMapper.map(user, UserServiceModel.class));
        Assert.assertEquals(user.getPhoneNumber(), userServiceModel.getPhoneNumber());
        Assert.assertEquals(user.getCity(), userServiceModel.getCity());
        Assert.assertEquals(user.getCountry(), userServiceModel.getCountry());
        Assert.assertEquals(user.getAddress(), userServiceModel.getAddress());
    }

    @Test
    public void test_changeProfilePicture() {
        userRepository.save(rootUser);
        Assert.assertEquals(1, this.userRepository.count());

        UserServiceModel user = userService.findUserByUsername(rootUser.getUsername());
        user.setImgUrl("neshto novo");

        userService.changeProfilePicture(modelMapper.map(user, UserServiceModel.class));

        Assert.assertEquals("neshto novo", user.getImgUrl());
    }

    @Test
    public void test_GetAllUsers() {
        userRepository.saveAll(List.of(rootUser, clientUser));
        Assert.assertEquals(2, this.userRepository.count());

        List<UserViewModel> allUsers = userService.getAllUsers();
        Assert.assertEquals(2, allUsers.size());
    }

    @Test
    public void test_saveUser() {
        userService.saveUser(rootUser);
    }

    @Test
    public void test_SetUserRole() {
        userRepository.saveAll(List.of(rootUser, clientUser));
        Assert.assertEquals(2, this.userRepository.count());

        UserServiceModel client = userService.findUserByUsername(clientUser.getUsername());

        userService.setUserRole(client.getId(), "admin");
        Assert.assertEquals(1, client.getAuthorities().size());
        Assert.assertEquals("ROLE_ADMIN", adminRole.getAuthority());

        userService.setUserRole(client.getId(), "moderator");
        Assert.assertEquals(1, client.getAuthorities().size());
        Assert.assertEquals("ROLE_MODERATOR", modRole.getAuthority());

        userService.setUserRole(client.getId(), "client");
        Assert.assertEquals(1, client.getAuthorities().size());
        Assert.assertEquals("ROLE_CLIENT", clientRole.getAuthority());

    }

}