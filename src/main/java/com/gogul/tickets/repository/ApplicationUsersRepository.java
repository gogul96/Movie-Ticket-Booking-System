package com.gogul.tickets.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.ApplicationUsers;

public interface ApplicationUsersRepository extends JpaRepository<ApplicationUsers, Long>{

	public Optional<ApplicationUsers> findByUsername(String name);
}
