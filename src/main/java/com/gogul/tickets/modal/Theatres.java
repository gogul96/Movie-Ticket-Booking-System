package com.gogul.tickets.modal;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "Theatres")
public class Theatres {
	
	@Id
	private long theatreId;
	
	@Column
	private long clientId;
	
	@Column
	private String name;
	
	@Column
	@Nullable
	private String description;
	
	@Column
	@ColumnDefault(value = "false")
	private boolean isOpened;
	
	@Column
	@ColumnDefault(value = "false")
	private boolean bookingEnabled;
	
	@Column
	@OneToMany(cascade = CascadeType.REMOVE)
	private List<Screens> screens;
	
	@CreationTimestamp
    private LocalDateTime createdDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

	public long getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(long theatreId) {
		this.theatreId = theatreId;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}

	public boolean isBookingEnabled() {
		return bookingEnabled;
	}

	public void setBookingEnabled(boolean bookingEnabled) {
		this.bookingEnabled = bookingEnabled;
	}

	public List<Screens> getScreens() {
		return screens;
	}

	public void setScreens(List<Screens> screens) {
		this.screens = screens;
	}
	
}
