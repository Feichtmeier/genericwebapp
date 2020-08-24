package org.feichtmeier.genericwebapp.repository;

import java.util.List;

import org.feichtmeier.genericwebapp.entity.User;

public interface UserRepository extends GenericRepository<User> {

    List<User> findByFullNameStartsWithIgnoreCase(String fullName);
    User findByEmail(String email);
    User findByUsername(String userName);

}