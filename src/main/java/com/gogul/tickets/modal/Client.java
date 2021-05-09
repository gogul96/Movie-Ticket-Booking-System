package com.gogul.tickets.modal;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

@Entity
@Table(name= "Cineams")
public class Client {
	
	public Client() {}
	
	@Id
	private long clientId;
	
	@Column
	private String name;
	
	@Column
	private String username;
	
	@Column
	private String createdBy;
	
	@Column
	@Nullable
	@OneToMany(targetEntity = Theatres.class, cascade = CascadeType.DETACH)
	private List<Theatres> theatres;
	
	@CreationTimestamp
    private LocalDateTime createdDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<Theatres> getTheatres() {
		return theatres;
	}

	public void setTheatres(List<Theatres> theatres) {
		this.theatres = theatres;
	}	
	
}
