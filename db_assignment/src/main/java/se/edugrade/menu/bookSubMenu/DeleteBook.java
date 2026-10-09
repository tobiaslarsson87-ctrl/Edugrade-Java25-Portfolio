package se.edugrade.menu.bookSubMenu;

import se.edugrade.dao.BookDAO;
import se.edugrade.model.Book;
import se.edugrade.utility.Helper;
import se.edugrade.utility.ExceptionLogger;
import se.edugrade.menu.UserInput;

import java.util.List;

public class DeleteBook {
    public static void call() {
        BookDAO bookDAO = new BookDAO();

        System.out.println(Helper.colorS("yellow", "******************************"));
        System.out.println(Helper.colorS("blue", "       📚 DELETE BOOK ⚠️      "));
        System.out.println(Helper.colorS("yellow", "******************************"));

        try {

            List<Book> books = bookDAO.getAllBooks();

            if (books.isEmpty()) {
                System.out.println(Helper.colorS("red", "\n❌ No books found in database."));
                System.out.println(Helper.colorS("yellow", "\nPress [Enter] to return to book menu..."));
                UserInput.paus();
                return;
            }


            System.out.println(Helper.colorS("yellow", "\nAvailable books:"));
            for (Book b : books) {
                System.out.println(Helper.colorS("green",
                        b.getId() + " - " + b.getTitle() + " - " + b.getAuthorName() + " (" + b.getIsbn() + ")"));
            }

            System.out.println(Helper.colorS("blue", "\nEnter book ID to delete: "));
            int bookID = UserInput.getInt();

            // Kontrollera att boken finns
            Book bookToDelete = bookDAO.getBookById(bookID);
            if (bookToDelete == null) {
                System.out.println(Helper.colorS("red", "\n❌ No book found with ID: " + bookID + "."));
                System.out.println(Helper.colorS("yellow", "\nPress [Enter] to return to book menu"));
                UserInput.paus();
                return;
            }

            // Bekräftelse
            System.out.println(Helper.colorS("yellow", "Are you sure you want to delete this book?"));
            System.out.println(Helper.colorS("green", "Title: ") + bookToDelete.getTitle());
            System.out.print(Helper.colorS("blue", "\nType 'yes' to confirm: "));
            String confirmDelete = UserInput.getString();

            if (confirmDelete.equalsIgnoreCase("yes")) {
                int rows = bookDAO.deleteBookById(bookID);

                if (rows > 0) {
                    System.out.println(Helper.colorS("green", "\n✅ Book deleted successfully!"));

                    // Visa uppdaterad lista
                    System.out.println(Helper.colorS("yellow", "\n📘 Updated Book List:"));
                    List<Book> updatedBooks = bookDAO.getAllBooks();

                    if (updatedBooks.isEmpty()) {
                        System.out.println(Helper.colorS("red", "❌ No remaining books in the list."));
                    } else {
                        for (Book b : updatedBooks) {
                            System.out.println(Helper.colorS("green",
                                    b.getId() + " - " + b.getTitle() + " - " + b.getAuthorName() + " (" + b.getIsbn() + ")"));
                        }
                    }

                } else {
                    System.out.println(Helper.colorS("red", "\n❌ Failed to delete book!"));
                }

            } else {
                System.out.println(Helper.colorS("yellow", "\nCancelled delete."));
            }

        } catch (Exception e) {
            ExceptionLogger.show(e, "❌ Unexpected error occurred while deleting book.");
        }

        System.out.println(Helper.colorS("yellow", "\nPress [Enter] to return to book menu."));
        UserInput.paus();
    }
}
