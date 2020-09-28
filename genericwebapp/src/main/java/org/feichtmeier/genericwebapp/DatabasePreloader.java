package org.feichtmeier.genericwebapp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.javafaker.Faker;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Project;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.entity.View;
import org.feichtmeier.genericwebapp.repository.PermissionRepository;
import org.feichtmeier.genericwebapp.repository.ProjectRepository;
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
            PermissionRepository permissionRepository, ViewRepository viewRepository, PasswordEncoder passwordEncoder,
            ProjectRepository projectRepository) {
        return args -> {

            Project projectOne = new Project("projectOne");
            projectRepository.save(projectOne);
            Project projectTwo = new Project("projectTwo");
            projectRepository.save(projectTwo);
            Set<Project> allProjects = new HashSet<>(projectRepository.findAll());

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

            Permission projectViewPermission = new Permission();
            View projectViewEntity = new View(ViewNames.PROJECT_VIEW);
            projectViewPermission.setView(projectViewEntity);
            viewRepository.save(projectViewEntity);
            permissionRepository.save(projectViewPermission);

            Role admin = new Role("admin");
            Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());
            admin.setPermissions(allPermissions);
            roleRepository.save(admin);

            User heinrich = new User("heinrich", "Heinrich Schmidt", passwordEncoder.encode("password"),
                    "heinrich@schmidt.de", false, allProjects);
            Set<Role> roles = new HashSet<>();
            roles.add(admin);
            heinrich.setRoles(roles);
            userRepository.save(heinrich);

            for (int i = 0; i < 20; i++) {
                Faker faker = new Faker();
                String fullName = faker.name().fullName();
                String[] fullNameArray = fullName.split(" ");
                String firstName = fullNameArray[0];
                String username = fullName.replace(" ", "").toLowerCase();
                String email = firstName + i + "@gmail.com";

                User normalUser = new User(username, fullName, passwordEncoder.encode("password"), email, false,
                        new HashSet<>(Arrays.asList(projectOne)));
                userRepository.save(normalUser);
            }

        };
    }
}
