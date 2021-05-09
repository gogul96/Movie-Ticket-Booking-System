package com.gogul.tickets.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gogul.tickets.data.Constants;
import com.gogul.tickets.modal.BookingHistory;
import com.gogul.tickets.modal.MoviesSchedule;
import com.gogul.tickets.modal.Screens;
import com.gogul.tickets.modal.SeatLayout;
import com.gogul.tickets.modal.SeatsBooked;
import com.gogul.tickets.modal.TemporaryBlockedSeats;
import com.gogul.tickets.repository.BlockedSeatRepository;
import com.gogul.tickets.repository.BookingHistoryRepository;
import com.gogul.tickets.repository.ScheduleRepository;
import com.gogul.tickets.repository.ScreenRepository;
import com.gogul.tickets.repository.SeatsBookedRepository;

@Service
public class MovieScheduleService {

	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private BookingHistoryRepository bookingHistoryRepository;
	
	@Autowired
	private SeatsBookedRepository seatsBookedRepository;
	
	@Autowired
	private ScreenRepository screenRepository;
	
	@Autowired
	private BlockedSeatRepository blockSeatRepository; 
	
	public List<MoviesSchedule> findScheduleByMovieId(long movieId) {
		return scheduleRepository.findByMovieId(movieId);
	}
	
	public List<MoviesSchedule> findScheduleByTheatreId(long theatreId) {
		return scheduleRepository.findByTheatreId(theatreId);
	}
	
	public List<MoviesSchedule> findScheduleByListOfTheatreId(List<Long> theatreIds){
		return findAll().stream().filter(schedule-> theatreIds.contains(schedule.getTheatreId())).collect(Collectors.toList());
	}
	
	public List<MoviesSchedule> findScheduleByScreenId(long screenId) {
		return scheduleRepository.findByScreenId(screenId);
	}

	public List<MoviesSchedule> findAll() {
		return scheduleRepository.findAll();
	}

	public List<MoviesSchedule> findScheduleWithParam(List<Long> movieIdList) {
		return findAll().stream().filter(schedule-> movieIdList.contains(schedule.getMovieId())).collect(Collectors.toList());
	}

	public List<MoviesSchedule> findScheduleByTheatreIdWithParam(long theatreId, List<Long> movieIdList) {
		return findScheduleByTheatreId(theatreId).stream().filter(schedule-> movieIdList.contains(schedule.getMovieId())).collect(Collectors.toList());
	}

	public List<MoviesSchedule> findScheduleByScreenIdWithParam(long screenId, List<Long> movieIdList) {
		return findScheduleByScreenId(screenId).stream().filter(schedule-> movieIdList.contains(schedule.getMovieId())).collect(Collectors.toList());
	}

	public boolean existsByScheduleId(long scheduleId) {
		return scheduleRepository.existsById(scheduleId);
	}

	public Optional<Map<Long,Map<String,List<String>>>> findAvailableSeats(long screenId, long scheduleId, Map<Long, Map<String, Integer>> seats) {
		Map<Long,Map<String,List<String>>> seatAvailablity = new HashMap<Long,Map<String,List<String>>>(); 
		seats.forEach((layoutKey,layoutMap)->{ 
			String seatId = scheduleId + "_" + layoutKey;
			seatAvailablity.put(layoutKey,getAllSeats(scheduleId,seatId,layoutMap));
		});
		return Optional.of(seatAvailablity);
	}
	
	private Map<String,List<String>> getAllSeats(long scheduleId,String layoutKey, Map<String, Integer> layoutMap) {
		Map<String,List<String>> seatStatus = new HashMap<String,List<String>>();
		Set<String> blockedSeats = blockSeatRepository.findByBlockedSeatKey(layoutKey)
				.parallelStream()
				.filter(object->object.getExpiryDateTime().isAfter(LocalDateTime.now()))
				.flatMap(object-> object.getSeats().stream()).collect(Collectors.toSet());
		seatStatus.put("blocked", new ArrayList<>(blockedSeats));
		Set<String> bookedSeats = seatsBookedRepository.findByScheduleId(scheduleId)
				.parallelStream()
				.flatMap(object->object.getSeatNo().stream())
				.collect(Collectors.toSet());
		seatStatus.put("booked", new ArrayList<>(bookedSeats));
		List<String> availableSeats = new ArrayList<String>();
		layoutMap.forEach((key,value)->{
			while(value>0) {
				String seatName = key+value;
				if(!blockedSeats.contains(seatName) && !bookedSeats.contains(seatName)) {
					availableSeats.add(seatName);
				}
				value--;
			}
		});
		seatStatus.put("available", availableSeats);
		return seatStatus;
	}
	
	
	public Optional<JSONObject> blockSelectedSeats(long scheduleId, long screenId,long layoutId,JSONObject requestMessage, Map<Long, Map<String, Integer>> seatLayout){
		if(!screenRepository.existsById(screenId) || 
				screenRepository.findById(screenId).get().getSeatLayouts().parallelStream()
				.filter(layout->layout.getLayoutId()==layoutId).count()!=1) {
			return Optional.ofNullable(null);
		}
		try {
			JSONObject response = new JSONObject();
			long userId = requestMessage.optLong("userId",0L);
			if(!requestMessage.has("selectedSeats")) {
				return Optional.ofNullable(null);
			}
			Set<String> seatSelected = requestMessage.getJSONArray("selectedSeats").toList().stream().parallel().map(object->object.toString()).collect(Collectors.toSet());
			if(seatSelected.size()<1 || seatSelected.size()>Constants.MAX_TICKET_BOOKING_ALLOWED) {
				response.put("status", false);
				response.put("errorMessage", "Max 6 seats only allowed");
				return Optional.of(response);
			}
			String blockSeatKey = scheduleId +"_"+layoutId;
			
			// Reallocate seat based on the max selected seats, if tied then pick first one
			/*
			 * Set<String> blockedSeats =
			 * blockSeatRepository.findByBlockedSeatKey(blockSeatKey) .parallelStream()
			 * .filter(object->object.getExpiryDateTime().isAfter(LocalDateTime.now()))
			 * .flatMap(object->object.getSeats().stream()) .collect(Collectors.toSet());
			 */
			Set<String> bookedSeats = seatsBookedRepository.findByScheduleId(scheduleId)
					.parallelStream()
					.flatMap(object->object.getSeatNo().stream())
					.collect(Collectors.toSet());
			if(seatSelected.parallelStream()
					.anyMatch(seat-> bookedSeats.contains(seat))) {
				response.put("status", false);
				response.put("errorMessage", "Seats are unavailable");	
			}else {
				List<TemporaryBlockedSeats> blockedSeats = blockSeatRepository.findByBlockedSeatKey(blockSeatKey);
				if(blockedSeats.isEmpty()) {
					createTemporaryBlockedSeats(userId, seatSelected, blockSeatKey);
				}else {
					blockedSeats = blockedSeats.parallelStream()
													.filter(blockedSeat-> (blockedSeat.getSeats()
															.parallelStream()
															.anyMatch(seat->seatSelected.contains(seat))))
													.collect(Collectors.toList());
					if(blockedSeats.isEmpty()) {
						createTemporaryBlockedSeats(userId, seatSelected, blockSeatKey);
					}else {
						OptionalInt maxSeatBlocked = blockedSeats.parallelStream()
														.mapToInt(blockedSeat-> blockedSeat.getSeats().size())
														.max();
						if(maxSeatBlocked.isPresent() && seatSelected.size()>maxSeatBlocked.getAsInt()) {
							blockSeatRepository.deleteAll(blockedSeats);
							createTemporaryBlockedSeats(userId, seatSelected, blockSeatKey);
						}else {
							response.put("status", false);
							response.put("errorMessage", "Seats are unavailable");
							return Optional.of(response);
						}	
					}
				}
				response.put("status", true);	
			}
			return Optional.of(response);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(null);
	}

	private void createTemporaryBlockedSeats(long userId, Set<String> seatSelected, String blockSeatKey) {
		TemporaryBlockedSeats temporaryBlockedSeats = blockSeatRepository.findById(userId).orElse(new TemporaryBlockedSeats());
		temporaryBlockedSeats.setUserId(userId);
		temporaryBlockedSeats.setSeats(seatSelected);
		temporaryBlockedSeats.setBlockedSeatKey(blockSeatKey);
		temporaryBlockedSeats.setExpiryDateTime(LocalDateTime.now().plusSeconds(Constants.BLOCK_SEATS_EXPIRY_TIME_IN_SECONDS));
		blockSeatRepository.save(temporaryBlockedSeats);
	}

	public Optional<BookingHistory> bookSeats(long scheduleId,JSONObject requestMessage) {
		try {
			long userId = requestMessage.optLong("userId",0L);
			long screenId = requestMessage.optLong("screenId",0L);
			long seatLayoutId = requestMessage.optLong("seatLayoutId",0L);
			if(!requestMessage.has("seats")) {
				return Optional.ofNullable(null);
			}
			Set<String> seatNumbers = requestMessage.getJSONArray("seats").toList().stream().map(value->value.toString()).collect(Collectors.toSet());
			if(seatNumbers.size()<1 || seatNumbers.size()>Constants.MAX_TICKET_BOOKING_ALLOWED) {
				return Optional.ofNullable(null);
			}
			Optional<TemporaryBlockedSeats> blockedSeatByUser = blockSeatRepository.findById(userId); 
			if(!blockedSeatByUser.isPresent() || blockedSeatByUser.get().getExpiryDateTime().isBefore(LocalDateTime.now())
					|| !blockedSeatByUser.get().getSeats().containsAll(seatNumbers)) {
				return Optional.ofNullable(null);
			}
			
			Optional<Screens> screens = screenRepository.findById(screenId);
			if(!screens.isPresent()) {
				return Optional.ofNullable(null);
			}
			
			Optional<SeatLayout> seatLayout = screens.get().getSeatLayouts().stream().filter(layout->layout.getLayoutId()==seatLayoutId).findFirst();
			if(!seatLayout.isPresent()) {
				return Optional.ofNullable(null);
			}
			
			BookingHistory bookingHistory = new BookingHistory();
			bookingHistory.setScheduleId(scheduleId);
			bookingHistory.setUserId(userId);
			bookingHistory.setTransactionTimestamp(LocalDateTime.now());
			bookingHistory.setTotalSeatBooked(seatNumbers.size());
			bookingHistory.setAmount(seatLayout.get().getEachSeatTicketPrice()*seatNumbers.size());
			bookingHistory.setPaymentBy("TEST");
			
			List<SeatsBooked> seatsBookedList = new ArrayList<SeatsBooked>();
			SeatsBooked seatsBooked = new SeatsBooked();
			seatsBooked.setBookingId(bookingHistory.getBookingHistoryId());
			seatsBooked.setScheduleId(scheduleId);
			seatsBooked.setSeatNo(seatNumbers);
			seatsBookedList.add(seatsBooked);
			
			seatsBookedRepository.saveAll(seatsBookedList);
			bookingHistoryRepository.save(bookingHistory);
			blockSeatRepository.deleteById(userId);
			return Optional.of(bookingHistory);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(null);
	}

}
