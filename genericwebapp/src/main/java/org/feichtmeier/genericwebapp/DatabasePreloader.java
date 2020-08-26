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
            View userViewEntity = new View();
            userViewEntity.setName(ViewNames.USER_VIEW);
            userViewPermission.setView(userViewEntity);
            viewRepository.save(userViewEntity);
            userViewEntity.setPermission(userViewPermission);
            permissionRepository.save(userViewPermission);

            Permission homeViewPermission = new Permission();
            View homeViewEntity = new View();
            homeViewEntity.setName(ViewNames.HOME_VIEW);
            homeViewPermission.setView(homeViewEntity);
            viewRepository.save(homeViewEntity);
            homeViewEntity.setPermission(homeViewPermission);
            permissionRepository.save(homeViewPermission);

            Permission roleViewPermission = new Permission();
            View roleViewEntity = new View();
            roleViewEntity.setName(ViewNames.ROLE_VIEW);
            roleViewPermission.setView(roleViewEntity);
            viewRepository.save(roleViewEntity);
            roleViewEntity.setPermission(roleViewPermission);
            permissionRepository.save(roleViewPermission);

            Role admin = new Role("ADMIN");
            Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());
            admin.setPermissions(allPermissions);
            roleRepository.save(admin);

            User user = new User("user", "Heinrich Schmidt", passwordEncoder.encode("password"), "heinrich@schmidt.de",
                    false);
            Set<Role> roles = new HashSet<>();
            roles.add(admin);
            user.setRoles(roles);
            userRepository.save(user);
        };
    }
}
