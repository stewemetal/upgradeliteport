package hu.stewe.UpgradeLiteDB;

public class DbConstants {

	// fajlnev, amiben az adatbazis lesz
	public static final String DATABASE_NAME = "save.db";
	// verzioszam
	public static final int DATABASE_VERSION = 1;
	// osszes belso osztaly DATABASE_CREATE szkriptje osszefuzve
	public static String DATABASE_CREATE_ALL = SaveSlot.DATABASE_CREATE;
	// osszes belso osztaly DATABASE_DROP szkriptje osszefuzve
	public static String DATABASE_DROP_ALL = SaveSlot.DATABASE_DROP;
	
	/* SaveSlot osztaly DB konstansai */
	public static class SaveSlot{
		// tabla neve
		public static final String DATABASE_TABLE = "upgradesave";
		// oszlopnevek
		public static final String KEY_ROWID = "_id";
		public static final String KEY_LEVEL = "level";
		public static final String KEY_CASH = "cash";
		public static final String KEY_GRAPHICS = "graphics";
		public static final String KEY_WEAPON = "weapon";
		public static final String KEY_HEALTH = "health";
		public static final String KEY_SOUND_P = "sound";
		public static final String KEY_MUSIC_P = "music";
		public static final String KEY_SOUNDON = "soundstate";
		public static final String KEY_MUSICON = "musicstate";
		// sema letrehozo szkript
		public static final String DATABASE_CREATE =
		    "create table if not exists "+DATABASE_TABLE+" ( " 
		    + KEY_ROWID +" integer primary key autoincrement, "
		    + KEY_LEVEL + " integer, " 
		    + KEY_CASH + " integer, " 
		    + KEY_GRAPHICS +" integer, " 
		    + KEY_WEAPON +" integer, " 
		    + KEY_HEALTH +" integer, "
		    + KEY_SOUND_P +" integer, "
		    + KEY_MUSIC_P +" integer, "
		    + KEY_SOUNDON +" integer, "
		    + KEY_MUSICON +" integer"
		    + "); ";
		// sema torlo szkript
		public static final String DATABASE_DROP = 
			"drop table if exists " + DATABASE_TABLE + "; ";
	}
}
