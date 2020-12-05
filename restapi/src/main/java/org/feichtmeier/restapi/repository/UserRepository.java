package org.feichtmeier.restapi.repository;

import java.util.List;

import org.feichtmeier.restapi.entity.User;

public interface UserRepository extends GenericRepository<User> {

    List<User> findByFullNameStartsWithIgnoreCase(String fullName);
    User findByEmail(String email);
    User findByUsername(String userName);

}