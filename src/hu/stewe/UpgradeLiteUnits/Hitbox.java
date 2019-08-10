package hu.stewe.UpgradeLiteUnits;

import android.graphics.RectF;

public class Hitbox
{
	private float left;
	private float right;
	private float top;
	private float bottom;
	
	
	public Hitbox(float l, float r, float t, float b)
	{
		left = l;
		right = r;
		top = t;
		bottom = b;
	}
	
	public void setPos(float l, float r, float t, float b)
	{
		left = l;
		right = r;
		top = t;
		bottom = b;
	}
	
	
	public float getLeft()
	{
		return left;
	}
	public void setLeft(float left)
	{
		this.left = left;
	}
	public float getRight()
	{
		return right;
	}
	public void setRight(float right)
	{
		this.right = right;
	}
	public float getTop()
	{
		return top;
	}
	public void setTop(float top)
	{
		this.top = top;
	}
	public float getBottom()
	{
		return bottom;
	}
	public void setBottom(float bottom)
	{
		this.bottom = bottom;
	}
	
	public RectF getRectF()
	{
		return new RectF(left, top, right, bottom);
	}
}
