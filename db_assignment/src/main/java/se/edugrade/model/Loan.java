package se.edugrade.model;

import java.sql.Date;
import java.time.LocalDate;

public class Loan {
    private int id;
    private String bookISBN;
    private int renter;
    private Date loanDate;
    private Date returnDate;

    public Loan(int id, String bookISBN, int renter, Date loanDate, Date returnDate) {
        this.id = id;
        this.bookISBN = bookISBN;
        this.renter = renter;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Loan(int renter, String bookISBN) {
        this.bookISBN = bookISBN;
        this.renter = renter;
        this.loanDate = Date.valueOf(today);
        this.returnDate = Date.valueOf(today.plusMonths(1));
    }
    public Loan(Book book, Member member) {
        this.bookISBN = book.getIsbn();
        this.renter = member.getId();
        this.loanDate = Date.valueOf(today);
        this.returnDate = Date.valueOf(today.plusMonths(1));
    }
    LocalDate today = LocalDate.now();

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public int getRenter() {
        return renter;
    }

    public void setRenter(int renter) {
        this.renter = renter;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return String.format(" ✅Loan [ID%d, 📕 BookISBN'%s', 🪪renter %d, ⏰ loanDate %s, 🔔returnDate %s]", id, bookISBN, renter, loanDate,  returnDate);
    }



}
