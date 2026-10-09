package se.edugrade.java25.enterprise.gym.service;
import se.edugrade.java25.enterprise.gym.dto.BookingResponse;
import se.edugrade.java25.enterprise.gym.dto.GymClassResponse;
import se.edugrade.java25.enterprise.gym.model.Booking;
import se.edugrade.java25.enterprise.gym.model.GymClass;

public class ServiceUtility {
    public static GymClassResponse toGymClassResponse(GymClass gymClass) {
        return new GymClassResponse(
                gymClass.getId(),
                gymClass.getName(),
                gymClass.getInstructor(),
                gymClass.getDescription(),
                gymClass.getDayOfWeek(),
                gymClass.getStartTime(),
                gymClass.getDurationMinutes(),
                gymClass.getMaxParticipants()
        );
    }
    public static BookingResponse toBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getParticipantName(),
                booking.getEmail(),
                booking.getBookedAt()
        );
    }
}
