package se.edugrade.exceptions;

public class UserNotFoundException extends  SocialMediaException {
    // För "vanliga" fel
    public UserNotFoundException(String message) {
        super(message);
    }
    // Har kvar stacktrace
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
