package hu.stewe.UpgradeLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UpgradeDbLoader {
	
	private Context ctx;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase mDb;
	
	public UpgradeDbLoader(Context ctx) {
		this.ctx = ctx;
	}
	
	public void open() throws SQLException{
		// DatabaseHelper objektum
		dbHelper = new DatabaseHelper(
				ctx, DbConstants.DATABASE_NAME);
		// adatbázis objektum
		mDb = dbHelper.getWritableDatabase();
		// ha nincs még séma, akkor létrehozzuk
		dbHelper.onCreate(mDb);
	}
	
	public void close(){
		dbHelper.close();
	}


	// INSERT 
	public long createSaveSlot(SaveSlot s){
		ContentValues values = new ContentValues();
		int temp;
		values.put(DbConstants.SaveSlot.KEY_ROWID, s.getID());
		values.put(DbConstants.SaveSlot.KEY_LEVEL, s.getLevel());
		values.put(DbConstants.SaveSlot.KEY_CASH, s.getCash());
		values.put(DbConstants.SaveSlot.KEY_GRAPHICS, s.getGraphicsLevel());
		values.put(DbConstants.SaveSlot.KEY_WEAPON, s.getWeaponLevel());
		values.put(DbConstants.SaveSlot.KEY_HEALTH, s.getHealthLevel());
		temp = s.getSoundPurchased() ? 1:0;
		values.put(DbConstants.SaveSlot.KEY_SOUND_P, temp);
		temp = s.getMusicPurchased() ? 1:0;
		values.put(DbConstants.SaveSlot.KEY_MUSIC_P, temp);
		temp = s.getSoundState() ? 1:0;
		values.put(DbConstants.SaveSlot.KEY_SOUNDON, temp);
		temp = s.getMusicState() ? 1:0;
		values.put(DbConstants.SaveSlot.KEY_MUSICON, temp);
		
		return mDb.insert(DbConstants.SaveSlot.DATABASE_TABLE, null, values);
	}

	// DELETE
	public boolean deleteSaveSlot(long rowId){
		return mDb.delete(
				DbConstants.SaveSlot.DATABASE_TABLE, 
				DbConstants.SaveSlot.KEY_ROWID + "=" + rowId, 
				null) > 0;
	}

	// UPDATE
	public boolean updateProduct(long rowId, SaveSlot newSaveSlot){
		ContentValues values = new ContentValues();
		values.put(DbConstants.SaveSlot.KEY_LEVEL, newSaveSlot.getLevel());
		values.put(DbConstants.SaveSlot.KEY_CASH, newSaveSlot.getCash());
		values.put(DbConstants.SaveSlot.KEY_GRAPHICS, newSaveSlot.getGraphicsLevel());
		values.put(DbConstants.SaveSlot.KEY_WEAPON, newSaveSlot.getWeaponLevel());
		values.put(DbConstants.SaveSlot.KEY_HEALTH, newSaveSlot.getHealthLevel());
		values.put(DbConstants.SaveSlot.KEY_SOUND_P, newSaveSlot.getSoundPurchased() ? 1:0);
		values.put(DbConstants.SaveSlot.KEY_MUSIC_P, newSaveSlot.getMusicPurchased() ? 1:0);
		values.put(DbConstants.SaveSlot.KEY_SOUNDON, newSaveSlot.getSoundState() ? 1:0);
		values.put(DbConstants.SaveSlot.KEY_MUSICON, newSaveSlot.getMusicState() ? 1:0);
		return mDb.update(
				DbConstants.SaveSlot.DATABASE_TABLE, 
				values, 
				DbConstants.SaveSlot.KEY_ROWID + "=" + rowId , 
				null) > 0;
	}
	
	// minden Todo lekérése
	public Cursor fetchAll(){
		// cursor minden rekordra (where = null)
		return mDb.query(
				DbConstants.SaveSlot.DATABASE_TABLE, 
				new String[]{ 
						DbConstants.SaveSlot.KEY_ROWID,
						DbConstants.SaveSlot.KEY_LEVEL,
						DbConstants.SaveSlot.KEY_CASH,
						DbConstants.SaveSlot.KEY_GRAPHICS,
						DbConstants.SaveSlot.KEY_WEAPON,
						DbConstants.SaveSlot.KEY_HEALTH,
						DbConstants.SaveSlot.KEY_SOUND_P,
						DbConstants.SaveSlot.KEY_MUSIC_P,
						DbConstants.SaveSlot.KEY_SOUNDON,
						DbConstants.SaveSlot.KEY_MUSICON
						
				}, null, null, null, null, DbConstants.SaveSlot.KEY_ROWID);
	}

	// egy Todo lekérése
	public SaveSlot fetchSaveSlot(long rowId){
		// a Todo-ra mutato cursor
		Cursor c = mDb.query(
				DbConstants.SaveSlot.DATABASE_TABLE, 
				new String[]{ 
						DbConstants.SaveSlot.KEY_ROWID,
						DbConstants.SaveSlot.KEY_LEVEL,
						DbConstants.SaveSlot.KEY_CASH,
						DbConstants.SaveSlot.KEY_GRAPHICS,
						DbConstants.SaveSlot.KEY_WEAPON,
						DbConstants.SaveSlot.KEY_HEALTH,
						DbConstants.SaveSlot.KEY_SOUND_P,
						DbConstants.SaveSlot.KEY_MUSIC_P,
						DbConstants.SaveSlot.KEY_SOUNDON,
						DbConstants.SaveSlot.KEY_MUSICON
				}, DbConstants.SaveSlot.KEY_ROWID + "=" + rowId, 
				null, null, null, DbConstants.SaveSlot.KEY_ROWID);
		// ha van rekord amire a Cursor mutat
		if(c.moveToFirst())
			return getSaveSlotByCursor(c);
		// egyebkent null-al terunk vissza
		return null;
	}

	public static SaveSlot getSaveSlotByCursor(Cursor c){
		return new SaveSlot(
				c.getInt(c.getColumnIndex(DbConstants.SaveSlot.KEY_ROWID)),
				c.getInt(c.getColumnIndex(DbConstants.SaveSlot.KEY_LEVEL)),
				c.getInt(c.getColumnIndex(DbConstants.SaveSlot.KEY_CASH)),
				c.getInt(c.getColumnIndex(DbConstants.SaveSlot.KEY_GRAPHICS)),
				c.getInt(c.getColumnIndex(DbConstants.SaveSlot.KEY_WEAPON)),
				c.getInt(c.getColumnIndex(DbConstants.SaveSlot.KEY_HEALTH)),
				(c.getInt(c.getColumnIndex(DbConstants.SaveSlot.KEY_SOUND_P))) == 1 ? true : false,
				(c.getInt(c.getColumnIndex(DbConstants.SaveSlot.KEY_MUSIC_P))) == 1 ? true : false,
			    (c.getInt(c.getColumnIndex(DbConstants.SaveSlot.KEY_SOUNDON))) == 1 ? true : false,
			    (c.getInt(c.getColumnIndex(DbConstants.SaveSlot.KEY_MUSICON))) == 1 ? true : false
				);
	}
}
