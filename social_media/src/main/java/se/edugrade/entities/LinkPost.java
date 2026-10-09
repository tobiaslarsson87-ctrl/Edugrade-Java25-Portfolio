package se.edugrade.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

// En LinkPost är en post som innehåller en URL - Ärver allt från PostEn

@Entity
@DiscriminatorValue("LINK") // Markerar vilken typ av Post detta är

public class LinkPost extends Post {

    // URL är obligatorisk för LinkPost
    @Column(nullable = true)
    private String linkUrl;

    @Column
    private String description; // Valfri text användaren kan skriva

    public LinkPost() {}

    /**
     * Skapar en LinkPost.
     *
     * @param author användaren som skapar posten
     * @param linkUrl länken som delas
     * @param description valfri text som hör till länken
     */

    public LinkPost(Users author, String linkUrl, String description) {
        super(author); //Skickar author vidare till post
        this.linkUrl = linkUrl;
        this.description = description;
    }

    public String getLinkUrl() { return linkUrl; }
    public String getDescription() { return description; }

    @Override
    public String getContentType() {
        return "LINK";
    }

    // För backupDatabase
    @Override
    public String getContent() {
        return this.linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
