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
    boolean isRed;
    String alliance;

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

        public void getExtrasForSetup () {
            numberOfMatch = previous.getExtras().getString("matchNumber");
            isRed = previous.getExtras().getBoolean("allianceColor");
            leftViewColor = previous.getExtras().getString("leftViewColor");
        }
        public void getAllianceColor() {
            if (isRed) {
                alliance = "red";
                Log.e("fieldSETUP","red");
            } else if (!isRed) {
                alliance = "blue";
                Log.e("fieldSETUP","blue");
            } else {
                Log.e("alliance","cry");
                //Do null.
            }
        }

        public void setUpLeftField() {
            Intent intent = new Intent(FieldSetupPage.this, LeftField.class);
            intent.putExtra("matchNumber", numberOfMatch);
            intent.putExtra("leftViewColor", leftViewColor);
            intent.putExtra("allianceColor", alliance);
            FieldSetupPage.this.startActivity(intent);
        }

        public void setUpRightField() {
            Intent intent = new Intent(FieldSetupPage.this, RightField.class);
            intent.putExtra("matchNumber", numberOfMatch);
            intent.putExtra("leftViewColor", leftViewColor);
            intent.putExtra("allianceColor", alliance);
            FieldSetupPage.this.startActivity(intent);
        }

}
