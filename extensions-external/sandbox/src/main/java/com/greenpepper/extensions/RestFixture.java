package com.greenpepper.extensions;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.io.IOUtils;

import com.greenpepper.util.ExceptionImposter;

public class RestFixture {
	private String hostUrl;
	private int responseStatus;
	private String responseBody;
	private Header[] responseHeaders = null;
	private Map<String, String> requestHeaders = new HashMap<String, String>();
	
	private String requestBody;
	
	public RestFixture(String host) {
		this.hostUrl = host;
	}

	public void get(String resourceUrl) {
		executeMethod(new GetMethod(buildUrl(resourceUrl)));
	}
	
	public void head(String resourceUrl) {
		executeMethod(new HeadMethod(buildUrl(resourceUrl)));
	}
	
	@SuppressWarnings("deprecation")
	public void post(String resourceUrl) {
		PostMethod post = new PostMethod(buildUrl(resourceUrl));
		post.setRequestBody(requestBody);
		executeMethod(post);
	}

	public void delete(String resourceUrl) {
		executeMethod(new DeleteMethod(buildUrl(resourceUrl)));
	}
	
	@SuppressWarnings("deprecation")
	public void put(String resourceUrl) {
		PutMethod put = new PutMethod(buildUrl(resourceUrl));
		put.setRequestBody(requestBody);
		executeMethod(put);
	}

	private String buildUrl(String resourceUrl) {
		return hostUrl + resourceUrl;
	}
	
	private void executeMethod(HttpMethod httpRequest) {
		HttpClient client = new HttpClient();
		
		addHeadersToRequest(httpRequest);
		
		try {
			client.executeMethod(httpRequest);
			responseStatus = httpRequest.getStatusCode();
			responseHeaders = httpRequest.getResponseHeaders();
			responseBody = responseBodyAsString(httpRequest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			requestHeaders.clear();
			httpRequest.releaseConnection();
		}
	}

	private void addHeadersToRequest(HttpMethod method) {
		for (String headerName : requestHeaders.keySet()) {
			method.addRequestHeader(headerName, requestHeaders.get(headerName));
		}
	}
	
	public String responseHeader(String headerName) {
		if (responseHeaders != null) {
			for (Header header : responseHeaders) {
				if (header.getName().equalsIgnoreCase(headerName)) {
					return header.getValue();
				}
			}
		}
		return null;
	}
	
	public int getResponseStatus() {
		return responseStatus;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	
	public void setRequestHeaderAs(String name, String value) {
		requestHeaders.put(name, value);
	}

	private String responseBodyAsString(HttpMethod httpRequest) {

		InputStream is = null;

		try {
			is = httpRequest.getResponseBodyAsStream();
			return is == null ? "" : IOUtils.toString(is);
		}
		catch (IOException ex) {
			throw ExceptionImposter.imposterize(ex);
		}
		finally {
			IOUtils.closeQuietly(is);
		}
	}
}
