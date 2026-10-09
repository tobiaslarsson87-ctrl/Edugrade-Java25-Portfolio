package se.edugrade.menu.memberSubMenu;
import se.edugrade.dao.MemberDAO;
import se.edugrade.menu.UserInput;
import se.edugrade.model.Member;
import se.edugrade.utility.Helper;
import java.util.List;

public class UpdateMember {
    public static void call() {
        MemberDAO memberDAO = new MemberDAO();
        boolean updateLoop = true;

        //Presentera Listan av Members med ID + Namn
        List<Member> members = memberDAO.getAllMembers();
        for (Member member : members) {
            System.out.println(member);
        }
        UserInput.paus();

        while (updateLoop) {
            System.out.print(Helper.colorS("blue", "[ID] "));
            System.out.print("Update member   ");
            System.out.print(Helper.colorS("yellow", "[0] "));
            System.out.print("Skip updating   \n");

            int choice = UserInput.getInt();
            if (choice == 0) {
                System.out.println(Helper.colorS("yellow", "❌ Update skipped"));
            }
            else {
                Member selection = null;
                for (Member member : members) {
                    if (choice == member.getId()) {
                        selection = member;
                        break;
                    }
                }
                System.out.println(Helper.colorS("yellow", "Selected Member: " + selection));
                System.out.println(Helper.colorS("blue", "*************** "));
                System.out.println(Helper.colorS("blue", "CHANGE NAME TO: "));
                System.out.println(Helper.colorS("blue", "*************** "));

                selection.setName(UserInput.getString());
                memberDAO.updateMember(selection);

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
            updateLoop = Helper.subMenuExit();
        }
    }
}

