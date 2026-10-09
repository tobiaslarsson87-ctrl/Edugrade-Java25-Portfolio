package se.edugrade.java25.enterprise.gym.exception;

public class GymClassNotFoundException extends RuntimeException {
    public GymClassNotFoundException(Long id) {
        super("GymClass with ID " + id + " was not found.");
    }
}
