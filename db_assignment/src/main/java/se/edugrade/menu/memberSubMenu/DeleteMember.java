package se.edugrade.menu.memberSubMenu;

import se.edugrade.dao.MemberDAO;
import se.edugrade.model.Member;
import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;

import java.util.List;

public class DeleteMember {
    public static void call() {
        MemberDAO memberDAO = new MemberDAO();
        boolean deleteLoop = true;

        while (deleteLoop) {
        List<Member> memberList = memberDAO.getAllMembers();

        for (Member member : memberList) {
            System.out.println(member);
        }

        System.out.print(Helper.colorS("red", "[ID] "));
        System.out.print("Delete Member   ");
        System.out.print(Helper.colorS("green", "[0] "));
        System.out.print("Skip Deletion   \n");

        int choice = UserInput.getInt();

        if (choice == 0) {
            System.out.println(Helper.colorS("green", "✅ No member deleted."));
        }
        else {
            memberDAO.deleteMember(choice);

            //Logga full member lista efter
            System.out.println("\n****************");
            System.out.println(Helper.colorS("green","\uD83D\uDD04 UPDATED LIST"));
            System.out.println("****************\n");

            //Dubbelkolla så att det går till databas
            List<Member> updatedMemberList = memberDAO.getAllMembers();
            for (Member member : updatedMemberList) {
                System.out.println(member + "\n");

            }
        }

        deleteLoop = Helper.subMenuExit();

        }
    }
}
