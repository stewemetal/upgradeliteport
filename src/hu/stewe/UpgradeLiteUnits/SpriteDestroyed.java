package hu.stewe.UpgradeLiteUnits;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Rect;

public class SpriteDestroyed
{
	private Bitmap mAnimation;
    private int mXPos;
    private int mYPos;
    private Rect mSRectangle;
    private int mFPS;
    private int mNoOfFrames;
    private int mCurrentFrame;
    private long mFrameTimer;
    private int mSpriteHeight;
    private int mSpriteWidth;
    
	public SpriteDestroyed(Movie animation, int posx, int posy)
	{
		mSRectangle = new Rect(0,0,0,0);
	    mFrameTimer = 0;
	    mCurrentFrame = 0;
	    mXPos = 80;
	    mYPos = 200;
	}
	
	public void Initialise(Bitmap theBitmap, int Height, int Width, int theFPS, int theFrameCount) {
	    mAnimation = theBitmap;
	    mSpriteHeight = Height;
	    mSpriteWidth = Width;
	    mSRectangle.top = 0;
	    mSRectangle.bottom = mSpriteHeight;
	    mSRectangle.left = 0;
	    mSRectangle.right = mSpriteWidth;
	    mFPS = 1000 /theFPS;
	    mNoOfFrames = theFrameCount;
	}
	
	public void Update(long GameTime) {
	    if(GameTime > mFrameTimer + mFPS ) {
	        mFrameTimer = GameTime;
	        mCurrentFrame +=1;
	 
	        if(mCurrentFrame >= mNoOfFrames) {
	            mCurrentFrame = 0;
	        }
	    }
	 
	    mSRectangle.left = mCurrentFrame * mSpriteWidth;
	    mSRectangle.right = mSRectangle.left + mSpriteWidth;
	}
	
	public void draw(Canvas canvas) {
	    /*Rect dest = new Rect(getXPos(), getYPos(), getXPos() + mSpriteWidth,
	                    getYPos() + mSpriteHeight);
	 
	    canvas.drawBitmap(mAnimation, mSRectangle, dest, null);*/
	}
	
	
}
