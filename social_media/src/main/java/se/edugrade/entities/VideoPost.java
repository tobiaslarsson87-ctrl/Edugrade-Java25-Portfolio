package se.edugrade.entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("VIDEO") // För att veta vilken 'post_type' det är
public class VideoPost extends Post{
    @Column(nullable = true)
    private String videoUrl;
    private int durationSeconds;

    public VideoPost () {}// Tom konstruktor för Hibernate

    public VideoPost(Users author, String videoUrl, int durationSeconds){
        super(author); // Vilken auther som gör en hashtag
        this.videoUrl = videoUrl;
        this.durationSeconds = durationSeconds;
    }
    public String getVideoUrl() {
        return videoUrl;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String getContentType() {
        return "VIDEO"; // För att veta att det är Content 'Video' som läggs till
    }
    // För backupDatabase
    @Override
    public String getContent() {
        return this.videoUrl;
    } //Retunernar att det är en videoURL


}
