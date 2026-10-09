package se.edugrade.java25.enterprise.gym.service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.edugrade.java25.enterprise.gym.dto.BookingRequest;
import se.edugrade.java25.enterprise.gym.dto.BookingResponse;
import se.edugrade.java25.enterprise.gym.exception.BookingNotFoundException;
import se.edugrade.java25.enterprise.gym.exception.CapacityExceededException;
import se.edugrade.java25.enterprise.gym.exception.GymClassNotFoundException;
import se.edugrade.java25.enterprise.gym.model.Booking;
import se.edugrade.java25.enterprise.gym.model.GymClass;
import se.edugrade.java25.enterprise.gym.repository.BookingRepository;
import se.edugrade.java25.enterprise.gym.repository.GymClassRepository;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final GymClassRepository gymClassRepository;
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepository, GymClassRepository gymClassRepository) {
        this.bookingRepository = bookingRepository;
        this.gymClassRepository = gymClassRepository;
    }

    //BASIC
    public List<BookingResponse> findAll(Long gymClassId) {
        GymClass target = gymClassRepository.findById(gymClassId)
                .orElseThrow(() -> new GymClassNotFoundException(gymClassId));

        log.info("Gym-class with ID {} was found. Showing bookings: ", gymClassId);

        List<Booking> bookings = target.getBookings();
        return bookings.stream()
                .map(ServiceUtility::toBookingResponse)
                .toList();
    }

    @Transactional
    public BookingResponse create(BookingRequest request, Long gymClassId) {
        GymClass target = gymClassRepository.findById(gymClassId)
                .orElseThrow(() -> new GymClassNotFoundException(gymClassId));

        Booking post = new Booking();
        post.setParticipantName(request.participantName());
        post.setEmail(request.email());

        if (!target.isBookable()) throw new CapacityExceededException("Class: " + target.getName() + " is already fully booked!");

        target.addBooking(post);
        Booking managed = bookingRepository.save(post);
        log.info("A new booking has been created: {}: {}.", managed.getId(), managed.getParticipantName(), managed.getEmail());
        log.info("Target for booking is gym-class: {}: {}", target.getId(), target.getName());
        return ServiceUtility.toBookingResponse(managed);
    }

    @Transactional
    public void delete(Long id) {
        Booking delete = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        bookingRepository.delete(delete);
        log.info("A booking has been deleted: {}: {} - {}.", delete.getId(), delete.getParticipantName(), delete.getEmail());
    }
}
