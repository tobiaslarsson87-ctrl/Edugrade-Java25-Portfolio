package se.edugrade.java25.enterprise.gym.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import se.edugrade.java25.enterprise.gym.exception.GymClassNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gym_class")
public class GymClass {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "instructor")
    private String instructor;
    @Column(name = "description")
    private String description;
    @Column(name = "day_of_week")
    private String dayOfWeek;
    @Column(name = "start_time")
    private String startTime;
    @Column(name = "duration_minutes")
    private int durationMinutes;
    @Column(name = "max_participants")
    private int maxParticipants;

    @OneToMany(mappedBy = "gymClass", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Booking> bookings;


    //No args constructor
    public GymClass () {
        this.bookings = new ArrayList<>();
    }

    //All args constructor
    public GymClass(Long id, String name, String instructor, String description, String dayOfWeek, String startTime, int durationMinutes, int maxParticipants) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
        this.description = description;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.maxParticipants = maxParticipants;
        this.bookings = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getDescription() {
        return description;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    //Helper Methods
    public void addBooking(Booking booking) {
        this.bookings.add(booking);
        booking.setGymClass(this);
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public int availableSpots() {
        int booked = this.getBookings().size();
        int available = this.getMaxParticipants() - booked;
        return available;
    }

    public boolean isBookable() {
        return availableSpots() > 0;
    }
}
