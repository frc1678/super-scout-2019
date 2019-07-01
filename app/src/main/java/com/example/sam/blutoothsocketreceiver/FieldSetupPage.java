package com.example.sam.blutoothsocketreceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.sam.blutoothsocketreceiver.Fields.LeftField;
import com.example.sam.blutoothsocketreceiver.Fields.RightField;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by niraq on 1/13/2018.
 */

public class FieldSetupPage extends AppCompatActivity{
    //Declares/"Imports" datapoints to use in this class
    Activity context;
    Intent previous;
    Intent next;
    String red;
    String blue;
    String redTeam="red";
    String blueTeam="blue";
    String numberOfMatch;
    String leftViewColor;
    DatabaseReference dataBase;
    String teamNumberOne, teamNumberTwo, teamNumberThree;
    boolean isRed;
    String alliance;
    String noShowTeamOne;
    String noShowTeamTwo;
    String noShowTeamThree;
    String scrollableConflictBar;

    //"Inflates" the content layout: fieldsetuppage and sets it up for that alliance color
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fieldsetuppage);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        context = this;
        previous = getIntent();
        getExtrasForSetup(); getAllianceColor();
        dataBase = FirebaseDatabase.getInstance().getReference();
        blue = "#aa0000ff";
        red = "#aaff0000";
        if (leftViewColor.equals("blue") && alliance.equals(blueTeam)) {
            setUpLeftField();
        } else if (leftViewColor.equals("blue") && alliance.equals(redTeam)) {
            setUpRightField();
        } else if (leftViewColor.equals("red") && alliance.equals(blueTeam)) {
            setUpRightField();
        } else if (leftViewColor.equals("red") && alliance.equals(redTeam)) {
            setUpLeftField();
        }
    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.field, menu);
            return true;
        }

        //Sets up the field layout depending on the team's alliance color
        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            int id = item.getItemId();


            if (id == R.id.field) {
                if (leftViewColor.equals("blue")) {
                    if (alliance.equals(blueTeam)){
                        setUpLeftField();
                    } else if (alliance.equals(redTeam)) {
                        setUpRightField();
                    }
                } else if (leftViewColor.equals("red")) {
                    if (alliance.equals(blueTeam)){
                        setUpRightField();
                    } else if (alliance.equals(redTeam)) {
                        setUpLeftField();
                    }
                }
            }
            return super.onOptionsItemSelected(item);
        }

        //Gets all of the extras (stored datapoints on an intent ) needed to set up the field
        public void getExtrasForSetup () {
            numberOfMatch = previous.getExtras().getString("matchNumber");
            isRed = previous.getExtras().getBoolean("allianceColor");
            leftViewColor = previous.getExtras().getString("leftViewColor");
            teamNumberOne = previous.getExtras().getString("teamNumberOne");
            teamNumberTwo = previous.getExtras().getString("teamNumberTwo");
            teamNumberThree = previous.getExtras().getString("teamNumberThree");

            noShowTeamOne = previous.getExtras().getString("teamNumberOneNoShow");
            noShowTeamTwo = previous.getExtras().getString("teamNumberTwoNoShow");
            noShowTeamThree = previous.getExtras().getString("teamNumberThreeNoShow");

            scrollableConflictBar = previous.getExtras().getString("scrollableConflictBar");

        }

        //Gets the alliance color for a team by using the isRed extra (bool)
        public void getAllianceColor() {
            if (isRed) {
                alliance = "red";
            } else if (!isRed) {
                alliance = "blue";
            } else {
                //null
            }
        }

        //Sets up the Left Field UI by making an intent, adding the needed extras, and then passing the intent into the FieldSetupPage
        public void setUpLeftField() {
            Intent intent = new Intent(FieldSetupPage.this, LeftField.class);
            intent.putExtra("matchNumber", numberOfMatch);
            intent.putExtra("leftViewColor", leftViewColor);
            intent.putExtra("allianceColor", alliance);
            intent.putExtra("teamNumberOne",teamNumberOne);
            intent.putExtra("teamNumberTwo",teamNumberTwo);
            intent.putExtra("teamNumberThree", teamNumberThree);
            intent.putExtra("noShowOne",noShowTeamOne);
            intent.putExtra("noShowTwo",noShowTeamTwo);
            intent.putExtra("noShowThree",noShowTeamThree);
            intent.putExtra("scrollableConflictBar",scrollableConflictBar);
            FieldSetupPage.this.startActivity(intent);
        }

    //Sets up the Right Field UI by making an intent, adding the needed extras, and then passing the intent into the FieldSetupPage
    public void setUpRightField() {
            Intent intent = new Intent(FieldSetupPage.this, RightField.class);
            intent.putExtra("matchNumber", numberOfMatch);
            intent.putExtra("leftViewColor", leftViewColor);
            intent.putExtra("allianceColor", alliance);
            intent.putExtra("teamNumberOne",teamNumberOne);
            intent.putExtra("teamNumberTwo",teamNumberTwo);
            intent.putExtra("teamNumberThree", teamNumberThree);
            intent.putExtra("noShowOne",noShowTeamOne);
            intent.putExtra("noShowTwo",noShowTeamTwo);
            intent.putExtra("noShowThree",noShowTeamThree);
            intent.putExtra("scrollableConflictBar",scrollableConflictBar);
            FieldSetupPage.this.startActivity(intent);
        }

}
