package se.edugrade.menu.loanSubMenu;

import se.edugrade.dao.LoanDAO;
import se.edugrade.menu.UserInput;
import se.edugrade.model.Loan;

import java.util.List;

public class ShowAllLoans {
    public static void call(){
        LoanDAO  loanDAO = new LoanDAO();
        List< Loan> loanList = loanDAO.findAll();
        System.out.println("");
        for (Loan loan : loanList) {
            System.out.println(loan);
        }
        UserInput.paus();
        }
    }

