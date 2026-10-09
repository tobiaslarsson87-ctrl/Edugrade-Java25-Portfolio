package se.edugrade.exceptions;

public class DuplicateUsernameException extends SocialMediaException {
    // För "vanliga" fel
    public DuplicateUsernameException(String message) {
        super(message);}
    // Har kvar stacktrace
    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}
