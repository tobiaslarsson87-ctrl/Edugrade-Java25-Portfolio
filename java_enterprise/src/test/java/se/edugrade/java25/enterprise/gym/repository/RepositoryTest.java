package se.edugrade.java25.enterprise.gym.repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import se.edugrade.java25.enterprise.gym.model.GymClass;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class RepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GymClassRepository gymClassRepository;

    @Autowired
    private BookingRepository bookingRepository;

    GymClass dummy;

    @BeforeEach
    void setup () {
        dummy = new GymClass();
        dummy.setName("Wrestling");
        dummy.setInstructor("Morgan");
        dummy.setDayOfWeek("Monday");
        dummy.setStartTime("18:00");
        dummy.setDurationMinutes(90);
        dummy.setMaxParticipants(15);

        entityManager.persistAndFlush(dummy);
    }

    @Test
    @DisplayName("findById returns correct GymClass")
    void findById () {
        GymClass found = gymClassRepository.findById(dummy.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isNotNull();
        assertThat(found.getName()).isEqualTo("Wrestling");
    }

    @Test
    @DisplayName("findByInstructor returns matching GymClass")
    void findByInstructor () {
        GymClass result = gymClassRepository.findByInstructor("Morgan").getFirst();

        assertThat(result).isNotNull();
        assertThat(result.getInstructor()).isEqualTo("Morgan");
    }

    @Test
    @DisplayName("findByDayOfWeek returns matching GymClass")
    void findByDayOfWeek () {
        GymClass result = gymClassRepository.findByDayOfWeek("Monday").getFirst();

        assertThat(result).isNotNull();
        assertThat(result.getDayOfWeek()).isEqualTo("Monday");
    }

    @Test
    @DisplayName("countByGymClassId returns 0 since no bookings exist in this context")
    void findCountByGymClasId () {
        int count = bookingRepository.countByGymClassId(1L);
        assertThat(count).isEqualTo(0);
    }
}
