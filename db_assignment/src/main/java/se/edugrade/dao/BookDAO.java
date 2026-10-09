package se.edugrade.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import se.edugrade.model.Book;
import se.edugrade.utility.DatabaseConnection;
import se.edugrade.utility.ExceptionLogger;

public class BookDAO {
    //Hämsta alla böcker (inklusive författaensnamn via JOIN
    public List<Book> getAllBooks() {
        List<Book> book = new ArrayList<>();

        //SQL frågar med JOIN för att få med författaens namn
        String sql = "SELECT b.id, b.title, b.isbn, b.pub_year AS pubYear, a.name AS authorName, a.id AS authorId " +
                "FROM book b " +
                "JOIN book_author ba ON b.id = ba.book_id " +
                "JOIN author a ON ba.author_id = a.id " +
                "ORDER BY a.name ASC";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                Book a = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("pubYear"),
                        rs.getInt("authorId"),
                        rs.getString("authorName")
                );

                book.add(a);
            }
        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem with fetching books from database");
        }

        return book;
    }

    //Lägger till en ny bok i databasen
    public int insertBook(Book book) {
        String sql = "INSERT INTO book(title, isbn, pub_year) VALUES (?, ?, ?)";
        int rows = 0;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
        stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getIsbn());
            stmt.setInt(3, book.getPubYear());

            rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
            insertbookAuthor(id,book.getAuthorId());
            }
            System.out.println("📚 Book added: " + book.getTitle());
        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem adding book to database.");
        }

        return rows;
    }
    // Lägger till kopplingen mellan author&book i addBook

    public int insertbookAuthor(int book_id, int author_id) {
        String sql = "INSERT INTO book_author(book_id, author_id) VALUES (?, ?)";
        int rows = 0;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, book_id);
            stmt.setInt(2, author_id);

            rows = stmt.executeUpdate();

        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem adding book to database.");
        }

        return rows;
    }

    //Hämta en specefik bok utifrån ID
    public Book getBookById(int id) {
        String sql = "SELECT id, title, isbn, pub_year AS pubYear FROM book WHERE id = ?";
        Book book = null;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("pubYear")

                );
            }
        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem fetching book with ID: " + id);
        }

        return book;
    }

    //Tar bort bok via ID
    public int deleteBookById(int id) {
        deleteBookAuthor(id);
        String sql = "DELETE FROM book WHERE id = ?";

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Book deleted with bookID: " + id);
            } else {
                System.out.println("No book found with bookID: " + id);
            }
            return rows;
        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem deleting this Book");
            return 0;
        }
    }
    public int deleteBookAuthor(int book_id) {
        String sql = "DELETE FROM book_author WHERE book_id = ?";
        int rows = 0;
        System.out.println("test12");
        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, book_id);
            System.out.println("etst");
            rows = stmt.executeUpdate();
            System.out.println("Test");
        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem deleting book from database.");
        }

        return rows;

    }

}
