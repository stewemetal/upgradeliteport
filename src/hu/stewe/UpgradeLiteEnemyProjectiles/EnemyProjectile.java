package hu.stewe.UpgradeLiteEnemyProjectiles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import java.lang.Math;

public class EnemyProjectile {
	
	public static final int LASER_L1_DMG = 2;
	public static final int LASER_L2_DMG = 4;
	public static final int LASER_L3_DMG = 6;
	
	private Bitmap vImage;
	
	private int vPosX;
	
	private int vPosY;
	
	private int vSpeedX;
	
	private int vSpeedY;
	
	private int vRotation;
	
	private int vDamage;

	private boolean isOnScreen;
	
	public EnemyProjectile(Bitmap Image, int PosX, int PosY, int SpeedX, int SpeedY, int Damage, int Rotation)
	{
		vImage = Image;
		vPosX = PosX;
		vPosY = PosY;
		vSpeedX = SpeedX;
		vSpeedY = SpeedY;
		vDamage = Damage;
		vRotation = Rotation;
	}
	
	public void setOnScreen(boolean b)
	{
		isOnScreen = b;
	}
	
	/**
	 * @return The damage of the projectile
	 */
	public int getDamage()
	{
		return vDamage;
	}

	public int getImageSizeX(Canvas c)
	{
		return vImage.getScaledWidth(c);
	}
	
	public int getImageSizeY(Canvas c)
	{
		return vImage.getScaledHeight(c);
	}
	
	public boolean isOnScreen()
	{
		return isOnScreen;
	}

	public Bitmap getImage()
	{
		return vImage;
	}
	
	public int getImageSizeX()
	{
		return vImage.getWidth();
	}
	
	public int getImageSizeY()
	{
		return vImage.getHeight();
	}
	
	public int getPosX()
	{
		return vPosX;
	}
	
	public int getPosY()
	{
		return vPosY;
	}
	
	public void setSpeedX(int speedx)
	{
		vSpeedX = speedx;
	}
	
	
	public void updatePosition()
	{
		if(vSpeedY > 0)
		{
			vPosY += vSpeedY;
		}
	}
	
	public void draw(Canvas c)
	{
		c.drawBitmap(vImage, vPosX, vPosY, null);
	}
}
