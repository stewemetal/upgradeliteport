package hu.stewe.UpgradeLite;

import hu.stewe.UpgradeLiteDB.SaveSlot;
import hu.stewe.UpgradeLiteDB.UpgradeDbLoader;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GraphicMenu extends View
{
	public static final int STATE_MAINMENU  = 0;
	public static final int STATE_STARTGAME = 1;
	public static final int STATE_LOADGAME  = 2;
	public static final int STATE_CONTINUE  = 3;
	public static final int STATE_ABOUT     = 4;
	public static final int STATE_STORY_1   = 5;
	public static final int STATE_STORY_2   = 6;
	public static final int STATE_STORY_3   = 7;
	public static final int STATE_STORY_4   = 8;
	
	
	int vState;
	Bitmap vMainMenu;
	Bitmap vSaveSlotsMenu;
	float MenuPosX;
	float MenuPosY;
	float SaveSlotsPosX;
	float SaveSlotsPosY;
	Bitmap vTitleImage;
	Bitmap vStartGameImage;
	Bitmap vBackgroundImage;
	Bitmap vMenuBckg;
	Bitmap vStory1;
	Bitmap vStory2;
	Bitmap vStory3;
	Bitmap vStory4;
	
	private UpgradeDbLoader dbloader;
	private SaveSlot[] vSaveSlots;
	private SaveSlot vSelectedSaveSlot;
	
	private Paint text;


	public GraphicMenu(Context context, AttributeSet attributes)
	{
		super(context,attributes);
		Resources res = getResources();
		vBackgroundImage = BitmapFactory.decodeResource(res, R.drawable.bckg);
		vMainMenu = BitmapFactory.decodeResource(res, R.drawable.mainmenu);
		vTitleImage = BitmapFactory.decodeResource(res, R.drawable.title2);
		vStory1 = BitmapFactory.decodeResource(res, R.drawable.story1);
		vStory2 = BitmapFactory.decodeResource(res, R.drawable.story2);
		vStory3 = BitmapFactory.decodeResource(res, R.drawable.story3);
		vStory4 = BitmapFactory.decodeResource(res, R.drawable.story4);
		vStartGameImage = BitmapFactory.decodeResource(res, R.drawable.load_title);
		vSaveSlotsMenu = BitmapFactory.decodeResource(res, R.drawable.startgamemenu);
		vMenuBckg = BitmapFactory.decodeResource(res, R.drawable.menubckg_alter);
		
		//vTitleImage = Bitmap.createScaledBitmap(vTitleImage, getWidth(), getHeight()/4, true);
		vState = STATE_MAINMENU;
	}
	
	@Override
	public void onDraw(Canvas c)
	{
		if(vState == STATE_MAINMENU)
		{
			
			MenuPosX = c.getWidth()/2 - vMainMenu.getWidth()/2;
			MenuPosY = c.getHeight()/20 + vTitleImage.getHeight();
			c.drawBitmap(vMenuBckg, 0, 0, null);
			c.drawBitmap(vTitleImage, c.getWidth()/2 - vTitleImage.getWidth()/2, 0, null);
			c.drawBitmap(vMainMenu, MenuPosX, MenuPosY, null);
		}
		else if(vState == STATE_STARTGAME)
		{
			SaveSlotsPosX = c.getWidth()/2 - vSaveSlotsMenu.getWidth()/2;
			SaveSlotsPosY = vStartGameImage.getHeight();
			c.drawBitmap(vStartGameImage, 0, 0, null);
			c.drawBitmap(vSaveSlotsMenu, SaveSlotsPosX, SaveSlotsPosY, null);
		}
		else if(vState == STATE_STORY_1)
		{
			c.drawBitmap(vStory1, 0, 0, null);
		}
		else if(vState == STATE_STORY_2)
		{
			c.drawBitmap(vStory2, 0, 0, null);
		}
		else if(vState == STATE_STORY_3)
		{
			c.drawBitmap(vStory3, 0, 0, null);
		}
		else if(vState == STATE_STORY_4)
		{
			c.drawBitmap(vStory4, 0, 0, null);
		}
	}
	
	@Override
	public void onSizeChanged(int w, int h, int ow, int oh)
	{
		float multiplier = w / vTitleImage.getWidth();
		vTitleImage = Bitmap.createScaledBitmap(vTitleImage, (int)(vTitleImage.getWidth()*multiplier), (int)(vTitleImage.getHeight()*multiplier), true);
	}
	
	public void setScreenDimensions(int w, int h)
	{
		int bckgw = vBackgroundImage.getWidth();
		int bckgh = vBackgroundImage.getHeight();
		double multiplierw = ((float)w / bckgw);
		double multiplierh = ((float)h / bckgh);
		double multiplier = multiplierh < multiplierw ? multiplierh:multiplierw;
		vTitleImage = Bitmap.createScaledBitmap(vTitleImage, (int)(vTitleImage.getWidth()*multiplier), (int)(vTitleImage.getHeight()*multiplier), true);
		vBackgroundImage = Bitmap.createScaledBitmap(vBackgroundImage,(int)(vBackgroundImage.getWidth()*multiplier), (int)(vBackgroundImage.getHeight()*multiplier), true);
		vStartGameImage = Bitmap.createScaledBitmap(vStartGameImage, (int)(vStartGameImage.getWidth()*multiplier), (int)(vStartGameImage.getHeight()*multiplier), true);
		vStory1 = Bitmap.createScaledBitmap(vStory1, (int)(vStory1.getWidth()*multiplier), (int)(vStory1.getHeight()*multiplier), true);
		vStory2 = Bitmap.createScaledBitmap(vStory2, (int)(vStory2.getWidth()*multiplier), (int)(vStory2.getHeight()*multiplier), true);
		vStory3 = Bitmap.createScaledBitmap(vStory3, (int)(vStory3.getWidth()*multiplier), (int)(vStory3.getHeight()*multiplier), true);
		vStory4 = Bitmap.createScaledBitmap(vStory4, (int)(vStory4.getWidth()*multiplier), (int)(vStory4.getHeight()*multiplier), true);
		vMenuBckg = Bitmap.createScaledBitmap(vMenuBckg, (int)(vMenuBckg.getWidth()*multiplier), (int)(vMenuBckg.getHeight()*multiplier), true);
		vSaveSlotsMenu = Bitmap.createScaledBitmap(vSaveSlotsMenu, (int)(vSaveSlotsMenu.getWidth()*multiplier), (int)(vSaveSlotsMenu.getHeight()*multiplier), true);
	}
	
	public void setSaveSlotContainer(SaveSlot[] saveslots)
	{
		vSaveSlots = saveslots;
	}
	
	public SaveSlot getSelectedSaveSlot()
	{
		return vSelectedSaveSlot;
	}
	
	public void setState(int state)
	{
		vState = state;
		invalidate();
	}
	
	public int getState()
	{
		return vState;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{	
		if(event.getAction() != MotionEvent.ACTION_UP)
			return true;
		else
		{			
			float x = event.getX();
			float y = event.getY();
			if(vState == STATE_MAINMENU)
			{
				if( MenuPosY < y 
						&& MenuPosX < x 
						&& (MenuPosY + vMainMenu.getHeight()/4) > y 
						&& (MenuPosX + vMainMenu.getWidth()) > x )
				{
					setState(STATE_STARTGAME);
					invalidate();
					return true;
				}
				
				else if( MenuPosY + vMainMenu.getHeight()/4 < y 
						&& MenuPosX < x 
						&& (MenuPosY + vMainMenu.getHeight()/2) > y 
						&& (MenuPosX + vMainMenu.getWidth()) > x )
				{
					setState(STATE_STORY_1);
					invalidate();
					return true;
				}
				
				else if( MenuPosY + vMainMenu.getHeight()/2 < y 
						&& MenuPosX < x 
						&& (MenuPosY + vMainMenu.getHeight()/4*3) > y 
						&& (MenuPosX + vMainMenu.getWidth()) > x )
				{
					//setState(STATE_OPTIONS);
					invalidate();
					return true;
				}
				else if( MenuPosY + vMainMenu.getHeight()/4*3 < y 
						&& MenuPosX < x 
						&& (MenuPosY + vMainMenu.getHeight()) > y 
						&& (MenuPosX + vMainMenu.getWidth()) > x )
				{
					setState(STATE_ABOUT);
					invalidate();
					return true;
				}
			}
			else if(vState == STATE_STARTGAME)
			{
				if( SaveSlotsPosY < y 
						&& SaveSlotsPosX < x 
						&& (SaveSlotsPosY + vSaveSlotsMenu.getHeight()/3) > y 
						&& (SaveSlotsPosX + vSaveSlotsMenu.getWidth()) > x )
				{
					Log.v("SAVESLOT","ss1 selected");
					vSelectedSaveSlot = vSaveSlots[0];
					setState(STATE_LOADGAME);
					invalidate();
					Intent intent = new Intent(getContext(),GameplayActivity.class);
					intent.putExtra(getContext().getPackageName() + ".saveslot", vSelectedSaveSlot);
					((Activity)getContext()).startActivity(intent);
					return true;
				}
				
				else if( SaveSlotsPosY + (vSaveSlotsMenu.getHeight()/3) < y 
						&& SaveSlotsPosX < x 
						&& (SaveSlotsPosY + (vSaveSlotsMenu.getHeight()/3)*2) > y 
						&& (SaveSlotsPosX + vSaveSlotsMenu.getWidth()) > x )
				{
					Log.v("SAVESLOT","ss2 selected");
					vSelectedSaveSlot = vSaveSlots[1];
					setState(STATE_LOADGAME);
					invalidate();
					Intent intent = new Intent(getContext(),GameplayActivity.class);
					intent.putExtra(getContext().getPackageName() + ".saveslot", vSelectedSaveSlot);
					((Activity)getContext()).startActivity(intent);
					return true;
				}
				else if( SaveSlotsPosY + (vSaveSlotsMenu.getHeight()/3)*2 < y 
						&& SaveSlotsPosX < x 
						&& SaveSlotsPosY + (vSaveSlotsMenu.getHeight()) > y 
						&& (SaveSlotsPosX + vSaveSlotsMenu.getWidth()) > x )
				{
					Log.v("SAVESLOT","ss3 selected");
					vSelectedSaveSlot = vSaveSlots[2];
					setState(STATE_LOADGAME);
					invalidate();
					Intent intent = new Intent(getContext(),GameplayActivity.class);
					intent.putExtra(getContext().getPackageName() + ".saveslot", vSelectedSaveSlot);
					((Activity)getContext()).startActivity(intent);
					return true;
				}
			}
			else if(vState == STATE_STORY_1)
			{
				setState(STATE_STORY_2);
				invalidate();
				return true;
			}
			else if(vState == STATE_STORY_2)
			{
				setState(STATE_STORY_3);
				invalidate();
				return true;
			}
			else if(vState == STATE_STORY_3)
			{
				setState(STATE_STORY_4);
				invalidate();
				return true;
			}
			else if(vState == STATE_STORY_4)
			{
				setState(STATE_MAINMENU);
				invalidate();
				return true;
			}
		}
		return false;
	}

}
