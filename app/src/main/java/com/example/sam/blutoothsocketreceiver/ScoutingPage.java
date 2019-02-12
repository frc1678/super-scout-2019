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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
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

    Integer kVal;
    Integer pVal;
    Integer dVal;

    String kValText;
    String pValText;
    String dValText;

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
        teamOneNotes = "";
        teamTwoNotes = "";
        teamThreeNotes = "";

        final Button defenseButtonOne = (Button) findViewById(R.id.defenseRobotOneButton);
        defenseButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateDefenseDialogOne();
            }
        });

       /* Button defenseButtonTwo = (Button) findViewById(R.id.defenseRobotTwoButton);
        defenseButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateDefenseDialogTwo();
            }
        });

        Button defenseButtonThree = (Button) findViewById(R.id.defenseRobotThreeButton);
        defenseButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateDefenseDialogThree();
            }
        });*/
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

    public void inflateDefenseDialogOne() {

        final AlertDialog.Builder defenseBuilder = new AlertDialog.Builder(context);
        defenseBuilder.setView(R.layout.defense);
        defenseBuilder.setCancelable(false)
        .setTitle("Team " + teamNumberOne + " Defensive Actions");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View defenseView = inflater.inflate(R.layout.defense, null);

        final SeekBar kDefense = (SeekBar) defenseView.findViewById(R.id.knockingSlider);
        final SeekBar dDefense = (SeekBar) defenseView.findViewById(R.id.dockingSlider);
        final SeekBar pDefense = (SeekBar) defenseView.findViewById(R.id.pathblockingSlider);
        final CheckBox knockingCheck = (CheckBox) defenseView.findViewById(R.id.knockingCheck);
        final CheckBox dockingCheck = (CheckBox) defenseView.findViewById(R.id.dockingCheck);
        final CheckBox pathblockingCheck = (CheckBox) defenseView.findViewById(R.id.pathblockingCheck);
        final TextView kBarValue = (TextView) defenseView.findViewById(R.id.kValue);
        final TextView dBarValue = (TextView) defenseView.findViewById(R.id.dValue);
        final TextView pBarValue = (TextView) defenseView.findViewById(R.id.pValue);

        //Get previously inputted value of each SeekBar
        try {
            if (dVal != null) {
                dDefense.setProgress(dVal);
                dBarValue.setText(dValText);
            } else if (dVal == 0) {
                dDefense.setProgress(0);
                dBarValue.setText(0);
            }
            if (kVal != null) {
                kDefense.setProgress(kVal);
                kBarValue.setText(kValText);
            } else if (kVal == 0) {
                kDefense.setProgress(0);
                kBarValue.setText(0);
            }
            if (pVal != null) {
                pDefense.setProgress(pVal);
                pBarValue.setText(pValText);
            } else if (pVal == 0) {
                pDefense.setProgress(0);
                pBarValue.setText(0);
            }
        }
        catch (NullPointerException npe) {
            dVal = 0;
            kVal = 0;
            pVal = 0;
        }
        catch (NumberFormatException nfe) {
            dVal = 0;
            kVal = 0;
            pVal = 0;
        }


        dDefense.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dVal = progress;
                }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                dBarValue.setText("" + dVal);
                }
        });

        kDefense.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                kVal = progress;
                }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                kBarValue.setText("" + kVal);
                }
        });

        pDefense.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pBarValue.setText("" + pVal);
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
               kVal = kDefense.getProgress();
               dVal = dDefense.getProgress();
               pVal = pDefense.getProgress();

               kValText = kVal.toString();
               dValText = dVal.toString();
               pValText = pVal.toString();

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

        panelOne.setAllianceColor(isRed, noShowOnePanel);
        panelOne.setTeamNumber(teamNumberOne, noShowOnePanel);
        panelTwo.setAllianceColor(isRed, noShowTwoPanel);
        panelTwo.setTeamNumber(teamNumberTwo, noShowTwoPanel);
        panelThree.setAllianceColor(isRed, noShowThreePanel);
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