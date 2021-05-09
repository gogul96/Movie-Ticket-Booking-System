package com.gogul.tickets.modal;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
public class SeatLayout {

	@Id
	private long layoutId;

	@CreationTimestamp
    private LocalDateTime createdDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
    
    @Column
    private long screenId;
    
    @Column
    private String name;
    
    @Column
    private String rowPattern;
    
    @Column
    private String columnPattern;
    
    @Column
    private Boolean isBookingEnabled;
    
    @Column
    private int totalSeats;
    
    @Column
    private float eachSeatTicketPrice;

	public long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(long layoutId) {
		this.layoutId = layoutId;
	}

	public long getScreenId() {
		return screenId;
	}

	public void setScreenId(long screenId) {
		this.screenId = screenId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRowPattern() {
		return rowPattern;
	}

	public void setRowPattern(String rowPattern) {
		this.rowPattern = rowPattern;
	}

	public String getColumnPattern() {
		return columnPattern;
	}

	public void setColumnPattern(String columnPattern) {
		this.columnPattern = columnPattern;
	}

	public void setBookingEnabled(Boolean isBookingEnabled) {
		this.isBookingEnabled = isBookingEnabled;
	}

	public Boolean getBookingEnabled() {
		return isBookingEnabled;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public Boolean getIsBookingEnabled() {
		return isBookingEnabled;
	}

	public void setIsBookingEnabled(Boolean isBookingEnabled) {
		this.isBookingEnabled = isBookingEnabled;
	}

	public float getEachSeatTicketPrice() {
		return eachSeatTicketPrice;
	}

	public void setEachSeatTicketPrice(float eachSeatTicketPrice) {
		this.eachSeatTicketPrice = eachSeatTicketPrice;
	}
}
