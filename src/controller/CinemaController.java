package controller;

import service.*;
import repository.*;
import entity.*;

import java.util.Scanner;

public class CinemaController {

    private Scanner scanner = new Scanner(System.in);
    private BookingService bookingService = new BookingService();
    private SessionRepository sessionRepo = new SessionRepository();
    private MovieRepository movieRepo = new MovieRepository();
    private HallRepository hallRepo = new HallRepository();

    private boolean adminMode = false;

    public void start() {
        while (true) {
            if (adminMode) {
                showAdminMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private void showUserMenu() {
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

    private void showAdminMenu() {
        System.out.println("=== Admin Mode ===");
        System.out.println("1. Add Movie\n2. Add Hall\n3. Add Session\n4. Show All Movies\n5. Show All Halls\n9. Toggle User Mode\n0. Exit");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addMovie();
            case 2 -> addHall();
            case 3 -> addSession();
            case 4 -> showAllMovies();
            case 5 -> showAllHalls();
            case 9 -> adminMode = false;
            case 0 -> { return; }
        }
    }

    private void bookSeat() {
        int sessionId = readInt("Session ID: ");
        int seat = readInt("Seat: ");
        bookingService.bookSeat(sessionId, seat);
    }

    private void cancelBooking() {
        int sessionId = readInt("Session ID: ");
        int seat = readInt("Seat: ");
        bookingService.cancelBooking(sessionId, seat);
    }

    private void showSessions() {
        for (Session s : sessionRepo.getAvailableSessions()) {
            Movie movie = movieRepo.getMovieById(s.movieId);
            Hall hall = hallRepo.getHallById(s.getHallId());

            if (movie != null && hall != null) {
                System.out.println("ID: " + s.id +
                    ", Movie: " + movie.getTitle() +
                    " (" + movie.getGenre() + ", " + movie.getDuration() + "min)" +
                    ", Hall: " + hall.getName() +
                    ", Price: $" + s.price +
                    ", Start: " + s.getStartTime());
            }
        }
    }

    private int readInt(String msg) {
        System.out.print(msg);
        return scanner.nextInt();
    }

    private void addMovie() {
        System.out.print("Movie title: ");
        String title = scanner.nextLine();
        System.out.print("Duration (minutes): ");
        int duration = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Genre: ");
        String genre = scanner.nextLine();

        movieRepo.addMovie(title, duration, genre);
    }

    private void addHall() {
        System.out.print("Hall name: ");
        String name = scanner.nextLine();
        System.out.print("Total seats: ");
        int seats = scanner.nextInt();
        scanner.nextLine();

        hallRepo.addHall(name, seats);
    }

    private void addSession() {
        System.out.println("Available movies:");
        showAllMovies();

        System.out.print("Movie ID: ");
        int movieId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Available halls:");
        showAllHalls();

        System.out.print("Hall ID: ");
        int hallId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Price: $");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Start time (any text): ");
        String startTime = scanner.nextLine();

        sessionRepo.addSession(movieId, price, startTime, hallId);
    }

    private void showAllMovies() {
        for (Movie movie : movieRepo.getAllMovies()) {
            System.out.println("ID: " + movie.getId() + ", Title: " + movie.getTitle() +
                ", Genre: " + movie.getGenre() + ", Duration: " + movie.getDuration() + "min");
        }
    }

    private void showAllHalls() {
        for (Hall hall : hallRepo.getAllHalls()) {
            System.out.println("ID: " + hall.getId() + ", Name: " + hall.getName() +
                ", Seats: " + hall.getTotalSeats());
        }
    }
}
