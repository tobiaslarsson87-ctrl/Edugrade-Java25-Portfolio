package se.edugrade.menu.memberSubMenu;
import se.edugrade.dao.MemberDAO;
import se.edugrade.model.Member;
import se.edugrade.menu.UserInput;
import java.util.List;

public class ShowAllMembers {
    public static void call() {
        MemberDAO memberDAO = new MemberDAO();
        List<Member> memberList = memberDAO.getAllMembers();
        System.out.println(""); // LINEBREAK
        for (Member member : memberList) {
            System.out.println(member);
        }
        UserInput.paus();
    }
}
