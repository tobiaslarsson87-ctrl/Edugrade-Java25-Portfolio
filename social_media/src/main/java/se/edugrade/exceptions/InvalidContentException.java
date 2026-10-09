package se.edugrade.exceptions;

public class InvalidContentException extends SocialMediaException {
  // För "vanliga" fel
    public InvalidContentException(String message) {
        super(message);
    }
    // Har kvar stacktrace
    public InvalidContentException(String message, Throwable cause) { super(message, cause);
    }

}
