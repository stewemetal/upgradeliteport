package hu.stewe.UpgradeLite;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContinueMenu extends Activity {

	ListView ContinueMenuList;
	TextView PlayerNameContainer;
	
	String playerName;
	int nextLevel;
	
	SharedPreferences uData;
	
	
	private static String[] MENUITEMS = new String[2];
	
	
	 @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    	loadPrefs();
    	setContentView(R.layout.continue_menu);
    	PlayerNameContainer = (TextView) findViewById(R.id.player_name_container);
    	PlayerNameContainer.setText(playerName);
    	MENUITEMS[0] = getString(R.string.continuemenu_nextlevel).concat(Integer.toString(nextLevel));
    	MENUITEMS[1] = getString(R.string.continuemenu_upgrades);
    	
        ContinueMenuList = (ListView) findViewById(R.id.continuemenu);
        ContinueMenuList.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,MENUITEMS));
        ContinueMenuList.setOnItemClickListener(new OnItemClickListener() 
        											{

    													public void onItemClick(AdapterView<?> arg0,View v,int arg2,long arg3)
    													{
    														if(((TextView)v) == (TextView)ContinueMenuList.getChildAt(0))
    														{
    															Intent changer = new Intent();
    															changer.setClass(ContinueMenu.this, GameplayActivity.class);
    															startActivity(changer);
    														} else
    														if(((TextView)v) == (TextView)ContinueMenuList.getChildAt(1))
    														{
    															Intent changer = new Intent();
    															changer.setClass(ContinueMenu.this, UpgradeMenu.class);
    															startActivity(changer);
    														}
    													}
        									        											
    	});	
    }
	 

	private void loadPrefs()
	{
    	uData = getSharedPreferences(getString(R.string.PREFS), 0);
    	playerName = uData.getString(getString(R.string.player_name),"N/A");
    	nextLevel = uData.getInt(getString(R.string.next_level), 1);
	}
}
