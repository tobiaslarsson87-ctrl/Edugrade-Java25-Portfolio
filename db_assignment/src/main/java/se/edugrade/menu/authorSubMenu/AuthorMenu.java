package se.edugrade.menu.authorSubMenu;

import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;

import static se.edugrade.menu.authorSubMenu.AddAndShowAuthors.showAuthors;

public class AuthorMenu {
    // Submeny Author, låter användaren göra olika Author-val.
    public static void call() {
        boolean stayInMenu = true;

        while (stayInMenu) {
            System.out.println(Helper.colorS("cyan", "\n=== 📚 Author Menu ==="));
            System.out.println(Helper.colorS("green", "[1] ") + "Show all authors");
            System.out.println(Helper.colorS("green", "[2] ") + "Add and show authors");
            System.out.println(Helper.colorS("green", "[3] ") + "Update author");
            System.out.println(Helper.colorS("green", "[4] ") + "Delete author");
            System.out.println(Helper.colorS("red", "[0] ") + "Back to previous menu");

            int choice = UserInput.getInt();

            switch (choice) {
                case 1 -> showAuthors();
                case 2 -> se.edugrade.menu.authorSubMenu.AddAndShowAuthors.call();
                case 3 -> UpdateAuthor.run();
                case 4 -> DeleteAuthor.run();
                case 0 -> {
                    System.out.println(Helper.colorS("yellow", "\n🔙 Returning to main menu..."));
                    stayInMenu = false;
                }
                default -> System.out.println(Helper.colorS("red", "❌ Invalid input. Please try again."));
            }
        }
    }
}
