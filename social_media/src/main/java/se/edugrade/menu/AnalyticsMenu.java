package se.edugrade.menu;

import jakarta.persistence.EntityManagerFactory;
import se.edugrade.entities.*;
import se.edugrade.service.AnalyticsService;
import se.edugrade.utility.PrintHelper;
import se.edugrade.utility.UserInput;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Menyklass som visar ollika analysfunktioner för plattformen.
 */
public class AnalyticsMenu {

    public static void run(EntityManagerFactory emf, Users currentUser) {
        boolean loop = true;
        while (loop) {
            System.out.println("────── 📊 ANALYTICS MENU ──────");
            System.out.println("1. 📈 Platform Engagement");
            System.out.println("2. 🔥 Most Liked Post");
            System.out.println("3. 🏷️ Hashtag Engagement");
            System.out.println("4. 🚀 Trending Posts");
            System.out.println("5. 💬 Comments per user");
            System.out.println("6. 📝 Avarage comment length per post");
            System.out.println("7. 🧪 Seed Analytics Test Data");
            System.out.println("0. 🔙 Back to Main Menu");
            System.out.println("───────────────────────────────");


            switch (UserInput.getInt()) {
                case 1 -> showEngagementRate(emf);
                case 2 -> showMostLikedPost(emf);
                case 3 -> showHashtagEngagement(emf);
                case 4 -> showTrendingPosts(emf);
                case 5 -> showCommentStats(emf, currentUser);
                case 6 -> showAverageCommentLength(emf, currentUser);
                case 7 -> seedAnalyticsData(emf, currentUser);
                case 0 -> loop = false;
                default -> System.out.println("Invalid choice, please choose 0-7.");
            }
        }
    }

    // Visar genomsnittlig "engagement" av likes, kommentarer + hashtags per post.
    private static void showEngagementRate(EntityManagerFactory emf) {
        AnalyticsService analyticsService = new AnalyticsService(emf);
        List<Post> posts = analyticsService.loadAllPosts();
        if (posts.isEmpty()) {
            System.out.println("There are no posts on the platform yet.");
            return;
        }
        double avg = analyticsService.getAverageEngagement(posts);
        System.out.println("📊 Platform Engagement");
        PrintHelper.divider();
        System.out.println("Average interactions per post: " + avg);
        System.out.println("Includes likes, comments, and hashtag usage. \n");
    }

    // Visar post med flest likes
    private static void showMostLikedPost(EntityManagerFactory emf) {
        AnalyticsService analyticsService = new AnalyticsService(emf);
        List<Post> posts = analyticsService.loadAllPosts();
        if (posts.isEmpty()) {
            System.out.println("There are no posts on the platform yet.");
            return;
        }
        Optional<Post> mostLiked = analyticsService.getMostLikedPost(posts);

        mostLiked.ifPresentOrElse(
                p -> {
                    System.out.println("🔥 Most Liked Post");
                    PrintHelper.divider();
                    System.out.println("Content: " + p.getContent());
                    System.out.println("Likes: " + p.getLikes().size() + "\n");},
                () -> System.out.println("No likes found on the platform."));
    }

    // Visar hur ofta varje hashtag används
    private static void showHashtagEngagement(EntityManagerFactory emf) {
        AnalyticsService analyticsService = new AnalyticsService(emf);
        List<Post> posts = analyticsService.loadAllPosts();
        if (posts.isEmpty()) {
            System.out.println("There are no posts on the platform yet.");
            return;
        }
        Map<String, Integer> map = analyticsService.getHashtagEngagement(posts);

        if (map.isEmpty()) {
            System.out.println("No hashtags found on the platform.");
            return;
        }
        System.out.println("🏷️ Hashtag Engagement");
        PrintHelper.divider();
        map.forEach((tag, count) ->
                System.out.println("#" + tag + ": " + count + " uses \n"));
    }

    // Visar de mest trendande inläggen baserat på likes och kommentarer
    private static void showTrendingPosts(EntityManagerFactory emf) {
        AnalyticsService analyticsService = new AnalyticsService(emf);
        List<Post> posts = analyticsService.loadAllPosts();
        if (posts.isEmpty()) {
            System.out.println("There are no posts on the platform yet.");
            return;
        }
        List<Post> trending = analyticsService.getTopTrendingPosts(posts, 5);

        System.out.println("🚀 Trending Posts");
        PrintHelper.divider();
        trending.forEach(p ->
                System.out.println("- " + p.getContentType() +
                        " | Likes: " + p.getLikes().size() +
                        " | Comments: " + p.getComments().size() +
                        " | Content: " + p.getContent() + "\n"));
    }

    // Visar stats: antal comments per användare
    private static void showCommentStats(EntityManagerFactory emf, Users currentUser) {
        AnalyticsService analyticsService = new AnalyticsService(emf);
        showAvailablePosts(analyticsService, currentUser);
        long postId = askForValidPostId(analyticsService);
        Map<String, Long> stats = analyticsService.countCommentsByUserFromPostId(postId);
        System.out.println("\n💬 Comments per user");
        PrintHelper.divider();
        stats.forEach((user, count) ->
                System.out.println("👤 " + user + " → " + count + " comments\n"));
    }

    // Visar avarega längd på kommentarer för en post
    private static void showAverageCommentLength(EntityManagerFactory emf, Users currentUser) {
        AnalyticsService analyticsService = new AnalyticsService(emf);
        showAvailablePosts(analyticsService,currentUser);
        long postId = askForValidPostId(analyticsService);
        double avg = analyticsService.averageCommentLengthByPostId(postId);
        System.out.println("\n📝 Average Comment Length");
        PrintHelper.divider();
        System.out.println("✏️  Average characters per comment: " + avg + "\n");
    }

    //Visar alla posts för användaren
    private static void showAvailablePosts(AnalyticsService analyticsService, Users currentUser) {
        System.out.println("Available posts:");
        analyticsService.loadAllPosts().forEach(p -> PrintHelper.printPost(p, currentUser));
    }
    /**
     * Fråga user om giltigt post-ID och
     * loopar tills giltigt hittas
     */
    private static long askForValidPostId(AnalyticsService analyticsService) {
        System.out.println("Enter post ID:");
        long id = UserInput.getLong();

        while (analyticsService.findById(id).isEmpty()) {
            System.out.println("Post not found, try again:");
            id = UserInput.getLong();
        }
        return id;
    }
    private static void seedAnalyticsData(EntityManagerFactory emf, Users currentUser) {
        AnalyticsService analyticsService = new AnalyticsService(emf);
        System.out.println("🧪 Seeding database with analytics test data...\n");
        analyticsService.seedforAnalytics(currentUser);
        System.out.println("Database seeded successfully!\n");
    }
}