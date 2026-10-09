package se.edugrade.java25.enterprise.gym.controller;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.edugrade.java25.enterprise.gym.dto.BookingRequest;
import se.edugrade.java25.enterprise.gym.dto.BookingResponse;
import se.edugrade.java25.enterprise.gym.dto.GymClassRequest;
import se.edugrade.java25.enterprise.gym.dto.GymClassResponse;
import se.edugrade.java25.enterprise.gym.service.BookingService;
import se.edugrade.java25.enterprise.gym.service.GymClassService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/classes")
public class GymClassController {
    private final GymClassService gymClassService;
    private final BookingService bookingService;

    public GymClassController(GymClassService gymClassService, BookingService bookingService) {
        this.gymClassService = gymClassService;
        this.bookingService = bookingService;
    }

    //BASIC GYM-CLASS ENDPOINTS
    @GetMapping()
    public ResponseEntity<Page<GymClassResponse>> getAll(@ParameterObject Pageable pageable) {
        Page<GymClassResponse> response = gymClassService.findAll(pageable);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GymClassResponse> getById(@PathVariable Long id) {
        GymClassResponse response = gymClassService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GymClassResponse>> getByInstructor(
            @RequestParam(required = false) String instructor,
            @RequestParam(required = false) String day
    )
    {
        if (instructor != null) {
            List<GymClassResponse> response = gymClassService.findByInstructor(instructor);
            return ResponseEntity.ok(response);
        }

        if (day != null) {
            List<GymClassResponse> response = gymClassService.findByDayOfWeek(day);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(List.of());
    }

    @PostMapping()
    public ResponseEntity<GymClassResponse> create(@Valid @RequestBody GymClassRequest request) {
        GymClassResponse response = gymClassService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GymClassResponse> update(@PathVariable Long id, @Valid @RequestBody GymClassRequest request) {
        GymClassResponse response = gymClassService.update(request, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gymClassService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //BOOKING ENDPOINTS GOING TROUGH /classes
    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(@PathVariable Long id) {
        List<BookingResponse> response = bookingService.findAll(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/bookings")
    public ResponseEntity<BookingResponse> bookClass(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.create(request, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //SPECIAL ENDPOINTS
    @GetMapping("/{id}/spots-remaining")
    public ResponseEntity<Map<String, Integer>> checkAvailableSpots(@PathVariable Long id) {
        int availableSpots = gymClassService.availableSpots(id);
        Map<String, Integer> body = Map.of("availableSpots", availableSpots);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/available")
    public ResponseEntity<List<GymClassResponse>> getAllBookable() {
        List<GymClassResponse> response = gymClassService.getAllBookable();
        return ResponseEntity.ok(response);
    }
}
