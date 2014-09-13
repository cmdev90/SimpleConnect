package org.zapto.p3o;

import org.zapto.p3o.database.SimpleDatabase;
import org.zapto.p3o.database.SimpleDatatable;

import android.content.Context;

public class GPSDatabase extends SimpleDatabase  {

	public static String DATABASE = "gps_db";
	public static int VERSION = 1;
	
	public GPSDatabase(Context context, String databasename, int version) {
		super(context, databasename, version);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		this.addTable("sample_table").addColumn("id", SimpleDatatable.INTEGER);
	}

}
