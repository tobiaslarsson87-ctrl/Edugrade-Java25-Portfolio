package se.edugrade.service;

import jakarta.persistence.EntityManagerFactory;
import se.edugrade.entities.*;
import se.edugrade.exceptions.InvalidContentException;
import se.edugrade.repositories.CommentRepository;
import se.edugrade.repositories.HashtagRepository;
import se.edugrade.repositories.LikeRepository;
import se.edugrade.repositories.PostRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * AnalyticsService innehåller olika metoder för att analysera olika inlägg,
 * t.ex. trending posts, engagement och hashtag-statistik.
 */
public class AnalyticsService {
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private HashtagRepository hashtagRepository;
    private LikeRepository likeRepository;

    public AnalyticsService() {}

    public AnalyticsService(EntityManagerFactory emf) {
        this.postRepository = new PostRepository(emf);
        this.commentRepository = new CommentRepository(emf);
        this.hashtagRepository = new HashtagRepository(emf);
        this.likeRepository = new LikeRepository(emf);
    }

    /**
     * Hämtar alla posts med laddade relationer för att undvika LazyInitializationException
     */
    public List<Post> loadAllPosts() {
        return postRepository.findAllWithRelations();
    }
    /**
     * Returnerar pupuläraste inläggen baserat på likes och kommentarer
     * @param posts listan av inlägg som ska analyseras
     * @param limit hur många toppresultat som ska returnas
     * @return en lista med dom mest populära inläggen
     */
    public List<Post> getTopTrendingPosts(List<Post> posts, int limit) {
        Comparator<Post> comparator = Comparator
                .comparingInt((Post p) -> p.getLikes().size() + p.getComments().size())
                .reversed()
                // om två inlägg har samma antal likes&kommentarer sorterar på datum,
                .thenComparing(Post::getCreatedAt);

        PriorityQueue<Post> queue = new PriorityQueue<>(comparator);
        // lägg in alla posts i "kön"
        queue.addAll(posts);
        // lista som innehåller topp trending
        List<Post> result = new ArrayList<>();
        // plockar ut topp-listan en i taget
        for (int i = 0; i < limit && !queue.isEmpty(); i++) {
            result.add(queue.poll());} // poll tar ut de "bästa" enligt comparatorn
        return result;
    }
    /**
     * Beräknar avarage engagement per post(likes & comments)
     * @param posts listan av inlägg
     * @return snittvärdet som double
     */
    public double getAverageEngagement(List<Post> posts) {
        return posts.stream().mapToInt(p -> p.getLikes().size() +
                p.getComments().size()).average().orElse(0);
    }
    /**
     * Det inlägg som har flest likes
     * @param posts listan av inlägg
     * @return optional med mest like:Ade inlägget
     */
    public Optional<Post> getMostLikedPost(List<Post> posts) {
        return posts.stream().max(Comparator.comparingInt(p -> p.getLikes().size()));
    }
    /**
     * Räknas hur många gånger varje hashtag förekommer i listan av posts
     * @param posts listan av posts
     * @return en MAp där key är hashtag och värdet är hur många det finns
     */
    public Map<String, Integer> getHashtagEngagement(List<Post> posts) {
        // Streama alla posts
        Stream<Post> postStream = posts.stream();
        // tar ut alla hashtags från varje post
        Stream<Hashtag> hashtagStream = postStream
                .flatMap(p -> p.getHashtags().stream());
        // Gruppering tag -> antal förekomster
        Map<String, Integer> result = hashtagStream.collect(
                Collectors.groupingBy(
                        Hashtag::getTag,
                        Collectors.summingInt(h -> 1)));
        return result;}

    /**
     * Räknar kommentarer per användare baserat på post-id
     * @param postId
     * @return en map med användare & antal kommentarer på en post
     */
    public Map<String, Long> countCommentsByUserFromPostId(long postId) {
        Post post = postRepository.findByIdWithRelations(postId).get();
        Optional.ofNullable(post)
                .orElseThrow(() -> new InvalidContentException("Post cannot be null"));

        return post.getComments().stream()
                .collect(Collectors.groupingBy(
                        c -> c.getAuthor().getUserName(),
                        Collectors.counting()
                ));
    }

    /**
     * Räknar ut avarage kommentarlängd per post
     * @param postId
     * @return en double med medellängden på kommentarerna
     */
    public double averageCommentLengthByPostId(long postId) {
        Post post = postRepository.findByIdWithRelations(postId).get();
        Optional.ofNullable(post)
                .orElseThrow(() -> new InvalidContentException("Post cannot be null"));

        return post.getComments().stream()
                .mapToInt(c -> c.getText().length())
                .average()
                .orElse(0.0);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    // Hämtar en hashtag om den finns annars skapar den en ny.
    private Hashtag getOrCreateHashtag(String tag) {
        return hashtagRepository.findAll().stream()
                .filter(h -> h.getTag().equals(tag))
                .findFirst()
                .orElseGet(() -> {
                    Hashtag h = new Hashtag(tag);
                    h = hashtagRepository.create(h);
                    return h;
                });
    }
    // Hämtar en kommentar om den finns annars skapas en ny
    private Comment getOrCreateComment(Post post, String text) {
        return commentRepository.findAll().stream()
                .filter(c -> c.getPost().getId().equals(post.getId()) && c.getText().equals(text))
                .findFirst()
                .orElseGet(() -> {
                    Comment c = new Comment(post, post.getAuthor(), text);
                    c = commentRepository.create(c);
                    return c;
                });
    }
    // Lägger till en hashtag på ett inlägg om den inte redan finns.
    private void addHashtagToPost(Post post, Hashtag hashtag) {
        if (!post.getHashtags().contains(hashtag)) {
            post.getHashtags().add(hashtag);
            hashtag.getPost().add(post);
        }
    }
    // Lägger till en like från en användare om den inte redan finns.
    private void addLikeIfMissing(Post post, Users user) {
        boolean exists = post.getLikes().stream()
                .anyMatch(l -> l.getUser().getId().equals(user.getId()));
        if (!exists) {
            post.getLikes().add(new Like(post, user));
        }
    }
    /**
     * Skapar testdata för analysfunktionerna.
     * Försöker hitta posten i db(på content)
     * Om den finns -> återanvänd
     * Om den inte finns -> skapa den
     * Görs att den kan köra flera gånger utan att krasha.
     */
    public void seedforAnalytics(Users currentUser) {

        // Skapar eller hämtar hashtag
        Hashtag h1 = getOrCreateHashtag("QA");
        Hashtag h2 = getOrCreateHashtag("coding");
        Hashtag h3 = getOrCreateHashtag("fun");

        List<Post> posts = postRepository.findAllWithRelations();
        // Skapar eller hämtar test-post
        Post p1 = posts.stream()
                .filter(p -> p.getContent().equals("Learning Java is fun!"))
                .findFirst()
                .orElseGet(() -> {
                    Post p = new TextPost(currentUser, "Learning Java is fun!");
                    addHashtagToPost(p, h1);
                    postRepository.create(p);
                    return p;
                });

        Post p2 = posts.stream()
                .filter(p -> p.getContent().equals("Coding late at night..."))
                .findFirst()
                .orElseGet(() -> {
                    Post p = new TextPost(currentUser, "Coding late at night...");
                    addHashtagToPost(p, h2);
                    postRepository.create(p);
                    return p;
                });

        Post p3 = posts.stream()
                .filter(p -> p.getContent().equals("Analytics testing post"))
                .findFirst()
                .orElseGet(() -> {
                    Post p = new TextPost(currentUser, "Analytics testing post");
                    addHashtagToPost(p, h1);
                    addHashtagToPost(p, h3);
                    postRepository.create(p);
                    return p;
                });
        // adderar like om det saknas så att alla har minst en like
        addLikeIfMissing(p1, currentUser);
        addLikeIfMissing(p2, currentUser);
        addLikeIfMissing(p3, currentUser);
        // uppdaterar post i DB
        postRepository.update(p1);
        postRepository.update(p2);
        postRepository.update(p3);
        // Skapar eller hämtar kommentarer
        getOrCreateComment(p1, "Nice!");
        getOrCreateComment(p2, "Wow!");
        getOrCreateComment(p3, "Test for analytics!");
    }

    // ANVÄNDS BARA I TEST JUST NU
    /**
     * returnerar det mest populära inlägget
     * @param posts listan a v inlägg
     * @return optional med populäraste inlägget
     */
    public Optional<Post> peekTrending(List<Post> posts) {

        return getTopTrendingPosts(posts, 1).stream().findFirst();
    }
    /**
     * Tar bort dubbelt och sorterar inlägg efter datum(nyast först)
     * @param posts en lista av posts
     * @return en sorterad lista utan dubbletter
     */
    public List<Post> getDistinctRecentPosts(List<Post> posts) {
        return posts.stream()
                .distinct()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .limit(20)
                .toList(); }

}




