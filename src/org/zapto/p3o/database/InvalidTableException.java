package org.zapto.p3o.database;

@SuppressWarnings("serial")
public class InvalidTableException extends Exception {

	public InvalidTableException() {
		super("The table is in an invalid state. To fix call"
				+ " addColumn on this table. No columns found on table.");
	}
}
