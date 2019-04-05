package com.example.sam.blutoothsocketreceiver.Utils;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public class TimerUtil {

	public static CountDownTimer matchTimer = null;

	public static TextView mTimerView;
	public static TextView mActivityView;

	public static float timestamp;

	public static String displayTime;

	public static class DefenseTimer extends Thread {

		@Override
		public void run() {
			matchTimer = new CountDownTimer(150000, 10) {
				public void onTick(long millisUntilFinished) {
					float tempTime = millisUntilFinished / 1000f;
					timestamp = Float.parseFloat(String.format("%.1f", tempTime));
					displayTime = String.valueOf(Math.round(tempTime));
					mTimerView.setText(displayTime);
				}

				public void onFinish() {

				}
			}.start();
		}

		public void initTimer() {
			run();
		}

	}

	public static class Stopwatch {
		private long startTime = 0;
		private boolean running = false;
		Handler handler = new Handler();

		public void start() {
			this.startTime = System.currentTimeMillis();
			this.running = true;
			handler.postDelayed(runnable, 0);
		}

		public void stop() {
			this.running = false;
		}

		public long getElapsedTimeSecs() {
			long elapsed = 0;
			if (running) {
				elapsed = ((System.currentTimeMillis() - startTime) / 1000) % 60;
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