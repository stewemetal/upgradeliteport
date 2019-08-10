package hu.stewe.UpgradeLiteUnits;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Player extends Unit
{
	
	ArrayList<Hitbox> hitboxes;
	private float scaledY;
	private float scaledX;
	
	public Player(Bitmap Image, Bitmap Destroyed, int Health, int PosX, int PosY)
	{
		super(Image, Destroyed, Health, PosX, PosY);
		setOnScreen(true);
		setDestroyed(false);
		scaledX = Image.getWidth();
		scaledY = Image.getHeight();
		generateHitboxes(vImage);
	}
	
	public void updatePositions(int posx, int posy)
	{	
		vPosX = posx;
		vPosY = posy;
		
	    //Update projectiles' positions
		/*for(int i=0; i<PlayerProjectiles.size();i++)
		{
				PlayerProjectiles.get(i).updatePosition();
		}*/
			
		hitboxes.get(0).setPos(vPosX + scaledX/2-scaledX/9, vPosX + scaledX/2+scaledX/9,vPosY,vPosY+scaledY);
		hitboxes.get(1).setPos(vPosX + scaledX/2-scaledX/5, vPosX + scaledX/2+scaledX/5,vPosY+scaledY/4,vPosY+scaledY);
		hitboxes.get(2).setPos(vPosX,vPosX + scaledX,vPosY+scaledY/2,vPosY+scaledY/15*14);
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
	
}
