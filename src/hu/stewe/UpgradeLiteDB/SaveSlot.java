package hu.stewe.UpgradeLiteDB;

import android.os.Parcel;
import android.os.Parcelable;

public class SaveSlot implements Parcelable
{
	private int _ID;
	private int vLevel;
	private int vCash; 
	private int vGraphicsLevel;
	private int vWeaponLevel;
	private int vHealthLevel;
	private boolean vSoundPurchased;
	private boolean vMusicPurchased;
	private boolean vSoundOn;
	private boolean vMusicOn;
	
	public static final Parcelable.Creator<SaveSlot> CREATOR = new Parcelable.Creator<SaveSlot>()
			{
				 public SaveSlot createFromParcel(Parcel in) {
				     return new SaveSlot(in);
				 }
				
				 public SaveSlot[] newArray(int size) {
				     return new SaveSlot[size];
				 }
			};
			
	public SaveSlot(int ID)
	{
		_ID = ID;
		vLevel = 1;
		vCash = 0;
		vGraphicsLevel = 1;
		vWeaponLevel = 1;
		vHealthLevel = 1;
		vSoundPurchased = false;
		vMusicPurchased = false;
		vSoundOn = false;
		vMusicOn = false;
	}
	
	public SaveSlot(int ID, int level,int cash,int graph, int weapon, int health, boolean sound, boolean music, boolean sOn, boolean mOn)
	{
		_ID = ID;
		vLevel = level;
		vCash = cash;
		vGraphicsLevel = graph;
		vWeaponLevel = weapon;
		vHealthLevel = health;
		vSoundPurchased = sound;
		vMusicPurchased = music;
		vSoundOn = sOn;
		vMusicOn = mOn;
	}
	
	public SaveSlot(Parcel in)
	{
		_ID = in.readInt();
		vLevel = in.readInt();
		vCash = in.readInt();
		vGraphicsLevel = in.readInt();
		vWeaponLevel = in.readInt();
		vHealthLevel = in.readInt();
		vSoundPurchased = in.readByte() == 1;
		vMusicPurchased = in.readByte() == 1;
		vSoundOn = in.readByte() == 1;
		vMusicOn = in.readByte() == 1;
	}

	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(_ID);
		dest.writeInt(vLevel);
		dest.writeInt(vCash);
		dest.writeInt(vGraphicsLevel);
		dest.writeInt(vWeaponLevel);
		dest.writeInt(vHealthLevel);
		dest.writeByte((byte)(vSoundPurchased ? 1:0));
		dest.writeByte((byte)(vMusicPurchased ? 1:0));
		dest.writeByte((byte)(vSoundOn ? 1:0));
		dest.writeByte((byte)(vMusicOn ? 1:0));
		
	}
	
	public int getID()
	{
		return _ID;
	}
	
	public int getLevel()
	{
		return vLevel;
	}
	
	public int getCash()
	{
		return vCash;
	}
	
	public int getGraphicsLevel()
	{
		return vGraphicsLevel;
	}
	
	public int getWeaponLevel()
	{
		return vWeaponLevel;
	}
	
	public int getHealthLevel()
	{
		return vHealthLevel;
	}
	
	public boolean getSoundPurchased()
	{
		return vSoundPurchased;
	}
	
	public boolean getMusicPurchased()
	{
		return vMusicPurchased;
	}
	
	public boolean getSoundState()
	{
		return vSoundOn;
	}
	
	public boolean getMusicState()
	{
		return vMusicOn;
	}
	
	public void setID(int id)
	{
		_ID = id;
	}
	
	public void setLevel(int lvl)
	{
		vLevel = lvl;
	}
	
	public void setCash(int csh)
	{
		vCash = csh;
	}
	
	public void setGraphicsLevel(int glvl)
	{
		vGraphicsLevel = glvl;
	}
	
	public void setWeaponLevel(int wlvl)
	{
		vWeaponLevel = wlvl;
	}
	
	public void setHealthLevel(int hlvl)
	{
		vHealthLevel = hlvl;
	}
	
	public void setSoundPurchased(boolean b)
	{
		vSoundPurchased = b;
	}
	
	public void setMusicPurchased(boolean b)
	{
		vMusicPurchased = b;
	}
	
	public void setSoundState(boolean b)
	{
		vSoundOn = b;
	}
	
	public void setMusicState(boolean b)
	{
		vMusicOn = b;
	}
	
	public void incLevel()
	{
		vLevel++;
	}
	
	public void incHealthLevel()
	{
		vHealthLevel++;
	}
	
	public void incWeaponLevel()
	{
		vWeaponLevel++;
	}
	
	public void incCash(int amount)
	{
		vCash += amount;
	}
	
}
