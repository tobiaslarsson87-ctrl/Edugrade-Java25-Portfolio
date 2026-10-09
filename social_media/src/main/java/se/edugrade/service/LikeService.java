package se.edugrade.service;

import se.edugrade.dto.LikeDTO;
import se.edugrade.entities.Like;
import se.edugrade.entities.Post;
import se.edugrade.entities.Users;
import se.edugrade.exceptions.InvalidContentException;
import se.edugrade.mappers.LikeMapper;
import se.edugrade.repositories.LikeRepository;

import java.util.*;
import java.util.stream.Collectors;


public class LikeService {

    private final LikeRepository likeRepository;
    public LikeService(LikeRepository likeRepository) { this.likeRepository = likeRepository;}
    /*
    -------------------------------------
    DTO
    ------------------------------------
     */

    // Konverterar Like-entity → LikeDTO. Används när service ska returnera DTO istället för entity.

    //Create Like + Update like
    public LikeDTO likePost(Post post, Users user) {

        //1.Validera användaren
        if (user == null) {
            throw new InvalidContentException("❌User dosen't exist");
        }

        //2. Validera post
        if (post == null) {
            throw new InvalidContentException("❌Post dosen't exist");
        }

        //3. Kontrollera om användaren redan gillat posten
        boolean alreadyLiked = likeRepository.existsByUserAndPost(
                user.getId(),
                post.getId()
        );

        if (alreadyLiked) {
            throw new InvalidContentException(
                    "User has already liked this post"
            );
        }

        //4.Skapa och spara like (Entity)
        Like like = new Like(post, user);
        likeRepository.create(like);

        //5.Returnera DTO
        return LikeMapper.toDTO(like);

    }

    //Unlike (Delete Like)
    public void unlikePost(Post post, Users user) {

        if (post == null)
            throw new InvalidContentException("Post doesn't exist");
        if (user == null)
            throw new InvalidContentException("User doesn't exist");

        //Hitta like kopplad till user + post
        Like likeToRemove = likeRepository.findAll().stream()
                .filter(l ->
                        l.getPost().getId().equals(post.getId()) &&
                                l.getUser().getId().equals(user.getId())
                )
                .findFirst()
                .orElseThrow(() ->
                        new InvalidContentException("User has not liked this post")
                );

        //Ta bort like
        likeRepository.delete(likeToRemove.getId());
    }

    //Räkna likes (Update resultat) visar nytt antal likes.
    public long likeCountForPost(Long postId) {
        return likeRepository.findAll().stream()
                .filter(l -> l.getPost().getId().equals(postId))
                .count();
    }

    /*
    ---------------------------------
     Streams & LAMBDAS
     ---------------------------------
     */

    //Räknar antal likes per post. Key = Post-ID, Value = antal likes
    public Map<Long, Long> likesPerPost() {
        return likeRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        like -> like.getPost().getId(),
                        Collectors.counting()
                ));
    }


    // Returnerar posten som har flest likes
    public Optional<Post> mostLikedPost() {
        return likeRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        like -> like.getPost().getId(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .flatMap(e -> likeRepository.findByPostId(e.getKey())
                        .stream()
                        .map(Like::getPost)
                        .findFirst());

    }

    // PriorityQueue som returnerar de mest gillade posterna först (sorterat på likes).
    public List<Post> topLikedPosts(int limit) {

        Map<Long, Long> likeCounts = likesPerPost();

        Comparator<Post> byLikesDesc =
                Comparator.comparingLong((Post p) -> likeCounts.getOrDefault(p.getId(), 0L)).reversed();

        PriorityQueue<Post> queue = new PriorityQueue<>(byLikesDesc);

        Map<Long, Post> uniquePosts = likeRepository.findAll().stream()
                .map(Like::getPost)
                .collect(Collectors.toMap(
                        Post::getId,
                        p -> p,
                        (a, b) -> a
                ));

        uniquePosts.values().forEach(queue::offer);

        List<Post> result = new ArrayList<>();
        for (int i = 0; i < limit && !queue.isEmpty(); i++) {
            result.add(queue.poll());
        }
        return result;
    }



    //Hämtar alla likes för en specifik post.
    public List<Like> findLikesForPost(Long postId) {
        return likeRepository.findByPostId(postId);
    }

    //Kontrollerar om en användare redan gillat en post.
    public boolean hasUserLikedPost(Long userId, Long postId) {
        return likeRepository.existsByUserAndPost(userId, postId);
    }

    //Hämtar alla likes för en specefik post och returnerar dem som DTOs.
    public List<LikeDTO> findLikesForPostDTO(Long postId) {
        return LikeMapper.toDTOList(likeRepository.findByPostId(postId));
    }


    // Avancerad stream: genomsnittligt antal likes per post
    public double averageLikesPerPost() {
        return likeRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        like -> like.getPost().getId(),
                        Collectors.counting()
                ))
                .values().stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
    }


}
