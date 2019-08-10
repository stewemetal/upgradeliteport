package hu.stewe.UpgradeLite;

import hu.stewe.UpgradeLiteDB.SaveSlot;
import hu.stewe.UpgradeLiteDB.UpgradeDbLoader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class LauncherActivity extends Activity
{
	private SharedPreferences uPrefs;
	private boolean fts;
	
	private UpgradeDbLoader dbloader;
	private SaveSlot[] vSaveSlots;
	
	private GraphicMenu menu;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Context ctx = getApplicationContext();
		Resources res = getResources();
		
		setContentView(R.layout.graphical_menu);
		menu = (GraphicMenu)findViewById(R.id.graphmenu);
		
		vSaveSlots = new SaveSlot[3];
		uPrefs = ctx.getSharedPreferences(res.getString(R.string.PREFS), Context.MODE_PRIVATE);
		dbloader = new UpgradeDbLoader(ctx);
		SharedPreferences.Editor edit = uPrefs.edit();
		edit.putBoolean(res.getString(R.string.first_time), true).commit();
		fts = uPrefs.getBoolean(res.getString(R.string.first_time), true);
		dbloader.open();
		if(fts)
		{
			edit.putBoolean(res.getString(R.string.first_time), false).commit();
			for(int i = 0; i < 3; i++)
			{
				SaveSlot defSS = new SaveSlot(i);
				vSaveSlots[i] = defSS;
			}

			for(int i = 0; i < 3; i++)
			{
				dbloader.deleteSaveSlot(i);
				dbloader.createSaveSlot(vSaveSlots[i]);
			}

		}
		else
		{
			for(int i = 0; i < 3; i++)
			{
				vSaveSlots[i] = new SaveSlot(0);
				vSaveSlots[i] = dbloader.fetchSaveSlot(i);
			}
		}
		dbloader.close();
		menu.setScreenDimensions(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());
		menu.setSaveSlotContainer(vSaveSlots);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if(menu.getState() == GraphicMenu.STATE_MAINMENU) return;
		else if(menu.getState() != GraphicMenu.STATE_CONTINUE)
			menu.setState(GraphicMenu.STATE_CONTINUE);
	}
	
	@Override
	public void onBackPressed()
	{
		int state = menu.getState();
		if(state == GraphicMenu.STATE_STARTGAME || state == GraphicMenu.STATE_ABOUT)
		{
			menu.setState(GraphicMenu.STATE_MAINMENU);
		}
		else if(state == GraphicMenu.STATE_CONTINUE)
		{
			menu.setState(GraphicMenu.STATE_MAINMENU);
		}
		else if(state == GraphicMenu.STATE_STORY_1)
		{
			menu.setState(GraphicMenu.STATE_MAINMENU);
		}
		else if(state == GraphicMenu.STATE_STORY_2)
		{
			menu.setState(GraphicMenu.STATE_STORY_1);
		}
		else if(state == GraphicMenu.STATE_STORY_3)
		{
			menu.setState(GraphicMenu.STATE_STORY_2);
		}
		else if(state == GraphicMenu.STATE_STORY_4)
		{
			menu.setState(GraphicMenu.STATE_STORY_3);
		}
		else if(state == GraphicMenu.STATE_MAINMENU)
		{
			finish();
		}
			
	}
}
