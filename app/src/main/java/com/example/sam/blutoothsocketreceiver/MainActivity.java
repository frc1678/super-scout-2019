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
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.sam.blutoothsocketreceiver.Fields.Bay;
import com.example.sam.blutoothsocketreceiver.Fields.FieldLayout;
import com.example.sam.blutoothsocketreceiver.Fields.LeftField;
import com.example.sam.blutoothsocketreceiver.firebase_classes.Match;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity {
    protected SuperScoutApplication app;
    Activity context;
    Boolean teamNumberOneNoShow = false;
    Boolean teamNumberTwoNoShow = false;
    Boolean teamNumberThreeNoShow = false;
    EditText numberOfMatch;
    EditText teamNumberOne;
    EditText teamNumberTwo;
    EditText teamNumberThree;
    String teamOne, teamTwo, teamThree;
    EditText searchBar;
    Boolean overrideOnBackPressed;
    TextView alliance;
    ListView listView;
    Boolean isRed = false;
    String leftViewColor;
    Integer matchNumber = 0;
    DatabaseReference dataBase;
    Boolean leftSideBoolean = true, rightSideBoolean = true, firstClick = true;
    //TODO: Why are these global?
    //String previousScore, previousFoul, previousAllianceSimple;
    //Boolean facedTheBoss = false, didAutoQuest = false;
    //Integer previousBoost = 0, previousLevitate = 0, previousForce = 0;

    final static String dataBaseUrl = Constants.dataBaseUrl;
    boolean isMute = false;
    boolean isOverriden;
    ToggleButton mute;
    ArrayAdapter<String> adapter;

    //THIS IS THE MASTER BRANCH

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
        teamNumberOne = (EditText) findViewById(R.id.teamOneNumber);
        teamNumberTwo = (EditText) findViewById(R.id.teamTwoNumber);
        teamNumberThree = (EditText) findViewById(R.id.teamThreeNumber);
        mute = (ToggleButton) findViewById(R.id.mute);
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
        listView = (ListView) findViewById(R.id.view_files_received);
        listView.setAdapter(adapter);
        updateListView();

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
            setTeamNumbers(teamOne, teamTwo, teamThree);
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
                } else if (teamNumberOne.getText().toString().equals("")) {
                    Toast.makeText(context, "Input team one number!", Toast.LENGTH_SHORT).show();
                } else if (teamNumberTwo.getText().toString().equals("")) {
                    Toast.makeText(context, "Input team two number!", Toast.LENGTH_SHORT).show();
                } else if (teamNumberThree.getText().toString().equals("")) {
                    Toast.makeText(context, "Input team three number!", Toast.LENGTH_SHORT).show();
                }
                else {
                    commitSharedPreferences();
                    commitOverrideTeamsSP();
                    Intent intent = new Intent(context, FieldSetupPage.class);
                    intent.putExtra("matchNumber", numberOfMatch.getText().toString());
                    intent.putExtra("teamNumberOne", teamNumberOne.getText().toString());
                    intent.putExtra("teamNumberTwo", teamNumberTwo.getText().toString());
                    intent.putExtra("teamNumberThree", teamNumberThree.getText().toString());
                    intent.putExtra("alliance", alliance.getText().toString());
                    intent.putExtra("dataBaseUrl", dataBaseUrl);
                    intent.putExtra("mute", isMute);
                    intent.putExtra("allianceColor", isRed);
                    intent.putExtra("leftViewColor", leftViewColor);
                    intent.putExtra("teamNumberOneNoShow", String.valueOf(teamNumberOneNoShow));
                    intent.putExtra("teamNumberTwoNoShow", String.valueOf(teamNumberTwoNoShow));
                    intent.putExtra("teamNumberThreeNoShow", String.valueOf(teamNumberThreeNoShow));
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
        } else if (id == R.id.noShowTeams) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final View noShowLayout = LayoutInflater.from(context).inflate(R.layout.noshowteams, null);
            final Button noShowTeamOne = (Button) noShowLayout.findViewById(R.id.noShowTeamOne);
            final Button noShowTeamTwo = (Button) noShowLayout.findViewById(R.id.noShowTeamTwo);
            final Button noShowTeamThree = (Button) noShowLayout.findViewById(R.id.noShowTeamThree);

            noShowTeamOne.setText(teamNumberOne.getText().toString());
            noShowTeamTwo.setText(teamNumberTwo.getText().toString());
            noShowTeamThree.setText(teamNumberThree.getText().toString());

            if (teamNumberOneNoShow) {
                noShowTeamOne.setBackgroundColor(ContextCompat.getColor(this, R.color.TeamNumberRed));
            } else {
                noShowTeamOne.setBackgroundColor(ContextCompat.getColor(this, R.color.LightGrey));
            }
            if (teamNumberTwoNoShow) {
                noShowTeamTwo.setBackgroundColor(ContextCompat.getColor(this, R.color.TeamNumberRed));
            } else {
                noShowTeamTwo.setBackgroundColor(ContextCompat.getColor(this, R.color.LightGrey));
            }
            if (teamNumberThreeNoShow) {
                noShowTeamThree.setBackgroundColor(ContextCompat.getColor(this, R.color.TeamNumberRed));
            } else {
                noShowTeamThree.setBackgroundColor(ContextCompat.getColor(this, R.color.LightGrey));
            }

            noShowTeamOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer buttonColor = ((ColorDrawable) noShowTeamOne.getBackground()).getColor();
                    if (String.valueOf(buttonColor).equals(FieldLayout.neutralColor)) {
                        noShowTeamOne.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed));
                    } else {
                        noShowTeamOne.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.LightGrey));
                    }

                }
            });
            noShowTeamTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer buttonColor = ((ColorDrawable) noShowTeamTwo.getBackground()).getColor();
                    if (String.valueOf(buttonColor).equals(FieldLayout.neutralColor)) {
                        noShowTeamTwo.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed));
                    } else {
                        noShowTeamTwo.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.LightGrey));
                    }

                }
            });
            noShowTeamThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer buttonColor = ((ColorDrawable) noShowTeamThree.getBackground()).getColor();
                    if (String.valueOf(buttonColor).equals(FieldLayout.neutralColor)) {
                        noShowTeamThree.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed));
                    } else {
                        noShowTeamThree.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.LightGrey));
                    }

                }
            });
            builder.setView(noShowLayout); builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Integer teamOneColor = ((ColorDrawable) noShowTeamOne.getBackground()).getColor();
                    Integer teamTwoColor = ((ColorDrawable) noShowTeamTwo.getBackground()).getColor();
                    Integer teamThreeColor = ((ColorDrawable) noShowTeamThree.getBackground()).getColor();

                    if (String.valueOf(teamOneColor).equals(String.valueOf(FieldLayout.causedColor))) {
                        teamNumberOneNoShow = true;
                    } else {
                        teamNumberOneNoShow = false;
                    }
                    if (String.valueOf(teamTwoColor).equals(String.valueOf(FieldLayout.causedColor))) {
                        teamNumberTwoNoShow = true;
                    } else {
                        teamNumberTwoNoShow = false;
                    }
                    if (String.valueOf(teamThreeColor).equals(String.valueOf(FieldLayout.causedColor))) {
                        teamNumberThreeNoShow = true;
                    } else {
                        teamNumberThreeNoShow = false;
                    }

                }
            }).show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void updateListView() {

        final EditText searchBar = (EditText)findViewById(R.id.searchEditText);
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

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence Register, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (searchBar.getText().toString().equals("")){
                    adapter.clear();
                    searchBar.setFocusable(false);
                    try {
                        for (File tmpFile : files) {
                            adapter.add(tmpFile.getName());
                        }
                    } catch(Exception JE) {
                        Log.e("json error", "failed to add tempfile to adapter");
                        toasts("Failed to show past matches.", true);
                    }
                    searchBar.setFocusableInTouchMode(true);
                    adapter.sort(new Comparator<String>() {
                        @Override
                        public int compare(String lhs, String rhs) {
                            File lhsFile = new File(dir, lhs);
                            File rhsFile = new File(dir, rhs);
                            Date lhsDate = new Date(lhsFile.lastModified());
                            Date rhsDate = new Date(rhsFile.lastModified());
                            return rhsDate.compareTo(lhsDate);
                        }
                    });
                }else{
                    for (int i = 0; i < adapter.getCount();){
                        if(adapter.getItem(i).startsWith((searchBar.getText().toString()).toUpperCase()) || adapter.getItem(i).contains((searchBar.getText().toString()).toUpperCase())){
                            i++;
                        }else{
                            adapter.remove(adapter.getItem(i));
                        }
                    }
                }
                adapter.sort(new Comparator<String>() {
                    @Override
                    public int compare(String lhs, String rhs) {
                        File lhsFile = new File(dir, lhs);
                        File rhsFile = new File(dir, rhs);
                        Date lhsDate = new Date(lhsFile.lastModified());
                        Date rhsDate = new Date(rhsFile.lastModified());
                        return rhsDate.compareTo(lhsDate);
                    }
                });
            }
        });

        adapter.notifyDataSetChanged();
    }
    //updates the team numbers in the front screen according to the match number and the alliance;
    private void updateUI() {
        try {
            if (FirebaseLists.matchesList.getKeys().contains(matchNumber.toString())) {
                Match match = FirebaseLists.matchesList.getFirebaseObjectByKey(matchNumber.toString());

                List<Integer> teamsOnAlliance = new ArrayList<>();
                teamsOnAlliance.addAll((isRed) ? match.redAllianceTeamNumbers : match.blueAllianceTeamNumbers);

                teamNumberOne.setText(teamsOnAlliance.get(0).toString());
                teamNumberTwo.setText(teamsOnAlliance.get(1).toString());
                teamNumberThree.setText(teamsOnAlliance.get(2).toString());
                teamNumberOne.setHint("Enter a team number");
                teamNumberTwo.setHint("Enter a team number");
                teamNumberThree.setHint("Enter a team number");
            } else {
                teamNumberOne.setHint("Not Available");
                teamNumberTwo.setHint("Not Available");
                teamNumberThree.setHint("Not Available");
                teamNumberOne.setText("");
                teamNumberTwo.setText("");
                teamNumberThree.setText("");
            }
        }catch(NullPointerException NPE){
            toasts("Teams not available", true);
        }

        alliance.setTextColor((isRed) ? Color.RED : Color.BLUE);
        alliance.setText((isRed) ? "Red Alliance" : "Blue Alliance");
    }

    public void commitSharedPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences("prefs", MODE_PRIVATE).edit();
        editor.putInt("match_number", matchNumber);
        editor.putBoolean("allianceColor", isRed);
        editor.commit();
    }
    public void commitOverrideTeamsSP() {
        SharedPreferences.Editor editor = getSharedPreferences("override_teams", MODE_PRIVATE).edit();
        editor.putString("teamOne", String.valueOf(teamNumberOne.getText()));
        editor.putString("teamTwo", String.valueOf(teamNumberTwo.getText()));
        editor.putString("teamThree",String.valueOf(teamNumberThree.getText()));
        editor.putString("teamOneNoShow", String.valueOf(teamNumberOneNoShow));
        editor.putString("teamTwoNoShow", String.valueOf(teamNumberTwoNoShow));
        editor.putString("teamThreeNoShow",String.valueOf(teamNumberThreeNoShow));
        editor.putInt("matchNumber", Integer.valueOf(numberOfMatch.getText().toString()));
        editor.commit();
    }
    public void getOverrideTeamsSP() {
        SharedPreferences prefs = getSharedPreferences("override_teams", MODE_PRIVATE);
        teamOne = prefs.getString("teamOne", "");
        teamTwo = prefs.getString("teamTwo", "");
        teamThree = prefs.getString("teamThree", "");
        teamNumberOneNoShow = convertToBool(prefs.getString("teamOneNoShow","false"));
        teamNumberTwoNoShow = convertToBool(prefs.getString("teamTwoNoShow","false"));
        teamNumberThreeNoShow = convertToBool(prefs.getString("teamThreeNoShow","false"));
        matchNumber = prefs.getInt("matchNumber",0);
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
        teamNumberOne.setFocusableInTouchMode(true);
        teamNumberTwo.setFocusableInTouchMode(true);
        teamNumberThree.setFocusableInTouchMode(true);
        isOverriden = true;
    }

    public void disenableEditTextEditing() {

        numberOfMatch.setFocusable(false);
        teamNumberOne.setFocusable(false);
        teamNumberTwo.setFocusable(false);
        teamNumberThree.setFocusable(false);
        isOverriden = false;
    }

    public void setTeamNumbers(String teamOne, String teamTwo, String teamThree) {
        teamNumberOne.setText(teamOne);
        teamNumberTwo.setText(teamTwo);
        teamNumberThree.setText(teamThree);
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
            Log.e("isRedd",isRed.toString() + "" );
            isRed = prefs.getBoolean("allianceColor", false);
	        Log.e("isReddd",isRed.toString() + "" );
        }
        if (!backToHome.hasExtra("mute")) {
            mute.setChecked(false);
        } else if (backToHome.hasExtra("mute")) {
            mute.setChecked(true);
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

    public void initializeColorConceptLeft(final Button buttonSide) {
        buttonSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onclickcc","left");
                if (leftSideBoolean) {
                    buttonSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Bloo));leftSideBoolean = false;
                } else if (!leftSideBoolean) {
                    buttonSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed)); leftSideBoolean = true;
                }}});
    }
    public void initializeColorConceptRight(final Button buttonSide) {
        buttonSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onclickcc","left");
                if (rightSideBoolean) {
                    buttonSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Bloo));rightSideBoolean = false;
                } else if (!rightSideBoolean) {
                    buttonSide.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed)); rightSideBoolean = true;
                }}});
    }
    public void setPrimaryFieldLayout(Button left, Button right) {
        right.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Bloo));
        left.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.TeamNumberRed));


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
