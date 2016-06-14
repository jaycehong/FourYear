package com.coder.fouryear;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.coder.Constants;
import com.coder.fouryear.activity.FourYearApplication;
import com.coder.fouryear.activity.MyFragmentActivity;
import com.coder.fouryear.framwork.BaseFragment;
import com.coder.fouryear.framwork.BottomControlPanel;
import com.coder.fouryear.framwork.BottomControlPanel.BottomPanelCallback;
import com.coder.fouryear.framwork.HeadControlPanel;

public class MainActivity extends MyFragmentActivity implements BottomPanelCallback {
	private static MainActivity INSTANCE = null;
	private DrawerLayout mDrawerLayout;
	BottomControlPanel bottomPanel = null;
	HeadControlPanel headPanel = null;
	private FragmentManager fragmentManager = null;
	private FragmentTransaction fragmentTransaction = null;
	public static String currFragTag = "";
	// 单利模式
	public static MainActivity getInstance()
	{
		if (INSTANCE == null) {
			INSTANCE = new MainActivity();
		}
		return INSTANCE;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		INSTANCE = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();

		fragmentManager = this.getSupportFragmentManager();
		setDefaultFirstFragment(Constants.FRAGMENT_FLAG_FLEAMARKET);
		
	}
	
	private void initView() {
		// 底部控制栏
		bottomPanel = (BottomControlPanel) findViewById(R.id.bottom_layout);
		if (bottomPanel != null) {
			bottomPanel.initBottomPanel(null,true);
			bottomPanel.setBottomCallback(this);
		}
		// 顶部控制栏
		headPanel = (HeadControlPanel) findViewById(R.id.head_layout);
		if (headPanel != null) {
			headPanel.initHeadPanel();
		}
	}
	private void setDefaultFirstFragment(String tag) {
		Log.i("==MainActivity==", "setDefaultFirstFragment enter... currFragTag = "
				+ currFragTag);
		setTabSelection(tag,null);
		bottomPanel.defaultBtnChecked();
		Log.i("==MainActivity==", "setDefaultFirstFragment exit...");
	}
	
	@Override
	public void onBottomPanelClick(int itemId)
	{
		String tag = "";
		if ((itemId & Constants.BTN_FLAG_FLEAMARKET) != 0) {
			//tag = Constants.FRAGMENT_FLAG_MESSAGE;
			tag = Constants.FRAGMENT_FLAG_FLEAMARKET;
		} else if ((itemId & Constants.BTN_FLAG_CAMPUS) != 0) {
			tag = Constants.FRAGMENT_FLAG_CAMPUS;
		} else if ((itemId & Constants.BTN_FLAG_LOSTFOUND) != 0) {
			tag = Constants.FRAGMENT_FLAG_LOSTFOUND;
		}
		if(!currFragTag.equals(tag))
		{
			headPanel.setMiddleTitle(tag);// 切换标题
			setTabSelection(tag,null); // 切换Fragment
		}
	}
	/**
	 * 设置选中的Tag
	 * 
	 * @param tag
	 */
	public void setTabSelection(String tag,String type) {
		// 开启一个Fragment事务
		fragmentTransaction = fragmentManager.beginTransaction();
		/*
		 * if(TextUtils.equals(tag, Constant.FRAGMENT_FLAG_MESSAGE)){ if
		 * (messageFragment == null) { messageFragment = new MessageFragment();
		 * }
		 * 
		 * }else if(TextUtils.equals(tag, Constant.FRAGMENT_FLAG_CONTACTS)){ if
		 * (contactsFragment == null) { contactsFragment = new
		 * ContactsFragment(); }
		 * 
		 * }else if(TextUtils.equals(tag, Constant.FRAGMENT_FLAG_NEWS)){ if
		 * (newsFragment == null) { newsFragment = new NewsFragment(); }
		 * 
		 * }else if(TextUtils.equals(tag,Constant.FRAGMENT_FLAG_SETTING)){ if
		 * (settingFragment == null) { settingFragment = new SettingFragment();
		 * } }else if(TextUtils.equals(tag, Constant.FRAGMENT_FLAG_SIMPLE)){ if
		 * (simpleFragment == null) { simpleFragment = new SimpleFragment(); }
		 * 
		 * }
		 */
		switchFragment(tag,type);
	}

	/**
	 * 切换fragment
	 * 
	 * @param tag
	 */
	private void switchFragment(String tag,String type) {
		if (TextUtils.equals(tag, currFragTag)) {
			return;
		}
		// 把上一个fragment detach掉
		if (currFragTag != null && !currFragTag.equals("")) {
			detachFragment(getFragment(currFragTag));
		}
		attachFragment(R.id.fragment_content, getFragment(tag), tag ,type);
		commitTransactions(tag);
	}

	private BaseFragment getFragment(String tag) {
		BaseFragment f = (BaseFragment) fragmentManager.findFragmentByTag(tag);
		if (f == null) {
			f = BaseFragment.newInstance(getApplicationContext(), tag);
		}
		return f;
	}

	private void attachFragment(int layout, BaseFragment f, String tag ,String type) {
		if (f != null) {
			if(type!=null&&!"".equals(type)){
				f.setType(type);
			}
			if (f.isDetached()) {
				ensureTransaction();
				fragmentTransaction.attach(f);
			} else if (!f.isAdded()) {
				ensureTransaction();
				fragmentTransaction.add(layout, f, tag);
			}
		}
	}

	private void detachFragment(Fragment f) {
		if (f != null && !f.isDetached()) {
			ensureTransaction();
			fragmentTransaction.detach(f);
		}
	}

	private FragmentTransaction ensureTransaction() {
		if (fragmentTransaction == null) {
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		}
		return fragmentTransaction;
	}

	private void commitTransactions(String tag) {
		if (fragmentTransaction != null && !fragmentTransaction.isEmpty()) {
			fragmentTransaction.commit();
			currFragTag = tag;
			fragmentTransaction = null;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		currFragTag = "";
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}
	
	//先放这里吧。。
	public void login_out(View v) { // 标题栏 退出按钮
//		Intent intent = new Intent(MainActivity.this,
//		LoginActivity.class);
//		startActivity(intent);
//		FourYearApplication.getInstance().getCookies().clear();
//		FourYearApplication.getInstance().setCookie(null);
		this.finish();
		
	}
	private static boolean isExit=false;//判断是否二次点击返回键
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			exit();
			return false;
		} else{
			return super.onKeyDown(keyCode, event);
		}
		
	}
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
			FourYearApplication.getInstance().exit();
		}
	}

}
