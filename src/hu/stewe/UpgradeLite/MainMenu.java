package hu.stewe.UpgradeLite;

import java.util.prefs.Preferences;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends ListActivity
{

	// game progress and player related variables
	public boolean firstTimeStarted;
	public int soundLevel;
	public int musicLevel;
	boolean soundOn;
	boolean musicOn;
	public int graphicsLevel;
	public int healthLevel;
	public int weaponLevel;
	public int nextLevel;
	public int cash;
	public String playerName;

	// SP for storing game data
	static SharedPreferences uData;

	// Declaration of the main menu items
	ListView MainMenuList;
	TextView list_item;

	private static String[] MENUITEMS;

	private void initialize()
	{
		uData = getSharedPreferences(getString(R.string.PREFS), 0);
		SharedPreferences.Editor edit = uData.edit().clear();
		edit.commit();
		if(uData.contains("Name") == false || uData.getString(getString(R.string.player_name), "N/A").equals("N/A"))
		{
			firstTimeStarted = true;
		}
		if(firstTimeStarted)
		{
			createPrefsMap();
			// savePreferences();
		}
		MENUITEMS = new String[] { getString(R.string.mainmenuitem_continue), getString(R.string.mainmenuitem_newgame), getString(R.string.mainmenuitem_options) };
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, MENUITEMS));
		MainMenuList = getListView();
		MainMenuList.setOnItemClickListener(new OnItemClickListener()
		{

			public void onItemClick(AdapterView<?> list, View v, int arg2, long arg3)
			{
				if(((TextView) v).getText() == getString(R.string.mainmenuitem_newgame))
				{
					firstTimeStarted = uData.getBoolean(getString(R.string.first_time), true);
					if(firstTimeStarted == true)
					{
						firstTimeStarted = false;
						SharedPreferences.Editor edit = uData.edit();
						edit.putBoolean(getString(R.string.first_time), firstTimeStarted);
						edit.commit();
						Intent changer = new Intent();
						changer.setClass(MainMenu.this, NewGameMenu.class);
						startActivity(changer);
					} else
						showDialog(0);
				} else if(((TextView) v).getText() == getString(R.string.mainmenuitem_continue))
				{
					firstTimeStarted = uData.getBoolean(getString(R.string.first_time), true);
					if(firstTimeStarted == false)
					{
						Intent changer = new Intent();
						changer.setClass(MainMenu.this, ContinueMenu.class);
						startActivity(changer);
					} else
						Toast.makeText(getApplicationContext(), getString(R.string.mainmenu_toast_nosave), Toast.LENGTH_SHORT).show();
				} else if(((TextView) v).getText() == getString(R.string.mainmenuitem_options))
				{
					Intent changer = new Intent();
					changer.setClass(MainMenu.this, OptionsMenu.class);
					startActivity(changer);
				}

			}

		});
	}

	private void createPrefsMap()
	{
		SharedPreferences.Editor edit = uData.edit();
		edit.putBoolean(getString(R.string.first_time), true);
		edit.putInt(getString(R.string.sound_level), 0);
		edit.putInt(getString(R.string.music_level), 0);
		edit.putBoolean(getString(R.string.sound_enabled), false);
		edit.putBoolean(getString(R.string.music_enabled), false);
		edit.putInt(getString(R.string.graphics_level), 1);
		// edit.putInt("HealthLevel", healthLevel);
		edit.putInt(getString(R.string.weapon_level), 1);
		edit.putInt(getString(R.string.next_level), 1);
		edit.putInt(getString(R.string.cash), 10000);
		edit.putString(getString(R.string.player_name), "N/A");
		edit.commit();
	}

	public void savePreferences()
	{
		SharedPreferences.Editor edit = uData.edit();
		edit.putBoolean(getString(R.string.first_time), firstTimeStarted);
		edit.putInt(getString(R.string.sound_level), soundLevel);
		edit.putInt(getString(R.string.music_level), musicLevel);
		edit.putBoolean(getString(R.string.sound_enabled), soundOn);
		edit.putBoolean(getString(R.string.music_enabled), musicOn);
		edit.putInt(getString(R.string.graphics_level), graphicsLevel);
		// edit.putInt("HealthLevel", healthLevel);
		edit.putInt(getString(R.string.weapon_level), weaponLevel);
		edit.putInt(getString(R.string.next_level), nextLevel);
		edit.putInt(getString(R.string.cash), cash);
		edit.putString(getString(R.string.player_name), playerName);
		edit.commit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initialize();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onRestart()
	{
		super.onRestart();
		firstTimeStarted = uData.getBoolean(getString(R.string.first_time), false);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		firstTimeStarted = uData.getBoolean(getString(R.string.first_time), false);
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case 0:
				return new AlertDialog
					.Builder(this)
					.setTitle(getString(R.string.mainmenu_alert_title))
					.setMessage(getString(R.string.mainmenu_alert_text))
					.setPositiveButton(getString(R.string.alert_confirm), new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int whichButton)
						{
							createPrefsMap();
							Intent changer = new Intent();
							changer.setClass(MainMenu.this, NewGameMenu.class);
							startActivity(changer);
						}
					})
					.setNegativeButton(getString(R.string.alert_decline), null)
					.create();
		}
		return null;
	}

}
