package hu.stewe.UpgradeLitePlayerProjectiles;

import android.graphics.Bitmap;

public class PlayerLaser extends PlayerProjectile
{
	public static final float LASER_DMG_MULTIPLIER = 1.5f;
		
	public PlayerLaser(Bitmap image, int PosX, int PosY, int dmg)
	{		
		super(image, PosX, PosY, 0, 2, (int)(dmg*LASER_DMG_MULTIPLIER), 0);
	}

}
