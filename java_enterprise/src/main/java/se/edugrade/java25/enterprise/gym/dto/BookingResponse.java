package se.edugrade.java25.enterprise.gym.dto;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        String participantName,
        String email,
        LocalDateTime bookedAt
){}
