package se.edugrade.dao;

import se.edugrade.model.Author;
import se.edugrade.utility.DatabaseConnection;
import se.edugrade.utility.ExceptionLogger;
import se.edugrade.utility.Helper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT * FROM author";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql);

             // Här sparas resultatet av Queryn i rs
             ResultSet rs = stmt.executeQuery()) {

            // Så länge rs har en ny rad = true
            while (rs.next()) {
                // Här skapas author a
                Author a = new Author(rs.getInt("id"), rs.getString("name"), rs.getDate("birth_date"), rs.getString("nationality"));
                // Här läggs en nyskapad author a till i listan
                authors.add(a);
            }
        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem with fetching authors from database");
        }
        return authors;
    }

    public int insertAuthor(Author author) {
        String sql = "INSERT INTO author(name, birth_date, nationality) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // ← Viktigt!

            stmt.setString(1, author.getName());
            stmt.setDate(2, author.getBirthDate());
            stmt.setString(3, author.getNationality());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("✅ Author added! : " + author.getName() + " (ID: " + id + ")");
                return id;

                // RETURNERAR ID!!!!!
            }

        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem adding authors to database");
        }
        return 0;
    }


    public void updateAuthor(Author author) {
        String sql = "UPDATE author SET name = ?, birth_date = ?, nationality = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, author.getName());
            stmt.setDate(2, author.getBirthDate());
            stmt.setString(3, author.getNationality());
            stmt.setInt(4, author.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✏️ Updated author: " + author.getName());
            } else {
                System.out.println("⚠️ No author with that ID was found. " + author.getId());
            }
        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem updating this Author");
        }
    }

    public void deleteAuthor(int id) {
        String sql = "DELETE FROM author WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("🗑️ Author with ID " + id + " deleted.");
            } else {
                System.out.println("⚠️ No Author was found with that ID: " + id);
            }
        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem deleting this Author");
        }

    }

    public Author getAuthorById(int id) {
        String sql = "SELECT * FROM author WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            // Här sparas resultatet av Queryn i rs
            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                // Här skapas author a
                Author a = new Author(rs.getInt("id"), rs.getString("name"), rs.getDate("birth_date"), rs.getString("nationality"));

                System.out.println(Helper.colorS("yellow", "\n📌 Author found: ") + a);
                return a;
            } else {
                System.out.println(Helper.colorS("red", "⚠️ No author found with ID: " + id));
            }

        } catch (SQLException e) {
            ExceptionLogger.show(e,"❌ Problem fetching author with ID: " + id);
        }

        return null;
    }

}
