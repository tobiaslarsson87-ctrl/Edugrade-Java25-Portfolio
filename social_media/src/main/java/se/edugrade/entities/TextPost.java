package se.edugrade.entities;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Set;

@Entity
@DiscriminatorValue("TEXT")
public class TextPost extends Post {
    private String text;

    public TextPost(){}

    public TextPost(Users author, String text) {
        super (author);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getContentType() {
        return "TEXT";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Author: ").append(this.getAuthor()).append("\n");
        sb.append("Text: ").append(this.text).append("\n");
        return sb.toString();
    }
    // För backupDatabase
    @Override
    public String getContent() {
        return this.text;
    }

}
