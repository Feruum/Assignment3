package controller;

import service.*;
import repository.*;
import entity.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;

public class CinemaController {

    private Scanner scanner = new Scanner(System.in);
    private UserService userService = new UserService();
    private BookingService bookingService = new BookingService();

    private MovieRepository movieRepo = new MovieRepository();
    private HallRepository hallRepo = new HallRepository();
    private SessionRepository sessionRepo = new SessionRepository();

    public void start() {

        while (true) {

            if (!userService.isLoggedIn()) {
                System.out.println("1. Register\n2. Login\n0. Exit");
                int c = scanner.nextInt();
                scanner.nextLine();

                if (c == 1) register();
                else if (c == 2) login();
                else break;

            } else {

                if (userService.isAdmin()) showAdminMenu();
                else showCustomerMenu();
            }
        }
    }

    private void showCustomerMenu() {
        System.out.println("1. Sessions\n2. Seats\n3. Book\n4. Cancel\n9. Logout");
        int c = scanner.nextInt();
        scanner.nextLine();

        switch (c) {
            case 1 -> showSessions();
            case 2 -> bookingService.showAvailableSeats(readInt("Session ID: "));
            case 3 -> bookSeat();
            case 4 -> cancelBooking();
            case 9 -> userService.logout();
        }
    }

    private void bookSeat() {
        int s = readInt("Session ID: ");
        int seat = readInt("Seat: ");
        bookingService.bookSeat(s, seat, userService);
    }

    private void cancelBooking() {
        int s = readInt("Session ID: ");
        int seat = readInt("Seat: ");
        bookingService.cancelBooking(s, seat, userService);
    }

    private void showSessions() {
        for (Session s : sessionRepo.getAvailableSessions()) {
            System.out.println(s.id + " price " + s.price + " start " + s.getStartTime());
        }
    }

    private int readInt(String msg) {
        System.out.print(msg);
        return scanner.nextInt();
    }

    private void register() {
        System.out.print("Username: ");
        String u = scanner.nextLine();
        System.out.print("Password: ");
        String p = scanner.nextLine();
        System.out.print("Role: ");
        String r = scanner.nextLine();
        userService.register(u, p, r);
    }

    private void login() {
        System.out.print("Username: ");
        String u = scanner.nextLine();
        System.out.print("Password: ");
        String p = scanner.nextLine();
        userService.login(u, p);
    }

    private void showAdminMenu() {
        System.out.println("Admin mode (not expanded here)");
        userService.logout();
    }
}
