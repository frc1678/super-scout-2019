package com.example.sam.blutoothsocketreceiver.Fields;

public class Bay  {

    public static Integer yellow = -6108;
    public static Integer orange = -21492;
    public static Integer gray = -5658199;

    public static String noValue = "noValue";
    public static String orangeValue = "orangeValue";
    public static String yellowValue = "yellowValue";

    public static String errorMessage = "Select a configuration for each bay!";

    public static Boolean isGray(Integer color) {
        String stringColor = String.valueOf(color);
        if (stringColor.equals(String.valueOf(gray))) {
            return true;
        }
        return false;
    }
    public static Boolean isYellow(Integer color) {
        String stringColor = String.valueOf(color);
        if (stringColor.equals(String.valueOf(yellow))) {
            return true;
        }
        return false;
    }
    public static Boolean isOrange(Integer color) {
        String stringColor = String.valueOf(color);
        if (stringColor.equals(String.valueOf(orange))) {
            return true;
        }
        return false;
    }

}
