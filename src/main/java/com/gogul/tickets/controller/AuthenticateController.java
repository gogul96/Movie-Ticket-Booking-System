package com.gogul.tickets.controller;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gogul.tickets.authentication.JwtUtils;
import com.gogul.tickets.modal.ApplicationUsers;
import com.gogul.tickets.modal.User;
import com.gogul.tickets.repository.ApplicationUsersRepository;
import com.gogul.tickets.repository.UserRepository;

@RestController
@RequestMapping(value ="/api/")
public class AuthenticateController {
	
	@Autowired
	private ApplicationUsersRepository applicationUserRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/login")
	public ResponseEntity<Object> authenticate(@RequestBody String requestMessage){
		try {
			JSONObject jsonObject = new JSONObject(requestMessage);
			String username = jsonObject.getString("username");
			String password = jsonObject.getString("password");
			Optional<ApplicationUsers> admin = applicationUserRepository.findByUsername(username);
			if(admin.isPresent()) {
				if(passwordEncoder.matches(password, admin.get().getPassword())){
					return ResponseEntity.ok(jwtUtils.generateJwtResponse(admin.get().getId(),admin.get().getUsername()));
				} else {
					return ResponseEntity.badRequest().body("Invalid Credentionals");
				}
			}
		}catch(Exception e) {
			return ResponseEntity.unprocessableEntity().body(requestMessage);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/signup/users")
	public ResponseEntity<Object> signUpUser(@RequestBody User user) {
		try {
			if(applicationUserRepository.existsById(user.getUserId()) || userRepository.existsById(user.getUserId())) {
				return ResponseEntity.badRequest().build();
			}
			ApplicationUsers applicationUsers = new ApplicationUsers();
			applicationUsers.setId(user.getUserId());
			applicationUsers.setUsername(user.getUsername());
			applicationUsers.setPassword(passwordEncoder.encode(user.getUsername()));
			applicationUsers.setRole("USERS");
			applicationUserRepository.save(applicationUsers);
			userRepository.save(user);
			return ResponseEntity.ok().build();
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.unprocessableEntity().build();
		}
	}
}