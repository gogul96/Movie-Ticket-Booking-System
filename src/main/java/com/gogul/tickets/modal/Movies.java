package com.gogul.tickets.modal;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

@Entity
@Table
public class Movies {

	@Id
	private long movieId;

	@CreationTimestamp
    private LocalDateTime createdDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
	@Column
	private String name;
	
	@Column
	@Nullable
	private String description;
	
	@Column
	private String language;
	
	@Column
	private boolean isBookingEnabled;
	
	@Column
	private LocalDate startDate;
	
	@Column
	private LocalDate cutOffDate;
	
	@Column
	private int durationInSeconds;
	
	
	public long getMovieId() {
		return movieId;
	}

	public void setMovieId(long movieId) {
		this.movieId = movieId;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public boolean isBookingEnabled() {
		return isBookingEnabled;
	}

	public void setBookingEnabled(boolean isBookingEnabled) {
		this.isBookingEnabled = isBookingEnabled;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getCutOffDate() {
		return cutOffDate;
	}

	public void setCutOffDate(LocalDate cutOffDate) {
		this.cutOffDate = cutOffDate;
	}

	public int getDurationInSeconds() {
		return durationInSeconds;
	}

	public void setDurationInSeconds(int durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}

}
