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

import com.example.sam.blutoothsocketreceiver.Utils.PushAbilityDialog;
import com.example.sam.blutoothsocketreceiver.Utils.TimerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

	public TimerUtil.Stopwatch defenseTimer = null;

	ArrayList<String> alliance_effectiveness_list = new ArrayList<String>() {{ //list for the dropdown menu for ranking defense for ALLIANCE

		add("N/A");
		add("Not effective");
		add("Slow down");
		add("Shut down");
	}};
	ArrayList<String> opponent_effectiveness_list_resistor = new ArrayList<String>() {{ //list for the dropdown menu for ranking defense for OPPONENTS
		add("N/A");
		add("Not affected");
		add("Slowed down");
		add("Shut down");
	}};

	public ArrayList<Map<String, String>> timelineRobotOne = new ArrayList<>();
	public ArrayList<Map<String, String>> timelineRobotTwo = new ArrayList<>();
	public ArrayList<Map<String, String>> timelineRobotThree = new ArrayList<>();

	public int allianceRobotOneDefensiveEffectivenessValue, allianceRobotTwoDefensiveEffectivenessValue, allianceRobotThreeDefensiveEffectivenessValue;
	public int opponentRobotOneDefensiveEffectivenessValue, opponentRobotTwoDefensiveEffectivenessValue ,opponentRobotThreeDefensiveEffectivenessValue;
	public int opponentRobotOneDefensiveEffectivenessValue_resistor, opponentRobotTwoDefensiveEffectivenessValue_resistor, opponentRobotThreeDefensiveEffectivenessValue_resistor;

	public int[][] defensiveEffectivenessValues = new int[3][6];

	Boolean opOne_r = true; Boolean opTwo_r = true; Boolean opThree_r = true;

	//Holds all of the data in a matrix
	public String[][] opponentDataStructure = new String[3][3];
				//First [], [O.teamOne, O.teamTwo, O.teamThree]
				//Second [] :
				//  [ robotDefensiveReaction ('RESISTOR' or 'C. DEFENDER'), robotDefensiveCDefenseEffectiveness ('Poor C. Defense', etc), O.team ]

	public String[][] allianceDataStructure = new String[3][3];
				//First [], [teamOne, teamTwo, teamThree]
				//Second [] :
				//  [ robotDefensiveEffectiveness (Effective, Attempted, etc), totalDefenseTime <- ?, team]

	ArrayList<Map<String, String>> opponentRobotOneDataStructure = new ArrayList<>();
	ArrayList<Map<String, String>> opponentRobotTwoDataStructure = new ArrayList<>();
	ArrayList<Map<String, String>> opponentRobotThreeDataStructure = new ArrayList<>();

	ArrayList<Map<String, String>> pushAbilityDataStructure = new ArrayList<>();

	int[] teams = new int[3];
	int[] opposingTeams = new int[3];

	PushAbilityDialog pushAbilityDialog;

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
		if (id == R.id.push_ability) {
			inflatePushAbilityDialog();
		}
		if (id == R.id.final_data) {
			setupForFinalData();
			intentFinalData();
		}
		return super.onOptionsItemSelected(item);
	}

	//gives the user a chance to stop a back press when the back button is pressed accidentally
	@Override
	public void onBackPressed(){
		if (defenseTimer.isRunning()) defenseTimer.stop();
		final Activity activity = this;
		new AlertDialog.Builder(this)
				.setTitle("WARNING!")
				.setMessage("GOING BACK WILL CAUSE LOSS OF DATA")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						new TimerUtil.MatchTimer().stopTimer();
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

		//sample: a[x][y]
		//in opponentDataStructure, [x] is the opposing alliance team number
		//[y] is the actual datapoint identifier (how they react to the defense played on them etc)

		//in allianceDataStructure, [x] is the alliance team number
		//[0] is the actual defensive effectiveness value for the team
		//[1] is the total amount of effective defense time played for the team
		//[2] is the team number

		opponentDataStructure[0][0] = String.valueOf(robotOneDefensiveReaction.getText());
		opponentDataStructure[0][1] = String.valueOf(opponentRobotOneDefensiveEffectivenessValue);
		opponentDataStructure[0][2] = String.valueOf(opposingTeamOne);

		opponentDataStructure[1][0] = String.valueOf(robotTwoDefensiveReaction.getText());
		opponentDataStructure[1][1] = String.valueOf(opponentRobotTwoDefensiveEffectivenessValue);
		opponentDataStructure[1][2] = String.valueOf(opposingTeamTwo);

		opponentDataStructure[2][0] = String.valueOf(robotThreeDefensiveReaction.getText());
		opponentDataStructure[2][1] = String.valueOf(opponentRobotThreeDefensiveEffectivenessValue);
		opponentDataStructure[2][2] = String.valueOf(opposingTeamThree);

		allianceDataStructure[0][0] = String.valueOf(allianceRobotOneDefensiveEffectivenessValue);
		allianceDataStructure[1][0] = String.valueOf(allianceRobotTwoDefensiveEffectivenessValue);
		allianceDataStructure[2][0] = String.valueOf(allianceRobotThreeDefensiveEffectivenessValue);
		allianceDataStructure[0][2] = String.valueOf(teamOne);
		allianceDataStructure[1][2] = String.valueOf(teamTwo);
		allianceDataStructure[2][2] = String.valueOf(teamThree);

		addOpponentData(opposingTeamOne); addOpponentData(opposingTeamTwo); addOpponentData(opposingTeamThree);

		if (defenseTimer.isRunning()) defenseTimer.stop();
		updateTimerColor();

	}

	public Integer indexOfTeamString() {
		if (teamOne.equals(selectedDefensiveRobot) || selectedDefensiveRobot.equals("0")) return 0;
		if (teamTwo.equals(selectedDefensiveRobot)) return 1;
		if (teamThree.equals(selectedDefensiveRobot)) return 2;
		return null;
	}

	public void addOpponentData(String team) {
		Map<String, String> data = new HashMap<>();
		data.put("teamNumber", team);
		if (team.equals(opposingTeamOne)) data.put("rankCounterDefense", String.valueOf(opponentRobotOneDefensiveEffectivenessValue));
		if (team.equals(opposingTeamTwo)) data.put("rankCounterDefense", String.valueOf(opponentRobotTwoDefensiveEffectivenessValue));
		if (team.equals(opposingTeamThree)) data.put("rankCounterDefense", String.valueOf(opponentRobotThreeDefensiveEffectivenessValue));

		if (team.equals(opposingTeamOne)) data.put("rankResistance", String.valueOf(opponentRobotOneDefensiveEffectivenessValue_resistor));
		if (team.equals(opposingTeamTwo)) data.put("rankResistance", String.valueOf(opponentRobotTwoDefensiveEffectivenessValue_resistor));
		if (team.equals(opposingTeamThree)) data.put("rankResistance", String.valueOf(opponentRobotThreeDefensiveEffectivenessValue_resistor));

		if (team.equals(opposingTeamOne)) opponentRobotOneDataStructure.add(data);
		if (team.equals(opposingTeamTwo)) opponentRobotTwoDataStructure.add(data);
		if (team.equals(opposingTeamThree)) opponentRobotThreeDataStructure.add(data);

	}

	//the method that handles the intent to the next activity
	public void intentFinalData() {
		Intent intent = new Intent(getApplicationContext(), AfterMatchScouting.class);
		intent.putExtra("allianceDataStructure", allianceDataStructure);
		intent.putExtra("opponentDataStructure",opponentDataStructure);
		intent.putExtra("alliance", alliance);
		intent.putExtra("matchNumber", matchNumber);
		intent.putExtra("robotOneTimeline",timelineRobotOne);
		intent.putExtra("robotTwoTimeline",timelineRobotTwo);
		intent.putExtra("robotThreeTimeline",timelineRobotThree);
		intent.putExtra("opponentRobotOneData",opponentRobotOneDataStructure);
		intent.putExtra("opponentRobotTwoData",opponentRobotTwoDataStructure);
		intent.putExtra("opponentRobotThreeData",opponentRobotThreeDataStructure);
		intent.putExtra("defensiveEffectivenessValues", defensiveEffectivenessValues);
		intent.putExtra("pushAbilityDataStructure", pushAbilityDataStructure);
		startActivity(intent);
	}

	public void getExtras() {

		//makes sure the defense timer isn't active on the oncreate
		if (defenseTimer.isRunning()) defenseTimer.stop();

		matchNumber = getIntent().getExtras().getString("matchNumber");
		alliance = getIntent().getExtras().getString("alliance");
		teamOne = getIntent().getExtras().getString("allianceTeamOne");
		teamTwo = getIntent().getExtras().getString("allianceTeamTwo");
		teamThree = getIntent().getExtras().getString("allianceTeamThree");
		opposingTeamOne = getIntent().getExtras().getString("opponentTeamOne");
		opposingTeamTwo = getIntent().getExtras().getString("opponentTeamTwo");
		opposingTeamThree = getIntent().getExtras().getString("opponentTeamThree");

		createTeamsList();

	}

	public void createTeamsList() {
		teams[0] = Integer.valueOf(teamOne);
		teams[1] = Integer.valueOf(teamTwo);
		teams[2] = Integer.valueOf(teamThree);

		opposingTeams[0] = Integer.valueOf(opposingTeamOne);
		opposingTeams[1] = Integer.valueOf(opposingTeamTwo);
		opposingTeams[2] = Integer.valueOf(opposingTeamThree);
	}

	//initializes the xml elements with their according xml id's.
	public void initializeXML() {

		defenseTimer = new TimerUtil.Stopwatch();

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

		//resets the timer on the onCreate
		timerIsActive = false;
		if (new TimerUtil.Stopwatch().isRunning()) new TimerUtil.Stopwatch().stop();

		setDefensiveValues();
		createTimerListener();
		createAllianceTeamSelectionListener();
		createAllianceTeamColors();
		createRobotDefensiveEffectiveness();
		createSpinnerClickListener();
		setTeamNumbersToTeamNumberView();
		setTeamNumbersToTeamRankingView();
		setBackgroundOfDefenseRankingTeams();

	}

	//creates the listener for a click on the spinner (dropdown menu) in the ranking portion of the super scout
	public void createSpinnerClickListener() {
		ArrayAdapter<String> robotEffectivenessAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, alliance_effectiveness_list);
		robotEffectivenessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		ArrayAdapter<String> opponentSpinnerAdapter_resistor = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, opponent_effectiveness_list_resistor);
		opponentSpinnerAdapter_resistor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


		currentRobotDefenseEffectiveness.setAdapter(robotEffectivenessAdapter);

		robotOneDefensiveReactionEffectiveness.setAdapter(String.valueOf(robotOneDefensiveReaction.getText()).equals("RESISTOR") ?
				opponentSpinnerAdapter_resistor : robotEffectivenessAdapter);
		robotTwoDefensiveReactionEffectiveness.setAdapter(String.valueOf(robotTwoDefensiveReaction.getText()).equals("RESISTOR") ?
				opponentSpinnerAdapter_resistor : robotEffectivenessAdapter);
		robotThreeDefensiveReactionEffectiveness.setAdapter(String.valueOf(robotThreeDefensiveReaction.getText()).equals("RESISTOR") ?
				opponentSpinnerAdapter_resistor : robotEffectivenessAdapter);

		//sets the dropdown menus to the right list values (shows the lists after updating)
		setSpinnerSelectedItems();

		//only enable the robot defensiveness value dropdown menu when a team has been selected ("0" is the null value for the selected team)
		currentRobotDefenseEffectiveness.setEnabled(!selectedDefensiveRobot.equals("0"));
		robotOneDefensiveReactionEffectiveness.setEnabled(!selectedDefensiveRobot.equals("0"));
		robotTwoDefensiveReactionEffectiveness.setEnabled(!selectedDefensiveRobot.equals("0"));
		robotThreeDefensiveReactionEffectiveness.setEnabled(!selectedDefensiveRobot.equals("0"));

		//for the current selected robot, the defensive effectiveness value gets set to the selected item
		currentRobotDefenseEffectiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if (teamOne.equals(String.valueOf(currentSelectedTeam.getText())))
					allianceRobotOneDefensiveEffectivenessValue = i;
				if (teamTwo.equals(String.valueOf(currentSelectedTeam.getText())))
					allianceRobotTwoDefensiveEffectivenessValue = i;
				if (teamThree.equals(String.valueOf(currentSelectedTeam.getText())))
					allianceRobotThreeDefensiveEffectivenessValue = i;
				setSpinnerSelectedItems();
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) { }
		});

		//for the opposing alliance's robots, if it's in resistor mode (the toggle), the resistor value is set
		//to the selected item. Else (counter defender mode), the counter defender value is set to the selected item
		robotOneDefensiveReactionEffectiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if (!opOne_r) {
					opponentRobotOneDefensiveEffectivenessValue = i;
					defensiveEffectivenessValues[indexOfTeamString()][0] = opponentRobotOneDefensiveEffectivenessValue;
				} else {
					opponentRobotOneDefensiveEffectivenessValue_resistor = i;
					defensiveEffectivenessValues[indexOfTeamString()][3] = opponentRobotOneDefensiveEffectivenessValue_resistor;
				}
				setSpinnerSelectedItems();
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) { }
		});
		robotTwoDefensiveReactionEffectiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if (!opTwo_r) {
					opponentRobotTwoDefensiveEffectivenessValue = i;
					defensiveEffectivenessValues[indexOfTeamString()][1] = opponentRobotTwoDefensiveEffectivenessValue;
				} else {
					opponentRobotTwoDefensiveEffectivenessValue_resistor = i;
					defensiveEffectivenessValues[indexOfTeamString()][4] = opponentRobotTwoDefensiveEffectivenessValue_resistor;
				}
				setSpinnerSelectedItems();
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		robotThreeDefensiveReactionEffectiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				if (!opThree_r) {
					opponentRobotThreeDefensiveEffectivenessValue = i;
					defensiveEffectivenessValues[indexOfTeamString()][2] = opponentRobotThreeDefensiveEffectivenessValue;
				} else {
					opponentRobotThreeDefensiveEffectivenessValue_resistor = i;
					defensiveEffectivenessValues[indexOfTeamString()][5] = opponentRobotThreeDefensiveEffectivenessValue_resistor;
				}
				setSpinnerSelectedItems();
			}
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) { }
		});
	}

	//updates the selected item on the dropdown menu when the mode is switched from both C. Defender to Resistor or current team switches, and vice versa
	public void setSpinnerSelectedItems() {
		if (selectedDefensiveRobot.equals(teamOne)) currentRobotDefenseEffectiveness.setSelection(allianceRobotOneDefensiveEffectivenessValue);
		if (selectedDefensiveRobot.equals(teamTwo)) currentRobotDefenseEffectiveness.setSelection(allianceRobotTwoDefensiveEffectivenessValue);
		if (selectedDefensiveRobot.equals(teamThree)) currentRobotDefenseEffectiveness.setSelection(allianceRobotThreeDefensiveEffectivenessValue);
		robotOneDefensiveReactionEffectiveness.setSelection(opOne_r ?
				defensiveEffectivenessValues[indexOfTeamString()][3] : defensiveEffectivenessValues[indexOfTeamString()][0]);
		robotTwoDefensiveReactionEffectiveness.setSelection(opTwo_r ?
				defensiveEffectivenessValues[indexOfTeamString()][4] : defensiveEffectivenessValues[indexOfTeamString()][1]);
		robotThreeDefensiveReactionEffectiveness.setSelection(opThree_r ?
				defensiveEffectivenessValues[indexOfTeamString()][5] : defensiveEffectivenessValues[indexOfTeamString()][2]);

		setSpinnerBackgroundColor(robotOneDefensiveReactionEffectiveness, opOne_r, opOne_r ?
				defensiveEffectivenessValues[indexOfTeamString()][3] : defensiveEffectivenessValues[indexOfTeamString()][0]);
		setSpinnerBackgroundColor(robotTwoDefensiveReactionEffectiveness, opTwo_r, opTwo_r ?
				defensiveEffectivenessValues[indexOfTeamString()][4] : defensiveEffectivenessValues[indexOfTeamString()][1]);
		setSpinnerBackgroundColor(robotThreeDefensiveReactionEffectiveness, opThree_r, opThree_r ?
				defensiveEffectivenessValues[indexOfTeamString()][5] : defensiveEffectivenessValues[indexOfTeamString()][2]);
		if (selectedDefensiveRobot.equals(teamOne)) setSpinnerBackgroundColor(currentRobotDefenseEffectiveness, false, allianceRobotOneDefensiveEffectivenessValue);
		if (selectedDefensiveRobot.equals(teamTwo)) setSpinnerBackgroundColor(currentRobotDefenseEffectiveness, false, allianceRobotTwoDefensiveEffectivenessValue);
		if (selectedDefensiveRobot.equals(teamThree)) setSpinnerBackgroundColor(currentRobotDefenseEffectiveness, false, allianceRobotThreeDefensiveEffectivenessValue);

	}

	public void setSpinnerBackgroundColor(Spinner spinner, boolean isResistor, int effectiveness) {
		if (isResistor)
			switch (effectiveness) {
				case 0:
					spinnerColor(spinner, "white");
					break;
				case 1:
					spinnerColor(spinner, "light_green");
					break;
				case 2:
					spinnerColor(spinner, "light_yellow");
					break;
				case 3:
					spinnerColor(spinner, "light_red");
					break;
				default:
					spinnerColor(spinner, "white");
			}
		if (!isResistor)
			switch (effectiveness) {
				case 0:
					spinnerColor(spinner, "white");
					break;
				case 1:
					spinnerColor(spinner, "light_red");
					break;
				case 2:
					spinnerColor(spinner, "light_yellow");
					break;
				case 3:
					spinnerColor(spinner, "light_green");
					break;
				default:
					spinnerColor(spinner, "white");
			}
	}
	public void spinnerColor(Spinner spinner, String color) {
		switch (color) {
			case "light_green":
				spinner.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.MediumSpringGreen));
				break;
			case "light_yellow":
				spinner.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.JustinYellow));
				break;
			case "light_red":
				spinner.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.CarlRed));
				break;
			case "white":
				spinner.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.White));
				break;
		}
	}

	//creates the listener for handling the on click of the effectiveness tracker in the ranking portion of the super scout
	//either 'RESISTOR' or 'COUNTER DEFENDER'
	public void createRobotDefensiveEffectiveness() {
		robotOneDefensiveReaction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				robotOneDefensiveReaction.setText(opOne_r ?
						"C. DEFENDER" : "RESISTOR");
				opOne_r = !opOne_r;
				robotOneDefensiveReactionEffectiveness.setSelection(opOne_r ?
						defensiveEffectivenessValues[indexOfTeamString()][3] : defensiveEffectivenessValues[indexOfTeamString()][0]);
				createSpinnerClickListener();
			}
		});
		robotTwoDefensiveReaction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				robotTwoDefensiveReaction.setText(opTwo_r ?
						"C. DEFENDER" : "RESISTOR");
				opTwo_r = !opTwo_r;
				robotTwoDefensiveReactionEffectiveness.setSelection(opTwo_r ?
						defensiveEffectivenessValues[indexOfTeamString()][4] : defensiveEffectivenessValues[indexOfTeamString()][1]);
				createSpinnerClickListener();
			}
		});
		robotThreeDefensiveReaction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				robotThreeDefensiveReaction.setText(opThree_r ?
						"C. DEFENDER" : "RESISTOR");
				opThree_r = !opThree_r;
				robotThreeDefensiveReactionEffectiveness.setSelection(opThree_r ?
						defensiveEffectivenessValues[indexOfTeamString()][5] : defensiveEffectivenessValues[indexOfTeamString()][2]);
				createSpinnerClickListener();
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
				if (!selectedDefensiveRobot.equals(teamOne) && !timerIsActive) {
					selectedDefensiveRobot = teamOne;
				} else {
					Toast.makeText(DuringMatchScouting.this, "Please stop the defense timer before switching teams!", Toast.LENGTH_SHORT).show();
				}
				createAllianceTeamColors();
				createSpinnerClickListener();
			}
		});
		teamTwoSelectionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!selectedDefensiveRobot.equals(teamTwo) && !timerIsActive) {
					selectedDefensiveRobot = teamTwo;
				} else {
					Toast.makeText(DuringMatchScouting.this, "Please stop the defense timer before switching teams!", Toast.LENGTH_SHORT).show();
				}
				createAllianceTeamColors();
				createSpinnerClickListener();
			}
		});
		teamThreeSelectionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!selectedDefensiveRobot.equals(teamThree) && !timerIsActive) {
					selectedDefensiveRobot = teamThree;
				} else {
					Toast.makeText(DuringMatchScouting.this, "Please stop the defense timer before switching teams!", Toast.LENGTH_SHORT).show();
				}
				createAllianceTeamColors();
				createSpinnerClickListener();
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

		opponentRobotOneDefensiveEffectivenessValue_resistor = 0;
		opponentRobotTwoDefensiveEffectivenessValue_resistor = 0;
		opponentRobotThreeDefensiveEffectivenessValue_resistor = 0;
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
		timerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!timerIsActive && !selectedDefensiveRobot.equals("0")) {
					defenseTimer.start();
					createTimelineInput("startDefense");
					updateTimerColor();
				} else if (timerIsActive) {
					createTimelineInput("endDefense");
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
					updateTimerColor();
				} else {
					Toast.makeText(DuringMatchScouting.this, "Please select a team before starting the DEFENSE timer!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void updateTimerColor() {
		if (!defenseTimer.isRunning()) {
			timerStatusDisplay.setText("START");
			timerButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.CarlGreen));
			timerIsActive = false;
		} else {
			timerStatusDisplay.setText("STOP");
			timerButton.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.TeamNumberRed));
			timerIsActive = true;
		}
	}

	public void inflatePushAbilityDialog() {
		pushAbilityDialog = new PushAbilityDialog(this, teams, opposingTeams, alliance);
		pushAbilityDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				createPushAbilityInput(pushAbilityDialog);
			}
		});
		pushAbilityDialog.show();
	}

	//adds the timeline value for each selected alliance robot when the defensive timer is either pressed or stopped
	public void createTimelineInput(String type) {
		TimerUtil.MatchTimer match_timer = new TimerUtil.MatchTimer();
		Map<String, String> timeline = new HashMap<>();
		timeline.put("time", match_timer.getTime());
		timeline.put("type", type);
		if (selectedDefensiveRobot.equals(teamOne)) timelineRobotOne.add(timeline);
		if (selectedDefensiveRobot.equals(teamTwo)) timelineRobotTwo.add(timeline);
		if (selectedDefensiveRobot.equals(teamThree)) timelineRobotThree.add(timeline);
	}

	public void createPushAbilityInput(PushAbilityDialog pushAbilityDialog) {
		Map<String, String> temp_PushAbilityData = new HashMap<>();

		if (pushAbilityDialog.getPredator().equals("alliance"))
			temp_PushAbilityData.put("K", pushAbilityDialog.getAllianceSelectedTeam());
		else temp_PushAbilityData.put("K", pushAbilityDialog.getOpposingSelectedTeam());

		if (pushAbilityDialog.getPredator().equals("alliance"))
			temp_PushAbilityData.put("M", pushAbilityDialog.getOpposingSelectedTeam());
		else temp_PushAbilityData.put("M", pushAbilityDialog.getAllianceSelectedTeam());

		temp_PushAbilityData.put("N", (pushAbilityDialog.getEffectivity().equals("effective")) ? "true" : "false");

		pushAbilityDataStructure.add(temp_PushAbilityData);

	}
}
