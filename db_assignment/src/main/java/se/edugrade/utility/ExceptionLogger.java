package se.edugrade.utility;

public class ExceptionLogger {
    public static void show(Exception e, String message) {
        System.out.println(Helper.colorS("red", "********************"));
        System.out.println("⚠️ AN ERROR OCCURRED");
        System.out.println(Helper.colorS("red", "********************"));
        System.out.println("ERROR TYPE: " + e.getClass().getSimpleName());
        System.out.println("DEV MESSAGE: " + message);
    }
}
