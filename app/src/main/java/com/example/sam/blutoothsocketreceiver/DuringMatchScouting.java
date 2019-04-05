package com.example.sam.blutoothsocketreceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sam.blutoothsocketreceiver.Utils.TimerUtil;

import java.util.ArrayList;

public class DuringMatchScouting extends AppCompatActivity { //Comments will be included to explain meanings of variable names
															 //to prevent unnecessary confusion.

	//Uninitialized variables will be separated into the groups they are in within the code (ex, columns together or sections together)

	public Button teamOneSelectionButton, teamTwoSelectionButton, teamThreeSelectionButton; //The three buttons to select which team is on defense

	public FrameLayout timerButton; //timer button is a frame layout because it has two text views inside of it (which a button can't have) to display
							        //the status (START, STOP) and the time itself. Will have an onClick to determine when timer is activated.

	public TextView timerStatusDisplay, timerTimeDisplay; //As stated above, the status is the 'START, STOP', and the time itself is the '135 --> 134'

	public TextView currentSelectedTeam, firstOpposingTeam, secondOpposingTeam, thirdOpposingTeam; //Current selected team refers to the team from
														// the SCOUT's alliance. The opposing teams are the three robots that defense is
														// being played against.

	public Button robotOneDefensiveReaction, robotTwoDefensiveReaction, robotThreeDefensiveReaction; //How a robot reacts to the defense being played
														// Button as it's a toggle (boolean type). Can either be 'RESISTOR' or 'COUNTER DEFENSE'
														// 'COUNTER DEFENSE' means they're responding by playing counter defense
														// 'RESISTOR' means they are not playing counter defense. AKA: if counter def, counter def.
																										// else, resistor.

	public Spinner currentRobotDefenseEffectiveness, robotOneDefensiveReactionEffectiveness, robotTwoDefensiveReactionEffectiveness, robotThreeDefensiveReactionEffectiveness;
							//For the SCOUT's alliance's robot, the dropdown menu (spinner) will have a selection on how effective their defense was
							//For the OPPONENT's alliance's robots, the dropdown menu (spinner) will have a selection on how effective their reaction was
																		// REACTION as in 'COUNTER DEFENSE' or 'RESISTOR' scaling.

	public Boolean timerIsActive = false; //When false, the DEFENSIVE timer is not running.

	public String teamOne, teamTwo, teamThree, opposingTeamOne, opposingTeamTwo, opposingTeamThree;
				//The first three are the three teams from the SCOUT's alliance. The opposing teams are from the OPPOSITE alliance.
	public String matchNumber, alliance; // <-

	public String selectedDefensiveRobot = "0"; //the value contains which team is on defense for the SCOUT's alliance.

	ArrayList<String> alliance_effectiveness_list = new ArrayList<String>() {{ //list for the dropdown menu for ranking defense for ALLIANCE
		add("No Attempt");
		add("Attempted");
		add("Effective");
		add("Shutdown");
	}};
	ArrayList<String> opponent_effectiveness_list = new ArrayList<String>() {{ //list for the dropdown menu for ranking defense for OPPONENTS
		add("Not Applicable");
		add("Poor C. Defense");
		add("Fair C. Defense");
		add("Good C. Defense");
	}};

	public int allianceRobotOneDefensiveEffectivenessValue, allianceRobotTwoDefensiveEffectivenessValue, allianceRobotThreeDefensiveEffectivenessValue;
	public int opponentRobotOneDefensiveEffectivenessValue, opponentRobotTwoDefensiveEffectivenessValue, opponentRobotThreeDefensiveEffectivenessValue;


	//Holds all of the data in a matrix
	public String[][] opponentDataStructure = new String[3][3];
				//First [], [O.teamOne, O.teamTwo, O.teamThree]
				//Second [] :
				//  [ robotDefensiveReaction ('RESISTOR' or 'C. DEFENDER'), robotDefensiveCDefenseEffectiveness ('Poor C. Defense', etc), O.team ]

	public String[][] allianceDataStructure = new String[3][3];
				//First [], [teamOne, teamTwo, teamThree]
				//Second [] :
				//  [ robotDefensiveEffectiveness (Effective, Attempted, etc), totalDefenseTime <- ?, team]

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) { //During Match Scouting is the MAIN scouting page for the super scout
		super.onCreate(savedInstanceState);                        //While in a match, this is the page you'll be on.
		setContentView(R.layout.during_match_scouting);            //       Know:
		initializeXML();                                           //   In the team number view, the top team is the team from the SCOUT's
		//                                                         //   alliance. This team can change by pressing the team that is on
		//                                                         //   defense from the three team selection section at the top of the
		//                                                         //   screen.
		//                                                         //   The three teams underneath that team are the three robots from the
		//                                                         //   OPPOSING alliance. Example:
		//                                                         //               Scout's alliance: Blue.
		//                                                         //               Top team: current blue robot on defense.
		//                                                         //               Bottom three teams: RED robots that are being defended
		getExtras(); //Used to get the teams and alliance from MainActivity
		activateListeners(); //Creates listeners for all onClicks.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.during_match_scouting, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.final_data) {
			setupForFinalData();
			intentFinalData();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed(){
		final Activity activity = this;
		new AlertDialog.Builder(this)
				.setTitle("WARNING!")
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

	//populates the last lists before finally going to the next activity
	public void setupForFinalData() {
		opponentDataStructure[0][0] = String.valueOf(robotOneDefensiveReaction.getText());
		opponentDataStructure[0][1] = String.valueOf(opponentRobotOneDefensiveEffectivenessValue);
		opponentDataStructure[0][2] = String.valueOf(opposingTeamOne);

		opponentDataStructure[1][0] = String.valueOf(robotTwoDefensiveReaction.getText());
		opponentDataStructure[1][1] = String.valueOf(opponentRobotTwoDefensiveEffectivenessValue);
		opponentDataStructure[1][2] = String.valueOf(opposingTeamTwo);

		opponentDataStructure[2][0] = String.valueOf(robotTwoDefensiveReaction.getText());
		opponentDataStructure[2][1] = String.valueOf(opponentRobotTwoDefensiveEffectivenessValue);
		opponentDataStructure[2][2] = String.valueOf(opposingTeamThree);


		allianceDataStructure[0][0] = String.valueOf(allianceRobotOneDefensiveEffectivenessValue);
		allianceDataStructure[1][0] = String.valueOf(allianceRobotTwoDefensiveEffectivenessValue);
		allianceDataStructure[2][0] = String.valueOf(allianceRobotThreeDefensiveEffectivenessValue);
		allianceDataStructure[0][2] = String.valueOf(teamOne);
		allianceDataStructure[1][2] = String.valueOf(teamTwo);
		allianceDataStructure[2][2] = String.valueOf(teamThree);

	}

	//the method that handles the intent to the next activity
	public void intentFinalData() {
		Intent intent = new Intent(getApplicationContext(), AfterMatchScouting.class);
		intent.putExtra("allianceDataStructure", allianceDataStructure);
		intent.putExtra("opponentDataStructure",opponentDataStructure);
		intent.putExtra("alliance", alliance);
		intent.putExtra("matchNumber", matchNumber);
		startActivity(intent);
	}

	public void getExtras() {
		matchNumber = getIntent().getExtras().getString("matchNumber");
		alliance = getIntent().getExtras().getString("alliance");
		teamOne = getIntent().getExtras().getString("allianceTeamOne");
		teamTwo = getIntent().getExtras().getString("allianceTeamTwo");
		teamThree = getIntent().getExtras().getString("allianceTeamThree");
		opposingTeamOne = getIntent().getExtras().getString("opponentTeamOne");
		opposingTeamTwo = getIntent().getExtras().getString("opponentTeamTwo");
		opposingTeamThree = getIntent().getExtras().getString("opponentTeamThree");
	}



	//initializes the xml elements with their according xml id's.
	public void initializeXML() {
		teamOneSelectionButton = (Button) findViewById(R.id.teamOneSelectionButton);
		teamTwoSelectionButton = (Button) findViewById(R.id.teamTwoSelectionButton);
		teamThreeSelectionButton = (Button) findViewById(R.id.teamThreeSelectionButton);

		timerButton = (FrameLayout) findViewById(R.id.timerFrameLayout);

		timerStatusDisplay = (TextView) findViewById(R.id.timerStatusDisplay);
		TimerUtil.mTimerView = (TextView) findViewById(R.id.timerTimeDisplay);

		currentSelectedTeam = (TextView) findViewById(R.id.currentSelectedTeam);
		firstOpposingTeam = (TextView) findViewById(R.id.firstOpposingTeam);
		secondOpposingTeam = (TextView) findViewById(R.id.secondOpposingTeam);
		thirdOpposingTeam = (TextView) findViewById(R.id.thirdOpposingTeam);

		robotOneDefensiveReaction = (Button) findViewById(R.id.robotOneDefensiveReaction);
		robotTwoDefensiveReaction = (Button) findViewById(R.id.robotTwoDefensiveReaction);
		robotThreeDefensiveReaction = (Button) findViewById(R.id.robotThreeDefensiveReaction);

		currentRobotDefenseEffectiveness = (Spinner) findViewById(R.id.currentRobotDefenseEffectiveness);
		robotOneDefensiveReactionEffectiveness = (Spinner) findViewById(R.id.robotOneDefensiveReactionEffectiveness);
		robotTwoDefensiveReactionEffectiveness = (Spinner) findViewById(R.id.robotTwoDefensiveReactionEffectiveness);
		robotThreeDefensiveReactionEffectiveness = (Spinner) findViewById(R.id.robotThreeDefensiveReactionEffectiveness);

	}

	//method that calls other methods to create listeners for on clicks
	public void activateListeners() {
		createTimerListener();
		createAllianceTeamSelectionListener();
		createAllianceTeamColors();
		createRobotDefensiveEffectiveness();
		createSpinnerClickListener();
		setTeamNumbersToTeamNumberView();
		setDefensiveValues();
		setTeamNumbersToTeamRankingView();
		setBackgroundOfDefenseRankingTeams();

	}

	//creates the listener for a click on the spinner (dropdown menu) in the ranking portion of the super scout
	public void createSpinnerClickListener() {
		ArrayAdapter<String> allianceSpinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, alliance_effectiveness_list);
		allianceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayAdapter<String> opponentSpinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, opponent_effectiveness_list);
		opponentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		currentRobotDefenseEffectiveness.setAdapter(allianceSpinnerAdapter);
		robotOneDefensiveReactionEffectiveness.setAdapter(opponentSpinnerAdapter);
		robotTwoDefensiveReactionEffectiveness.setAdapter(opponentSpinnerAdapter);
		robotThreeDefensiveReactionEffectiveness.setAdapter(opponentSpinnerAdapter);
		currentRobotDefenseEffectiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if (teamOne.equals(String.valueOf(currentSelectedTeam.getText()))) allianceRobotOneDefensiveEffectivenessValue = i;
				if (teamTwo.equals(String.valueOf(currentSelectedTeam.getText()))) allianceRobotTwoDefensiveEffectivenessValue = i;
				if (teamThree.equals(String.valueOf(currentSelectedTeam.getText()))) allianceRobotThreeDefensiveEffectivenessValue = i;
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		robotOneDefensiveReactionEffectiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				opponentRobotOneDefensiveEffectivenessValue = i;
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		robotTwoDefensiveReactionEffectiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				opponentRobotTwoDefensiveEffectivenessValue = i;
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		robotThreeDefensiveReactionEffectiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				opponentRobotThreeDefensiveEffectivenessValue = i;
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

	}

	//creates the listener for handling the on click of the effectiveness tracker in the ranking portion of the super scout
	//either 'RESISTOR' or 'COUNTER DEFENDER'
	public void createRobotDefensiveEffectiveness() {
		robotOneDefensiveReaction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				robotOneDefensiveReaction.setText(String.valueOf(robotOneDefensiveReaction.getText()).equals("RESISTOR") ?
						"C. DEFENDER" : "RESISTOR");
			}
		});
		robotTwoDefensiveReaction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				robotTwoDefensiveReaction.setText(String.valueOf(robotTwoDefensiveReaction.getText()).equals("RESISTOR") ?
						"C. DEFENDER" : "RESISTOR");
			}
		});
		robotThreeDefensiveReaction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				robotThreeDefensiveReaction.setText(String.valueOf(robotThreeDefensiveReaction.getText()).equals("RESISTOR") ?
						"C. DEFENDER" : "RESISTOR");
			}
		});
	}

	//sets the team numbers to the views in the actual ranking section of the screen (the column with four teams)
	public void setTeamNumbersToTeamRankingView() {
		currentSelectedTeam.setText(selectedDefensiveRobot.equals("0") ? "?" : selectedDefensiveRobot);

		firstOpposingTeam.setText(opposingTeamOne);
		secondOpposingTeam.setText(opposingTeamTwo);
		thirdOpposingTeam.setText(opposingTeamThree);
	}

	//sets the background of the 4 teams that are to be ranked in the ranking section (red if red alliance, blue if blue alliance)
	public void setBackgroundOfDefenseRankingTeams() {
		currentSelectedTeam.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.LightBlue));
		if (alliance.equals("Red Alliance")) currentSelectedTeam.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.CarlRed));

		//changed colors because it's the opposing alliance, so opposite colors.
		firstOpposingTeam.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.CarlRed));
		if (alliance.equals("Red Alliance")) firstOpposingTeam.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.LightBlue));
		secondOpposingTeam.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.CarlRed));
		if (alliance.equals("Red Alliance")) secondOpposingTeam.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.LightBlue));
		thirdOpposingTeam.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.CarlRed));
		if (alliance.equals("Red Alliance")) thirdOpposingTeam.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.LightBlue));

	}

	//creates the on click listener for the three team number selection at the top of the screen
	//team that is chosen becomes the team on defense
	public void createAllianceTeamSelectionListener() {
		teamOneSelectionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!selectedDefensiveRobot.equals(teamOne)) selectedDefensiveRobot = teamOne;
				createAllianceTeamColors();
			}
		});
		teamTwoSelectionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!selectedDefensiveRobot.equals(teamTwo)) selectedDefensiveRobot = teamTwo;
				createAllianceTeamColors();
			}
		});
		teamThreeSelectionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!selectedDefensiveRobot.equals(teamThree)) selectedDefensiveRobot = teamThree;
				createAllianceTeamColors();
			}
		});

	}

	//sets each defensive value to "0", or the default value to avoid null values
	public void setDefensiveValues() {
		allianceRobotOneDefensiveEffectivenessValue = 0;
		allianceRobotTwoDefensiveEffectivenessValue = 0;
		allianceRobotThreeDefensiveEffectivenessValue = 0;

		opponentRobotOneDefensiveEffectivenessValue = 0;
		opponentRobotTwoDefensiveEffectivenessValue = 0;
		opponentRobotThreeDefensiveEffectivenessValue = 0;
	}

	//sets the team numbers to the text views of the three team views at the top of the screen
	public void setTeamNumbersToTeamNumberView() {
		teamOneSelectionButton.setText(teamOne);
		teamTwoSelectionButton.setText(teamTwo);
		teamThreeSelectionButton.setText(teamThree);
	}

	//sets the colors of the teams at the top of the screen according to the alliance
	public void createAllianceTeamColors() {
		teamOneSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.Bloo));
		if (selectedDefensiveRobot.equals(teamOne)) teamOneSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.DarkBlue));
		if (alliance.equals("Red Alliance")) {
			teamOneSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.TeamNumberRed));
			if (selectedDefensiveRobot.equals(teamOne)) teamOneSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.Red));
		}
		teamTwoSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.Bloo));
		if (selectedDefensiveRobot.equals(teamTwo)) teamTwoSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.DarkBlue));
		if (alliance.equals("Red Alliance")) {
			teamTwoSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.TeamNumberRed));
			if (selectedDefensiveRobot.equals(teamTwo)) teamTwoSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.Red));
		}
		teamThreeSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.Bloo));
		if (selectedDefensiveRobot.equals(teamThree)) teamThreeSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.DarkBlue));
		if (alliance.equals("Red Alliance")) {
			teamThreeSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.TeamNumberRed));
			if (selectedDefensiveRobot.equals(teamThree)) teamThreeSelectionButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.Red));
		}
		setTeamNumbersToTeamRankingView();
	}

	//Creates a stopwatch timer that increases from 0 to count how long a robot has been on defense.
	public void createTimerListener() {
		final TimerUtil.Stopwatch defenseTimer = new TimerUtil.Stopwatch();
		timerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!timerIsActive && !selectedDefensiveRobot.equals("0")) {
					defenseTimer.start();
					timerStatusDisplay.setText("STOP");
					timerButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.TeamNumberRed));
					timerIsActive = true;
				} else if (timerIsActive) {
					if (String.valueOf(currentSelectedTeam.getText()).equals(teamOne)) allianceDataStructure[0][1] = allianceDataStructure[0][1] == null ?
							String.valueOf(TimerUtil.mTimerView.getText()) :
							String.valueOf(Integer.valueOf(allianceDataStructure[0][1]) + Integer.valueOf(TimerUtil.mTimerView.getText().toString()));
					if (String.valueOf(currentSelectedTeam.getText()).equals(teamTwo)) allianceDataStructure[1][1] = allianceDataStructure[1][1] == null ?
							String.valueOf(TimerUtil.mTimerView.getText()) :
							String.valueOf(Integer.valueOf(allianceDataStructure[1][1]) + Integer.valueOf(TimerUtil.mTimerView.getText().toString()));
					if (String.valueOf(currentSelectedTeam.getText()).equals(teamThree)) allianceDataStructure[2][1] = allianceDataStructure[2][1] == null ?
							String.valueOf(TimerUtil.mTimerView.getText()) :
							String.valueOf(Integer.valueOf(allianceDataStructure[2][1]) + Integer.valueOf(TimerUtil.mTimerView.getText().toString()));
					defenseTimer.stop();
					timerStatusDisplay.setText("START");
					timerButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.CarlGreen));
					timerIsActive = false;
				} else {
					Toast.makeText(DuringMatchScouting.this, "Please select a team before starting the DEFENSE timer!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


}
