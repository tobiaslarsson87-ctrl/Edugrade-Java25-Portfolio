package se.edugrade.java25.enterprise.gym.service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.edugrade.java25.enterprise.gym.dto.GymClassRequest;
import se.edugrade.java25.enterprise.gym.dto.GymClassResponse;
import se.edugrade.java25.enterprise.gym.exception.GymClassNotFoundException;
import se.edugrade.java25.enterprise.gym.model.Booking;
import se.edugrade.java25.enterprise.gym.model.GymClass;
import se.edugrade.java25.enterprise.gym.repository.GymClassRepository;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GymClassServiceTest {
    @Mock
    private GymClassRepository gymClassRepository;

    @InjectMocks
    private GymClassService gymClassService;

    private GymClass getMockEntity() {
        return new GymClass(
                1L,
                "Boxing",
                "Petter",
                "Fight!",
                "Monday",
                "15:00",
                60,
                12
        );
    }

    private GymClassRequest getMockRequest() {
        return new GymClassRequest(
                "Boxing",
                "Petter",
                "Fight!",
                "Monday",
                "15:00",
                60,
                12
        );
    }

    @Test
    @DisplayName("findById returns GymClassResponse")
    void findById_returnsDTO () {
        GymClass gymClass = getMockEntity();

        when(gymClassRepository.findById(gymClass.getId())).thenReturn(Optional.of(gymClass));

        GymClassResponse response = gymClassService.findById(gymClass.getId());
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Boxing");
    }

    @Test
    @DisplayName("findByDayOfWeek returns List<GymClassResponse>")
    void findByDayOfWeek_returnsCollection () {
        GymClass gymClass = getMockEntity();

        when(gymClassRepository.findByDayOfWeek(gymClass.getDayOfWeek())).thenReturn(List.of(gymClass));

        List<GymClassResponse> response = gymClassService.findByDayOfWeek("Monday");
        assertThat(response.getFirst().dayOfWeek()).isEqualTo("Monday");
    }

    @Test
    @DisplayName("findByInstructor returns List<GymClassResponse>")
    void findByInstructor_returnsCollection () {
        GymClass gymClass = getMockEntity();

        when(gymClassRepository.findByInstructor(gymClass.getInstructor())).thenReturn(List.of(gymClass));

        List<GymClassResponse> response = gymClassService.findByInstructor("Petter");
        assertThat(response.getFirst().instructor()).isEqualTo("Petter");
    }

    @Test
    @DisplayName("findById returns GymClassNotfoundException")
    void findById_returnsException () {
        when(gymClassRepository.findById(9999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> gymClassService.findById(9999L))
                .isInstanceOf(GymClassNotFoundException.class);
    }

    @Test
    @DisplayName("create saves and returns new GymClass")
    void create_saveAndReturn () {
        GymClassRequest request = getMockRequest();
        GymClass saved = getMockEntity();

        when(gymClassRepository.save(any(GymClass.class))).thenReturn(saved);
        GymClassResponse response = gymClassService.create(request);

        assertThat(response.name()).isEqualTo("Boxing");
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("availableSpots return correct number of remaining spots")
    void availableSpots_correctCalculation () {
        GymClass gymClass = getMockEntity();
        gymClass.addBooking(new Booking());

        when(gymClassRepository.findById(1L)).thenReturn(Optional.of(gymClass));
        int available = gymClassService.availableSpots(1L);
        assertThat(available).isEqualTo(11); // 12 - 1 should equal 11
    }
}
