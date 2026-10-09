import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.edugrade.dto.CommentDTO;
import se.edugrade.entities.Comment;
import se.edugrade.entities.ImagePost;
import se.edugrade.entities.Post;
import se.edugrade.entities.Users;
import se.edugrade.repositories.CommentRepository;
import se.edugrade.repositories.PostRepository;
import se.edugrade.repositories.UsersRepository;
import se.edugrade.service.CommentService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCommentService {
    private EntityManagerFactory emf;
    private CommentRepository commentRepository;
    private UsersRepository usersRepository;
    private PostRepository postRepository;
    private CommentService cs;

    private Users user1;
    private Users user2;
    private Post post;

    @BeforeEach
    void setup() {
        emf = Persistence.createEntityManagerFactory("TestPU");
        commentRepository = new CommentRepository(emf);
        usersRepository = new UsersRepository(emf);
        postRepository = new PostRepository(emf);
        cs = new CommentService(emf);
        // Skapar testanvändare
        user1 = new Users("Lisa_" + System.nanoTime(), "Bio");
        user2 = new Users("Test_" + System.nanoTime(), "Bio 2");
        user1 = usersRepository.create(user1);
        user2 = usersRepository.create(user2);
        // skapar testpost
        post = new ImagePost(user1, "bild.jpg", "Caption");
        post = postRepository.create(post);

    }

    @AfterEach
    void tearDown() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    @Test
    @DisplayName("SearchCommentbykeyword ska hitta kommentar med keyword")
    void testSearchcommentbykeyword() {
        // given
        Comment c = new Comment(post, user1, "det här är en text");
        commentRepository.create(c);
        Comment a = new Comment(post, user1, "det här är en annan kommentar");
        commentRepository.create(a);
        Comment b = new Comment(post, user1, "det här är en sjätte kommentar");
        commentRepository.create(b);
        post = postRepository.findById(post.getId()).orElseThrow();
        // when
        List<CommentDTO> foundComments = cs.searchCommentsByKeyword(post,"text");
        // then
        assertEquals(1,foundComments.size(), "Ska hitta den kommentar som innehåller text");
        assertTrue(foundComments.get(0).text().contains("text"), "Kommentaren ska innehålla 'text'");
    }

    @Test
    @DisplayName("getsortedComments ska returnerakommentarer sorterade")
    void testgetsortedComments() {
        //given
        Comment c = new Comment(post, user1, "Första");
        commentRepository.create(c);
        Comment a = new Comment(post, user1, "Andra");
        commentRepository.create(a);
        Comment b = new Comment(post, user1, "Tredje");
        commentRepository.create(b);
        post = postRepository.findById(post.getId()).orElseThrow();
        // when
        List<CommentDTO> sorted = cs.getSortedComments(post);
        // then
        assertEquals(3, sorted.size(), "Ska returnera 3 kommentarer");
        assertTrue(sorted.get(0).text().contains("Första"), "Första kommentaren ska vara först");
        assertTrue(sorted.get(2).text().contains("Tredje"), "Tredje kommentaren ska vara sist");
    }

    @Test
    @DisplayName("countcommentsperuser ska räkna comments per användare ")
    void testcountcommentperuser() {
        // given
        Comment c1 = new Comment(post, user1, "User1 kommentar 1");
        Comment c2 = new Comment(post, user1, "User1 kommentar 2");
        Comment c3 = new Comment(post, user1, "User1 kommentar 3");
        Comment c4 = new Comment(post, user2, "User2 kommentar 1");

        commentRepository.create(c1);
        commentRepository.create(c2);
        commentRepository.create(c3);
        commentRepository.create(c4);
        post = postRepository.findById(post.getId()).orElseThrow();
        // when
        Map<String, Long> counts = cs.countCommentsByUser(post);
        // then
        assertEquals(2, counts.size(), "Ska ha 2 användare");
        assertEquals(3L, counts.get(user1.getUserName()), "User1 ska ha 3 kommentarer");
        assertEquals(1L, counts.get(user2.getUserName()), "User2 ska ha 1 kommentar");
    }

    @Test
    @DisplayName("Avarage comment length ska räkna genomsnittlig längd på comment")
    void testavaragecommentleght() {
        // given
        Comment a = new Comment(post, user1, "abc");
        Comment b = new Comment(post, user1, "abcde");
        Comment c = new Comment(post, user1, "abcdefg");
        commentRepository.create(a);
        commentRepository.create(b);
        commentRepository.create(c);
        post = postRepository.findById(post.getId()).orElseThrow();
        // when
        double avarage = cs.averageCommentLength(post);
        // then
        assertEquals(5.0,avarage, 0.01, "Genomsnittlig längd ska vara 5.0");
    }
    @Test
    @DisplayName("Metoder ska bara räkna kommentarer för rätt post")
    void testMethodsOnlyCountCorrectPost() {
        // Given
        Post post2 = new ImagePost(user1, "https://example.com/other.jpg", "Other");
        post2 = postRepository.create(post2);

        // Kommentarer på post1
        commentRepository.create(new Comment(post, user1, "Post 1 comment"));

        // Kommentarer på post2
        commentRepository.create(new Comment(post2, user1, "Post 2 comment"));
        commentRepository.create(new Comment(post2, user1, "Post 2 another comment"));
        post = postRepository.findById(post.getId()).orElseThrow();
        post2 = postRepository.findById(post2.getId()).orElseThrow();

        // when
        List<CommentDTO> post1Comments = cs.getSortedComments(post);
        List<CommentDTO> post2Comments = cs.getSortedComments(post2);

        // then
        assertEquals(1, post1Comments.size(), "Post 1 ska ha 1 kommentar");
        assertEquals(2, post2Comments.size(), "Post 2 ska ha 2 kommentarer");
    }
}
