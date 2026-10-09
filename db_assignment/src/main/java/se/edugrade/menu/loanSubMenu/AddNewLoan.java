package se.edugrade.menu.loanSubMenu;

import se.edugrade.dao.BookDAO;
import se.edugrade.dao.LoanDAO;
import se.edugrade.dao.MemberDAO;
import se.edugrade.menu.UserInput;
import se.edugrade.model.Book;
import se.edugrade.model.Loan;
import se.edugrade.model.Member;
import se.edugrade.utility.Helper;

import java.sql.*;
import java.util.List;

public class AddNewLoan {
    public static void call() {

        LoanDAO loanDAO = new LoanDAO();
        System.out.println(" 📕Rent a book ");
        boolean updateLoop = true;

        MemberDAO memberDAO = new MemberDAO();
        List<Member> members = memberDAO.getAllMembers();
        for (Member member : members) {
            System.out.println(member);
        }
        UserInput.paus();

        while (updateLoop) {
            System.out.print(Helper.colorS("blue", "[ID] "));
            System.out.print("Choose member   ");
            System.out.print(Helper.colorS("yellow", "[0] "));
            System.out.print("Skip renting  \n");

            int choice = UserInput.getInt();
            if (choice == 0) {
                System.out.println(Helper.colorS("yellow", "❌ No member selected."));
            } else {
                Member selection = null;
                for (Member member : members) {
                    if (choice == member.getId()) {
                        selection = member;
                        break;
                    }
                }
                System.out.println(Helper.colorS("yellow", "Selected Member: " + selection));
                System.out.println(Helper.colorS("blue", "*************** "));


                BookDAO bookDAO = new BookDAO();
                List<Book> books = bookDAO.getAllBooks();
                for (Book book : books) {
                    System.out.println(book);
                }

                    System.out.print(Helper.colorS("blue", "[ID] "));
                    System.out.print("Choose book   ");
                    System.out.print(Helper.colorS("yellow", "[0] "));
                    System.out.print("Skip renting  \n");

                    int bookid = UserInput.getInt();
                    if (bookid == 0) {
                        System.out.println(Helper.colorS("yellow", "❌ Skip choose Book!"));
                    } else {
                        Book choose = null;
                        for (Book book1 : books) {
                            if (bookid == book1.getId()) {
                                choose = book1;
                                break;
                            }
                        }


                        Loan loanForMember = new Loan(choose, selection);
                        loanDAO.insertLoan(loanForMember);

                        System.out.println("Loan added successfully for " +  selection);


                        System.out.println("\n*************");
                        System.out.println(Helper.colorS("pink", "LOANS"));
                        System.out.println("*************\n");

                        List<Loan> updateLoanList = loanDAO.findAll();
                        for (Loan loan : updateLoanList) {
                            System.out.println(loan + "\n");
                        }
                    }
                    updateLoop = Helper.subMenuExit();
                }
            }
        }
    }



