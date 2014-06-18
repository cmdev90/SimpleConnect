package org.zapto.p3o.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public interface DatabaseListener {
		public void onCreate(SQLiteDatabase db);

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}
	
	private DatabaseHelper.DatabaseListener dListener = null;

	protected DatabaseHelper(Context context, String databasename, int version) {
		super(context, databasename, null, version);
	}

	protected DatabaseHelper(Context context, String databasename, int version,
			DatabaseHelper.DatabaseListener listener) {
		super(context, databasename, null, version);
		
		this.dListener = listener;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (this.dListener != null)
			this.dListener.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (this.dListener != null)
			this.dListener.onUpgrade(db, oldVersion, newVersion);
	}
}