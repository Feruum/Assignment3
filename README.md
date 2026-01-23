# Cinema Booking Management System

## Project: Cinema Ticket Booking System

### Project Description (20%)

**Topic:** We develop a cinema ticket booking system with user login and loyalty program.

**Project Goal:** We create a full system to manage movie shows. Admins can add movies and shows. Customers can book tickets with personal discounts.

**Main Features:**
- 🔐 **User Login** with roles (admin/customer)
- 🎬 **Movie Catalog** (add and view movies)
- 🎭 **Movie Show Management** with time check
- 💺 **Seat Booking** with business rules and availability check
- 🎁 **Loyalty Program** for regular customers (discounts)
- 📊 **View User Bookings**
- 🛡️ **Interfaces** for flexible design

### Project Structure (20%)

**Design Pattern:** MVC (Model-View-Controller)
- **Model (Entity):** Main objects in the system
- **Repository:** Database work
- **Service:** Business logic
- **Controller:** User input handling

**Project Structure:**
```
src/
├── interfaces/                    # Interfaces for repositories
│   ├── BaseRepository.java        # Base repository interface
│   ├── UserRepositoryInterface.java      # Interface for user work
│   ├── MovieRepositoryInterface.java     # Interface for movie work
│   ├── SessionRepositoryInterface.java   # Interface for show work
│   └── BookingRepositoryInterface.java   # Interface for booking work
├── controller/
│   └── CinemaController.java      # Main app controller
├── entity/                        # Main objects
│   ├── User.java                  # System user
│   ├── Movie.java                 # Movie
│   ├── Session.java               # Movie show
│   ├── Booking.java               # Booking
│   ├── Hall.java                  # Cinema hall
│   └── Seat.java                  # Seat in hall
├── repository/                    # Data access layer (implements interfaces)
│   ├── UserRepository.java        # User work
│   ├── MovieRepository.java       # Movie work
│   ├── SessionRepository.java     # Show work
│   ├── BookingRepository.java     # Booking work
│   └── SeatRepository.java        # Seat work
├── service/                       # Business logic
│   ├── UserService.java           # User service
│   └── BookingService.java        # Booking service
└── util/
    └── DatabaseConnection.java    # Database connection
```

### Database Connection (30%)

**Database:** PostgreSQL
**Connection Settings:**
- Host: localhost:5432
- Database: CinemaAlem
- User: postgres
- Password: M_asdf_321

**📁 Database Setup:**
1. Open pgAdmin
2. Create database `CinemaAlem`
3. Run `database_setup.sql` script to create tables
4. Add test data (optional)

**Main Tables:**
```sql
-- System users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL, -- 'admin' or 'customer'
    booking_count INT DEFAULT 0
);

-- Movies
CREATE TABLE movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    duration INT NOT NULL, -- in minutes
    genre VARCHAR(50) NOT NULL
);

-- Cinema halls
CREATE TABLE halls (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    total_seats INT NOT NULL
);

-- Movie shows
CREATE TABLE sessions (
    id SERIAL PRIMARY KEY,
    movie_id INT REFERENCES movies(id),
    price DECIMAL(10,2) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    hall_id INT REFERENCES halls(id)
);

-- Bookings
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    session_id INT REFERENCES sessions(id),
    seat_number INT NOT NULL,
    customer VARCHAR(100) NOT NULL,
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Functions and Business Logic (30%)

#### User Login and Roles
- **Registration:** Data check, unique username check
- **Authentication:** Login/password check
- **Role Model:** Access control (admin/customer)

#### Admin Business Logic
- Add movies with data validation
- Create shows with time check (no past shows)
- View all available shows

#### Customer Business Logic
- **Loyalty Program:**
  - 2+ bookings: 5% discount
  - 5+ bookings: 10% discount
  - 10+ bookings: 15% discount
- **Booking Validation:**
  - User login check
  - Show exists check
  - No booking for past shows
  - Seat availability check
- Automatic loyalty points

#### Additional Business Logic
- **Time Limits:** Cannot book seats for started shows
- **Price Policy:** Base price with personal discounts
- **Data Validation:** Check input data at all levels

### Key Features

1. **Security:** Use PreparedStatement to prevent SQL injection
2. **Validation:** Data check at service level
3. **Error Handling:** Good exception handling
4. **Extensibility:** Clear separation between layers
5. **User Experience:** Easy console interface



**🎯 What the program can do:**
- Register new users (customers and admins)
- Login with access check
- Admins can add movies and create shows
- Customers can view shows and book seats
- Discount system for regular customers
- Validation of all operations

### 🎓 Features for Beginners

**Code is easy to understand:**
- ✅ Detailed comments in English
- ✅ Clear variable and method names
- ✅ Interfaces to understand contracts
- ✅ Business logic separated from data
- ✅ Error handling with clear messages

**Architecture is easy to extend:**
- 🔧 Adding new features does not change old code
- 🎯 Each class has one job
- 📚 Interfaces allow easy replacement

---

**💡 This project is a great example of modern Java development with best practices for beginners.**
