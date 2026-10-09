package se.edugrade.java25.enterprise.gym.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import se.edugrade.java25.enterprise.gym.model.GymClass;
import java.util.List;

public interface GymClassRepository extends JpaRepository<GymClass, Long> {
    List<GymClass> findByInstructor(String key);
    List<GymClass> findByDayOfWeek(String key);
}
