package se.edugrade.menu.bookSubMenu;

import se.edugrade.dao.BookDAO;
import se.edugrade.model.Book;

import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;

import java.util.List;

public class ShowAllBooks {
    //Huvudmeny för att visa alla böcker från databasen
    public static void call() {
        BookDAO bookDAO = new BookDAO();
        List<Book> books = bookDAO.getAllBooks();

        System.out.println(Helper.colorS("yellow", "************************"));
        System.out.println(Helper.colorS("blue", "       BOOK LIST         "));
        System.out.println(Helper.colorS("yellow", "************************"));

        //Om det inte finns några böcker i databasen
        if (books.isEmpty()) {
            System.out.println(Helper.colorS("red", "❌No books found."));
        } else {

            //Skriver ut rubriker för kolumner
            System.out.println(Helper.colorS("blue", "ID    Titel                                ISBN            År     AuthorID    AuthorName"));
            System.out.println(Helper.colorS("yellow", "---------------------------------------------------------------------------------------------"));


            // Loopar igenom och skriver ut alla böcker
            for (Book b : books) {
                System.out.println(
                        Helper.colorS("green",
                                b.getId() + "    " +
                                        b.getTitle() + getSpacing(b.getTitle()) +
                                        b.getIsbn() + "   " +
                                        b.getPubYear() + "     " +
                                        b.getAuthorId() + "     " +
                                        (b.getAuthorName() != null ? b.getAuthorName() : "Unknown") // visar författarens namn om det finns, annars "Unknown"

                        )
                );
            }
        }

            System.out.println(Helper.colorS("yellow", "---------------------------------------------------------------------------------------------"));
            System.out.println(Helper.colorS("green", "Press [Enter] to go back to previous menu"));
            UserInput.paus();
        }


        // Lägger till lite extra mellanrum efter titel för snyggae justering i tabellen
        private static String getSpacing(String title) {
            int spaceCount = 40 - title.length();
            if (spaceCount < 1) spaceCount = 1;
            return " ".repeat(spaceCount);
    }
}
