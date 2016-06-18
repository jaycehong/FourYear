package com.coder.fouryear.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.coder.fouryear.MainActivity;
import com.coder.fouryear.R;
import com.coder.fouryear.utils.AppUtils;


public class WelcomeActivity extends Activity {
	protected static final String TAG = "==WelcomeActivity==";
	public static SharedPreferences sp;
	private ProgressDialog progressDialog = null;
	private static final int MSG_SET_ALIAS = 1001;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题
		setContentView(R.layout.welcome_screen);
		FourYearApplication.getInstance().addActivity(this);//FIXME welcome页面背景图片要换一个
		//sp = PreferenceManager.getDefaultSharedPreferences(this);
		sp=getSharedPreferences("userInfo", 0);
		     Timer timer=new Timer();
		     TimerTask task=new TimerTask(){
		        public void run(){
		        	loginIng();
		        }
		    };
		    timer.schedule(task, 2500);
	}
	
	private static boolean isExit=false;//判断是否二次点击返回键
	
	/*再按一次返回，退出erp软件
	 * */
	private Handler mhandler =new Handler(){
		
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			isExit=false;
			Log.i("isExit", "---------new--------"+isExit);
		}
	};
	
	public void exit(){
		if(!isExit){
			isExit=true;
			Toast.makeText(getApplicationContext(),"再按一次返回键，退出软件！", Toast.LENGTH_SHORT).show();
			mhandler.sendEmptyMessageDelayed(0, 2000);
		}else{
			//finish();
			//System.exit(0);
			FourYearApplication.getInstance().exit();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

/**
	有网：
	有username，password，并且自动登录正确--->4
	无username，无password/自动登录失败----->2
	没网：
	有username，password，和本地密码验证无误？--->4
	无username，无password ----->这种情况应该是第一次安装时会出现，需要用户联网再登陆--->2
 */
	private Handler handler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(progressDialog!=null && progressDialog.isShowing())
				progressDialog.dismiss();
			super.handleMessage(msg);
		}
	};
	
	
	//自动登陆操作
	private void loginIng() {
		/*if(progressDialog != null && progressDialog.isShowing()){}else{
			progressDialog = ProgressDialog.show(WelcomeActivity.this, null,
					"正在登录...", true);
		}*/
		
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
					//获取用户
					String username = sp.getString("USERNAME", null);
					String password = sp.getString("PASSWORD", null);
					boolean loginOk = false;
					//eg:{PASSWORD=1, ISCHECK=true, USERNAME=杨亚妮, USERID=37,SESSIONID=81311AF5718FDE82EAB92C30A5D71719}
					//Log.i("isExit","username="+username+";password="+password+";ifexist="+ifExist+";ischangedcode="+isChangedCode);
					if(isNetworkConnected(getApplicationContext())){//网络可用
						//有username，password，执行自动登录
						if (username !=null && password != null) {			
							JSONObject json;
							try {
								json = AppUtils.doLogin(username, password, true,sp);
								if(json!=null){
									int loginFlag = json.optInt("ok",0);
									if(loginFlag == 1){
										loginOk = true;
									}
								}
							} catch (Exception e) {
							//	Toast.makeText(getApplicationContext(), "登录异常",Toast.LENGTH_LONG).show();
								e.printStackTrace();
								loginOk = false;//服务器端异常，或者联接上chinanet但无法访问Internet
							}
						}
					}else{//网络不可用
						if (username !=null && password != null) {	//离线使用//FIXME 密码是否要加密，等等
							loginOk = false;
						}
					}

					Message message = handler.obtainMessage();
					handler.sendMessage(message);
					
					if(loginOk){//已经初始化过，且自动登录成功-->主页面
						setAlias(username);//推送设置
						Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
			        	startActivity(intent);
			        	WelcomeActivity.this.finish();
					}else {//1.更改密码，2.或者服务器异常,3.无用户密码（也就第一次使用本软件会存在这种情况）-->登录页面
						Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
						startActivity(intent);
						WelcomeActivity.this.finish();
					}
					
			}
		});
		t.start();
	}	
	
	
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	   protected void onDestroy() { 
	        super.onDestroy(); 
	        //移出管理栈
	        FourYearApplication.getInstance().RemoveActivity(this); 
	} 
	   
	   private void setAlias(String str){
			String alias = str;
			if (TextUtils.isEmpty(alias)) {
				Toast.makeText(this,"设置alias失败", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!AppUtils.isValidTagAndAlias(alias)) {
				Toast.makeText(this,"设置alias失败", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		
		private final Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(android.os.Message msg) {
	            super.handleMessage(msg);
	        }
	    };
}