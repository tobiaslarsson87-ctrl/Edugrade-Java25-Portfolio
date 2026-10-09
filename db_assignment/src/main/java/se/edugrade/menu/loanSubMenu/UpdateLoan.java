package se.edugrade.menu.loanSubMenu;

import se.edugrade.dao.LoanDAO;
import se.edugrade.menu.UserInput;
import se.edugrade.model.Loan;
import se.edugrade.utility.DatabaseConnection;
import se.edugrade.utility.Helper;

import java.sql.*;
import java.util.List;

public class UpdateLoan {
    public static void call() {
        List<Loan> loans = LoanDAO.findAll();
        for (Loan loan : loans) {
            System.out.println(loan);
        }
        boolean updateLoop = true;

        UserInput.paus();

        while (updateLoop) {
            System.out.print(Helper.colorS("blue", "[ID] "));
            System.out.print("Extend Loan   ");
            System.out.print(Helper.colorS("yellow", "[0] "));
            System.out.print("Skip updating   \n");

            int loanid = UserInput.getInt();
            if (loanid == 0) {
                System.out.println(Helper.colorS("yellow", "❌ Skip choose Book!"));
            } else {
                Loan extendLoan = null;
                for (Loan l : loans) {
                    if (loanid == l.getId()) {
                        extendLoan = l;
                        break;
                    }
                }
                System.out.println(Helper.colorS("yellow", "Selected Book: " + extendLoan));
                //LOGIK SÅ ANVÄNDARE FÅR SKRIVA IN NYTT DATUM
                System.out.println("Enter date yyyy-mm-dd%n");
               Date newReturnDate = Date.valueOf(UserInput.getString());

                LoanDAO.returndateUpdate(loanid, newReturnDate);
                // LOGGA UPDATERING TILL ANVÄNDARE
                System.out.println(Helper.colorS("yellow", " 📕Loan " + newReturnDate + " Updated ✅"));
            }
            updateLoop = Helper.subMenuExit();
        }
    }
}

