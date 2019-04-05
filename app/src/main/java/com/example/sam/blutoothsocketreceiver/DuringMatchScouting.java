package com.example.sam.blutoothsocketreceiver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class DuringMatchScouting extends AppCompatActivity { //Comments will be included to explain meanings of variable names
															 //to prevent unnecessary confusion.

	//Uninitialized variables will be separated into the groups they are in within the code (ex, columns together or sections together)

	Button teamOneSelectionButton, teamTwoSelectionButton, teamThreeSelectionButton; //The three buttons to select which team is on defense

	FrameLayout timerButton; //timer button is a frame layout because it has two text views inside of it (which a button can't have) to display
							 //the status (START, STOP) and the time itself. Will have an onClick to determine when timer is activated.

	TextView timerStatusDisplay, timerTimeDisplay; //As stated above, the status is the 'START, STOP', and the time itself is the '135 --> 134'

	TextView currentSelectedTeam, firstOpposingTeam, secondOpposingTeam, thirdOpposingTeam; //Current selected team refers to the team from
														// the SCOUT's alliance. The opposing teams are the three robots that defense is
														// being played against.

	Button robotOneDefensiveReaction, robotTwoDefensiveReaction, robotThreeDefensiveReaction; //How a robot reacts to the defense being played
														// Button as it's a toggle (boolean type). Can either be 'RESISTOR' or 'COUNTER DEFENSE'
														// 'COUNTER DEFENSE' means they're responding by playing counter defense
														// 'RESISTOR' means they are not playing counter defense. AKA: if counter def, counter def.
																										// else, resistor.

	Spinner currentRobotDefenseEffectiveness, robotOneDefensiveReactionEffectiveness, robotTwoDefensiveReactionEffectiveness, robotThreeDefensiveReactionEffectiveness;
							//For the SCOUT's alliance's robot, the dropdown menu (spinner) will have a selection on how effective their defense was
							//For the OPPONENT's alliance's robots, the dropdown menu (spinner) will have a selection on how effective their reaction was
																		// REACTION as in 'COUNTER DEFENSE' or 'RESISTOR' scaling.


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

	}

	//initializes the xml elements with their according xml id's.
	public void initializeXML() {
		teamOneSelectionButton = (Button) findViewById(R.id.teamOneSelectionButton);
		teamTwoSelectionButton = (Button) findViewById(R.id.teamTwoSelectionButton);
		teamThreeSelectionButton = (Button) findViewById(R.id.teamThreeSelectionButton);

		timerButton = (FrameLayout) findViewById(R.id.timerFrameLayout);

		timerStatusDisplay = (TextView) findViewById(R.id.timerStatusDisplay);
		timerTimeDisplay = (TextView) findViewById(R.id.timerTimeDisplay);

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
}
