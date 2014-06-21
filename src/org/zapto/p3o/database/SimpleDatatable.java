package org.zapto.p3o.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.ContentValues;

public class SimpleDatatable {

	public final static String TEXT = "TEXT";
	public final static String NUMERIC = "NUMERIC";
	public final static String INTEGER = "INTEGER";
	public final static String REAL = "REAL";
	public final static String NONE = "NONE";

	private int executeStatus;

	private String tablename;

	private HashSet<String> primaryKeys = new HashSet<String>();
	private HashSet<String> uniqueKeys = new HashSet<String>();
	private HashSet<String> autoIncrement = new HashSet<String>();

	private HashMap<String, String> columns;

	private String preparedQuery = null;
	private ContentValues preparedContent = null;

	private DatabaseHelper database;

	protected SimpleDatatable(String tablename) {
		this.tablename = tablename;
		this.columns = new HashMap<String, String>();
	}

	public SimpleDatatable(String tablename,Class<?> mClass) {
		this.tablename = tablename;
		
		
		
	}

	public void addColumn(String colname, String type) {
		this.columns.put(colname, type);
	}

	public HashMap<String, String> getColumns() {
		return this.columns;
	}

	public void setPrimaryKey(String column) {
		this.primaryKeys.add(column);
	}

	public void setAutoIncrement(String column) {
		this.autoIncrement.add(column);
	}

	public void setColumnUnique(String column) {
		this.uniqueKeys.add(column);
	}

	protected void setSQLiteDatabase(DatabaseHelper database) {
		this.database = database;
	}

	protected String createTable() throws InvalidTableException {
		String createStatement = "CREATE TABLE " + this.tablename;
		Set<String> keys = this.columns.keySet();

		if (keys.isEmpty())
			throw new InvalidTableException();

		createStatement += " (";

		Iterator<String> keyIterator = keys.iterator();

		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			createStatement += key + " " + this.columns.get(key);

			// Add the following field modifiers.
			if (primaryKeys.contains(key))
				createStatement += " PRIMARY KEY";
			if (autoIncrement.contains(key))
				createStatement += " AUTO INCREMENT";

			// if there is another field add the separator ",".
			if (keyIterator.hasNext())
				createStatement += ", ";
		}
		createStatement += ")"; // close the statement

		return createStatement + ";"; // always terminate the SQL.
	}

	protected String dropTable() {
		return "DROP TABLE IF EXISTS " + this.tablename;
	}

	public SimpleDatatable prepareSelect() throws CannotPreparException {
		return this.prepareSelect(null, null);
	}

	public SimpleDatatable prepareSelect(String condition)
			throws CannotPreparException {
		return this.prepareSelect(null, condition);
	}

	public SimpleDatatable prepareSelect(String[] cols)
			throws CannotPreparException {
		return this.prepareSelect(cols, null);
	}

	public SimpleDatatable prepareSelect(String[] cols, String condition)
			throws CannotPreparException {

		// Start the select statement.
		this.preparedQuery = "SELECT ";

		// If there are user specified columns then add them to the query.
		if (cols != null && cols.length > 0) {
			for (int i = 0; i < cols.length; i++) {
				this.preparedQuery += cols[i];
				if (i < cols.length - 1)
					this.preparedQuery += ", ";
			}
		} else {
			// otherwise select all columns.
			this.preparedQuery += "*";
		}

		// Add the table select part of the clause
		this.preparedQuery += " FROM " + this.tablename;

		// If there is a condition then add the Where clause.
		if (condition != null && !condition.equalsIgnoreCase("")) {
			this.preparedQuery += " WHERE " + condition;
		}

		// Set the table object ready to execute a select.
		executeStatus = SimpleQuery.EXECUTE_SELECT;
		return this;
	}

	public SimpleDatatable prepareInsert(String[] cols, String[] values)
			throws CannotPreparException {
		preparedContent = new ContentValues();

		if (cols.length == values.length) {
			for (int i = 0; i < cols.length; i++) {
				preparedContent.put(cols[i], values[i]);
			}
		} else {
			throw new CannotPreparException();
		}

		// set the execute status to insert.
		executeStatus = SimpleQuery.EXECUTE_INSERT;
		return this;
	}

	public SimpleDatatable prepareUpdate(String[] cols, String[] values)
			throws CannotPreparException {
		preparedContent = new ContentValues();

		if (cols.length == values.length) {
			for (int i = 0; i < cols.length; i++) {
				preparedContent.put(cols[i], values[i]);
			}
		} else {
			throw new CannotPreparException();
		}

		// set the execute status to insert.
		executeStatus = SimpleQuery.EXECUTE_UPDATE;
		return this;
	}

	public SimpleDatatable prepareDelete(String whereClause) {
		this.preparedQuery = whereClause;
		this.executeStatus = SimpleQuery.EXECUTE_DELETE;
		return this;
	}

	public SimpleDatatable prepareDelete() {
		return this.prepareDelete(null);
	}

	public void execute(QueryListener listener) throws NotPreparedException,
			NoDatabaseException {

		// Check to see if we have a database helper object that we can use to
		// obtain a database reader/writer. If not we throw and exception that
		// must be handled by the programmer.
		if (this.database == null)
			throw new NoDatabaseException();

		SimpleQuery query = null;

		// For each case create a new Simple query tasks object and pass the
		// necessary state information such as table name, database accessor,
		// and execution state to run the query in. We must also set the
		// queryContent/queryStatement depending on the type of transaction.
		switch (this.executeStatus) {
		case SimpleQuery.EXECUTE_INSERT:
			if (preparedContent != null) {
				query = new SimpleQuery(this.database.getWritableDatabase(),
						this.tablename, this.executeStatus, listener);
				query.setPreparedContent(preparedContent);
				preparedContent = null;
			} else
				throw new NotPreparedException();
			break;
		case SimpleQuery.EXECUTE_UPDATE:
			if (preparedContent != null) {
				query = new SimpleQuery(this.database.getWritableDatabase(),
						this.tablename, this.executeStatus, listener);
				query.setPreparedContent(preparedContent);
				preparedContent = null;
			} else
				throw new NotPreparedException();
			break;
		case SimpleQuery.EXECUTE_SELECT:
			if (preparedQuery != null) {
				query = new SimpleQuery(this.database.getReadableDatabase(),
						this.tablename, this.executeStatus, listener);
				query.setPreparedQuery(this.preparedQuery);
				this.preparedQuery = null;
			} else
				throw new NotPreparedException();
			break;
		case SimpleQuery.EXECUTE_DELETE:
			if (preparedQuery != null) {
				query = new SimpleQuery(this.database.getReadableDatabase(),
						this.tablename, this.executeStatus, listener);
				query.setPreparedQuery(this.preparedQuery);
				this.preparedQuery = null;
			} else
				throw new NotPreparedException();
			break;
		}

		if (query == null)
			throw new NotPreparedException();

		// set the execute status of the table to none ensuring that previous
		// preparations are overwritten
		this.executeStatus = SimpleQuery.EXECUTE_NONE;
		query.execute();
	}

	public void clear() {
		this.preparedQuery = null;
		this.preparedContent = null;
	}

}
