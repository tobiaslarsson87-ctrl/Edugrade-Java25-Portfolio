/*

import  static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestPost {
    private EntityManagerFactory emf;
    private PostService postService;


    @BeforeAll
    void init() {
        emf = Persistence.createEntityManagerFactory("TestPU");
        postService = new PostService(emf);
    }
    @Test
    void testGetAllPostsNotNull(){
        List<PostDTO> posts = postService.getAll();
        assertNotNull(posts);

}
    @AfterAll
    void close() {
        if (emf != null) emf.close();
    }
    @Test
    void testGetHighestPriorityPosts() {
        List<PostDTO> posts = postService.getHighPriorityPosts();
        assertNotNull(posts);
    }

    //Hämtar poster sorterade efter nyast först
    @Test
    void testGetHighestPriorityPostsByDate() {
        List<PostDTO> posts = postService.getPostsSortedByDate();
        assertNotNull(posts);
    }

    @Test
    void testCreatePost() {
        Post post = new Post() {
            @Override
            public String getContentType() {
                return "text";
            }
        };

        PostDTO saved = postService.create(post);

        assertNotNull(saved);
        assertEquals("Testing", saved.getPost());
    }
// hämtar post via Id
    @Test
    void testGetPostById() {
        Post post = new Post() {
            @Override
            public String getContentType() {
                return "text";
            }
        };

        PostDTO saved = postService.create(post);
        PostDTO found = postService.getClass(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }
}

 */


