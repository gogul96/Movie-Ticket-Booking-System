package com.gogul.tickets.authentication;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gogul.tickets.modal.ApplicationUsers;
import com.gogul.tickets.repository.ApplicationUsersRepository;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private ApplicationUsersRepository applicationUserRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String password="";
		ApplicationUsers applicationUsers = applicationUserRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
		password = passwordEncoder.encode(applicationUsers.getPassword());
		GrantedAuthority authority = new SimpleGrantedAuthority(applicationUsers.getRole().toUpperCase());
		return new User(username,password,Arrays.asList(authority));
	}

}
