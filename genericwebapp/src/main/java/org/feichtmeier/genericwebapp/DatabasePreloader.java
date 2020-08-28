package org.feichtmeier.genericwebapp;

import java.util.HashSet;
import java.util.Set;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.entity.View;
import org.feichtmeier.genericwebapp.repository.PermissionRepository;
import org.feichtmeier.genericwebapp.repository.RoleRepository;
import org.feichtmeier.genericwebapp.repository.UserRepository;
import org.feichtmeier.genericwebapp.repository.ViewRepository;
import org.feichtmeier.genericwebapp.view.ViewNames;
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
            Permission userViewPermission = new Permission();
            View userViewEntity = new View(ViewNames.USER_VIEW);
            userViewPermission.setView(userViewEntity);
            viewRepository.save(userViewEntity);
            permissionRepository.save(userViewPermission);

            Permission homeViewPermission = new Permission();
            View homeViewEntity = new View(ViewNames.HOME_VIEW);
            homeViewPermission.setView(homeViewEntity);
            viewRepository.save(homeViewEntity);
            permissionRepository.save(homeViewPermission);

            Permission roleViewPermission = new Permission();
            View roleViewEntity = new View(ViewNames.ROLE_VIEW);
            roleViewPermission.setView(roleViewEntity);
            viewRepository.save(roleViewEntity);
            permissionRepository.save(roleViewPermission);

            Role admin = new Role("ADMIN");
            Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());
            admin.setPermissions(allPermissions);
            roleRepository.save(admin);

            User heinrich = new User("admin", "Heinrich Schmidt", passwordEncoder.encode("password"), "heinrich@schmidt.de",
                    false);
            Set<Role> roles = new HashSet<>();
            roles.add(admin);
            heinrich.setRoles(roles);
            userRepository.save(heinrich);

            for (int i = 0; i < 20; i++) {
                User normalUser = new User("normalUser"+i, "Firstname"+i + " Surname"+i, passwordEncoder.encode("password"), i+"noob@noob.de",
                    false);
                userRepository.save(normalUser);
            }
        };
    }
}
