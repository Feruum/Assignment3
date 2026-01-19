package service;

import entity.Session;
import repository.BookingRepository;
import repository.SessionRepository;
import service.UserService;
import java.time.LocalDateTime;

public class BookingService {

    BookingRepository bookingRepo = new BookingRepository();
    SessionRepository sessionRepo = new SessionRepository();

    public void bookSeat(int sessionId, int seat, UserService userService) {
        // Check if user is logged in
        if (!userService.isLoggedIn()) {
            System.out.println("Please login to book seats");
            return;
        }

        // Check if session exists and not started
        Session session = sessionRepo.getSessionById(sessionId);
        if (session == null) {
            System.out.println("Session not found");
            return;
        }

        if (session.getStartTime().isBefore(LocalDateTime.now())) {
            System.out.println("Cannot book seats for sessions that have already started");
            return;
        }

        // Check if seat is available
        if (!bookingRepo.isSeatFree(sessionId, seat)) {
            System.out.println("Seat already taken");
            return;
        }

        // Calculate price with discount
        double basePrice = session.price;
        double discount = userService.calculateDiscount(basePrice);
        double finalPrice = basePrice - discount;

        // Book the seat
        String customerName = userService.getCurrentUser().getUsername();
        bookingRepo.book(sessionId, seat, customerName);

        // Update user booking count for loyalty
        userService.incrementBookingCount();

        System.out.println("Seat booked successfully!");
        System.out.println("Base price: $" + basePrice);
        if (discount > 0) {
            System.out.println("Discount applied: $" + discount);
        }
        System.out.println("Final price: $" + finalPrice);
        System.out.println("Loyalty points earned: 1 (Total: " + userService.getCurrentUser().getBookingCount() + ")");
    }

    // Get available seats for session
    public void showAvailableSeats(int sessionId) {
        Session session = sessionRepo.getSessionById(sessionId);
        if (session == null) {
            System.out.println("Session not found");
            return;
        }

        System.out.println("Available seats for session " + sessionId + ":");
        // Show all available seats (assuming 100 seats per hall)
        for (int seat = 1; seat <= 100; seat++) {
            if (bookingRepo.isSeatFree(sessionId, seat)) {
                System.out.print(seat + " ");
            }
        }
        System.out.println();
    }
}