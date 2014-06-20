/**
 * This class is a simple wrapper class used to make use of this
 * package a joy (T_T) <-tears of joy!
 */

package org.zapto.p3o.http;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class RequestMaker {
	private Context context;

	public RequestMaker(Context context) {
		this.context = context;
	}

	public void get(String uri, HttpResponder callback) {
		// If no network is avaiable prompt the user to connect to one.
		if (!isNetworkAvailable())
			createNetErrorDialog();
		
		// First thing we need to do is create a new AsyncTask to handle the
		// call.
		Request httpGetRequest = new Request(Request.GET, uri, callback);
		httpGetRequest.execute(uri);
	}

	public void put(String uri, String jsonData, HttpResponder callback) throws ConnectionErrorException {
		if (!isNetworkAvailable())
			throw new ConnectionErrorException();

		Request httpPutRequest = new Request(Request.PUT, uri, callback);
		httpPutRequest.setPayload(jsonData);
		httpPutRequest.execute(uri);
	}

	public void post(String uri, String jsonData, HttpResponder callback) throws ConnectionErrorException {
		if (!isNetworkAvailable())
			throw new ConnectionErrorException();

		Request httpPostRequest = new Request(Request.POST, uri, callback);
		httpPostRequest.setPayload(jsonData);
		httpPostRequest.execute(uri);
	}

	public void delete(String uri, HttpResponder callback) throws ConnectionErrorException {
		if (!isNetworkAvailable())
			throw new ConnectionErrorException();

		Request httpDeleteRequest = new Request(Request.DELETE, uri, callback);
		httpDeleteRequest.execute(uri);
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();

		// if no network is available networkInfo will be null
		// otherwise check if we are connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	
	protected void createNetErrorDialog() {

	    AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
	    builder.setMessage("You need a network connection to use this application. Please turn on mobile network or Wi-Fi in Settings.")
	        .setTitle("Unable to connect")
	        .setCancelable(false)
	        .setPositiveButton("Settings",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
	                context.startActivity(i);
	            }
	        }
	    )
	    .setNegativeButton("Cancel",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	dialog.dismiss();
	            }
	        }
	    );
	    
	    AlertDialog alert = builder.create();
	    alert.show();
	}
}
