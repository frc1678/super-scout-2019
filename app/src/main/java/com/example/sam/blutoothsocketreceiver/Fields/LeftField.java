package com.example.sam.blutoothsocketreceiver.Fields;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sam.blutoothsocketreceiver.Constants;
import com.example.sam.blutoothsocketreceiver.FieldSetupPage;
import com.example.sam.blutoothsocketreceiver.MainActivity;
import com.example.sam.blutoothsocketreceiver.R;
import com.example.sam.blutoothsocketreceiver.SandstormConflict;
import com.example.sam.blutoothsocketreceiver.ScoutingPage;

import java.util.HashMap;
import java.util.Map;

import static com.example.sam.blutoothsocketreceiver.Fields.Bay.isOrange;
import static com.example.sam.blutoothsocketreceiver.Fields.Bay.isYellow;

public class LeftField extends AppCompatActivity {
    //"Imports" Data points to use within this class
    String numberOfMatch;
    Intent previous;
    boolean isRed;
    String scrollableConflictBar;
    String alliance;
    String leftViewColor;

    Bundle bundle;

    Button right_frontOfCargoShip, left_frontOfCargoShip;
    Button leftNear, leftMid, leftFar;
    Button rightNear, rightMid, rightFar;
    Button centerNear, centerMid, centerFar;
    Boolean leftNearBoolean = true, leftMidBoolean = true, leftFarBoolean = true;
    Boolean rightNearBoolean = true, rightMidBoolean = true, rightFarBoolean = true;

    String teamNumberOne, teamNumberTwo, teamNumberThree;

    Map<String, String> cargoShipInputValues = new HashMap<>();

    String noShowOne;
    String noShowTwo;
    String noShowThree;
    
    Context context;

    //Creates the left_field layout and gets all the information on it (Match Number, Team Number, Color, etc.)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.left_field); setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); previous = getIntent(); bundle = previous.getExtras();
        getMatchNumber(); getTeamNumbers(); getAlliance(); getLeftViewColor(); getXML();
        prepareField(); startFieldListener();
    }
    //Gets the match number from the bundle it is stored in (from another class), unless it is null
    public void getMatchNumber() {
        if (bundle != null) {
            numberOfMatch = getIntent().getStringExtra("matchNumber");
        }
    }
    //Gets the alliance color from the bundle it is stored in (from another class), unless it is null
    public void getAlliance() {
        if (bundle != null) {
            alliance = getIntent().getStringExtra("allianceColor");
            scrollableConflictBar = getIntent().getStringExtra("scrollableConflictBar");
        }
    }
    //Gets the leftViewColor from the bundle it is stored in (from another class), unless it is null
    public void getLeftViewColor() {
        if (bundle != null) {
            leftViewColor = getIntent().getStringExtra("leftViewColor");
        }
    }
    //Gets the team numbers, and noShowTeams (if there are any), from the bundle it is stored in (from another class), unless it is null
    public void getTeamNumbers() {
        if (bundle != null) {
            teamNumberOne = previous.getExtras().getString("teamNumberOne");
            teamNumberTwo = previous.getExtras().getString("teamNumberTwo");
            teamNumberThree = previous.getExtras().getString("teamNumberThree");

            //get noShow teams

            noShowOne = previous.getExtras().getString("noShowOne");
            noShowTwo = previous.getExtras().getString("noShowTwo");
            noShowThree = previous.getExtras().getString("noShowThree");

        }
    }
    //Gets the XML IDs for front of cargo ship, the rest of the cargo ship, and the cargo bays
    public void getXML() {
        //Front of cargo ship
        right_frontOfCargoShip = (Button) findViewById(R.id.right_frontOfCargoShip);
        left_frontOfCargoShip = (Button) findViewById(R.id.left_frontOfCargoShip);

        //Rest of cargo ship
        leftNear = (Button) findViewById(R.id.leftNear);
        leftMid = (Button) findViewById(R.id.leftMid);
        leftFar = (Button) findViewById(R.id.leftFar);

        rightNear = (Button) findViewById(R.id.rightNear);
        rightMid = (Button) findViewById(R.id.rightMid);
        rightFar = (Button) findViewById(R.id.rightFar);

        //Cargo bay buttons separators
        centerNear = (Button) findViewById(R.id.centerNear);
        centerMid = (Button) findViewById(R.id.centerMid);
        centerFar = (Button ) findViewById(R.id.centerFar);
    }
    //"Prepares" the field by setting the Cargo Bay colors red (if on the red alliance) and blue (if on the blue alliance)
    public void prepareField() {
        if (alliance.equals("red")){
            centerNear.setBackgroundColor(ContextCompat.getColor(this, R.color.TeamNumberRed));
            centerMid.setBackgroundColor(ContextCompat.getColor(this, R.color.TeamNumberRed));
            centerFar.setBackgroundColor(ContextCompat.getColor(this, R.color.TeamNumberRed));
        } else if (alliance.equals("blue")) {
            centerNear.setBackgroundColor(ContextCompat.getColor(this, R.color.Bloo));
            centerMid.setBackgroundColor(ContextCompat.getColor(this, R.color.Bloo));
            centerFar.setBackgroundColor(ContextCompat.getColor(this, R.color.Bloo));
        }
    }
    //Sets the OnClickListener for each of the Cargo Bays. If clicked, then it will change the color to the one for a panel or cargo
    public void startFieldListener() {
        leftNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftNearBoolean) {
                    leftNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));leftNearBoolean = false;
                } else if (!leftNearBoolean) {
                    leftNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));leftNearBoolean = true;
                }}});
        leftMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftMidBoolean) {
                    leftMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));leftMidBoolean = false;
                } else if (!leftMidBoolean) {
                    leftMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));leftMidBoolean = true;
                }}});
        leftFar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftFarBoolean) {
                    leftFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));leftFarBoolean = false;
                } else if (!leftFarBoolean) {
                    leftFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));leftFarBoolean = true;
                }}});
        rightNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightNearBoolean) {
                    rightNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));rightNearBoolean = false;
                } else if (!rightNearBoolean) {
                    rightNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));rightNearBoolean = true;
                }}});
        rightMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightMidBoolean) {
                    rightMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));rightMidBoolean = false;
                } else if (!rightMidBoolean) {
                    rightMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));rightMidBoolean = true;
                }}});
        rightFar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightFarBoolean) {
                    rightFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));rightFarBoolean = false;
                } else if (!rightFarBoolean) {
                    rightFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));rightFarBoolean = true;
                }}});
    }
    //Creates a dialog to warns the user that going back will change data
    @Override
    public void onBackPressed () {
        final Activity activity = this;
        new AlertDialog.Builder(this)
                .setTitle("WARNING").setMessage("GOING BACK WILL CAUSE LOSS OF DATA").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent FieldSetupPageIntent = new Intent(LeftField.this, MainActivity.class);
                        startActivity(FieldSetupPageIntent);}}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    //Inflates the Sandstorm screen
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.sandstorm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        //If the item (the xml) selected is sandstorm, then get the values of the cargo ship bays. If the cargo ship bay is empty,
        //then create the error message (created above). If the cargo bay value isn't empty, put the cargo ship bay values, no show teams, alliance, team numbers,
        //and scrollable conflict bar variables onto the "next" intent.
        if (id == R.id.sandstorm) {
            Intent next = new Intent(LeftField.this, SandstormConflict.class);
            getCargoShipInputValues();
            if (cargoShipInputValues.containsValue(Bay.noValue)) {
                Toast toast = Toast.makeText(LeftField.this, Bay.errorMessage, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 65);
                toast.show();
            } else {
                //TODO: Add data check against other scout if extra time (check if what is currently in the switches and plates conflicts with what this has, notify).
                next.putExtras(previous);
                next.putExtra(Constants.leftNear, cargoShipInputValues.get(Constants.leftNear));
                next.putExtra(Constants.leftMid, cargoShipInputValues.get(Constants.leftMid));
                next.putExtra(Constants.leftFar, cargoShipInputValues.get(Constants.leftFar));
                next.putExtra(Constants.rightNear, cargoShipInputValues.get(Constants.rightNear));
                next.putExtra(Constants.rightMid, cargoShipInputValues.get(Constants.rightMid));
                next.putExtra(Constants.rightFar, cargoShipInputValues.get(Constants.rightFar));
                next.putExtra("noShowOne",noShowOne);
                next.putExtra("noShowTwo",noShowTwo);
                next.putExtra("noShowThree",noShowThree);
                next.putExtra("alliance", alliance);
                next.putExtra("teamNumberOne",teamNumberOne);
                next.putExtra("teamNumberTwo",teamNumberTwo);
                next.putExtra("teamNumberThree", teamNumberThree);
                next.putExtra("matchNumber",numberOfMatch);
                next.putExtra("scrollableConflictBar",scrollableConflictBar);
                startActivity(next);
            }
            //If the item selected is the noShow xml, inflate the noshowteams layout
        } else if (id == R.id.noShow) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LeftField.this);
            final View noShowLayout = LayoutInflater.from(LeftField.this).inflate(R.layout.noshowteams, null);
            final Button noShowTeamOne = (Button) noShowLayout.findViewById(R.id.noShowTeamOne);
            final Button noShowTeamTwo = (Button) noShowLayout.findViewById(R.id.noShowTeamTwo);
            final Button noShowTeamThree = (Button) noShowLayout.findViewById(R.id.noShowTeamThree);

            //Sets the noShowTeam buttons' text to the No Show Teams
            noShowTeamOne.setText(teamNumberOne);
            noShowTeamTwo.setText(teamNumberTwo);
            noShowTeamThree.setText(teamNumberThree);

            //Sets the no show team buttons red if that team is no show, or light grey if they aren't. Also, sets the onClickListeners for them.
            if (noShowOne.equals("true")) {
                noShowTeamOne.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.TeamNumberRed));
            } else {
                noShowTeamOne.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.LightGrey));
            }
            if (noShowTwo.equals("true")) {
                noShowTeamTwo.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.TeamNumberRed));
            } else {
                noShowTeamTwo.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.LightGrey));
            }
            if (noShowThree.equals("true")) {
                noShowTeamThree.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.TeamNumberRed));
            } else {
                noShowTeamThree.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.LightGrey));
            }
            noShowTeamOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer buttonColor = ((ColorDrawable) noShowTeamOne.getBackground()).getColor();
                    if (String.valueOf(buttonColor).equals(FieldLayout.neutralColor)) {
                        noShowTeamOne.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.TeamNumberRed));
                    } else {
                        noShowTeamOne.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.LightGrey));
                    }

                }
            });
            noShowTeamTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer buttonColor = ((ColorDrawable) noShowTeamTwo.getBackground()).getColor();
                    if (String.valueOf(buttonColor).equals(FieldLayout.neutralColor)) {
                        noShowTeamTwo.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.TeamNumberRed));
                    } else {
                        noShowTeamTwo.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.LightGrey));
                    }

                }
            });
            noShowTeamThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer buttonColor = ((ColorDrawable) noShowTeamThree.getBackground()).getColor();
                    if (String.valueOf(buttonColor).equals(FieldLayout.neutralColor)) {
                        noShowTeamThree.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.TeamNumberRed));
                    } else {
                        noShowTeamThree.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.LightGrey));
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
                        noShowOne = "true";
                    } else {
                        noShowTwo = "false";
                    }
                    if (String.valueOf(teamTwoColor).equals(String.valueOf(FieldLayout.causedColor))) {
                        noShowTwo = "true";
                    } else {
                        noShowTwo = "false";
                    }
                    if (String.valueOf(teamThreeColor).equals(String.valueOf(FieldLayout.causedColor))) {
                        noShowThree = "true";
                    } else {
                        noShowThree = "false";
                    } 

                }
            });
            builder.show();

        }
        return super.onOptionsItemSelected(item);
    }
    //Sends the cargo ship bay colors to the updateCargoShipValue function, which puts the bay location and value (depending on its color) into the cargoShipInputValues map (defined above)
    public void getCargoShipInputValues() {
        Integer leftNearColor = ((ColorDrawable)leftNear.getBackground()).getColor();
        Integer leftMidColor = ((ColorDrawable)leftMid.getBackground()).getColor();
        Integer leftFarColor = ((ColorDrawable)leftFar.getBackground()).getColor();
        Integer rightNearColor = ((ColorDrawable)rightNear.getBackground()).getColor();
        Integer rightMidColor = ((ColorDrawable)rightMid.getBackground()).getColor();
        Integer rightFarColor = ((ColorDrawable)rightFar.getBackground()).getColor();

        updateCargoShipValue(leftNearColor, Constants.leftNear);
        updateCargoShipValue(leftMidColor, Constants.leftMid);
        updateCargoShipValue(leftFarColor, Constants.leftFar);
        updateCargoShipValue(rightNearColor, Constants.rightNear);
        updateCargoShipValue(rightMidColor, Constants.rightMid);
        updateCargoShipValue(rightFarColor, Constants.rightFar);
    }

    public void updateCargoShipValue(Integer bayLocationColor, String bayLocation ) {
        if (Bay.isGray(bayLocationColor)) {
            cargoShipInputValues.put(bayLocation, Bay.noValue);
        } else { if (Bay.isYellow(bayLocationColor)) {
            cargoShipInputValues.put(bayLocation, Bay.yellowValue);
        } else if (Bay.isOrange(bayLocationColor)) {
            cargoShipInputValues.put(bayLocation, Bay.orangeValue);
            }
        }
    }

}
