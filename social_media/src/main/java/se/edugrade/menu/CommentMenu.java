package se.edugrade.menu;

import jakarta.persistence.EntityManagerFactory;
import se.edugrade.dto.CommentDTO;
import se.edugrade.entities.Comment;
import se.edugrade.entities.Post;
import se.edugrade.entities.Users;
import se.edugrade.service.CommentService;
import se.edugrade.utility.PrintHelper;
import se.edugrade.utility.UserInput;

import java.util.List;

public class CommentMenu {

    public static void run(EntityManagerFactory emf, Users currentUser) {

        CommentService commentService = new CommentService(emf);

        boolean loop = true;
        while (loop) {
            System.out.println("────── 💬 COMMENT MENU ──────");
            System.out.println("1. 📚 Show all comments");
            System.out.println("2. 📑 Show sorted comments on a post");
            System.out.println("3. 🔍 Search comments on a post");
            System.out.println("4. ✏️  Update a comment");
            System.out.println("5. 🗑️  Delete a comment");
            System.out.println("0. 🔙 Back");
            System.out.println("──────────────────────────────");

            switch (UserInput.getInt()) {
                case 1 -> showAllComments(commentService);
                case 2 -> showSortedCommentsOnPost(commentService, currentUser);
                case 3 -> searchCommentsOnPost(commentService, currentUser);
                case 4 -> updateComment(commentService, currentUser);
                case 5 -> deleteComment(commentService, currentUser);
                case 0 -> loop = false;
                default -> System.out.println("⚠️ Invalid choice, please choose 0-5.");
            }
        }
    }
    // Visar alla kommentarer
    private static void showAllComments(CommentService service) {
        List<Comment> all = service.getAllComments();
        if (all.isEmpty()) {
            System.out.println("\n⚠️ No comments found.\n");
            return;
        }
        System.out.println("\n📚 ALL COMMENTS");
        PrintHelper.divider();
        all.forEach(PrintHelper::printComment);
    }

    // Visar sorterade kommentarer
    private static void showSortedCommentsOnPost(CommentService service, Users currentUser) {
        showAvailablePosts(service, currentUser);
        long postId = askForValidPostId(service);
        Post post = service.findPostByIdWithRelations(postId).get();

        if (post.getComments().isEmpty()) {
            System.out.println("\n⚠️ No comments on this post.\n");
            return;
        }
        System.out.println("\n📑 SORTED COMMENTS");
        PrintHelper.divider();
        service.getSortedComments(post)
                .forEach(PrintHelper::printCommentDTO);
    }
   // Sök kommentarer per post
    private static void searchCommentsOnPost(CommentService service, Users currentUser) {
        showAvailablePosts(service, currentUser);

        long postId = askForValidPostId(service);
        Post post = service.findPostByIdWithRelations(postId).get();

        if (post.getComments().isEmpty()) {
            System.out.println("\n⚠️ No comments on this post.\n");
            return;
        }
        System.out.println("\n🔍 Enter keyword:");
        String keyword = UserInput.getString();
        List<CommentDTO> results = service.searchCommentsByKeyword(post, keyword);

        if (results.isEmpty()) {
            System.out.println("\n⚠️ No comments matched your search.\n");
            return;
        }
        System.out.println("\n📚 SEARCH RESULTS");
        PrintHelper.divider();
        results.forEach(PrintHelper::printCommentDTO);
    }
    // Uppdaterar kommentar
    private static void updateComment(CommentService service, Users currentUser) {
        showAvailablePosts(service, currentUser);

        long postId = askForValidPostId(service);
        Post post = service.findPostByIdWithRelations(postId).get();

        if (post.getComments().isEmpty()) {
            System.out.println("\n⚠️ No comments on this post.\n");
            return;
        }
        System.out.println("\n💬 Comments on this post:");
        post.getComments().forEach(PrintHelper::printComment);
        long commentId = askForValidCommentId(service);
        service.findById(commentId).ifPresentOrElse(comment -> {
            System.out.println("\n✏️ Updating comment:");
            PrintHelper.printComment(comment);

            System.out.println("Enter new text:");
            String newText = UserInput.getString();
            service.updateComment(comment, newText);
            Post updatedPost = service.findPostByIdWithRelations(postId).get();
            System.out.println("\n✅ Comment updated!");
            System.out.println("Updated comments:");
            updatedPost.getComments().forEach(PrintHelper::printComment);

        }, () -> System.out.println("\n⚠️ Comment not found."));
    }
// Tar bort kommentar
    private static void deleteComment(CommentService service, Users currentUser) {
        showAvailablePosts(service, currentUser);

        long postId = askForValidPostId(service);
        Post post = service.findPostByIdWithRelations(postId).get();

        if (post.getComments().isEmpty()) {
            System.out.println("\n⚠️ No comments on this post.\n");
            return;
        }
        System.out.println("\n💬 Comments on this post:");
        post.getComments().forEach(PrintHelper::printComment);
        long commentId = askForValidCommentId(service);
        service.findById(commentId).ifPresentOrElse(comment -> {
            System.out.println("\n🗑️ Deleting comment:");
            PrintHelper.printComment(comment);

            post.getComments().remove(comment);
            service.update(post);
            service.deleteComment(commentId);
            Post updatedPost = service.findPostByIdWithRelations(postId).get();

            System.out.println("\n✅ Comment deleted!");
            System.out.println("Updated comments:");
            updatedPost.getComments().forEach(PrintHelper::printComment);

        }, () -> System.out.println("\n⚠️ Comment not found."));
    }

    private static void showAvailablePosts(CommentService service, Users currentUser) {
        System.out.println("\n📌 AVAILABLE POSTS");
        PrintHelper.divider();

        service.findAllWithRelations()
                .forEach(p -> PrintHelper.printPost(p, currentUser));
    }
// Hjälptmetoder
    private static long askForValidPostId(CommentService service) {
        System.out.println("Enter post ID:");
        long id = UserInput.getLong();

        while (service.findPostByIdWithRelations(id).isEmpty()) {
            System.out.println("⚠️ Post not found, try again:");
            id = UserInput.getLong();
        }
        return id;
    }

    private static long askForValidCommentId(CommentService service) {
        System.out.println("Enter comment ID:");
        long id = UserInput.getLong();

        while (service.findById(id).isEmpty()) {
            System.out.println("⚠️ Comment not found, try again:");
            id = UserInput.getLong();
        }
        return id;
    }
}
