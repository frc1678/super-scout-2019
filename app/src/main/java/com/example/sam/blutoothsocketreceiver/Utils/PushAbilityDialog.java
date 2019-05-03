package com.example.sam.blutoothsocketreceiver.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sam.blutoothsocketreceiver.R;

public class PushAbilityDialog extends Dialog implements android.view.View.OnClickListener {

	private TextView allianceWinnerText, opposingWinnerText;

	private Activity activity;
	private Button allianceTeamOne_b, allianceTeamTwo_b, allianceTeamThree_b;
	private Button opposingTeamOne_b, opposingTeamTwo_b, opposingTeamThree_b;

	private RelativeLayout togglePredator;
	private ImageView upArrowView;
	private ImageView downArrowView;

	private Button ineffectiveButton, effectiveButton, confirmButton, cancelButton;

	private int[] alliance;
	private int[] opposingAlliance;
	private String allianceColor;
	private String selectedRobot;

	public boolean dismissed = false;

	//

	private String selectedAllianceTeam = "0";
	private String selectedOpposingTeam = "0";
	private String effectivity = "0";

	public PushAbilityDialog(Activity a, int[] alliance, int[] opposingAlliance, String allianceColor, String selectedRobot) {
		super(a);
		this.activity = a;
		this.alliance = alliance;
		this.opposingAlliance = opposingAlliance;
		this.allianceColor = allianceColor;
		this.selectedRobot = selectedRobot;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.push_ability_dialog);

		setCancelable(false);
		initXml();
	}

	public void initXml() {

		dismissed = false;
		if (!selectedRobot.equals("0")) selectedAllianceTeam = selectedRobot;

		cancelButton = (Button) findViewById(R.id.dismiss_button);

		allianceWinnerText = (TextView) findViewById(R.id.allianceWinnerText);
		opposingWinnerText = (TextView) findViewById(R.id.opposingWinnerText);

		ineffectiveButton = (Button) findViewById(R.id.ineffective_button);
		effectiveButton = (Button) findViewById(R.id.effective_button);
		confirmButton = (Button) findViewById(R.id.confirm_button);

		upArrowView = (ImageView) findViewById(R.id.upArrowView);
		downArrowView = (ImageView) findViewById(R.id.downArrowView);
		togglePredator = (RelativeLayout) findViewById(R.id.togglePredator);

		allianceTeamOne_b = (Button) findViewById(R.id.allianceTeamOneButton);
		allianceTeamTwo_b = (Button) findViewById(R.id.allianceTeamTwoButton);
		allianceTeamThree_b = (Button) findViewById(R.id.allianceTeamThreeButton);

		opposingTeamOne_b = (Button) findViewById(R.id.opposingTeamOneButton);
		opposingTeamTwo_b = (Button) findViewById(R.id.opposingTeamTwoButton);
		opposingTeamThree_b = (Button) findViewById(R.id.opposingTeamThreeButton);


		allianceTeamOne_b.setOnClickListener(this);
		allianceTeamTwo_b.setOnClickListener(this);
		allianceTeamThree_b.setOnClickListener(this);

		opposingTeamOne_b.setOnClickListener(this);
		opposingTeamTwo_b.setOnClickListener(this);
		opposingTeamThree_b.setOnClickListener(this);

		togglePredator.setOnClickListener(this);

		effectiveButton.setOnClickListener(this);
		ineffectiveButton.setOnClickListener(this);
		confirmButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		ineffectiveButton.setText("A LITTLE");
		effectiveButton.setText("A LOT");

		setupButtons();
	}

	public void setupButtons() {
		setColor(allianceColor);

		allianceTeamOne_b.setText(String.valueOf(alliance[0]));
		allianceTeamTwo_b.setText(String.valueOf(alliance[1]));
		allianceTeamThree_b.setText(String.valueOf(alliance[2]));

		opposingTeamOne_b.setText(String.valueOf(opposingAlliance[0]));
		opposingTeamTwo_b.setText(String.valueOf(opposingAlliance[1]));
		opposingTeamThree_b.setText(String.valueOf(opposingAlliance[2]));
	}

	public void setButtonColor(Button button, String color) {
		switch (color) {
			case "red":
				button.setBackgroundColor(activity.getResources().getColor(R.color.CarlRed));
				break;
			case "blue":
				button.setBackgroundColor(activity.getResources().getColor(R.color.LightBlue));
				break;
			case "selectedBlue":
				button.setBackgroundColor(activity.getResources().getColor(R.color.TeamNumberBlue));
				break;
			case "selectedRed":
				button.setBackgroundColor(activity.getResources().getColor(R.color.Red));
				break;
			case "green":
				button.setBackgroundColor(activity.getResources().getColor(R.color.LightGreen));
				break;
			case "neutral":
				button.setBackgroundColor(activity.getResources().getColor(R.color.LightGrey));
				break;
			case "selectedGreen":
				button.setBackgroundColor(activity.getResources().getColor(R.color.JustinOrange));
				break;
		}
	}

	public void setTextColorWhite(Button button) {
		button.setTextColor(activity.getResources().getColor(R.color.White));
		button.setTypeface(null, Typeface.BOLD);
	}
	
	public void resetTextColors() {
		allianceTeamOne_b.setTextColor(activity.getResources().getColor(R.color.Black));
		allianceTeamTwo_b.setTextColor(activity.getResources().getColor(R.color.Black));
		allianceTeamThree_b.setTextColor(activity.getResources().getColor(R.color.Black));
		opposingTeamOne_b.setTextColor(activity.getResources().getColor(R.color.Black));
		opposingTeamTwo_b.setTextColor(activity.getResources().getColor(R.color.Black));
		opposingTeamThree_b.setTextColor(activity.getResources().getColor(R.color.Black));

		allianceTeamOne_b.setTypeface(null,Typeface.NORMAL);
		allianceTeamTwo_b.setTypeface(null,Typeface.NORMAL);
		allianceTeamThree_b.setTypeface(null,Typeface.NORMAL);
		opposingTeamOne_b.setTypeface(null,Typeface.NORMAL);
		opposingTeamTwo_b.setTypeface(null,Typeface.NORMAL);
		opposingTeamThree_b.setTypeface(null,Typeface.NORMAL);
		
	}

	public void setColor(String allianceColor) {
		
		resetTextColors();
		
		setButtonColor(opposingTeamOne_b, "red");
		setButtonColor(opposingTeamTwo_b, "red");
		setButtonColor(opposingTeamThree_b, "red");

		setButtonColor(allianceTeamOne_b, "blue");
		setButtonColor(allianceTeamTwo_b, "blue");
		setButtonColor(allianceTeamThree_b, "blue");

		allianceWinnerText.setTextColor(activity.getResources().getColor(R.color.TeamNumberBlue));
		opposingWinnerText.setTextColor(activity.getResources().getColor(R.color.Red));

		//

		if (String.valueOf(alliance[0]).equals(selectedAllianceTeam)) setButtonColor(allianceTeamOne_b, "selectedBlue");
		if (String.valueOf(alliance[1]).equals(selectedAllianceTeam)) setButtonColor(allianceTeamTwo_b, "selectedBlue");
		if (String.valueOf(alliance[2]).equals(selectedAllianceTeam)) setButtonColor(allianceTeamThree_b, "selectedBlue");

		if (String.valueOf(opposingAlliance[0]).equals(selectedOpposingTeam)) setButtonColor(opposingTeamOne_b, "selectedRed");
		if (String.valueOf(opposingAlliance[1]).equals(selectedOpposingTeam)) setButtonColor(opposingTeamTwo_b, "selectedRed");
		if (String.valueOf(opposingAlliance[2]).equals(selectedOpposingTeam)) setButtonColor(opposingTeamThree_b, "selectedRed");

		if (String.valueOf(alliance[0]).equals(selectedAllianceTeam)) setTextColorWhite(allianceTeamOne_b);
		if (String.valueOf(alliance[1]).equals(selectedAllianceTeam)) setTextColorWhite(allianceTeamTwo_b);
		if (String.valueOf(alliance[2]).equals(selectedAllianceTeam)) setTextColorWhite(allianceTeamThree_b);

		if (String.valueOf(opposingAlliance[0]).equals(selectedOpposingTeam)) setTextColorWhite(opposingTeamOne_b);
		if (String.valueOf(opposingAlliance[1]).equals(selectedOpposingTeam)) setTextColorWhite(opposingTeamTwo_b);
		if (String.valueOf(opposingAlliance[2]).equals(selectedOpposingTeam)) setTextColorWhite(opposingTeamThree_b);

		if (allianceColor.equals("Red Alliance")) {
			setButtonColor(opposingTeamOne_b, "blue");
			setButtonColor(opposingTeamTwo_b, "blue");
			setButtonColor(opposingTeamThree_b, "blue");

			setButtonColor(allianceTeamOne_b, "red");
			setButtonColor(allianceTeamTwo_b, "red");
			setButtonColor(allianceTeamThree_b, "red");

			allianceWinnerText.setTextColor(activity.getResources().getColor(R.color.Red));
			opposingWinnerText.setTextColor(activity.getResources().getColor(R.color.TeamNumberBlue));

			//

			if (String.valueOf(alliance[0]).equals(selectedAllianceTeam)) setButtonColor(allianceTeamOne_b, "selectedRed");
			if (String.valueOf(alliance[1]).equals(selectedAllianceTeam)) setButtonColor(allianceTeamTwo_b, "selectedRed");
			if (String.valueOf(alliance[2]).equals(selectedAllianceTeam)) setButtonColor(allianceTeamThree_b, "selectedRed");

			if (String.valueOf(opposingAlliance[0]).equals(selectedOpposingTeam)) setButtonColor(opposingTeamOne_b, "selectedBlue");
			if (String.valueOf(opposingAlliance[1]).equals(selectedOpposingTeam)) setButtonColor(opposingTeamTwo_b, "selectedBlue");
			if (String.valueOf(opposingAlliance[2]).equals(selectedOpposingTeam)) setButtonColor(opposingTeamThree_b, "selectedBlue");

		}

		if (canDismiss()) setButtonColor(confirmButton, "selectedGreen");
		else setButtonColor(confirmButton, "neutral");

	}

	public void updateEffectiveToggleColor() {
		if (effectivity.equals("effective")) {
			setButtonColor(effectiveButton, "green");
		} else {
			setButtonColor(effectiveButton, "neutral");
		}
		if (effectivity.equals("ineffective")) {
			setButtonColor(ineffectiveButton, "green");
		} else {
			setButtonColor(ineffectiveButton, "neutral");
		}

		if (canDismiss()) setButtonColor(confirmButton, "selectedGreen");
		else setButtonColor(confirmButton, "neutral");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.allianceTeamOneButton:
				selectedAllianceTeam = String.valueOf(alliance[0]);
				setColor(allianceColor);
				break;
			case R.id.allianceTeamTwoButton:
				selectedAllianceTeam = String.valueOf(alliance[1]);
				setColor(allianceColor);
				break;
			case R.id.allianceTeamThreeButton:
				selectedAllianceTeam = String.valueOf(alliance[2]);
				setColor(allianceColor);
				break;
			case R.id.opposingTeamOneButton:
				selectedOpposingTeam = String.valueOf(opposingAlliance[0]);
				setColor(allianceColor);
				break;
			case R.id.opposingTeamTwoButton:
				selectedOpposingTeam = String.valueOf(opposingAlliance[1]);
				setColor(allianceColor);
				break;
			case R.id.opposingTeamThreeButton:
				selectedOpposingTeam = String.valueOf(opposingAlliance[2]);
				setColor(allianceColor);
				break;

			case R.id.togglePredator:
				activatePredator();
				break;
			case R.id.effective_button:
				effectivity = "effective";
				updateEffectiveToggleColor();
				break;
			case R.id.ineffective_button:
				effectivity = "ineffective";
				updateEffectiveToggleColor();
				break;
			case R.id.confirm_button:
				if (canDismiss()) {
					dismiss();
					dismissed = true;
				}
				else makeToast("Please make sure you've inputted everything!");
				break;
			case R.id.dismiss_button:
				dismiss();
			default:
				break;
		}
	}

	public void makeToast(String message) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}

	public Boolean canDismiss() {
		if (selectedOpposingTeam.equals("0") || selectedAllianceTeam.equals("0") || effectivity.equals("0")) return false;
		return true;
	}

	public void activatePredator() {
		if (upArrowView.getVisibility() == View.INVISIBLE) {
			upArrowView.setVisibility(View.VISIBLE);
			downArrowView.setVisibility(View.INVISIBLE);

			allianceWinnerText.setVisibility(View.INVISIBLE);
			opposingWinnerText.setVisibility(View.VISIBLE);
		} else
		if (downArrowView.getVisibility() == View.INVISIBLE) {
			downArrowView.setVisibility(View.VISIBLE);
			upArrowView.setVisibility(View.INVISIBLE);

			allianceWinnerText.setVisibility(View.VISIBLE);
			opposingWinnerText.setVisibility(View.INVISIBLE);
		}
	}
	public Boolean getDismissed() { return dismissed; }
	public String getAllianceSelectedTeam() {
		return selectedAllianceTeam;
	}
	public String getOpposingSelectedTeam() {
		return selectedOpposingTeam;
	}
	public String getEffectivity() {
		return effectivity;
	}
	public String getPredator() {
		if (upArrowView.getVisibility() == View.INVISIBLE) return "alliance";
		return "opposingAlliance";
	}
}
