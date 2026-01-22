package controller;

import service.BookingService;
import repository.SessionRepository;
import repository.MovieRepository;
import entity.Session;
import entity.Movie;

import java.util.Scanner;

public class CinemaController {

    private Scanner scanner = new Scanner(System.in);
    private BookingService bookingService = new BookingService();
    private SessionRepository sessionRepo = new SessionRepository();
    private MovieRepository movieRepo = new MovieRepository();

    private boolean adminMode = false;

    public void start() throws java.sql.SQLException {
        while (true) {
            if (adminMode) {
                showAdminMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private void showUserMenu() throws java.sql.SQLException {
        System.out.println("=== User Mode ===");
        System.out.println("1. Show Sessions\n2. Show Available Seats\n3. Book Seat\n4. Cancel Booking\n9. Toggle Admin Mode\n0. Exit");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> showSessions();
            case 2 -> bookingService.showAvailableSeats(readInt("Session ID: "));
            case 3 -> bookSeat();
            case 4 -> cancelBooking();
            case 9 -> adminMode = true;
            case 0 -> { return; }
        }
    }

    private void showAdminMenu() throws java.sql.SQLException {
        System.out.println("=== Admin Mode ===");
        System.out.println("1. Add Movie\n2. Add Session\n3. Show All Movies\n9. Toggle User Mode\n0. Exit");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addMovie();
            case 2 -> addSession();
            case 3 -> showAllMovies();
            case 9 -> adminMode = false;
            case 0 -> { return; }
        }
    }

    private void bookSeat() throws java.sql.SQLException {
        int sessionId = readInt("Session ID: ");
        int seat = readInt("Seat: ");
        bookingService.bookSeat(sessionId, seat);
    }

    private void cancelBooking() throws java.sql.SQLException {
        int sessionId = readInt("Session ID: ");
        int seat = readInt("Seat: ");
        bookingService.cancelBooking(sessionId, seat);
    }

    private void showSessions() throws java.sql.SQLException {
        for (Session s : sessionRepo.getAvailableSessions()) {
            Movie movie = movieRepo.getMovieById(s.movieId);

            if (movie != null) {
                System.out.println("ID: " + s.id +
                    ", Movie: " + movie.getTitle() +
                    " (" + movie.getGenre() + ", " + movie.getDuration() + " min)" +
                    ", Price: $" + s.price +
                    ", Start: " + s.getStartTime());
            }
        }
    }

    private int readInt(String msg) {
        System.out.print(msg);
        return scanner.nextInt();
    }

    private void addMovie() throws java.sql.SQLException {
        System.out.print("Movie title: ");
        String title = scanner.nextLine();
        System.out.print("Duration (minutes): ");
        int duration = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();

        movieRepo.addMovie(title, duration, genre);
    }


    private void addSession() throws java.sql.SQLException {
        System.out.println("Available movies:");
        showAllMovies();

        System.out.print("Movie ID: ");
        int movieId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Price: $");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Start time (any text): ");
        String startTime = scanner.nextLine();

        sessionRepo.addSession(movieId, price, startTime);
    }

    private void showAllMovies() throws java.sql.SQLException {
        for (Movie movie : movieRepo.getAllMovies()) {
            System.out.println("ID: " + movie.getId() + ", Title: " + movie.getTitle() +
                ", Genre: " + movie.getGenre() + ", Duration: " + movie.getDuration() + " min");
        }
    }

}
