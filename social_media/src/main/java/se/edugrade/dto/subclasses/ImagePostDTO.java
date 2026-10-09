package se.edugrade.dto.subclasses;
import se.edugrade.utility.Colors;

public record ImagePostDTO(Long id, String author, String url, String caption, int likes, int comments, String hashtag) implements ContentType {
    public ImagePostDTO {
        if(id == null) throw new RuntimeException("ID can't be null");
        if(author == null||author.isBlank()) throw new RuntimeException("Author must contain at least one character & can't be null");
        if(url == null||url.isBlank()) throw new RuntimeException("URL must contain at least one character & can't be null");
        if(caption == null||caption.isBlank()) throw new RuntimeException("A caption must contain at least one character & can't be null");
        if(likes < 0) likes = 0;
        if(comments < 0) comments = 0;
    }

    @Override
    public String toString() {
        final String C = Colors.rgb(25,175,75);
        final String X = Colors.reset();
        StringBuilder sb = new StringBuilder();
        sb.append(C+"🖼️IMAGE POST"+X).append("\n");
        sb.append("-".repeat(10)).append("\n");
        sb.append("🆔ID: ").append(this.id).append("\n");
        sb.append("👤Author: ").append(this.author).append("\n");
        sb.append("🌍URL: ").append(this.url).append("\n");
        sb.append("📋Caption: ").append(this.caption).append("\n");
        sb.append("❤️Likes: ").append(this.likes).append("\n");
        sb.append("💬Comments: ").append(this.comments).append("\n");
        sb.append("#️⃣Hashtag: ").append(this.hashtag).append("\n");
        return sb.toString();
    }
}
