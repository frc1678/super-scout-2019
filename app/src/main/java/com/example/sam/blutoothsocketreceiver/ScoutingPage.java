package com.example.sam.blutoothsocketreceiver;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    Map<String, Integer> allianceCubesForPowerup = new HashMap<>();
    Integer allianceScoreInt = 0;
    Integer allianceFoulInt = 0;
    Boolean facedTheBoss = false;
    Boolean didAutoQuest = false;
    Integer boostC = 0;
    Integer levitateC = 0;
    Integer forceC = 0;
    Boolean isMute;
    JSONObject object;
    Intent next;
    DatabaseReference dataBase;
    Boolean isRed;
    String teamOneNotes;
    String teamTwoNotes;
    String teamThreeNotes;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ToggleButton levitate;
    Integer levitateNum = 0;
    Counter boostCounterView;
    Counter levitateCounterView;
    Counter forceCounterView;
    SuperScoutingPanel panelOne;
    SuperScoutingPanel panelTwo;
    SuperScoutingPanel panelThree;
    RadioButton f0;
    RadioButton f1;
    RadioButton f2;
    RadioButton f3;
    RadioButton b0;
    RadioButton b1;
    RadioButton b2;
    RadioButton b3;

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
        context = this;
        levitate = (ToggleButton) findViewById(R.id.Lev);

        teamOneNotes = "";
        teamTwoNotes = "";
        teamThreeNotes = "";

        allianceCubesForPowerup.put("Force", 0);
        allianceCubesForPowerup.put("Boost", 0);
        allianceCubesForPowerup.put("Levitate", 0);
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

    // work these two below
    public Boolean canProceed() {
        Boolean canProceed = true;
        ArrayList<String> dataNames = new ArrayList<>(Arrays.asList("Speed", "Agility", "Defense"));
        for (int i = 0; i < 3; i++) {
            String dataName = dataNames.get(i);
            int valOne = panelOne.getData().get(dataName);
            int valTwo = panelTwo.getData().get(dataName);
            int valThree = panelThree.getData().get(dataName);

            if(dataName.equals("Defense")) {
                if ((valOne != 0 && valTwo != 0 && valOne != 1 && valTwo != 1 && valOne == valTwo) || (valOne != 0 && valThree != 0 && valOne != 1 && valThree != 1 && valOne == valThree) || (valTwo != 0 && valThree != 0 && valTwo != 1 && valThree != 1 && valTwo == valThree)){
                    canProceed = false;
                    return canProceed;
                }
            } else {
                if ((valOne != 0 && valTwo != 0 && valOne == valTwo) || (valOne != 0 && valThree != 0 && valOne == valThree) || (valTwo != 0 && valThree != 0 && valTwo == valThree)){
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

                levitateNum = 0;

                if(levitate.isChecked()) {
                    levitateNum = 3;
                }
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






    public void inflateFinalDataMenu() {
        final AlertDialog.Builder endDataBuilder = new AlertDialog.Builder(context);
        endDataBuilder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View finalDataView = inflater.inflate(R.layout.finaldatapoints, null);
        boostCounterView = (Counter) finalDataView.findViewById(R.id.BoostCounter);
        levitateCounterView = (Counter) finalDataView.findViewById(R.id.LevitateCounter);
        forceCounterView = (Counter) finalDataView.findViewById(R.id.ForceCounter);
        if (allianceScoreInt != null && allianceScoreInt != 0) {
            ((EditText) finalDataView.findViewById(R.id.finalScoreEditText)).setText(String.valueOf(allianceScoreInt));
        }
        if (allianceFoulInt != null && allianceFoulInt != 0) {
            ((EditText) finalDataView.findViewById(R.id.finalFoulEditText)).setText(String.valueOf(allianceFoulInt));
        }
        ((Switch) finalDataView.findViewById(R.id.didAutoQuestBoolean)).setChecked(didAutoQuest);
        ((Switch) finalDataView.findViewById(R.id.didFaceBossBoolean)).setChecked(facedTheBoss);
        if (boostC != null) {
            boostCounterView.refreshCounter(boostC);
        }
        if (levitateC != null) {
            levitateCounterView.refreshCounter(levitateC);
        }
        if (forceC != null) {
            forceCounterView.refreshCounter(forceC);
        }
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
                Switch facedBoss = (Switch) d.findViewById(R.id.didFaceBossBoolean);
                Switch completedAutoQuest = (Switch) d.findViewById(R.id.didAutoQuestBoolean);

                allianceFoulData = foulText.getText().toString();
                allianceScoreData = scoreText.getText().toString();
                didAutoQuest = completedAutoQuest.isChecked();
                facedTheBoss = facedBoss.isChecked();
                boostC = boostCounterView.getDataValue();
                forceC = forceCounterView.getDataValue();
                levitateC = levitateCounterView.getDataValue();

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
    }


    public void setPanels() {
        panelOne.setAllianceColor(isRed);
        panelOne.setTeamNumber(teamNumberOne);
        panelTwo.setAllianceColor(isRed);
        panelTwo.setTeamNumber(teamNumberTwo);
        panelThree.setAllianceColor(isRed);
        panelThree.setTeamNumber(teamNumberThree);
    }
    // TODO: Fix the above error.
    public void sendExtras() {
        Intent intent = new Intent(this, FinalDataPoints.class);
        intent.putExtra("teamOneNotes", teamOneNotes);
        intent.putExtra("teamTwoNotes", teamTwoNotes);
        intent.putExtra("teamThreeNotes", teamThreeNotes);
        intent.putExtra("matchNumber", numberOfMatch);
        intent.putExtra("teamNumberOne", teamNumberOne);
        intent.putExtra("teamNumberTwo", teamNumberTwo);
        intent.putExtra("teamNumberThree", teamNumberThree);
        intent.putExtra("alliance", alliance);
        intent.putExtra("dataBaseUrl", dataBaseUrl);
        intent.putExtra("allianceScore", allianceScoreData);
        intent.putExtra("allianceFoul", allianceFoulData);
        intent.putExtra("levitateCount", levitateC);
        intent.putExtra("forceCount", forceC);
        intent.putExtra("boostCount", boostC);
        intent.putExtra("completedAutoQuest", didAutoQuest);
        System.out.println(didAutoQuest);
        intent.putExtra("facedTheBoss", facedTheBoss);
        intent.putExtra("forceForPowerup", allianceCubesForPowerup.get("Force"));
        intent.putExtra("boostForPowerup", allianceCubesForPowerup.get("Boost"));
        intent.putExtra("levitateForPowerup", levitateNum);
        intent.putExtra("mute", isMute);
        intent.putStringArrayListExtra("dataNameOne", teamOneDataName);
        intent.putStringArrayListExtra("ranksOfOne", teamOneDataScore);
        intent.putStringArrayListExtra("dataNameTwo", teamTwoDataName);
        intent.putStringArrayListExtra("ranksOfTwo", teamTwoDataScore);
        intent.putStringArrayListExtra("dataNameThree", teamThreeDataName);
        intent.putStringArrayListExtra("ranksOfThree", teamThreeDataScore);
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

                if (!teamOneNotes.equals("")) {
                    teamOneNotesEditText.setText(teamOneNotes);
                }
                teamOneNotesEditText.setTextColor(Color.BLACK);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Super Notes for team " + teamNumber)
                        .setView(teamOneNotesLayout)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                teamOneNotes = teamOneNotesEditText.getText().toString();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
        teamNumberTwoTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String teamNumber = teamNumberTwoTextview.getText().toString();

                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout teamTwoNotesLayout = (LinearLayout)layoutInflater.inflate(R.layout.team_notes, null);
                final EditText teamTwoNotesEditText = (EditText)teamTwoNotesLayout.findViewById(R.id.notesEditText);

                if (!teamTwoNotes.equals("")) {
                    teamTwoNotesEditText.setText(teamTwoNotes);
                }
                teamTwoNotesEditText.setTextColor(Color.BLACK);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Super Notes for " + teamNumber)
                        .setView(teamTwoNotesLayout)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                teamTwoNotes = teamTwoNotesEditText.getText().toString();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
        teamNumberThreeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String teamNumber = teamNumberThreeTextview.getText().toString();

                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout teamThreeNotesLayout = (LinearLayout)layoutInflater.inflate(R.layout.team_notes, null);
                final EditText teamThreeNotesEditText = (EditText)teamThreeNotesLayout.findViewById(R.id.notesEditText);

                if (!teamThreeNotes.equals("")) {
                    teamThreeNotesEditText.setText(teamThreeNotes);
                }
                teamThreeNotesEditText.setTextColor(Color.BLACK);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Super Notes for " + teamNumber)
                        .setView(teamThreeNotesLayout)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                teamThreeNotes = teamThreeNotesEditText.getText().toString();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    public void ForceDialogs(View view){
        AlertDialog.Builder forceDialog = new AlertDialog.Builder(context);
        final View forceView = LayoutInflater.from(context).inflate(R.layout.force_dialog, null);
        f0 = (RadioButton) forceView.findViewById(R.id.forceZero);
        f1 = (RadioButton) forceView.findViewById(R.id.forceone);
        f2 = (RadioButton) forceView.findViewById(R.id.forcetwo);
        f3 = (RadioButton) forceView.findViewById(R.id.forcethree);
        if (allianceCubesForPowerup.get("Force").equals(3)){
            f3.setChecked(true);
        } else if (allianceCubesForPowerup.get("Force").equals(2)){
            f2.setChecked(true);
        } else if (allianceCubesForPowerup.get("Force").equals(1)){
            f1.setChecked(true);
        } else {
            f0.setChecked(true);
        }

        forceDialog.setCancelable(false);
        forceDialog.setView(forceView);
        forceDialog.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<RadioButton> forceButtons = new ArrayList<>(Arrays.asList(f0, f1, f2, f3));
                        for (int i = 0; i < forceButtons.size(); i++){
                            if(forceButtons.get(i).isChecked()){
                                allianceCubesForPowerup.put("Force", Integer.parseInt(forceButtons.get(i).getText().toString()));
                                Log.e("MAP", allianceCubesForPowerup.toString());
                            }
                        }
                    }
                });

        forceDialog.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = forceDialog.create();
        alert11.show();
    }

    public void BoostDialogs(View view) {
        AlertDialog.Builder boostDialog = new AlertDialog.Builder(context);
        final View boostView = LayoutInflater.from(context).inflate(R.layout.boost_dialog, null);
        b0 = (RadioButton)boostView.findViewById(R.id.boostzero);
        b1 = (RadioButton) boostView.findViewById(R.id.boostone);
        b2 = (RadioButton) boostView.findViewById(R.id.boosttwo);
        b3 = (RadioButton) boostView.findViewById(R.id.boostthree);
        if (allianceCubesForPowerup.get("Boost").equals(3)){
            b3.setChecked(true);
        } else if (allianceCubesForPowerup.get("Boost").equals(2)){
            b2.setChecked(true);
        } else if (allianceCubesForPowerup.get("Boost").equals(1)){
            b1.setChecked(true);
        } else {
            b0.setChecked(true);
        }
        System.out.println(allianceCubesForPowerup.get("Boost").toString());
        boostDialog.setCancelable(false);
        boostDialog.setView(boostView);
        boostDialog.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<RadioButton> boostButtons = new ArrayList<>(Arrays.asList(b0, b1, b2, b3));
                        for (int i = 0; i < boostButtons.size(); i++){
                            if(boostButtons.get(i).isChecked()){
                                allianceCubesForPowerup.put("Boost", Integer.parseInt(boostButtons.get(i).getText().toString()));
                                Log.e("MAP", allianceCubesForPowerup.toString());
                            }
                        }
                    }
                });

        boostDialog.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = boostDialog.create();
        alert11.show();
    }

}


