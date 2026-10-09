package se.edugrade.service;
import jakarta.persistence.EntityManagerFactory;
import se.edugrade.dto.CommentDTO;
import se.edugrade.mappers.CommentMapper;
import se.edugrade.entities.Comment;
import se.edugrade.entities.Post;
import se.edugrade.entities.Users;
import se.edugrade.exceptions.InvalidContentException;
import se.edugrade.repositories.CommentRepository;
import se.edugrade.repositories.PostRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service-klass som hanterar logik för kommentarer.
 * Använder CommentRepository för databas-op och CommentMapper för DTO-konvertering.
 */
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(EntityManagerFactory emf){
        this.commentRepository = new CommentRepository(emf);
        this.postRepository = new PostRepository(emf);
    }
    /**
     * Skapar en ny kommentar till en post.
     * @param post   posten som kommentaren tillhör
     * @param author användaren som skriver kommentaren
     * @param text   kommentartexten
     * @return CommentDTO av den sparade kommentaren
     * @throws InvalidContentException om post, author eller text är ogiltiga
     */
    public CommentDTO createComment(Post post, Users author, String text) {

        Optional.ofNullable(post)
                .orElseThrow(() -> new InvalidContentException("Post cannot be null"));

        Optional.ofNullable(author)
                .orElseThrow(() -> new InvalidContentException("Author cannot be null"));

        Optional.ofNullable(text)
                .filter(t -> !t.isBlank())
                .orElseThrow(() -> new InvalidContentException("Text cannot be empty"));

        Comment comment = new Comment(post, author, text);
        Comment saved = commentRepository.create(comment);
        return CommentMapper.toDTO(saved);
    }
    /**
     * Söker kommentarer på en post som innehåller ett visst keyword.
     * @param post posten vars kommentarer ska sökas igenom
     * @param keyword sökordet
     * @return lista av CommentDTO som matchar sökordet
     * @throws InvalidContentException om post eller keyword är ogiltiga
     */
    public List<CommentDTO> searchCommentsByKeyword(Post post, String keyword) {

        Optional.ofNullable(post)
                .orElseThrow(() -> new InvalidContentException("Post cannot be null"));

        Optional.ofNullable(keyword)
                .filter(k -> !k.isBlank())
                .orElseThrow(() -> new InvalidContentException("Keyword cannot be empty"));

        return post.getComments().stream()
                .filter(c -> c.getText().toLowerCase().contains(keyword.toLowerCase()))
                .map(CommentMapper::toDTO)
                .toList();
    }
    /**
     * Hämtar och sorterar alla kommentarer på en post i kronologisk ordning.
     * @param post posten vars kommentarer ska sorteras
     * @return sorterad lista av CommentDTO
     * @throws InvalidContentException om post är null
     */
    public List<CommentDTO> getSortedComments(Post post) {

        Optional.ofNullable(post)
                .orElseThrow(() -> new InvalidContentException("Post cannot be null"));

        return post.getComments().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .map(CommentMapper::toDTO)
                .toList();
    }
    /**
     * Räknar antal kommentarer per användare på en post.
     * @param post posten vars kommentarer ska analyseras
     * @return en Map där key = användarnamn och value = antal kommentarer
     * @throws InvalidContentException om post är null
     */
    public Map<String, Long> countCommentsByUser(Post post) {

        Optional.ofNullable(post)
                .orElseThrow(() -> new InvalidContentException("Post cannot be null"));

        return post.getComments().stream()
                .collect(Collectors.groupingBy(
                        c -> c.getAuthor().getUserName(),
                        Collectors.counting()
                ));
    }
    /**
     * Beräknar genomsnittlig längd på kommentarstext för en post.
     * @param post posten vars kommentarer ska analyseras
     * @return genomsnittlig längd, eller 0.0 om inga kommentarer finns
     * @throws InvalidContentException om post är null
     */
    public double averageCommentLength(Post post) {

        Optional.ofNullable(post)
                .orElseThrow(() -> new InvalidContentException("Post cannot be null"));

        return post.getComments().stream()
                .mapToInt(c -> c.getText().length())
                .average()
                .orElse(0.0);
    }
    /**
     * Uppdaterar texten på en kommentar.
     * @param comment kommentaren som ska uppdateras
     * @param newText ny text
     * @return uppdaterad CommentDTO
     * @throws InvalidContentException om texten är ogiltig
     */
    public CommentDTO updateComment(Comment comment, String newText) {

        Optional.ofNullable(comment)
                .orElseThrow(() -> new InvalidContentException("Comment cannot be null"));

        Optional.ofNullable(newText)
                .filter(t -> !t.isBlank())
                .orElseThrow(() -> new InvalidContentException("Text cannot be empty"));

        comment.setText(newText);
        Comment updated = commentRepository.update(comment);

        return CommentMapper.toDTO(updated);
    }
    /**
     * Tar bort en kommentar.
     * @param id kommentarens ID
     */
    public void deleteComment(long id) {
        commentRepository.delete(id);
    }
    public Optional<Comment> findById(long id){
        return commentRepository.findById(id);
    }

    public Comment update(Comment comment) {
        return commentRepository.update(comment);
    }
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    // Dessa kanske borde ligga i postservice men vill inte vara där och pilla nu.
    public Optional<Post> findPostByIdWithRelations(Long id){
        return postRepository.findByIdWithRelations(id);
    }
    public Post update(Post post){
        return postRepository.update(post);
    }

    public List<Post> findAllWithRelations() {
        return postRepository.findAllWithRelations();
    }


}
