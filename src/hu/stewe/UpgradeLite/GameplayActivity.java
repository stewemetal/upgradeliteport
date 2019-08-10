package hu.stewe.UpgradeLite;

import hu.stewe.UpgradeLite.UpgradePlayField.UpgradeThread;
import hu.stewe.UpgradeLiteDB.SaveSlot;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class GameplayActivity extends Activity
{
	
	private static final int STATE_PAUSE = 1;
	private static final int STATE_RUNNING = 2;
	private static final int STATE_WIN = 3;
	private static final int STATE_LOSE = 4;

	UpgradeThread vUpgradeThread;

	UpgradePlayField vUpgradePlayField;

	SaveSlot save;

	private int backKeyLogger;

	private int vXmax;
	private int vYmax;

	private Display vDisplay;
	private ProgressDialog loadingDialog;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.playfield);
		
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loadingDialog.setMessage("Loading...");
		loadingDialog.setCancelable(false);
		loadingDialog.show();
		Log.v("progressdialog", "shown");

		backKeyLogger = 0;

		save = (SaveSlot) (getIntent().getParcelableExtra(getPackageName() + ".saveslot"));

		vDisplay = getWindowManager().getDefaultDisplay();

		vUpgradePlayField = (UpgradePlayField) findViewById(R.id._playfield);

		vUpgradePlayField.setDisplayParameters(vDisplay.getWidth(), vDisplay.getHeight());
		vUpgradePlayField.setSaveSlot(save);
		vUpgradePlayField.LOAD();
		vUpgradeThread = vUpgradePlayField.getThread();

		vUpgradeThread.setDisplay(vDisplay);

		if(savedInstanceState == null)
		{
			// we were just launched: set up a new game
			vUpgradeThread.setState(STATE_RUNNING);
		} else
		{
			// we are being restored: resume a previous game
			vUpgradeThread.restoreState(savedInstanceState);
		}
		
		loadingDialog.dismiss();
		Log.v("progressdialog", "dismissed");
		vUpgradeThread.doStart();
	}

	/**
	 * Invoked when the Activity loses user focus.
	 */
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.v("gameplayActivity", "GPA Paused");
		vUpgradePlayField.SAVE();
		vUpgradeThread.pause(); // pause game when Activity pauses
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if(vUpgradeThread.getGameState() == STATE_PAUSE)
			vUpgradePlayField.LOAD();
		vUpgradeThread.unpause(); // resume game when Activity resumes
		Log.v("gameplayActivity", "GPA Resumed");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		try
		{
			Log.v("gameplayActivity", "GPA Stopped");
			vUpgradeThread.join();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed()
	{
		Log.v("GameplayActivity","Back pressed");
		int state = vUpgradeThread.getGameState();
		if(state == STATE_WIN || state == STATE_LOSE)
		{
			finish();
		}
		else if(state == STATE_RUNNING)
		{
			vUpgradeThread.pause();
		} 
		else if(state == STATE_PAUSE)
		{
			vUpgradeThread.unpause();
		}
		
	}

	/**
	 * Notification that something is about to happen, to give the Activity a
	 * chance to save state.
	 * 
	 * @param outState
	 *            a Bundle into which this Activity should save its state
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// just have the View's thread save its state into our Bundle
		super.onSaveInstanceState(outState);
		vUpgradeThread.saveState(outState);
	}
}
