package controller;

import entity.Booking;
import entity.Session;
import entity.Movie;
import entity.Hall;
import service.BookingService;
import service.UserService;
import repository.BookingRepository;
import repository.HallRepository;
import repository.MovieRepository;
import repository.SessionRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Main app controller
 * Manages user interface and services
 */
public class CinemaController {

    // Scanner for user input
    private Scanner scanner = new Scanner(System.in);

    // Services for business logic
    private BookingService bookingService = new BookingService();
    private UserService userService = new UserService();

    // Repositories for data work
    private MovieRepository movieRepository = new MovieRepository();
    private SessionRepository sessionRepository = new SessionRepository();
    private HallRepository hallRepository = new HallRepository();

    /**
     * Main method to start app
     * Has main program loop
     */
    public void start() {
        System.out.println("=== CINEMA BOOKING SYSTEM ===");
        System.out.println("Welcome to the Cinema Management System!");

        // Main program loop
        while (true) {
            // Show menu based on login status
            if (!userService.isLoggedIn()) {
                showAuthenticationMenu();
            } else {
                showMainMenu();
            }

            // Get user choice
            int userChoice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            // Handle choice based on login status
            if (!userService.isLoggedIn()) {
                handleAuthenticationChoice(userChoice);
            } else {
                handleMainMenuChoice(userChoice);
            }

            // Exit program
            if (userChoice == 0) {
                System.out.println("Thank you for using our system!");
                break;
            }
        }
    }

    /**
     * Show login menu for not logged users
     */
    private void showAuthenticationMenu() {
        System.out.println("\n=== AUTHENTICATION MENU ===");
        System.out.println("1. Register as Customer");
        System.out.println("2. Register as Admin");
        System.out.println("3. Login");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Show main menu for logged users
     * Menu depends on user role (admin or customer)
     */
    private void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("Logged in as: " + userService.getCurrentUser().getUsername() +
                " (Role: " + userService.getCurrentUser().getRole() + ")");

        // Different menus for different roles
        if (userService.isAdmin()) {
            // Admin menu
            System.out.println("1. Add Movie");
            System.out.println("2. Add Hall");
            System.out.println("3. View All Halls");
            System.out.println("4. Add Session");
            System.out.println("5. View All Sessions");
        } else {
            // Customer menu
            System.out.println("1. View Available Sessions");
            System.out.println("2. View Available Seats for Session");
            System.out.println("3. Book a Seat");
            System.out.println("4. My Bookings");
        }
        System.out.println("9. Logout");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }

    /**
     * Handle login menu choice
     * @param choice selected option
     */
    private void handleAuthenticationChoice(int choice) {
        switch (choice) {
            case 1:
                registerNewUser("customer");
                break;
            case 2:
                registerNewUser("admin");
                break;
            case 3:
                loginExistingUser();
                break;
            case 0:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    /**
     * Handle main menu choice
     * @param choice selected option
     */
    private void handleMainMenuChoice(int choice) {
        if (userService.isAdmin()) {
            handleAdminMenuChoice(choice);
        } else {
            handleCustomerMenuChoice(choice);
        }
    }

    /**
     * Handle admin choice
     * @param choice selected option
     */
    private void handleAdminMenuChoice(int choice) {
        switch (choice) {
            case 1:
                addNewMovie();
                break;
            case 2:
                addNewHall();
                break;
            case 3:
                displayAllHalls();
                break;
            case 4:
                addNewSession();
                break;
            case 5:
                displayAllSessions();
                break;
            case 9:
                userService.logout();
                break;
            case 0:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    /**
     * Handle customer choice
     * @param choice selected option
     */
    private void handleCustomerMenuChoice(int choice) {
        switch (choice) {
            case 1:
                displayAvailableSessions();
                break;
            case 2:
                showAvailableSeatsForSession();
                break;
            case 3:
                bookSeatForUser();
                break;
            case 4:
                displayUserBookings();
                break;
            case 9:
                userService.logout();
                break;
            case 0:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    /**
     * Register new user
     * @param role user role (customer or admin)
     */
    private void registerNewUser(String role) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        userService.register(username, password, role);
    }

    /**
     * Login existing user
     */
    private void loginExistingUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        userService.login(username, password);
    }

    /**
     * Add new movie (admin only)
     */
    private void addNewMovie() {
        System.out.print("Enter movie title: ");
        String title = scanner.nextLine();
        System.out.print("Enter movie duration (in minutes): ");
        int duration = scanner.nextInt();
        scanner.nextLine(); // clear buffer
        System.out.print("Enter movie genre: ");
        String genre = scanner.nextLine();

        movieRepository.addMovie(title, duration, genre);
    }

    /**
     * Add new session (admin only)
     */
    private void addNewSession() {
        // Show available movies
        System.out.println("\n=== AVAILABLE MOVIES ===");
        List<Movie> movies = movieRepository.getAllMovies();
        if (movies.isEmpty()) {
            System.out.println("No movies available. Please add movies first.");
            return;
        }
        for (Movie movie : movies) {
            System.out.println("ID: " + movie.getId() + ", Title: " + movie.getTitle() +
                    ", Duration: " + movie.getDuration() + " min, Genre: " + movie.getGenre());
        }

        System.out.print("Enter Movie ID: ");
        int movieId = scanner.nextInt();

        // Show available halls
        System.out.println("\n=== AVAILABLE HALLS ===");
        List<Hall> halls = hallRepository.getAllHalls();
        if (halls.isEmpty()) {
            System.out.println("No halls available. Please add halls first.");
            return;
        }
        for (Hall hall : halls) {
            System.out.println("ID: " + hall.getId() + ", Name: " + hall.getName() +
                    ", Seats: " + hall.getTotalSeats());
        }

        System.out.print("Enter Ticket Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // clear buffer

        System.out.print("Enter session start time (yyyy-MM-dd HH:mm): ");
        String timeString = scanner.nextLine();
        LocalDateTime startTime = LocalDateTime.parse(timeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        System.out.print("Enter Hall ID: ");
        int hallId = scanner.nextInt();

        sessionRepository.addSession(movieId, price, startTime, hallId);
    }

    /**
     * Show all sessions (for admin and customer)
     */
    private void displayAvailableSessions() {
        List<Session> sessions = sessionRepository.getAvailableSessions();
        System.out.println("\n=== AVAILABLE SESSIONS ===");
        if (sessions.isEmpty()) {
            System.out.println("No sessions available.");
        } else {
            for (Session session : sessions) {
                System.out.println("ID: " + session.id +
                        ", Movie ID: " + session.movieId +
                        ", Price: $" + session.price +
                        ", Start: " + session.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                        ", Hall: " + session.getHallId());
            }
        }
    }

    private void displayAllSessions() {
        displayAvailableSessions();
    }

    /**
     * Book seat for current user
     */
    private void bookSeatForUser() {
        System.out.print("Enter Session ID: ");
        int sessionId = scanner.nextInt();
        System.out.print("Enter Seat Number: ");
        int seatNumber = scanner.nextInt();

        bookingService.bookSeat(sessionId, seatNumber, userService);
    }

    /**
     * Shows bookings of the current user
     */
    private void displayUserBookings() {
        System.out.println("Loading your bookings...");
        String currentUsername = userService.getCurrentUser().getUsername();

        BookingRepository bookingRepo = new BookingRepository();
        List<Booking> userBookings = bookingRepo.getBookingsByCustomer(currentUsername);

        if (userBookings.isEmpty()) {
            System.out.println("You have no active bookings.");
        } else {
            System.out.println("Your Bookings:");
            for (Booking booking : userBookings) {
                System.out.println("Session ID: " + booking.sessionId +
                        ", Seat: " + booking.seatNumber);
            }
        }
    }

    /**
     * Show available seats for a session (customer)
     */
    private void showAvailableSeatsForSession() {
        System.out.print("Enter Session ID to view available seats: ");
        int sessionId = scanner.nextInt();

        bookingService.showAvailableSeats(sessionId);
    }

    /**
     * Add new hall (admin only)
     */
    private void addNewHall() {
        System.out.print("Enter hall name: ");
        String name = scanner.nextLine();
        System.out.print("Enter total number of seats: ");
        int totalSeats = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        hallRepository.addHall(name, totalSeats);
    }

    /**
     * Display all halls (admin only)
     */
    private void displayAllHalls() {
        List<Hall> halls = hallRepository.getAllHalls();
        System.out.println("\n=== HALLS ===");
        if (halls.isEmpty()) {
            System.out.println("No halls available.");
        } else {
            for (Hall hall : halls) {
                System.out.println("ID: " + hall.getId() +
                        ", Name: " + hall.getName() +
                        ", Total Seats: " + hall.getTotalSeats());
            }
        }
    }
}