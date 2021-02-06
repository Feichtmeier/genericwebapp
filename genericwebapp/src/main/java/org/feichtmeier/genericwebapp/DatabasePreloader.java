package org.feichtmeier.genericwebapp;

import java.util.HashSet;
import java.util.Set;

import com.github.javafaker.Faker;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.entity.View;
import org.feichtmeier.genericwebapp.repository.PermissionRepository;
import org.feichtmeier.genericwebapp.repository.RoleRepository;
import org.feichtmeier.genericwebapp.repository.UserRepository;
import org.feichtmeier.genericwebapp.repository.ViewRepository;
import org.feichtmeier.genericwebapp.view.constants.ViewNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabasePreloader {

    Logger log = LoggerFactory.getLogger(DatabasePreloader.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository,
            PermissionRepository permissionRepository, ViewRepository viewRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            Permission homePermission = null;
            for (String viewName : ViewNames.viewNames) {
                Permission permission = new Permission();
                View view = new View(viewName);
                if (viewName.equals(ViewNames.HOME_VIEW)) {
                    homePermission = permission;
                }
                permission.setView(view);
                viewRepository.save(view);
                permissionRepository.save(permission);
            }

            Role admin = new Role("admin");
            Set<Permission> adminPermissions = new HashSet<>(permissionRepository.findAll());
            for (Permission permission : adminPermissions) {
                permission.setEdit(true);
                permissionRepository.save(permission);
            }
            admin.setPermissions(adminPermissions);
            roleRepository.save(admin);

            Role user = new Role("user");
            Set<Permission> userPermissions = new HashSet<>();
            if (null != homePermission) {
                userPermissions.add(homePermission);
            }
            user.setPermissions(userPermissions);
            roleRepository.save(user);

            User heinrich = new User("heinrich", "Heinrich Schmidt", passwordEncoder.encode("password"),
                    "heinrich@schmidt.de", false);
            Set<Role> roles = new HashSet<>();
            roles.add(admin);
            heinrich.setRoles(roles);
            userRepository.save(heinrich);

            for (int i = 0; i < 100; i++) {
                Faker faker = new Faker();
                String fullName = faker.name().fullName();
                String[] fullNameArray = fullName.split(" ");
                String firstName = fullNameArray[0];
                String username = fullName.replace(" ", "").toLowerCase();
                String email = firstName + i + "@gmail.com";

                User normalUser = new User(username, fullName, passwordEncoder.encode("password"), email, false);
                Set<Role> normalUserRoles = new HashSet<>();
                normalUserRoles.add(user);
                normalUser.setRoles(normalUserRoles);
                userRepository.save(normalUser);
            }

        };
    }
}
