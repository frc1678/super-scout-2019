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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
    Boolean teamNumberOneBooleanTippy=false, teamNumberOneBooleanAlignment=false, teamNumberOneBooleanGrip=false, teamNumberOneBooleanInterference=false;
    Boolean teamNumberTwoBooleanTippy=false, teamNumberTwoBooleanAlignment=false, teamNumberTwoBooleanGrip=false, teamNumberTwoBooleanInterference=false;
    Boolean teamNumberThreeBooleanTippy=false, teamNumberThreeBooleanAlignment=false, teamNumberThreeBooleanGrip=false, teamNumberThreeBooleanInterference=false;
    SuperScoutingPanel panelOne;
    SuperScoutingPanel panelTwo;
    SuperScoutingPanel panelThree;
    String leftNear;
    String leftMid;
    String leftFar;
    String rightNear;
    String rightMid;
    String rightFar;

    String noShowOne;
    String noShowTwo;
    String noShowThree;

    String defenseTextOne = "";
    String defenseTextTwo = "";
    String defenseTextThree = "";

    String CDTextOne = "";
    String CDTextTwo = "";
    String CDTextThree = "";


    static String noShowOnePanel;
    static String noShowTwoPanel;
    static String noShowThreePanel;
    static String teamOne;
    static String teamTwo;
    static String teamThree;

    String teamOneConflict;
    String teamTwoConflict;
    String teamThreeConflict;

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
        defenseSpinners();
        context = this;
        teamOneNotes = "";
        teamTwoNotes = "";
        teamThreeNotes = "";

        seekBarActiveThumb = getResources().getDrawable(R.drawable.seekbar_thumb);
        seekBarInactiveThumb = getResources().getDrawable(R.drawable.seekbar_thumb_inactive);

    }

    public void defenseSpinners() {

        final Spinner defenseSpinnerOne = (Spinner) findViewById(R.id.defenseSpinnerOne);

        ArrayAdapter<String> defenseAdapterOne = new ArrayAdapter<String>(ScoutingPage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.defenseValues));
        defenseAdapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defenseSpinnerOne.setAdapter(defenseAdapterOne);

        defenseSpinnerOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        defenseTextOne = convDefToNum(parent.getItemAtPosition(pos).toString());


    }
    public void onNothingSelected(AdapterView<?> parent) {

    }
        });

        Spinner defenseSpinnerTwo = (Spinner) findViewById(R.id.defenseSpinnerTwo);

        ArrayAdapter<String> defenseAdapterTwo = new ArrayAdapter<String>(ScoutingPage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.defenseValues));
        defenseAdapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defenseSpinnerTwo.setAdapter(defenseAdapterTwo);

        defenseSpinnerTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                defenseTextTwo = convDefToNum(parent.getItemAtPosition(pos).toString());
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Spinner defenseSpinnerThree = (Spinner) findViewById(R.id.defenseSpinnerThree);

        ArrayAdapter<String> defenseAdapterThree = new ArrayAdapter<String>(ScoutingPage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.defenseValues));
        defenseAdapterThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defenseSpinnerThree.setAdapter(defenseAdapterThree);

        defenseSpinnerThree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                defenseTextThree = convDefToNum(parent.getItemAtPosition(pos).toString());

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner CDSpinnerOne = (Spinner) findViewById(R.id.CDSpinnerOne);

        ArrayAdapter<String> CDAdapterOne = new ArrayAdapter<String>(ScoutingPage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.defenseValues));
        CDAdapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CDSpinnerOne.setAdapter(CDAdapterOne);

        CDSpinnerOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                CDTextOne = convDefToNum(parent.getItemAtPosition(pos).toString());

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner CDSpinnerTwo = (Spinner) findViewById(R.id.CDSpinnerTwo);

        ArrayAdapter<String> CDAdapterTwo = new ArrayAdapter<String>(ScoutingPage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.defenseValues));
        CDAdapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CDSpinnerTwo.setAdapter(CDAdapterTwo);

        CDSpinnerTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                CDTextTwo = convDefToNum(parent.getItemAtPosition(pos).toString());

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner CDSpinnerThree = (Spinner) findViewById(R.id.CDSpinnerThree);

        ArrayAdapter<String> CDAdapterThree = new ArrayAdapter<String>(ScoutingPage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.defenseValues));
        CDAdapterThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CDSpinnerThree.setAdapter(CDAdapterThree);

        CDSpinnerThree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                CDTextThree = convDefToNum(parent.getItemAtPosition(pos).toString());

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public String convDefToNum(String typeDef) {
        if (typeDef.equals("No Defense")) {
            return "0";
        }
        if (typeDef.equals("Ineffective Defense")) {
            return "1";
        }
        if (typeDef.equals("Effective Defense")) {
            return "2";
        }
        if (typeDef.equals("Shut Down Alliance")) {
            return "3";
        }
        return "0";
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
        ArrayList<String> dataNames = new ArrayList<>(Arrays.asList("Speed", "Agility"));
        for (int i = 0; i < 2; i++) {
            String dataName = dataNames.get(i);
            int valOne = panelOne.getData().get(dataName);
            int valTwo = panelTwo.getData().get(dataName);
            int valThree = panelThree.getData().get(dataName);

    if ((valOne != 0 && valTwo != 0 && valOne == valTwo) || (valOne != 0 && valThree != 0 && valOne == valThree) || (valTwo != 0 && valThree != 0 && valTwo == valThree)) {
                    canProceed = false;
                    return canProceed;
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
        if (alliance.equals("red")) {
            ((TextView) endDataDialog.findViewById(R.id.finalScoreTextView)).setTextColor(Color.RED);
        } else if (alliance.equals("blue")) {
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
        intent.putExtra("teamOneDefense", defenseTextOne);
        intent.putExtra("teamTwoDefense", defenseTextTwo);
        intent.putExtra("teamThreeDefense", defenseTextThree);
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
        intent.putExtra("teamOneDefense", defenseTextOne);
        intent.putExtra("teamTwoDefense", defenseTextTwo);
        intent.putExtra("teamThreeDefense", defenseTextThree);
        intent.putExtra("teamOneCounterDefense", CDTextOne);
        intent.putExtra("teamTwoCounterDefense", CDTextTwo);
        intent.putExtra("teamThreeCounterDefense", CDTextThree);
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
                
                if (teamNumberOneBooleanTippy) {teamNumberOneCheckboxTippy.setChecked(true);}
	            if (teamNumberOneBooleanAlignment) {teamNumberOneCheckboxAlignment.setChecked(true);}
	            if (teamNumberOneBooleanGrip) {teamNumberOneCheckboxGrip.setChecked(true);}
	            if (teamNumberOneBooleanInterference) {teamNumberOneCheckboxInterference.setChecked(true);}
	          
	            if (!teamOneNotes.equals("")) {teamOneNotesEditText.setText(teamOneNotes); }
                teamOneNotesEditText.setTextColor(Color.BLACK);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Super Notes: " + teamNumber).setView(teamOneNotesLayout).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
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

	            if (teamNumberTwoBooleanTippy) {teamNumberTwoCheckboxTippy.setChecked(true);}
	            if (teamNumberTwoBooleanAlignment) {teamNumberTwoCheckboxAlignment.setChecked(true);}
	            if (teamNumberTwoBooleanGrip) {teamNumberTwoCheckboxGrip.setChecked(true);}
	            if (teamNumberTwoBooleanInterference) {teamNumberTwoCheckboxInterference.setChecked(true);}

                if (!teamTwoNotes.equals("")) {teamTwoNotesEditText.setText(teamTwoNotes); }
                teamTwoNotesEditText.setTextColor(Color.BLACK);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Super Notes: " + teamNumber).setView(teamTwoNotesLayout).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
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

	            if (teamNumberThreeBooleanTippy) {teamNumberThreeCheckboxTippy.setChecked(true);}
	            if (teamNumberThreeBooleanAlignment) {teamNumberThreeCheckboxAlignment.setChecked(true);}
	            if (teamNumberThreeBooleanGrip) {teamNumberThreeCheckboxGrip.setChecked(true);}
	            if (teamNumberThreeBooleanInterference) {teamNumberThreeCheckboxInterference.setChecked(true);}

                if (!teamThreeNotes.equals("")) {teamThreeNotesEditText.setText(teamThreeNotes); }
                teamThreeNotesEditText.setTextColor(Color.BLACK);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Super Notes: " + teamNumber).setView(teamThreeNotesLayout).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
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