package hu.stewe.UpgradeLiteUnits;

import hu.stewe.UpgradeLitePlayerProjectiles.PlayerProjectile;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Unit {
	
	protected Bitmap vImage;
	
	private Bitmap vDestroyedSprite;
	
	protected boolean isOnScreen;
	
	protected boolean isDestroyed;
	
	protected int vMaxHealth;
	
	protected int vActualHealth;
	
	protected float vPosX;
	
	protected float vPosY;
	
	public float vSpeedX;
	
	public float vSpeedY;
	
	public float vAccelX;
	
	public float vAccelY;
	
	private int vDestroyedFrames;
	
	private int vDestroyedCurrentFrame;
	
	private Rect vSRectangle;
	
    private int vDestroyedSpriteHeight;
    
    private int vDestroyedSpriteWidth;
    
    private boolean vDestroyedAnimationStarted;
    
	
	protected ArrayList<PlayerProjectile> PlayerProjectiles;
	
	/**
	 * The constructor of a unit. All parameters should be set for the game to work
	 * @param Image The unit's image
	 * @param DestroyedSprite The sprite used to animate a unit's death
	 * @param Health The maximum health the unit can have
	 * @param PosX The initial X coordinate of the unit on the screen
	 * @param PosY The initial Y coordinate of the unit on the screen
	 */
	public Unit(Bitmap Image, Bitmap DestroyedSprite, int Health, int PosX, int PosY)
	{
		vImage = Image;
		vDestroyedSprite = DestroyedSprite;
		vDestroyedSpriteWidth = (int)DestroyedSprite.getWidth() / 8;
		vDestroyedSpriteHeight = DestroyedSprite.getHeight();
		vDestroyedAnimationStarted = false;
		vDestroyedCurrentFrame = 0;
		vDestroyedFrames = 8;
		vSRectangle = new Rect(0,0,0,0);
		vSRectangle.top = 0;
	    vSRectangle.bottom = vDestroyedSpriteHeight;
	    vSRectangle.left = 0;
	    vSRectangle.right = vDestroyedSpriteWidth;
		isOnScreen = false;
		isDestroyed = false;
		vMaxHealth = Health;
		vActualHealth = vMaxHealth;
		vPosX = PosX;
		vPosY = PosY;
		vSpeedX = 0;
		vSpeedY = 0;
		PlayerProjectiles = new ArrayList<PlayerProjectile>();
	}
	
	/**
	 * Returns the X coordinate of the unit
	 * @return
	 */
	public float getPosX()
	{
		return vPosX;
	}
	
	/**
	 * Returns the Y coordinate of the unit
	 * @return
	 */
	public float getPosY()
	{
		return vPosY;
	}
	
	/**
	 * Returns the Player's projectile bay
	 * @return
	 */
	public ArrayList<PlayerProjectile> getPlayerProjectiles()
	{
		return PlayerProjectiles;
	}
	
	/**
	 * Returns the maximum health the unit can have
	 * @return
	 */
	public int getMaxHealth()
	{
		return vMaxHealth;
	}
	
	public void initialHealth(int health)
	{
		vMaxHealth = health;
		vActualHealth = health;
	}
	
	/**
	 * Returns the unit's actual Health level
	 * @return
	 */
	public int getActualHealth()
	{
		return vActualHealth;
	}
	
	/**
	 * The function that handles taken damage, and starts death animation if necessary
	 * @param dmg The amount of damage the unit takes
	 */
	public void hitForDamage(int dmg)
	{
		vActualHealth -= dmg;
		if(getActualHealth() <= 0)
		{
			vActualHealth = 0;
			setDestroyed(true);
			vDestroyedAnimationStarted = true;
		}
	}
	
	/**
	 * Set the destroyed state of the unit
	 * @param b True or False
	 */
	public void setDestroyed(boolean b)
	{
		isDestroyed = b;
	}
	
	/**
	 * Returns the life state of the unit
	 * @return True, if the unit was destroyed, False is it is alive
	 */
	public boolean isDestroyed()
	{
		return isDestroyed;
	}
	
	/**
	 * Sets the visibility for the unit
	 * @param b True or False
	 */
	public void setOnScreen(boolean b)
	{
		isOnScreen = b;
	}
	
	/**
	 * Returns with the visibility of the unit
	 * @return True, if the Unit is on the screen, and False if it is not.
	 */
	public boolean isOnScreen()
	{
		return isOnScreen;
	}
	
	/**
	 * Returns the unit's Bitmap's width
	 * @return
	 */
	public int getImageSizeX()
	{
		return vImage.getWidth();
	}
	
	/**
	 * Returns the height of the unit's Bitmap's height
	 * @return
	 */
	public int getImageSizeY()
	{
		return vImage.getHeight();
	}
	
	/**
	 * Returns the sprite that is drawn as an animation when a unit is destroyed
	 * @return The sprite
	 */
	public Bitmap getDestroyedSprite()
	{
		return vDestroyedSprite;
	}
	
	/**
	 * Draws the Player-Type unit and all managed projectiles of it on the supported canvas
	 * @param c The canvas to draw on
	 */
	public void drawPlayer(Canvas c)
	{
		if(vDestroyedAnimationStarted == false)
			c.drawBitmap(vImage, vPosX, vPosY, null);
		else 
		{
			if(vDestroyedCurrentFrame < vDestroyedFrames)
			{
				int AnimationPosX = (int)(getPosX()+(getImageSizeX()/2));
				int AnimationPosY = (int)(getPosY()+(getImageSizeY()/2));
				
				if(getImageSizeX() <= vDestroyedSpriteWidth)
					AnimationPosX -= (int)(vDestroyedSpriteWidth/2);
				if(getImageSizeY() <= vDestroyedSpriteHeight)
					AnimationPosY -= (int)(vDestroyedSpriteHeight/2);
				
				
				Rect dest = new Rect(AnimationPosX, AnimationPosY, AnimationPosX + vDestroyedSpriteWidth,
						AnimationPosY + vDestroyedSpriteHeight);
	 
				c.drawBitmap(vDestroyedSprite, vSRectangle, dest, null);
	            vDestroyedCurrentFrame++;
	            vSRectangle.left = vDestroyedCurrentFrame * vDestroyedSpriteWidth;
				vSRectangle.right = vSRectangle.left + vDestroyedSpriteWidth;
	        }
			else setOnScreen(false); 
		}
		drawProjectiles(c);
	}
	
	/**
	 * Handles the drawing of an enemy and it's destruction
	 * @param c The canvas to draw on
	 */
	public void drawEnemy(Canvas c)
	{
		if(vDestroyedAnimationStarted == false)
			c.drawBitmap(vImage, vPosX, vPosY, null);
		else 
		{
			if(vDestroyedCurrentFrame < vDestroyedFrames)
			{
				int AnimationPosX = (int)(getPosX()+(getImageSizeX()/2));
				int AnimationPosY = (int)(getPosY()+(getImageSizeY()/2));
				
				if(getImageSizeX() <= vDestroyedSpriteWidth)
					AnimationPosX -= (int)(vDestroyedSpriteWidth/2);
				if(getImageSizeY() <= vDestroyedSpriteHeight)
					AnimationPosY -= (int)(vDestroyedSpriteHeight/2);
				
				
				Rect dest = new Rect(AnimationPosX, AnimationPosY, AnimationPosX + vDestroyedSpriteWidth,
						AnimationPosY + vDestroyedSpriteHeight);
	 
				c.drawBitmap(vDestroyedSprite, vSRectangle, dest, null);
	            vDestroyedCurrentFrame++;
	            vSRectangle.left = vDestroyedCurrentFrame * vDestroyedSpriteWidth;
				vSRectangle.right = vSRectangle.left + vDestroyedSpriteWidth;
	        }
			else setOnScreen(false); 
		}
	}
	
	/**
	 * Used to draw managed projectiles. Submethod of drawPlayer(Canvas c).
	 * @param c The canvas to draw on
	 */
	public void drawProjectiles(Canvas c)
	{
		for(int i=0; i<PlayerProjectiles.size();i++)
		{
			if((PlayerProjectiles.get(i).getPosY()+PlayerProjectiles.get(i).getImageSizeY()) < 0)
			{
				PlayerProjectiles.remove(i);
			}
			else
				c.drawBitmap(PlayerProjectiles.get(i).getImage(), PlayerProjectiles.get(i).getPosX(), PlayerProjectiles.get(i).getPosY(), null);
		}
	}
}
