package com.example.sam.blutoothsocketreceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.example.sam.blutoothsocketreceiver.Fields.FieldLayout;
import com.example.sam.blutoothsocketreceiver.Utilities.TeamAssignment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.jcodec.common.DictionaryCompressor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {
    protected SuperScoutApplication app;
    Activity context;
    EditText numberOfMatch;
    EditText allianceTeamOne, allianceTeamTwo, allianceTeamThree;
    EditText opposingAllianceTeamOne, opposingAllianceTeamTwo, opposingAllianceTeamThree;
    String teamOne, teamTwo, teamThree, opposingTeamOne, opposingTeamTwo, opposingTeamThree;
    Boolean overrideOnBackPressed;
    TextView alliance;
    ListView listView;
    Boolean isRed = false;
    String leftViewColor;
    Integer matchNumber = 0;
    DatabaseReference dataBase;
    Boolean firstClick = true;
    Button resendButton;

    boolean isMute = false;
    boolean isOverriden;
    ToggleButton mute;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        context = this;
        isOverriden = false; overrideOnBackPressed = false;

        Constants.teamOneNoteHolder = "";
        Constants.teamTwoNoteHolder = "";
        Constants.teamThreeNoteHolder = "";

        numberOfMatch = (EditText) findViewById(R.id.matchNumber);
        allianceTeamOne = (EditText) findViewById(R.id.allianceTeamOne);
        allianceTeamTwo = (EditText) findViewById(R.id.allianceTeamTwo);
        allianceTeamThree = (EditText) findViewById(R.id.allianceTeamThree);
        opposingAllianceTeamOne = (EditText) findViewById(R.id.opponentTeamOne);
        opposingAllianceTeamTwo = (EditText) findViewById(R.id.opponentTeamTwo);
        opposingAllianceTeamThree = (EditText) findViewById(R.id.opponentTeamThree);
        alliance = (TextView) findViewById(R.id.allianceName);
        dataBase = FirebaseDatabase.getInstance().getReference();
        //If there was an intent to MainActivity (qr code -> mainact), get the alliance and new match num
        //else, get the previous team numbers which clear on override onbackpressed
        getLeftViewColor();
        if (getIntent().getExtras()!=null) {
            checkPreviousMatchNumAndAlliance();
        } else {
            getOverrideTeamsSP();
            isOverriden = true;
            overrideOnBackPressed = true;
        }

        updateUI();
        if (leftViewColor == null) {
            numberOfMatch.setText("0");
            numberOfMatch.setTextColor(Color.RED);
            numberOfMatch.setTextSize(28);
        } else {
            numberOfMatch.setText(matchNumber.toString());
        }
        matchNumber = Integer.parseInt(numberOfMatch.getText().toString());
        disenableEditTextEditing();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        resendButton = (Button) findViewById(R.id.resendButton);

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.view_files_received);
        listView = (ListView) dialog.findViewById(R.id.filesListView);
        listView.setAdapter(adapter);
        dialog.setTitle("Resend");
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateListView();
                dialog.show();
            }
        });


        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    updateUI();
                } catch (NullPointerException NPE) {
                    toasts("Teams not available", true);
                }
            }
        }, new IntentFilter("matches_updated"));

        //Change team numbers as the user changes the match number
        changeTeamsByMatchName();
        commitSharedPreferences();
        clearTeamSP();

        if (overrideOnBackPressed) {
            setTeamNumbers(teamOne, teamTwo, teamThree, opposingTeamOne, opposingTeamTwo, opposingTeamThree);
        }
        listenForResendClick();
        //listLongClick();
    }
    public void catClicked(View view){
        if(mute.isChecked()){
            //Don't Do anything
            isMute = true;
        }else {
            isMute = false;
            int randNum = (int) (Math.random() * 3);
            playSound(randNum);
        }
    }
    public void playSound(int playTrak){
        if (playTrak == 0){
            MediaPlayer mp = MediaPlayer.create(this, R.raw.catsound);
            mp.start();
        }else if(playTrak == 1){
            MediaPlayer mp = MediaPlayer.create(this, R.raw.catsound2);
            mp.start();
        }else if(playTrak == 2){
            MediaPlayer mp = MediaPlayer.create(this, R.raw.dog);
            mp.start();
        }else if(playTrak == 3){
            MediaPlayer mp = MediaPlayer.create(this, R.raw.kittenmeow);
            mp.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.changeAlliance) {
            isRed = !isRed;
            SuperScoutApplication.isRed = true;
            commitSharedPreferences();
            updateUI();
        }
        if (id == R.id.scout) {
                if (numberOfMatch.getText().toString().equals("")) {
                    Toast.makeText(context, "Input match name!", Toast.LENGTH_SHORT).show();
                } else if (allianceTeamOne.getText().toString().equals("")) {
                    Toast.makeText(context, "Input alliance team one number!", Toast.LENGTH_SHORT).show();
                } else if (allianceTeamTwo.getText().toString().equals("")) {
                    Toast.makeText(context, "Input alliance team two number!", Toast.LENGTH_SHORT).show();
                } else if (allianceTeamThree.getText().toString().equals("")) {
                    Toast.makeText(context, "Input alliance team three number!", Toast.LENGTH_SHORT).show();
                } else if (opposingAllianceTeamOne.getText().toString().equals("")) {
                    Toast.makeText(context, "Input opponent team one number!", Toast.LENGTH_SHORT).show();
                } else if (opposingAllianceTeamTwo.getText().toString().equals("")) {
                    Toast.makeText(context, "Input opponent team two number!", Toast.LENGTH_SHORT).show();
                } else if (opposingAllianceTeamThree.getText().toString().equals("")) {
                    Toast.makeText(context, "Input opponent team three number!", Toast.LENGTH_SHORT).show();
                } else {
                    commitSharedPreferences();
                    commitOverrideTeamsSP();
                    Intent intent = new Intent(context, DuringMatchScouting.class);
                    intent.putExtra("matchNumber", numberOfMatch.getText().toString());
                    intent.putExtra("allianceTeamOne", allianceTeamOne.getText().toString());
                    intent.putExtra("allianceTeamTwo", allianceTeamTwo.getText().toString());
                    intent.putExtra("allianceTeamThree", allianceTeamThree.getText().toString());
                    intent.putExtra("opponentTeamOne", opposingAllianceTeamOne.getText().toString());
                    intent.putExtra("opponentTeamTwo", opposingAllianceTeamTwo.getText().toString());
                    intent.putExtra("opponentTeamThree", opposingAllianceTeamThree.getText().toString());
                    intent.putExtra("alliance", alliance.getText().toString());
                    startActivity(intent);
                }

        } else if (id == R.id.action_override) {
            if (item.getTitle().toString().equals("Override Match and Team Number")) {
                enableEditTextEditing();
                item.setTitle("Automate");
            } else if (item.getTitle().toString().equals("Automate")) {
                View view = context.getCurrentFocus();
                updateUI();
                commitSharedPreferences();
                disenableEditTextEditing();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                item.setTitle("Override Match and Team Number");
            }

        } else if (id == R.id.fieldLayout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final View fieldLayout = LayoutInflater.from(context).inflate(R.layout.field_layout, null);
            final Button leftSide = (Button) fieldLayout.findViewById(R.id.leftSide);
            final Button rightSide = (Button) fieldLayout.findViewById(R.id.rightSide);

            if (leftViewColor.equals(FieldLayout.blue)) {
                leftSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Bloo));
                rightSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed));
            } else if (leftViewColor.equals(FieldLayout.red)) {
                rightSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Bloo));
                leftSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed));
            }
            initializeColorConcept(rightSide, leftSide);

            builder.setView(fieldLayout); builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Integer leftSideColor = ((ColorDrawable)leftSide.getBackground()).getColor();
                    if (String.valueOf(leftSideColor).equals(String.valueOf(FieldLayout.Blue))) {
                        leftViewColor = FieldLayout.blue;
                    } else if (String.valueOf(leftSideColor).equals(String.valueOf(FieldLayout.Red))) {
                        leftViewColor = FieldLayout.red;
                    } else {
                        Log.e("this is when we cry","");
                    }

                }
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateListView() {

        final File dir;
        dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Super_scout_data");
        if (!dir.mkdir()) {
            Log.i("File Info", "Failed to make Directory. Unimportant");
        }
        final File[] files = dir.listFiles();
        adapter.clear();
        try {
            for (File tmpFile : files) {
                adapter.add(tmpFile.getName());
            }
        } catch(Exception JE) {
            Log.e("json error", "failed to add tempfile to adapter");
            toasts("Failed to show past matches.", true);
        }
    }

    //updates the team numbers in the front screen according to the match number and the alliance;
    public void updateUI() {
        if (matchNumber >= 0) {
            String matchesKey = "matches";
            Log.e("match",String.valueOf(matchesKey));
            String matchNumberKey = String.valueOf(matchNumber);
            Log.e("match number", String.valueOf(matchNumberKey));

            try {
                JSONObject backupData = new JSONObject(TeamAssignment.retrieveSDCardFile("assignments.txt"));
                backupData = backupData.getJSONObject(matchesKey).getJSONObject(matchNumberKey);

                //start
                if(isRed) {
                    allianceTeamOne.setText(String.valueOf(backupData.getJSONObject("1").getInt("number")));
                    allianceTeamTwo.setText(String.valueOf(backupData.getJSONObject("2").getInt("number")));
                    allianceTeamThree.setText(String.valueOf(backupData.getJSONObject("3").getInt("number")));
                    opposingAllianceTeamOne.setText(String.valueOf(backupData.getJSONObject("4").getInt("number")));
                    opposingAllianceTeamTwo.setText(String.valueOf(backupData.getJSONObject("5").getInt("number")));
                    opposingAllianceTeamThree.setText(String.valueOf(backupData.getJSONObject("6").getInt("number")));
                } else if(!isRed) {
                    allianceTeamOne.setText(String.valueOf(backupData.getJSONObject("4").getInt("number")));
                    allianceTeamTwo.setText(String.valueOf(backupData.getJSONObject("5").getInt("number")));
                    allianceTeamThree.setText(String.valueOf(backupData.getJSONObject("6").getInt("number")));
                    opposingAllianceTeamOne.setText(String.valueOf(backupData.getJSONObject("1").getInt("number")));
                    opposingAllianceTeamTwo.setText(String.valueOf(backupData.getJSONObject("2").getInt("number")));
                    opposingAllianceTeamThree.setText(String.valueOf(backupData.getJSONObject("3").getInt("number")));
                }
            }
            catch(JSONException JE) {
                JE.printStackTrace();
            }

            alliance.setTextColor((isRed) ? Color.RED : Color.BLUE);
            alliance.setText((isRed) ? "Red Alliance" : "Blue Alliance");
        }
    }

    public void commitSharedPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        editor.putInt("match_number", matchNumber);
        editor.putBoolean("allianceColor", isRed);
        editor.commit();
    }
    public void commitOverrideTeamsSP() {
        SharedPreferences.Editor editor = getSharedPreferences("override_teams", MODE_PRIVATE).edit();
        editor.putString("allianceTeamOne", String.valueOf(allianceTeamOne.getText()));
        editor.putString("allianceTeamTwo", String.valueOf(allianceTeamTwo.getText()));
        editor.putString("allianceTeamThree", String.valueOf(allianceTeamThree.getText()));
        editor.putString("opponentTeamOne", String.valueOf(opposingAllianceTeamOne.getText()));
        editor.putString("opponentTeamTwo", String.valueOf(opposingAllianceTeamTwo.getText()));
        editor.putString("opponentTeamThree", String.valueOf(opposingAllianceTeamThree.getText()));
        editor.putInt("matchNumber", Integer.valueOf(numberOfMatch.getText().toString()));
        editor.putString("isRed",String.valueOf(isRed));
        editor.commit();
    }
    public void getOverrideTeamsSP() {
        SharedPreferences prefs = getSharedPreferences("override_teams", MODE_PRIVATE);
        teamOne = prefs.getString("allianceTeamOne", "");
        teamTwo = prefs.getString("allianceTeamTwo", "");
        teamThree = prefs.getString("allianceTeamThree", "");
        opposingTeamOne = prefs.getString("opponentTeamOne", "");
        opposingTeamTwo = prefs.getString("opponentTeamTwo", "");
        opposingTeamThree = prefs.getString("opponentTeamThree", "");
        matchNumber = prefs.getInt("matchNumber",0);
        isRed = convertToBool(prefs.getString("isRed","false"));
    }

    //changes the team numbers while the user changes the match number
    public void changeTeamsByMatchName() {
        EditText numberOfMatch = (EditText) findViewById(R.id.matchNumber);
        numberOfMatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    matchNumber = Integer.parseInt(s.toString());
                } catch (NumberFormatException NFE) {
                    matchNumber = 0;
                }
                updateUI();
            }
        });
    }

    public void enableEditTextEditing() {

        numberOfMatch.setFocusableInTouchMode(true);
        allianceTeamOne.setFocusableInTouchMode(true);
        allianceTeamTwo.setFocusableInTouchMode(true);
        allianceTeamThree.setFocusableInTouchMode(true);
        opposingAllianceTeamOne.setFocusableInTouchMode(true);
        opposingAllianceTeamTwo.setFocusableInTouchMode(true);
        opposingAllianceTeamThree.setFocusableInTouchMode(true);
        isOverriden = true;
    }

    public void disenableEditTextEditing() {

        numberOfMatch.setFocusable(false);
        allianceTeamOne.setFocusableInTouchMode(false);
        allianceTeamTwo.setFocusableInTouchMode(false);
        allianceTeamThree.setFocusableInTouchMode(false);
        opposingAllianceTeamOne.setFocusableInTouchMode(false);
        opposingAllianceTeamTwo.setFocusableInTouchMode(false);
        opposingAllianceTeamThree.setFocusableInTouchMode(false);
        isOverriden = false;
    }

    public void setTeamNumbers(String teamOne, String teamTwo, String teamThree, String opponentOne, String opponentTwo, String opponentThree) {
        allianceTeamOne.setText(teamOne);
        allianceTeamTwo.setText(teamTwo);
        allianceTeamThree.setText(teamThree);
        opposingAllianceTeamOne.setText(opponentOne);
        opposingAllianceTeamTwo.setText(opponentTwo);
        opposingAllianceTeamThree.setText(opponentThree);
    }
    //reads the data of the clicked file
    public String readFile(String name) {
        BufferedReader file;
        try {
            file = new BufferedReader(new InputStreamReader(new FileInputStream(
                    new File(name))));
        } catch (IOException ioe) {
            Toast.makeText(context, "Failed To Open File", Toast.LENGTH_LONG).show();
            return null;
        }
        String dataOfFile = "";
        String buf;
        try {
            while ((buf = file.readLine()) != null) {
                dataOfFile = dataOfFile.concat(buf + "\n");
            }
        } catch (IOException ioe) {
            Toast.makeText(context, "Failed To Read From File", Toast.LENGTH_LONG).show();
            return null;
        }
        return dataOfFile;
    }


    public void toasts(final String message, boolean isLongMessage) {
        if (!isLongMessage) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void checkPreviousMatchNumAndAlliance(){
        Intent backToHome = getIntent();
        if (backToHome.hasExtra("number")) {
            matchNumber = Integer.parseInt(backToHome.getExtras().getString("number")) + 1;
        } else {
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            matchNumber = prefs.getInt("match_number", 1);
        }
        if (backToHome.hasExtra("shouldBeRed")) {
            isRed = getIntent().getBooleanExtra("shouldBeRed", false);
        } else {
            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            isRed = prefs.getBoolean("allianceColor", false);
        }
    }

    public void clearTeamSP() {
        SharedPreferences override_teams = context.getSharedPreferences("override_teams", Context.MODE_PRIVATE);
        override_teams.edit().clear().commit();
    }

    public void getLeftViewColor() {
        Intent backToHome = getIntent();
        if (backToHome.hasExtra("leftViewColor")){
            leftViewColor = backToHome.getExtras().getString("leftViewColor");
            firstClick = false;
        } else {
            leftViewColor = "blue";
        }
    }

    public void listenForResendClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                name = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Super_scout_data/" + name;
                final String fileName = name;
                String content = readFile(fileName);
                displayQR(content);
            }
        });
    }

    public void displayQR(String qrCode){
        try {
            //setting size of qr code
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallestDimension = width < height ? width : height;
            //setting parameters for qr code
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            createQRCode(qrCode, charset, hintMap, smallestDimension, smallestDimension);
        } catch (Exception ex) {
            Log.e("QrGenerate",ex.getMessage());
        }
    }
    public void createQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth){

        try {
            //generating qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //setting bitmap to image view
            AlertDialog.Builder builder = new AlertDialog.Builder(context); //TODO: Add an edit alertdialog for fieldsetup and notes (maybe-check)
            builder.setTitle("QR CODE");
            final View QrView = LayoutInflater.from(context).inflate(R.layout.qr_regenerate, null);
            ((ImageView) QrView.findViewById(R.id.QRCode_Regenerate)).setImageBitmap(null);
            ((ImageView) QrView.findViewById(R.id.QRCode_Regenerate)).setImageBitmap(bitmap);
            builder.setView(QrView);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }catch (Exception er){
            Log.e("QrGenerate",er.getMessage());
        }
    }
    public void initializeColorConcept(final Button rightSide, final Button leftSide) {
        rightSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer rightSideColor = ((ColorDrawable)rightSide.getBackground()).getColor();
                if (String.valueOf(rightSideColor).equals(String.valueOf(FieldLayout.Blue))) {
                    rightSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed));
                    leftSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Bloo));
                } else if (String.valueOf(rightSideColor).equals(String.valueOf(FieldLayout.Red))) {
                    leftSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed));
                    rightSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Bloo));
                }
            }
        });
        leftSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer leftSideColor = ((ColorDrawable)leftSide.getBackground()).getColor();
                if (String.valueOf(leftSideColor).equals(String.valueOf(FieldLayout.Blue))) {
                    leftSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed));
                    rightSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Bloo));
                } else if (String.valueOf(leftSideColor).equals(String.valueOf(FieldLayout.Red))) {
                    rightSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed));
                    leftSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Bloo));
                }
            }
        });
    }

    public Boolean convertToBool(String bool) {
        if (bool.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

}
