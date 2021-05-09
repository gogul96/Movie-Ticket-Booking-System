package com.gogul.tickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.TemporaryBlockedSeats;

public interface BlockedSeatRepository extends JpaRepository<TemporaryBlockedSeats, Long>{
	
	public List<TemporaryBlockedSeats> findByBlockedSeatKey(String blockedSeatKey);
}
