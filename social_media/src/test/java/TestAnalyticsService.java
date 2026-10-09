import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.edugrade.entities.*;
import se.edugrade.repositories.HashtagRepository;
import se.edugrade.repositories.PostRepository;
import se.edugrade.repositories.UsersRepository;
import se.edugrade.service.AnalyticsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestAnalyticsService {

    private EntityManagerFactory emf;
    private UsersRepository usersRepository;
    private PostRepository postRepository;
    private HashtagRepository hashtagRepository;
    private AnalyticsService analytics;
    private Users user1;
    private Users user2;

    @BeforeEach
    void setup() {
        emf = Persistence.createEntityManagerFactory("TestPU");
        usersRepository = new UsersRepository(emf);
        postRepository = new PostRepository(emf);
        hashtagRepository = new HashtagRepository(emf);
        analytics = new AnalyticsService();
        // Skapar testanvändare
        user1 = usersRepository.create(new Users("Lisa_" + System.nanoTime(), "Bio"));
        user2 = usersRepository.create(new Users("Test_" + System.nanoTime(), "Bio2"));
    }

    @AfterEach
    void tearDown() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    private void persistLike(Like like) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(like);
        em.getTransaction().commit();
        em.close();
    }

    private void persistHashtag(Hashtag hashtag) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(hashtag);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    @DisplayName("Trending posts ska sorteras efter engagement (likes + comments)")
    void testTrendingPosts() {
        // Given - Tre posts med olika antal likes
        Post p1 = postRepository.create(new TextPost(user1, "A"));
        Post p2 = postRepository.create(new TextPost(user1, "B"));
        Post p3 = postRepository.create(new TextPost(user1, "C"));

        persistLike(new Like(p3, user1));
        persistLike(new Like(p3, user2)); // 2 likes
        persistLike(new Like(p2, user1)); // 1 like

        p1 = postRepository.findByIdWithRelations(p1.getId()).orElseThrow();
        p2 = postRepository.findByIdWithRelations(p2.getId()).orElseThrow();
        p3 = postRepository.findByIdWithRelations(p3.getId()).orElseThrow();
        // When
        List<Post> trending = analytics.getTopTrendingPosts(List.of(p1, p2, p3), 3);
        // then
        assertEquals(p3.getId(), trending.get(0).getId(), "p3 ska vara först");
        assertEquals(p2.getId(), trending.get(1).getId(), "p2 ska vara tvåa");
    }

    @Test
    @DisplayName("peekTrending ska returnera högst rankade post")
    void testPeekTrending() {
        // given
        Post p1 = postRepository.create(new TextPost(user1, "A"));
        Post p2 = postRepository.create(new TextPost(user1, "B"));

        persistLike(new Like(p2, user1)); // p2 - 1 like

        p1 = postRepository.findByIdWithRelations(p1.getId()).orElseThrow();
        p2 = postRepository.findByIdWithRelations(p2.getId()).orElseThrow();
        // when
        Optional<Post> peek = analytics.peekTrending(List.of(p1, p2));
        // then
        assertTrue(peek.isPresent());
        assertEquals(p2.getId(), peek.get().getId());
    }

    @Test
    @DisplayName("Average engagement ska räkna snittet korrekt")
    void testAverageEngagement() {
        // given
        Post p1 = postRepository.create(new TextPost(user1, "A"));
        Post p2 = postRepository.create(new TextPost(user1, "B"));

        persistLike(new Like(p1, user1));
        persistLike(new Like(p1, user2)); // 2 likes
        persistLike(new Like(p2, user1)); // 1 like

        p1 = postRepository.findByIdWithRelations(p1.getId()).orElseThrow();
        p2 = postRepository.findByIdWithRelations(p2.getId()).orElseThrow();
        // when
        double avg = analytics.getAverageEngagement(List.of(p1, p2));
        // then
        assertEquals(1.5, avg, 0.01);
    }

    @Test
    @DisplayName("Most liked post ska returnera posten med flest likes")
    void testMostLikedPost() {
        // given
        Post p1 = postRepository.create(new TextPost(user1, "A"));
        Post p2 = postRepository.create(new TextPost(user1, "B"));

        persistLike(new Like(p2, user1));
        persistLike(new Like(p2, user2)); // 2 likes
        p1 = postRepository.findByIdWithRelations(p1.getId()).orElseThrow();
        p2 = postRepository.findByIdWithRelations(p2.getId()).orElseThrow();
        // when
        Optional<Post> mostLiked = analytics.getMostLikedPost(List.of(p1, p2));
        // then
        assertTrue(mostLiked.isPresent());
        assertEquals(p2.getId(), mostLiked.get().getId());
    }

    @Test
    @DisplayName("Hashtag engagement ska summera korrekt")
    void testHashtagEngagement() {
        // given
        Hashtag h1 = hashtagRepository.create(new Hashtag("#java"));
        Hashtag h2 = hashtagRepository.create(new Hashtag("#backend"));

        Post p1 = postRepository.create(new TextPost(user1, "A"));
        Post p2 = postRepository.create(new TextPost(user1, "B"));

        // Koppla båda sidor hashtag o post
        p1.getHashtags().add(h1);
        h1.getPost().add(p1);
        p1.getHashtags().add(h2);
        h2.getPost().add(p1);
        p2.getHashtags().add(h2);
        h2.getPost().add(p2);

        postRepository.update(p1);
        postRepository.update(p2);
        persistHashtag(h1);
        persistHashtag(h2);

        p1 = postRepository.findByIdWithRelations(p1.getId()).orElseThrow();
        p2 = postRepository.findByIdWithRelations(p2.getId()).orElseThrow();
        // when
        Map<String, Integer> result = analytics.getHashtagEngagement(List.of(p1, p2));
        // then
        assertEquals(1, result.get("#java"));
        assertEquals(2, result.get("#backend"));
    }
    @Test
    @DisplayName("Distinct recent posts ska ta bort dubbletter och sortera efter datum")
    void testDistinctRecentPosts() {
        // given
        Post p1 = new TextPost(user1, "A");
        // Sätter tiden manuellt så sorteringen blir rätt för testet
        p1.setCreatedAt(LocalDateTime.parse("2025-01-01T10:00"));
        p1 = postRepository.create(p1);

        Post p2 = new TextPost(user1, "B");
        p2.setCreatedAt(LocalDateTime.parse("2025-01-02T10:00"));
        p2 = postRepository.create(p2);

        Post p3 = p1; // dublett
        // when
        List<Post> result = analytics.getDistinctRecentPosts(List.of(p1, p2, p3));
        // then
        assertEquals(2, result.size());
        assertEquals(p2, result.get(0));
    }

    // Test för bugg-sökning när hashtag ej kopplades med post
    @Test
    @DisplayName("Hashtag ska sparas och laddas korrekt på en Post")
    void testHashtagSavedOnPost() {

        // Given
        Hashtag h = hashtagRepository.create(new Hashtag("#testtag"));
        Post p = postRepository.create(new TextPost(user1, "Hello"));

        // Koppla båda sidor
        p.getHashtags().add(h);
        h.getPost().add(p);
        postRepository.update(p);
        persistHashtag(h);

        // When
        Post fetched = postRepository.findByIdWithRelations(p.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Then - posten ska ha exakt EN hashtag och den ska vara #testtag
        assertEquals(1, fetched.getHashtags().size(), "Posten ska ha 1 hashtag");
        assertTrue(
                fetched.getHashtags().stream().anyMatch(tag -> tag.getTag().equals("#testtag")),
                "Hashtag #testtag ska finnas på posten"
        );
    }
}
