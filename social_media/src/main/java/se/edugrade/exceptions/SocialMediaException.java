package se.edugrade.exceptions;

public class SocialMediaException extends RuntimeException {
    // För "vanliga" fel
    public SocialMediaException(String message) {
        super(message);
    }
    // Har kvar stacktrace
    public SocialMediaException(String message, Throwable cause) { super(message, cause);
    }
}
