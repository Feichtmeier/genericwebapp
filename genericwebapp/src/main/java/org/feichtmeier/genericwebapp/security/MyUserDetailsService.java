package org.feichtmeier.genericwebapp.security;

import java.util.ArrayList;
import java.util.List;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class MyUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public MyUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
		for (Role role : user.getRoles()) {
			for (Permission permission : role.getPermissions()) {
				simpleGrantedAuthorities.add(new SimpleGrantedAuthority(permission.getView().getName()));
			}
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPasswordHash(),
				simpleGrantedAuthorities);
	}
}