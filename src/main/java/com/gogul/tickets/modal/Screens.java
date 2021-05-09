package com.gogul.tickets.modal;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
public class Screens {

	@Id
	private long screenId;

	@CreationTimestamp
    private LocalDateTime createdDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
    
    @Column
    private long theatreId;
    
    @Column
    private String name;
    
    @Column
    private boolean isBookingEnabled;
    
    @Column
    private int totalCapacity;
    
    @Column
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<SeatLayout> seatLayouts;

	public long getScreenId() {
		return screenId;
	}

	public void setScreenId(long screenId) {
		this.screenId = screenId;
	}

	public long getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(long theatreId) {
		this.theatreId = theatreId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isBookingEnabled() {
		return isBookingEnabled;
	}

	public void setBookingEnabled(boolean isBookingEnabled) {
		this.isBookingEnabled = isBookingEnabled;
	}

	public int getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(int totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public List<SeatLayout> getSeatLayouts() {
		return seatLayouts;
	}

	public void setSeatLayouts(List<SeatLayout> seatLayouts) {
		this.seatLayouts = seatLayouts;
	}
}
