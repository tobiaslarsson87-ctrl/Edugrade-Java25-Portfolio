package se.edugrade.java25.enterprise.gym.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.edugrade.java25.enterprise.gym.dto.GymClassRequest;
import se.edugrade.java25.enterprise.gym.dto.GymClassResponse;
import se.edugrade.java25.enterprise.gym.exception.GymClassNotFoundException;
import se.edugrade.java25.enterprise.gym.model.GymClass;
import se.edugrade.java25.enterprise.gym.repository.GymClassRepository;
import java.util.List;

@Service
public class GymClassService {
    private final GymClassRepository gymClassRepository;
    private static final Logger log = LoggerFactory.getLogger(GymClassService.class);

    public GymClassService(GymClassRepository gymClassRepository) {
        this.gymClassRepository = gymClassRepository;
    }

    //BASICS
    public Page<GymClassResponse> findAll(Pageable pageable) {
        return gymClassRepository.findAll(pageable).map(ServiceUtility::toGymClassResponse);
    }

    public GymClassResponse findById(Long id) {
        return ServiceUtility.toGymClassResponse(
                gymClassRepository.findById(id).orElseThrow(() -> new GymClassNotFoundException(id))
        );
    }

    @Transactional
    public GymClassResponse create(GymClassRequest request) {
        GymClass post = new GymClass();
        post.setName(request.name());
        post.setInstructor(request.instructor());
        post.setDescription(request.description());
        post.setDayOfWeek(request.dayOfWeek());
        post.setStartTime(request.startTime());
        post.setDurationMinutes(request.durationMinutes());
        post.setMaxParticipants(request.maxParticipants());

        GymClass managed = gymClassRepository.save(post);
        log.info("A new gym-class has been created: {} - {} ", managed.getId(), managed.getName());
        log.info("Instructor set to: {}", managed.getInstructor());
        log.info("Description set to: {}", managed.getDescription());
        log.info("Weekday set to: {}", managed.getDayOfWeek());
        log.info("Start-time set to: {}", managed.getStartTime());
        log.info("Duration in minutes set to: {}", managed.getDurationMinutes());
        log.info("Participants set to: {}", managed.getMaxParticipants());
        return ServiceUtility.toGymClassResponse(managed);
    }

    @Transactional
    public GymClassResponse update(GymClassRequest request, Long id) {
        GymClass put = gymClassRepository.findById(id)
                        .orElseThrow(() -> new GymClassNotFoundException(id));

        log.info("Class was found: {} - {}", put.getId(), put.getName());

        put.setName(request.name());
        put.setInstructor(request.instructor());
        put.setDescription(request.description());
        put.setDayOfWeek(request.dayOfWeek());
        put.setStartTime(request.startTime());
        put.setDurationMinutes(request.durationMinutes());
        put.setMaxParticipants(request.maxParticipants());

        GymClass managed = gymClassRepository.save(put);
        log.info("Changes performed on GymClass ID: {}", managed.getId());
        log.info("Name set to: {}", managed.getName());
        log.info("Instructor set to: {}", managed.getInstructor());
        log.info("Description set to: {}", managed.getDescription());
        log.info("Weekday set to: {}", managed.getDayOfWeek());
        log.info("Start-time set to: {}", managed.getStartTime());
        log.info("Duration in minutes set to: {}", managed.getDurationMinutes());
        log.info("Participants set to: {}", managed.getMaxParticipants());

        return ServiceUtility.toGymClassResponse(managed);
    }

    @Transactional
    public void delete(Long id) {
        GymClass delete = gymClassRepository.findById(id)
                .orElseThrow(() -> new GymClassNotFoundException(id));

        gymClassRepository.delete(delete);
        log.info("A gym-class has been deleted: {} - {}", delete.getId(), delete.getName());
    }

    //SPECIAL
    public List<GymClassResponse> findByInstructor(String key) {
        List<GymClass> search = gymClassRepository.findByInstructor(key);
        List<GymClassResponse> result = search.stream()
                .map(ServiceUtility::toGymClassResponse)
                .toList();
        return result;
    }

    public List<GymClassResponse> findByDayOfWeek(String key) {
        List<GymClass> search = gymClassRepository.findByDayOfWeek(key);
        List<GymClassResponse> result = search.stream()
                .map(ServiceUtility::toGymClassResponse)
                .toList();
        return result;
    }

    public int availableSpots(Long id) {
        GymClass target = gymClassRepository.findById(id)
                .orElseThrow(() -> new GymClassNotFoundException(id));

        int booked = target.getBookings().size();
        int available = target.getMaxParticipants() - booked;
        return available;
    }

    public boolean bookable(Long id) {
        return availableSpots(id) > 0;
    }

    //Needed for higher grade
    public List<GymClassResponse> getAllBookable() {
        List<GymClass> allClasses = gymClassRepository.findAll();

        List<GymClass> bookable = allClasses.stream()
                .filter(c -> bookable(c.getId()))
                .toList();

        return bookable.stream()
                .map(ServiceUtility::toGymClassResponse)
                .toList();
    }
}
