package se.edugrade.utility;

import se.edugrade.dto.CommentDTO;
import se.edugrade.entities.Comment;
import se.edugrade.entities.Post;
import se.edugrade.entities.Users;

public class PrintHelper {
    private PrintHelper() {}

    /**
     * Skriver en CommentDTO i "snyggare" format.
     */
    public static void printCommentDTO(CommentDTO dto) {
        System.out.println(
                "💬 Author ID: " + dto.authorId() +
                        "\n📝 Commented: \"" + dto.text() + "\"" +
                        "\n🕒 Created at: " + dto.createdAt() +
                        "\n📌 Post ID: " + dto.postId() +
                        "\n"
        );
    }
    /**
     * Skriver ut en comment med ID, user och text.
     */
    public static void printComment(Comment c) {
        System.out.println(
                "💬 [" + c.getId() + "] " +
                        c.getAuthor().getUserName() +
                        " commented: \"" + c.getText() + "\"" +
                        "\n🕒 " + c.getCreatedAt() +
                        "\n"
        );
    }
    /** Skriver ut ett inlägg med metadata,hashtags, likes och kommentarer.
     * */
    public static void printPost(Post p, Users currentUser) {
        boolean isMine = p.getAuthor().getId().equals(currentUser.getId());

        String hashtags = p.getHashtags().isEmpty()
                ? "None"
                : p.getHashtags().stream()
                .map(h -> "#" + h.getTag())
                .reduce((a, b) -> a + " " + b)
                .orElse("");

        System.out.println(
                (isMine ? "⭐ YOUR POST ⭐\n" : "") +
                        "📌 Post " + p.getId() +
                        " by @" + p.getAuthor().getUserName() +
                        "\n📝 \"" + p.getContent() + "\"" +
                        "\n🏷️ Hashtags: " + hashtags +
                        "\n🕒 Created at: " + p.getCreatedAt() +
                        "\n❤️ Likes: " + p.getLikes().size() +
                        "   💬 Comments: " + p.getComments().size() +
                        "\n"
        );
    }
    public static void divider(){
        System.out.println("----------------------");
    }
}
