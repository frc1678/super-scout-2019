package com.example.sam.blutoothsocketreceiver;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.View;
import android.widget.Button;

import org.jcodec.common.DictionaryCompressor;
import org.jcodec.common.RunLength;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by niraq on 1/15/2018.
 */

//This class is for getting and setting the color of plates in FieldSetup.
public class PlateConfig {

    private Context context;
    private boolean isRed;

    private Button leftTopPlateButton;
    private Button leftBottomPlateButton;
    private Button scaleTopPlateButton;
    private Button scaleBottomPlateButton;
    private Button rightTopPlateButton;
    private Button rightBottomPlateButton;

    private Map<Integer, String> configMap;

    private String red;
    private String blue;
    private String grey;

    public PlateConfig(Context context, boolean isRed) {
        this.context = context;
        this.isRed = isRed;

        red = "#FF0000";
        blue = "#0000FF";
        grey = "#CCCCCC";

        configMap = new HashMap<>();

        leftTopPlateButton = (Button) ((Activity)context).findViewById(R.id.leftTopPlateButton);
        configMap.put(R.id.leftTopPlateButton, "noColor");
        leftBottomPlateButton = (Button) ((Activity)context).findViewById(R.id.leftBottomPlateButton);
        configMap.put(R.id.leftBottomPlateButton, "noColor");
        scaleTopPlateButton = (Button) ((Activity)context).findViewById(R.id.scaleTopPlateButton);
        configMap.put(R.id.scaleTopPlateButton, "noColor");
        scaleBottomPlateButton = (Button) ((Activity)context).findViewById(R.id.scaleBottomPlateButton);
        configMap.put(R.id.scaleBottomPlateButton, "noColor");
        rightTopPlateButton = (Button) ((Activity)context).findViewById(R.id.rightTopPlateButton);
        configMap.put(R.id.rightTopPlateButton, "noColor");
        rightBottomPlateButton = (Button) ((Activity)context).findViewById(R.id.rightBottomPlateButton);
        configMap.put(R.id.rightBottomPlateButton, "noColor");

        leftTopPlateButton.setBackgroundColor(Color.parseColor(grey));
        leftBottomPlateButton.setBackgroundColor(Color.parseColor(grey));
        scaleTopPlateButton.setBackgroundColor(Color.parseColor(grey));
        scaleBottomPlateButton.setBackgroundColor(Color.parseColor(grey));
        rightTopPlateButton.setBackgroundColor(Color.parseColor(grey));
        rightBottomPlateButton.setBackgroundColor(Color.parseColor(grey));
    }

    public Map<Integer, String> getConfig() {
        return configMap;
    }

    public void swapColor(View button) {
        Integer buttonId = button.getId();
        String oppositeButtonState;
        String oppositeButtonColor;

        if(isRed) {
            if(configMap.get(buttonId).equals("red")) {
                button.setBackgroundColor(Color.parseColor(blue));
                //button.setBackgroundDrawable(blueDrawable);
                configMap.put(buttonId, "blue");
                oppositeButtonState = "red";
                oppositeButtonColor = red;
            } else {
                button.setBackgroundColor(Color.parseColor(red));
                //button.setBackgroundDrawable(blueDrawable);
                configMap.put(buttonId, "red");
                oppositeButtonState = "blue";
                oppositeButtonColor = blue;
            }
        } else {
            if(configMap.get(buttonId).equals("blue")) {
                button.setBackgroundColor(Color.parseColor(red));
                //button.setBackgroundDrawable(redDrawable);
                configMap.put(buttonId, "red");
                oppositeButtonState = "blue";
                oppositeButtonColor = blue;
            } else {
                button.setBackgroundColor(Color.parseColor(blue));
                //button.setBackgroundDrawable(redDrawable);
                configMap.put(buttonId, "blue");
                oppositeButtonState = "red";
                oppositeButtonColor = red;
            }
        }

        switch(buttonId)
        {
            case R.id.leftTopPlateButton:
                leftBottomPlateButton.setBackgroundColor(Color.parseColor(oppositeButtonColor));
                configMap.put(R.id.leftBottomPlateButton, oppositeButtonState);
                break;

            case R.id.leftBottomPlateButton:
                leftTopPlateButton.setBackgroundColor(Color.parseColor(oppositeButtonColor));
                configMap.put(R.id.leftTopPlateButton, oppositeButtonState);
                break;

            case R.id.scaleTopPlateButton:
                scaleBottomPlateButton.setBackgroundColor(Color.parseColor(oppositeButtonColor));
                configMap.put(R.id.scaleBottomPlateButton, oppositeButtonState);
                break;

            case R.id.scaleBottomPlateButton:
                scaleTopPlateButton.setBackgroundColor(Color.parseColor(oppositeButtonColor));
                configMap.put(R.id.scaleTopPlateButton, oppositeButtonState);
                break;

            case R.id.rightTopPlateButton:
                rightBottomPlateButton.setBackgroundColor(Color.parseColor(oppositeButtonColor));
                configMap.put(R.id.rightBottomPlateButton, oppositeButtonState);
                break;

            case R.id.rightBottomPlateButton:
                rightTopPlateButton.setBackgroundColor(Color.parseColor(oppositeButtonColor));
                configMap.put(R.id.rightTopPlateButton, oppositeButtonState);
                break;
        }

    }
}
