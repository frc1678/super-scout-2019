package com.example.sam.blutoothsocketreceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sam.blutoothsocketreceiver.Fields.FieldLayout;
import com.example.sam.blutoothsocketreceiver.Fields.LeftField;
import com.example.sam.blutoothsocketreceiver.Fields.RightField;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SandstormConflict extends AppCompatActivity {
    //SandstormConflict is an activity used for the purpose of declaring which robots NEGATIVELY INTERACTED with their
    //own teammates. For example, the page is a 3 button screen with each button being a robot on the team; if robot
    //one were to crash into robot two during autonomous/sandstorm, the super scout would mark robot one down on the screen

    Intent previous;
    Intent next;
    Bundle bundle;

    String leftNear;
    String leftMid;
    String leftFar;
    String rightNear;
    String rightMid;
    String rightFar;

    String scrollableConflictBar;

    public static String alliance;
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

    ListView conflictBarRight;
    ListView conflictBarLeft;
    TextView conflictBarRightTV;
    TextView conflictBarLeftTV;

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

            scrollableConflictBar = getIntent().getStringExtra("scrollableConflictBar");

        }
    }
    public void initXML() {

        String conflict = "       conflict";
        ArrayList<String> conflictLetters = new ArrayList<>();
            for (int p = 0; p < conflict.length(); p++) {
                conflictLetters.add(String.valueOf(conflict.charAt(p)));
            }
        teamOneButton = (Button) findViewById(R.id.teamNumberOneButton);
        teamTwoButton = (Button) findViewById(R.id.teamNumberTwoButton);
        teamThreeButton = (Button) findViewById(R.id.teamNumberThreeButton);

        teamOneButton.setText(String.valueOf(teamNumberOne));
        teamTwoButton.setText(String.valueOf(teamNumberTwo));
        teamThreeButton.setText(String.valueOf(teamNumberThree));

        teamOneButtonColor = ((ColorDrawable)teamOneButton.getBackground()).getColor();
        teamTwoButtonColor = ((ColorDrawable)teamTwoButton.getBackground()).getColor();
        teamThreeButtonColor = ((ColorDrawable)teamThreeButton.getBackground()).getColor();

        conflictBarLeft = (ListView) findViewById(R.id.conflictBarLeft);
        conflictBarRight = (ListView) findViewById(R.id.conflictBarRight);

        conflictBarLeftTV = (TextView) findViewById(R.id.conflictBarLeftTV);
        conflictBarRightTV = (TextView) findViewById(R.id.conflictBarRightTV);

        ConflictBarAdapter adapter = new ConflictBarAdapter(getApplicationContext(), conflictLetters);
        if (scrollableConflictBar.equals("true")) {
            conflictBarLeft.setAdapter(adapter);
            conflictBarRight.setAdapter(adapter);
            activateConflictBars(conflictBarRight, conflictBarLeft);
        } else {
            conflictBarLeftTV.setVisibility(View.VISIBLE);
            conflictBarRightTV.setVisibility(View.VISIBLE);
            if (SandstormConflict.alliance.equals("blue")) {
                conflictBarLeftTV.setTextColor(ContextCompat.getColor(this, R.color.Bloo));
                conflictBarRightTV.setTextColor(ContextCompat.getColor(this, R.color.Bloo));
            }
            if (SandstormConflict.alliance.equals("red")) {
                conflictBarRightTV.setTextColor(ContextCompat.getColor(this, R.color.Rausch));
                conflictBarLeftTV.setTextColor(ContextCompat.getColor(this, R.color.Rausch));
            }
        }

    }

    //Turns the button given as an argument into the color of one that is affected (the conflict-er)
    public void makeAffected(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(SandstormConflict.this, R.color.EmilyPurple));
    }

    //Turns the button given as an argument into the color of one that is neutral (default color)
    public void makeNeutral(Button button) {
        button.setBackgroundColor(ContextCompat.getColor(SandstormConflict.this, R.color.LightGrey));
    }

    //Returns a true or false depending on whether the button given as an argument is of the affected's color or not
    // AKA: if affected color: return true. else, return false;
    public Boolean isAffected(Button button) {
        Integer buttonColor = ((ColorDrawable)button.getBackground()).getColor();
        if (String.valueOf(buttonColor).equals(FieldLayout.affectedColor)) {
            return true;
        }
        return false;
    }

    //Returns a true or false depending on whether the button given as an argument is one of the neutral's color or not
    // AKA: if neutral: return true. else, return false;
    public Boolean isNeutral(Button button) {
        Integer buttonColor = ((ColorDrawable)button.getBackground()).getColor();
        if (String.valueOf(buttonColor).equals(FieldLayout.neutralColor)) {
            return true;
        }
        return false;
    }


    //Begins the listeners for the button onClicks
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

    //Method called on the button press. If the button is neutral, it will become affected. VICE VERSA
    public void initColors(Button button) {
        if (isNeutral(button)) {
            makeAffected(button);
            dataCounter ++;
        } else if (isAffected(button)) {
            makeNeutral(button);
            dataCounter --;
        }
    }

    //Updates placeholder booleans for each button
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
    public void activateConflictBars(final ListView right, final ListView left) {
        final long totalScrollTime = Long.MAX_VALUE;

        final int scrollPeriod = 50;

        final int heightToScroll = 1;

        right.post(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(totalScrollTime, scrollPeriod ) {
                    public void onTick(long millisUntilFinished) {
                        right.scrollBy(0, heightToScroll);
                    }

                    public void onFinish() {
                        //you can add code for restarting timer here
                        Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast", Toast.LENGTH_SHORT).show();
                    }
                }.start();
            }
        });
        left.post(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(totalScrollTime, scrollPeriod ) {
                    public void onTick(long millisUntilFinished) {
                        left.scrollBy(0, heightToScroll);
                    }

                    public void onFinish() {
                        //you can add code for restarting timer here
                    }
                }.start();
            }
        });
    }

}

class ConflictBarAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> conflictLetters;

    public ConflictBarAdapter(Context context, ArrayList<String> conflictLetterList) {
        mContext = context;
        conflictLetters = conflictLetterList;
    }
    @Override
    public int getCount() {
        return conflictLetters.size();
        //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return conflictLetters.get(position);
        //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.conflict_cell, parent, false);
        }

        //makes current letter be the team value of the cell
        String currentLetter = (String) getItem(position);

        //inits xml per according xml element
        TextView letter = (TextView)
                convertView.findViewById(R.id.conflictLetter);

        if (SandstormConflict.alliance.equals("blue")) {
            letter.setTextColor(ContextCompat.getColor(mContext, R.color.Bloo));
        }
        if (SandstormConflict.alliance.equals("red")) {
            letter.setTextColor(ContextCompat.getColor(mContext, R.color.Rausch));
        }

        //sets text of the letter to the current letter
        letter.setText(currentLetter);

        return convertView;
    }


}