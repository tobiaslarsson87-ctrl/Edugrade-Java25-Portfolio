package se.edugrade.entities;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import se.edugrade.exceptions.InvalidContentException;

import java.util.Optional;

/**
 * Subklass till Post som representerar ett bildinlägg.
 * Innehåller en bild-URL och en valfri bildtext.
 */
@Entity
@DiscriminatorValue("IMAGE")
public class ImagePost extends Post {

    /** URL till bilden (obligatorisk) */
    @Column(nullable = true)
    private String imageUrl;

    /** Bildtext, max 500 tecken, valfri */
    @Column(length = 500)
    private String caption;

    /** Tom konstruktor krävs av Hibernate */
    public ImagePost() {}

    /**
     * Skapar ett nytt ImagePost-objekt med validering.
     * @param author   användaren som skapar posten
     * @param imageUrl URL till bilden (får ej vara null eller blank)
     * @param caption  valfri bildtext
     * @throws InvalidContentException om imageUrl är ogiltig
     */
    public ImagePost(Users author, String imageUrl, String caption) {
        super(author);

        this.imageUrl = Optional.ofNullable(imageUrl)
                .filter(url -> !url.isBlank())
                .orElseThrow(() -> new InvalidContentException("Image URL cannot be null or blank"));

        this.caption = caption;
    }

    /** @return URL till bilden */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Uppdaterar bildens URL.
     *
     * @param imageUrl ny URL (får ej vara null eller blank)
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = Optional.ofNullable(imageUrl)
                .filter(url -> !url.isBlank())
                .orElseThrow(() -> new InvalidContentException("Image URL cannot be null or blank"));
    }

    /** @return bildtexten */
    public String getCaption() {
        return caption;
    }

    /**
     * Uppdaterar bildtexten.
     *
     * @param caption ny bildtext (valfri)
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Returnerar postens typ.
     * @return alltid "IMAGE"
     */
    @Override
    public String getContentType() {
        return "IMAGE";
    }

    /**
     * Returnerar innehållet som används vid backup/export.
     * @return bildens URL
     */
    @Override
    public String getContent() {
        return this.imageUrl;
    }

}
