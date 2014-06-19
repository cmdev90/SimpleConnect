package org.zapto.p3o.database;

@SuppressWarnings("serial")
public class TableNotFoundException extends Exception {

	public TableNotFoundException() {
		super("Table not found in this database. Please call addTable() to add"
				+ " the requested table.");
	}
}
