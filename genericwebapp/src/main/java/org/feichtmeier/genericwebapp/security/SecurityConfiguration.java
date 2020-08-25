package org.feichtmeier.genericwebapp.security;

import java.util.HashSet;
import java.util.Set;

import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.repository.PermissionRepository;
import org.feichtmeier.genericwebapp.repository.RoleRepository;
import org.feichtmeier.genericwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionsRepository;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() //

                .requestCache().requestCache(new CustomRequestCache()) //

                .and().authorizeRequests() //

                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll() //

                .anyRequest().authenticated() //

                .and().formLogin().loginPage(LOGIN_URL).permitAll() //
                .loginProcessingUrl(LOGIN_PROCESSING_URL) //
                .failureUrl(LOGIN_FAILURE_URL)

                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);

        Role admin = new Role("ADMIN");
        roleRepository.save(admin);

        // TODO: Delete proto use
        User user = new User("user", "Heinrich Schmidt", passwordEncoder.encode("password"), false, "heinrich@schmidt.de");
        Set<Role> roles = new HashSet<>();
        roles.add(admin);
        user.setRoles(roles);;
        userRepository.save(user);

        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
    }


	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CurrentUser currentUser(UserRepository userRepository) {
		final String username = SecurityUtils.getUsername();
		User user =
			username != null ? userRepository.findByUsername(username) :
				null;
		return () -> user;
	}

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/VAADIN/**",

                "/favicon.ico",

                "/robots.txt",

                "/manifest.webmanifest", "/sw.js", "/offline-page.html",

                "/frontend/**",

                "/webjars/**",

                "/frontend-es5/**", "/frontend-es6/**");
    }
}