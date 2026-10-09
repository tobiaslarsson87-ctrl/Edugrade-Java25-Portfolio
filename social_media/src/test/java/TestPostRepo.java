import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.edugrade.entities.*;
import se.edugrade.repositories.PostRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testar:
 * - Create
 * - findbyId
 * - findbyId om id inte finns
 * - findAll
 * - findAll om post är tom
 * - update
 * - delete
 * - delete med id som inte finns ska INTE kasta exception (?)
 * - findAll ska hantera alla post-typer
 * - Discriminator value ska sparas som korrekt post-typ
 */
public class TestPostRepo {

    private EntityManagerFactory emf;
    private PostRepository repo;
    private Users user;

    // Hjälpmetod för att kasta Post → ImagePost
    private ImagePost asImagePost(Post post) {
        assertTrue(post instanceof ImagePost, "Post is not an ImagePost");
        return (ImagePost) post;
    }

    @BeforeEach
    void setup() {
        emf = Persistence.createEntityManagerFactory("TestPU");
        repo = new PostRepository(emf);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        // Ny user skapas för att kunna testa post
        // nanoTime görs så att det skapas nya users för varje testfall så att det inte krockar.
        user = new Users("Lisa_" + System.nanoTime(), "Bio");
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }

    @AfterEach
    void tearDown() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    @Test
    @DisplayName("create ska spara en post i databasen")
    void testCreatePost() {
        // given
        Post post = new ImagePost(user, "bild.jpg", "Caption");
        // when
        Post saved = repo.create(post);
        // then
        ImagePost imagePost = asImagePost(saved);
        assertNotNull(imagePost.getId());
        assertEquals("bild.jpg", imagePost.getImageUrl());
    }

    @Test
    @DisplayName("findById ska returnera post om den finns")
    void testFindById() {
        // given
        Post post = new ImagePost(user, "bild.jpg", "Caption");
        repo.create(post);
        // when
        Optional<Post> found = repo.findById(post.getId());
        // then
        assertTrue(found.isPresent());
        ImagePost imagePost = asImagePost(found.get());
        assertEquals("bild.jpg", imagePost.getImageUrl());
    }

    @Test
    @DisplayName("findById ska returnera tom Optional om ID inte finns")
    void testFindByIdNotFound() {
        assertTrue(repo.findById(999L).isEmpty());
    }

    @Test
    @DisplayName("findAll ska returnera alla poster")
    void testFindAll() {
        // given
        repo.create(new ImagePost(user, "bild1.jpg", "Caption1"));
        repo.create(new ImagePost(user, "bild2.jpg", "Caption2"));
        // when
        List<Post> posts = repo.findAll();
        // then
        assertEquals(2, posts.size());
        ImagePost p1 = asImagePost(posts.get(0));
        ImagePost p2 = asImagePost(posts.get(1));
        assertNotNull(p1.getImageUrl());
        assertNotNull(p2.getImageUrl());
    }

    @Test
    @DisplayName("findAll ska returnera tom lista om inga poster finns")
    void testFindAllEmpty() {
        List<Post> posts = repo.findAll();
        assertTrue(posts.isEmpty());
    }

    @Test
    @DisplayName("update ska uppdatera en post")
    void testUpdatePost() {
        // given
        Post post = new ImagePost(user, "bild.jpg", "Caption");
        repo.create(post);
        // when
        ImagePost imagePost = asImagePost(post);
        imagePost.setCaption("Ny caption");
        Post updated = repo.update(imagePost);
        // then
        ImagePost updatedImage = asImagePost(updated);
        assertEquals("Ny caption", updatedImage.getCaption());
    }

    @Test
    @DisplayName("delete ska ta bort en post")
    void testDeletePost() {
        // given
        Post post = new ImagePost(user, "bild.jpg", "Caption");
        repo.create(post);
        Long id = post.getId();

        // when
        repo.delete(id);

        // then
        assertTrue(repo.findById(id).isEmpty());
    }

    @Test
    @DisplayName("delete med icke-existerande ID ska inte kasta exception")
    void testDeleteNotFound() {
        assertDoesNotThrow(() -> repo.delete(999L));
    }

    @Test
    @DisplayName("Hittar sorterad lista av post & kollar sorteringen")
    void testfindAllOrdered() throws InterruptedException {
        // given
        repo.create(new ImagePost(user, "bild.jpg", "Caption"));
        Thread.sleep(5);
        repo.create(new TextPost(user, "Hej hej"));
        Thread.sleep(5);
        repo.create(new LinkPost(user, "https://example.com", "Länk"));
        Thread.sleep(5);
        repo.create(new VideoPost(user, "https://example.com", 76767));

        // when
        List<Post> posts = repo.findAllOrdered();

        // then
        assertEquals(4, posts.size());
        assertTrue(posts.get(0).getCreatedAt().isAfter(posts.get(1).getCreatedAt()));
        assertTrue(posts.get(1).getCreatedAt().isAfter(posts.get(2).getCreatedAt()));
        assertTrue(posts.get(2).getCreatedAt().isAfter(posts.get(3).getCreatedAt()));
    }

    @Test
    @DisplayName("findAll ska hantera flera olika posttyper")
    void testFindAllPolymorphic() {
        // given
        repo.create(new ImagePost(user, "bild.jpg", "Caption"));
        repo.create(new TextPost(user, "Hej hej"));
        repo.create(new LinkPost(user, "https://example.com", "Länk"));
        repo.create(new VideoPost(user, "https://example.com", 76767));
        // when
        List<Post> posts = repo.findAll();
        // then
        assertEquals(4, posts.size());
        assertTrue(posts.stream().anyMatch(p -> p instanceof ImagePost));
        assertTrue(posts.stream().anyMatch(p -> p instanceof TextPost));
        assertTrue(posts.stream().anyMatch(p -> p instanceof LinkPost));
        assertTrue(posts.stream().anyMatch(p -> p instanceof VideoPost));

    }
    @Test
    @DisplayName("DiscriminatorValue ska sparas korrekt")
    void testDiscriminator() {
        // given
        Post p = new ImagePost(user, "bild.jpg", "Caption");
        // when
        repo.create(p);
        // then
        Optional<Post> found = repo.findById(p.getId());
        assertTrue(found.isPresent());
        assertEquals("IMAGE", found.get().getContentType());
    }
}
