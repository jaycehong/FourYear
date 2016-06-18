package com.coder.fouryear.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.widget.Toast;

import com.coder.Constants;
import com.coder.SimpleHttpClient;


/**
 * 一些暂时不知如何归类的方法放在这里。
 */
public class AppUtils {
	public static JSONObject doLogin(String username, String password, boolean persist, SharedPreferences sp) throws IOException, JSONException{
		
		String actionUrl = Constants.urlRoot+"/login.action?method:login";
		JSONObject jsonObj = null;
		Map m = new HashMap();
		m.put("username", username);
		m.put("password", password);
		//m中不能有空值（即传递的参数中不能有空值），否则action中无法接收
		String sendInfo = SimpleHttpClient.simplePost(actionUrl,m);
		//返回来的信息：{"ok":1,"data":{"user":{"username":"洪坤波","city":"","id":37}}}  {"ok":0,"msg":"用户或密码错误"}
		if(sendInfo!=null){
			jsonObj = new JSONObject(sendInfo);
			int loginFlag = jsonObj.optInt("ok");
			if(loginFlag == 1){
				JSONObject jsonObj2 = jsonObj.optJSONObject("data");
				JSONObject jsonObj3 = jsonObj2.optJSONObject("user");
				String id = jsonObj3.optString("id");
				Editor editor = sp.edit();
				if (persist) {
					editor.putBoolean("ISCHECK", true);
					editor.putString("USERNAME", username);
					editor.putString("PASSWORD", password);  
					editor.putString("USERID", id);  
				} else {
					editor.putBoolean("ISCHECK", false);
					editor.putString("USERNAME", null);
					editor.putString("PASSWORD", null);  
					editor.putString("USERID", null);  
				}
				//取得sessionid，并保存到SharedPreferences中
				editor.commit(); 
				// 把用户名和用户id保存到Constants中
				Constants.userName = username;
				Constants.userId = id;
			}
		}
		return jsonObj;
	}
	
	/**
	 * 网络是否可用。
	 * @param ctx
	 * @return
	 */
    public static boolean isNetworkAvailable(Context ctx) {   
        try {   
            ConnectivityManager cm = (ConnectivityManager) ctx   
                    .getSystemService(Context.CONNECTIVITY_SERVICE);   
            NetworkInfo info = cm.getActiveNetworkInfo();   
            return (info != null && info.isConnected());   
        } catch (Exception e) {   
            e.printStackTrace();   
            return false;   
        }   
    }
    /**
     * 基站定位。
     * @param ctx
     * @return
     */
	public static CdmaCellLocation getCdmaLocation(Context ctx) {
		TelephonyManager phoneMgr = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		CellLocation location = phoneMgr.getCellLocation();
		if (location instanceof CdmaCellLocation) {
			CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) location;
			return cdmaCellLocation;
		}
		return null;
	}
	
	//进入邮箱
//	public static void showMailOperationDialog(Context context,String mail) {
//		MailOperationDialog dialog = new MailOperationDialog(context, mail);
//		dialog.show();
//	}
	//进入网址
	public static void gotoWebSite(Context context,String webSite){
		Uri uri = Uri.parse(webSite);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}	
		
	//打电话操作
//	public static void showTelOpertaionDialog(Context context,String mobile) {
//		TelOperationDialog dialog = new TelOperationDialog(context, mobile);
//		dialog.show();
//	}
	
	public static void gotoQQ(Context context,String qq) {
		try {
			//方法1
//			Intent intent = new Intent();
//			intent.setAction(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_LAUNCHER);
//			intent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity");
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
			//方法2
			String url="mqqwpa://im/chat?chat_type=wpa&uin="+qq;
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		} catch (ActivityNotFoundException e) {
			Toast.makeText(context, "您还没有安装QQ", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	//极光 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
	
}

