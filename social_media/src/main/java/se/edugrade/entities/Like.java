package se.edugrade.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Objects;


//Varje Like är kopplad till en specefik post och en specefik användare

@Entity

//Säkerställer att en användare endast kan gilla en post en gång.
@Table(
        name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})
        },
        indexes = {
                @Index(name = "idx_like_post", columnList = "post_id"),
                @Index(name = "idx_like_user", columnList = "user_id"),
        }
)

public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    ------------------
    Relationer
    ------------------
     */

    //ManyToOne = Många likes kan tillhöra samma post
    // Fetch = LAZY - laddas vid behov

    @ManyToOne(optional = false, fetch =FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


    //En användare kan likea många poster
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) //En like får aldrig sakna en post
    private Users users;


    //Tidpunkt då användaren gillade posten
    @CreationTimestamp
    private LocalDateTime timestamp;

    // Hibernate kräver en tom protected constructor
    protected Like() {}

    /**
     * Skapar en ny Like mellan en användare och en post.
     *
     * @param post En post som gillas.
     * @param users Användaren som gillar posten.
     */

    public Like(Post post, Users users) {
        this.post = post;
        this.users = users;
    }

    public Long getId() { return id; }
    public Post getPost() { return post; }
    public Users getUser() { return users; }
    public LocalDateTime getTimestamp() { return timestamp; }


    // Tillåter uppdatering i timestamp för update funktion.
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Like)) return false;
        Like that = (Like) o;
        return Objects.equals(id, that.id);

    }
    @Override
    public int hashCode() { return Objects.hash(id); }

}