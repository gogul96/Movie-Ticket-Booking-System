package com.gogul.tickets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gogul.tickets.data.Constants;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class MovieServiceTest {

	@Autowired
	private MovieScheduleService movieScheduleService;
	
	@Autowired
	private ClientService clientService;
	 
	private long DUMMYID = 9999L;
	private long THEATREID = 3001L;
	private long SCREENID=4001L;
	private long LAYOUTID=5001L;
	private long MOVIEID = 20001L;
	private long SCHEDULEID = 30001L;
	private long USERID = 1001L;
	
	@Test
	@Order(1)
	public void checkSchedule() throws Exception {
		
		assertEquals(movieScheduleService.existsByScheduleId(DUMMYID),false);
		assertEquals(movieScheduleService.existsByScheduleId(SCHEDULEID),true);
		
		assertEquals(movieScheduleService.findScheduleByMovieId(DUMMYID).size(), 0);
		assertEquals(movieScheduleService.findScheduleByMovieId(MOVIEID).size(), 1);
		
		assertEquals(movieScheduleService.findScheduleByTheatreId(DUMMYID).size(), 0);
		assertEquals(movieScheduleService.findScheduleByTheatreId(THEATREID).size(), 1);
		
		List<Long> theatreIds = new ArrayList<Long>();
		assertEquals(movieScheduleService.findScheduleByListOfTheatreId(theatreIds).size(),0);
		theatreIds.add(THEATREID);
		assertEquals(movieScheduleService.findScheduleByListOfTheatreId(theatreIds).size(),1);
		theatreIds.add(THEATREID+1);
		theatreIds.add(THEATREID+2);
		assertEquals(movieScheduleService.findScheduleByListOfTheatreId(theatreIds).size(),3);
		
		
		assertEquals(movieScheduleService.findScheduleByScreenId(DUMMYID).size(),0);
		assertEquals(movieScheduleService.findScheduleByScreenId(SCREENID).size(),1);
		
		assertEquals(movieScheduleService.findAll().size(), 5);
		
		List<Long> movieIdList = new ArrayList<Long>();
		assertEquals(movieScheduleService.findScheduleWithParam(movieIdList).size(), 0);
		assertEquals(movieScheduleService.findScheduleByTheatreIdWithParam(DUMMYID,movieIdList).size(), 0);
		assertEquals(movieScheduleService.findScheduleByTheatreIdWithParam(THEATREID,movieIdList).size(), 0);
		assertEquals(movieScheduleService.findScheduleByScreenIdWithParam(DUMMYID,movieIdList).size(), 0);
		assertEquals(movieScheduleService.findScheduleByScreenIdWithParam(SCREENID,movieIdList).size(), 0);
		
		movieIdList.add(MOVIEID);
		movieIdList.add(MOVIEID+1);
		assertEquals(movieScheduleService.findScheduleWithParam(movieIdList).size(), 2);
		assertEquals(movieScheduleService.findScheduleByTheatreIdWithParam(DUMMYID,movieIdList).size(), 0);
		assertEquals(movieScheduleService.findScheduleByTheatreIdWithParam(THEATREID,movieIdList).size(), 1);
		assertEquals(movieScheduleService.findScheduleByScreenIdWithParam(DUMMYID,movieIdList).size(), 0);
		assertEquals(movieScheduleService.findScheduleByScreenIdWithParam(SCREENID,movieIdList).size(), 1);
	}
	
	
	@Test
	@Order(2)
	public void checkSeatsListMethod() throws Exception{
		Optional<Map<Long, Map<String, Integer>>> seatMap = clientService.getSeatLayout(SCREENID);
		assertEquals(seatMap.isPresent(), true);
		assertEquals(seatMap.get().containsKey(LAYOUTID),true);
		assertEquals(seatMap.get().get(LAYOUTID).size(),10);
		Optional<Map<Long,Map<String,List<String>>>> seats = movieScheduleService.findAvailableSeats(
				SCREENID,SCHEDULEID,seatMap.get());
		assertEquals(seats.isPresent(), true);
		assertEquals(seats.get().size(),1);
		assertEquals(seats.get().containsKey(LAYOUTID),true);
		assertEquals(seats.get().get(LAYOUTID).size(),3);
		assertEquals(seats.get().get(LAYOUTID).containsKey("available"),true);
		assertEquals(seats.get().get(LAYOUTID).containsKey("booked"),true);
		assertEquals(seats.get().get(LAYOUTID).containsKey("blocked"),true);
		assertEquals(seats.get().get(LAYOUTID).get("available").size(),100);
		assertEquals(seats.get().get(LAYOUTID).get("booked").size(),0);
		assertEquals(seats.get().get(LAYOUTID).get("blocked").size(),0);
		assertEquals(seats.get().get(LAYOUTID).values().stream().flatMap(object->object.stream()).count(),100);
		
	}
	
	@Test
	@Order(3)
	public void checkBlockSeatMethod() throws Exception{
		Optional<Map<Long, Map<String, Integer>>> seatMap = clientService.getSeatLayout(SCREENID);
		Optional<Map<Long,Map<String,List<String>>>> seats = movieScheduleService.findAvailableSeats(
				SCREENID,SCHEDULEID,seatMap.get());
		JSONObject object = new JSONObject();
		object.put("userId",USERID);
		object.put("selectedSeats",new JSONArray());
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, DUMMYID, DUMMYID, object, seatMap.get()).isPresent(),false);
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, SCREENID, DUMMYID, object, seatMap.get()).isPresent(),false);
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, SCREENID, LAYOUTID, object, seatMap.get())
				.get().getBoolean("status"),false);
		
		object.put("selectedSeats", new JSONArray(Arrays.asList("A1","A2","A3","A4","A5","A6","A7")));
		
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, SCREENID, LAYOUTID, object, seatMap.get())
				.get().getBoolean("status"),false);
		
		object.put("selectedSeats", new JSONArray(Arrays.asList("A1","A2","A3","A4","A5","A6")));
		
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, SCREENID, LAYOUTID, object, seatMap.get())
				.get().getBoolean("status"),true);
		
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, SCREENID, LAYOUTID, object, seatMap.get())
				.get().getBoolean("status"),false);
		
		seats = movieScheduleService.findAvailableSeats(
				SCREENID,SCHEDULEID,seatMap.get());
		assertEquals(seats.get().get(LAYOUTID).get("available").size(),94);
		assertEquals(seats.get().get(LAYOUTID).get("booked").size(),0);
		assertEquals(seats.get().get(LAYOUTID).get("blocked").size(),6);
		
		object.put("selectedSeats", new JSONArray(Arrays.asList("B1","B2","B3","B4","B5")));
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, SCREENID, LAYOUTID, object, seatMap.get())
				.get().getBoolean("status"),true);
		
		seats = movieScheduleService.findAvailableSeats(
				SCREENID,SCHEDULEID,seatMap.get());
		assertEquals(seats.get().get(LAYOUTID).get("available").size(),95);
		assertEquals(seats.get().get(LAYOUTID).get("booked").size(),0);
		assertEquals(seats.get().get(LAYOUTID).get("blocked").size(),5);
		
		
		object.put("selectedSeats", new JSONArray(Arrays.asList("B6","B7","B8","B9","B10")));
		object.put("userId",USERID+1);
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, SCREENID, LAYOUTID, object, seatMap.get())
				.get().getBoolean("status"),true);
		
		seats = movieScheduleService.findAvailableSeats(
				SCREENID,SCHEDULEID,seatMap.get());
		assertEquals(seats.get().get(LAYOUTID).get("available").size(),90);
		assertEquals(seats.get().get(LAYOUTID).get("booked").size(),0);
		assertEquals(seats.get().get(LAYOUTID).get("blocked").size(),10);
		
		object.put("selectedSeats", new JSONArray(Arrays.asList("B3","B4","B5","B6","B7","B8")));
		object.put("userId",USERID+2);
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, SCREENID, LAYOUTID, object, seatMap.get())
				.get().getBoolean("status"),true);
		seats = movieScheduleService.findAvailableSeats(
				SCREENID,SCHEDULEID,seatMap.get());
		assertEquals(seats.get().get(LAYOUTID).get("available").size(),94);
		assertEquals(seats.get().get(LAYOUTID).get("booked").size(),0);
		assertEquals(seats.get().get(LAYOUTID).get("blocked").size(),6);
		
		Thread.sleep((Constants.BLOCK_SEATS_EXPIRY_TIME_IN_SECONDS+1)*1000);
		
		seats = movieScheduleService.findAvailableSeats(
				SCREENID,SCHEDULEID,seatMap.get());
		assertEquals(seats.get().get(LAYOUTID).get("available").size(),100);
		assertEquals(seats.get().get(LAYOUTID).get("booked").size(),0);
		assertEquals(seats.get().get(LAYOUTID).get("blocked").size(),0);	
	}
	
	@Test
	@Order(4)
	public void checkBookSeatMethod() throws Exception{
		Optional<Map<Long, Map<String, Integer>>> seatMap = clientService.getSeatLayout(SCREENID);
		JSONObject object = new JSONObject();
		object.put("userId",USERID);
		object.put("screenId", DUMMYID);
		object.put("seatLayoutId", DUMMYID);
		object.put("seats",new JSONArray());
		
		assertEquals(movieScheduleService.bookSeats(SCHEDULEID, object).isPresent(),false);
		
		object.put("screenId", SCREENID);
		assertEquals(movieScheduleService.bookSeats(SCHEDULEID, object).isPresent(),false);
		
		object.put("seatLayoutId", LAYOUTID);
		assertEquals(movieScheduleService.bookSeats(SCHEDULEID, object).isPresent(),false);
		
		object.put("seats", new JSONArray(Arrays.asList("A1","A2","A3","A4","A5","A6","A7")));
		assertEquals(movieScheduleService.bookSeats(SCHEDULEID, object).isPresent(),false);
		
		object.put("seats", new JSONArray(Arrays.asList("A1","A2","A3","A4","A5","A6")));
		assertEquals(movieScheduleService.bookSeats(SCHEDULEID, object).isPresent(),false);
		
		object.put("selectedSeats", new JSONArray(Arrays.asList("A1","A2","A3","A4","A5","A6")));
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, SCREENID, LAYOUTID, object, seatMap.get())
				.get().getBoolean("status"),true);
		Optional<Map<Long,Map<String,List<String>>>> seats = movieScheduleService.findAvailableSeats(
				SCREENID,SCHEDULEID,seatMap.get());
		assertEquals(seats.get().get(LAYOUTID).get("available").size(),94);
		assertEquals(seats.get().get(LAYOUTID).get("booked").size(),0);
		assertEquals(seats.get().get(LAYOUTID).get("blocked").size(),6);
		
		assertEquals(movieScheduleService.bookSeats(SCHEDULEID, object).isPresent(),true);
		
		seats = movieScheduleService.findAvailableSeats(
				SCREENID,SCHEDULEID,seatMap.get());
		assertEquals(seats.get().get(LAYOUTID).get("available").size(),94);
		assertEquals(seats.get().get(LAYOUTID).get("booked").size(),6);
		assertEquals(seats.get().get(LAYOUTID).get("blocked").size(),0);
		
		assertEquals(movieScheduleService.blockSelectedSeats(
				SCHEDULEID, SCREENID, LAYOUTID, object, seatMap.get())
				.get().getBoolean("status"),false);
	}
	
}
