package com.example.sam.blutoothsocketreceiver;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by sam on 5/11/16.
 */
public class Counter extends RelativeLayout {
    //Declares/"Imports" data points to be used in this class
    private String dataName;
    private int max;
    private int min;
    private int increment;
    private int value;
    private int startingValue;
    TextView counterTextView;
    TextView counterTitleTextView;
    Button addButton;
    Button subtractButton;
    SuperScoutingPanel superScoutingPanel;

    public Counter(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Creates a new SuperScoutingPanel and then inflates the Counter layout (xml)
        superScoutingPanel = new SuperScoutingPanel();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.counter, this, true);

        counterTextView = (TextView)findViewById(R.id.scoreCounter);
        counterTitleTextView = (TextView)findViewById(R.id.dataName);
        addButton = (Button)findViewById(R.id.plusButton);
        subtractButton = (Button)findViewById(R.id.minusButton);

        //Creates typed array called "a"
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Counter,
                0, 0);

        try {
            //gets datapoints for each counter (dataName, max, min, startingValue, increment) and puts them into typed array "a". Then, it calls the listenForAddClicked and listenForMinusClicked functions (below). Finally,
            //the refresh counter is called and then typed array "a" is recycled
            this.dataName = a.getString(R.styleable.Counter_dataName);
            this.max = a.getInt(R.styleable.Counter_max, 4);
            this.min = a.getInt(R.styleable.Counter_min, 0);
            this.startingValue = a.getInt(R.styleable.Counter_startingValue, 2);
            this.increment = a.getInt(R.styleable.Counter_increment, 1);

            this.value = this.startingValue;

            listenForAddClicked();
            listenForMinusClicked();

            refreshCounter(startingValue);

        } finally {
            a.recycle();
        }
    }

    //Sets the onClickListener for the "addButton". When clicked, if the value + the increment is less than or equal to the max (all gotten in the Counter class above), then value will be equal to value + increment
    private void listenForAddClicked(){
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value + increment <= max) {
                    value += increment;
                    refreshCounter(value);
                }
            }
        });
    }

    //Sets the onClickListener fot the "subtractButton". When clicked, if the value - the increment is greater than or equal to the miniumum (all datapoints gotten in the Counter class above), then value will be equal to value - increment
    private void listenForMinusClicked(){
        subtractButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value - increment >= min) {
                    value -= increment;
                    refreshCounter(value);
                }
            }
        });
    }

    //When called, it will set the counterTitleTextView to the dataName (gotten in the Counter class above), and will set the text of the counterTextView to the someValue integer that was passed in.
    public void refreshCounter(int someValue) { //TODO: Make this private and make a new setCounter method (low priority) (or just refactor).
        value = someValue;
        counterTitleTextView.setText(dataName);
        counterTextView.setText(someValue + "");
    }

    public String getDataName() {
        return dataName;
    }

    public Integer getDataValue() {
        return value;
    }
}
