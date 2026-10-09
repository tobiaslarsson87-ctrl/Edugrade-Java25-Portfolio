import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import se.edugrade.entities.Comment;
import se.edugrade.entities.ImagePost;
import se.edugrade.entities.Users;
import se.edugrade.exceptions.InvalidContentException;
import se.edugrade.repositories.CommentRepository;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCommentRepo {

    private EntityManagerFactory emf;
    private CommentRepository repo;
    private Users users;
    private ImagePost post;

    @BeforeEach
    void setup() {
        emf = Persistence.createEntityManagerFactory("TestPU");
        repo = new CommentRepository(emf);

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        // Skapar en ny user & post, det behövs för att kunna testa kommentarerna i databasen.
        users = new Users("Lisa_" + System.nanoTime(), "Bio");
        em.persist(users);

        post = new ImagePost(users, "bild.jpg", "Caption");
        em.persist(post);

        em.getTransaction().commit();
        em.close();
    }

    @AfterEach
    void tearDown() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    @Test
    @DisplayName("findById ska returnera kommentar om den finns")
    void testFindById() {
        Comment comment = new Comment(post, users, "Test");
        repo.create(comment);
        Optional<Comment> found = repo.findById(comment.getId());
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getText());
    }

    // Skapar en comment, uppdaterar den till något nytt och sparar den uppdaterade i databasen
    @Test
    @DisplayName("update ska ändra texten på en kommentar")
    void testUpdateComment() {
        Comment c = new Comment(post, users, "Original");
        repo.create(c);

        c.setText("Uppdaterad");
        Comment updated = repo.update(c);

        assertNotNull(updated);
        assertEquals("Uppdaterad", updated.getText());
    }
    @Test
    @DisplayName("delete ska ta bort kommentar från databasen")
    void testDeleteComment() {
        Comment comment = new Comment(post, users, "Delete me");
        repo.create(comment);
        repo.delete(comment.getId());
        assertTrue(repo.findAll().isEmpty());
    }
    @Test
    @DisplayName("Kollar så att det går att hämta kommentarer genom findAll")
    void testfindAll(){
        Comment c = new Comment(post, users, "det här är en text");
        repo.create(c);
       List<Comment> comments = repo.findAll();
       assertTrue(comments.size()>0);
    }
    @Test
    @DisplayName("Kollar så att comment-listan är empty")
    void testEmptyfindAll(){
        List<Comment> comments = repo.findAll();
        assertTrue(comments.isEmpty());
    }

    @Test
    @DisplayName("findById ska returnera tom Optional om ID inte finns")
    void testFindByIdNotFound() {
        assertTrue(repo.findById(999L).isEmpty());
    }

    @Test
    @DisplayName("delete med icke-existerande ID ska inte kasta exception")
    void testDeleteNotFound() {
        assertDoesNotThrow(() -> repo.delete(999L));
    }

    @ParameterizedTest
    @DisplayName("Testa giltig text i kommentarer")
    @ValueSource(strings = {
            "Hejhej",
            "Det här ska va en ny kommentar",
            "Testitest",
            "test igen",
            "Hur många ska man göra?"
    })
    void testValidCommentTexts(String text) {
        Comment c = new Comment(post, users, text);
        Comment saved = repo.create(c);
        assertNotNull(saved.getId());
        assertEquals(text, saved.getText());
    }

    @ParameterizedTest
    @DisplayName("Testar ogiltiga kommentarer")
    @NullAndEmptySource
    @ValueSource(strings = {" ", " "})
    void testInvalidCommentTexts(String text) {
        assertThrows(InvalidContentException.class, () -> {
            new Comment(post, users, text);
        });
    }
}