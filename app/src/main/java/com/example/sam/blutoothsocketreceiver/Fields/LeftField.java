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

    String numberOfMatch;
    Intent previous;
    boolean isRed;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.left_field); setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); previous = getIntent(); bundle = previous.getExtras();
        getMatchNumber(); getTeamNumbers(); getAlliance(); getLeftViewColor(); getXML();
        prepareField(); startFieldListener();
    }
    public void getMatchNumber() {
        if (bundle != null) {
            numberOfMatch = getIntent().getStringExtra("matchNumber");
        }
    }
    public void getAlliance() {
        if (bundle != null) {
            alliance = getIntent().getStringExtra("allianceColor");
        }
    }
    public void getLeftViewColor() {
        if (bundle != null) {
            leftViewColor = getIntent().getStringExtra("leftViewColor");
        }
    }
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
    public void getXML() {
        //front of cargo ship???? hahahahahahahhahahahaa
        right_frontOfCargoShip = (Button) findViewById(R.id.right_frontOfCargoShip);
        left_frontOfCargoShip = (Button) findViewById(R.id.left_frontOfCargoShip);

        //rest of cargo ship??? orange tree hahahhahahahahahahahahahahahahahahahahahahahahahahahahahahahahahaha
        leftNear = (Button) findViewById(R.id.leftNear);
        leftMid = (Button) findViewById(R.id.leftMid);
        leftFar = (Button) findViewById(R.id.leftFar);

        rightNear = (Button) findViewById(R.id.rightNear);
        rightMid = (Button) findViewById(R.id.rightMid);
        rightFar = (Button) findViewById(R.id.rightFar);

        // SEPARATORS for baysLMAOOOOOOOOO haahahahhahaahahaaahahahhhaaahhahahah
        centerNear = (Button) findViewById(R.id.centerNear);
        centerMid = (Button) findViewById(R.id.centerMid);
        centerFar = (Button ) findViewById(R.id.centerFar);
    }
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
    public void startFieldListener() {
        leftNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftNearBoolean) {
                    leftNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));leftNearBoolean = false;
                } else if (!leftNearBoolean) {
                    leftNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));leftNearBoolean = true;
                }}});
        leftMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftMidBoolean) {
                    leftMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));leftMidBoolean = false;
                } else if (!leftMidBoolean) {
                    leftMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));leftMidBoolean = true;
                }}});
        leftFar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftFarBoolean) {
                    leftFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));leftFarBoolean = false;
                } else if (!leftFarBoolean) {
                    leftFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));leftFarBoolean = true;
                }}});
        rightNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightNearBoolean) {
                    rightNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));rightNearBoolean = false;
                } else if (!rightNearBoolean) {
                    rightNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));rightNearBoolean = true;
                }}});
        rightMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightMidBoolean) {
                    rightMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));rightMidBoolean = false;
                } else if (!rightMidBoolean) {
                    rightMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));rightMidBoolean = true;
                }}});
        rightFar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightFarBoolean) {
                    rightFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));rightFarBoolean = false;
                } else if (!rightFarBoolean) {
                    rightFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));rightFarBoolean = true;
                }}});
    }
    //Warns the user that going back will change data
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
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.sandstorm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
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
                startActivity(next);
            }
        } else if (id == R.id.noShow) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final View noShowLayout = LayoutInflater.from(context).inflate(R.layout.noshowteams, null);
            final Button noShowTeamOne = (Button) noShowLayout.findViewById(R.id.noShowTeamOne);
            final Button noShowTeamTwo = (Button) noShowLayout.findViewById(R.id.noShowTeamTwo);
            final Button noShowTeamThree = (Button) noShowLayout.findViewById(R.id.noShowTeamThree);

            noShowTeamOne.setText(teamNumberOne);
            noShowTeamTwo.setText(teamNumberTwo);
            noShowTeamThree.setText(teamNumberThree);
            
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
            }).show();

        }
        return super.onOptionsItemSelected(item);
    }

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
