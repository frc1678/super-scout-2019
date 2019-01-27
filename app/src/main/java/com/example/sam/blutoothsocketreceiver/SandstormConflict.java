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
import android.widget.Toast;

import com.example.sam.blutoothsocketreceiver.Fields.FieldLayout;
import com.example.sam.blutoothsocketreceiver.Fields.LeftField;
import com.example.sam.blutoothsocketreceiver.Fields.RightField;


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

    RadioButton teleopCollision;
    RadioButton autoCollision;
    RadioButton habInterference;

    Button teamOneButton;
    Button teamTwoButton;
    Button teamThreeButton;

    RelativeLayout radiobuttonLayout;

    Integer dataCounter;
    Integer teamOneButtonColor;
    Integer teamTwoButtonColor;
    Integer teamThreeButtonColor;

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
        if (id == R.id.sandstorm) {
            next = new Intent(SandstormConflict.this, ScoutingPage.class);
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
        }
    }
    public void initXML() {
        autoCollision = (RadioButton) findViewById(R.id.autoCollision);
        teleopCollision = (RadioButton) findViewById(R.id.teleopCollision);
        habInterference = (RadioButton) findViewById(R.id.habInterference);

        teamOneButton = (Button) findViewById(R.id.teamNumberOneButton);
        teamTwoButton = (Button) findViewById(R.id.teamNumberTwoButton);
        teamThreeButton = (Button) findViewById(R.id.teamNumberThreeButton);

        radiobuttonLayout = (RelativeLayout) findViewById(R.id.radioButtonLayout);

        teamOneButton.setText(String.valueOf(teamNumberOne));
        teamTwoButton.setText(String.valueOf(teamNumberTwo));
        teamThreeButton.setText(String.valueOf(teamNumberThree));

        teamOneButtonColor = ((ColorDrawable)teamOneButton.getBackground()).getColor();
        teamTwoButtonColor = ((ColorDrawable)teamTwoButton.getBackground()).getColor();
        teamThreeButtonColor = ((ColorDrawable)teamThreeButton.getBackground()).getColor();
    }
    public void hideRadioButtonLayout() {
        radiobuttonLayout.setVisibility(View.GONE);
    }
    public void revealRadioButtonLayout() {
        radiobuttonLayout.setVisibility(View.VISIBLE);
    }
    public void makeCaused(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(SandstormConflict.this, R.color.TeamNumberRed));
    }
    public void makeAffected(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(SandstormConflict.this, R.color.JustinYellow));
    }
    public void makeNeutral(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(SandstormConflict.this, R.color.LightGrey));
    }
    public void makeEqualConflict(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(SandstormConflict.this, R.color.JustinOrange));
    }
    public Boolean isCaused(Button button) {
        Integer buttonColor = ((ColorDrawable)button.getBackground()).getColor();
        if (String.valueOf(buttonColor).equals(FieldLayout.causedColor)) {
            return true;
        }
        return false;
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
    public Boolean isEqualConflict(Button button) {
        Integer buttonColor = ((ColorDrawable)button.getBackground()).getColor();
        if (String.valueOf(buttonColor).equals(FieldLayout.equalConflictColor)) {
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
            makeCaused(button);
        } else if (isCaused(button)) {
            makeAffected(button);
        } else if (isAffected(button)) {
            makeEqualConflict(button);
        } else if (isEqualConflict(button)) {
            makeNeutral(button);
        }
    }

}
