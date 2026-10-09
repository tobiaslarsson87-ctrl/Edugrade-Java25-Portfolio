package se.dsve.presentation.ui_utils;

/***
 * Simple ANSI-code getters
 */
public enum MyColor {
    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    MAGENTA("\u001B[35m"),
    CYAN("\u001B[36m");

    private final String code;

    MyColor(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
