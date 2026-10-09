package se.edugrade.menu;
import se.edugrade.dao.QueryDAO;
import se.edugrade.menu.authorSubMenu.AuthorMenu;

import se.edugrade.menu.bookSubMenu.BookMenu;
import se.edugrade.menu.loanSubMenu.LoanMenu;
import se.edugrade.menu.memberSubMenu.MemberMenu;
import se.edugrade.utility.Helper;

import java.util.List;

public class LibraryOverview {
    public static void showBooksWithAuthors() {
        QueryDAO dao = new QueryDAO();
        List<String> result = dao.getBooksWithAuthors();
        System.out.println(Helper.colorS("blue", "\n Books sorted by authors:\n"));
        for (String line : result) {
            System.out.println(line);
        }
    }
}
