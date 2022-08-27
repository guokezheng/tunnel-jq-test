package com.tunnel.platform.business.board.device;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


/**
 * 
 * @Description
 * 
 * @since 1.4
 * @version 0.11
 */
public class HttpClientManager {

	/**
	 * 懒汉模式实例化。
	 */
	private static HttpClientManager instance;

	public static HttpClientManager getInstance() {
		if (instance == null)
			instance = new HttpClientManager();
		return instance;
	}

	private HttpClientManager() {
	}

	/**
	 * 
	 * @Description: 获取URL的返回串。
	 * @param url
	 * @return
	 */
	public String getResponse(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url);
			String responseBody = httpclient.execute(httpget, new ResponseHandler<String>() {
				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			});
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
}
