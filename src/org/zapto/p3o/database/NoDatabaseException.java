package org.zapto.p3o.database;

@SuppressWarnings("serial")
public class NoDatabaseException extends Exception {

	public NoDatabaseException() {
		super("The table is not associated with any database. To fix this problem"
				+ " create a new SimpleDatabase and add this table to it.");
	}
}
