package com.coder.fouryear.activity;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.coder.fouryear.MainActivity;
import com.coder.fouryear.R;
import com.coder.fouryear.utils.AppUtils;

public class LoginActivity extends Activity {
	protected static final String TAG = "==LoginActivity==";
	private static final int MSG_SET_ALIAS = 1001;
	private EditText mUser; // 帐号编辑框
	private EditText mPassword; // 密码编辑框
	public SharedPreferences sp;
	private CheckBox cb;
	protected JSONObject json;
	protected String username;
	protected String password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		FourYearApplication.getInstance().addActivity(this);
		// 存储用户信息，以便自动登录
		// sp = PreferenceManager.getDefaultSharedPreferences(this);
		sp = getSharedPreferences("userInfo", 0);
		System.out.println(sp);
		String username = sp.getString("USERNAME", null);
		String password = sp.getString("PASSWORD", null);
		cb = (CheckBox) this.findViewById(R.id.remember_password_box);
		cb.setChecked(true);
		mUser = (EditText) findViewById(R.id.login_user_edit);
		mPassword = (EditText) findViewById(R.id.login_passwd_edit);
		
		mUser.setText(username);
		mPassword.setText(password);
	}

	public void login_main(View v) {
		if (isNetworkConnected(getApplicationContext())) {
			/**
			 * 用户码和密码
			 */
			if (!"".equals(mUser.getText().toString())
					&& !"".equals(mPassword.getText().toString())) // 判断 帐号和密码
			{
				username = mUser.getText().toString();
				password = mPassword.getText().toString();
				json = null;

				new Thread() {
					@Override
					public void run() {
						Looper.prepare();
						try {
//							json = AppUtils.doLogin(username, password,
//									cb.isChecked(), sp);//先不请求后台吧
							json = new JSONObject("{'ok':1,'data':{'user':{'username':'洪坤波','city':'深圳','id':37}}} ");
						} 
//							catch (IOException e) {
						//	Toast.makeText(getApplicationContext(),
						//			"登录异常,请检查网络", Toast.LENGTH_LONG).show();
						//	return;
						//} 
						catch (JSONException e) {
							Toast.makeText(getApplicationContext(),"登录异常,请检查网络", Toast.LENGTH_LONG).show();
							return;
						}
						handler.sendEmptyMessage(0);
						Looper.loop();
					}
				}.start();

			} else {
				new AlertDialog.Builder(LoginActivity.this)
						.setIcon(
								getResources().getDrawable(
										R.drawable.login_error_icon))
						.setTitle("登录失败").setMessage("帐号和密码不能为空！").create()
						.show();
			}
		} else {
			new AlertDialog.Builder(LoginActivity.this)
					.setIcon(
							getResources().getDrawable(
									R.drawable.login_error_icon))
					.setTitle("登录失败").setMessage("网络不给力！").create().show();
		}
	}

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				int loginFlag = json.optInt("ok");
				if (loginFlag == 1) {
					setAlias(username);//推送设置
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
				} else {
					String error = json.optString("msg");
					mPassword.setError(error);
				}
				break;
			}
		};
	};
	
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
			
			//调用JPush API设置Alias
			mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
		}
		
		
		private final Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(android.os.Message msg) {
	            super.handleMessage(msg);
	        }
	    };

	public void regist(View v) { // 注册新帐号
		// Intent activityintent = new
		// Intent(LoginActivity.this,RegistActivity.class);
		// startActivity(activityintent);
		// overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}

	public void login_pw(View v) { // 忘记密码按钮

	}

	/*
	 * @Override public void onBackPressed() {
	 * ExitApplication.getInstance().exit(); return; }
	 */

	private static boolean isExit = false;// 判断是否二次点击返回键

	/*
	 * 再按一次返回，退出软件
	 */
	private Handler mhandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
			Log.i("isExit", "---------new--------" + isExit);
		}
	};

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次返回键，退出软件！",
					Toast.LENGTH_SHORT).show();
			mhandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			// finish();
			// System.exit(0);
			FourYearApplication.getInstance().exit();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
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
		// 移出管理栈
		FourYearApplication.getInstance().RemoveActivity(this);
	}
}
