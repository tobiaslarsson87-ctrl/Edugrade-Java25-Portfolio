package se.edugrade.menu.bookSubMenu;

import se.edugrade.dao.AuthorDAO;
import se.edugrade.dao.BookDAO;
import se.edugrade.menu.authorSubMenu.AddAndShowAuthors;
import se.edugrade.model.Author;
import se.edugrade.model.Book;

import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;
import se.edugrade.menu.authorSubMenu.AddAndShowAuthors;

import java.util.List; // ✨ tillagt för att kunna visa alla författare

public class AddNewBook {
    public static void call() {


        BookDAO bookDAO = new BookDAO();
        AuthorDAO authorDAO = new AuthorDAO(); // tillagt för att hämta författare

        System.out.println(Helper.colorS("yellow", "***********************************"));
        System.out.println(Helper.colorS("blue", "           BOOK LIST         "));
        System.out.println(Helper.colorS("yellow", "***********************************"));


        System.out.print(Helper.colorS("blue", "Title: "));
        String title = UserInput.getString();

        System.out.print(Helper.colorS("blue", "ISBN: "));
        String isbn = UserInput.getString();

        System.out.print(Helper.colorS("blue","Publication Year: "));
        int pubYear = UserInput.getInt();


        System.out.println(Helper.colorS("yellow", "\nAvailable Authors:"));

        System.out.println(Helper.colorS("green", "0 - Add new Author: "));


        // Hämtar alla författare från databasen och visar dem i listan
        List<Author> authors = authorDAO.getAllAuthors();

        for (Author a : authors) {
            System.out.println(Helper.colorS("green", a.getId() + " - " + a.getName()));
        }

        System.out.print(Helper.colorS("blue", "Enter Author ID (choose from list above): "));
        int authorId = UserInput.getInt();


        //Om användare väljer 0, öppnas add new author i author submeny.
        if (authorId == 0) {
            Author newAuthor = AddAndShowAuthors.call();
            authorId = newAuthor.getId();
            System.out.println(Helper.colorS("green", "Author ID: " + newAuthor.getId()));
            System.out.println(Helper.colorS("green", "Author ID: " + authorId));
        }


        //Skapa ett nytt bokobjekt baserat på användarens inmatning
        Book newBook = new Book(0, title, isbn, pubYear, authorId);

        int rows = bookDAO.insertBook(newBook);

        if (rows > 0) {
            System.out.println(Helper.colorS("green", "\n✅ Book added successfully!"));
            System.out.println(Helper.colorS("yellow", "Title: ") + title);
            System.out.println(Helper.colorS("yellow", "ISBN: ") + isbn);
            System.out.println(Helper.colorS("yellow", "Publication Year: ") + pubYear);
            System.out.println(Helper.colorS("yellow", "Author ID: ") + authorId); //
        }
        else {
            System.out.println(Helper.colorS("red", "\n❌ Something went wrong — the book is not saved."));
        }

        System.out.println(Helper.colorS("yellow", "\nPress [Enter] to return to menu..."));
        UserInput.paus();
    }
}
