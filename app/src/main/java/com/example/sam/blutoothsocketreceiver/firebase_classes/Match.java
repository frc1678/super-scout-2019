package com.example.sam.blutoothsocketreceiver.firebase_classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import com.shaded.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shaded.fasterxml.jackson.annotation.JsonTypeInfo;
import com.shaded.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.shaded.fasterxml.jackson.databind.KeyDeserializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by citruscircuits on 1/17/16
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@IgnoreExtraProperties
public class Match extends Object {
	//TODO : Keep up with schema changes

	public Map<String, String> cargoShipPreloads;
			/*	public String leftNear;
			public String leftMid;
			public String leftFar;
			public String rightNear;
			public String rightMid;
			public String rightFar;*/

	public List<String> noShowTeams;
	public Integer foulPointsGainedRed;
	public Integer foulPointsGainedBlue;
	public Integer redScore;
	public Integer blueScore;
	public Boolean blueDidRocketRP;
	public Boolean redDidRocketRP;
	public Boolean blueDidClimbRP;
	public Boolean redDidClimbRP;


	@PropertyName("number")
	public Integer number;
	@PropertyName("redAllianceTeamNumbers")
	public List<Integer> redAllianceTeamNumbers;
	@PropertyName("blueAllianceTeamNumbers")
	public List<Integer> blueAllianceTeamNumbers;
}