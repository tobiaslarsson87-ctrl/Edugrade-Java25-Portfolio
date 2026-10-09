package se.edugrade.utility;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.entities.*;
import se.edugrade.repositories.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

//CATHYS och DOROTEAS kod copy/paste från annan branch

public class DataSeeder {
    private final EntityManagerFactory emf;
    public static final Random RANDOM = new Random();

    // Minneslistor för att återanvända seedad data
    private static final List<Users> USERS = new ArrayList<>();
    private static final List<Post> POSTS = new ArrayList<>();
    private static final List<Hashtag> HASHTAGS = new ArrayList<>();

    public DataSeeder(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Startar hela autogenereringen av testdata
    public void seedAll() {

        UsersRepository usersRepo = new UsersRepository(emf);
        PostRepository postRepo = new PostRepository(emf);
        LikeRepository likeRepo = new LikeRepository(emf);
        CommentRepository commentRepo = new CommentRepository(emf);
        HashtagRepository hashtagRepo = new HashtagRepository(emf);

        System.out.println("DATABASE SEED:");

        seedUsers(usersRepo);          // Skapar slumpade användare
        seedHashtags(hashtagRepo);     // Skapar hashtags
        seedPosts(postRepo);           // Skapar poster (text/bild/länk)
        seedComments(commentRepo);     // Lägger till kommentarer
        seedLikes(likeRepo);           // automat-genererade likes

        System.out.println("DATABASE DONE\n");
    }

    // Skapar slumpade användare
    private void seedUsers(UsersRepository usersRepo) {

        String[] names = {
                "emma", "marcus", "luna", "alexander", "sofia",
                "oliver", "nora", "isak", "elin", "felix",
                "milo", "wilma", "leo", "agnes", "noah"
        };

        String[] bios = {
                "Coffee addict ☕", "Java enthusiast 💻", "Digital artist 🎨",
                "Gym + gaming 💪🎮", "Playlist creator 🎧", "Food explorer 🍕",
                "Adventure seeker ✈️", "Dog person 🐶", "Book nerd 📚"
        };

        for (int i = 0; i < 8; i++) {

            String username = names[RANDOM.nextInt(names.length)] + RANDOM.nextInt(500);

            Users u = new Users(
                    username,
                    bios[RANDOM.nextInt(bios.length)],
                    LocalDateTime.now().minusDays(RANDOM.nextInt(365))
            );

            usersRepo.create(u);
            USERS.add(u);
        }

        System.out.println("✔ Created random users");
    }

    // Skapar slumpade poster för varje användare
    private void seedPosts(PostRepository postRepo) {

        for (Users u : USERS) {
            int postCount = 1 + RANDOM.nextInt(3);
            for (int i = 0; i < postCount; i++) {
                Post p;
                int type = RANDOM.nextInt(3);
                Supplier<String> randomText =
                        () -> "Random thought " + RANDOM.nextInt(1000);

                switch (type) {
                    // Skapar en TEXT-post kopplad till användaren
                    case 0 -> p = new TextPost(
                            u,
                            randomText.get()
                    );
                    // Skapar en BILDer-post med slumpad bild och caption
                    case 1 -> p = new ImagePost(
                            u,
                            "https://picsum.photos/200?img=" + RANDOM.nextInt(100),
                            "A cool image #" + RANDOM.nextInt(99)
                    );

                    // Skapar en LÄNK-post med slumpad URL (default = när värdet inte är 0 eller 1)
                    default -> p = new LinkPost(
                            u,
                            "https://example.com/" + RANDOM.nextInt(500),
                            "Check this out!"
                    );
                }

                postRepo.create(p);          // Spara posten i databasen
                POSTS.add(p);                // Lägg även in posten i vår lokala lista (så seed kan använda den senare)

                if (!HASHTAGS.isEmpty()) {   // Om det finns hashtags att använda
                    int tagCount = 1 + RANDOM.nextInt(4);   // Slumpa hur många hashtags posten ska få (1–4)

                    for (int h = 0; h < tagCount; h++) {    // Loopar så många gånger som slumpats fram
                        Hashtag ht = HASHTAGS.get(RANDOM.nextInt(HASHTAGS.size()));  // Välj en random hashtag
                        ht.getPost().add(p);   // Koppla posten till hashtag (relation Many-To-Many)
                    }
                }

            }
        }

        System.out.println("✔ Created posts");
    }

    // Skapar slumpade kommentarer till poster
    private void seedComments(CommentRepository commentRepo) {

        String[] comments = {
                "This is awesome! 🔥",
                "Love this!",
                "Nice post!",
                "I totally agree 👌",
                "Wow 😍",
                "This made my day!",
                "Interesting take 🤔",
                "Bookmarking this 👍",
                "So cool!",
                "Respect 💯"
        };

        for (Post post : POSTS) {                     // Gå igenom alla skapade poster
            int commentCount = RANDOM.nextInt(5);     // Slumpa hur många kommentarer posten får (0–4)
            for (int i = 0; i < commentCount; i++) {  // Skapa så många kommentarer som slumpats fram
                Users commenter = USERS.get(RANDOM.nextInt(USERS.size()));   // Välj en random användare
                Comment comment = new Comment(
                        post,                                            // Koppla kommentaren till posten
                        commenter,                                       // Koppla kommentaren till användaren
                        comments[RANDOM.nextInt(comments.length)]        // Slumpa text från listan
                );
                commentRepo.create(comment);          // Spara kommentaren i databasen
            }
        }

        System.out.println("✔ Created comments");     // Logga att vi är klara
    }

    // Skapar slumpade likes till poster
    private void seedLikes(LikeRepository repo) {
        for (Post post : POSTS) {                        // Gå igenom alla poster
            int count = RANDOM.nextInt(6);               // Slumpa hur många likes posten får (0–5)
            Set<Users> likeUsers = new HashSet<>();      // Ser till att samma user inte kan likea två gånger
            for (int i = 0; i < count; i++) {            // Kör så många gånger som vi ska skapa likes
                Users user = USERS.get(RANDOM.nextInt(USERS.size()));   // Hämta random user
                if (!likeUsers.add(user)) continue;      // Hoppa över om användaren redan likeat posten
                Like like = new Like(post, user);        // Skapa like-objekt (kopplar post + user)
                repo.create(like);                       // Spara i databasen
            }
        }

        System.out.println("✔ Created likes");           // Logg ✔
    }

    // Skapar hashtags
    private void seedHashtags(HashtagRepository repo) {

        String[] tags = {
                "#java", "#backend", "#codinglife", "#fitness", "#music",
                "#gaming", "#travel", "#doglover", "#coffee", "#devlife"
        };

        for (String t : tags) {
            Hashtag h = new Hashtag(t);
            repo.create(h);
            HASHTAGS.add(h);
        }

        System.out.println("✔ Created hashtags");
    }
}
