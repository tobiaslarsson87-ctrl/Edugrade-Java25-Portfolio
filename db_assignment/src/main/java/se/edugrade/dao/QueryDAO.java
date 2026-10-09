package se.edugrade.dao;

import se.edugrade.model.Book;
import se.edugrade.utility.DatabaseConnection;
import se.edugrade.utility.Helper;

import java.sql.*;
import java.util.*;

/**
 * QueryDAO innehåller SQL som JOIN, GROUP BY, WHERE och subqueries.
 * Den används för att visa sammansatt eller filtrerad information i bibliotekssystemet.
 */
public class QueryDAO {

    /**
     * Hämtar alla böcker tillsammans med tillhörande författarnamn.
     * Använder en JOIN mellan book och author.
     *
     * @return Lista med strängar i formatet "Titel – Författare"
     */
    public List<String> getBooksWithAuthors() {
        List<String> result = new ArrayList<>();
        String sql = """
                SELECT b.title, a.name 
                FROM book b
                JOIN book_author ba ON b.id = ba.book_id
                JOIN author a ON ba.author_id = a.id
                ORDER BY a.name ASC;
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(String.format("📘 %s – ✍ %s", rs.getString("title"), rs.getString("name")));
            }
        } catch (SQLException e) {
            System.err.println("Fel vid JOIN-query: " + e.getMessage());
        }
        return result;
    }


    /**
     * Räknar hur många böcker varje författare har skrivit.
     * Använder GROUP BY och COUNT.
     *
     * @return Map med författarnamn som nyckel och antal böcker som värde
     */
    public Map<String, Integer> getBookCountPerAuthor() {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT a.name," +
                " COUNT(ba.book_id) AS book_count" +
                " FROM author a " +
                "JOIN book_author ba ON a.id = ba.author_id " +
                "GROUP BY a.name" +
                " ORDER BY book_count";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.put(rs.getString("name"), rs.getInt("book_count"));
            }
        } catch (SQLException e) {
            System.err.println("Fel vid GROUP BY-query: " + e.getMessage());
        }
        return result;
    }

    /**
     * Hämtar alla böcker som är skrivna av svenska författare.
     * Använder en subquery för att filtrera på nationalitet.
     *
     * @return Lista med Book-objekt av svenska författare
     */
    public List<Book> getBooksBySwedishAuthors() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.id, b.title, b.isbn, b.pub_year,a.id AS author_id, a.name AS authorName" +
                " FROM book b" +
                " JOIN book_author ba ON b.id = ba.book_id" +
                " JOIN author a ON ba.author_id = a.id" +
                " WHERE a.nationality = 'Sweden'" +
                " ORDER BY a.name ASC;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("pub_year"),
                        rs.getInt("author_id"),
                        rs.getString("authorName")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Fel vid subquery: " + e.getMessage());
        }
        return books;
    }
    //Söker upp böcker släppta senare än ett visst år, önskat årtal anges som arg till metod.
    // ✅objektnamn.getBooksAfterPubYear(Önskat årtal)
    public List<Book> getBooksAfterPubYear(int year) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE pub_year >= ? ";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, year);

        try {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("isbn"),
                rs.getInt("pub_year")

                );
                    books.add(book);
                    System.out.print(Helper.colorS("green", "✅MATCH FOUND: "));
                    System.out.println(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Query Error: " + e.getMessage());
        }
        return books;
    }

    //Söker upp böcker släppta innan ett visst år, önskat årtal anges som arg till metod.
    // ✅objektnamn.getBooksBeforePubYear(Önskat årtal)
    public List<Book> getBooksBeforePubYear(int year) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE pub_year < ? ";

        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, year);

        try {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("pub_year")
                );
                books.add(book);
                System.out.print(Helper.colorS("green", "✅MATCH FOUND: "));
                System.out.println(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Query Error: " + e.getMessage());
        }
        return books;
    }
}




