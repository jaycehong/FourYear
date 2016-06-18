package com.coder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

import com.coder.fouryear.activity.FourYearApplication;

/**
 * 读取html并解析�?
 * 目前的设计一个Browser对象对应�?��域名，因为每次会获取response返回的cookie数据作为下次请求的cookie内容�?
 * 不会为不同的域名保存cookie�?
 * 包括编码等都没有仔细考虑�?
 */
public class WebBrowser {
	public static WebBrowser webBrowser = new WebBrowser();
	public String host;
	private int port = 80;
	DefaultHttpClient httpClient;
	HttpRequestBase httpMethod = null;
	CookieStore cookies;
	HttpResponse response ;
	int statusCode;
	private static final String TAG = "webBrowser";
	/**
	 * 执行本方法后可以再获取httpClient或httpMethod等对象以获取其他相关信息�?
	 * @param url
	 * @param params
	 * @param method
	 * @throws HttpException
	 * @throws IOException
	 */
	public void request(String url,Map variables,String method) throws HttpException, IOException{
	    ArrayList<BasicNameValuePair> pairs;
		if(!url.contains("http"))url="http://"+host+":"+port+url;
		Log.d(TAG, url);
		if(!"post".equalsIgnoreCase(method)){
			httpMethod = new HttpGet(url);
		}else{
			httpMethod = new HttpPost(url);
		}
        pairs = new ArrayList();
        if(variables != null){
             Set keys = variables.keySet();
             for(Iterator i = keys.iterator(); i.hasNext();) {
                  String key = (String)i.next();
                  String v = variables.get(key)==null?null:variables.get(key)+"";
                  pairs.add(new BasicNameValuePair(key, v));
             }
        }
        UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs, "UTF-8");
        
		if(!"post".equalsIgnoreCase(method)){
			//TODO 有问�?
		}else{
			((HttpPost)httpMethod).setEntity(p_entity);
		}
		
		if(cookies != null)httpClient.setCookieStore(cookies);
		
		try{
			response = httpClient.execute(httpMethod);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		cookies = httpClient.getCookieStore();
		if(cookies!=null){
			Map cookie = new HashMap();
			String user = "";
			String path = "";
			String domain = "";
			String expires = "";
			//String JSESSIONID= cookies.getCookies().get(0).getValue();
			if(cookies.getCookies().size()>1)
			{
				path = cookies.getCookies().get(1).getPath();
				domain = cookies.getCookies().get(1).getDomain();
				expires = cookies.getCookies().get(1).getExpiryDate().toGMTString();
				user = cookies.getCookies().get(1).getValue();
				
				//cookie.put("JSESSIONID",JSESSIONID);
				cookie.put("user","\""+user+"\"");
				cookie.put("path",path);
				cookie.put("domain",domain);
				cookie.put("expires",expires);
			}
			
			Map oldCookie = FourYearApplication.getInstance().getCookies();
			if(oldCookie==null||oldCookie.isEmpty())
				FourYearApplication.getInstance().setCookies(cookie);
			
			//String cookiesStr = "JSESSIONID="+JSESSIONID+"; user=\""+user+"\";path="+path+";domain="+domain+";Expires="+expires+"";
			String cookiesStr = " user=\""+user+"\";path="+path+";domain="+domain+";Expires="+expires+"";
			FourYearApplication.getInstance().setCookie(cookiesStr);
		}
	}
	public String requestAsString(String url,Map params,String method) throws HttpException, IOException{
		request( url, params, method);
        /** 发出实际的HTTP POST请求 */
        HttpEntity entity = response.getEntity(); 
        InputStream  content = entity.getContent();
        String returnConnection = convertStreamToString(content);
        return returnConnection;
		
	}
	public InputStream requestAsInputStream(String url,Map params,String method) throws HttpException, IOException{
		request( url, params, method);
        HttpEntity entity = response.getEntity();
        System.out.println("获取httpEntity的长度：------"+entity.getContentLength());
        InputStream  content = entity.getContent();
        return content;
	}
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
             while ((line = reader.readLine()) != null) {
                  sb.append(line + "\n");
             }
        } catch (IOException e) {
             e.printStackTrace();
        } finally {
             try {
                  is.close();
             } catch (IOException e) {
                  e.printStackTrace();
             }
        }

        return sb.toString();
   }
	public WebBrowser() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		ConnManagerParams.setMaxTotalConnections(params, 100);   
		SchemeRegistry schemeRegistry = new SchemeRegistry();   
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));   

		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);
		httpClient = new DefaultHttpClient(cm,params);
		HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);   
		//System.out.println("-------i am HtmlParser----------------------()");
	}
	public WebBrowser(String host, int port) {
		this();
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public HttpClient getHttpClient() {
		return httpClient;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public HttpRequestBase getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(HttpRequestBase httpMethod) {
		this.httpMethod = httpMethod;
	}
	public CookieStore getCookies() {
		return cookies;
	}
	public void setCookies(CookieStore cookies) {
		this.cookies = cookies;
	}
	public HttpResponse getResponse() {
		return response;
	}
	public void setResponse(HttpResponse response) {
		this.response = response;
	}
	public void setHttpClient(DefaultHttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
}
