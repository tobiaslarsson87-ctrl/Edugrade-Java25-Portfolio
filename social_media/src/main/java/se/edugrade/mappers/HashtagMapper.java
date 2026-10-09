package se.edugrade.mappers;

import se.edugrade.dto.HashtagDTO;
import se.edugrade.entities.Hashtag;
import se.edugrade.exceptions.InvalidContentException;

// Hashtag Mapper är en mellan hand mellan Hashtag och HashtagDTO

public class HashtagMapper {
    public static HashtagDTO toDTO(Hashtag hashtag){
        if(hashtag == null) throw new InvalidContentException("hashtag can not be null");
        return new HashtagDTO(
                hashtag.getId(),
                hashtag.getTag(),
                hashtag.getWeeklyGrowth()
        );
    }
}
