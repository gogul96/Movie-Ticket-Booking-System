package com.gogul.tickets.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.SystemAdmin;

public interface AdminRepository extends JpaRepository<SystemAdmin, Long>{

	public Optional<SystemAdmin> findByUsername(String name);
}
