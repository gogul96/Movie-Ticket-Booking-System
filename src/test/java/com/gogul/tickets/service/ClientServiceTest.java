package com.gogul.tickets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gogul.tickets.modal.SeatLayout;

@SpringBootTest
public class ClientServiceTest {
	
	@Autowired
	private ClientService clientService;
	
	@Test
	public void testLayoutMap() throws Exception{
		assertEquals(clientService.getLayoutMap("", "").size(), 0);
		assertEquals(clientService.getLayoutMap("", "{}").size(), 0);
		assertEquals(clientService.getLayoutMap("{}", "{}").size(), 0);
		assertEquals(clientService.getLayoutMap("{A-E,F,G-J}", "{10,6,11}").size(), 10);
		assertEquals(clientService.getLayoutMap("{A-E,F,G-J}", "{10,6,11}").get("F"), 6);
		assertEquals(clientService.getLayoutMap("{A-E,F,G-J}", "{10,6,11}").get("A"), 10);
		assertEquals(clientService.getLayoutMap("{A-E,F,G-J}", "{10,6,11}").get("D"), 10);
		assertEquals(clientService.getLayoutMap("{A-E,F,G-J}", "{10,6,11}").get("E"), 10);
		assertEquals(clientService.getLayoutMap("{A-E,F,G-J}", "{10,6,11}").get("G"), 11);
		assertEquals(clientService.getLayoutMap("{A-E,F,G-J}", "{10,6,11}").get("J"), 11);
	}
	
	@Test
	public void testTotalSeatsCalc() throws Exception{
		SeatLayout seatLayout = new SeatLayout();
		seatLayout.setRowPattern("{A-E,F,G-J}");
		seatLayout.setColumnPattern("{10,6,11}");
		seatLayout.setTotalSeats(100);
		assertEquals(clientService.validateSeatLayout(seatLayout).getTotalSeats(), seatLayout.getTotalSeats());
	}
}

