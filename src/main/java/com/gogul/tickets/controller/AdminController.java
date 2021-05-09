package com.gogul.tickets.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gogul.tickets.repository.AdminRepository;
import com.gogul.tickets.service.ClientService;
import com.gogul.tickets.service.MoviesService;

@RestController
@RequestMapping(value ="/api/admin")
public class AdminController {
	
	private final ClientService clientService;

	private final AdminRepository adminRepository;
	
	private final MoviesService moviesService;
	
	@Autowired
	public AdminController(ClientService clientService, AdminRepository adminRepository, MoviesService moviesService) {
		this.clientService=clientService;
		this.adminRepository = adminRepository;
		this.moviesService = moviesService;
	}
	
	@GetMapping("/{adminId}")
	public ResponseEntity<Object> getAdmin(@PathVariable long adminId){
		return ResponseEntity.ok(adminRepository.findById(adminId));
	}
	
	@DeleteMapping("/{adminId}")
	public ResponseEntity<Object> deleteAdmin(@PathVariable long adminId){
		if(adminRepository.existsById(adminId)){
			adminRepository.deleteById(adminId);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/clients")
	public ResponseEntity<Object> getAllClient(){
		return ResponseEntity.ok(clientService.findAll());
	}
	
	@GetMapping("/clients/{clientId}")
	public ResponseEntity<Object> getClient(@PathVariable long clientId){
		return ResponseEntity.ok(clientService.findByID(clientId));
	}
	
	@PostMapping(path = "/clients",consumes = "application/json")
	public ResponseEntity<Object> storeClient(@RequestBody String requestBody){
		try{
			return ResponseEntity.ok(clientService.saveClient(new JSONObject(requestBody),true));
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/clients/{clientId}",consumes = "application/json")
	public ResponseEntity<Object> storeClient(@RequestBody String requestBody,@PathVariable long clientId){
		try{
			return ResponseEntity.ok(clientService.saveClient(new JSONObject(requestBody),true));
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@DeleteMapping(path="/clients/{clientId")
	public ResponseEntity<Object> deleteClient(@PathVariable long clientId){
		if(clientService.deleteClient(clientId)) {
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping(path = "/movies")
	public ResponseEntity<Object> getMovies(){
		return ResponseEntity.ok(moviesService.findAll());
	}
	
	@PostMapping(path = "/movies")
	public ResponseEntity<Object> createMovie(@RequestBody String requestMessage) {
		return ResponseEntity.ok("Not yet functional");
	}
	
	@GetMapping(path = "/movies/{movieId}")
	public ResponseEntity<Object> getMovie(@PathVariable long movieId){
		return ResponseEntity.ok(moviesService.findById(movieId));
	}
	
	@PutMapping(path = "/movies/{movieId}")
	public ResponseEntity<Object> updateMovie(@PathVariable long movieId){
		return ResponseEntity.ok("Not yet functional");
	}
	
	@DeleteMapping(path = "/movies/{movieId}")
	public ResponseEntity<Object> deleteMovie(@PathVariable long movieId){
		return ResponseEntity.ok(moviesService.deleteByID(movieId));
	}
	
}
