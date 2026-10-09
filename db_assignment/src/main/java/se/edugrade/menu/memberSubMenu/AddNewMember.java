package se.edugrade.menu.memberSubMenu;

import se.edugrade.dao.MemberDAO;
import se.edugrade.model.Member;
import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;

import java.util.List;

public class AddNewMember {
    public static void call() {
        boolean addingMember = true;
        while (addingMember) {
            MemberDAO memberDAO = new MemberDAO();

            System.out.print(Helper.colorS("green", "[1] "));
            System.out.print("Add Member   ");
            System.out.print(Helper.colorS("red", "[0] "));
            System.out.print("Skip Addition   \n");

            int choice = UserInput.getInt();

            if (choice == 0) {
                System.out.println(Helper.colorS("red", "❌ No member added."));
            }
            else {
                System.out.println(Helper.colorS("yellow", "******************* "));
                System.out.println(Helper.colorS("yellow", "NAME OF NEW MEMBER: "));
                System.out.println(Helper.colorS("yellow", "******************* "));
                String name = UserInput.getString();

                Member newMember = new Member(name);
                memberDAO.insertMember(newMember);

                //Logga full member lista efter
                System.out.println("\n****************");
                System.out.println(Helper.colorS("green", "\uD83D\uDD04 UPDATED LIST"));
                System.out.println("****************\n");
                //Dubbelkolla så att det går till databas
                List<Member> updatedMemberList = memberDAO.getAllMembers();
                for (Member member : updatedMemberList) {
                    System.out.println(member + "\n");
                }
            }

            addingMember = Helper.subMenuExit();

        }
    }
}
