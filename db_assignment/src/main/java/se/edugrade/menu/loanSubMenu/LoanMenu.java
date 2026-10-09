package se.edugrade.menu.loanSubMenu;

import se.edugrade.menu.UserInput;
import se.edugrade.utility.Helper;

public class LoanMenu {
    public static void call() {


        boolean loanmenuLoop= true;

        while (loanmenuLoop) {

            System.out.println("********************");
            System.out.println("     Loan Menu      ");
            System.out.println("********************");
            System.out.print(Helper.colorS("yellow", " [1] "));
            System.out.println("📕Show All Loans");
            System.out.print(Helper.colorS("yellow", " [2] "));
            System.out.println("➕ Make a loan");
            System.out.print(Helper.colorS("yellow", " [3] "));
            System.out.println("🔁 Extend Loan");
            System.out.print(Helper.colorS("yellow", " [4] "));
            System.out.println("❌ Delete Loan");
            System.out.print(Helper.colorS("red", " [0] "));
            System.out.println("\uD83D\uDD19 Return to previous menu");

            switch (UserInput.getInt()) {
                case 1 -> { ShowAllLoans.call(); }
                case 2 -> { AddNewLoan.call(); }
                case 3 -> { UpdateLoan.call(); }
                case 4 -> { DeleteLoan.call(); }
                case 0 -> { loanmenuLoop= false; }

                default -> { System.out.println("\n❌Invalid Input."); UserInput.paus(); }


            }
        }
    }
    }

