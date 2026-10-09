package se.edugrade.java25.enterprise.gym.dto;
import jakarta.validation.constraints.NotBlank;

public record BookingRequest(
        @NotBlank
        String participantName,
        @NotBlank
        String email
){}
