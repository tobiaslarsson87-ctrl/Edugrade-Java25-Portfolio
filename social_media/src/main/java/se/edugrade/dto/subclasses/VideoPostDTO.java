package se.edugrade.dto.subclasses;

public record VideoPostDTO(
        String url,
        int durationSeconds,
        int likes,
        int comments,
        String hashtag
) implements ContentType {
    public VideoPostDTO{
        if (url == null )
            throw new IllegalArgumentException("Url, can not be null");
        if ( durationSeconds < 0 )
            throw new IllegalArgumentException("A video can not be negative in sec");
    }
    @Override
    public String toString() { //Gör printout snyggare
        return """
     🎥 Video POST
─────────────────────────
🌍 URL: %s
🕒 Sec: %s
❤️ Likes: %s
💬 Comments: %s
#️⃣ Hashtag: %s
""".formatted(
                url, durationSeconds, likes, comments, hashtag
        );
    }
}

