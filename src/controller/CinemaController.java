package controller;

import entity.Booking;
import entity.Session;
import service.BookingService;
import service.UserService;
import repository.BookingRepository;
import repository.HallRepository;
import repository.MovieRepository;
import repository.SessionRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Main app controller
 * Manages user interface and services
 */
public class CinemaController {

    // Scanner for user input
    private Scanner scanner = new Scanner(System.in);

    // Services for business logic
    private BookingService bookingService = new BookingService();
    private UserService userService = new UserService();

    // Repositories for data work
    private MovieRepository movieRepository = new MovieRepository();
    private SessionRepository sessionRepository = new SessionRepository();
    private HallRepository hallRepository = new HallRepository();

    /**
     * Main method to start app
     * Has main program loop
     */
    public void start() {
        System.out.println("=== СИСТЕМА БРОНИРОВАНИЯ КИНОБИЛЕТОВ ===");
        System.out.println("Добро пожаловать в систему управления кинотеатром!");

        // Main program loop
        while (true) {
            // Show menu based on login status
            if (!userService.isLoggedIn()) {
                showAuthenticationMenu();
            } else {
                showMainMenu();
            }

            // Get user choice
            int userChoice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            // Handle choice based on login status
            if (!userService.isLoggedIn()) {
                handleAuthenticationChoice(userChoice);
            } else {
                handleMainMenuChoice(userChoice);
            }

            // Exit program
            if (userChoice == 0) {
                System.out.println("Thank you for using our system!");
                break;
            }
        }
    }

    /**
     * Show login menu for not logged users
     */
    private void showAuthenticationMenu() {
        System.out.println("\n=== МЕНЮ АУТЕНТИФИКАЦИИ ===");
        System.out.println("1. Зарегистрироваться как клиент");
        System.out.println("2. Зарегистрироваться как администратор");
        System.out.println("3. Войти в систему");
        System.out.println("0. Выход");
        System.out.print("Выберите опцию: ");
    }

    /**
     * Show main menu for logged users
     * Menu depends on user role (admin or customer)
     */
    private void showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("You logged as: " + userService.getCurrentUser().getUsername() +
                          " (Role: " + userService.getCurrentUser().getRole() + ")");

        // Different menus for different roles
        if (userService.isAdmin()) {
            // Admin menu
            System.out.println("1. Add movie");
            System.out.println("2. Add hall");
            System.out.println("3. View all halls");
            System.out.println("4. Add session");
            System.out.println("5. View all sessions");
        } else {
            // Customer menu
            System.out.println("1. View available sessions");
            System.out.println("2. View available seats for session");
            System.out.println("3. Book seat");
            System.out.println("4. My bookings");
        }
        System.out.println("9. Logout");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }

    /**
     * Handle login menu choice
     * @param choice selected option
     */
    private void handleAuthenticationChoice(int choice) {
        switch (choice) {
            case 1:
                registerNewUser("customer");
                break;
            case 2:
                registerNewUser("admin");
                break;
            case 3:
                loginExistingUser();
                break;
            case 0:
                System.out.println("До свидания!");
                break;
            default:
                System.out.println("Неверная опция. Попробуйте еще раз.");
        }
    }

    /**
     * Handle main menu choice
     * @param choice selected option
     */
    private void handleMainMenuChoice(int choice) {
        if (userService.isAdmin()) {
            handleAdminMenuChoice(choice);
        } else {
            handleCustomerMenuChoice(choice);
        }
    }

    /**
     * Handle admin choice
     * @param choice selected option
     */
    private void handleAdminMenuChoice(int choice) {
        switch (choice) {
            case 1:
                addNewMovie();
                break;
            case 2:
                addNewHall();
                break;
            case 3:
                displayAllHalls();
                break;
            case 4:
                addNewSession();
                break;
            case 5:
                displayAllSessions();
                break;
            case 9:
                userService.logout();
                break;
            case 0:
                System.out.println("До свидания!");
                break;
            default:
                System.out.println("Неверная опция. Попробуйте еще раз.");
        }
    }

    /**
     * Handle customer choice
     * @param choice selected option
     */
    private void handleCustomerMenuChoice(int choice) {
        switch (choice) {
            case 1:
                displayAvailableSessions();
                break;
            case 2:
                showAvailableSeatsForSession();
                break;
            case 3:
                bookSeatForUser();
                break;
            case 4:
                displayUserBookings();
                break;
            case 9:
                userService.logout();
                break;
            case 0:
                System.out.println("До свидания!");
                break;
            default:
                System.out.println("Неверная опция. Попробуйте еще раз.");
        }
    }

    /**
     * Register new user
     * @param role user role (customer or admin)
     */
    private void registerNewUser(String role) {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        userService.register(username, password, role);
    }

    /**
     * Login existing user
     */
    private void loginExistingUser() {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        userService.login(username, password);
    }

    /**
     * Add new movie (admin only)
     */
    private void addNewMovie() {
        System.out.print("Введите название фильма: ");
        String title = scanner.nextLine();
        System.out.print("Введите продолжительность фильма (в минутах): ");
        int duration = scanner.nextInt();
        scanner.nextLine(); // clear buffer
        System.out.print("Введите жанр фильма: ");
        String genre = scanner.nextLine();

        movieRepository.addMovie(title, duration, genre);
    }

    /**
     * Add new session (admin only)
     */
    private void addNewSession() {
        // Show available movies
        System.out.println("\n=== ДОСТУПНЫЕ ФИЛЬМЫ ===");
        List<entity.Movie> movies = movieRepository.getAllMovies();
        if (movies.isEmpty()) {
            System.out.println("Нет доступных фильмов. Сначала добавьте фильмы.");
            return;
        }
        for (entity.Movie movie : movies) {
            System.out.println("ID: " + movie.getId() + ", Название: " + movie.getTitle() +
                             ", Длительность: " + movie.getDuration() + " мин, Жанр: " + movie.getGenre());
        }

        System.out.print("Введите ID фильма: ");
        int movieId = scanner.nextInt();

        // Show available halls
        System.out.println("\n=== ДОСТУПНЫЕ ЗАЛЫ ===");
        List<entity.Hall> halls = hallRepository.getAllHalls();
        if (halls.isEmpty()) {
            System.out.println("Нет доступных залов. Сначала добавьте залы.");
            return;
        }
        for (entity.Hall hall : halls) {
            System.out.println("ID: " + hall.getId() + ", Название: " + hall.getName() +
                             ", Мест: " + hall.getTotalSeats());
        }

        System.out.print("Введите цену билета: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // clear buffer

        System.out.print("Введите время начала сеанса (гггг-мм-дд чч:мм): ");
        String timeString = scanner.nextLine();
        LocalDateTime startTime = LocalDateTime.parse(timeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        System.out.print("Введите ID зала: ");
        int hallId = scanner.nextInt();

        sessionRepository.addSession(movieId, price, startTime, hallId);
    }

    /**
     * Show all sessions (for admin)
     */
    private void displayAllSessions() {
        List<Session> sessions = sessionRepository.getAvailableSessions();
        System.out.println("\n=== ДОСТУПНЫЕ СЕАНСЫ ===");
        if (sessions.isEmpty()) {
            System.out.println("Нет доступных сеансов.");
        } else {
            for (Session session : sessions) {
                System.out.println("ID: " + session.id +
                                 ", Фильм ID: " + session.movieId +
                                 ", Цена: $" + session.price +
                                 ", Начало: " + session.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                                 ", Зал: " + session.getHallId());
            }
        }
    }

    /**
     * Show available sessions for customers
     */
    private void displayAvailableSessions() {
        List<Session> sessions = sessionRepository.getAvailableSessions();
        System.out.println("\n=== ДОСТУПНЫЕ СЕАНСЫ ===");
        if (sessions.isEmpty()) {
            System.out.println("Нет доступных сеансов.");
        } else {
            for (Session session : sessions) {
                System.out.println("ID: " + session.id +
                                 ", Фильм ID: " + session.movieId +
                                 ", Цена: $" + session.price +
                                 ", Начало: " + session.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                                 ", Зал: " + session.getHallId());
            }
        }
    }

    /**
     * Book seat for current user
     */
    private void bookSeatForUser() {
        System.out.print("Введите ID сеанса: ");
        int sessionId = scanner.nextInt();
        System.out.print("Введите номер места: ");
        int seatNumber = scanner.nextInt();

        bookingService.bookSeat(sessionId, seatNumber, userService);
    }

    /**
     * Показывает бронирования текущего пользователя
     */
    private void displayUserBookings() {
        System.out.println("Загрузка ваших бронирований...");
        // Get current user name
        String currentUsername = userService.getCurrentUser().getUsername();

        // Get user bookings list
        BookingRepository bookingRepo = new BookingRepository();
        List<Booking> userBookings = bookingRepo.getBookingsByCustomer(currentUsername);

        if (userBookings.isEmpty()) {
            System.out.println("У вас нет активных бронирований.");
        } else {
            System.out.println("Ваши бронирования:");
            for (Booking booking : userBookings) {
                System.out.println("Сеанс ID: " + booking.sessionId +
                                 ", Место: " + booking.seatNumber);
            }
        }
    }

    /**
     * Show available seats for a session (customer)
     */
    private void showAvailableSeatsForSession() {
        System.out.print("Введите ID сеанса для просмотра доступных мест: ");
        int sessionId = scanner.nextInt();

        bookingService.showAvailableSeats(sessionId);
    }

    /**
     * Add new hall (admin only)
     */
    private void addNewHall() {
        System.out.print("Введите название зала: ");
        String name = scanner.nextLine();
        System.out.print("Введите количество мест в зале: ");
        int totalSeats = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        hallRepository.addHall(name, totalSeats);
    }

    /**
     * Display all halls (admin only)
     */
    private void displayAllHalls() {
        List<entity.Hall> halls = hallRepository.getAllHalls();
        System.out.println("\n=== ЗАЛЫ ===");
        if (halls.isEmpty()) {
            System.out.println("Нет доступных залов.");
        } else {
            for (entity.Hall hall : halls) {
                System.out.println("ID: " + hall.getId() +
                                 ", Название: " + hall.getName() +
                                 ", Мест: " + hall.getTotalSeats());
            }
        }
    }
}