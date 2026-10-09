package se.edugrade.menu.memberSubMenu;

import se.edugrade.utility.Helper;
import se.edugrade.menu.UserInput;

public class MemberMenu {
    public static void call() {
        boolean memberMenuLoop = true;
        while (memberMenuLoop) {

            System.out.println(Helper.colorS("yellow", "******************"));
            System.out.println(Helper.colorS("yellow", "    MEMBER MENU   "));
            System.out.println(Helper.colorS("yellow", "******************"));
            System.out.print(Helper.colorS("yellow", " [1] "));
            System.out.println("\uD83D\uDC65 Show All Members");
            System.out.print(Helper.colorS("yellow", " [2] "));
            System.out.println("➕ Add Member");
            System.out.print(Helper.colorS("yellow", " [3] "));
            System.out.println("❌ Delete Member");
            System.out.print(Helper.colorS("yellow", " [4] "));
            System.out.println("\uD83D\uDD04 Update Member");
            System.out.print(Helper.colorS("red", " [0] "));
            System.out.println("\uD83D\uDD19 Return to previous menu");

            switch (UserInput.getInt()) {

                case 1 -> { ShowAllMembers.call(); }
                case 2 -> { AddNewMember.call(); }
                case 3 -> { DeleteMember.call(); }
                case 4 -> { UpdateMember.call(); }
                case 0 -> { memberMenuLoop = false; }
                default ->  System.out.println(Helper.colorS("red", "❌Invalid Input"));

            }
        }
    }
}
