package org.zapto.p3o.http;

import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;

public class Request extends AsyncTask<String, Integer, ServerResponse> {

	// Lets declare some static variables for the sake of being OOP.
	public static final int GET = 0;
	public static final int PUT = 1;
	public static final int POST = 2;
	public static final int DELETE = 3;

	private HttpResponder handler;
	private int method;
	private String uri, payload;

	protected Request(int method, String uri, HttpResponder callback) {
		this.handler = callback;
		this.method = method;
		this.uri = uri;
	}

	@Override
	protected ServerResponse doInBackground(String... params) {

		switch (this.method) {
		case Request.GET:
			return doGetRequest(this.uri);
		case Request.PUT:
			return doPutRequest(this.uri, this.getPayload());
		case Request.POST:
			return doPostRequest(this.uri, this.getPayload());
		case Request.DELETE:
			return doDeleteRequest(this.uri);
		}

		return new ServerResponse(ServerResponse.THREAD_ERROR,
				"So there was this random ass error "
						+ "in the execution process that "
						+ "caused this error... wtf.");
	}

	private ServerResponse doGetRequest(String uri) {
		try {
			URI link = new URI(uri);
			HttpGet request = new HttpGet(link.toString());
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);

			return new ServerResponse(response);

		} catch (Exception e) {
			return new ServerResponse(ServerResponse.CONNECTION_ERROR,
					e.getMessage());
		}

	}

	private ServerResponse doDeleteRequest(String uri) {
		try {
			URL url = new URL(uri);
			HttpDelete httpPost = new HttpDelete(url.toString());
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpPost);

			return new ServerResponse(response);

		} catch (Exception e) {
			e.printStackTrace();
			return new ServerResponse(ServerResponse.CONNECTION_ERROR,
					e.getMessage());
		}
	}

	private ServerResponse doPostRequest(String uri, String payload) {
		try {

			StringEntity entity = new StringEntity(payload);
			entity.setContentType("application/json;charset=UTF-8");
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));

			URL url = new URL(uri);
			HttpPost httpPost = new HttpPost(url.toString());
			httpPost.setEntity(entity);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpPost);

			return new ServerResponse(response);

		} catch (Exception e) {
			e.printStackTrace();
			return new ServerResponse(ServerResponse.CONNECTION_ERROR,
					e.getMessage());
		}
	}

	private ServerResponse doPutRequest(String uri, String payload) {
		try {
			StringEntity entity = new StringEntity(payload);
			entity.setContentType("application/json;charset=UTF-8");
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));

			URL url = new URL(uri);
			HttpPut httpPut = new HttpPut(url.toString());
			httpPut.setEntity(entity);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpPut);

			return new ServerResponse(response);

		} catch (Exception e) {
			e.printStackTrace();
			return new ServerResponse(ServerResponse.CONNECTION_ERROR,
					e.getMessage());
		}
	}

	@Override
	protected void onPostExecute(ServerResponse result) {
		// if (result instanceof ServerResponse)
		this.handler.onHttpResponse((ServerResponse) result);
		// else if (result instanceof AndroidResponse)
		// this.handler.onAndroidResponse((AndroidResponse) result);
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getPayload() {
		return payload != null ? payload : "";
	}
}
