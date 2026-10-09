package se.edugrade.java25.enterprise.gym.dto;

public record AuthResponse(
        String token,
        String type,
        long expiresIn
) {
    public static AuthResponse of(String token, long expiresIn) {
        return new AuthResponse(token, "Bearer", expiresIn);
    }
}
