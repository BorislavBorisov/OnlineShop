package project.onlinestore.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.onlinestore.domain.entities.RoleEntity;
import project.onlinestore.domain.service.RoleServiceModel;
import project.onlinestore.repository.RoleRepository;
import project.onlinestore.service.RoleService;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleServiceImplTest {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleRepository roleRepository;

    RoleEntity roleRoot;
    RoleEntity roleAdmin;
    RoleEntity roleMod;
    RoleEntity roleClient;

    @Before
    public void setup() {
        roleRepository.deleteAll();

        roleRoot = new RoleEntity("ROLE_ROOT");
        roleAdmin = new RoleEntity("ROLE_ADMIN");
        roleMod = new RoleEntity("ROLE_MODERATOR");
        roleClient = new RoleEntity("ROLE_CLIENT");

    }

    @Test
    public void test_SeedInDB() {
        Assert.assertEquals(0, roleRepository.count());
        roleService.seedRolesInDb();
        Assert.assertEquals(4, roleRepository.count());
    }

    @Test
    public void test_FindAllRoles() {
        roleService.seedRolesInDb();
        Assert.assertEquals(4, roleRepository.count());

        Set<RoleServiceModel> allRoles = roleService.findAllRoles();
        Assert.assertEquals(4, allRoles.size());
    }

    @Test
    public void test_findByAuthority(){
        roleService.seedRolesInDb();
        Assert.assertEquals(4, roleRepository.count());

        RoleServiceModel rootAuthority = roleService.findByAuthority(roleRoot.getAuthority());
        Assert.assertEquals(roleRoot.getAuthority(), rootAuthority.getAuthority());

        RoleServiceModel adminAuthority = roleService.findByAuthority(roleAdmin.getAuthority());
        Assert.assertEquals(roleAdmin.getAuthority(), adminAuthority.getAuthority());

        RoleServiceModel modAuthority = roleService.findByAuthority(roleMod.getAuthority());
        Assert.assertEquals(modAuthority.getAuthority(), modAuthority.getAuthority());

        RoleServiceModel clientAuthority = roleService.findByAuthority(roleClient.getAuthority());
        Assert.assertEquals(roleClient.getAuthority(), clientAuthority.getAuthority());
    }


}