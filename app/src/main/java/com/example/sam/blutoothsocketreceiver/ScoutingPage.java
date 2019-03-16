package com.example.sam.blutoothsocketreceiver;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ScoutingPage extends ActionBarActivity {
    Activity context;
    String numberOfMatch;
    String teamNumberOne;
    String teamNumberTwo;
    String teamNumberThree;
    String alliance;
    String dataBaseUrl;
    String allianceScoreData, allianceFoulData;
    TextView teamNumberOneTextview;
    TextView teamNumberTwoTextview;
    TextView teamNumberThreeTextview;
    ArrayList<String> teamOneDataName;
    ArrayList<String> teamOneDataScore;
    ArrayList<String> teamTwoDataName;
    ArrayList<String> teamTwoDataScore;
    ArrayList<String> teamThreeDataName;
    ArrayList<String> teamThreeDataScore;
    Integer allianceScoreInt = 0;
    Integer allianceFoulInt = 0;
    Boolean didHabClimb = false;
    Boolean didRocketRP = false;
    Boolean isMute;
    JSONObject object;
    Intent next;
    DatabaseReference dataBase;
    Boolean isRed;
    String teamOneNotes;
    String teamTwoNotes;
    String teamThreeNotes;
    Boolean teamNumberOneBooleanTippy, teamNumberOneBooleanAlignment, teamNumberOneBooleanGrip, teamNumberOneBooleanInterference;
    Boolean teamNumberTwoBooleanTippy, teamNumberTwoBooleanAlignment, teamNumberTwoBooleanGrip, teamNumberTwoBooleanInterference;
    Boolean teamNumberThreeBooleanTippy, teamNumberThreeBooleanAlignment, teamNumberThreeBooleanGrip, teamNumberThreeBooleanInterference;
    SuperScoutingPanel panelOne;
    SuperScoutingPanel panelTwo;
    SuperScoutingPanel panelThree;
    String leftNear;
    String leftMid;
    String leftFar;
    String rightNear;
    String rightMid;
    String rightFar;

    Integer dValueOne = 0;
    Integer dValueTwo = 0;
    Integer dValueThree = 0;

    String dValTextOne;

    String noShowOne;
    String noShowTwo;
    String noShowThree;

    static String noShowOnePanel;
    static String noShowTwoPanel;
    static String noShowThreePanel;
    static String teamOne;
    static String teamTwo;
    static String teamThree;

    String teamOneConflict;
    String teamTwoConflict;
    String teamThreeConflict;

    Boolean dProgressActive = false;
    Boolean kProgressActive = false;
    Boolean pProgressActive = false;

    Drawable seekBarActiveThumb;
    Drawable seekBarInactiveThumb;
    
    ArrayList<String> teamsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.super_scouting);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getPanels();
        next = getIntent();
        object = new JSONObject();
        getExtrasForScouting();
        dataBase = FirebaseDatabase.getInstance().getReference();
        setPanels();
        initializeTeamTextViews();
        initTeamsList();
        context = this;
        teamOneNotes = "";
        teamTwoNotes = "";
        teamThreeNotes = "";

        seekBarActiveThumb = getResources().getDrawable( R.drawable.seekbar_thumb);
        seekBarInactiveThumb = getResources().getDrawable( R.drawable.seekbar_thumb_inactive);

        final Button defenseButtonOne = (Button) findViewById(R.id.defenseRobotOneButton);
        defenseButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateDefenseDialog(1);
            }
        });

        Button defenseButtonTwo = (Button) findViewById(R.id.defenseRobotTwoButton);
        defenseButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateDefenseDialog(2);
            }
        });

        Button defenseButtonThree = (Button) findViewById(R.id.defenseRobotThreeButton);
        defenseButtonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateDefenseDialog(3);
            }
        });
    }

    //Warns the user that going back will change data
    @Override
    public void onBackPressed() {
        final Activity activity = this;
        new AlertDialog.Builder(this)
                .setTitle("WARNING")
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
        getMenuInflater().inflate(R.menu.finaldata, menu);
        return true;
    }
    
    public void initTeamsList() {
        if (teamOne != null) {
            teamsList.add(teamOne);
        }
        if (teamTwo != null) {
            teamsList.add(teamTwo);
        }
        if (teamThree != null) {
            teamsList.add(teamThree);
        }
    }

    // work these two below
    public Boolean canProceed() {
        Boolean canProceed = true;
        ArrayList<String> dataNames = new ArrayList<>(Arrays.asList("Speed", "Agility", "Counter Defense"));
        for (int i = 0; i < 3; i++) {
            String dataName = dataNames.get(i);
            int valOne = panelOne.getData().get(dataName);
            int valTwo = panelTwo.getData().get(dataName);
            int valThree = panelThree.getData().get(dataName);

            if (dataName.equals("Counter Defense")) {
                if ((valOne != 0 && valTwo != 0 && valOne != 1 && valTwo != 1 && valOne == valTwo) || (valOne != 0 && valThree != 0 && valOne != 1 && valThree != 1 && valOne == valThree) || (valTwo != 0 && valThree != 0 && valTwo != 1 && valThree != 1 && valTwo == valThree)) {
                    canProceed = false;
                    return canProceed;
                }
            } else {
                if ((valOne != 0 && valTwo != 0 && valOne == valTwo) || (valOne != 0 && valThree != 0 && valOne == valThree) || (valTwo != 0 && valThree != 0 && valTwo == valThree)) {
                    canProceed = false;
                    return canProceed;
                }
            }
        }
        return canProceed;
    }

    //The next Button, to see if boolean r valid
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.endDataShortcut) {
            inflateFinalDataMenu();
        }

        if (id == R.id.finalNext) {
            if (canProceed()) {

                listDataValues();
                sendExtras();
            } else {
                //toast
                final String NextString = "Active teams cannot have the same ranking values!";

                Toast.makeText(getApplicationContext(), NextString, Toast.LENGTH_LONG).show();

            }

        }

        return super.onOptionsItemSelected(item);
    }
// DEFENSE FOR TEAM N
    public void inflateDefenseDialog(final Integer teamPosition) {

        //TODO: IF THE BOX IS NOT CHECKED, SEND A ZERO

        final AlertDialog.Builder defenseBuilder = new AlertDialog.Builder(context);
        defenseBuilder.setView(R.layout.defense);
        defenseBuilder.setCancelable(false)
        .setTitle("Team " + teamsList.get(teamPosition - 1) + " Defensive Actions");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View defenseView = inflater.inflate(R.layout.defense, null);

        final SeekBar defenseSeekbar = (SeekBar) defenseView.findViewById(R.id.defenseSlider);
        final TextView defenseValue = (TextView) defenseView.findViewById(R.id.defenseValue);
        final Button knockingCheck = (Button) defenseView.findViewById(R.id.knockingButton);
        final Button dockingCheck = (Button) defenseView.findViewById(R.id.dockingButton);
        final Button pathblockingCheck = (Button) defenseView.findViewById(R.id.pathblockingButton);

        //Get previously inputted value of each SeekBar
        if (teamPosition == 1) {
            if (dValueOne != null) {
                defenseSeekbar.setProgress(dValueOne); defenseValue.setText(String.valueOf(dValueOne));
            } else {
                defenseSeekbar.setProgress(0);
                defenseValue.setText(0);
            }
        } else
            if (teamPosition == 2) {
                if (dValueTwo != null) {
                    defenseSeekbar.setProgress(dValueTwo); defenseValue.setText(String.valueOf(dValueTwo));
                } else {
                    defenseSeekbar.setProgress(0);
                    defenseValue.setText(0);
                }
            } else
                if (teamPosition == 3) {
                    if (dValueThree != null) {
                        defenseSeekbar.setProgress(dValueThree); defenseValue.setText(String.valueOf(dValueThree));
                    } else {
                        defenseSeekbar.setProgress(0);
                        defenseValue.setText(0);
                    }
                }
     
        defenseSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (teamPosition == 1) {
                    dValTextOne = String.valueOf(progress);
                    defenseValue.setText("" + dValTextOne);
                } else if (teamPosition == 2) {
                    dValTextOne = String.valueOf(progress);
                    defenseValue.setText("" + dValTextOne);
                } else if (teamPosition == 3) {
                    dValTextOne = String.valueOf(progress);
                    defenseValue.setText("" + dValTextOne);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                }
        });


        defenseBuilder.setView(defenseView);
        //End of onSeekBarChangeListeners
        defenseBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        defenseBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (teamPosition == 1) {
                    dValueOne = defenseSeekbar.getProgress();
                } else if (teamPosition == 2) {
                    dValueTwo = defenseSeekbar.getProgress();
                } else if (teamPosition == 3) {
                    dValueThree = defenseSeekbar.getProgress();
                }
                dialog.cancel();
            }
        });

        AlertDialog defenseAlert = defenseBuilder.create();
        defenseAlert.show();
    }

    public void inflateFinalDataMenu() {
        final AlertDialog.Builder endDataBuilder = new AlertDialog.Builder(context);
        endDataBuilder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View finalDataView = inflater.inflate(R.layout.finaldatapoints, null);
        if (allianceScoreInt != null && allianceScoreInt != 0) {
            ((EditText) finalDataView.findViewById(R.id.finalScoreEditText)).setText(String.valueOf(allianceScoreInt));
        }
        if (allianceFoulInt != null && allianceFoulInt != 0) {
            ((EditText) finalDataView.findViewById(R.id.finalFoulEditText)).setText(String.valueOf(allianceFoulInt));
        }
        ((Switch) finalDataView.findViewById(R.id.didRocketRP)).setChecked(didRocketRP);
        ((Switch) finalDataView.findViewById(R.id.didHabClimb)).setChecked(didHabClimb);
        endDataBuilder.setView(finalDataView);
        endDataBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        endDataBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog d = (Dialog) dialog;
                EditText scoreText = (EditText) d.findViewById(R.id.finalScoreEditText);
                EditText foulText = (EditText) d.findViewById(R.id.finalFoulEditText);
                Switch habClimb = (Switch) d.findViewById(R.id.didHabClimb);
                Switch rocketRP = (Switch) d.findViewById(R.id.didRocketRP);

                allianceFoulData = foulText.getText().toString();
                allianceScoreData = scoreText.getText().toString();
                didRocketRP = rocketRP.isChecked();
                didHabClimb = habClimb.isChecked();

                try {
                    allianceScoreInt = Integer.parseInt(allianceScoreData);
                    allianceFoulInt = Integer.parseInt(allianceFoulData);
                } catch (NumberFormatException nfe) {
                    allianceScoreInt = 0;
                    allianceFoulInt = 0;
                } catch (NullPointerException npe) {
                    allianceScoreInt = 0;
                    allianceFoulInt = 0;
                }
                dialog.cancel();
            }
        });
        AlertDialog endDataDialog = endDataBuilder.create();
        endDataDialog.show();
        if (isRed) {
            ((TextView) endDataDialog.findViewById(R.id.finalScoreTextView)).setTextColor(Color.RED);
        } else {
            ((TextView) endDataDialog.findViewById(R.id.finalScoreTextView)).setTextColor(Color.BLUE);
        }
    }

    public void getExtrasForScouting() {

        numberOfMatch = next.getExtras().getString("matchNumber");
        teamNumberOne = next.getExtras().getString("teamNumberOne");
        teamNumberTwo = next.getExtras().getString("teamNumberTwo");
        teamNumberThree = next.getExtras().getString("teamNumberThree");
        alliance = next.getExtras().getString("alliance");
        dataBaseUrl = next.getExtras().getString("dataBaseUrl");
        isMute = next.getExtras().getBoolean("mute");
        isRed = next.getExtras().getBoolean("allianceColor");

        leftNear = next.getStringExtra(Constants.leftNear);
        leftFar = next.getStringExtra(Constants.leftMid);
        leftFar = next.getStringExtra(Constants.leftFar);
        rightNear = next.getStringExtra(Constants.rightNear);
        rightMid = next.getStringExtra(Constants.rightMid);
        rightFar = next.getStringExtra(Constants.rightFar);

        noShowOne = next.getExtras().getString("noShowOne");
        noShowTwo = next.getExtras().getString("noShowTwo");
        noShowThree = next.getExtras().getString("noShowThree");

        teamOneConflict = next.getExtras().getString("teamOneConflict");
        teamTwoConflict = next.getExtras().getString("teamTwoConflict");
        teamThreeConflict = next.getExtras().getString("teamThreeConflict");

        noShowOnePanel = noShowOne;
        noShowTwoPanel = noShowTwo;
        noShowThreePanel = noShowThree;

        teamOne = teamNumberOne;
        teamTwo = teamNumberTwo;
        teamThree = teamNumberThree;
    }

    public void setPanels() {

        panelOne.setAllianceColor(alliance, noShowOnePanel);
        panelOne.setTeamNumber(teamNumberOne, noShowOnePanel);
        panelTwo.setAllianceColor(alliance, noShowTwoPanel);
        panelTwo.setTeamNumber(teamNumberTwo, noShowTwoPanel);
        panelThree.setAllianceColor(alliance, noShowThreePanel);
        panelThree.setTeamNumber(teamNumberThree, noShowThreePanel);
    }
    // TODO: Fix the above error.
    public void sendExtras() {
        Intent intent = new Intent(this, FinalDataPoints.class);
        intent.putExtra("teamOneNotes", teamOneNotes);
        intent.putExtra("teamTwoNotes", teamTwoNotes);
        intent.putExtra("teamThreeNotes", teamThreeNotes);
        intent.putExtra("teamOneBooleanTippy", String.valueOf(teamNumberOneBooleanTippy));
        intent.putExtra("teamTwoBooleanTippy",String.valueOf(teamNumberTwoBooleanTippy));
        intent.putExtra("teamThreeBooleanTippy",String.valueOf(teamNumberThreeBooleanTippy));
        intent.putExtra("teamOneBooleanAlignment", String.valueOf(teamNumberOneBooleanAlignment));
        intent.putExtra("teamTwoBooleanAlignment",String.valueOf(teamNumberTwoBooleanAlignment));
        intent.putExtra("teamThreeBooleanAlignment", String.valueOf(teamNumberThreeBooleanAlignment));
        intent.putExtra("teamOneBooleanGrip",String.valueOf(teamNumberOneBooleanGrip));
        intent.putExtra("teamTwoBooleanGrip",String.valueOf(teamNumberTwoBooleanGrip));
        intent.putExtra("teamThreeBooleanGrip",String.valueOf(teamNumberThreeBooleanGrip));
        intent.putExtra("teamOneBooleanInterference",String.valueOf(teamNumberThreeBooleanInterference));
        intent.putExtra("teamTwoBooleanInterference",String.valueOf(teamNumberTwoBooleanInterference));
        intent.putExtra("teamThreeBooleanInterference", String.valueOf(teamNumberThreeBooleanInterference));
        intent.putExtra(Constants.leftNear,leftNear);
        intent.putExtra(Constants.leftMid,leftMid);
        intent.putExtra(Constants.leftFar,leftFar);
        intent.putExtra(Constants.rightNear,rightNear);
        intent.putExtra(Constants.rightMid,rightMid);
        intent.putExtra(Constants.rightFar, rightFar);
        intent.putExtra("matchNumber", numberOfMatch);
        intent.putExtra("teamNumberOne", teamNumberOne);
        intent.putExtra("teamNumberTwo", teamNumberTwo);
        intent.putExtra("teamNumberThree", teamNumberThree);
        intent.putExtra("alliance", alliance);
        intent.putExtra("dataBaseUrl", dataBaseUrl);
        intent.putExtra("allianceScore", allianceScoreData);
        intent.putExtra("allianceFoul", allianceFoulData);
        intent.putExtra("teamOneDocking", String.valueOf(dValueOne));
        intent.putExtra("teamOneKnocking", String.valueOf(dValueOne));
        intent.putExtra("teamOnePathblocking", String.valueOf(dValueOne));
        intent.putExtra("teamTwoDocking", String.valueOf(dValueTwo));
        intent.putExtra("teamTwoKnocking", String.valueOf(dValueTwo));
        intent.putExtra("teamTwoPathblocking", String.valueOf(dValueTwo));
        intent.putExtra("teamThreeDocking", String.valueOf(dValueThree));
        intent.putExtra("teamThreeKnocking", String.valueOf(dValueThree));
        intent.putExtra("teamThreePathblocking", String.valueOf(dValueThree));
        intent.putExtra("mute", isMute);
        intent.putExtra("noShowOne",noShowOne);
        intent.putExtra("noShowTwo",noShowTwo);
        intent.putExtra("noShowThree",noShowThree);
        intent.putStringArrayListExtra("dataNameOne", teamOneDataName);
        intent.putStringArrayListExtra("ranksOfOne", teamOneDataScore);
        intent.putStringArrayListExtra("dataNameTwo", teamTwoDataName);
        intent.putStringArrayListExtra("ranksOfTwo", teamTwoDataScore);
        intent.putStringArrayListExtra("dataNameThree", teamThreeDataName);
        intent.putStringArrayListExtra("ranksOfThree", teamThreeDataScore);
        intent.putExtra("didRocketRP",String.valueOf(didRocketRP));
        intent.putExtra("didHabClimb",String.valueOf(didHabClimb));
        intent.putExtra("teamOneConflict", teamOneConflict);
        intent.putExtra("teamTwoConflict",teamTwoConflict);
        intent.putExtra("teamThreeConflict",teamThreeConflict);
        intent.putExtras(next);
        startActivity(intent);
    }

    public void listDataValues() {
        teamOneDataName = new ArrayList<>(panelOne.getData().keySet());
        teamTwoDataName = new ArrayList<>(panelTwo.getData().keySet());
        teamThreeDataName = new ArrayList<>(panelThree.getData().keySet());
        teamOneDataScore = new ArrayList<>();
        teamTwoDataScore = new ArrayList<>();
        teamThreeDataScore = new ArrayList<>();

        for (int i = 0; i < teamOneDataName.size(); i++) {
            teamOneDataScore.add(panelOne.getData().get(teamOneDataName.get(i)).toString());
        }
        for (int i = 0; i < teamTwoDataName.size(); i++) {
            teamTwoDataScore.add(panelTwo.getData().get(teamTwoDataName.get(i)).toString());
        }
        for (int i = 0; i < teamThreeDataName.size(); i++) {
            teamThreeDataScore.add(panelThree.getData().get(teamThreeDataName.get(i)).toString());
        }
    }
    public void getPanels() {
        panelOne = (SuperScoutingPanel) getSupportFragmentManager().findFragmentById(R.id.panelOne);
        panelTwo = (SuperScoutingPanel) getSupportFragmentManager().findFragmentById(R.id.panelTwo);
        panelThree = (SuperScoutingPanel) getSupportFragmentManager().findFragmentById(R.id.panelThree);
    }

    public void initializeTeamTextViews() {
        teamNumberOneTextview = (TextView) panelOne.getView().findViewById(R.id.teamNumberTextView);
        teamNumberTwoTextview = (TextView) panelTwo.getView().findViewById(R.id.teamNumberTextView);
        teamNumberThreeTextview = (TextView) panelThree.getView().findViewById(R.id.teamNumberTextView);

        teamNumberOneTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String teamNumber = teamNumberOneTextview.getText().toString();
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout teamOneNotesLayout = (LinearLayout)layoutInflater.inflate(R.layout.team_notes, null);
                final EditText teamOneNotesEditText = (EditText)teamOneNotesLayout.findViewById(R.id.notesEditText);
                final CheckBox teamNumberOneCheckboxTippy = (CheckBox) teamOneNotesLayout.findViewById(R.id.checkboxTippy);
                final CheckBox teamNumberOneCheckboxAlignment = (CheckBox) teamOneNotesLayout.findViewById(R.id.checkboxAlignment);
                final CheckBox teamNumberOneCheckboxGrip = (CheckBox) teamOneNotesLayout.findViewById(R.id.checkboxGrip);
                final CheckBox teamNumberOneCheckboxInterference = (CheckBox) teamOneNotesLayout.findViewById(R.id.checkboxInterference);

                if (!teamOneNotes.equals("")) {teamOneNotesEditText.setText(teamOneNotes); }
                teamOneNotesEditText.setTextColor(Color.BLACK);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Super Notes for team " + teamNumber).setView(teamOneNotesLayout).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) { teamOneNotes = teamOneNotesEditText.getText().toString();
                                teamNumberOneBooleanTippy = teamNumberOneCheckboxTippy.isChecked();
                                teamNumberOneBooleanAlignment = teamNumberOneCheckboxAlignment.isChecked();
                                teamNumberOneBooleanGrip = teamNumberOneCheckboxGrip.isChecked();
                                teamNumberOneBooleanInterference = teamNumberOneCheckboxInterference.isChecked();}
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();

            }
        });
        teamNumberTwoTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String teamNumber = teamNumberTwoTextview.getText().toString();

                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout teamTwoNotesLayout = (LinearLayout)layoutInflater.inflate(R.layout.team_notes, null);
                final EditText teamTwoNotesEditText = (EditText)teamTwoNotesLayout.findViewById(R.id.notesEditText);
                final CheckBox teamNumberTwoCheckboxTippy = (CheckBox) teamTwoNotesLayout.findViewById(R.id.checkboxTippy);
                final CheckBox teamNumberTwoCheckboxAlignment = (CheckBox) teamTwoNotesLayout.findViewById(R.id.checkboxAlignment);
                final CheckBox teamNumberTwoCheckboxGrip = (CheckBox) teamTwoNotesLayout.findViewById(R.id.checkboxGrip);
                final CheckBox teamNumberTwoCheckboxInterference = (CheckBox) teamTwoNotesLayout.findViewById(R.id.checkboxInterference);

                if (!teamTwoNotes.equals("")) {teamTwoNotesEditText.setText(teamTwoNotes); }
                teamTwoNotesEditText.setTextColor(Color.BLACK);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Super Notes for " + teamNumber).setView(teamTwoNotesLayout).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) { teamTwoNotes = teamTwoNotesEditText.getText().toString();
                                teamNumberTwoBooleanTippy = teamNumberTwoCheckboxTippy.isChecked();
                                teamNumberTwoBooleanAlignment = teamNumberTwoCheckboxAlignment.isChecked();
                                teamNumberTwoBooleanGrip = teamNumberTwoCheckboxGrip.isChecked();
                                teamNumberTwoBooleanInterference = teamNumberTwoCheckboxInterference.isChecked();}
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
        teamNumberThreeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String teamNumber = teamNumberThreeTextview.getText().toString();
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout teamThreeNotesLayout = (LinearLayout)layoutInflater.inflate(R.layout.team_notes, null);
                final EditText teamThreeNotesEditText = (EditText)teamThreeNotesLayout.findViewById(R.id.notesEditText);
                final CheckBox teamNumberThreeCheckboxTippy = (CheckBox) teamThreeNotesLayout.findViewById(R.id.checkboxTippy);
                final CheckBox teamNumberThreeCheckboxAlignment = (CheckBox) teamThreeNotesLayout.findViewById(R.id.checkboxAlignment);
                final CheckBox teamNumberThreeCheckboxGrip = (CheckBox) teamThreeNotesLayout.findViewById(R.id.checkboxGrip);
                final CheckBox teamNumberThreeCheckboxInterference = (CheckBox) teamThreeNotesLayout.findViewById(R.id.checkboxInterference);

                if (!teamThreeNotes.equals("")) {teamThreeNotesEditText.setText(teamThreeNotes); }
                teamThreeNotesEditText.setTextColor(Color.BLACK);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Super Notes for " + teamNumber).setView(teamThreeNotesLayout).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {teamThreeNotes = teamThreeNotesEditText.getText().toString();
                                teamNumberThreeBooleanTippy = teamNumberThreeCheckboxTippy.isChecked();
                                teamNumberThreeBooleanAlignment = teamNumberThreeCheckboxAlignment.isChecked();
                                teamNumberThreeBooleanGrip = teamNumberThreeCheckboxGrip.isChecked();
                                teamNumberThreeBooleanInterference = teamNumberThreeCheckboxInterference.isChecked();}
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }


}