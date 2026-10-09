package se.edugrade.menu.loanSubMenu;

import se.edugrade.dao.LoanDAO;
import se.edugrade.menu.UserInput;
import se.edugrade.model.Loan;
import se.edugrade.utility.Helper;

import java.util.List;

public class DeleteLoan {
    public static void call (){
        boolean updateLoop = true;
        List<Loan> loans = LoanDAO.findAll();
        for (Loan loan : loans) {
            System.out.println(loan);

        }
        while (updateLoop) {
            System.out.print(Helper.colorS("blue", "[ID] "));
            System.out.print("Delete loan   ");
            System.out.print(Helper.colorS("yellow", "[0] "));
            System.out.print("Skip updating   \n");

            int loanid = UserInput.getInt();
            if (loanid == 0) {
                System.out.println(Helper.colorS("yellow", "❌ Skip choose Book!"));
            } else {
                Loan choose = null;
                for (Loan l : loans) {
                    if (loanid == l.getId()) {
                        choose = l;
                        break;
                    }
                    LoanDAO.delete(l.getId());

                }
                System.out.println(Helper.colorS("yellow", "Loan deleted " + choose));
                System.out.println(Helper.colorS("blue", "*************** "));
                LoanDAO.delete(choose.getId());
            }
            updateLoop = Helper.subMenuExit();
        }

        }
        }


