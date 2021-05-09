package com.gogul.tickets.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gogul.tickets.modal.Movies;
import com.gogul.tickets.repository.MoviesRepository;

@Service
public class MoviesService {

	@Autowired
	private MoviesRepository moviesRepository;
	
	public List<Movies> findAll() {
		return moviesRepository.findAll();
	}

	public Movies findById(long movieId) {
		return moviesRepository.getOne(movieId);
	}

	public boolean deleteByID(long movieId) {
		if(moviesRepository.existsById(movieId)) {
			moviesRepository.deleteById(movieId);
			return true;
		}
		return false;
	}

	public boolean existsById(long movieId) {
		return moviesRepository.existsById(movieId);
	}
	
	public List<Long> findAllMovieIdWithParam(Map<String, String> requestParam) {
		List<Long> movies = new ArrayList<Long>();
		String movieSearchString = requestParam.getOrDefault("movieSearchString", "");
		LocalDate dateToSearch = parseDateTime(requestParam.getOrDefault("dateToSearch", ""),"dd-MM-yyyy");
		String movieLanguage = requestParam.getOrDefault("movieLanguage", "");
		
		try {
			for(Movies movie : findAll()) {
				if((movieSearchString.isEmpty() || movie.getName().contains(movieSearchString)) && 
						(movieLanguage.isEmpty() || movie.getLanguage().equals(movieLanguage)) &&
						(dateToSearch==null || (dateToSearch.isEqual(movie.getStartDate()) 
								|| dateToSearch.isEqual(movie.getCutOffDate()) || (dateToSearch.isAfter(movie.getStartDate())
								&& dateToSearch.isBefore(movie.getCutOffDate()))))) {
					movies.add(movie.getMovieId());
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return movies;
	}

	private LocalDate parseDateTime(String parseString,String dateTimeFormat) {
		LocalDate dateToSearch = null;
		try {
			if(!parseString.isEmpty()) {
				dateToSearch = LocalDate.parse(parseString, DateTimeFormatter.ofPattern(dateTimeFormat));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dateToSearch;
	}
}
