package com.example.sam.blutoothsocketreceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jcodec.common.DictionaryCompressor;
import org.jcodec.common.RunLength;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FinalDataPoints extends ActionBarActivity {
    String numberOfMatch;
    String teamNumberOne;
    String teamNumberTwo;
    String teamNumberThree;
    String teamOneNotes;
    String teamTwoNotes;
    String teamThreeNotes;
    String alliance;
    String allianceSimple;
    String dataBaseUrl;
    String allianceScoreData, allianceFoulData;
    String leftViewColor;
    TextView finalScore;
    EditText allianceScore, allianceFoul;
    JSONObject superExternalData;
    ArrayList<String> teamOneDataName;
    ArrayList<String> teamOneDataScore;
    ArrayList<String> teamTwoDataName;
    ArrayList<String> teamTwoDataScore;
    ArrayList<String> teamThreeDataName;
    ArrayList<String> teamThreeDataScore;
    Boolean isMute;
    File dir;
    PrintWriter file;
    DatabaseReference firebaseRef;
    Intent intent;
    boolean hasRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finaldatapoints);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        intent = getIntent();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        superExternalData = new JSONObject();
        getExtrasForFinalData();
        firebaseRef = FirebaseDatabase.getInstance().getReference();
        allianceScore = (EditText) findViewById(R.id.finalScoreEditText);
        allianceFoul = (EditText) findViewById(R.id.finalFoulEditText);
        //
        //facedTheBoss = (Switch) findViewById(R.id.didHabClimb);
        //facedTheBoss.setChecked(intent.getExtras().getBoolean("facedTheBoss")); //todo update
        //completedAutoQuest = (Switch) findViewById(R.id.didRocketRP);
        //completedAutoQuest.setChecked(intent.getExtras().getBoolean("completedAutoQuest"));
        //
        finalScore = (TextView)findViewById(R.id.finalScoreTextView);
        allianceScore.setCursorVisible(false);

        if(alliance.equals("blue")){
            finalScore.setTextColor(Color.BLUE);
            alliance = "Blue Alliance";
        }else if(alliance.equals("red")){
            finalScore.setTextColor(Color.RED);
            alliance = "Red Alliance";
        }

        allianceSimple = alliance.substring(0,1).toLowerCase() + alliance.substring(1,alliance.indexOf(" "));

        allianceScore.setText(allianceScoreData);
        allianceFoul.setText(allianceFoulData);
        dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Super_scout_data");

        hasRun = false;
    }

    @Override
    public void onBackPressed(){
        final Activity activity = this;
        new AlertDialog.Builder(this)
                .setTitle("WARNING!")
                .setMessage("GOING BACK WILL CAUSE LOSS OF DATA")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.Submit) {
            final Activity context = this;
            int score;
            int foul;
            try {
                score = Integer.parseInt(allianceScore.getText().toString());
                foul = Integer.parseInt(allianceFoul.getText().toString());
            } catch (NumberFormatException nfe) {
                Toast.makeText(this, "Invalid inputs", Toast.LENGTH_LONG).show();
                return false;
            } catch (NullPointerException npe) {
                Toast.makeText(this, "Enter a score and foul", Toast.LENGTH_LONG).show();
                return false;
            }

            updateNotes();
            Intent QrDisplay = new Intent(context, QrDisplay.class);
            QrDisplay.putExtra("matchNumber", numberOfMatch);
            QrDisplay.putExtra("alliance", alliance);
            QrDisplay.putExtra("teamNumberOne", teamNumberOne);
            QrDisplay.putExtra("teamNumberTwo", teamNumberTwo);
            QrDisplay.putExtra("teamNumberThree", teamNumberThree);

            QrDisplay.putExtra("superNotesOne", teamOneNotes);
            QrDisplay.putExtra("superNotesTwo", teamTwoNotes);
            QrDisplay.putExtra("superNotesThree", teamThreeNotes);
            QrDisplay.putExtra("score", allianceScore.getText().toString());
            QrDisplay.putExtra("foul", allianceFoul.getText().toString());


            QrDisplay.putStringArrayListExtra("teamOneDataName", teamOneDataName);
            QrDisplay.putStringArrayListExtra("teamOneDataScore", teamOneDataScore);
            QrDisplay.putStringArrayListExtra("teamTwoDataName", teamTwoDataName);
            QrDisplay.putStringArrayListExtra("teamTwoDataScore", teamTwoDataScore);
            QrDisplay.putStringArrayListExtra("teamThreeDataName", teamThreeDataName);
            QrDisplay.putStringArrayListExtra("teamThreeDataScore", teamThreeDataScore);

            QrDisplay.putExtra("leftViewColor", leftViewColor);

            QrDisplay.putExtra("isMute", isMute);
            startActivity(QrDisplay);
        }

        if(id == R.id.finalSuperNotes){
            final Activity context = this;
            Intent finalNotesIntent = new Intent(context, finalNotes.class);
            finalNotesIntent.putExtra("teamNumOne", teamNumberOne);
            finalNotesIntent.putExtra("teamNumTwo", teamNumberTwo);
            finalNotesIntent.putExtra("teamNumThree", teamNumberThree);
            updateNotes();
            finalNotesIntent.putExtra("teamOneNotes", teamOneNotes); //TODO: Make sure notes are saved on return & save notes to teamOneNotes.
            finalNotesIntent.putExtra("teamTwoNotes", teamTwoNotes);
            finalNotesIntent.putExtra("teamThreeNotes", teamThreeNotes);
            finalNotesIntent.putExtra("qualNum", numberOfMatch);

            startActivity(finalNotesIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public String reformatDataNames(String dataName) {
        String reformattedDataName = "";
        if(dataName.equals("Good Decisions") || dataName.equals("Bad Decisions")){
            reformattedDataName = "num" + dataName.replace(" ", "");
        } else if(dataName.equals("superNotes")) {
            reformattedDataName = dataName;
        } else {
            reformattedDataName = "rank" + dataName.replace(" ", "");
        }
        return reformattedDataName;
    }

    public void getExtrasForFinalData(){
        teamOneNotes = intent.getExtras().getString("teamOneNotes");
        teamTwoNotes = intent.getExtras().getString("teamTwoNotes");
        teamThreeNotes = intent.getExtras().getString("teamThreeNotes");

        numberOfMatch = intent.getExtras().getString("matchNumber");
        teamNumberOne = intent.getExtras().getString("teamNumberOne");
        teamNumberTwo = intent.getExtras().getString("teamNumberTwo");
        teamNumberThree = intent.getExtras().getString("teamNumberThree");
        alliance = intent.getExtras().getString("alliance");

        teamOneDataName = intent.getStringArrayListExtra("dataNameOne");
        teamOneDataScore = intent.getStringArrayListExtra("ranksOfOne");
        teamTwoDataName = intent.getStringArrayListExtra("dataNameTwo");
        teamTwoDataScore = intent.getStringArrayListExtra("ranksOfTwo");
        teamThreeDataName = intent.getStringArrayListExtra("dataNameThree");
        teamThreeDataScore = intent.getStringArrayListExtra("ranksOfThree");

        dataBaseUrl = intent.getExtras().getString("dataBaseUrl");
        allianceScoreData = intent.getExtras().getString("allianceScore");
        allianceFoulData = intent.getExtras().getString("allianceFoul");

        leftViewColor = intent.getExtras().getString("leftViewColor");
    }

    public void sendAfterMatchData(){ //TODO: Replace 'hard-coded' red abd blue with a variable (ex: alliance + "Score")
        //TODO: Prevent submission of vault data lower than ForPowerup
        JSONObject allianceTeams = new JSONObject();
        int teamIntOne = Integer.parseInt(teamNumberOne);
        int teamIntTwo = Integer.parseInt(teamNumberTwo);
        int teamIntThree = Integer.parseInt(teamNumberThree);

        try {
            allianceTeams.put("0", teamIntOne);
            allianceTeams.put("1", teamIntTwo);
            allianceTeams.put("2", teamIntThree);
        } catch(JSONException JE) {
            Log.e("JSONException", "Failed to make allianceTeams");
        }
    }

    private void updateNotes() {
        if(!Constants.teamOneNoteHolder.equals("")) {
            teamOneNotes = Constants.teamOneNoteHolder;
        }
        if(!Constants.teamTwoNoteHolder.equals("")) {
            teamTwoNotes = Constants.teamTwoNoteHolder;
        }
        if(!Constants.teamThreeNoteHolder.equals("")) {
            teamThreeNotes = Constants.teamThreeNoteHolder;
        }
    }
}