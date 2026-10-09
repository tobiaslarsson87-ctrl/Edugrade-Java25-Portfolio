package se.edugrade.menu;

import se.edugrade.utility.Helper;

import java.util.Scanner;

public class UserInput {
    private static Scanner sc = new Scanner(System.in);

    //Ta siffror från användare
    //*TOB*Lägger in parsing för egen räkning då jag krashar program hela tiden genom att trycka för snabbt
    public static int getInt(){
        while(true){
            System.out.print("Enter Number: ");
            try {
                int userInt = Integer.parseInt(sc.nextLine());
                System.out.println(""); // radbyte
                return userInt;
            }
            catch (NumberFormatException e) {
                System.out.println(Helper.colorS("red", "❌ Invalid Input. Enter a number.\n"));
            }
        }
    }

    //Ta text från användare
    public static String getString(){
        System.out.print("Enter Text: ");
        return sc.nextLine();
    }

    //Vänta på input från användare
    public static void paus(){
        System.out.print(Helper.colorS("yellow", "\n[PRESS ENTER]"));
        sc.nextLine();
        System.out.println(); //RADBYTE
    }
}
