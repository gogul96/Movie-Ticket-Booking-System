package com.gogul.tickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.Theatres;

public interface TheatreRepository extends JpaRepository<Theatres, Long>{

	public List<Theatres> findByClientId(long clientId);
	
}
