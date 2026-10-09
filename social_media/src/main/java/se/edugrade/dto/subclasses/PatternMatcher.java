package se.edugrade.dto.subclasses;
import se.edugrade.entities.*;
import se.edugrade.exceptions.InvalidContentException;

import java.util.stream.Collectors;

public class PatternMatcher {
    public static ContentType unknownType(Post post){
        return switch(post){
            case TextPost t -> new TextPostDTO(t.getId(), t.getAuthor().toString(), t.getText(), t.getLikes().size(), t.getComments().size(),t.getHashtags().stream().map(Hashtag::toString).collect(Collectors.joining(" ")));
            case ImagePost i -> new ImagePostDTO(i.getId(), i.getAuthor().toString(), i.getImageUrl(), i.getCaption(), i.getLikes().size(), i.getComments().size(), i.getHashtags().stream().map(Hashtag::toString).collect(Collectors.joining(" ")));
            case VideoPost v -> new VideoPostDTO(v.getVideoUrl(), v.getDurationSeconds(), v.getLikes().size(), v.getComments().size(), v.getHashtags().stream().map(Hashtag::toString).collect(Collectors.joining(" ")));
            case LinkPost l -> new LinkPostDTO(l.getAuthor().toString(), l.getLinkUrl(), l.getDescription(), l.getCreatedAt(), l.getLikes().size(), l.getComments().size(), l.getHashtags().stream().map(Hashtag::toString).collect(Collectors.joining(" ")));
            default -> throw new InvalidContentException("Unknown content type: " + post.getClass().getSimpleName());
        };
    }
}
