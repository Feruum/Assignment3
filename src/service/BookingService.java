package service;

import entity.Session;
import repository.BookingRepository;
import repository.SessionRepository;

public class BookingService {

    private BookingRepository bookingRepo = new BookingRepository();
    private SessionRepository sessionRepo = new SessionRepository();

    public void bookSeat(int sessionId, int seat) throws java.sql.SQLException {
        Session session = sessionRepo.getSessionById(sessionId);
        if (session == null) {
            System.out.println("Session not found");
            return;
        }

        if (!bookingRepo.isSeatFree(sessionId, seat)) {
            System.out.println("Seat is already taken");
            return;
        }

        bookingRepo.book(sessionId, seat, "Anonymous");
        System.out.println("Booking successful! Price: $" + session.getPrice());
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

    public void cancelBooking(int sessionId, int seatNumber) throws java.sql.SQLException {
        boolean cancelled = bookingRepo.cancelBooking(sessionId, seatNumber, "Anonymous");

        if (cancelled) {
            System.out.println("Booking cancelled successfully");
        } else {
            System.out.println("Booking not found");
        }
    }

}
