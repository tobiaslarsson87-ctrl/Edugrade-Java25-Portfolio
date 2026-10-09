package se.edugrade.java25.enterprise.gym.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import se.edugrade.java25.enterprise.gym.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    int countByGymClassId(Long id);
}
