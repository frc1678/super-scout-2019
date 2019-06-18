package com.example.sam.blutoothsocketreceiver.Utilities;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TeamAssignment {
    /* TeamAssignment is a class apart of the assignment of scouts according to an assignment file
       pre-downloaded on each of the Super Scout's tablets before each competition
    */

    public static File bluetoothDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/bluetooth");

    //Returns a JSONObject type from a given file's data
    public static JSONObject toJSONObject(String data){
        if(data.isEmpty()){
            return new JSONObject();
        }
        try{
            return new JSONObject(data);
        }catch(JSONException je){
            je.printStackTrace();
            return new JSONObject();
        }
    }

    public static int StringToInt(String s){
        return Integer.parseInt(s);
    }

    public static void makeToast(Context context, String text, int size){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        setToastSize(toast, size);
        toast.show();
    }

    public static void setToastSize(Toast toast, int size){
        ViewGroup vGroup = (ViewGroup) toast.getView();
        TextView toastTxt = (TextView) vGroup.getChildAt(0);
        toastTxt.setTextSize(size);
    }


    //Returns the data within the file path given as an argument.
    public static String retrieveSDCardFile(String pFileName) {
        Log.e("Retrieve Called", pFileName);

        if(!bluetoothDir.exists()){
            bluetoothDir.mkdir();
        }
        final File[] files = bluetoothDir.listFiles();

        Log.e("Null Check 1", files.toString());

        try{
            if(!(files == null)){
                for(File aFile: files){
                    return readAssignmentFile(aFile.getPath());
                }
            }
        }
        catch(NullPointerException npe){
            Log.e("NPE", "getting file path");
        }
        return null;
    }

    //Returns the data of the assignment file given the name of the file
    public static String readAssignmentFile(String pPathName) {
        BufferedReader bReader;
        try{
            bReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pPathName))));
        }
        catch (IOException IOE) {
            Log.e("File Error", "Failed to open file");
            return null;

        }
        String dataOfFile = "";
        String buf;
        try {
            while ((buf = bReader.readLine()) != null) {
                dataOfFile = dataOfFile.concat(buf + "\n");
            }
        }
        catch (IOException IOE) {
            Log.e("File Error", "Failed to read from file");
            return null;
        }
        Log.i("fileData", dataOfFile);

        return dataOfFile;
    }
}
