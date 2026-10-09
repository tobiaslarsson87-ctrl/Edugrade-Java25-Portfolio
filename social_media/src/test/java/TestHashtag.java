import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.edugrade.dto.HashtagDTO;
import se.edugrade.entities.Hashtag;
import se.edugrade.repositories.HashtagRepository;
import se.edugrade.service.HashtagService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;


public class TestHashtag {

    private EntityManagerFactory emf;
    private HashtagRepository repo;
    private HashtagService service;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("TestPU");
        repo = new HashtagRepository(emf);
        service = new HashtagService();
    }

    @AfterEach
    void tearDown() {
        if (repo != null)
            repo.closeEmf();
        if (emf != null && emf.isOpen())
            emf.close();
    }

    @Test
    @DisplayName("CreateHashtag should save 'Hashtag' in databas and generate an ID")
    void testCreateHashtag() {
        //Given
        Hashtag hashtag = new Hashtag("#java");

        //When
        repo.create(hashtag);

        //Then
        assertNotNull(hashtag.getId(), "ID should generate auto");
        assertEquals(1, repo.findAll().size(), "It should be 1 'Hashtag' in database ");
    }

    @Test
    @DisplayName("getHashtagById will return a hashtag if it exists")
    void testGetHashtagById(){
        //Given
        Hashtag hashtag = new Hashtag("#id");
        repo.create(hashtag);
        Long id = hashtag.getId();

        //When
        Optional<Hashtag> getId = repo.findById(id);

        //Then
        assertNotNull(getId, "Hashtag found");
        assertEquals("#id", getId.get().getTag());
    }

    @Test
    @DisplayName("getHashtagById will return !!! if hashtag not exist")
    void testGetHashtagByIdNotFound(){
        //Given
        Long noExistId = 4L;

        //When
        Optional<Hashtag> id = repo.findById(noExistId);

        //Then
        assertTrue(id.isEmpty(), "Should return empty Optional if hashtag not exist");
    }

    @Test
    @DisplayName("getAllHashtags will return empty if databas is empty")
    void testGetAllHashtagsEmptyDatabase(){
        // Given: Tom database (från @BeforeEach)

        //When
        List<Hashtag> hashtags = repo.findAll();

        //Then
        assertNotNull(hashtags, "Should never return 'NULL'");
        assertTrue(hashtags.isEmpty(), "List should be empty");
    }

    @Test
    @DisplayName("getAllHashtags will return all Hashtags")
    void testGetAllHashtags(){

        //Given
        repo.create(new Hashtag("#Hashtag1"));
        repo.create(new Hashtag("#Hashtag2"));
        repo.create(new Hashtag("#Hashtag3"));

        //When
        List<Hashtag> hashtags = repo.findAll();

        //Then
        assertEquals(3, hashtags.size(), "Should return all 3 hashtags");

    }

    @Test
    @DisplayName("updateHashtag will update existing hashtag")
    void testUpdateHashtag(){

        //Given
        Hashtag hashtag = new Hashtag("#old");
        repo.create(hashtag);

        //When
        hashtag.setTag("#updated");
        repo.update(hashtag);

        //Then
        Optional<Hashtag> updated = repo.findById(hashtag.getId());
        assertEquals("#updated", updated.get().getTag());
    }

    @Test
    @DisplayName("deletedHashtag will delete Hashtag from database")
    void testDeleteHashtag(){

        //Given
        Hashtag hashtag = new Hashtag("#deleted");
        repo.create(hashtag);
        Long id = hashtag.getId();
        assertEquals(1, repo.findAll().size());

        //When
        repo.delete(id);

        assertEquals(0, repo.findAll().size(), "Hashtag will be deleted");
        assertTrue(repo.findById(id).isEmpty(), "Hashtag will not exist");
    }

    @Test
    @DisplayName("deletedHashtag will not throw exception if not found")
    void testDeletedHashtagNotFound(){

        //Given
        Long noExId = 4L;

        // When & Then
        assertDoesNotThrow(() -> repo.delete(noExId),
                "Delete should not throw exception if Hashtag not found");
    }

    @ParameterizedTest
    @MethodSource("hashtagProvider")
    void testTopWeeklyGrowthHashtag(List<Hashtag> hashtags, int limit, List<String> expected){
        // Given & When
        List<HashtagDTO> result = service.topWeeklyGrowthHashtag(hashtags, limit);
        // Then
        List<String> tag = result.stream().map(HashtagDTO::tag).toList();

        assertEquals(expected,tag);
    }
    static Stream<org.junit.jupiter.params.provider.Arguments> hashtagProvider(){
        Hashtag lowest =  new Hashtag();
        lowest.setId(4L);
        lowest.setTag("#lowest");
        lowest.setWeeklyGrowth(10);

        Hashtag medel =  new Hashtag();
        medel.setId(5L);
        medel.setTag("#medel");
        medel.setWeeklyGrowth(30);

        Hashtag highest =  new Hashtag();
        highest.setId(6L);
        highest.setTag("#highest");
        highest.setWeeklyGrowth(20);

        return Stream.of(Arguments.of(
                List.of(lowest, medel, highest),
                2, List.of("#medel", "#highest")
                ),
                Arguments.of(
                        List.of(highest, lowest, medel , highest),
                        3, List.of("#medel", "#highest", "#lowest")
                ),
                Arguments.of(
                        List.of(medel),
                        1, List.of("#medel")
                ),
                Arguments.of(
                        List.of(),
                        5,
                        List.of()
                ));
    }
    @Test
    void testTrendingHashtag(){
        //Given
        List<String> hashtags = Arrays.asList(
                "#java", "#java","#java", "#code", "#code", "#lol"
        );

        long minCount = 2;

        //When
        List<String> result = service.trendingHashtags(hashtags, minCount);

        //Then
        assertEquals(2, result.size());
        assertTrue(result.contains("#java"));
        assertTrue(result.contains("#code"));
        assertFalse(result.contains("#lol"));
    }
    }

