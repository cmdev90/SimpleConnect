package org.zapto.p3o.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class SimpleQuery extends AsyncTask<Object, Object, QueryResult> {
	// The query types available on this object
	public final static int EXECUTE_UPDATE = 1;
	public final static int EXECUTE_SELECT = 2;
	public final static int EXECUTE_DELETE = 3;
	public final static int EXECUTE_INSERT = 4;
	public static final int EXECUTE_NONE = 0;

	private SQLiteDatabase database;
	private int queryType;
	private String preparedQuery;
	private ContentValues preparedContent;
	private String tablename;
	private QueryListener queryListener;

	/**
	 * 
	 * @param db
	 *            The SQLiteDatabase to perform the query on.
	 * @param type
	 *            The query type whether it be select, insert, update or delete.
	 */
	protected SimpleQuery(SQLiteDatabase db, String table, int type,
			QueryListener listener) {
		this.database = db;
		this.queryType = type;
		this.tablename = table;
		this.queryListener = listener;
	}

	@Override
	protected QueryResult doInBackground(Object... params) {
		switch (queryType) {
		case SimpleQuery.EXECUTE_DELETE:
			return executeDelete();

		case SimpleQuery.EXECUTE_INSERT:
			return executeInsert();

		case SimpleQuery.EXECUTE_SELECT:
			return executeSelect();

		case SimpleQuery.EXECUTE_UPDATE:
			return executeUpdate();
		}

		return null;
	}

	@Override
	protected void onPostExecute(QueryResult result) {
		if (this.queryListener != null)
			this.queryListener.onResult(result);
	}

	protected void setPreparedQuery(String query) {
		this.preparedQuery = query;
	}

	protected void setPreparedContent(ContentValues content) {
		this.preparedContent = content;
	}

	private QueryResult executeSelect() {
		Cursor c = this.database.rawQuery(this.preparedQuery, null);
		c.moveToFirst();
		return new QueryResult(QueryResult.SUCCESS, "Query successful", c);
	}

	private QueryResult executeInsert() {
		this.database.insert(this.tablename, null, this.preparedContent);
		Log.i("Database", "Insert executed successfully");
		return new QueryResult(QueryResult.SUCCESS, "Query successful");
	}

	private QueryResult executeUpdate() {
		this.database.insert(this.tablename, null, this.preparedContent);
		return new QueryResult(QueryResult.SUCCESS, "Query successful");
	}

	private QueryResult executeDelete() {
		this.database.delete(this.tablename, this.preparedQuery, null);
		Log.i("Database", "Delete executed successfully");
		return new QueryResult(QueryResult.SUCCESS, "Query successful");
	}
}
