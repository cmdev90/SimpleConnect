package org.zapto.p3o.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SimpleDatabase implements DatabaseHelper.DatabaseListener{

	private List<SimpleDatatable> tables = new ArrayList<SimpleDatatable>();
	private Context context;
	private String databasename;
	private int version;

	private DatabaseHelper mdbHelper;

	public SimpleDatabase(Context context, String databasename, int version) {
		this.setContext(context);
		this.setDatabasename(databasename);
		this.setVersion(version);

		this.mdbHelper = new DatabaseHelper(context, databasename, version, this);
	}

	public boolean addTable(SimpleDatatable table) {
		if (this.tables.add(table)) {
			table.setSQLiteDatabase(mdbHelper);
			return true;
		}
		return false;
	}

	public boolean removeTable(SimpleDatatable table){
		if (this.tables.remove(table)) {
			return true;
		}
		return false;
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
		for (SimpleDatatable table : this.tables){
			try {
				db.execSQL(table.createTable());
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InvalidTableException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (SimpleDatatable table : this.tables){
			try {
				db.execSQL(table.dropTable());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		onCreate(db);
	}

}
