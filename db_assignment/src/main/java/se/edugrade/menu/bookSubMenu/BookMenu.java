package se.edugrade.menu.bookSubMenu;
import se.edugrade.menu.UserInput;
import se.edugrade.menu.authorSubMenu.AddAndShowAuthors;
import se.edugrade.utility.Helper;


public class BookMenu {

    public static void call() {
        boolean bookMenuLoop = true;

        while (bookMenuLoop) {
            System.out.println(Helper.colorS("yellow", "******************************"));
            System.out.println(Helper.colorS("blue", "       BOOK MENU    "));
            System.out.println(Helper.colorS("yellow", "******************************"));
            System.out.println(Helper.colorS("green","[1] Show all books"));
            System.out.println(Helper.colorS("green","[2] Add new book"));
            System.out.println(Helper.colorS("green","[3] Search for a book"));
            System.out.println(Helper.colorS("green","[4] Delete a book"));
            System.out.println(Helper.colorS("green","[5] Book reports"));
            System.out.println(Helper.colorS("red", "[0] Back to previous menu"));
            System.out.print("Select an option: ");

            switch (UserInput.getInt()) {
                case 1 -> ShowAllBooks.call();
                case 2 -> AddNewBook.call();
                case 3 -> SearchForBook.call();
                case 4 -> DeleteBook.call();
                case 5 -> BookReports.call();
                case 0 -> {
                    bookMenuLoop = false;
                    System.out.println("\nReturning to previous menu...");
                }
                default -> {
                    System.out.println("\n❌ Invalid input. Please try again.");
                    UserInput.paus();
                }
            }
        }
    }
}
