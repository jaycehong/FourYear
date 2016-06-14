package com.coder;

import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
  
public class Constants {
	//Btn的标识
	public static final int BTN_FLAG_FLEAMARKET  = 0x01;
	public static final int BTN_FLAG_CAMPUS = 0x01 << 1;
	public static final int BTN_FLAG_LOSTFOUND = 0x01 << 2;
	
	//Fragment的标识additional
	public static final String FRAGMENT_FLAG_FLEAMARKET = "跳蚤市场"; //中间标题
	public static final String FRAGMENT_FLAG_CAMPUS = "图说校园"; //中间标题
	public static final String FRAGMENT_FLAG_LOSTFOUND = "失物招领"; //中间标题
	public static final String FRAGMENT_FLAG_SEARCH = "查询"; //右边标题
	
	//基本信息
//	public static String host ="www.fouryear.com";//服务器ip地址
	public static String host ="192.168.0.104";//本机ip地址
//	public static String host ="192.168.1.100";
//	public static int port =8080;//本机端口
	public static String ctx = "/njupt";
	public static String urlRoot = "http://"+host+":80"+ctx;//服务器urlRoot
//	public static String urlRoot = "http://"+host+":"+port+ctx;//本机urlRoot
	
	public static String imgUrlRoot = "http://"+host+":80"+ctx;  

	
	public static final String APPLICATION_NAME = "FOURYEAR";
	public static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	public static final String PATH = Environment.getExternalStorageDirectory().toString() + "/" + APPLICATION_NAME; 
	
	//用户
	public static String userId ; 
	public static String userName;    
}
