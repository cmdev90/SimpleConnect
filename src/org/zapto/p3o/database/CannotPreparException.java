package org.zapto.p3o.database;

@SuppressWarnings("serial")
public class CannotPreparException extends Exception {

	public CannotPreparException() {
		super("The query cannot been prepared because of mis-matching"
				+ " parameters in parameter list.");
	}
}
