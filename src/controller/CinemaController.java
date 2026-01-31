package controller;

import service.BookingService;
import repository.SessionRepository;
import repository.MovieRepository;
import entity.Session;
import entity.Movie;

import java.util.Scanner;

public class CinemaController {

    private final Scanner scanner = new Scanner(System.in);
    private final BookingService bookingService = new BookingService();
    private final SessionRepository sessionRepo = new SessionRepository();
    private final MovieRepository movieRepo = new MovieRepository();
    private final repository.UserRepository userRepo = new repository.UserRepository();
    
    private entity.User currentUser;

    public void start() throws java.sql.SQLException {
        System.out.println("Welcome to Cinema System!");
        
        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private void showAuthMenu() throws java.sql.SQLException {
        System.out.println("\n=== AUTHENTICATION ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        
        int choice = readInt("Choose option: ");
        
        switch (choice) {
            case 1 -> login();
            case 2 -> register();
            case 0 -> System.exit(0);
            default -> System.out.println("Invalid option");
        }
    }
    
    private void login() throws java.sql.SQLException {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        entity.User user = userRepo.getUserByUsername(username);
        
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        } else {
            System.out.println("Invalid username or password.");
        }
    }
    
    private void register() throws java.sql.SQLException {
        System.out.println("\n--- Registration ---");
        System.out.print("Username (min 4 chars): ");
        String username = scanner.nextLine();
        
        System.out.print("Password (min 4 chars): ");
        String password = scanner.nextLine();
        
        System.out.print("Role (admin/customer): ");
        String role = scanner.nextLine(); // In a real app, admin registration would be restricted
        
        try {
            if (userRepo.userExists(username)) {
                System.out.println("User already exists!");
                return;
            }
            
            entity.User newUser = util.UserFactory.createUser(username, password, role);
            userRepo.addUser(newUser);
            System.out.println("Registration successful! Please login.");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void showMainMenu() throws java.sql.SQLException {
        System.out.println("\n=== Cinema System Menu (" + currentUser.getUsername() + ") ===");
        System.out.println("1. Show Sessions");
        System.out.println("2. Show Available Seats");
        System.out.println("3. Book Seat");
        System.out.println("4. Cancel Booking");
        
        if (currentUser.isAdmin()) {
            System.out.println("5. [ADMIN] Add Movie");
            System.out.println("6. [ADMIN] Add Session");
            System.out.println("9. [ADMIN] Revenue Report");
        }
        
        System.out.println("7. Show All Movies");
        System.out.println("8. My Bookings");
        System.out.println("10. Show Top Movie");
        System.out.println("11. 🔍 Search & Filter Movies");
        System.out.println("0. Logout");

        int choice = readInt("Choose option: ");

        switch (choice) {
            case 1 -> showSessions();
            case 2 -> bookingService.showAvailableSeats(readInt("Session ID: "));
            case 3 -> bookSeat();
            case 4 -> cancelBooking();
            case 5 -> {
                if (currentUser.isAdmin()) addMovie();
                else System.out.println("Access Denied: Admin only.");
            }
            case 6 -> {
                if (currentUser.isAdmin()) addSession();
                else System.out.println("Access Denied: Admin only.");
            }
            case 7 -> showAllMovies();
            case 8 -> showMyBookings();
            case 9 -> {
                 if (currentUser.isAdmin()) showRevenueReport();
                 else System.out.println("Access Denied: Admin only.");
            }
            case 10 -> bookingService.showTopMovie();
            case 11 -> showSearchMenu();
            case 0 -> {
                currentUser = null;
                System.out.println("Logged out.");
            }
            default -> System.out.println("Invalid option");
        }
    }
    
    private void showSearchMenu() throws java.sql.SQLException {
        service.MovieService movieService = new service.MovieService();
        
        System.out.println("\n=== 🔍 Search & Filter ===");
        System.out.println("1. Search by Title");
        System.out.println("2. Filter by Genre");
        System.out.println("3. Sort by Duration");
        System.out.println("0. Back");
        
        int choice = readInt("Choose: ");
        
        java.util.List<Movie> results = java.util.Collections.emptyList();
        
        switch (choice) {
            case 1 -> {
                System.out.print("Enter title: ");
                String q = scanner.nextLine();
                results = movieService.searchByTitle(q);
            }
            case 2 -> {
                System.out.print("Enter genre: ");
                String g = scanner.nextLine();
                results = movieService.filterByGenre(g);
            }
            case 3 -> {
                results = movieService.sortByDuration();
                System.out.println("(Sorted by duration ascending)");
            }
            case 0 -> { return; }
        }
        
        if (results.isEmpty()) {
            System.out.println("No movies found.");
        } else {
            System.out.println("\n--- Results ---");
            for (Movie m : results) {
                System.out.println(m.getTitle() + " | " + m.getGenre() + " | " + m.getDuration() + " min");
            }
        }
    }

    private void showSessions() throws java.sql.SQLException {
        for (Session s : sessionRepo.getAvailableSessions()) {
            Movie movie = movieRepo.getMovieById(s.movieId);

            if (movie != null) {
                System.out.println(
                        "Session ID: " + s.id +
                                ", Movie: " + movie.getTitle() +
                                " (" + movie.getGenre() + ", " + movie.getDuration() + " min)" +
                                ", Price: $" + s.price +
                                ", Start: " + s.getStartTime()
                );
            }
        }
    }

    private void bookSeat() throws java.sql.SQLException {
        showSessions();
        int sessionId = readInt("Session ID: ");

        bookingService.showAvailableSeats(sessionId);

        int seat = readInt("Seat number: ");
        // Auto-use current user name
        bookingService.bookSeat(sessionId, seat, currentUser.getUsername());
    }

    private void cancelBooking() throws java.sql.SQLException {
        int sessionId = readInt("Session ID: ");
        int seat = readInt("Seat number: ");
        
        bookingService.cancelBooking(sessionId, seat, currentUser.getUsername());
    }

    private void addMovie() throws java.sql.SQLException {
        System.out.print("Movie title: ");
        String title = scanner.nextLine();

        int duration = readInt("Duration (minutes): ");

        System.out.print("Genre: ");
        String genre = scanner.nextLine();
        
        if (!util.ValidationUtils.isValidDuration(duration)) {
             System.out.println("Invalid duration!");
             return;
        }

        movieRepo.addMovie(title, duration, genre);
        System.out.println("Movie added successfully");
    }

    private void addSession() throws java.sql.SQLException {
        System.out.println("Available movies:");
        showAllMovies();

        int movieId = readInt("Movie ID: ");

        System.out.print("Price: $");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Start time: ");
        String startTime = scanner.nextLine();
        
        int hallId = readInt("Hall ID (1=Main, 2=VIP): "); // Simple prompt
        
        if (!util.ValidationUtils.isValidPrice(price)) {
            System.out.println("Invalid price!");
            return;
        }

        sessionRepo.addSession(movieId, price, startTime, hallId);
        System.out.println("Session added successfully");
    }

    private void showAllMovies() throws java.sql.SQLException {
        var movies = movieRepo.getAllMovies();
        // Lambda Sort by Title
        movies.sort((m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()));
        
        for (Movie movie : movies) {
            System.out.println(
                    "ID: " + movie.getId() +
                            ", Title: " + movie.getTitle() +
                            ", Genre: " + movie.getGenre() +
                            ", Duration: " + movie.getDuration() + " min"
            );
        }
    }

    private int readInt(String msg) {
        System.out.print(msg);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
    
    private void showMyBookings() throws java.sql.SQLException {
        bookingService.showUserBookings(currentUser.getUsername());
    }

    private void showRevenueReport() throws java.sql.SQLException {
        bookingService.printRevenueReport();
    }
}

