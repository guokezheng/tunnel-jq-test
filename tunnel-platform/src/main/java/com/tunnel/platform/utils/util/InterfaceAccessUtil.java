package com.tunnel.platform.utils.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;


/**
 * describe: 接口访问工具类
 *
 * @author zs
 * @date 2020/11/12
 */
@RestController
public class InterfaceAccessUtil {

	/**
	 * GET方式请求---(手动在url后面加上参数)
	 * 
	 * @param params
	 */
	public static String doGet(String path, String token) {
		String result = "";
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 创建Get请求
		HttpGet httpGet = new HttpGet(path);
		if (token != null && !"".equals(token)) {
			httpGet.setHeader("Authorization", "Bearer " + token);
		}
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 配置信息
			RequestConfig requestConfig = RequestConfig.custom()
					// 设置连接超时时间(单位毫秒)
					.setConnectTimeout(5000)
					// 设置请求超时时间(单位毫秒)
					.setConnectionRequestTimeout(5000)
					// socket读写超时时间(单位毫秒)
					.setSocketTimeout(5000)
					// 设置是否允许重定向(默认为true)
					.setRedirectsEnabled(true).build();
			// 将上面的配置信息 运用到这个Get请求里
			httpGet.setConfig(requestConfig);
			// 由客户端执行(发送)Get请求
			response = httpClient.execute(httpGet);
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				InputStream inputStream = responseEntity.getContent();
				result = getInputStream2String(inputStream);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			result = "-1";
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * GET方式请求---(手动在url后面加上参数)
	 */
	public static String doGetParam(String path, String params, String token) {
		String result = "";
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 创建Get请求
		HttpGet httpGet = new HttpGet(path + "?" + params);
		httpGet.setHeader("token", token);
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 配置信息
			RequestConfig requestConfig = RequestConfig.custom()
					// 设置连接超时时间(单位毫秒)
					.setConnectTimeout(5000)
					// 设置请求超时时间(单位毫秒)
					.setConnectionRequestTimeout(5000)
					// socket读写超时时间(单位毫秒)
					.setSocketTimeout(5000)
					// 设置是否允许重定向(默认为true)
					.setRedirectsEnabled(true).build();

			// 将上面的配置信息 运用到这个Get请求里
			httpGet.setConfig(requestConfig);

			// 由客户端执行(发送)Get请求
			response = httpClient.execute(httpGet);

			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				InputStream inputStream = responseEntity.getContent();
				result = getInputStream2String(inputStream);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * GET方式请求---(手动在url后面加上参数)
	 */
	public static String doGetParams(String path, String params) {
		String result = "";
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 创建Get请求
		HttpGet httpGet = new HttpGet(path + "?" + params);
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 配置信息
			RequestConfig requestConfig = RequestConfig.custom()
					// 设置连接超时时间(单位毫秒)
					.setConnectTimeout(5000)
					// 设置请求超时时间(单位毫秒)
					.setConnectionRequestTimeout(5000)
					// socket读写超时时间(单位毫秒)
					.setSocketTimeout(5000)
					// 设置是否允许重定向(默认为true)
					.setRedirectsEnabled(true).build();

			// 将上面的配置信息 运用到这个Get请求里
			httpGet.setConfig(requestConfig);

			// 由客户端执行(发送)Get请求
			response = httpClient.execute(httpGet);

			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				InputStream inputStream = responseEntity.getContent();
				result = getInputStream2String(inputStream);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * POST方式请求---(普通参数)
	 * 
	 * @param xfAlarmPush
	 *
	 * @date 2018年7月13日 下午4:18:50
	 */
	public static String doPostParam(String path, HttpEntity entity, String token) {
		String result = "";
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 创建Post请求
		HttpPost httpPost = new HttpPost(path);
		// 响应模型
		CloseableHttpResponse response = null;
		try {
			// 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
			httpPost.setHeader("Content-Type", "application/json;charset=utf8");
			httpPost.setHeader("token", token);
			httpPost.setEntity(entity);
			// 由客户端执行(发送)Post请求
			response = httpClient.execute(httpPost);
			// 从响应模型中获取响应实体
			HttpEntity responseEntity = response.getEntity();
			System.out.println("响应状态为:" + response.getStatusLine());
			if (responseEntity != null) {
				InputStream inputStream = responseEntity.getContent();
				result = getInputStream2String(inputStream);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String doPostParams(String path,String params,String token) {
        String result = "";
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost(path);
        
        //json格式的请求数据封装
        StringEntity se = null;
		try {
			se = new StringEntity(params);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
        httpPost.setEntity(se);

        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        
        if(token != null && !"".equals(token)){
        	httpPost.setHeader("Authentication",token);
        }
        
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                InputStream inputStream =  responseEntity.getContent();
                result = getInputStream2String(inputStream);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

	/**
	 * json格式的数据作为请求体
	 * 
	 * @param path
	 *            请求路径
	 * @param json
	 *            请求体
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String doHttpEntity(String path, String json) {
		String content = "";
		// String token =
		// "R5amyr6NyXCtWdScmNiuvVwBCJztfByZDUGaE2V0NwOUheW4XYlvUusYkrViTYt584RgcyXRhjxAJZG3rFlPLg";
		// String url =
		// "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
		// + token;
		// String json =
		// "{\"action_name\":\"QR_LIMIT_SCENE\",\"action_info\":{\"scene\":{\"scene_id\":234}}}";
		// 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(path);
		// json传递
		StringEntity postingString = null;
		try {
			postingString = new StringEntity(json);
			post.setEntity(postingString);
			post.setHeader("Content-type", "application/json");
			HttpResponse response = httpClient.execute(post);
			content = EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(content);
		return content;
	}

	/**
	 * InputStream转为String
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String getInputStream2String(InputStream inputStream) {
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str = sb.toString();
		return str;
	}

	/**
	 * 请求结果String转换成JSONObject
	 * 
	 * @param data
	 *            请求结果
	 * @return
	 */
	public static JSONObject getResultJSON(String data) {
		// data格式
		// {"token":"3D100B53FE638CAC8F853BB2A9D9831D","status":"success"}
		JSONObject jsonObject = JSONObject.parseObject(data);
		JSONObject dataObject = jsonObject.getJSONObject("data");
		return dataObject;
	}

}
