package org.zapto.p3o;

import org.zapto.p3o.database.SimpleDatabase;
import org.zapto.p3o.database.SimpleDatatable;

import android.content.Context;

public class TestDatabase extends SimpleDatabase {
	
	public static String DATABASE = "simple_db";
	public static int VERSION = 2;
	
	public TestDatabase(Context context) {
		super(context, DATABASE, VERSION);
	}

	@Override
	public void onCreate() {
		// Create a sample table and add a column to it.
		this.addTable("sample_table").addColumn("id", SimpleDatatable.INTEGER);
	}

}
