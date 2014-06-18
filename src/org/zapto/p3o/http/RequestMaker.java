/**
 * This class is a simple wrapper class used to make use of this
 * package a joy (T_T) <-tears of joy!
 */

package org.zapto.p3o.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RequestMaker {
	private Context context;

	public RequestMaker(Context context) {
		this.context = context;
	}

	public void get(String uri, HttpResponder callback) throws ConnectionErrorException {
		if (!isNetworkAvailable())
			throw new ConnectionErrorException();
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
}
