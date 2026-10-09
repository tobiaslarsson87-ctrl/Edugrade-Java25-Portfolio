package se.edugrade.utility;
import java.util.IllegalFormatException;
import java.util.Scanner;

public class UserInput {
    private static Scanner sc = new Scanner(System.in);

    //Accepterar bara siffror och returnerar en int.
    public static int getInt(){
        while(true){
            System.out.print("Enter number: ");
            String input = sc.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(e + "Input can only be numbers!");
            }
        }
    }
    //Accepterar bara siffror och returnerar en Long.
    public static Long getLong(){
        while(true){
            System.out.print("Enter number: ");
            String input = sc.nextLine();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println(e + "Input can only be numbers!");
            }
        }
    }
    //Accepterar bara en sträng som inte är tom
    public static String getString(){
        while(true){
            System.out.print("Enter text: ");
            String input = sc.nextLine();
            if(!input.trim().isEmpty()){
                return input;
            }
            else{
                System.out.println("Can't be empty!");
            }
        }
    }
    //Hjälpmetod för att kolla giltigt URL format.
    private static boolean isValidUrl(String string){
        return string.trim().matches("^www.[A-Za-zÅÄÖåäö]+\\.[A-Za-zÅÄÖåäö]+$");
    }
    //Accepterar bara en sträng i formatet www.fritext.fritext EXEMPEL: www.hej.se
    public static String getUrl(){
        while(true){
            System.out.print("Type an URL www.xxxxx.xxxxx: ");
            String input = sc.nextLine();
            if(isValidUrl(input)){
                return input;
            }
            else{
                System.out.println("Must be in the following format: www.freetext.freetext");
            }
        }
    }
    //Hjälpmetod för att kolla giltigt HashTag format.
    private static boolean isValidHashTag(String string){
        return string.trim().matches("^#(.+)$");
    }
    //Accepterar bara en sträng som börjar med ett #.
    public static String getHashTag(){
        while(true){
            System.out.print("Write a HashTag, Start with #: ");
            String input = sc.nextLine();
            if(isValidHashTag(input)){
                return input;
            }
            else{
                System.out.println("A HashTag must start with a #!");
            }
        }
    }
    //Accepterar bara en tom sträng
    public static String empty(){
        while(true){
            System.out.print("[PRESS KEY]");
            String input = sc.nextLine();
            if(input.trim().isEmpty()){
                return input;
            }
            else{
                System.out.println("Must be empty!");
            }
        }
    }
    //Stäng Scanner vi program avslut
    public static void close(){
        sc.close();
    }
}
