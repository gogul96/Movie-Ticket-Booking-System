package com.gogul.tickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.Screens;

public interface ScreenRepository extends JpaRepository<Screens, Long> {

}
