import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.edugrade.dto.subclasses.ContentType;
import se.edugrade.dto.subclasses.ImagePostDTO;
import se.edugrade.entities.*;
import se.edugrade.repositories.LikeRepository;
import se.edugrade.repositories.PostRepository;
import se.edugrade.repositories.UsersRepository;
import se.edugrade.service.PostService;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPostService {

    private EntityManagerFactory emf;
    private PostRepository postRepository;
    private PostService service;
    private Users user;
    private Users user2;
    private Users user3;
    private LikeRepository likeRepository;
    private final int SLEEP_TIME = 10;

    @BeforeEach
    void setup() {
        emf = Persistence.createEntityManagerFactory("TestPU");
        postRepository = new PostRepository(emf);
        service = new PostService(postRepository);
        likeRepository = new LikeRepository(emf);
        UsersRepository usersRepository = new UsersRepository(emf);

        // Ny user skapas för att kunna testa post
        // nanoTime görs så att det skapas nya users för varje testfall så att det inte krockar.
        user = new Users("Lisa_" + System.nanoTime(), "Bio");
        user2 = new Users("Lisa_2_" + System.nanoTime(), "Bio2");
        user3 = new Users("Lisa_3_" + System.nanoTime(), "Bio3");
        usersRepository.create(user);
        usersRepository.create(user2);
        usersRepository.create(user3);
    }

    @AfterEach
    void tearDown() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    @Test
    @DisplayName("showAll ska returnera alla poster som ContentType")
    void testShowAll() {
        // given
        postRepository.create(new ImagePost(user, "bild1.jpg", "cap1"));
        postRepository.create(new ImagePost(user, "bild2.jpg", "cap2"));
        // when
        List<ContentType> result = service.showAll();
        // then
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("showById ska returnera rätt post")
    void testShowById() {
        // given
        ImagePost imagePost = new ImagePost(user, "bild.jpg", "cap");
        Post p = postRepository.create(imagePost);

        // when
        ContentType result = service.showById(p.getId());

        // then
        // Här försäkrar jag mig om att det är en ImagepostDTO
        assertInstanceOf(ImagePostDTO.class, result);

        // Här "kastas" en ContentType till en ImagePost DTO
        ImagePostDTO dto = (ImagePostDTO) result;

        assertEquals("bild.jpg", dto.url(), "URL ska matcha posten som skapades");
        assertEquals("cap", dto.caption(), "Caption ska matcha posten som skapades");
    }


    @Test
    @DisplayName("showById ska kasta exception om ID saknas")
    void testShowByIdNotFound() {
        assertThrows(RuntimeException.class, () -> service.showById(999L));
    }

    @Test
    @DisplayName("sortByNewest ska returnera posts med nyaste först")
    void testSortByNewest() throws InterruptedException {
        // given - Var tvungen att använda thread sleep så att posterna skapades olika millisekunder
        TextPost post1 = new TextPost(user, "First post");
        postRepository.create(post1);

        Thread.sleep(SLEEP_TIME);
        TextPost post2 = new TextPost(user, "Second post");
        postRepository.create(post2);

        Thread.sleep(SLEEP_TIME);
        TextPost post3 = new TextPost(user, "Third post");
        postRepository.create(post3);

        // when
        List<ContentType> sorted = service.sortByNewest();

        // then - Kollar så att sorted size är 3 & att den första i listan är den 3e posten.
        // & att den sista i listan är första posten.

        assertEquals(3, sorted.size(), "Ska returnera 3 posts");
        String first = sorted.get(0).toString();
        String last = sorted.get(2).toString();
        assertTrue(first.contains("Third post"), "Första posten ska vara nyast (Third)");
        assertTrue(last.contains("First post"), "Sista posten ska vara äldst (First)");
    }

    @Test
    @DisplayName("sortByOldest ska returnera posts med äldsta först")
    void testSortByOldest() throws InterruptedException {
        // given
        TextPost post1 = new TextPost(user, "First post");
        postRepository.create(post1);
        Thread.sleep(SLEEP_TIME);
        TextPost post2 = new TextPost(user, "Second post");
        postRepository.create(post2);
        Thread.sleep(SLEEP_TIME);
        TextPost post3 = new TextPost(user, "Third post");
        postRepository.create(post3);

        // when
        List<ContentType> sorted = service.sortByOldest();

        // then
        assertEquals(3, sorted.size(), "Ska returnera 3 posts");
        String first = sorted.get(0).toString();
        String last = sorted.get(2).toString();
        assertTrue(first.contains("First post"), "Första posten ska vara nyast (First)");
        assertTrue(last.contains("Third post"), "Sista posten ska vara äldst (Third)");
    }

    @Test
    @DisplayName("sortByLikes ska returnera posts med flest likes först")
    void testSortByLikes() {
        // given
        TextPost post1 = new TextPost(user, "Blablabla 1 like");
        TextPost post2 = new TextPost(user, "Blaha blaha 3 likes");
        TextPost post3 = new TextPost(user, "Testi test 2 likes");

        postRepository.create(post1);
        postRepository.create(post2);
        postRepository.create(post3);

        // post 1 - 1 like
        likeRepository.create(new Like(post1, user));
        // post 2 - 3 likes
        likeRepository.create(new Like(post2, user));
        likeRepository.create(new Like(post2, user2));
        likeRepository.create(new Like(post2, user3));
        // post 3 - 2 likes
        likeRepository.create(new Like(post3, user));
        likeRepository.create(new Like(post3, user2));

        // when
        List<ContentType> sorted = service.sortByLikes();

        // then
        assertEquals(3, sorted.size());

        String first = sorted.get(0).toString();
        String second = sorted.get(1).toString();
        String third = sorted.get(2).toString();

        // Kollar så den första i listan innehåller den andra posten osv.
        assertTrue(first.contains(post2.getText()), "Post med 3 likes ska vara först");
        assertTrue(second.contains(post3.getText()), "Post med 2 likes ska vara andra");
        assertTrue(third.contains(post1.getText()), "Post med 1 like ska vara sist, minst likes");
    }
}
