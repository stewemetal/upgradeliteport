package hu.stewe.UpgradeLiteUnits;

import hu.stewe.UpgradeLiteEnemyProjectiles.EnemyProjectile;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;

public class Enemy extends Unit
{
	
	private static int vXmax;
	private static int vYmax;
	private int vSpeedX;
	private int vSpeedY;
	private boolean onScreen;
	public long lastAttackTime;
	private int attackDelay;
	private int cashValue;
	private float scaledX;
	private float scaledY;
	
	ArrayList<Hitbox> hitboxes;
	
	public Enemy(Bitmap Image, Bitmap Destroyed, int Health, int atcktime, int PosX, int PosY,int cash)
	{
		super(Image, Destroyed, Health, PosX, PosY);
		cashValue = cash;
		setAttackDelay(new Random().nextInt(atcktime)+3000);
		lastAttackTime = System.currentTimeMillis();
		setOnScreen(true); 
		scaledX = Image.getWidth();
		scaledY = Image.getHeight();
		generateHitboxes(Image);
	}
	
	private void generateHitboxes(Bitmap image)
	{
		Hitbox body = new Hitbox(0,0,0,0);
		Hitbox wings1 = new Hitbox(0,0,0,0);
		Hitbox wings2 = new Hitbox(0,0,0,0);
		hitboxes = new ArrayList<Hitbox>();
		hitboxes.add(body);
		hitboxes.add(wings1);
		hitboxes.add(wings2);
	}
	
	public ArrayList<Hitbox> getHitboxes()
	{
		return hitboxes;
	}

	public void setHitboxes(ArrayList<Hitbox> hitboxes)
	{
		this.hitboxes = hitboxes;
	}
	
	public void updatePosition()
	{
		
		vPosX += 0;
		vPosY += 0.7;
		
		//Check boundaries
		
	    if (vPosX > vXmax-getImageSizeX()) {
	        vPosX = vXmax-getImageSizeX();
	    } else if (vPosX < 0) {
	        vPosX = 0;
	    }
	    if (vPosY > vYmax) { 
	        setOnScreen(false);
	    }
	    
	    hitboxes.get(0).setPos(vPosX + scaledX/2-scaledX/7, vPosX + scaledX/2+scaledX/7,vPosY,vPosY+scaledY/15*14);
		//hitboxes.get(1).setPos(vPosX + scaledX/2-scaledX/4, vPosX + scaledX/2+scaledX/4,vPosY+scaledY/5,vPosY+scaledY);
		hitboxes.get(1).setPos(vPosX,vPosX + scaledX,vPosY+scaledY/3,vPosY+scaledY/4*3);
	}
	
	public int getAttackDelay()
	{
		return attackDelay;
	}
	
	public void setAttackDelay(int dly)
	{
		attackDelay = dly; 
	}
	
	/**
	 * Adds a proectile to the global Enemy bay
	 * @param projs The global Enemy projectile bay
	 * @param ep The EnemyProjectile that will be loaded into the bay
	 */
	public void fire()
	{
		setAttackDelay(new Random().nextInt(5000)+3500);
		lastAttackTime = System.currentTimeMillis();
	}
	
	public long getLastAttackTime()
	{
		return lastAttackTime;
	}
	
	public static void setScreenBoundaries(int x,int y)
	{
		vXmax = x;
		vYmax = y;
	}
	
	public void setOnScreen(boolean b)
	{
		onScreen = b;
	}
	
	public boolean isOnScreen()
	{
		return onScreen;
	}
	
	public void setSpeedX(int sx)
	{
		vSpeedX = sx;
	}
	
	public int getSpeedX()
	{
		return vSpeedX;
	}
	
	public void setSpeedY(int sy)
	{
		vSpeedY = sy;
	}
	
	public int getSpeedY()
	{
		return vSpeedY;
	}
	
	public void setMaxHealth(int h)
	{
		vMaxHealth = h;
	}
	
	public int getValue()
	{
		return cashValue;
	}
}
