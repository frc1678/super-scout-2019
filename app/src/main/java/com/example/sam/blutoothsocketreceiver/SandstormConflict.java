package com.example.sam.blutoothsocketreceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sam.blutoothsocketreceiver.Fields.FieldLayout;
import com.example.sam.blutoothsocketreceiver.Fields.LeftField;
import com.example.sam.blutoothsocketreceiver.Fields.RightField;

import org.w3c.dom.Text;


public class SandstormConflict extends AppCompatActivity {

    Intent previous;
    Intent next;
    Bundle bundle;

    String leftNear;
    String leftMid;
    String leftFar;
    String rightNear;
    String rightMid;
    String rightFar;

    String alliance;
    String teamNumberOne;
    String teamNumberTwo;
    String teamNumberThree;

    Button teamOneButton;
    Button teamTwoButton;
    Button teamThreeButton;

    Integer dataCounter = 0;
    Integer teamOneButtonColor;
    Integer teamTwoButtonColor;
    Integer teamThreeButtonColor;

    String matchNumber;

    String noShowOne;
    String noShowTwo;
    String noShowThree;

    TextView conflictBarRight;
    TextView conflictBarLeft;

    String teamOneConflict;
    String teamTwoConflict;
    String teamThreeConflict;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sandstorm); setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); previous = getIntent(); bundle = previous.getExtras(); dataCounter = 0;
        getExtras(); initXML(); initActivity();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.teleop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.teleop) {
            next = new Intent(SandstormConflict.this, ScoutingPage.class);
            updateConflict();
            next.putExtras(previous);
            next.putExtra(Constants.leftNear, leftNear);
            next.putExtra(Constants.leftMid, leftMid);
            next.putExtra(Constants.leftFar, leftFar);
            next.putExtra(Constants.rightNear, rightNear);
            next.putExtra(Constants.rightMid, rightMid);
            next.putExtra(Constants.rightFar, rightFar);
            next.putExtra("noShowOne",noShowOne);
            next.putExtra("noShowTwo",noShowTwo);
            next.putExtra("noShowThree",noShowThree);
            next.putExtra("alliance", alliance);
            next.putExtra("teamNumberOne",teamNumberOne);
            next.putExtra("teamNumberTwo",teamNumberTwo);
            next.putExtra("teamNumberThree", teamNumberThree);
            next.putExtra("matchNumber",matchNumber);
            next.putExtra("teamOneConflict", teamOneConflict);
            next.putExtra("teamTwoConflict",teamTwoConflict);
            next.putExtra("teamThreeConflict",teamThreeConflict);

            startActivity(next);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed () {
        final Activity activity = this;
        new AlertDialog.Builder(this)
                .setTitle("WARNING").setMessage("GOING BACK WILL CAUSE LOSS OF DATA").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finish(); }}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void getExtras() {
        if (bundle != null) {
            leftNear = getIntent().getStringExtra(Constants.leftNear);
            leftMid = getIntent().getStringExtra(Constants.leftMid);
            leftFar = getIntent().getStringExtra(Constants.leftFar);
            rightNear = getIntent().getStringExtra(Constants.rightNear);
            rightMid = getIntent().getStringExtra(Constants.rightMid);
            rightFar = getIntent().getStringExtra(Constants.rightFar);

            alliance = getIntent().getStringExtra(FieldLayout.alliance);
            teamNumberOne = getIntent().getStringExtra(FieldLayout.teamNumberOne);
            teamNumberTwo = getIntent().getStringExtra(FieldLayout.teamNumberTwo);
            teamNumberThree = getIntent().getStringExtra(FieldLayout.teamNumberThree);

            matchNumber = getIntent().getStringExtra("matchNumber");

            noShowOne = getIntent().getStringExtra("noShowOne");
            noShowTwo = getIntent().getStringExtra("noShowTwo");
            noShowThree = getIntent().getStringExtra("noShowThree");

        }
    }
    public void initXML() {

        teamOneButton = (Button) findViewById(R.id.teamNumberOneButton);
        teamTwoButton = (Button) findViewById(R.id.teamNumberTwoButton);
        teamThreeButton = (Button) findViewById(R.id.teamNumberThreeButton);

        teamOneButton.setText(String.valueOf(teamNumberOne));
        teamTwoButton.setText(String.valueOf(teamNumberTwo));
        teamThreeButton.setText(String.valueOf(teamNumberThree));

        teamOneButtonColor = ((ColorDrawable)teamOneButton.getBackground()).getColor();
        teamTwoButtonColor = ((ColorDrawable)teamTwoButton.getBackground()).getColor();
        teamThreeButtonColor = ((ColorDrawable)teamThreeButton.getBackground()).getColor();

        conflictBarLeft = (TextView) findViewById(R.id.conflictBarLeft);
        conflictBarRight = (TextView) findViewById(R.id.conflictBarRight);

        if (alliance.equals("blue")) {
            conflictBarLeft.setTextColor(ContextCompat.getColor(this, R.color.Bloo));
            conflictBarRight.setTextColor(ContextCompat.getColor(this, R.color.Bloo));
        }
        if (alliance.equals("red")) {
            conflictBarRight.setTextColor(ContextCompat.getColor(this, R.color.Rausch));
            conflictBarLeft.setTextColor(ContextCompat.getColor(this, R.color.Rausch));
        }
    }
/*    public void hideRadioButtonLayout() {
        radiobuttonLayout.setVisibility(View.GONE);
    }
    public void revealRadioButtonLayout() {
        radiobuttonLayout.setVisibility(View.VISIBLE);
    }*/
    public void makeAffected(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(SandstormConflict.this, R.color.JustinOrange));
    }
    public void makeNeutral(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(SandstormConflict.this, R.color.LightGrey));
    }
    public Boolean isAffected(Button button) {
        Integer buttonColor = ((ColorDrawable)button.getBackground()).getColor();
        if (String.valueOf(buttonColor).equals(FieldLayout.affectedColor)) {
            return true;
        }
        return false;
    }
    public Boolean isNeutral(Button button) {
        Integer buttonColor = ((ColorDrawable)button.getBackground()).getColor();
        if (String.valueOf(buttonColor).equals(FieldLayout.neutralColor)) {
            return true;
        }
        return false;
    }


    public void initActivity() {


        teamOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initColors(teamOneButton);

            }
        });
        teamTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initColors(teamTwoButton);

            }
        });
        teamThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initColors(teamThreeButton);

            }
        });
    }


    public void initColors(Button button) {
        if (isNeutral(button)) {
            makeAffected(button);
            dataCounter ++;
        } else if (isAffected(button)) {
            makeNeutral(button);
            dataCounter --;
        }
    }
    public void updateConflict() {
        if (isAffected(teamOneButton)) {
            teamOneConflict = "true";
        } else if (isNeutral(teamOneButton)) {
            teamOneConflict = "false";
        }
        if (isAffected(teamTwoButton)) {
            teamTwoConflict = "true";
        } else if (isNeutral(teamTwoButton)) {
            teamTwoConflict = "false";
        }
        if (isAffected(teamThreeButton)) {
            teamThreeConflict = "true";
        } else if (isNeutral(teamThreeButton)) {
            teamThreeConflict = "false";
        }
    }

}
