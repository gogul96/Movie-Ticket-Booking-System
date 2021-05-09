package com.gogul.tickets.modal;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table
public class BookingHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long bookingHistoryId;

	@CreationTimestamp
    private LocalDateTime createdDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updatedDateTime;
    
    @Column
    private long scheduleId;
    
    @Column
    private long userId;
    
    @Column
    private double amount;
    
    @Column
    private int totalSeatBooked;
    
    @Column
    private LocalDateTime transactionTimestamp;
    
    @Column
    private String paymentBy;

	public long getBookingHistoryId() {
		return bookingHistoryId;
	}

	public void setBookingHistoryId(long bookingHistoryId) {
		this.bookingHistoryId = bookingHistoryId;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getTotalSeatBooked() {
		return totalSeatBooked;
	}

	public void setTotalSeatBooked(int totalSeatBooked) {
		this.totalSeatBooked = totalSeatBooked;
	}

	public LocalDateTime getTransactionTimestamp() {
		return transactionTimestamp;
	}

	public void setTransactionTimestamp(LocalDateTime transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
	}

	public String getPaymentBy() {
		return paymentBy;
	}

	public void setPaymentBy(String paymentBy) {
		this.paymentBy = paymentBy;
	}
    
}
