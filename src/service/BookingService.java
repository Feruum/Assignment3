package service;

import entity.Session;
import repository.BookingRepository;
import repository.SessionRepository;
import repository.MovieRepository;
import entity.Booking;
import entity.Movie;

import java.util.List;


public class BookingService {

    private BookingRepository bookingRepo = new BookingRepository();
    private SessionRepository sessionRepo = new SessionRepository();
    private MovieRepository movieRepo = new MovieRepository();


    private repository.HallRepository hallRepo = new repository.HallRepository();

    public void bookSeat(int sessionId, int seat, String customerName) throws java.sql.SQLException {
        Session session = sessionRepo.getSessionById(sessionId);
        if (session == null) {
            System.out.println("Session not found");
            return;
        }
        
        entity.Hall hall = hallRepo.getHallById(session.getHallId());
        int maxSeats = (hall != null) ? hall.getTotalSeats() : 10; // Fallback

        if (seat < 1 || seat > maxSeats) {
             System.out.println("Invalid seat number! This hall only has " + maxSeats + " seats.");
             return;
        }

        if (!bookingRepo.isSeatFree(sessionId, seat)) {
            System.out.println("Seat is already taken");
            return;
        }
        
        int bookingCount = bookingRepo.getBookingCount(customerName);
        double discount = 0.0;
        
        if (bookingCount >= 10) {
            discount = 0.15;
        } else if (bookingCount >= 5) {
            discount = 0.10;
        } else if (bookingCount >= 2) {
            discount = 0.05;
        }

        double finalPrice = session.getPrice() * (1.0 - discount);

        bookingRepo.book(sessionId, seat, customerName);
        
        // Update user booking count
        repository.UserRepository userRepo = new repository.UserRepository();
        if (userRepo.userExists(customerName)) {
             userRepo.updateUserBookingCount(customerName, bookingCount + 1);
        }
        
        System.out.println("Booking successful!");
        System.out.println("Original Price: $" + session.getPrice());
        if (discount > 0) {
            System.out.println("Discount applied: " + (int)(discount * 100) + "% (based on " + bookingCount + " previous bookings)");
        }
        System.out.println("Final Price: $" + String.format("%.2f", finalPrice));
    }

    public void showAvailableSeats(int sessionId) throws java.sql.SQLException {
        Session session = sessionRepo.getSessionById(sessionId);
        if (session == null) {
             System.out.println("Session not found.");
             return;
        }
        
        entity.Hall hall = hallRepo.getHallById(session.getHallId());
        int maxSeats = (hall != null) ? hall.getTotalSeats() : 10;
        
        System.out.println("Available seats for session " + sessionId + " (Hall: " + (hall!=null ? hall.getName() : "Unknown") + "):");

        // Dynamic seat loop
        for (int seat = 1; seat <= maxSeats; seat++) {
            if (bookingRepo.isSeatFree(sessionId, seat)) {
                System.out.print(seat + " ");
            }
        }
        System.out.println();
    }

    public void cancelBooking(int sessionId, int seatNumber, String customerName) throws java.sql.SQLException {
        boolean cancelled = bookingRepo.cancelBooking(sessionId, seatNumber, customerName);

        if (cancelled) {
            System.out.println("Booking cancelled successfully");
        } else {
            System.out.println("Booking not found or it belongs to another user");
        }
    }

    public void showUserBookings(String customerName) throws java.sql.SQLException {
        List<String> bookings = bookingRepo.getFullBookingDetails(customerName);
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found for: " + customerName);
            return;
        }
        
        // Example of Lambda: Sorting strings by length (just for demo) or alphabetically
        bookings.sort((s1, s2) -> s1.compareTo(s2));

        System.out.println("\n=== Bookings for " + customerName + " ===");
        bookings.forEach(System.out::println);
    }

    public void printRevenueReport() throws java.sql.SQLException {
        double revenue = bookingRepo.getTotalRevenue();
        System.out.println("\n=== Revenue Report ===");
        System.out.println("Total Gross Revenue: $" + String.format("%.2f", revenue));
    }
    public void showTopMovie() throws java.sql.SQLException {
        int movieId = bookingRepo.getMostBookedMovieId();
        if (movieId == -1) {
            System.out.println("No bookings yet.");
            return;
        }
        
        Movie movie = movieRepo.getMovieById(movieId);
        if (movie != null) {
            System.out.println("Most Popular Movie: " + movie.getTitle());
        }
    }
}

