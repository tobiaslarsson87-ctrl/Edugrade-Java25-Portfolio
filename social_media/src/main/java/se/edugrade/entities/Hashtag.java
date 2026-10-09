package se.edugrade.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "hashtags")
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tag;

    @ManyToMany(mappedBy = "hashtags") // En many-to-many relation till Post
    private Set<Post> post = new HashSet<>();

    @Column
    private int weeklyGrowth;

    public Hashtag(){} // Tom konstruktor för Hibernate

    public Hashtag(String tag, int weeklyGrowth) {
        this.tag = tag;
        this.weeklyGrowth = weeklyGrowth;
    }
    public Hashtag(String tag){
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setWeeklyGrowth(int weeklyGrowth){
        this.weeklyGrowth = weeklyGrowth;
    }

    public int getWeeklyGrowth() {
        return weeklyGrowth;
    }

    public Set<Post> getPost() {
        return post;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Hashtag hashtag = (Hashtag) o;
        return Objects.equals(id, hashtag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return tag;
    }
}
