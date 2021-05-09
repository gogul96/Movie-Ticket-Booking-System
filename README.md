# Movie Ticket Booking System

Movie Ticket Booking System is a simple Backend Rest API application server focused on the providing the API's for the clients with a Java 8 Spring Boot Rest server.

The application offers a three different role management,
* Admin: has permissions to check and handle the Cinemas and the option to add movies to data.
* Cinema/Clients: has permissions on read/write, its Theatres and screen data with ability to add schedule for movies. 
* User: has permissions to movies and its schedules data and able to book the seats to Cinemas.

A User can create signup to the service and login and check the availability and proceed to booking of seats.

The application has the following features:
* CRUD operations on whole data.
* Server side is implemented with Spring boot with spring boot security
* sign-in to permit the registration of new users.
* authentication based on oauth2 JWT
* consumes REST resources `/api/admin`, `/api/client` , `/api/users` and `/api/movies` where movie listing and schedule listing is open to everyone.

Available users are:
|Username|Password|Role|
|----------|--------|----|
|ADMIN1|ADMIN1|Admin|
|CLIENT1|CLIENT1|Client|
|USER1|USER1|User|
|USER1|USER1|User|
|...|

List of Open API's: **(append `/api` to all Path)**
|API Method |API Path| Accessible Roles|
|----------|--------|----|
|POST |/login| ALL|
|POST |/signup/users| ALL|
|GET |/movies| ALL|
|GET |/movies/schedule| ALL|