package hu.stewe.UpgradeLite;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpgradeMenu extends Activity
{

	ListView UpgradesList;
	TextView cashreg;
	
	ArrayList<UpgradeItem> vUpgrades;
	
	RatingBar soundBar;
	RatingBar musicBar;
	RatingBar graphBar;
	RatingBar weaponBar;
	
	int soundLevel;
	int musicLevel;
	int graphLevel;
	int weaponLevel;
	int cash;
	
	SharedPreferences uData;
	UpgradeMenuAdapter adapter;
	
	private static final int PRICE_SOUND_L1 = 200;
	private static final int PRICE_SOUND_L2 = (int)(PRICE_SOUND_L1*1.5);
	private static final int PRICE_SOUND_L3 = PRICE_SOUND_L2*2;
	private static final int[] PRICE_SOUND = {PRICE_SOUND_L1,PRICE_SOUND_L2,PRICE_SOUND_L3};
    private static final int PRICE_MUSIC_L1 = 400;
    private static final int PRICE_MUSIC_L2 = (int)(PRICE_MUSIC_L1*1.5);
    private static final int PRICE_MUSIC_L3 = PRICE_MUSIC_L2*2;
    private static final int[] PRICE_MUSIC = {PRICE_MUSIC_L1,PRICE_MUSIC_L2,PRICE_MUSIC_L3};
    private static final int PRICE_GRAPHICS_L2 = 1200;
    private static final int PRICE_GRAPHICS_L3 = (int)(PRICE_GRAPHICS_L2*1.5);
    private static final int[] PRICE_GRAPHICS = {0,PRICE_GRAPHICS_L2,PRICE_GRAPHICS_L3};
    private static final int PRICE_WEAPON_L2 = 500;
    private static final int PRICE_WEAPON_L3 = PRICE_WEAPON_L2*3;
    private static final int[] PRICE_WEAPON = {0,PRICE_WEAPON_L2,PRICE_WEAPON_L3};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upgrade_menu);
		UpgradesList = (ListView) findViewById(R.id.upList);
		/*sound = (TextView) findViewById(R.id.sound);
		music = (TextView) findViewById(R.id.music);
		graphics = (TextView) findViewById(R.id.graphics);
		weapon = (TextView) findViewById(R.id.weapon);
		soundBar = (RatingBar) findViewById(R.id.sound_bar);
		musicBar = (RatingBar) findViewById(R.id.music_bar);
		graphBar = (RatingBar) findViewById(R.id.graphics_bar);
		weaponBar = (RatingBar) findViewById(R.id.weapon_bar);*/
		cashreg = (TextView) findViewById(R.id.cashdisp);
		uData = getSharedPreferences(getString(R.string.PREFS), 0);
		loadPrefs();
		
		vUpgrades = new ArrayList<UpgradeItem>();
		
		
		//TODO List items
		
		vUpgrades.add(new UpgradeItem(getString(R.string.upgrademenu_sound),soundLevel,3));
		vUpgrades.add(new UpgradeItem(getString(R.string.upgrademenu_music),musicLevel,3));
		vUpgrades.add(new UpgradeItem(getString(R.string.upgrademenu_graphics),graphLevel,3));
		vUpgrades.add(new UpgradeItem(getString(R.string.upgrademenu_weapon),weaponLevel,3));
		
		adapter = new UpgradeMenuAdapter(getApplicationContext(), vUpgrades);
		UpgradesList.setAdapter(adapter);
		
		UpgradesList.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long arg3)
			{
				UpgradeItem selected = (UpgradeItem) UpgradesList.getItemAtPosition(position);
				RatingBar lvlInd = (RatingBar) findViewById(R.id.upgradeItemBar);

				if(selected.getLvl() < selected.getMaxLvl())
				{
					switch(position)
					{
						case 0:
							soundLevel = shopHandler(soundLevel,PRICE_SOUND);
							selected.setLvl(soundLevel);
							break;
						case 1:
							musicLevel = shopHandler(musicLevel,PRICE_MUSIC);
							selected.setLvl(musicLevel);
							break;
						case 2:
							graphLevel = shopHandler(graphLevel,PRICE_GRAPHICS);
							selected.setLvl(graphLevel);
							break;
						case 3:
							weaponLevel = shopHandler(weaponLevel,PRICE_WEAPON);
							selected.setLvl(weaponLevel);
							break;														
					}
					
				}
				/*UpgradeMenuAdapter tempAdapter = new UpgradeMenuAdapter(getApplicationContext(), vUpgrades);
				UpgradesList.setAdapter(tempAdapter);
				UpgradesList.setSelection(position);*/
						
				savePrefs();
				adapter.notifyDataSetChanged();
			}
			
		});
		
		
	}
	
	private void loadPrefs()
	{
		soundLevel = uData.getInt(getString(R.string.sound_level), 0);
		musicLevel = uData.getInt(getString(R.string.music_level), 0);
		graphLevel = uData.getInt(getString(R.string.graphics_level), 0);
		weaponLevel = uData.getInt(getString(R.string.weapon_level), 0);
		cash = uData.getInt(getString(R.string.cash), 0);
		
		cashreg.setText(getString(R.string.cashreg)+(" ")+Integer.toString(cash));
	}
	
	private void savePrefs()
	{
		SharedPreferences.Editor edit = uData.edit();
		edit.putInt(getString(R.string.sound_level), soundLevel);
		edit.putInt(getString(R.string.music_level), musicLevel);
		edit.putInt(getString(R.string.graphics_level), graphLevel);
		edit.putInt(getString(R.string.weapon_level), weaponLevel);
		edit.putInt(getString(R.string.cash), cash);
		edit.commit();
	}
	
	private int shopHandler(int level, int[] prices)
	{
		if(prices[level]<=cash)
		{
			cash-=prices[level];
			level++;
		}
		else
			Toast.makeText(getApplicationContext(), getString(R.string.upgrademenu_nocash), Toast.LENGTH_SHORT).show();
		cashreg.setText(getString(R.string.cashreg).concat(" ").concat(Integer.toString(cash)));
		return level;
	}

}
