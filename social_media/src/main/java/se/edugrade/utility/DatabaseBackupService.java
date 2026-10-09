package se.edugrade.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import se.edugrade.backupDTO.HashtagBackupDTO;
import se.edugrade.backupDTO.PostBackupDTO;
import se.edugrade.backupDTO.UsersBackupDTO;
import se.edugrade.entities.Post;
import se.edugrade.entities.Users;
import se.edugrade.entities.Hashtag;
import se.edugrade.exceptions.SocialMediaException;
import se.edugrade.repositories.UsersRepository;
import se.edugrade.repositories.PostRepository;
import se.edugrade.repositories.HashtagRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Service-klass som ansvarar för att skapa en fullständig backup av databasen.
 * Backupen sparas som tre separata JSON-filer: users.json, posts.json och hashtags.json.
 */
public class DatabaseBackupService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final UsersRepository usersRepository;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty-print
    }
    /**
     * Skapar en ny instans av DatabaseBackupService.
     *  @param usersRepo repository för användare
     *  @param postRepo repository för poster
     *  @param hashtagRepo repository för hashtags
     *  */
    public DatabaseBackupService(UsersRepository usersRepo, PostRepository postRepo, HashtagRepository hashtagRepo) {
        this.usersRepository = usersRepo;
        this.postRepository = postRepo;
        this.hashtagRepository = hashtagRepo;
    }

    /**
     * Backup på hela databasen till 3 separata JSON-filer
     * @param backupFolder - Path till mappen där backupen sparas
     * @throws SocialMediaException om backup misslyckas
     */
    public void backupDatabase(Path backupFolder) {
        try {
            // Skapar backup-mappen om den inte finns
            Files.createDirectories(backupFolder);
            System.out.println("Backup folder created: " + backupFolder);

            // Hämtar hem all data från db via repos
            List<Users> allUsers = usersRepository.findAll();
            List<Post> allPosts = postRepository.findAllWithRelations();
            List<Hashtag> allHashtags = hashtagRepository.findAllWithPosts();
            System.out.println("Fetched: " + allUsers.size() + " users, " +
                    allPosts.size() + " posts, " + allHashtags.size() + " hashtags");

            // Konverterar enteties till backup-DTO
            var userDtos = allUsers.stream()
                    .map(UsersBackupDTO::fromEntity)
                    .toList();

            var postDtos = allPosts.stream()
                    .map(PostBackupDTO::fromEntity)
                    .toList();

            var hashtagDtos = allHashtags.stream()
                    .map(HashtagBackupDTO::fromEntity)
                    .toList();

            String usersJson = objectMapper.writeValueAsString(userDtos);
            String postsJson = objectMapper.writeValueAsString(postDtos);
            String hashtagsJson = objectMapper.writeValueAsString(hashtagDtos);

            // Skriver till 3 separata filer
            Files.writeString(
                    backupFolder.resolve("users.json"),
                    usersJson,
                    StandardOpenOption.CREATE, // Skapar om den inte finns
                    StandardOpenOption.TRUNCATE_EXISTING // Skriver över om den finns
            );

            Files.writeString(
                    backupFolder.resolve("posts.json"),
                    postsJson,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            Files.writeString(
                    backupFolder.resolve("hashtags.json"),
                    hashtagsJson,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            System.out.println("✅ Database backup was successful!: ");
            System.out.println("   - " + backupFolder.resolve("users.json"));
            System.out.println("   - " + backupFolder.resolve("posts.json"));
            System.out.println("   - " + backupFolder.resolve("hashtags.json"));

        } catch (IOException e) {
            throw new SocialMediaException("Failed to backup database" , e);
        }
    }
}
