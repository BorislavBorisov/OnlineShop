package project.onlinestore.web.controllers.admin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import project.onlinestore.domain.entities.RoleEntity;
import project.onlinestore.domain.entities.UserEntity;
import project.onlinestore.repository.RoleRepository;
import project.onlinestore.repository.UserRepository;

import java.time.Instant;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

//    @BeforeEach
//    public void setup() {
//        UserEntity userEntity = initUser();
//
//    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
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
    public void changeRoleToMod() throws Exception {
        long count = roleRepository.count();
        long count1 = userRepository.count();
        RoleEntity role_root = roleRepository.findByAuthority("ROLE_ROOT");
        RoleEntity role_client = roleRepository.findByAuthority("ROLE_CLIENT");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("gerasim@gerasim.bg")
                .setFullName("gerasim")
                .setPassword("1234")
                .setUsername("gerasim")
                .setAuthorities(Set.of(role_root))
                .setRegistered(Instant.now());

        userRepository.save(userEntity);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setEmail("pepa@pepa.bg")
                .setFullName("pepa")
                .setPassword("1234")
                .setUsername("pepa")
                .setAuthorities(Set.of(role_client))
                .setRegistered(Instant.now());

        userRepository.save(userEntity1);

       UserEntity pepa = userRepository.findByUsername("pepa").
               orElse(null);

        mockMvc.perform(post("/admin/users/set-moderator/" + pepa.getId())
                        .param("id", ""+ pepa.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("setModerator"))
                .andExpect(redirectedUrl("/admin/users"));
    }

//    public UserEntity initUser() {
//        RoleEntity roleAdmin = new RoleEntity();
//        roleAdmin.setAuthority("ROLE_ADMIN");
//
//        RoleEntity roleMod = new RoleEntity();
//        roleMod.setAuthority("ROLE_MODERATOR");
//
//        RoleEntity roleRoot = new RoleEntity();
//        roleRoot.setAuthority("ROLE_ROOT");
//
//        RoleEntity roleClient = new RoleEntity();
//        roleClient.setAuthority("ROLE_CLIENT");
//
//        roleRepository.saveAll(List.of(roleAdmin, roleClient, roleMod, roleRoot));
//
//        UserEntity userEntity = new UserEntity();
//        userEntity.setEmail("test@test.bg")
//                .setFullName("test")
//                .setPassword("1234")
//                .setUsername("test")
//                .setAuthorities(Set.of(roleAdmin, roleClient, roleMod, roleRoot))
//                .setRegistered(Instant.now());
//        userRepository.save(userEntity);
//        return userEntity;
//    }


}