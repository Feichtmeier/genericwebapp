package org.feichtmeier.genericwebapp.service;

import java.util.List;

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
    
}
