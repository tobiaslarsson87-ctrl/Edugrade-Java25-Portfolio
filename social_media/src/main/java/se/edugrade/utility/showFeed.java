package se.edugrade.utility;

import jakarta.persistence.EntityManagerFactory;
import se.edugrade.dto.LikeDTO;
import se.edugrade.entities.Post;
import se.edugrade.entities.Users;
import se.edugrade.repositories.LikeRepository;
import se.edugrade.repositories.PostRepository;
import se.edugrade.dto.subclasses.ContentType;
import se.edugrade.service.LikeService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;


// CATHYS och DOROTEAS kod
public class showFeed {
    // =========================================================
// 1. CREATE POST
// =========================================================
//Dorotea start
    public static void showCreatePostMenu() {
        System.out.println(" === Create a post📝 ===\n");
        System.out.println("What post do you want to create? ");
        printSchemaForCreate();
    }

    public static void printSchemaForCreate() {
        System.out.println("[1] 🗓️Text");
        System.out.println("[2] 🏙️Image");
        System.out.println("[3] 🎥Video");
        System.out.println("[4] 🧷Link");
        System.out.println("[0] 🔙Back to menu");
    }


    public static void showTextHeader() {
        System.out.println("--- 🗓️TextPost --- \n");
        System.out.println("✏️ What 'text' do you want: ");
    }

    public static void showImageHeader() {
        System.out.println("--- 🏙️ImagePost --- \n");
        System.out.println("✏️ Write the caption:");
    }

    public static void showVideoHeader() {
        System.out.println("--- 🎥VideoPost --- \n");
        System.out.println("🔗Paste in the URL ");
    }

    public static void showLinkHeader() {
        System.out.println("--- 🧷 LinkPost --- \n");
        System.out.println("🔗 Past in the URL: ");
    }


    private static final DateTimeFormatter POST_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static void printPostCard(Post post, long likeCount) {

        String time = post.getCreatedAt() != null
                ? post.getCreatedAt().format(POST_TIME_FORMATTER)
                : "N/A";

        String authorName;
        try {
            authorName = post.getAuthor() != null ? post.getAuthor().getUserName() : "N/A";
        } catch (Exception e) {
            authorName = "N/A";
        }

        System.out.printf("""
🆔 ID: %d
👤 User: %s
📝 Post: %s
❤️ Likes: %d
⏰ Timestamp: %s
────────────────────────
""",
                post.getId(),
                authorName,
                post,
                likeCount,
                time
        );
    }


    // =========================================================
    // GENERELLA FEL
    // =========================================================
    public static void showPostNotFound() {
        System.out.println("❌ Post not found");
    }

    public static void showNotLoggedIn() {
        System.out.println("❌ You must be logged in.");
    }


    //----------------------------------------------------------
    // COMMENT
    //----------------------------------------------------------
    public static void showCommentSelection(EntityManagerFactory emf) {

        PostRepository postRepo = new PostRepository(emf);
        LikeService likeService = new LikeService(new LikeRepository(emf));

        List<Post> feed = postRepo.findAllOrdered();

        if (feed.isEmpty()) {
            showPostNotFound();
            return;
        }

        System.out.println("💬 COMMENT POST");
        System.out.println("────────────────────────");

        feed.stream()
                .limit(5)
                .forEach(post ->
                        printPostCard(post, likeService.likeCountForPost(post.getId()))
                );

        System.out.println("👉 Enter Post ID to comment:");
    }

    public static void showWriteCommentText() {
        System.out.println("✏️ Write your comment:");
    }

    public static void showCommentSuccess(String userName, long postId, String text) {
        System.out.println("✅ " + userName + " commented on post " + postId + "\n\"" + text + "\"");
    }


    //----------------------------------------------------------
// LIKE
//----------------------------------------------------------
    public static void showLikeSelection(EntityManagerFactory emf, Users currentUser) {

        PostRepository postRepo = new PostRepository(emf);
        LikeService likeService = new LikeService(new LikeRepository(emf));

        List<Post> feed = postRepo.findAllOrdered();

        if (feed.isEmpty()) {
            showPostNotFound();
            return;
        }

        System.out.println("❤️ LIKE POST");
        System.out.println("────────────────────────");

        feed.stream()
                .limit(5)
                .forEach(post -> {

                    long likes = likeService.likeCountForPost(post.getId());

                    boolean alreadyLiked =
                            currentUser != null
                                    && likeService.hasUserLikedPost(currentUser.getId(), post.getId());

                    printPostCard(post, likes);


                    if (alreadyLiked) {
                        System.out.println("👍 You have already liked this post!");
                    }

                    System.out.println("────────────────────────");
                });

        System.out.println("👉 Enter Post ID to like:");
    }


    public static void showLikeSuccess(String userName, long postId) {
        System.out.printf("✅ %s liked post %d%n", userName, postId);
    }

    public static void showLikeAlreadyExists(String userName, long postId) {
        System.out.printf("❌ %s has already liked post %d%n", userName, postId);
    }

    @SuppressWarnings("unused")
    public static void showLikeFail() {
        System.out.println("❌ Failed to like post");
    }


    //----------------------------------------------------------
    // UNLIKE
    //----------------------------------------------------------
    public static void showUnlikeSelection(EntityManagerFactory emf) {

        PostRepository postRepo = new PostRepository(emf);
        LikeRepository likeRepo = new LikeRepository(emf);

        List<Post> feed = postRepo.findAllOrdered();

        if (feed.isEmpty()) {
            showPostNotFound();
            return;
        }

        System.out.println("💔 UNLIKE POST");
        System.out.println("────────────────────────");

        feed.stream()
                .limit(5)
                .forEach(post ->
                        printPostCard(post, likeRepo.countLikesForPost(post.getId()))
                );

        System.out.println("👉 Enter Post ID to unlike:");
    }

    public static void showUnlikeSuccess(String userName, long postId) {
        System.out.printf("✅ %s removed like from post %d%n", userName, postId);
    }

    public static void showUnlikeNotFound(String userName, long postId) {
        System.out.printf("❌ %s has not liked post %d%n", userName, postId);
    }

    @SuppressWarnings("unused")
    public static void showUnlikeFail() {
        System.out.println("❌ Failed to unlike post");
    }
    //----------------------------------------------------------
    // SEARCH
    //----------------------------------------------------------
    public static void showSearchHeader() {
        System.out.println("🔎 SEARCH POSTS");
        System.out.println("────────────────────────");
    }

    public static void showSearchPrompt() {
        System.out.println("✏️ Enter keyword to search:");
    }

    public static void showSearchNoResults() {
        System.out.println("❌ No posts found!");
    }

    public static void showSearchResults(List<ContentType> results) {

        final String BLUE = Colors.rgb(100,150,255);

        System.out.println(BLUE + "🔎 SEARCH RESULTS");
        System.out.println("────────────────────────");

        results.stream()
                .limit(10)
                .forEach(dto -> {
                    System.out.println(dto);
                    System.out.println("────────────────────────");
                });
    }


    //----------------------------------------------------------
// FEED
//----------------------------------------------------------
    public static void showFeedHeaderStats() {
        System.out.println("🏙️ FEED");
        System.out.println("────────────────────────");
    }

    public static void printPostsDTO(
            List<ContentType> posts,
            Map<Long, Long> likesPerPost,
            Optional<Long> mostLikedPostId,
            double averageLikes
    ) {

        if (posts == null || posts.isEmpty()) {
            showPostNotFound();
            return;
        }


        System.out.println("📊 Likes per post:");
        likesPerPost.forEach((id, count) ->
                System.out.println("Post " + id + " → Likes: " + count)
        );

        mostLikedPostId.ifPresent(id ->
                System.out.println("🏆 Most liked post ID: " + id)
        );

        System.out.println("📊 Average likes per post: " + averageLikes);
        System.out.println("────────────────────────");

        // --------------------------------------------------
        // FEED (DTO)
        // --------------------------------------------------
        posts.stream()
                .limit(10)
                .forEach(dto -> {
                    System.out.println(dto);
                    System.out.println("────────────────────────");
                });
    }


    //----------------------------------------------------------
    // TRENDING
    //----------------------------------------------------------
    public static void showNoHashtagsFound() {
        System.out.println("❌ No hashtags found!");
    }

    public static void showNoTrendingHashtags() {
        System.out.println("❌ No trending hashtags right now!");
    }

    public static void showTrendingResults(List<String> hashtags) {

        final String BLUE = Colors.rgb(100,150,255);
        final String RESET = Colors.reset();

        System.out.println(BLUE + "🔥 TOP 10 TRENDING HASHTAGS 🔥" + RESET);
        System.out.println("────────────────────────");

        hashtags.forEach(System.out::println);
    }

    //----------------------------------------------------------
    // FEED
    //----------------------------------------------------------
    public static void showFeedHeader() {
        System.out.println("🏙️ FEED");
        System.out.println("────────────────────────");
    }

    public static void printPostsDTO(EntityManagerFactory emf, List<ContentType> posts) {

        if (posts == null || posts.isEmpty()) {
            showPostNotFound();
            return;
        }

        LikeService likeService = new LikeService(new LikeRepository(emf));


        Map<Long, Long> likesPerPost = likeService.likesPerPost();
        System.out.println("📊 Likes per post:");
        likesPerPost.forEach((id, count) ->
                System.out.println("Post " + id + " → Likes: " + count)
        );


        likeService.mostLikedPost().ifPresent(post ->
                System.out.println("🏆 Most liked post: ID " + post.getId())
        );


        System.out.println("🔥 Top liked posts:");
        try {
            likeService.topLikedPosts(5).forEach(post ->
                    System.out.println("Post " + post.getId()
                            + " | Likes: " + likesPerPost.getOrDefault(post.getId(), 0L))
            );
        } catch (Exception e) {
            System.out.println("❌ Could not show top liked posts right now.");
        }


        System.out.println("👤 Users who liked each post:");
        likesPerPost.keySet().forEach(postId -> {
            List<LikeDTO> likes = likeService.findLikesForPostDTO(postId);
            System.out.println("Post " + postId + " liked by users: " +
                    likes.stream().map(LikeDTO::userId).toList());
        });


        System.out.println("📊 Average likes per post: "
                + likeService.averageLikesPerPost());
        System.out.println("────────────────────────");

        // UI: visa posts (DTO only)
        posts.stream()
                .limit(10)
                .forEach(p -> {
                    System.out.println(p);
                    System.out.println("────────────────────────");
                });
    }

}
