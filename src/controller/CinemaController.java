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

    public void start() throws java.sql.SQLException {
        while (true) {
            showMenu();
        }
    }

    private void showMenu() throws java.sql.SQLException {
        System.out.println("\n=== Cinema System ===");
        System.out.println("""
                1. Show Sessions
                2. Show Available Seats
                3. Book Seat
                4. Cancel Booking
                5. Add Movie
                6. Add Session
                7. Show All Movies
                8. My Bookings
                9. Admin: Revenue Report
                10. Show Top Movie
                0. Exit
                """);

        int choice = readInt("Choose option: ");

        switch (choice) {
            case 1 -> showSessions();
            case 2 -> bookingService.showAvailableSeats(readInt("Session ID: "));
            case 3 -> bookSeat();
            case 4 -> cancelBooking();
            case 5 -> addMovie();
            case 6 -> addSession();
            case 7 -> showAllMovies();
            case 8 -> showMyBookings();
            case 9 -> showRevenueReport();
            case 10 -> bookingService.showTopMovie();
            case 0 -> System.exit(0);
            default -> System.out.println("Invalid option");
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
        System.out.print("Your name: ");
        String name = scanner.nextLine();
        
        bookingService.bookSeat(sessionId, seat, name);
    }

    private void cancelBooking() throws java.sql.SQLException {
        int sessionId = readInt("Session ID: ");
        int seat = readInt("Seat number: ");
        
        System.out.print("Your name: ");
        String name = scanner.nextLine();
        
        bookingService.cancelBooking(sessionId, seat, name);
    }

    private void addMovie() throws java.sql.SQLException {
        System.out.print("Movie title: ");
        String title = scanner.nextLine();

        int duration = readInt("Duration (minutes): ");

        System.out.print("Genre: ");
        String genre = scanner.nextLine();

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

        sessionRepo.addSession(movieId, price, startTime);
        System.out.println("Session added successfully");
    }

    private void showAllMovies() throws java.sql.SQLException {
        for (Movie movie : movieRepo.getAllMovies()) {
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
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
    private void showMyBookings() throws java.sql.SQLException {
        System.out.print("Your name: ");
        String name = scanner.nextLine();
        bookingService.showUserBookings(name);
    }

    private void showRevenueReport() throws java.sql.SQLException {
        bookingService.printRevenueReport();
    }
}

