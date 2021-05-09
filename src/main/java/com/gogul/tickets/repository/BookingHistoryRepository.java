package com.gogul.tickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.BookingHistory;

public interface BookingHistoryRepository extends JpaRepository<BookingHistory, Long>{

}
