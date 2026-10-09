package se.edugrade.dao;

import se.edugrade.model.Loan;
import se.edugrade.utility.DatabaseConnection;
import se.edugrade.utility.Helper;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public static boolean isbnExists(String isbn) {
        String sql = "SELECT COUNT(*) FROM book WHERE isbn = ?";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, isbn);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting book ISBN: " + e.getMessage());
        }
        return false;
    }

    private static Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public static List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT id, book_isbn , renter, loan_Date, return_Date FROM loan";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
              ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan(
                            rs.getInt("id"),
                            rs.getString("book_isbn"),
                            rs.getInt("renter"),
                            rs.getDate("loan_Date"),
                            rs.getDate("return_date")
                    );
                    loans.add(loan);
                }

            } catch(SQLException e){
                System.err.println("Fel vid findAll: " + e.getMessage());
            }
            return loans;
        }

    public static Loan insertLoan(Loan loan) {
        if (!isbnExists(loan.getBookISBN())) {
            System.out.println("Book ISBN " + loan.getBookISBN() + " does not exist in book table ");
            return null;
        }
        String sql = "INSERT INTO loan (book_ISBN, renter, loan_date, return_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, loan.getBookISBN());
            pstmt.setInt(2, loan.getRenter());
            pstmt.setDate(3, loan.getLoanDate());
            pstmt.setDate(4, loan.getReturnDate());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    loan.setId(rs.getInt(1));
                }
            }
            System.out.println(Helper.colorS("green", "\uD83D\uDC64 Added a new loan: "
                    + "ID: " + loan.getId()
                    + "Isbn: " + loan.getBookISBN()
                    + " Renter " + loan.getRenter()
                    + " Return date: " + loan.getReturnDate()
                    + " Loan Date: " + loan.getLoanDate()));
        } catch (SQLException e) {


            System.err.println("Fel med att skapa nya loans: " + e.getMessage());
        }
        return null;
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM loan WHERE renter = ?";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Fel vid delete: " + e.getMessage());
            return false;
        }
    }

    public static void returndateUpdate(int id, Date returnDate) {
        String returnDateSQL = "UPDATE loan SET return_date = ? WHERE id = ?";
        try(Connection connection=getConnection();
            PreparedStatement pstmt = connection.prepareStatement(returnDateSQL)){
            pstmt.setDate(1, returnDate);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e){
            System.err.println("Wrong: " + e.getMessage());
        }

    }

}
























