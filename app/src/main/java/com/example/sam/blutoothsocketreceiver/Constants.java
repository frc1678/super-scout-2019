package com.example.sam.blutoothsocketreceiver;


import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final Map<String, String> dataBases = new HashMap<>();
    public static final String dataBaseUrl = "https://scouting-2018-houston.firebaseio.com/";
    public static String teamOneNoteHolder = "";
    public static String teamTwoNoteHolder = "";
    public static String teamThreeNoteHolder = "";
    static {
        dataBases.put("https://scouting-2018-houston.firebaseio.com/", "AIzaSyC289_whaNh46_U9GTeZ2rPQsqaYTK0A3o");
        dataBases.put("https://1678-extreme-testing.firebaseio.com/", "lGufYCifprPw8p1fiVOs7rqYV3fswHHr9YLwiUWh");

    }

}
