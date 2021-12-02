package project.onlinestore.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import project.onlinestore.domain.entities.RoleEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.domain.service.UserServiceModel;
import project.onlinestore.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserServiceImpl testService;
    private UserEntity testUser;

    @Mock
    private UserRepository testRepository;
    @Mock
    private RoleServiceImpl testRoleService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void init() {
        testService = new UserServiceImpl(testRepository, testRoleService, modelMapper);


        testUser = new UserEntity();
        testUser.setEmail("test@test.bg")
                .setUsername("test")
                .setFullName("test test")
                .setPassword("test")
                .setAuthorities(Set.of(new RoleEntity("ROLE_ROOT")));
    }

    @Test
    void testUserNotFound() {
        Assertions.assertThrows(
                UsernameNotFoundException.class, () -> {
                    testService.loadUserByUsername("Invalid username");
                });
    }

    @Test
    void testUserFound() {

        //Arrange
        Mockito.when(testRepository.findByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser));

        //Act
        var actual = this.testService.loadUserByUsername(testUser.getUsername());

        //Assert
        assertEquals(actual.getUsername(), testUser.getUsername());

        String actualRoles = actual.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        assertEquals("ROLE_ROOT", actualRoles);
    }

    @Test
    void findByUserNameNotWork() {
        Assertions.assertThrows(
                UsernameNotFoundException.class, () -> {
                    testService.findUserByUsername("Invalid username");
                });
    }

//    @Test
//    void findByUsernameWork() {
//        UserServiceModel userServiceModel = new UserServiceModel();
//        userServiceModel.setEmail("test@test.bg");
//        userServiceModel.setUsername("test");
//        userServiceModel.setFullName("test test");
//        userServiceModel.setPassword("test");
//
//        Mockito.when(testRepository.findByUsername(userServiceModel.getUsername()))
//                .thenReturn(Optional.of(testUser));
//
//
//    }
}