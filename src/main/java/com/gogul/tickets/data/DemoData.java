package com.gogul.tickets.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gogul.tickets.modal.ApplicationUsers;
import com.gogul.tickets.modal.Client;
import com.gogul.tickets.modal.Movies;
import com.gogul.tickets.modal.MoviesSchedule;
import com.gogul.tickets.modal.Screens;
import com.gogul.tickets.modal.SeatLayout;
import com.gogul.tickets.modal.SystemAdmin;
import com.gogul.tickets.modal.Theatres;
import com.gogul.tickets.modal.User;
import com.gogul.tickets.repository.AdminRepository;
import com.gogul.tickets.repository.ApplicationUsersRepository;
import com.gogul.tickets.repository.ClientRepository;
import com.gogul.tickets.repository.MoviesRepository;
import com.gogul.tickets.repository.ScheduleRepository;
import com.gogul.tickets.repository.ScreenRepository;
import com.gogul.tickets.repository.SeatLayoutRepository;
import com.gogul.tickets.repository.TheatreRepository;
import com.gogul.tickets.repository.UserRepository;

@Component
public class DemoData implements CommandLineRunner{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private TheatreRepository theatreRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private ScreenRepository screenRepository;
	@Autowired
	private SeatLayoutRepository seatLayoutRepository;
	@Autowired
	private MoviesRepository movieRepository;
	@Autowired
	private ScheduleRepository scheduleRepository;
	@Autowired
	private ApplicationUsersRepository applicationUsersRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		createUser(10);
		createAdmin(2);
		createClient(5);
		createMovies(5);
		createMoviesSchedule(5);
	}
	
	private void createApplicationUsers(int i, long id, String role) {
		ApplicationUsers applicationUser = new ApplicationUsers();
		applicationUser.setId(id);
		applicationUser.setRole(role.toUpperCase());
		applicationUser.setUsername(role+i);
		applicationUser.setPassword(passwordEncoder.encode(role+i));
		applicationUsersRepository.save(applicationUser);
	}
	
	private void createMoviesSchedule(int count) {
		for(int i=1;i<=count;i++) {
			long j = 30000+i;
			try {
				MoviesSchedule schedule = new MoviesSchedule();
				schedule.setMovieScheduleId(j);
				schedule.setMovieId(20000+i);
				schedule.setStartTime(LocalTime.of(19, 15));
				schedule.setScreenId(4000+i);
				schedule.setTheatreId(3000+i);
				schedule.setTotalSeats(100);
				schedule.setAvailableSeats(100);
				scheduleRepository.save(schedule);
			}catch(Exception e) {
				System.out.println("Failed to create MOVIE SCHEDULE "+ j );
			}
		}
	}

	private void createMovies(int count) {
		for(int i=1;i<=count;i++) {
			long j= 20000+i;
			try {
				Movies movies = new Movies();
				movies.setMovieId(j);
				movies.setName("MOVIE"+i);
				movies.setDescription("TEST DATA");
				movies.setLanguage("ENG");
				movies.setDurationInSeconds(120*60);
				movies.setStartDate(LocalDate.now());
				movies.setCutOffDate(LocalDate.now().plusDays(10));
				movies.setBookingEnabled(true);
				movieRepository.save(movies);
			}catch(Exception e) {
				System.out.println("Failed to create MOVIE "+ j );
			}
		}
	}

	private void createAdmin(int count) {
		for(int i=1;i<=count;i++) {
			long j = 1000 + i;
			try {
				SystemAdmin admin = new SystemAdmin();
				admin.setId(j);
				admin.setUsername("ADMIN"+i);
				createApplicationUsers(i,j,"ADMIN");
				adminRepository.save(admin);
			}catch(Exception e) {
				System.out.println("Failed to create ADMIN "+ j );
			}
		}
	}

	private Theatres createTheatre(int i, long clientId, long theatreId) {
		Theatres theatres = new Theatres();
		theatres.setTheatreId(theatreId);
		theatres.setClientId(clientId);
		theatres.setName("CINEMA"+i);
		theatres.setDescription("CINEMA"+i);
		theatres.setBookingEnabled(true);
		theatres.setOpened(true);
		theatres.setScreens(Arrays.asList(createScreen(i,theatreId,4000+i)));
		theatreRepository.save(theatres);
		return theatres;
	}

	private Screens createScreen(int i,long theatreId, int screenId) {
		Screens screens = new Screens();
		screens.setScreenId(screenId);
		screens.setName("SCREEN"+i);
		screens.setTheatreId(theatreId);
		screens.setBookingEnabled(true);
		screens.setTotalCapacity(100);
		screens.setSeatLayouts(Arrays.asList(createLayout(i,screenId,5000+i)));
		screenRepository.save(screens);
		return screens;
	}

	private SeatLayout createLayout(int i, int screenId, int layoutId) {
		SeatLayout seatLayout = new SeatLayout();
		seatLayout.setLayoutId(layoutId);
		seatLayout.setScreenId(screenId);
		seatLayout.setName("PREMIUM");
		seatLayout.setBookingEnabled(true);
		seatLayout.setTotalSeats(100);
		seatLayout.setRowPattern("{A-E,F,G-J}");
		seatLayout.setColumnPattern("{10,6,11}");
		seatLayoutRepository.save(seatLayout);
		return seatLayout;
	}

	private void createClient(int count) {
		for(int i=1;i<=count;i++) {
			long j = 2000 + i; 
			try {
				Client client = new Client();
				client.setClientId(j);
				client.setCreatedBy("ADMIN"+((i%2)+1));
				client.setName("CINEMA"+i);
				client.setUsername("CINEMA"+i);
				client.setTheatres(Arrays.asList(createTheatre(i, j, 3000+i)));
				createApplicationUsers(i,j,"CLIENT");
				clientRepository.save(client);
			}catch(Exception e) {
				System.out.println("Failed to create CLIENT "+ j );
			}
		}
	}
	
	
	private void createUser(int count) {
		for(int i=1;i<=count;i++) {
			long j = 10000 + i;
			try {
				User user = new User();
				user.setUserId(j);
				user.setUsername("USER"+i);
				user.setEmailAddress("abc"+i+"@xyz.com");
				createApplicationUsers(i,j,"USERS");
				userRepository.save(user);
			}catch(Exception e) {
				System.out.println("Failed to create USER "+ j );
			}
		}
	}
	
}
