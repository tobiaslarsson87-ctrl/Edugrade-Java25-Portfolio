package se.edugrade.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 25)
    private String userName;
    @Column(length = 1000)
    private String bio;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    //FOLLOWERS
    @ManyToMany
    @JoinTable(
        name = "user_followers",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<Users> followers = new HashSet<>();

    //FOLLOWING
    @ManyToMany(mappedBy = "followers")
    private Set<Users> following = new HashSet<>();

    //POSTS
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    public Users(){}

    public Users(String userName, String bio) {
        this.userName = userName;
        this.bio = bio;
        this.createdAt = LocalDateTime.now();
    }

    public Users(String userName, String bio, LocalDateTime createdAt) {
        this.userName = userName;
        this.bio = bio;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void autoDate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getBio() {
        return bio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Set<Users> getFollowers() {
        return followers;
    }

    public Set<Users> getFollowing() {
        return following;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(id, users.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(this.id).append("\n");
        sb.append("Username: ").append(this.userName).append("\n");
        sb.append("Biography: ").append(this.bio).append("\n");
        sb.append("CreatedAt: ").append(this.createdAt).append("\n");
        return sb.toString();
    }

    //HJÄLPMETODER
    public void follow(Users other) {
        other.followers.add(this);   // B.followers.add(A)  ← OWNING SIDE
        this.following.add(other);   // A.following.add(B)  ← INVERSE SIDE
    }

    public void unFollow(Users other) {
        other.followers.remove(this);
        this.following.remove(other);
    }
}
