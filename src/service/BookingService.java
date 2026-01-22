package service;

import entity.Session;
import repository.BookingRepository;
import repository.SessionRepository;
import repository.HallRepository;
import entity.Hall;

public class BookingService {

    private BookingRepository bookingRepo = new BookingRepository();
    private SessionRepository sessionRepo = new SessionRepository();
    private HallRepository hallRepo = new HallRepository();

    public void bookSeat(int sessionId, int seat) {
        Session session = sessionRepo.getSessionById(sessionId);
        if (session == null) {
            System.out.println("Session not found");
            return;
        }

        // Упрощаем - убираем проверку времени
        if (!bookingRepo.isSeatFree(sessionId, seat)) {
            System.out.println("Seat is already taken");
            return;
        }

        bookingRepo.book(sessionId, seat, "Anonymous");
        System.out.println("Booking successful! Price: $" + session.getPrice());
    }

    public void showAvailableSeats(int sessionId) {

        Session session = sessionRepo.getSessionById(sessionId);
        if (session == null) {
            System.out.println("Session not found");
            return;
        }

        Hall hall = hallRepo.getHallById(session.getHallId());
        if (hall == null) {
            System.out.println("Hall not found");
            return;
        }

        int totalSeats = hall.getTotalSeats();

        System.out.println("Available seats:");

        for (int seat = 1; seat <= totalSeats; seat++) {
            if (bookingRepo.isSeatFree(sessionId, seat)) {
                System.out.print(seat + " ");
            }
        }
        System.out.println();
    }

    public void cancelBooking(int sessionId, int seatNumber) {
        boolean cancelled = bookingRepo.cancelBooking(sessionId, seatNumber, "Anonymous");

        if (cancelled) {
            System.out.println("Booking cancelled successfully");
        } else {
            System.out.println("Booking not found");
        }
    }

}
