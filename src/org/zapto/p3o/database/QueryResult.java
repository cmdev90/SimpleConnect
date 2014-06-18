package org.zapto.p3o.database;

import android.database.Cursor;

public class QueryResult {
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	public static final int NONE = 2;
	
	private int status;
	private String message;
	private Cursor cursor;
	
	public QueryResult(){
		this.status = QueryResult.NONE;
		this.message = "Object not init";
	}
	
	public QueryResult(int status, String message, Cursor cursor){
		this.status = status;
		this.message = message;
		this.cursor = cursor;
	}

	public QueryResult(int status, String message){
		this.status = status;
		this.message = message;
		this.cursor = null;
	}
	
	public Cursor getCursor() {
		return cursor;
	}

	protected void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	public String getMessage() {
		return message;
	}

	protected void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	protected void setStatus(int status) {
		this.status = status;
	}
}
