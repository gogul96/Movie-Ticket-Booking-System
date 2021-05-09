package com.gogul.tickets.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.User;

public interface UserRepository extends JpaRepository<User, Long>{
	public Optional<User> findByUsername(String name);
}
