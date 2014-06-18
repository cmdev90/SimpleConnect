package org.zapto.p3o.database;


@SuppressWarnings("serial")
public class NotPreparedException extends Exception {

	public NotPreparedException() {
		super("The query has not been prepared yet. Must call"
				+ " prepareInsert, prepareSelect, prepareUpdate or prepareDelete "
				+ "before invoking execute.");
	}
}
