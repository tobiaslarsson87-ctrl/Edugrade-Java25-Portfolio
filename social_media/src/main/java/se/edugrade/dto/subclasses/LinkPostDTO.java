package se.edugrade.dto.subclasses;

import se.edugrade.utility.Colors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record LinkPostDTO(
        String authorName,
        String url,
        String description,
        LocalDateTime createdAt,
       int like,
        int comment,
        String hashtag
) implements ContentType {

    @Override
    public String toString() {
        String C = Colors.rgb(0, 200, 255);
        String R = Colors.reset();
        createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));


        return """
%s🔗 LINK POST%s
─────────────────────────
👤 User: %s
🌍 URL: %s
📝 Description: %s
🕒 Created: %s
❤️ Likes: %s
💬 Comments: %s
#️⃣ Hashtag: %s
""".formatted(
                C, R,
                authorName,
                url,
                description != null ? description : "No description",
                createdAt, like, comment, hashtag != null ? hashtag : "No hashtag"
        );
    }
}
