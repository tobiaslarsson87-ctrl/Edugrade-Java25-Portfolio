package se.edugrade.java25.enterprise.gym.dto;

public record GymClassResponse(
        Long id,
        String name,
        String instructor,
        String description,
        String dayOfWeek,
        String startTime,
        int durationMinutes,
        int maxParticipants
){}
