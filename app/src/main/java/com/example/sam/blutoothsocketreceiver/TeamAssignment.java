package com.example.sam.blutoothsocketreceiver;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TeamAssignment {

    public static File bluetoothDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/bluetooth");

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
