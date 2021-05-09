package com.gogul.tickets.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gogul.tickets.cache.SeatsCache;
import com.gogul.tickets.modal.ApplicationUsers;
import com.gogul.tickets.modal.Client;
import com.gogul.tickets.modal.Screens;
import com.gogul.tickets.modal.SeatLayout;
import com.gogul.tickets.modal.Theatres;
import com.gogul.tickets.repository.ApplicationUsersRepository;
import com.gogul.tickets.repository.ClientRepository;
import com.gogul.tickets.repository.ScreenRepository;
import com.gogul.tickets.repository.TheatreRepository;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private TheatreRepository theatreRepository;
	@Autowired
	private ScreenRepository screenRepository;
	
	@Autowired
	private ApplicationUsersRepository applicationUsersRepository;
	
	@Autowired
	private SeatsCache seatsCache;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Optional<Client> findByUsername(String username) {
		return clientRepository.findByUsername(username);
	}
	
	private void createApplicationUsers(long id, String username, String password,String role) {
		ApplicationUsers applicationUser = new ApplicationUsers();
		applicationUser.setRole(role.toUpperCase());
		applicationUser.setUsername(username);
		applicationUser.setPassword(passwordEncoder.encode(password));
		applicationUsersRepository.save(applicationUser);
	}
	
	public Client saveClient(JSONObject requestObject, boolean toPresist) throws Exception{
		Client client = new Client();		
		client.setClientId(requestObject.getLong("clientId"));
		client.setName(requestObject.getString("name"));
		client.setUsername(requestObject.getString("username"));
		client.setCreatedBy("admin");
		createApplicationUsers(client.getClientId(), client.getUsername(), requestObject.getString("password"), "CLIENT");
		if(!requestObject.isNull("theatres")) {
			List<Theatres> theatresList = new ArrayList<Theatres>();
			requestObject.getJSONArray("theatres").forEach(object->{
				theatresList.add(saveTheatre(client.getClientId(), (JSONObject) object, false));
			});
			client.setTheatres(theatresList);
		}
		if(toPresist) {
			clientRepository.save(client);
		}
		return client;
	}

	public Theatres saveTheatre(long clientId,JSONObject theatreObject, boolean toPresist) {
		Theatres theatres = new Theatres();
		theatres.setClientId(clientId);
		theatres.setName(theatreObject.getString("name"));
		theatres.setDescription(theatreObject.optString("description",""));
		theatres.setOpened(theatreObject.getBoolean("isOpened"));
		theatres.setBookingEnabled(theatreObject.getBoolean("isBookingEnabled"));
		if(!theatreObject.isNull("screens")) {
			List<Screens> screensList = new ArrayList<>();
			theatreObject.getJSONArray("screens").forEach(object->{
				screensList.add(saveScreen(theatres.getTheatreId(),(JSONObject) object));
			});
			theatres.setScreens(screensList);
		}
		if(toPresist) {
			theatreRepository.save(theatres);
		}
		return theatres;
	}
	
	private Screens saveScreen(long theatreId, JSONObject screenObject) {
		Screens screens = new Screens();
		screens.setTheatreId(theatreId);
		screens.setName(screenObject.getString("name"));
		screens.setTotalCapacity(screenObject.getInt("totalCapacity"));
		screens.setBookingEnabled(screenObject.getBoolean("bookingEnabled"));
		if(!screenObject.isNull("seatLayout")) {
			List<SeatLayout> seatLayoutList = new ArrayList<>();
			screenObject.getJSONArray("seatLayout").forEach(object->{
				seatLayoutList.add(saveLayouts(screens.getScreenId(),(JSONObject) object));
			});
			screens.setSeatLayouts(seatLayoutList);
		}
		return screens;
	}

	private SeatLayout saveLayouts(long screenId, JSONObject object) {
		try {
			SeatLayout layout = new SeatLayout();
			layout.setName(object.getString("name"));
			layout.setScreenId(screenId);
			layout.setBookingEnabled(object.getBoolean("bookingEnabled"));
			layout.setRowPattern(object.getString("rowPattern"));
			layout.setColumnPattern(object.getString("columnPattern"));
			layout.setTotalSeats(object.getInt("totalSeats"));
			if(seatsCache.exists(screenId)) {
				seatsCache.remove(screenId);
			}
			return validateSeatLayout(layout);
		}catch(Exception e) {
			return null;
		}
	}

	public SeatLayout validateSeatLayout(SeatLayout layout) throws Exception {
		Map<String,Integer> seatLayoutMap = getLayoutMap(layout.getRowPattern(),layout.getColumnPattern());
		int totalSeats = seatLayoutMap.values().stream().reduce(0, Integer::sum);
		if(layout.getTotalSeats()!=totalSeats) {
			throw new Exception("Invalid Pattern or Total Seats Count");
		}
		return layout;
	}
	
	public Optional<Map<Long,Map<String,Integer>>> getSeatLayout(long screenId){
		Optional<Screens> screen = screenRepository.findById(screenId);
		if(screen.isPresent()) {
			if(seatsCache.exists(screenId)) {
				return seatsCache.get(screenId);
			}
			Map<Long,Map<String,Integer>> seatlayouts = new HashMap<>();
			List<SeatLayout> seatLayoutsList = screen.get().getSeatLayouts();
			seatLayoutsList.forEach(layout->{
				try {
					Map<String,Integer> layoutMap = getLayoutMap(layout.getRowPattern(), layout.getColumnPattern());
					seatlayouts.put(layout.getLayoutId(), layoutMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			seatsCache.set(screenId,seatlayouts);
			return Optional.of(seatlayouts);
		}
		return null;
	}

	/*
	 *  "rowPattern": "{A-E,F,G-J}",
         "columnPattern": "{10,6,11}",
	 */
	public Map<String,Integer> getLayoutMap(String rowPattern,String columnPattern) throws Exception {
		Map<String,Integer> layoutMap = new HashMap<>();
		try {
		List<Integer> columns = parseLayoutColumnPattern(columnPattern);
		int columnIndex = 0;
		if(columns.size()==0 || rowPattern.isEmpty() || rowPattern.length()<3) {
			return layoutMap;
		}
		rowPattern = rowPattern.substring(1, rowPattern.length()-1);
		if(rowPattern.isEmpty()) {
			return layoutMap;
		}
		for(String letters: rowPattern.split(",")) {
			if(letters.length()>=3) {
				int startChar = (int)letters.charAt(0);
				int endChar = (int)letters.charAt(2);
				for(int i=startChar; i<=endChar;i++) {
					layoutMap.put(Character.toString((char)i),columns.get(columnIndex));
				}
			}else {
				layoutMap.put(letters,columns.get(columnIndex));
			}
			columnIndex++;
		}
		}catch(Exception e) {
			throw new Exception("Invalid Row Pattern,"+e.getLocalizedMessage());
		}
		return layoutMap;
	}

	private List<Integer> parseLayoutColumnPattern(String columnPattern) throws Exception {
		List<Integer> columns = new ArrayList<Integer>();
		try {
				if(columnPattern.isEmpty() && columnPattern.length()<3) {
					return columns;
				}
				columnPattern = columnPattern.substring(1, columnPattern.length()-1);
				if(columnPattern.isEmpty()) {
					return columns;
				}
				for(String numbers: columnPattern.split(",")) {
					columns.add(Integer.parseInt(numbers));
				}
			}catch(Exception e) {
				throw new Exception("Invalid Column Pattern,"+e.getLocalizedMessage());
			}
		return columns;
	}

	public boolean deleteClient(long clientId) {
		if(clientRepository.existsById(clientId)) {
			clientRepository.deleteById(clientId);
			return true;
		}else {
			return false;
		}
	}
	
	public List<Client> findAll() {
		return clientRepository.findAll();
	}

	public Client findByID(long clientId) {
		return clientRepository.getOne(clientId);
	}

	public List<Theatres> findByClientId(long clientId) {
		return theatreRepository.findByClientId(clientId);
	}

	public boolean existsById(long clientId) {
		return clientRepository.existsById(clientId);
	}
	
	public boolean existsByTheatreId(long theatreId) {
		return theatreRepository.existsById(theatreId);
	}
	
	public boolean existsByScreenId(long screenId) {
		return screenRepository.existsById(screenId);
	}

	public boolean deleteTheatre(long clientId, long theatreId) {
		try {
			List<Theatres> theatres= clientRepository.getOne(clientId).getTheatres();
			for(Theatres theatre: theatres) {
				if(theatre.getTheatreId()==theatreId) {
					theatres.remove(theatre);
					return true;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Theatres updateTheatre(long clientId, long theatreId, JSONObject theatreObject) {
		try {
			List<Theatres> theatres= clientRepository.getOne(clientId).getTheatres();
			for(Theatres theatre: theatres) {
				if(theatre.getTheatreId()==theatreId) {
					theatre.setName(theatreObject.getString("name"));
					theatre.setDescription(theatreObject.optString("description",""));
					theatre.setOpened(theatreObject.getBoolean("isOpened"));
					theatre.setBookingEnabled(theatreObject.getBoolean("isBookingEnabled"));
					return theatre;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
