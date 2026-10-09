package se.edugrade.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.dto.subclasses.*;
import se.edugrade.entities.Post;
import se.edugrade.entities.TextPost;
import se.edugrade.repositories.PostRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PostService {
    private final PostRepository pr;

    public PostService(PostRepository pr) {
        this.pr = pr;
    }

    public List<ContentType> showAll() {
        return pr.findAllWithRelations().stream()
                .map(PatternMatcher::unknownType)
                .toList();
    }

    public ContentType showById(Long id){
        return pr.findByIdWithRelations(id)
                .map(PatternMatcher::unknownType)
                .orElseThrow(() -> new RuntimeException("No post found with ID: " + id));
    }

    public List<ContentType> sortByLikes(){
        return pr.findAllWithRelations().stream()
                .sorted(Comparator.comparing((Post p) -> p.getLikes().size()).reversed())
                .map(PatternMatcher::unknownType)
                .toList();
    }

    public List<ContentType> sortByComments(){
        return pr.findAllWithRelations().stream()
                .sorted(Comparator.comparing((Post p) -> p.getComments().size()).reversed())
                .map(PatternMatcher::unknownType)
                .toList();
    }

    public List<ContentType> sortByNewest(){
        return pr.findAllWithRelations().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(PatternMatcher::unknownType)
                .toList();
    }

    public List<ContentType> sortByOldest(){
        return pr.findAllWithRelations().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt))
                .map(PatternMatcher::unknownType)
                .toList();
    }

    public List<ContentType> search(String keyword){
        return pr.findAllWithRelations().stream()
                .filter(p -> p.toString()
                        .toLowerCase()
                        .contains(keyword.toLowerCase()))
                .map(PatternMatcher::unknownType)
                .toList();
    }
    public List<ContentType> showAllText() {
        return pr.findAllWithRelations().stream()
                .map(PatternMatcher::unknownType)
                .filter(c -> c instanceof TextPostDTO)
                .toList();
    }
    public List<ContentType> showAllImage(){
        return pr.findAllWithRelations().stream()
                .map(PatternMatcher::unknownType)
                .filter(c -> c instanceof ImagePostDTO)
                .toList();
    }
    public List<ContentType> showAllVideo(){
        return pr.findAllWithRelations().stream()
                .map(PatternMatcher::unknownType)
                .filter(c -> c instanceof VideoPostDTO)
                .toList();
    }
    public List<ContentType> showAllLink(){
        return pr.findAllWithRelations().stream()
                .map(PatternMatcher::unknownType)
                .filter(c -> c instanceof LinkPostDTO)
                .toList();
    }
}
















