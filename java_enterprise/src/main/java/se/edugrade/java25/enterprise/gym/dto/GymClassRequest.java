package se.edugrade.java25.enterprise.gym.dto;
import jakarta.validation.constraints.*;

public record GymClassRequest(
        @NotBlank
        String name,
        @NotBlank
        String instructor,
        String description,
        @NotBlank
        String dayOfWeek,
        @NotBlank
        String startTime,
        @Min(15) @Max(120)
        int durationMinutes,
        @Min(1) @Max(50)
        int maxParticipants
){}
