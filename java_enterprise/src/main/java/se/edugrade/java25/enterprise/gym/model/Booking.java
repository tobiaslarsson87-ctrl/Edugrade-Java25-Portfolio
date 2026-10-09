package se.edugrade.java25.enterprise.gym.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "participant_name")
    private String participantName;
    @Column(name = "e_mail")
    private String email;
    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

    //use only setter, even in mock test its better to create a new GymClass anyway and send it to the setter
    @ManyToOne
    @JoinColumn(name = "gym_class_id")
    @JsonBackReference
    private GymClass gymClass;


    //No args constructor
    public Booking () {}

    //All args constructor
    public Booking(Long id, String participantName, String email) {
        this.id = id;
        this.participantName = participantName;
        this.email = email;
        this.bookedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getParticipantName() {
        return participantName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }

    //Helper Methods
    public void setGymClass (GymClass gymClass){
        this.gymClass = gymClass;
    }

    //set time when JPA uses no-args constructor
    @PrePersist
    protected void onCreate() {
        this.bookedAt = LocalDateTime.now();
    }
}
