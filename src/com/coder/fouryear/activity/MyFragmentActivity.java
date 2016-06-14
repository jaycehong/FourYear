package com.coder.fouryear.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
public class MyFragmentActivity extends FragmentActivity{
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
			FourYearApplication.getInstance().exit();
		}
	}
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			exit();
			return false;
		} else{
			return super.onKeyDown(keyCode, event);
		}
		
	}

}
