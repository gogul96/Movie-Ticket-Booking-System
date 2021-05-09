package com.gogul.tickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.Movies;

public interface MoviesRepository extends JpaRepository<Movies, Long>{

}
