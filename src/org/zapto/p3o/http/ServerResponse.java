package org.zapto.p3o.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class ServerResponse implements Respondable {
	
	public final static int CONNECTION_ERROR = 0;
	public static final int THREAD_ERROR = 1;
	
	private String contentType;
	private String encoding;
	private Date date;
	private long length;
	private String content;
	private int status;
	private String message;
	private String protocol;

	protected ServerResponse(HttpResponse httpResponse) {
		// Get HTTP header details.
		this.setStatus(httpResponse.getStatusLine().getStatusCode());
		this.setMessage(httpResponse.getStatusLine().getReasonPhrase());
		this.setProtocol(httpResponse.getStatusLine().getProtocolVersion()
				.getProtocol());

		// Use the HttpEntity class to obtain the contents of the
		// httpResponse.
		HttpEntity httpEntity = httpResponse.getEntity();

		this.setContentType(httpEntity.getContentType());
		this.setEncoding(httpEntity.getContentEncoding());
		this.setDate(new Date());
		this.setLength(httpEntity.getContentLength());
		this.setContent(httpEntity);
	}

	public ServerResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public String getContent() {
		return content;
	}

	public void setContent(HttpEntity entity) {
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		String l = "";

		try {
			in = new BufferedReader(new InputStreamReader(entity.getContent()));

			while ((l = in.readLine()) != null) {
				sb.append(l + System.getProperty("line.separator"));
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.content = sb.toString();
		}

	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(Header header) {
		this.encoding = header != null ? header.getName() : "";
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(Header header) {
		this.contentType = header != null ? header.getName() : "";
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
