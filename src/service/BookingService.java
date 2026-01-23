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


    public void bookSeat(int sessionId, int seat, String customerName) throws java.sql.SQLException {
        Session session = sessionRepo.getSessionById(sessionId);
        if (session == null) {
            System.out.println("Session not found");
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
        
        System.out.println("Booking successful!");
        System.out.println("Original Price: $" + session.getPrice());
        if (discount > 0) {
            System.out.println("Discount applied: " + (int)(discount * 100) + "% (based on " + bookingCount + " previous bookings)");
        }
        System.out.println("Final Price: $" + String.format("%.2f", finalPrice));
    }

    public void showAvailableSeats(int sessionId) throws java.sql.SQLException {
        System.out.println("Available seats for session " + sessionId + ":");

        // Упрощаем - показываем места 1-10 для всех сессий
        for (int seat = 1; seat <= 10; seat++) {
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
        List<Booking> bookings = bookingRepo.getBookingsByCustomer(customerName);
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found for: " + customerName);
            return;
        }
        
        System.out.println("\n=== Bookings for " + customerName + " ===");
        for (Booking b : bookings) {
            Session s = sessionRepo.getSessionById(b.sessionId);
            if (s != null) {
                Movie m = movieRepo.getMovieById(s.movieId);
                String movieTitle = (m != null) ? m.getTitle() : "Unknown Movie";
                System.out.println( "Movie: " + movieTitle + 
                                  " | Time: " + s.getStartTime() + 
                                  " | Seat: " + b.seatNumber);
            }
        }
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

