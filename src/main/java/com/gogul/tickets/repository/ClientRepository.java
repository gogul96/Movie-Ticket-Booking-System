package com.gogul.tickets.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogul.tickets.modal.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{

	public Optional<Client> findByUsername(String name);
}
