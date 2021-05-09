package com.gogul.tickets.modal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.gogul.tickets.data.Constants;

@Entity
@Table
public class SeatsBooked {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long seatBookingId;
	
	@CreationTimestamp
    private LocalDateTime createdDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
    
    @Column
    private long bookingId;
    
    @Column
    private long scheduleId;
    
    @Column
    private long layoutId;

	@Column
    private String seatNo;

	public long getSeatBookingId() {
		return seatBookingId;
	}

	public void setSeatBookingId(long seatBookingId) {
		this.seatBookingId = seatBookingId;
	}

	public long getBookingId() {
		return bookingId;
	}

	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}
	
    public long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(long layoutId) {
		this.layoutId = layoutId;
	}

	public Set<String> getSeatNo() {
		return new HashSet<String>(Arrays.asList(seatNo.split(Constants.DELIMITER)));
	}

	public void setSeatNo(Set<String> seatNo) {
		this.seatNo = seatNo.parallelStream().collect(Collectors.joining(Constants.DELIMITER));
	}
	
}
