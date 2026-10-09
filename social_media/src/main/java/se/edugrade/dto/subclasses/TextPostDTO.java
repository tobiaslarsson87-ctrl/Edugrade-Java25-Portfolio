package se.edugrade.dto.subclasses;
import se.edugrade.utility.Colors;

public record TextPostDTO (Long id, String author, String text, int likes, int comments, String hashtag) implements ContentType {
    public TextPostDTO {
        if(id == null) throw new RuntimeException("ID can't be null");
        if(author == null||author.isBlank()) throw new RuntimeException("Author must contain at least one character & can't be null");
        if(text == null||text.isBlank()) throw new RuntimeException("A TextPost must contain at least one character");
        if(likes < 0) likes = 0;
        if(comments < 0) comments = 0;
    }

    @Override
    public String toString() {
        final String C = Colors.rgb(150,150,150);
        final String X = Colors.reset();
        StringBuilder sb = new StringBuilder();
        sb.append(C+"📝TEXT POST"+X).append("\n");
        sb.append("-".repeat(10)).append("\n");
        sb.append("🆔ID: ").append(this.id).append("\n");
        sb.append("👤Author: ").append(this.author).append("\n");
        sb.append("📋Text: ").append(this.text).append("\n");
        sb.append("❤️Likes: ").append(this.likes).append("\n");
        sb.append("💬Comments: ").append(this.comments).append("\n");
        sb.append("️#️⃣Hashtag: ").append(this.hashtag).append("\n");
        return sb.toString();
    }
}
