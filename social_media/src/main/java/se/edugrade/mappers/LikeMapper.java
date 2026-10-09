package se.edugrade.mappers;

import se.edugrade.dto.LikeDTO;
import se.edugrade.entities.Like;

import java.util.List;

public class LikeMapper {

    public static LikeDTO toDTO(Like like) {
        // Skyddar mot NullPointerException om metoden får in null istället för en Like
        if (like == null) return null;

        return new LikeDTO(
                like.getId(),
                like.getPost().getId(),
                like.getUser().getId(),
                like.getTimestamp()
        );
    }
    // Konverterar en lista av Like-entities till LikeDTO och garanterar att vi aldrig returnerar null (alltid en säker lista)
    public static List<LikeDTO> toDTOList(List<Like> likes) {
        if (likes == null) return List.of();
        return likes.stream()
                .map(LikeMapper::toDTO)
                .toList();
    }
}
