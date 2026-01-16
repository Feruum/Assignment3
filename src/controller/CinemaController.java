package controller;

import service.BookingService;
import repository.MovieRepository;
import repository.SessionRepository;

import java.util.Scanner;

public class CinemaController {

    Scanner sc = new Scanner(System.in);
    BookingService bookingService = new BookingService();
    MovieRepository movieRepo = new MovieRepository();
    SessionRepository sessionRepo = new SessionRepository();

    public void start() {
        while (true) {
            System.out.println("1 Add movie");
            System.out.println("2 Add session");
            System.out.println("3 Book seat");
            System.out.println("0 Exit");

            int c = sc.nextInt();

            if (c == 1) {
                System.out.print("Movie title: ");
                movieRepo.addMovie(sc.next());
            }

            if (c == 2) {
                System.out.print("Movie id: ");
                int m = sc.nextInt();
                System.out.print("Price: ");
                sessionRepo.addSession(m, sc.nextDouble());
            }

            if (c == 3) {
                System.out.print("Session id: ");
                int s = sc.nextInt();
                System.out.print("Seat number: ");
                int seat = sc.nextInt();
                System.out.print("Name: ");
                bookingService.bookSeat(s, seat, sc.next());
            }

            if (c == 0) break;
        }
    }
}