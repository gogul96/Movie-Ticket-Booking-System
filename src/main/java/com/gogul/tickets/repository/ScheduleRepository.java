package com.gogul.tickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.MoviesSchedule;

public interface ScheduleRepository extends JpaRepository<MoviesSchedule, Long>{
	
	public List<MoviesSchedule> findByMovieId(long movieId);
	
	public List<MoviesSchedule> findByTheatreId(long theatreId);
	
	public List<MoviesSchedule> findByScreenId(long screenId);
	
}
