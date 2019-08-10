package hu.stewe.UpgradeLite;

import android.widget.RatingBar;


public class UpgradeItem
{
	private String uName;
	private int uLvl;
	private int uMaxLvl;
	
	public UpgradeItem(String name, int initLvl, int maxLvl)
	{
		uName = name;
		uLvl = initLvl;
		uMaxLvl = maxLvl;
	}
	
	public String getName()
	{
		return uName;
	}
	
	public int getLvl()
	{
		return uLvl;
	}
	
	public int getMaxLvl()
	{
		return uMaxLvl;
	}
	
	public void setLvl(int lvl)
	{
		uLvl = lvl;
	}
}
