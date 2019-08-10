package hu.stewe.UpgradeLite;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewGameMenu extends Activity implements OnClickListener{

	EditText input_player;
	Button startng;
	
	SharedPreferences uData;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newgame_menu);
		input_player = (EditText)findViewById(R.id.input_name_et);
		startng = (Button)findViewById(R.id.start_ng);
		startng.setOnClickListener(this);
		uData = getSharedPreferences(getString(R.string.PREFS), 0);
		resetValues();
	}
	
	private void resetValues()
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
	        edit.putInt(getString(R.string.cash), 10000);
	        edit.putString(getString(R.string.player_name), "N/A");
	        edit.commit();
	}

	public void onClick(View v) {
		SharedPreferences.Editor edit = uData.edit();
		edit.putString(getString(R.string.player_name), input_player.getText().toString());
		edit.putBoolean(getString(R.string.first_time), false);
		edit.commit();
		Intent changer = new Intent();
		changer.setClass(NewGameMenu.this, ContinueMenu.class);
		startActivity(changer);
		finish();
		
	}
	
}
