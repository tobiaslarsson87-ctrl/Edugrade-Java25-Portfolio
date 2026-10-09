import jakarta.persistence.EntityManagerFactory;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.edugrade.entities.Users;
import se.edugrade.entities.Like;
import se.edugrade.entities.Post;
import se.edugrade.entities.TextPost;
import se.edugrade.repositories.LikeRepository;


import java.util.Optional;

public class TestLike {
    private static EntityManagerFactory emf;
    private LikeRepository likeRepository;

    private Users user;
    private Post post;


    // Körs en gång innan alla tester
    @BeforeAll
    static void setupDatabase() {
        emf = Persistence.createEntityManagerFactory("TestPU");
    }

    //Körs före varje test
    @BeforeEach void setup() {
        likeRepository = new LikeRepository(emf);

        var em = emf.createEntityManager();
        em.getTransaction().begin();

        // Rensar likes-tabellen så varje test körs mot en tom databas
        em.createQuery("DELETE FROM Like").executeUpdate();

        // Används i test för att skapa ett unikt användarnamn vid varje testkörning
        user = new Users("TestUser_" + System.nanoTime(), "Bio");
        em.persist(user);

        post = new TextPost(user, "Test post");
        em.persist(post);

        em.getTransaction().commit();
        em.close();
    }

    // Create

    @Test
    void shouldCreateLike() {
        Like like = new Like(post, user);

        Like saved = likeRepository.create(like);

        assertNotNull(saved.getId());
    }


    @Test
    void shouldFindLikeById() {
        Like like = likeRepository.create(new Like(post, user));

        Optional<Like> found = likeRepository.findById(like.getId());

        assertTrue(found.isPresent());
    }

    // Read

    @Test
    void shouldFindAllLikes() {
        likeRepository.create(new Like(post, user));
        assertEquals(1, likeRepository.findAll().size());

    }

    // Update

    // UPDATE
    @Test
    void shouldUpdateLikeTimestamp() {
        // Arrange – skapa like
        Like like = likeRepository.create(new Like(post, user));

        // Act – uppdatera något på entiteten
        like.setTimestamp(like.getTimestamp().plusSeconds(10));
        Like updated = likeRepository.update(like);

        // Assert – kontrollera att databasen sparade ändringen
        assertEquals(like.getTimestamp(), updated.getTimestamp());
    }

    //Delete

    @Test
    void shouldDeleteLike() {
        Like like = likeRepository.create(new Like(post, user));
        likeRepository.delete(like.getId());

        Optional<Like> found = likeRepository.findById(like.getId());
        assertTrue(found.isEmpty());
    }
    //Edge cases & Constraints

    //Visar att Optional används korrekt (inga nulls)
    @Test
    void shouldReturnEmptyOptionalWhenLikeDoesNotExist() {
        Optional<Like> result = likeRepository.findById(99999L);
        assertTrue(result.isEmpty());
    }
/*
    @ParameterizedTest
    @ValueSource(longs = { -1L, 0L, 5L, 99999L })
    void shouldReturnEmptyOptionalForMultipleInvalidIds(long id) {
        Optional<Like> result = likeRepository.findById(id);
        assertTrue(result.isEmpty());
    }

 */
    //Test när tabellen är tom

    @Test
    void shouldReturnEmptyListWhenNoLikesExist() {
        assertEquals(0, likeRepository.findAll().size());
    }

    //Unique constraint test, En användare får inte likea samma post 2 gånger (testar databashantering & exeptions)

    @Test
    void shouldNotAllowUserToLikeSamePostTwice() {
        likeRepository.create(new Like(post, user));

        assertThrows(Exception.class,
                () -> likeRepository.create(new Like(post, user)));
    }

    //Stänger alla tester
    @AfterAll
    static void closeDatabase() {
        emf.close();
    }
}
