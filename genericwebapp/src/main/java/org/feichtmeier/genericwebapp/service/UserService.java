package org.feichtmeier.genericwebapp.service;

import java.util.List;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService implements DataService<User> {
    
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User entity) {
        userRepository.save(entity);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isViewAllowed(User user, String viewName) {
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                if (permission.getView().getName().equals(viewName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isViewEditable(User user, String viewName) {
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                if (permission.getView().getName().equals(viewName)) {
                    return permission.isEdit();
                }
            }
        }
        return false;
    }

    @Override
    public User getOne(User entity) {
        return userRepository.getOne(entity.getId());
    }
    
}
