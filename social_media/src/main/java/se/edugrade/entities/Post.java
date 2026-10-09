package se.edugrade.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import se.edugrade.exceptions.InvalidContentException;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "post")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextPost.class, name= "TextPost"),
        @JsonSubTypes.Type(value = LinkPost.class, name= "LinkPost"),
        @JsonSubTypes.Type(value = VideoPost.class, name = "VideoPost"),
        @JsonSubTypes.Type(value = ImagePost.class, name = "ImagePost")
})
public abstract class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) //Relation till Users
    @JoinColumn(name = "author_id")
    private Users author;
    private LocalDateTime createdAt;

    /*
    Sätter Orphan removal till true för att det inte längre ska finnas kvar comments, likes och hashtags på post
    som tagits bort.
    *      * EAGER behövs för att comments ska vara laddade när PatternMatcher skapar DTO:er.
     * Annars kastas LazyInitializationException eftersom sessionen är stängd i service/test.(otrevlig upptäckt)
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    // Ingen Orphan removal för att olika post kan ha samma hashtag.
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "post_hashtag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags = new HashSet<>();

    public Post() {
    }

    public Post(Users author) {
        this.author = Optional.ofNullable(author)
                .orElseThrow(() -> new InvalidContentException("Author cannot be null"));
 this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Users getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Comment> getComments() { return comments; }
    public Set<Like> getLikes() { return likes; }
    public Set<Hashtag> getHashtags() { return hashtags; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Post p) && Objects.equals(id, p.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
    public abstract String getContentType();

    // För backupDatabase
    public abstract String getContent();

    //För att Hashtag ska kunna visas i post
    public void setHashtags(Set<Hashtag> hashtags){
        this.hashtags = hashtags;
    }
}
