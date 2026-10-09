package se.edugrade.entities;

import jakarta.persistence.*;
import se.edugrade.exceptions.InvalidContentException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Representerar kommentarer gjorde av användare på en post.
 * Comments - länkas ihop med både Post & Users
 */
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation till PostEn
    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Relation till UserEn
    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Users author;

    @Column(nullable = false, length = 500)
    private String text;
    // Tidpunkt då kommentar skapades.
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Tom konstruktor för hibernate
    public Comment() {
    }

    /**
     * Skapar en ny kommentar med validering
     *
     * @param post   posten som kommentaren tillhör
     * @param author "författaren" till kommentaren
     * @param text   Textkommentaren (kan ej vara tom)
     * @throws InvalidContentException om något av fälten är ogiltiga
     */
    public Comment(Post post, Users author, String text) {
        this.post = Optional.ofNullable(post)
                .orElseThrow(() -> new InvalidContentException("Post cannot be null"));

        this.author = Optional.ofNullable(author)
                .orElseThrow(() -> new InvalidContentException("Author cannot be null"));

        this.text = Optional.ofNullable(text)
                .filter(t -> !t.isBlank())
                .orElseThrow(() -> new InvalidContentException("Text cannot be null or blank"));
    }
    /**
     * @return kommentarens ID
     */
    public Long getId() { return id;}

    /**
     * @return inlägget som kommentaren tillhör
     */
    public Post getPost() { return post;}

    /**
     * @return användaren som skrev kommentaren
     */
    public Users getAuthor() { return author;}

    /**
     * @return kommentartexten
     */
    public String getText() { return text;}

    /**
     * @return tidpunkten då kommentaren skapades
     */
    public LocalDateTime getCreatedAt() { return createdAt;}

    /**
     * @return uppdaterar kommentarstexten
     */
    public void setText(String text) {
        this.text = Optional.ofNullable(text)
                .map(String::trim)
                .filter(t -> !t.isEmpty())
                .orElseThrow(() -> new InvalidContentException("Text cannot be null or empty."));
    }

    public boolean equals(Object o) {
        return (this == o) || (o instanceof Comment c && Objects.equals(id, c.id));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "💬 Author: " + author.getUserName() +
                "\n📝 Commented: \"" + text + "\"" +
                "\n🕒 Created at: " + createdAt +
                "\n📌 Post ID: " + post.getId() +
                "\n";
    }

}
