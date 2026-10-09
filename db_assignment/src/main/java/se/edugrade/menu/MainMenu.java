package se.edugrade.menu;

import se.edugrade.menu.authorSubMenu.AuthorMenu;
import se.edugrade.menu.bookSubMenu.BookMenu;
import se.edugrade.menu.loanSubMenu.LoanMenu;
import se.edugrade.menu.memberSubMenu.MemberMenu;
import se.edugrade.utility.Helper;

public class MainMenu {

    //Metod visar val för huvudmeny
    public static void call() {
        boolean mainMenu = true;
        while (mainMenu) {
            System.out.println(Helper.colorS("yellow", "***********************"));

            System.out.print(Helper.colorS("yellow", "LIBRARY "));
            System.out.println(Helper.colorS("blue", "MENU "));
            System.out.println(Helper.colorS("yellow", "***********************"));
            System.out.println(Helper.colorS("green", "[1]") + " Author-menu");
            System.out.println(Helper.colorS("green", "[2]") + " Book-menu");
            System.out.println(Helper.colorS("green", "[3]") + " Loan-menu");
            System.out.println(Helper.colorS("green", "[4]") + " Member-menu");
            System.out.println(Helper.colorS("green", "[5]") + " Show all books & authors in the library");
            System.out.println(Helper.colorS("red", "[0]") + " QUIT");
            System.out.println("************************\n");

            switch (UserInput.getInt()) {
                case 1 -> { AuthorMenu.call(); }
                case 2 -> { BookMenu.call(); }
                case 3 -> { LoanMenu.call(); }
                case 4 -> { MemberMenu.call(); }
                case 5 -> { LibraryOverview.showBooksWithAuthors();  }
                case 0 -> { mainMenu = false; }
                default -> { System.out.println("\n❌Invalid Input."); UserInput.paus(); }
            }
        }
    }
}
