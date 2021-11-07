package project.onlinestore.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.onlinestore.service.RoleService;

@Component
public class DbInit implements CommandLineRunner {

    private final RoleService roleService;

    public DbInit(RoleService roleService) {
        this.roleService = roleService;

    }

    @Override
    public void run(String... args) {
        this.roleService.seedRolesInDb();
    }
}
