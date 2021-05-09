package com.gogul.tickets.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gogul.tickets.service.ClientService;
import com.gogul.tickets.service.MovieScheduleService;
import com.gogul.tickets.service.MoviesService;

@RestController
@RequestMapping(value ="/api/movies")
public class MoviesController {

	@Autowired
	private MoviesService moviesService;
	
	@Autowired
	private MovieScheduleService movieScheduleService;
	
	@Autowired
	private ClientService clientService;
	
	@GetMapping
	public ResponseEntity<Object> fetchMovies(){
		return ResponseEntity.ok(moviesService.findAll());
	}
	
	@GetMapping("/schedule")
	public ResponseEntity<Object> fetchScheduleWithMovies(@RequestParam Map<String,String> reqeustParam){
		if(reqeustParam.isEmpty()) {
			return ResponseEntity.ok(movieScheduleService.findAll());
		}else {
			return ResponseEntity.ok(movieScheduleService.findScheduleWithParam(moviesService.findAllMovieIdWithParam(reqeustParam)));
		}
	}
	
	@GetMapping("/schedule/{movieId}")
	public ResponseEntity<Object> fetchMoviesScheduleByMovie(@PathVariable long movieId){
		if(moviesService.existsById(movieId)) {
			return ResponseEntity.ok(movieScheduleService.findScheduleByMovieId(movieId));
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/schedule/theatre/{theatreId}")
	public ResponseEntity<Object> fetchMoviesScheduleByTheatre(@PathVariable long theatreId, @RequestParam Map<String,String> reqeustParam){
		if(clientService.existsByTheatreId(theatreId)) {
			if(reqeustParam.isEmpty()) {
				return ResponseEntity.ok(movieScheduleService.findScheduleByTheatreId(theatreId));
			}else {
				return ResponseEntity.ok(movieScheduleService.findScheduleByTheatreIdWithParam(theatreId,moviesService.findAllMovieIdWithParam(reqeustParam)));
			}
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/schedule/screen/{screenId}")
	public ResponseEntity<Object> fetchMoviesScheduleByScreen(@PathVariable long screenId, @RequestParam Map<String,String> reqeustParam){
		if(clientService.existsByScreenId(screenId)) {
			if(reqeustParam.isEmpty()) {
				return ResponseEntity.ok(movieScheduleService.findScheduleByScreenId(screenId));
			}else {
				return ResponseEntity.ok(movieScheduleService.findScheduleByScreenIdWithParam(screenId,moviesService.findAllMovieIdWithParam(reqeustParam)));
			}
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/schedule/{scheduleId}/{screenId}/availableSeats")
	public ResponseEntity<Object> checkSeatIsAvailable(@PathVariable long screenId,@PathVariable long scheduleId){
		Optional<Map<Long, Map<String, Integer>>> seatLayoutMap = clientService.getSeatLayout(screenId); 
		if(seatLayoutMap.isPresent() && movieScheduleService.existsByScheduleId(scheduleId)) {
			Map<String,Object> response = new HashMap<String,Object>();
			response.put("scheduleId", scheduleId);
			response.put("screenId",screenId);
			response.put("seatsStatus", movieScheduleService.findAvailableSeats(screenId,scheduleId,seatLayoutMap.get()));
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/schedule/{scheduleId}/{screenId}/{layoutId}/blockSeats")
	public ResponseEntity<Object> blockSeatsTemporailyToUsers(@PathVariable long screenId,@PathVariable long scheduleId,
			@PathVariable long layoutId, @RequestBody String requestMessage){
		Optional<Map<Long, Map<String, Integer>>> seatLayoutMap = clientService.getSeatLayout(screenId);
		if(seatLayoutMap.isPresent() && movieScheduleService.existsByScheduleId(scheduleId)) {
			Optional<JSONObject> response = movieScheduleService.blockSelectedSeats(scheduleId, screenId, layoutId, new JSONObject(requestMessage), seatLayoutMap.get());
			if(response.isPresent()) {
				if(!response.get().optBoolean("status", true)) {
					return ResponseEntity.badRequest().body(response.get());
				}else {
					return ResponseEntity.ok(response.get());
				}
			}
			
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/schedule/{scheduleId}/book")
	public ResponseEntity<Object> bookSeats(@PathVariable long scheduleId, @RequestBody String requestMessage){
		if(movieScheduleService.existsByScheduleId(scheduleId)) {
			Optional<?> response = movieScheduleService.bookSeats(scheduleId,new JSONObject(requestMessage));
			if(response.isPresent()) {
				return ResponseEntity.ok(response.get());
			}
			return ResponseEntity.badRequest().body(requestMessage);
		}
		return ResponseEntity.notFound().build();
	}
	
}
