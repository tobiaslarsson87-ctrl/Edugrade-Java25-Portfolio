package se.edugrade.service;

import se.edugrade.dto.HashtagDTO;
import se.edugrade.entities.Hashtag;
import se.edugrade.mappers.HashtagMapper;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HashtagService {
    // Visar vilken/vilka Hashtag som används beronde på int
    public List<String> trendingHashtags (List<String> hashtags, long minCount){
        return hashtags.stream()
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.counting()))
                .entrySet().stream()
                .filter(h -> h.getValue() >= minCount)
                .map(Map.Entry::getKey)
                .toList();
        /**
         * @filter = behåller endast 'Hashtag' baseras på 'minCount'
         */
    }
    // Visar vilka hashtags som har högsta weeklegrowth, Hashtagen som används mest baserats på int
    public List<HashtagDTO> topWeeklyGrowthHashtag(List<Hashtag> hashtags, int limit){
        return hashtags.stream()
                .sorted(Comparator.comparingInt(Hashtag::getWeeklyGrowth).reversed())
                .map(HashtagMapper::toDTO)// Mappar om till Dto
                .distinct().limit(limit)
                .toList();
        /**
         * @param sorted = Sorterar 'Hashtags' från störst till mins growth
         * @param distinct = inga dubbletter
         * @param limit = begränsar antalet
         */
    }
}
