package org.zapto.p3o.database;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class SimpleDatabase implements DatabaseHelper.DatabaseListener {

	private HashMap<String, SimpleDatatable> tables = new HashMap<String, SimpleDatatable>();
	private Context context;
	private String databasename;
	private int version;

	private DatabaseHelper mdbHelper;

	public SimpleDatabase(Context context, String databasename, int version) {
		this.setContext(context);
		this.setDatabasename(databasename);
		this.setVersion(version);

		this.mdbHelper = new DatabaseHelper(context, databasename, version,
				this);
		
		// call the defineDatabase method that will ready this object.
		defineDatabase();
	}
	
	/**
	 * Override this method, defining the classes and tables used within this database.
	 */
	public abstract void defineDatabase(); 

	public SimpleDatatable addTable(String tablename) {
		String name = tablename.toLowerCase(Locale.getDefault());
		SimpleDatatable table = new SimpleDatatable(name);
		this.tables.put(name, table);
		table.setSQLiteDatabase(mdbHelper);
		return table;
	}
	
	public SimpleDatatable addTable(Class<?> mClass){
		String name = mClass.getSimpleName();
		SimpleDatatable table = new SimpleDatatable(name, mClass);
		this.tables.put(name, table);
		table.setSQLiteDatabase(mdbHelper);
		return table;
	}

	public SimpleDatatable getTable(String tablename) throws TableNotFoundException{
		String name = tablename.toLowerCase(Locale.getDefault());
		SimpleDatatable t = tables.get(name);
		if (t != null)
			return t;
		
		throw new TableNotFoundException();
	}
	
	public void removeTable(String tablename) {
		this.tables.remove(tablename);
	}

	public int getVersion() {
		return version;
	}

	private void setVersion(int version) {
		this.version = version;
	}

	public String getDatabasename() {
		return databasename;
	}

	private void setDatabasename(String databasename) {
		this.databasename = databasename;
	}

	public Context getContext() {
		return context;
	}

	private void setContext(Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Set<String> keys = tables.keySet();
		
		Iterator<String> i = keys.iterator();
		while (i.hasNext()){
			SimpleDatatable t = tables.get(i.next());
			try {
				db.execSQL(t.createTable());
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InvalidTableException e) {
				e.printStackTrace();
			}			
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Set<String> keys = tables.keySet();
		
		Iterator<String> i = keys.iterator();
		while (i.hasNext()){
			SimpleDatatable t = tables.get(i.next());
			try {
				db.execSQL(t.dropTable());
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		onCreate(db);
	}

}
