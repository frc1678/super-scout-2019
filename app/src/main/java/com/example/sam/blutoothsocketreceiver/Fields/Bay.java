package com.example.sam.blutoothsocketreceiver.Fields;

public class Bay  {
    //Defines the three color preload options within the Cargo Ship: orange, yellow, and gray, and assigns them a number
    public static Integer yellow = -6108;
    public static Integer orange = -21492;
    public static Integer gray = -5658199;

    //Defines the three preload options (as Strings) within the Cargo Ship: orange, yellow, and gray/noValue
    public static String noValue = "noValue";
    public static String orangeValue = "orangeValue";
    public static String yellowValue = "yellowValue";

    //Creates error message
    public static String errorMessage = "Select a configuration for each bay!";

    //Creates function: isGray, which takes the integer color, checks if it is equal to gray, and then returns
    // true (if equal to gray) or false (if not equal to gray)
    public static Boolean isGray(Integer color) {
        String stringColor = String.valueOf(color);
        if (stringColor.equals(String.valueOf(gray))) {
            return true;
        }
        return false;
    }
    //Creates function: isYellow, which takes the integer color, checks if it is equal to yellow, and then returns
    // true (if equal to yellow) or false (if not equal to yellow)
    public static Boolean isYellow(Integer color) {
        String stringColor = String.valueOf(color);
        if (stringColor.equals(String.valueOf(yellow))) {
            return true;
        }
        return false;
    }
    //Creates function: isOrange, which takes the integer color, checks if it is equal to orange, and then returns
    // true (if equal to orange) or false (if not equal to orange)
    public static Boolean isOrange(Integer color) {
        String stringColor = String.valueOf(color);
        if (stringColor.equals(String.valueOf(orange))) {
            return true;
        }
        return false;
    }

}
