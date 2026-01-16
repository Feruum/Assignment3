package service;

import repository.BookingRepository;

public class BookingService {

    BookingRepository repo = new BookingRepository();

    public void bookSeat(int sessionId, int seat, String name) {
        if (repo.isSeatFree(sessionId, seat)) {
            repo.book(sessionId, seat, name);
        } else {
            System.out.println("Seat already taken");
        }
    }
}