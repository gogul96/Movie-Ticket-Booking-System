package com.gogul.tickets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gogul.tickets.repository.UserRepository;

@RestController
@RequestMapping(value ="/api/users")
public class UserController {
	
	private final UserRepository userRepository;
	
	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/*
	 * @PostMapping("/login") public ResponseEntity<Object>
	 * authenticate(@RequestBody String requestMessage){ try { JSONObject jsonObject
	 * = new JSONObject(requestMessage); String username =
	 * jsonObject.getString("username"); String password =
	 * jsonObject.getString("password"); Optional<User> user =
	 * userRepository.findByUsername(username); if(user.isPresent()) {
	 * if(user.get().getPassword().equals(password)){ return
	 * ResponseEntity.ok(jwtUtils.generateJwtResponse(user.get().getUserId(),user.
	 * get().getUsername())); } else { return
	 * ResponseEntity.badRequest().body("Invalid Credentionals"); } }
	 * }catch(Exception e) { return
	 * ResponseEntity.unprocessableEntity().body(requestMessage); } return
	 * ResponseEntity.notFound().build(); }
	 */
	
	@GetMapping("/{userId}")
	public ResponseEntity<Object> getUser(@PathVariable long userId){
		if(userRepository.existsById(userId)) {
			return ResponseEntity.ok(userRepository.findById(userId));
		}
		return ResponseEntity.notFound().build();
	}
	
	/*
	 * @GetMapping public ResponseEntity<Object> getUsers() { return
	 * ResponseEntity.ok(userRepository.findAll()); }
	 */
	
	/*
	 * @PostMapping("/signup") public ResponseEntity<Object> signUpUser(@RequestBody
	 * User user) { try { if(userRepository.existsById(user.getUserId())) { return
	 * ResponseEntity.badRequest().build(); } userRepository.save(user); return
	 * ResponseEntity.ok().build(); }catch(Exception e) { e.printStackTrace();
	 * return ResponseEntity.unprocessableEntity().build(); } }
	 */
	
	
	
}
