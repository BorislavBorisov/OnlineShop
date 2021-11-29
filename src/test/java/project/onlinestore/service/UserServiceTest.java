package project.onlinestore.service;

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
import project.onlinestore.repository.UserRepository;
import project.onlinestore.service.impl.UserServiceImpl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private ModelMapper modelMapper;
    private UserServiceImpl userServiceTest;
    private UserEntity userTest;
    private RoleEntity root, client;

    @Mock
    private UserRepository userRepositoryTest;
    @Mock
    private RoleService roleServiceTest;

    @BeforeEach
    void init() {
        this.userServiceTest = new UserServiceImpl(userRepositoryTest, roleServiceTest, modelMapper);

        this.root = new RoleEntity();
        this.root.setAuthority("ROLE_ROOT");

        this.client = new RoleEntity();
        this.client.setAuthority("ROLE_CLIENT");

        this.userTest = new UserEntity()
                .setUsername("bobi")
                .setEmail("bobi@bobi.bg")
                .setAuthorities(Set.of(this.root));
    }

    @Test
    void testUserNotFound() {
        Assertions.assertThrows(
                UsernameNotFoundException.class, () -> {
                    userServiceTest.loadUserByUsername("Invalid username");
                });
    }

    @Test
    void testUserFound() {
        //Arrange
        Mockito.when(userRepositoryTest.findByUsername(this.userTest.getUsername()))
                .thenReturn(Optional.of(this.userTest));

        //Act
        var actual = this.userServiceTest.loadUserByUsername(this.userTest.getUsername());

        //Assert
        Assertions.assertEquals(actual.getUsername(), this.userTest.getUsername());
        String actualRoles = actual.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        Assertions.assertEquals("ROLE_ROOT", actualRoles);
    }
}
