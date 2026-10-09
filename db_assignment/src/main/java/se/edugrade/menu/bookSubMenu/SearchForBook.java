package se.edugrade.menu.bookSubMenu;

import se.edugrade.dao.BookDAO;
import se.edugrade.model.Book;

import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;

import java.util.ArrayList;
import java.util.List;

public class SearchForBook {
    public static void call() {
        BookDAO bookDAO = new BookDAO();
        boolean searching = true;
        while (searching) {
            System.out.println(Helper.colorS("green", "\n***********************"));
            System.out.println(Helper.colorS("yellow", "SEARCH FOR A BOOK TITLE"));
            System.out.println(Helper.colorS("green", "***********************"));
            System .out.println();

            String search = UserInput.getString().trim();

            //Lista för att spara alla matchande böcker
            List<Book> foundMatch = new ArrayList<>();

            //Går igenom alla böcker
            for (Book book : bookDAO.getAllBooks()) {
                if (book.getTitle().toLowerCase().contains(search.toLowerCase())) { //*
                    foundMatch.add(book);
                }
            }

            //Om inga böcker hittas
            if (foundMatch.isEmpty()) {
                System.out.println();
                System.out.println(Helper.colorS("red", "❌No books found."));
            }

            // Om en eller flera bäcker matchar sökning
            else {
                for (Book book : foundMatch) {
                    System.out.println();
                    System.out.print(Helper.colorS("green", "✅FOUND MATCH: "));
                    System.out.print(Helper.colorS("yellow", "TITLE: ") + book.getTitle() + " ");
                    System.out.println(Helper.colorS("yellow", "ISBN: ") + book.getIsbn() + " ");
                }
            }
            searching = Helper.subMenuExit();
        }
    }
}
