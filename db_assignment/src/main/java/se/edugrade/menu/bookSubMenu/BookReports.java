package se.edugrade.menu.bookSubMenu;

import se.edugrade.dao.QueryDAO;
import se.edugrade.model.Book;

import se.edugrade.utility.ExceptionLogger;
import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BookReports {
    //Visar olika databasanrop med mer avancerade queries
    public static void call() {
        QueryDAO queryDAO = new QueryDAO();
        boolean reportsloop = true;


        while (reportsloop) {
            System.out.println(Helper.colorS("yellow", "**************************************"));
            System.out.println(Helper.colorS("blue", "            📚 BOOK REPORTS 📊           "));
            System.out.println(Helper.colorS("yellow", "**************************************"));
            System.out.println(Helper.colorS("green", "[1] Show books with authors"));
            System.out.println(Helper.colorS("green", "[2] Show book count per author"));
            System.out.println(Helper.colorS("green", "[3] Show books by Swedish authors"));
            System.out.println(Helper.colorS("green", "[4] Show books published after a specific year"));
            System.out.println(Helper.colorS("green", "[5] Show books published before a specific year"));
            System.out.println(Helper.colorS("green", "[0] Back to previous menu"));
            System.out.print(Helper.colorS("blue", "Select an option: "));

            int choice = UserInput.getInt();

            switch (choice) {
                // Lista alla böcker tillsammas med sina författare
                case 1 -> {
                    System.out.println(Helper.colorS("yellow", "\n📚 Books with Authors"));
                    queryDAO.getBooksWithAuthors().forEach(System.out::println);
                }
                // Visa antal böcker per författare
                case 2 -> {
                    System.out.println(Helper.colorS("yellow", "\n📖 Book count per Author"));
                    Map<String, Integer> counts = queryDAO.getBookCountPerAuthor();
                    counts.forEach((name, count) ->
                            System.out.println(Helper.colorS("green", name + ": " + count + " books"))
                    );
                }
                //Visa böcker skrivna av svenska författare
                case 3 -> {
                    System.out.println(Helper.colorS("yellow", "\n🇸🇪 Books by Swedish Authors"));
                    List<Book> swedishBooks = queryDAO.getBooksBySwedishAuthors();
                    swedishBooks.forEach(System.out::println);
                }
                //Visa böcker publicerade efter ett visst år
                case 4 -> {
                    System.out.print(Helper.colorS("blue", "Enter year: "));
                    try {
                        int year = UserInput.getInt();

                        List<Book> booksAfter = queryDAO.getBooksAfterPubYear(year);

                        if (booksAfter == null || booksAfter.isEmpty()) {
                            System.out.println(Helper.colorS("red", "❌ No books found published after " + year + "."));
                        } else {
                            System.out.println(Helper.colorS("yellow", "\n📚 Books published after " + year + ":"));
                            booksAfter.forEach(System.out::println);
                        }

                    } catch (NumberFormatException e) {
                        ExceptionLogger.show(e, "Invalid input for publication year.");
                    } catch (SQLException e) {
                        ExceptionLogger.show(e, "Database error while fetching books after year.");
                    }
                }

                // Visa böcker publicerade före ett visst år
                case 5 -> {
                    System.out.print(Helper.colorS("blue", "Enter year: "));
                    try {
                        int year = UserInput.getInt();

                        List<Book> booksBefore = queryDAO.getBooksBeforePubYear(year);

                        if (booksBefore == null || booksBefore.isEmpty()) {
                            System.out.println(Helper.colorS("red", "❌ No books found published before " + year + "."));
                        } else {
                            System.out.println(Helper.colorS("yellow", "\n📚 Books published before " + year + ":"));
                            booksBefore.forEach(System.out::println);
                        }

                    } catch (NumberFormatException e) {
                        ExceptionLogger.show(e, "Invalid input for publication year.");
                    } catch (SQLException e) {
                        ExceptionLogger.show(e, "Database error while fetching books before year.");
                    }
                }
                case 0 -> reportsloop = false;
                default -> System.out.println(Helper.colorS("red", "❌ Invalid input."));
            }

            if (reportsloop) UserInput.paus();
        }
    }
}
