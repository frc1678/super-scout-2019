package com.example.sam.blutoothsocketreceiver.Fields;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sam.blutoothsocketreceiver.R;

public class LeftField extends AppCompatActivity {

    String numberOfMatch;
    Intent previous;
    boolean isRed;
    String alliance;

    Bundle bundle;

    Button right_frontOfCargoShip, left_frontOfCargoShip;
    Button leftNear, leftMid, leftFar;
    Button rightNear, rightMid, rightFar;
    Button centerNear, centerMid, centerFar;
    Boolean leftNearBoolean = true, leftMidBoolean = true, leftFarBoolean = true;
    Boolean rightNearBoolean = true, rightMidBoolean = true, rightFarBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_field); setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); previous = getIntent(); bundle = previous.getExtras();
        getMatchNumber(); getAlliance(); getAllianceColor(); getXML();
        Log.e("INFORMATION", alliance + ": " + numberOfMatch);
        prepareField(); startFieldListener();
    }
    public void getMatchNumber() {
        if (bundle != null) {
            numberOfMatch = getIntent().getExtras().getString("matchNumber");
        }
    }
    public void getAlliance() {
        if (bundle != null) {
            isRed = getIntent().getExtras().getBoolean("allianceColor");
        }
    }
    public void getAllianceColor() {
        if (isRed) {
            alliance = "red";
            Log.e("alliance","red");
        } else if (!isRed) {
            alliance = "blue";
            Log.e("alliance","blue");
        } else {
            Log.e("alliance","cry");
            //Do null.
        }
    }
    public void getXML() {
        //front of cargo ship???? hahahahahahahhahahahaa
        right_frontOfCargoShip = (Button) findViewById(R.id.right_frontOfCargoShip);
        left_frontOfCargoShip = (Button) findViewById(R.id.left_frontOfCargoShip);

        //rest of cargo ship??? orange tree hahahahahahahahahahahahahahahahahahahahahahahahahahahahahahahahahaha
        leftNear = (Button) findViewById(R.id.leftNear);
        leftMid = (Button) findViewById(R.id.leftMid);
        leftFar = (Button) findViewById(R.id.leftFar);

        rightNear = (Button) findViewById(R.id.rightNear);
        rightMid = (Button) findViewById(R.id.rightMid);
        rightFar = (Button) findViewById(R.id.rightFar);

        // SEPARATORS LMAOOOOOOOOO haahahahhahaahahaaahahahhhaaahhahahah
        centerNear = (Button) findViewById(R.id.centerNear);
        centerMid = (Button) findViewById(R.id.centerMid);
        centerFar = (Button ) findViewById(R.id.centerFar);
    }
    public void prepareField() {
        Log.e("prepareField","prepareField");
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
                    leftNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));
                    leftNearBoolean = false;
                } else if (!leftNearBoolean) {
                    leftNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));
                    leftNearBoolean = true;
                }}});
        leftMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftMidBoolean) {
                    leftMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));
                    leftMidBoolean = false;
                } else if (!leftMidBoolean) {
                    leftMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));
                    leftMidBoolean = true;
                }}});
        leftFar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftFarBoolean) {
                    leftFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));
                    leftFarBoolean = false;
                } else if (!leftFarBoolean) {
                    leftFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));
                    leftFarBoolean = true;
                }}});
        rightNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightNearBoolean) {
                    rightNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));
                    rightNearBoolean = false;
                } else if (!rightNearBoolean) {
                    rightNear.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));
                    rightNearBoolean = true;
                }}});
        rightMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightMidBoolean) {
                    rightMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));
                    rightMidBoolean = false;
                } else if (!rightMidBoolean) {
                    rightMid.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));
                    rightMidBoolean = true;
                }}});
        rightFar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightFarBoolean) {
                    rightFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Oroonge));
                    rightFarBoolean = false;
                } else if (!rightFarBoolean) {
                    rightFar.setBackgroundColor(ContextCompat.getColor(LeftField.this, R.color.Hootch));
                    rightFarBoolean = true;
                }}});

    }
}
