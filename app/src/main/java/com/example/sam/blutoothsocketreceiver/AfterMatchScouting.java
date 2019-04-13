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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class AfterMatchScouting extends AppCompatActivity { //AfterMatchScouting is the counter page from the previous super scout
															//Only contains 'Speed' and 'Agility', and leads into the QR display
	String[][] allianceDataStructure = new String[3][3];    //IS ONLY DONE AFTER THE MATCH ENDS ('AFTER'MatchScouting)
	String[][] opponentDataStructure = new String[3][3];
	ArrayList<int[]> counterStats = new ArrayList<>();

	String alliance, matchNumber;

	SuperScoutingPanel panelOne;
	SuperScoutingPanel panelTwo;
	SuperScoutingPanel panelThree;

	public int[][] defensiveEffectivenessValues = new int[3][6];

	public ArrayList<Map<String, String>> timelineRobotOne = new ArrayList<>();
	public ArrayList<Map<String, String>> timelineRobotTwo = new ArrayList<>();
	public ArrayList<Map<String, String>> timelineRobotThree = new ArrayList<>();

	ArrayList<Map<String, String>> opponentRobotOneDataStructure = new ArrayList<>();
	ArrayList<Map<String, String>> opponentRobotTwoDataStructure = new ArrayList<>();
	ArrayList<Map<String, String>> opponentRobotThreeDataStructure = new ArrayList<>();

	ArrayList<Map<String, String>> pushAbilityDataStructure = new ArrayList<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.super_scouting);
		getExtras();
		initXml();
	}

	public void initXml() {
		getPanels();
		setPanels();
	}

	public void getPanels() {
		panelOne = (SuperScoutingPanel) getSupportFragmentManager().findFragmentById(R.id.panelOne);
		panelTwo = (SuperScoutingPanel) getSupportFragmentManager().findFragmentById(R.id.panelTwo);
		panelThree = (SuperScoutingPanel) getSupportFragmentManager().findFragmentById(R.id.panelThree);
	}

	//sets the panel color and team numbers according to the alliance and teams on the alliance
	public void setPanels() {

		panelOne.setAllianceColor(alliance.equals("Red Alliance") ? "red" : "blue");
		panelOne.setTeamNumber(allianceDataStructure[0][2]);
		panelTwo.setAllianceColor(alliance.equals("Red Alliance") ? "red" : "blue");
		panelTwo.setTeamNumber(allianceDataStructure[1][2]);
		panelThree.setAllianceColor(alliance.equals("Red Alliance") ? "red" : "blue");
		panelThree.setTeamNumber(allianceDataStructure[2][2]);
	}

	public void getExtras() {
		allianceDataStructure = (String[][]) getIntent().getSerializableExtra("allianceDataStructure");
		opponentDataStructure = (String[][]) getIntent().getSerializableExtra("opponentDataStructure");

		matchNumber = getIntent().getStringExtra("matchNumber");
		alliance = getIntent().getStringExtra("alliance");

		timelineRobotOne = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("robotOneTimeline");
		timelineRobotTwo = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("robotTwoTimeline");
		timelineRobotThree = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("robotThreeTimeline");

		opponentRobotOneDataStructure = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("opponentRobotOneData");
		opponentRobotTwoDataStructure = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("opponentRobotTwoData");
		opponentRobotThreeDataStructure = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("opponentRobotThreeData");

		pushAbilityDataStructure = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("pushAbilityDataStructure");

		defensiveEffectivenessValues = (int[][]) getIntent().getSerializableExtra("defensiveEffectivenessValues");

	}

	@Override
	public void onBackPressed() {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.submit, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		if (id == R.id.Submit) {
			if (canProceed()) listDataValues();
		}
		return super.onOptionsItemSelected(item);
	}

	//used to retrieve the data from the counters after the 'NEXT' button was pressed
	public void listDataValues() {
		ArrayList<String> teamOneDataName = new ArrayList<>(panelOne.getData().keySet());
		ArrayList<String> teamTwoDataName = new ArrayList<>(panelTwo.getData().keySet());
		ArrayList<String> teamThreeDataName = new ArrayList<>(panelThree.getData().keySet());
		int[] teamOneDataScore = new int[2];
		int[] teamTwoDataScore = new int[2];
		int[] teamThreeDataScore = new int[2];

		for (int i = 0; i < teamOneDataName.size(); i++) {
			teamOneDataScore[i] = (panelOne.getData().get(teamOneDataName.get(i)));
		}
		for (int i = 0; i < teamTwoDataName.size(); i++) {
			teamTwoDataScore[i] = (panelTwo.getData().get(teamTwoDataName.get(i)));
		}
		for (int i = 0; i < teamThreeDataName.size(); i++) {
			teamThreeDataScore[i] = (panelThree.getData().get(teamThreeDataName.get(i)));
		}

		counterStats.add(teamOneDataScore);
		counterStats.add(teamTwoDataScore);
		counterStats.add(teamThreeDataScore);

		Intent intent = new Intent(getApplicationContext(), QrDisplay.class);
		intent.putExtra("allianceDataStructure", allianceDataStructure);
		intent.putExtra("opponentDataStructure",opponentDataStructure);
		intent.putExtra("counterStats", counterStats);
		intent.putExtra("alliance", alliance);
		intent.putExtra("matchNumber", matchNumber);
		intent.putExtra("timelineOne",timelineRobotOne);
		intent.putExtra("timelineTwo",timelineRobotTwo);
		intent.putExtra("timelineThree", timelineRobotThree);
		intent.putExtra("opponentRobotOneData", opponentRobotOneDataStructure);
		intent.putExtra("opponentRobotTwoData", opponentRobotTwoDataStructure);
		intent.putExtra("opponentRobotThreeData", opponentRobotThreeDataStructure);
		intent.putExtra("defensiveEffectivenessValues", defensiveEffectivenessValues);
		intent.putExtra("pushAbilityDataStructure", pushAbilityDataStructure);
		startActivity(intent);
	}

	//checks whether all of the counters are different values from eachother per row
	public Boolean canProceed() {
		Boolean canProceed = true;
		ArrayList<String> dataNames = new ArrayList<>(Arrays.asList("Speed", "Agility"));
		for (int i = 0; i < 2; i++) {
			String dataName = dataNames.get(i);
			int valOne = panelOne.getData().get(dataName);
			int valTwo = panelTwo.getData().get(dataName);
			int valThree = panelThree.getData().get(dataName);

			if (dataName.equals("Speed")) {
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

}
