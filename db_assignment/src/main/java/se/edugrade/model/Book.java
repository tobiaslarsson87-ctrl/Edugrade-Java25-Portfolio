package se.edugrade.model;

public class Book {
    private int id;
    private String title;
    private String isbn;
    private int pubYear;
    private int authorId;
    private String authorName;

    public Book(int id, String title, String isbn, int pubYear, int authorId, String authorName) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.pubYear = pubYear;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public Book(int id, String title, String isbn, int pubYear, int authorId) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.pubYear = pubYear;
        this.authorId = authorId;
    }

    // Konstruktor utan ID (t.ex. vid skapande av ny bok)
    public Book(int id,String s, String number, int pubYear) {
        this.id = id;
        this.title = s;
        this.isbn = number;
        this.pubYear = pubYear;


    }

    // Konstruktor utan författar-ID (kan användas för enklare listor)
    public Book(String s, String number, int i) {
        this.title = s;
        this.isbn = number;
        this.pubYear = i;
    }

    // Getters och setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPubYear() {
        return pubYear;
    }



    public int getAuthorId() {
        return authorId;
    }


    public String getAuthorName() {
        return authorName;
    }


    // Skriver ut en läsbar representation av objektet
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", pubYear=" + pubYear +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                '}';
    }

    public String PubYear() {
        return "";
    }

    public String AuthorId() {
        return "";
    }
}
