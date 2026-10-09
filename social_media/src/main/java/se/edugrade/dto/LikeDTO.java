package se.edugrade.dto;

import se.edugrade.utility.Colors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record LikeDTO(
        Long id,
        Long postId,
        Long userId,
        LocalDateTime timestamp
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public String toString() {

        String C = Colors.rgb(255,100,150);
        String R = Colors.reset();

        return """
%s❤️ LIKE%s
─────────────────────────
🆔 Id: %d
👤 User: %d
📝 Post: %d
🕒 Time: %s
""".formatted(
                C, R,
                id,
                userId,
                postId,
                timestamp != null ? timestamp.format(FORMATTER) : "N/A"
        );
    }
}