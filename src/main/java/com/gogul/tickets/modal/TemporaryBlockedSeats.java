package com.gogul.tickets.modal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class TemporaryBlockedSeats {
	
	@Id
	private long userId;
	
	@Column
	private String blockedSeatKey;
	
	@Column
	private String seats;
	
	@Column
	private LocalDateTime expiryDateTime;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Set<String> getSeats() {
		return new HashSet<String>(Arrays.asList(seats.split(",")));
	}
	public void setSeats(Set<String> seats) {
		this.seats = seats.parallelStream().collect(Collectors.joining(","));
	}
	public String getBlockedSeatKey() {
		return blockedSeatKey;
	}
	public void setBlockedSeatKey(String blockedSeatKey) {
		this.blockedSeatKey = blockedSeatKey;
	}
	public LocalDateTime getExpiryDateTime() {
		return expiryDateTime;
	}
	public void setExpiryDateTime(LocalDateTime localDateTime) {
		this.expiryDateTime = localDateTime;
	}

}
