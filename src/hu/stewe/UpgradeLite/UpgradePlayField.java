package hu.stewe.UpgradeLite;

import hu.stewe.UpgradeLiteDB.SaveSlot;
import hu.stewe.UpgradeLiteDB.UpgradeDbLoader;
import hu.stewe.UpgradeLiteEnemyProjectiles.EnemyLaser;
import hu.stewe.UpgradeLiteEnemyProjectiles.EnemyProjectile;
import hu.stewe.UpgradeLitePlayerProjectiles.PlayerLaser;
import hu.stewe.UpgradeLitePlayerProjectiles.PlayerProjectile;
import hu.stewe.UpgradeLiteUnits.Enemy;
import hu.stewe.UpgradeLiteUnits.Hitbox;
import hu.stewe.UpgradeLiteUnits.Player;

import java.util.ArrayList;
import java.util.Random;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SyncAdapterType;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class UpgradePlayField extends SurfaceView implements SurfaceHolder.Callback
{

	/** Handle to the application context, used to e.g. fetch Drawables. */
	private Context vContext;

	private int gActualLevel;
	private int gHealthLevel;
	private int gEnemyNumber;
	private int gCash;

	private int gWeaponLevel;

	private Bitmap PAUSED;

	private Player player;
	private EnemyCreator enemyCreatorThread;
	private int enemiesDestroyed;
	private int enemiesMissed;

	Bitmap playerBitmap;
	Bitmap enemyBitmapL1;
	Bitmap explosionBitmap;
	private Bitmap vBackground;
	Bitmap playerLaserImage;
	Bitmap playerPlasmaImage;
	Bitmap playerPhotonImage;

	Paint HUDelement;
	RectF temprect;

	private float scale;

	private int vGameState;

	UpgradeDbLoader dbloader;
	SaveSlot vSaveSlot;
	int ssID;

	/** The thread that draws the animation */
	private UpgradeThread thread;

	private SensorManager sm;

	private Display vDisplay;

	private int vXmax;
	private int vYmax;

	ArrayList<Enemy> Enemies;
	ArrayList<EnemyProjectile> EnemyProjectiles;
	ArrayList<PlayerProjectile> PlayerProjectiles;
	ArrayList<Hitbox> PlayerHitboxes;
	ArrayList<Hitbox> HitboxesTemp;

	Enemy actualEnemy;
	PlayerProjectile actualPProjectile;
	EnemyProjectile actualEProjectile;
	Hitbox actualHitbox;

	ProgressDialog loadingDialog;

	public UpgradePlayField(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		dbloader = new UpgradeDbLoader(context);
		PlayerProjectiles = new ArrayList<PlayerProjectile>();
		PAUSED = BitmapFactory.decodeResource(getResources(), R.drawable.paused);
		vGameState = UpgradeThread.STATE_RUNNING;

		Resources res = getResources();
		playerBitmap = BitmapFactory.decodeResource(res, R.drawable.player);
		vBackground = BitmapFactory.decodeResource(res, R.drawable.bckg);
		enemyBitmapL1 = BitmapFactory.decodeResource(res, R.drawable.enemyl1);
		explosionBitmap = BitmapFactory.decodeResource(res, R.drawable.explosionsprite);
		PAUSED = BitmapFactory.decodeResource(res, R.drawable.paused);
		playerLaserImage = BitmapFactory.decodeResource(res, R.drawable.laser_test);

		PlayerHitboxes = new ArrayList<Hitbox>();
		HitboxesTemp = new ArrayList<Hitbox>();

		HUDelement = new Paint();
		temprect = new RectF();

		this.setWillNotDraw(false);
		setFocusable(true); // make sure we get key events
	}

	public SaveSlot getSaveSlot()
	{
		return vSaveSlot;
	}

	public void setSaveSlot(SaveSlot ss)
	{
		vSaveSlot = ss;
		ssID = vSaveSlot.getID();
		/*
		 * gHealthLevel = vSaveSlot.getHealthLevel(); gActualLevel =
		 * vSaveSlot.getLevel(); gWeaponLevel = vSaveSlot.getWeaponLevel();
		 * gCash = vSaveSlot.getCash(); gEnemyNumber = (int) (gActualLevel *
		 * 2.5); Log.v("PlayField","Game Loaded!");
		 */
	}

	public void SAVE()
	{
		dbloader.open();
		dbloader.updateProduct(ssID, vSaveSlot);
		dbloader.close();
		Log.v("PlayField", "Game Saved!");
	}

	public void LOAD()
	{
		gHealthLevel = vSaveSlot.getHealthLevel();
		gActualLevel = vSaveSlot.getLevel();
		gWeaponLevel = vSaveSlot.getWeaponLevel();
		gCash = vSaveSlot.getCash();
		gEnemyNumber = (int) (gActualLevel * 2.5);
		player.initialHealth(gHealthLevel);
		Log.v("PlayField", "Game Loaded!");
	}

	/**
	 * Fetches the animation thread corresponding to this UpgradePlayField.
	 * 
	 * @return the animation thread
	 */
	public UpgradeThread getThread()
	{
		return thread;
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus)
	{
		/*
		 * if(hasWindowFocus == false) thread.pause();
		 */
		Log.v("PlayField", "Window Focus changed to: " + (hasWindowFocus ? "true" : "false"));
	}

	public void setDisplayParameters(int xmax, int ymax)
	{
		vXmax = xmax;
		vYmax = ymax;
		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		Context context = getContext();

		// create thread only; it's started in surfaceCreated()
		thread = new UpgradeThread(holder, context, vXmax, vYmax);
	}

	/**
	 * Adapt surface parameters to change in suraface size
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		thread.setSurfaceSize(width, height);
	}

	public void surfaceCreated(SurfaceHolder holder)
	{
		// start the thread here so that we don't busy-wait in run()
		// waiting for the surface to be created

		thread.setRunning(true);
		thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder)
	{

		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode
		boolean retry = true;
		sm.unregisterListener(thread);
		thread.setRunning(false);
		while(retry)
		{
			try
			{
				thread.join();
				retry = false;
			} catch (InterruptedException e)
			{
			}
		}
	}

	@Override
	public void onDraw(Canvas c)
	{
		synchronized (getHolder())
		{
			if(thread.vMode == UpgradeThread.STATE_PAUSE)
			{
				c.drawBitmap(PAUSED, 0, 0, null);
				Log.v("PlayField", "PAUSE drawn");
			} else
			{
				Paint temp = new Paint();
				temp.setColor(Color.WHITE);
				temp.setAlpha(0);
				c.drawRect(new Rect(0, 0, c.getWidth(), c.getHeight()), temp);
				temp = null;
				Log.v("PlayField", "PAUSE cleared");
			}
		}
	}

	class EnemyCreator extends Thread
	{
		int enemiesCreated;

		public EnemyCreator()
		{
			enemiesCreated = 0;
			Enemies = new ArrayList<Enemy>();
			EnemyProjectiles = new ArrayList<EnemyProjectile>();
			Enemy.setScreenBoundaries(vXmax, vYmax);
		}

		public void run()
		{
			while(enemiesCreated < gEnemyNumber)
			{
				Random rand = new Random();
				Bitmap EnemyBitmap = enemyBitmapL1;
				synchronized (Enemies)
				{
					Enemy e = new Enemy(EnemyBitmap, explosionBitmap, 3, 3000, rand.nextInt(vXmax - EnemyBitmap.getWidth() + 1), -EnemyBitmap.getHeight(), 100);
					Enemies.add(e);
					enemiesCreated++;
				}

				try
				{
					sleep((new Random().nextInt(6000) + 3000));
				} catch (InterruptedException e)
				{
					Log.v("EnemyCreator", "Enemy creator error");
				}

			}
		}
	}

	class UpgradeThread extends Thread implements SensorEventListener, OnTouchListener
	{
		public static final int STATE_PAUSE = 1;
		public static final int STATE_RUNNING = 2;
		public static final int STATE_WIN = 3;
		public static final int STATE_LOSE = 4;

		private int vCanvasWidth = 1;
		private int vCanvasHeight = 1;

		private int vNextLevel;

		private boolean vRun = false;

		private int vMode;

		private SurfaceHolder vSurfaceHolder;

		private Canvas c;

		private Resources res;

		private int vXmax;
		private int vYmax;

		private float frameTime = 0.8f;

		/** Variables for the counter */
		private long vLastTime;
		/*
		 * private int frameSamplesCollected = 0; private int frameSampleTime =
		 * 0; private int fps = 0;
		 */

		private int shipPosX = 0;
		private int shipPosY = 0;

		@SuppressWarnings("static-access")
		public UpgradeThread(SurfaceHolder surfaceHolder, Context context, int xmax, int ymax)
		{
			vSurfaceHolder = surfaceHolder;
			vContext = context;
			res = context.getResources();

			// cache handles to our key sprites & other drawables

			vXmax = xmax;
			vYmax = ymax;

			float scalew = ((float) vXmax / vBackground.getWidth());
			float scaleh = ((float) vYmax / vBackground.getHeight());
			scale = scalew < scaleh ? scalew : scaleh;

			playerBitmap = Bitmap.createScaledBitmap(playerBitmap, (int) (vXmax / 8), (int) (vYmax / 10), true);
			vBackground = Bitmap.createScaledBitmap(vBackground, (int) (vBackground.getWidth() * scale), (int) (vBackground.getHeight() * scale), true);
			enemyBitmapL1 = Bitmap.createScaledBitmap(enemyBitmapL1, (int) (vXmax / 9), (int) (vYmax / 9), true);
			explosionBitmap = Bitmap.createScaledBitmap(explosionBitmap, (int) (explosionBitmap.getWidth() * scale), (int) (explosionBitmap.getHeight() * scale), true);
			playerLaserImage = Bitmap.createScaledBitmap(playerLaserImage, (int) (playerBitmap.getWidth() / 7.5), (int) (playerBitmap.getHeight() / 2.5), true);
			PAUSED = Bitmap.createScaledBitmap(PAUSED, (int) (PAUSED.getWidth() * scale), (int) (PAUSED.getHeight() * scale), true);

			shipPosX = (int) vXmax / 2;
			shipPosY = vYmax - playerBitmap.getHeight() - 1;

			player = new Player(playerBitmap, explosionBitmap, gHealthLevel, shipPosX, shipPosY);

			PlayerHitboxes = player.getHitboxes();

			enemyCreatorThread = new EnemyCreator();
			enemiesDestroyed = 0;
			enemiesMissed = 0;


			sm = (SensorManager) getContext().getSystemService(vContext.SENSOR_SERVICE);
			sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
			setOnTouchListener(this);
			
		}

		@Override
		public void run()
		{
			enemyCreatorThread.start();
			while(vRun)
			{
				if(vMode == STATE_RUNNING)
				{
					c = null;
					try
					{
						c = vSurfaceHolder.lockCanvas();
						if(vCanvasHeight == 1 || vCanvasWidth == 1)
						{
							vCanvasWidth = c.getWidth();
							vCanvasHeight = c.getHeight();
						}
						synchronized (vSurfaceHolder)
						{
							updatePositions(c);
							doDraw(c);
						}
					} finally
					{
						// do this in a finally so that if an exception is
						// thrown
						// during the above, we don't leave the Surface in an
						// inconsistent state
						if(c != null)
						{
							vSurfaceHolder.unlockCanvasAndPost(c);
						}
					}
				}
			}

			Paint temp = new Paint();
			temp.setColor(Color.WHITE);
			temp.setAlpha(0);
			c.drawRect(new Rect(0, 0, c.getWidth(), c.getHeight()), temp);
			temp = null;
			Log.v("PlayField", "PAUSE cleared");
		}

		/**
		 * Draws the ship, enemies, projectiles and background to the provided
		 * Canvas.
		 */
		private void doDraw(Canvas canvas)
		{
			//synchronized (vSurfaceHolder)
			{
				canvas.drawBitmap(vBackground, 0, 0, null);

				if(EnemyProjectiles.size() > 0)
				{
					for(int i = 0; i < EnemyProjectiles.size(); i++)
					{
						EnemyProjectiles.get(i).draw(c);
					}
				}
			}

			//synchronized (PlayerProjectiles)
			{
				if(PlayerProjectiles.size() > 0)
				{
					for(int i = 0; i < PlayerProjectiles.size(); i++)
					{
						PlayerProjectiles.get(i).draw(c);
					}
				}
			}

			//synchronized (Enemies)
			{
				for(int i = 0; i < Enemies.size(); i++)
				{
					if(Enemies.get(i).isOnScreen() == false)
					{
						if(Enemies.get(i).isDestroyed() == false)
							enemiesMissed++;
						Enemies.remove(i);
					}
				}
				if(Enemies.size() > 0)
				{
					for(int i = 0; i < Enemies.size(); i++)
					{
						Enemies.get(i).drawEnemy(canvas);
					}
				}

				if(player.isOnScreen() == true)
					player.drawPlayer(canvas);

				HUDelement.setColor(Color.WHITE);
				HUDelement.setTextSize(20);
				HUDelement.setTextAlign(Align.LEFT);
				HUDelement.setStyle(Style.FILL);
				HUDelement.setAlpha(150);

				c.drawText("Health: " + Integer.toString(player.getActualHealth()), 0, 0, HUDelement);

				HUDelement.setTextAlign(Align.RIGHT);

				c.drawText("Level " + Integer.toString(vSaveSlot.getLevel()), vXmax, 0, HUDelement);

			}

		}

		/**
		 * 
		 * Updates on-screen objects' positions, detects collisions Moves the
		 * game through states
		 */
		private void updatePositions(Canvas c)
		{
			long now = System.currentTimeMillis();

			// Do nothing if vLastTime is in the future.
			// This allows the game-start to delay the start of the physics
			// by 100ms or whatever.
			if(vLastTime > now)
				return;

			Random rand;
			int attack;

			for(Enemy e : Enemies)
			{
				if(System.currentTimeMillis() - e.getLastAttackTime() - e.getAttackDelay() > 0)
				{
					Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.laser_test);
					EnemyLaser el = new EnemyLaser(getResources(), (int) (e.getPosX() + (e.getImageSizeX() / 2) - (image.getWidth() / 2)), (int) (e.getPosY() + e.getImageSizeY() - image.getHeight()));
					e.fire();
					EnemyProjectiles.add(el);
				}
			}

			synchronized (vSurfaceHolder)
			{
				// Manage player's shots' collision
				manageCollisionsForPPandE();

				manageCollisionsForEPandP();

				if(player.isOnScreen() == true)
				{
					player.updatePositions(shipPosX, shipPosY);
				}

				for(int i = 0; i < PlayerProjectiles.size(); i++)
				{
					PlayerProjectiles.get(i).updatePosition();
				}

				for(int i = 0; i < Enemies.size(); i++)
				{
					Enemies.get(i).updatePosition();
				}

				for(int i = 0; i < EnemyProjectiles.size();i++)
				{
					EnemyProjectiles.get(i).updatePosition();
				}
			}

			if(vMode == STATE_RUNNING && enemiesDestroyed == gEnemyNumber && EnemyProjectiles.size() == 0 && Enemies.size() == 0 && player.isOnScreen() == true)
			{
				setState(STATE_WIN);
				advanceGame();
			}

			else if((vMode == STATE_RUNNING && Enemies.size() == 0 && enemiesMissed != 0) || player.isOnScreen() == false)
			{
				setState(STATE_LOSE);
			}

			vLastTime = now;
		}

		/**
		 * The collision detector between Enemies and Player's shots
		 */
		private void manageCollisionsForPPandE()
		{
			for(int i = 0; i < Enemies.size(); i++)
			{
				actualEnemy = Enemies.get(i);
				HitboxesTemp = actualEnemy.getHitboxes();

				for(int k = 0; k < HitboxesTemp.size(); k++)
				{
					temprect = HitboxesTemp.get(k).getRectF();
					synchronized (PlayerProjectiles)
					{
						// Enemy actualEnemy = Enemies.get(i);
						for(int j = 0; j < PlayerProjectiles.size(); j++)
						{
							actualPProjectile = PlayerProjectiles.get(j);
							if(actualPProjectile.getPosY() + actualPProjectile.getImageSizeY() <= 0)
								PlayerProjectiles.remove(j);
							else
							{
								float prX1 = actualPProjectile.getPosX();
								float prX2 = prX1 + actualPProjectile.getImageSizeX();
								float prY1 = actualPProjectile.getPosY();
								float prY2 = prY1 + actualPProjectile.getImageSizeY();
								float enX1 = temprect.left;
								float enX2 = temprect.right;
								float enY1 = temprect.top;
								float enY2 = temprect.bottom;
								if(!(enX1 >= prX2 || enX2 <= prX1 || enY1 >= prY2 || enY2 <= prY1) && actualEnemy.isDestroyed() == false)
								{
									actualEnemy.hitForDamage(actualPProjectile.getDamage());
									if(actualEnemy.isDestroyed())
									{
										vSaveSlot.incCash(actualEnemy.getValue());
										enemiesDestroyed++;
									}
									PlayerProjectiles.remove(j);
									return;
								}
							}
						}
					}
				}
			}
		}

		/**
		 * The collision detector between Player and enemy shots
		 */
		private void manageCollisionsForEPandP()
		{
			HitboxesTemp = player.getHitboxes();
			for(int i = 0; i < EnemyProjectiles.size(); i++)
			{
				synchronized (EnemyProjectiles)
				{
					actualEProjectile = EnemyProjectiles.get(i);

					if(actualEProjectile.getPosY() > vYmax)
						EnemyProjectiles.remove(i);

					else
					{

						for(int j = 0; j < HitboxesTemp.size(); j++)
						{
							temprect = HitboxesTemp.get(j).getRectF();
							float prX1 = actualEProjectile.getPosX();
							float prX2 = prX1 + actualEProjectile.getImageSizeX();
							float prY1 = actualEProjectile.getPosY();
							float prY2 = prY1 + actualEProjectile.getImageSizeY();
							float plX1 = temprect.left;
							float plX2 = temprect.right;
							float plY1 = temprect.top;
							float plY2 = temprect.bottom;

							if(!(plX1 >= prX2 || plX2 <= prX1 || plY1 >= prY2 || plY2 <= prY1) && player.isDestroyed() == false)
							{
								player.hitForDamage(actualEProjectile.getDamage());
								EnemyProjectiles.remove(i);
								return;
							}
						}
					}
				}
			}
		}

		public int getGameState()
		{
			Log.v("PlayField", "Game state sent!");
			return vMode;
		}

		public boolean isRunning()
		{
			return vRun;
		}

		public void setDisplay(Display d)
		{
			vDisplay = d;
		}

		/**
		 * Dump game state to the provided Bundle. Typically called when the
		 * Activity is being suspended.
		 * 
		 * @return Bundle with this view's state
		 */
		public Bundle saveState(Bundle map)
		{
			synchronized (vSurfaceHolder)
			{
				if(map != null)
				{
					// TODO saveState
				}
			}
			return map;
		}

		public void advanceGame()
		{
			vSaveSlot.incLevel();
			SAVE();
		}

		/**
		 * Restores game state from the indicated Bundle. Typically called when
		 * the Activity is being restored after having been previously
		 * destroyed.
		 * 
		 * @param savedState
		 *            Bundle containing the game state
		 */
		public synchronized void restoreState(Bundle savedState)
		{
			synchronized (vSurfaceHolder)
			{
				setState(STATE_PAUSE);

				// TODO restoreState
			}
		}

		public void setState(int mode)
		{
			synchronized (vSurfaceHolder)
			{
				vMode = mode;
				if(vMode == STATE_PAUSE)
					Log.v("STATECHG", "PAUSED");
				else if(vMode == STATE_RUNNING)
					Log.v("STATECHG", "RUNNING");
				else if(vMode == STATE_WIN)
					Log.v("STATECHG", "WON");
				else if(vMode == STATE_LOSE)
					Log.v("STATECHG", "LOST");
				// Log.v("STATECHG", Integer.toString(vMode));
			}
		}

		/**
		 * Starts the game, setting parameters for the current difficulty.
		 */
		public void doStart()
		{
			/*
			 * synchronized (vSurfaceHolder) { gActualLevel =
			 * vSaveSlot.getLevel(); gEnemyNumber = (int) (gActualLevel * 2.5);
			 * gWeaponLevel = vSaveSlot.getWeaponLevel(); }
			 */
			vLastTime = System.currentTimeMillis() + 100;
			setState(STATE_RUNNING);
		}

		/* Callback invoked when the surface dimensions change. */
		public void setSurfaceSize(int width, int height)
		{
			// synchronized to make sure these all change atomically
			synchronized (vSurfaceHolder)
			{
				vCanvasWidth = width;
				vCanvasHeight = height;

				// don't forget to resize the background image
				vBackground = Bitmap.createScaledBitmap(vBackground, width, height, true);
			}
		}

		/**
		 * Resumes from a pause.
		 */
		public void unpause()
		{
			if(vMode == STATE_PAUSE)
			{
				// Move the real time clock up to now
				synchronized (vSurfaceHolder)
				{
					vLastTime = System.currentTimeMillis() + 100;
					sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
					setState(STATE_RUNNING);
					invalidate();
				}
			}
		}

		/**
		 * Pauses the positions' update & animation.
		 */
		public void pause()
		{
			synchronized (vSurfaceHolder)
			{
				sm.unregisterListener(thread);
				if(vMode == STATE_RUNNING)
				{
					setState(STATE_PAUSE);
					invalidate();
				}
			}
		}

		/**
		 * Used to signal the thread whether it should be running or not.
		 * Passing true allows the thread to run; passing false will shut it
		 * down if it's already running. Calling start() after this was most
		 * recently called with false will result in an immediate shutdown.
		 * 
		 * @param b
		 *            true to run, false to shut down
		 */
		public void setRunning(boolean b)
		{
			vRun = b;
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{

		}

		public void onSensorChanged(SensorEvent event)
		{
			if(event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
				return;

			switch (vDisplay.getRotation())
			{
				case Surface.ROTATION_0:
					player.vAccelX = event.values[0] * 3;
					player.vAccelY = -event.values[1] * 5;
					break;
				case Surface.ROTATION_90:
					player.vAccelX = -event.values[1] * 3;
					player.vAccelY = event.values[0] * 5;
					break;
				case Surface.ROTATION_180:
					player.vAccelX = -event.values[0] * 3;
					player.vAccelY = -event.values[1] * 5;
					break;
				case Surface.ROTATION_270:
					player.vAccelX = event.values[1] * 3;
					player.vAccelY = -event.values[0] * 5;
					break;
			}

			float xSpeed = (float) (player.vSpeedX + (player.vAccelX * frameTime));
			float ySpeed = (float) (player.vSpeedY + (player.vAccelY * frameTime));

			// Calc distance traveled in that time
			float xS = ((player.vSpeedX + xSpeed) / 2) * frameTime;
			float yS = ((player.vSpeedY + ySpeed) / 2) * frameTime;

			int[] Border = new int[2];
			Border[0] = vXmax - player.getImageSizeX();
			Border[1] = vYmax - player.getImageSizeY();

			// Add to position negative due to sensor
			// readings being opposite to what we want!
			shipPosX -= xS;
			shipPosY -= yS;

			if(shipPosX > Border[0])
			{
				shipPosX = Border[0];
			} else if(shipPosX < 0)
			{
				shipPosX = 0;
			}
			if(shipPosY > Border[1])
			{
				shipPosY = Border[1];
			} else if(shipPosY < 0)
			{
				shipPosY = 0;
			}
		}

		public boolean onTouch(View v, MotionEvent event)
		{
			if(event.getAction() != MotionEvent.ACTION_UP)
				return true;
			if(vMode == STATE_RUNNING && player.isOnScreen() == true)
			{
				PlayerProjectile pp;

				if(gWeaponLevel < 5)
				{
					pp = new PlayerLaser(playerLaserImage, shipPosX + (int) (player.getImageSizeX() / 2) - (int) (BitmapFactory.decodeResource(getResources(), R.drawable.laser_test).getWidth() / 2), shipPosY, gWeaponLevel);
				} else if(gWeaponLevel < 10)
				{
					// pp = new PlayerLaser(playerPlasmaImage, shipPosX + (int)
					// (player.getImageSizeX() / 2) - (int)
					// (BitmapFactory.decodeResource(getResources(),
					// R.drawable.laser_test).getWidth() / 2),
					// shipPosY,gWeaponLevel);
				}
				// else
				// pp = new PlayerLaser(playerPhotonImage, shipPosX + (int)
				// (player.getImageSizeX() / 2) - (int)
				// (BitmapFactory.decodeResource(getResources(),
				// R.drawable.laser_test).getWidth() / 2),
				// shipPosY,gWeaponLevel);
				pp = new PlayerLaser(playerLaserImage, shipPosX + (int) (player.getImageSizeX() / 2) - (int) (BitmapFactory.decodeResource(getResources(), R.drawable.laser_test).getWidth() / 2), shipPosY, gWeaponLevel);
				PlayerProjectiles.add(pp);
			}
			return true;
		}
	}
}
