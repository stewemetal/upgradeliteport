package hu.stewe.UpgradeLite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class OptionsMenu extends Activity implements OnClickListener {

	
	ToggleButton soundT;
	ToggleButton musicT;
	Button resetB;
    boolean soundOn;
    boolean musicOn;
    int soundLevel;
    int musicLevel;
	SharedPreferences uData;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_menu);
		soundT = (ToggleButton)findViewById(R.id.soundToggle);
		soundT.setOnClickListener(this);
		musicT = (ToggleButton)findViewById(R.id.musicToggle);
		musicT.setOnClickListener(this);
		resetB = (Button)findViewById(R.id.resetButton);
		resetB.setOnClickListener(this);
		uData = getSharedPreferences(getString(R.string.PREFS), 0);
		soundOn = uData.getBoolean(getString(R.string.sound_enabled), false);
		musicOn = uData.getBoolean(getString(R.string.music_enabled), false);
		soundLevel = uData.getInt(getString(R.string.sound_level), 0);
		musicLevel = uData.getInt(getString(R.string.music_level), 0);
		setToggles();
	}
	
	private void setToggles()
	{
		if((soundT.isChecked() == true && soundOn == false) || (soundT.isChecked() == false && soundOn == true))
			soundT.toggle();
		if((musicT.isChecked() == true && musicOn == false) || (musicT.isChecked() == false && musicOn == true))
			musicT.toggle();
	}
	
	private void saveValues()
	{
		SharedPreferences.Editor edit = uData.edit();
		edit.putBoolean(getString(R.string.sound_enabled), soundOn);
		edit.putBoolean(getString(R.string.music_enabled), musicOn);
		edit.commit();
	}
	
	public void resetPrefs()
    {
    	SharedPreferences.Editor edit = uData.edit();
    	edit.putBoolean(getString(R.string.first_time), true);
        edit.putInt(getString(R.string.sound_level), 0);
        edit.putInt(getString(R.string.music_level), 0);
        edit.putBoolean(getString(R.string.sound_enabled), false);
        edit.putBoolean(getString(R.string.music_enabled), false);
        edit.putInt(getString(R.string.graphics_level), 1);
        //edit.putInt("HealthLevel", healthLevel);
        edit.putInt(getString(R.string.weapon_level), 1);
        edit.putInt(getString(R.string.next_level), 1);
        edit.putInt(getString(R.string.cash), 0);
        edit.putString(getString(R.string.player_name), "N/A");
        edit.commit();
        soundOn = false;
        musicOn = false;
        soundLevel = 0;
        musicLevel = 0;
        soundT.setChecked(false);
        musicT.setChecked(false);
    }
	
	@Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) 
    	{
    	case 0:
    		return new AlertDialog.Builder(this)
    		.setTitle(getString(R.string.optionsmenu_confirm))
    		.setPositiveButton(getString(R.string.alert_confirm), new DialogInterface.OnClickListener() 
    									 {
    										public void onClick(DialogInterface dialog,int whichButton)
    											{
    												resetPrefs();
    												Toast.makeText(getBaseContext(),getString(R.string.optionsmenu_deleted), Toast.LENGTH_SHORT).show();
    											}
    									 }
    							)
    		.setNegativeButton(getString(R.string.alert_decline), new DialogInterface.OnClickListener()
    										 {
    												public void onClick(DialogInterface dialog,int whichButton)
    												{
    													Toast.makeText(getBaseContext(),getString(R.string.optionsmenu_cancelled), Toast.LENGTH_SHORT).show();
    												}
    										 }
    		)
    		.setMessage(getString(R.string.optionsmenu_alerttext))
    		.create();
    	}
    return null;
   }
	
	public void onClick(View v) {
		if(v == soundT)
		{
			if(soundT.isChecked() == false)
			{
				soundOn = false;	
			}
			else
			{
				if(soundLevel > 0)
				{
					soundOn = true;
				}
				else 
					 {
						soundT.toggle();
						Toast.makeText(OptionsMenu.this,getString(R.string.optionsmenu_soundalert), Toast.LENGTH_SHORT).show();
					 }
			}
		}
		if(v == musicT)
		{
			if(musicT.isChecked() == false)
			{
				musicOn = false;
			}
			else
			{
				if(musicLevel > 0)
				{
					musicOn = true;
				}
				else 
					{
						musicT.toggle();
						Toast.makeText(OptionsMenu.this,getString(R.string.optionsmenu_musicalert), Toast.LENGTH_SHORT).show();
					}
			}
		}
		if(v == resetB)
		{
			showDialog(0);
		}
		saveValues();		
	}

	
	
	
	
}
