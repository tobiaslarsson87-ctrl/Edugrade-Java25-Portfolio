package se.edugrade.utility;

public class Colors {
    /*
    FULLT EXEMPEL
    -------------
    final String C = Colors.rgb(255,0,0);
        final String X = Colors.reset();
        System.out.println(C + "Hello World!" + X);
     */
    public static String rgb(int red, int green, int blue){
        int r = Math.clamp(red, 0, 255);
        int g = Math.clamp(green, 0, 255);
        int b = Math.clamp(blue, 0, 255);

        StringBuilder sb = new StringBuilder();
        sb.append("\u001B[38;2;");
        sb.append(r + ";" + g + ";" + b + "m");
        return sb.toString();
    }
    public static String reset(){
        return "\u001B[0m";
    }
}
