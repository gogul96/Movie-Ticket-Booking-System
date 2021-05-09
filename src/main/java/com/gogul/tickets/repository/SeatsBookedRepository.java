package com.gogul.tickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.SeatsBooked;

public interface SeatsBookedRepository extends JpaRepository<SeatsBooked, Long>{

	public List<SeatsBooked> findByScheduleId(long scheduleId);
}
