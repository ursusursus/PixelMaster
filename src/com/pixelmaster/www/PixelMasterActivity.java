package com.pixelmaster.www;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PixelMasterActivity extends Activity implements OnClickListener {
	
	private Timer timer;
	
	private boolean isRunning;
	//private boolean isFinished = false;
	
	private Button[] buttons;
	private Round[] rounds;
	
	private String currentCorrectAnswer;
	private String[] currentAnswers;
	private int currentRoundIndex = 0;

	private ProgressBar progressBar;
	private int progress;

	private Bitmap[] currentImages;
	private int currentImageIndex = 0;

	private ImageView imageView;
	
	private static final int ROUND_LENGTH = 30;
	private static final int IMAGES_COUNT = 3;
	private static final int FULL_TIMER = 100 + (100 / ROUND_LENGTH);
	
	private static final int TIME_IS_UP = 0;
	private static final int CORRECT_ANSWER = 1;
	private static final int GAME_WON = 2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//DrawableDrawable.createFromStream(is, srcName)
		init();
		gameLoop();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		timer.cancel();
	}
	
	private void gameLoop() {
				
		initRound(0);
		timer = new Timer();
		timer.schedule(new GameLoop(), 0, 1000);
	}
	
	
	
	private void init() {
		
		imageView = (ImageView) findViewById(R.id.imageView);
		
		currentImages = new Bitmap[IMAGES_COUNT];
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setProgress(100);
		
		this.buttons = new Button[4];
		
		buttons[0] = (Button) findViewById(R.id.answerButtonA);
		buttons[1] = (Button) findViewById(R.id.answerButtonB);
		buttons[2] = (Button) findViewById(R.id.answerButtonC);
		buttons[3] = (Button) findViewById(R.id.answerButtonD);
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setOnClickListener(this);
		}
		
		this.rounds = new Round[3];
		rounds[0] = new Round(new String[] {"Cat", "Dog", "Frog", "Duck"}, 1, new int[]{R.drawable.a0, R.drawable.a1,R.drawable.a2});
		rounds[1] = new Round(new String[] {"Cow", "Sheep", "Chicken", "Lion"}, 2, new int[]{R.drawable.c0, R.drawable.c1,R.drawable.c2});
		rounds[2] = new Round(new String[] {"Brick", "Stone", "Wood", "Paper"}, 0, new int[]{R.drawable.b0, R.drawable.b1,R.drawable.b2});
		
	}
	
	private void initNextRound() {
		if(currentRoundIndex == rounds.length - 1) {
			showDialog(GAME_WON);
		} else {
			initRound(++currentRoundIndex);
		}
	}
	
	private void initRound(int roundIndex) {
		
		isRunning = true;
		progress = FULL_TIMER;
		
		Round currentRound = rounds[roundIndex];
		
		int[] currentIDs = currentRound.getResourceIDs();
		
		for (int i = 0; i < IMAGES_COUNT; i++) {
			currentImages[i] = BitmapFactory.decodeResource(getResources(), currentIDs[i], null);
		}
		
		imageView.setImageBitmap(currentImages[0]);
		
		currentCorrectAnswer = currentRound.getCorrectAnswer();
		currentAnswers = currentRound.getAnswers();
		
		for (int i = 0; i < currentAnswers.length; i++) {
			buttons[i].setText(currentAnswers[i]);
		}
	}

	@Override
	public void onClick(View v) {
		if(((Button)v).getText().equals(currentCorrectAnswer)) {
			isRunning = false;
			showDialog(CORRECT_ANSWER);
		} else {
			Toast.makeText(this, "Incorrect answer", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch(id) {
			case TIME_IS_UP:
				dialog = new AlertDialog.Builder(this)
				.setMessage("Time is up !")
				.setTitle("Sorry, brah")
				.setPositiveButton("Next round", new Dialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						initNextRound();
					}
					
				})
				.create();
				break;
			case CORRECT_ANSWER:
				dialog = new AlertDialog.Builder(this)
				.setMessage("You are correct! Hell yeah!")
				.setTitle("Good job!")
				.setPositiveButton("Next round", new Dialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						initNextRound();
					}
					
				})
				.create();
			break;
			case GAME_WON:
				dialog = new AlertDialog.Builder(this)
				.setMessage("Game finished")
				.setTitle("Good job! You're gangster.")
				.setPositiveButton("Exit", new Dialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						timer.purge();
						finish();
					}
					
				})
				.create();
			break;
		}
		return dialog;
	}
		
	
	private class GameLoop extends TimerTask {
		
		private Runnable newRoundCheck = new Runnable() {
			
			@Override
			public void run() {
				Log.i("PROGRESS", String.valueOf(progress));
				if(progress <= 0) {
					isRunning = false;
					showDialog(TIME_IS_UP);
				} else if(progress <= 66 && progress > 33) {
					if(((BitmapDrawable)imageView.getDrawable()).getBitmap() != currentImages[1]) {
						imageView.setImageBitmap(currentImages[1]);
					}
				} else if(progress <= 33) {
					if(((BitmapDrawable)imageView.getDrawable()).getBitmap() != currentImages[2]) {
						imageView.setImageBitmap(currentImages[2]);
					}
				}
			}
		};

		@Override
		public void run() {
			if(isRunning) {
				progress -= (100 / ROUND_LENGTH);
				progressBar.setProgress(progress);
				runOnUiThread(newRoundCheck);
			}
		}
		
		
	}
	
}