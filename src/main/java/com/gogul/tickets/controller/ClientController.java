package com.gogul.tickets.controller;

import java.util.Optional;
import java.util.stream.Collectors;

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

import com.gogul.tickets.modal.Theatres;
import com.gogul.tickets.service.ClientService;
import com.gogul.tickets.service.MovieScheduleService;

@RestController
@RequestMapping(value ="/api/client/{clientId}")
public class ClientController {

	private final ClientService clientService;
	private final MovieScheduleService movieScheduleService;
	
	/*
	 * @Autowired private JwtUtils jwtUtils;
	 */
	
	@Autowired
	public ClientController(ClientService clientService, MovieScheduleService movieScheduleService) {
		this.clientService=clientService;
		this.movieScheduleService = movieScheduleService;
	}
	
	/*
	 * @PostMapping("/login") public ResponseEntity<Object>
	 * authenticate(@RequestBody String requestMessage){ try { JSONObject jsonObject
	 * = new JSONObject(requestMessage); String username =
	 * jsonObject.getString("username"); String password =
	 * jsonObject.getString("password"); Optional<Client> client =
	 * clientService.findByUsername(username); if(client.isPresent()) {
	 * if(client.get().getPassword().equals(password)){ return
	 * ResponseEntity.ok(jwtUtils.generateJwtResponse(client.get().getClientId(),
	 * client.get().getUsername())); } else { return
	 * ResponseEntity.badRequest().body("Invalid Credentionals"); } }
	 * }catch(Exception e) { return
	 * ResponseEntity.unprocessableEntity().body(requestMessage); } return
	 * ResponseEntity.notFound().build(); }
	 */
	
	@GetMapping("/theatres")
	public ResponseEntity<Object> findAllTheatresByClientId(@PathVariable long clientId){
		return ResponseEntity.ok(clientService.findByClientId(clientId));
	}
	
	@PostMapping("/theatres")
	public ResponseEntity<Object> updateTheatresWithClientId(@PathVariable long clientId,@RequestBody String requestMessage){
		try {
			if(clientService.existsById(clientId)) {
				return ResponseEntity.ok(clientService.saveTheatre(clientId, new JSONObject(requestMessage), true));
			}else {
				return ResponseEntity.notFound().build();
			}
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.fillInStackTrace());
		}
	}
	
	@PutMapping("/theatres/{theatreId}")
	public ResponseEntity<Object> updateTheatresWithTheatreId(@PathVariable long clientId, @PathVariable long theatreId,
										@RequestBody String requestMessage){
		if(clientService.existsById(clientId)) {
			Optional<Object> responseObject = Optional.of(clientService.updateTheatre(clientId,theatreId,new JSONObject(requestMessage)));
			if(responseObject.isPresent()) {
				return ResponseEntity.ok(responseObject.get());
			}else {
				return ResponseEntity.badRequest().body("Theatre is not belong to you");
			}
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/theatres/{theatreId}")
	public ResponseEntity<Object> deleteTheatreByTheatreId(@PathVariable long clientId, @PathVariable long theatreId){
		if(clientService.existsById(clientId) && clientService.deleteTheatre(clientId,theatreId)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	
	
	@GetMapping("/schedule")
	public ResponseEntity<Object> getMovieScheduledWithAvailability(@PathVariable long clientId){
		if(clientService.existsById(clientId)) {
			return ResponseEntity.ok(
					movieScheduleService.findScheduleByListOfTheatreId(
							clientService.findByClientId(clientId).stream()
								.map(Theatres::getTheatreId)
								.collect(Collectors.toList())));
						
		}
		return ResponseEntity.notFound().build();
	}
	
}
