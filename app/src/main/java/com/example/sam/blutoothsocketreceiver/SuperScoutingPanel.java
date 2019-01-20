package com.example.sam.blutoothsocketreceiver;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sam on 5/12/16.
 */
public class SuperScoutingPanel extends Fragment {
    public static ArrayList<Integer> Speed;
    public static ArrayList<Integer> Agility;
    public static ArrayList<Integer> Defense;
    public static ArrayList<Integer> Stacking;



    Boolean isRed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.super_scouting_panel, container, false);
    }

    public void setAllianceColor(boolean allianceColor) {
        TextView teamNumberTextView = (TextView) getView().findViewById(R.id.teamNumberTextView);
        this.isRed = allianceColor;
        if (isRed) {
            teamNumberTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.TeamNumberRed));
        } else {
            teamNumberTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.TeamNumberBlue));
        }
    }

    public void setTeamNumber(String teamNumber) {
        TextView teamNumberTextView = (TextView) getView().findViewById(R.id.teamNumberTextView);
        teamNumberTextView.setText(teamNumber);
    }

    public int getDataNameCount() {
        int numOfDataName = ((LinearLayout) getView()).getChildCount();
        return numOfDataName;
    }


    public Map<String, Integer> getData() {
        Map<String, Integer> mapOfData = new HashMap<>();   //Make this a LinkedHashMap if you want to make everything 0's when speed is 0
        LinearLayout rootLayout = (LinearLayout) getView();
        Counter counter;
        for (int i = 0; i < ((LinearLayout) getView()).getChildCount() - 1; i++) {
            counter = (Counter) rootLayout.getChildAt(i + 1);
            String dataName = counter.getDataName();
            Integer dataScore = counter.getDataValue();
            mapOfData.put(dataName, dataScore);
        }

        return mapOfData;
    }


}

