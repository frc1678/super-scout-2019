package com.example.sam.blutoothsocketreceiver;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.firebase.client.Firebase;

public class MainActivity extends ActionBarActivity {
    Activity context;
    TextView changing;
    PrintWriter file;
    EditText name;
    EditText foul;
    EditText alliance_score;
    String user_name;
    String foul_points;
    String alliance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        context = this;
        accept_loop loop = new accept_loop(context);
        loop.start();
        Firebase.setAndroidContext(this);
        changing = (TextView)findViewById(R.id.text);
        //Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase myFirebaseRef = new Firebase("https://popping-torch-4659.firebaseio.com");
        myFirebaseRef.keepSynced(true);
        name = (EditText)findViewById(R.id.userName);
        foul = (EditText)findViewById(R.id.foul_Points);
        alliance_score = (EditText)findViewById(R.id.scoreEdit);
        file = null;
    }

    public void deleteAllFiles(View view){
        ListView listView = (ListView)findViewById(R.id.view_files_received);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        adapter.add("");
        listView.setAdapter(adapter);
    }
    public void sendData(View view) {
        try {
            File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Super_scout_data");
            dir.mkdir();
            //can delete when doing the actual thing
            file = new PrintWriter(new FileOutputStream(new File(dir, "Send-Data.txt" + new SimpleDateFormat("MM-dd-yyyy-H:mm:ss").format(new Date()))));
        } catch (IOException IOE) {
            Log.e("File error", "Failed to open File");
            return;
        }

        user_name = name.getText().toString();
        foul_points = foul.getText().toString();
        alliance = alliance_score.getText().toString();
        if (user_name == "") {
            Toast.makeText(context, "You forgot Name input!", Toast.LENGTH_SHORT).show();
        } else if (foul_points == "") {
            Toast.makeText(context, "You forgot foul points!", Toast.LENGTH_SHORT).show();
        } else if (alliance == "") {
            Toast.makeText(context, "You forgot alliance score!", Toast.LENGTH_SHORT).show();
        } else {

            file.println(user_name + "\n" + foul_points + "\n" + alliance);
            Toast.makeText(context, "Sent to file",  Toast.LENGTH_SHORT).show();
            updateList();
            Firebase myFirebaseRef = new Firebase("https://popping-torch-4659.firebaseio.com");
            myFirebaseRef.child("Super Scout Data").child("Data").setValue(user_name + "\n" + foul_points + "\n" + alliance);
            System.out.println("sent to firebase");
            name.setText("");
            foul.setText("");
            alliance_score.setText("");
            System.out.println("cleared edittext");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void updateList(){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Super_scout_data");
                if (!dir.mkdir()) {
                    Log.i("File Info", "Failed to make Directory. Unimportant");
                }
                File[] files = dir.listFiles();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
                for (File tmpFile : files) {
                    adapter.add(tmpFile.getName() + new SimpleDateFormat("MM-dd-yyyy-H:mm:ss").format(new Date()));
                }
                ListView listView = (ListView)context.findViewById(R.id.view_files_received);
                listView.setAdapter(adapter);
            }
        });



    }
}

