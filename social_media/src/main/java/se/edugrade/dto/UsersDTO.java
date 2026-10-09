package se.edugrade.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import se.edugrade.utility.Colors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record UsersDTO(Long id,
                       String userName,
                       String bio,
                       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                       LocalDateTime createdAt,
                       int followerCount,
                       int postCount)
 {
    public UsersDTO {
        if (id == null) throw new IllegalArgumentException("Id can not be null");
        if(userName == null || userName.isBlank()) throw new IllegalArgumentException("Username can't be empty or null");
        if(bio == null || bio.isBlank()) throw new IllegalArgumentException("Biography can't be empty or null");
        if(createdAt == null) throw new IllegalArgumentException("Timestamp can't be null");
        if(followerCount < 0) followerCount = 0;
        if(postCount < 0) postCount = 0;
    }

    @Override
    public String toString() {
        final String C = Colors.rgb(150,150,150);
        final String X = Colors.reset();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append(C+"🆔ID: "+X).append(this.id).append("\n");
        sb.append(C+"👤Username: "+X).append(this.userName).append("\n");
        sb.append(C+"📋Biography: "+X).append(this.bio).append("\n");
        sb.append(C+"🕒Created: "+X).append(this.createdAt.format(format)).append("\n");
        sb.append(C+"🫂Followers: "+X).append(this.followerCount).append("\n");
        sb.append(C+"📝Posts: "+X).append(this.postCount).append("\n");

        return sb.toString();
    }
}
