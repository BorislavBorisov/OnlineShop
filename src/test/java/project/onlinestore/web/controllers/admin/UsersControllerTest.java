package project.onlinestore.web.controllers.admin;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.entities.RoleEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.repository.RoleRepository;
import project.onlinestore.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    UserEntity rootUser;
    UserEntity clientUser;
    UserEntity adminUser;

    RoleEntity roleRoot;
    RoleEntity roleClient;
    RoleEntity roleAdmin;
    RoleEntity roleModerator;

    @Before
    public void setup() {

        this.userRepository.deleteAll();
        this.roleRepository.deleteAll();

        roleRoot = new RoleEntity();
        roleRoot.setAuthority("ROLE_ROOT");

        roleClient = new RoleEntity();
        roleClient.setAuthority("ROLE_CLIENT");

        roleAdmin = new RoleEntity();
        roleAdmin.setAuthority("ROLE_ADMIN");

        roleModerator = new RoleEntity();
        roleModerator.setAuthority("ROLE_MODERATOR");

        roleRepository.saveAll(List.of(roleRoot, roleClient, roleAdmin, roleModerator));

        rootUser = new UserEntity();
        rootUser.setUsername("root")
                .setFullName("Root Root")
                .setEmail("root@root.bg")
                .setPassword("1234")
                .setAuthorities(Set.of(roleRoot));

        adminUser = new UserEntity();
        adminUser.setUsername("admin")
                .setFullName("admin admin")
                .setEmail("admin@admin.bg")
                .setPassword("1234")
                .setAuthorities(Set.of(roleAdmin));

        clientUser = new UserEntity();
        clientUser.setUsername("test")
                .setFullName("test testov")
                .setEmail("test@test.bg")
                .setPassword("1234")
                .setAuthorities(Set.of(roleClient));
    }


    @Test
    @WithMockUser(authorities = {"ROLE_ROOT", "ROLE_ADMIN"})
    public void get_UsersPage_ReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("allUsers"))
                .andExpect(model().attributeExists("allUsers"))
                .andExpect(view().name("/admin/users/all-users"));
    }


    @Test
    @WithMockUser(authorities = {"ROLE_ROOT"})
    public void changeRoleToMod() throws Exception {
        userRepository.save(clientUser);
        Assert.assertEquals(1, userRepository.count());

        Optional<UserEntity> byUsername = userRepository.findByUsername(clientUser.getUsername());
        Assert.assertNotNull(byUsername);

        mockMvc.perform(post("/admin/users/set-moderator/" + byUsername.get().getId())
                        .param("id", String.valueOf(byUsername.get().getId()))
                        .param("role", "moderator")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("setModerator"))
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ROOT"})
    public void changeRoleToAdmin() throws Exception {
        userRepository.save(clientUser);
        Assert.assertEquals(1, userRepository.count());

        Optional<UserEntity> byUsername = userRepository.findByUsername(clientUser.getUsername());
        Assert.assertNotNull(byUsername);

        mockMvc.perform(post("/admin/users/set-admin/" + byUsername.get().getId())
                        .param("id", String.valueOf(byUsername.get().getId()))
                        .param("role", "admin")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("setAdmin"))
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ROOT"})
    public void changeRoleToClient() throws Exception {
        userRepository.save(adminUser);
        Assert.assertEquals(1, userRepository.count());

        Optional<UserEntity> byUsername = userRepository.findByUsername(adminUser.getUsername());
        Assert.assertNotNull(byUsername);

        mockMvc.perform(post("/admin/users/set-client/" + byUsername.get().getId())
                        .param("id", String.valueOf(byUsername.get().getId()))
                        .param("role", "client")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("setClient"))
                .andExpect(redirectedUrl("/admin/users"));
    }


}