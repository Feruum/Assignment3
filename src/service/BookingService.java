package service;

import entity.Session;
import repository.BookingRepository;
import repository.SessionRepository;

import java.time.LocalDateTime;

public class BookingService {

    private BookingRepository bookingRepo = new BookingRepository();
    private SessionRepository sessionRepo = new SessionRepository();

    public void bookSeat(int sessionId, int seat, UserService userService) {

        if (!userService.isLoggedIn()) {
            System.out.println("Please login first");
            return;
        }

        Session session = sessionRepo.getSessionById(sessionId);
        if (session == null) {
            System.out.println("Session not found");
            return;
        }

        if (session.getStartTime().isBefore(LocalDateTime.now())) {
            System.out.println("Session already started");
            return;
        }

        if (!bookingRepo.isSeatFree(sessionId, seat)) {
            System.out.println("Seat is already taken");
            return;
        }

        double basePrice = session.getPrice();
        double discount = userService.calculateDiscount(basePrice);
        double finalPrice = basePrice - discount;

        String username = userService.getCurrentUser().getUsername();
        bookingRepo.book(sessionId, seat, username);

        userService.incrementBookingCount();

        System.out.println("Booking successful");
        System.out.println("Price: $" + basePrice);
        if (discount > 0) {
            System.out.println("Discount: $" + discount);
        }
        System.out.println("Final price: $" + finalPrice);
    }

    public void showAvailableSeats(int sessionId) {

        Session session = sessionRepo.getSessionById(sessionId);
        if (session == null) {
            System.out.println("Session not found");
            return;
        }

        System.out.println("Available seats:");

        for (int seat = 1; seat <= 30; seat++) {
            if (bookingRepo.isSeatFree(sessionId, seat)) {
                System.out.print(seat + " ");
            }
        }
        System.out.println();
    }
    public void cancelBooking(int sessionId, int seatNumber, UserService userService) {

        if (!userService.isLoggedIn()) {
            System.out.println("Please login first");
            return;
        }

        String customerName = userService.getCurrentUser().getUsername();

        boolean cancelled = bookingRepo.cancelBooking(sessionId, seatNumber, customerName);

        if (cancelled) {
            System.out.println("Booking cancelled successfully");
        } else {
            System.out.println("Booking not found or not yours");
        }
    }

}
