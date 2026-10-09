package se.edugrade.mappers;

import se.edugrade.dto.CommentDTO;
import se.edugrade.entities.Comment;
import se.edugrade.entities.Post;
import se.edugrade.entities.Users;
import se.edugrade.exceptions.InvalidContentException;
import se.edugrade.exceptions.UserNotFoundException;

import java.util.Optional;

/**
 * Mapper klass för att konvertera mellan Comment & CommentDTO
 */
public class CommentMapper {
    /**
     * Konverterar comment entity till CommentDTO
     * @param comment kommentar som ska konverteras
     * @return CommentDTO med samma data
     * @throws InvalidContentException om comment är null
     */
    public static CommentDTO toDTO(Comment comment) {

        // Validerar att comment inte är null och ger oss en "säker" referens att arbeta med, istället för att kolla en och en
        Comment safeComment = Optional.ofNullable(comment).orElseThrow(() -> new InvalidContentException("Comment cannot be null"));
        return new CommentDTO(
                safeComment.getId(),
                safeComment.getPost().getId(),
                safeComment.getAuthor().getId(),
                safeComment.getText(),
                safeComment.getCreatedAt());
    }
}
