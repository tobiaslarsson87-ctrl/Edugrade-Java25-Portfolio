package se.edugrade.java25.enterprise.gym.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(Long id) {
        super("Booking with ID " + id + " was not found.");
    }
}
