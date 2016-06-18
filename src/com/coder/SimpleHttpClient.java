package com.coder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.apache.http.HttpException;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

public class SimpleHttpClient {

	private static final String TAG = SimpleHttpClient.class.getName();
	static String x = "";
	/**
	 * 
	 * @param requestUrl
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws  
	 */
	public static String simpleRequest(String requestUrl, String params) throws IOException{
		try {
			return WebBrowser.webBrowser.requestAsString(requestUrl+"?"+params, null, "GET");
		} catch (HttpException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	static DefaultHttpClient httpclient;
	public static String simplePost(String url, Map<String,String> variables) throws IOException{
		try {
			return WebBrowser.webBrowser.requestAsString(url, variables, "POST");
		} catch (HttpException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(),e);
		}
	}
    
    public static String convertStreamToString(InputStream is) {
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));
         StringBuilder sb = new StringBuilder();

         String line = null;
         try {
        	 int i = 0;
              while ((line = reader.readLine()) != null) {
            	  if(i>0)sb.append("\n");
            	  i++;
                   sb.append(line);
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
    
	public static void uploadFile(String actionUrl,String typeNameStr, File myFile) throws IOException {
		if (!myFile.exists()) {
			Log.i(TAG, "照片文件不存在！");
			return;
		} else {
			// 存在则读数据
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(myFile);
				uploadFile(actionUrl, typeNameStr, inputStream);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(),e);
			}finally{
				try {
					if(inputStream != null)
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void uploadFile(String actionUrl,String fileNameStr, InputStream inputStream) throws IOException {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传�?的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file\";filename=\"" + fileNameStr + "\"" + end);
			ds.writeBytes("Content-Type: image/gif" + end);
			ds.writeBytes(end);

				/* 取得文件的FileInputStream */

				/* 设置每次写入1024bytes */
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];

				int length = -1;
				/* 从文件读取数据至缓冲�?*/
				while ((length = inputStream.read(buffer)) != -1) {
					/* 将资料写入DataOutputStream�?*/
					ds.write(buffer, 0, length);
				}
				ds.writeBytes(end);
				ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

				ds.flush();
				Log.i(TAG, "发送完成!");
				/* 取得Response内容 */
				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				System.out.println(b);
				ds.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	

	
} 
