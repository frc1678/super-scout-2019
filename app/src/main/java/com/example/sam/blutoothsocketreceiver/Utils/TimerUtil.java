package com.example.sam.blutoothsocketreceiver.Utils;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

public class TimerUtil {

	public static CountDownTimer matchTimer = null;

	public static TextView mTimerView;
	public static TextView mActivityView;

	public static float timestamp;

	public static String displayTime;

	public static class MatchTimer extends Thread {

		@Override
		public void run() {
			matchTimer = new CountDownTimer(150000, 10) {
				public void onTick(long millisUntilFinished) {
					float tempTime = millisUntilFinished / 1000f;
					timestamp = Float.parseFloat(String.format("%.1f", tempTime));
					displayTime = String.valueOf(Math.round(tempTime));
					}

				public void onFinish() {
					Stopwatch sw = new Stopwatch();
					sw.stop();
				}
			}.start();
		}

		public String getTime() {
			return String.valueOf(timestamp);
		}

		public void initTimer() {
			run();
		}

	}

	//Stopwatch is used for the defensive timer on the main scouting page
	public static class Stopwatch {
		private long startTime = 0;
		private boolean running = false;
		Handler handler = new Handler();

		public Boolean isRunning() {
			return this.running;
		}

		//start is used to begin the stopwatch (running = true)
		public void start() {
			this.startTime = System.currentTimeMillis();
			this.running = true;
			handler.postDelayed(runnable, 0);
		}

		//stop is used to stop the stopwatch (running = false)
		public void stop() {
			this.running = false;
		}

		public long getElapsedTimeSecs() {
			long elapsed = 0;
			if (running) {
				elapsed = ((System.currentTimeMillis() - startTime) / 1000);
			}
			return elapsed;
		}

		public Runnable runnable = new Runnable() {

			public void run() {
				displayTime = String.valueOf(Math.round(getElapsedTimeSecs()));
				mTimerView.setText(displayTime);
				handler.postDelayed(this, 0);
			}

		};

	}
}