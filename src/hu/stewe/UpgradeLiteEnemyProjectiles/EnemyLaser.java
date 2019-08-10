package hu.stewe.UpgradeLiteEnemyProjectiles;

import hu.stewe.UpgradeLite.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

public class EnemyLaser extends EnemyProjectile
{
		
	public EnemyLaser(Resources res, int PosX, int PosY)
	{		
		super(BitmapFactory.decodeResource(res, R.drawable.laser_test), PosX, PosY, 0, 2, 2, 0);
	}

}
