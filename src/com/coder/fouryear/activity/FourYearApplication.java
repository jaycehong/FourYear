package com.coder.fouryear.activity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.coder.fouryear.MainActivity;

import android.app.Activity;
import android.app.Application;

public class FourYearApplication extends Application {

	/*
	 * 可以退出多个Activity。。。 在每个在 Activity
	 * 的onCreate()方法中调用ExitApplication.getInstance().addActivity(this)方法,
	 * 在退出时调用ExitApplication.getInstance().exit()方法，就可以完全退出应用程序了。
	 */

	private List<Activity> activityList = new LinkedList<Activity>();

	private static FourYearApplication instance;

	private String Cookie = null;

	private Map Cookies = null;

	// private CookieStore Cookies = null;
	//
	// private String password;
	//
	// private ExecutorService threadpool;

	private FourYearApplication() {
		// password ="";
		// threadpool =Executors.newFixedThreadPool(5);
	}

	// 单例模式中获取唯一的ExitApplication 实例
	public static FourYearApplication getInstance() {
		if (instance == null) {
			instance = new FourYearApplication();
		}
		return instance;

	}

	// 存入cookie
	public void setCookies(Map cookies) {
		Cookies = cookies;
	}

	// 获取cookie
	public Map getCookies() {

		return Cookies;
	}

	public void setCookie(String cookie) {
		Cookie = cookie;
	}

	// 获取cookie
	public String getCookie() {

		return Cookie;
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
		// password ="";
		// threadpool =Executors.newFixedThreadPool(5);
	}

	// public ExecutorService get_threadpool()
	// {
	// return threadpool;
	//
	// }

	// public boolean is_passwordset(){
	// if(password.equals("")){return false;}
	// else{ return true;}
	// }

	// //保存密码
	// public void save_password(String pass)
	// {
	// this.password = pass;
	// }
	// //获取密码
	// public String get_password()
	// {
	// return password;
	// }

	// 添加Activity 到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void RemoveActivity(Activity activity) {
		activityList.remove(activity);

	}

	// 遍历所有Activity 并finish

	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}

		MainActivity.getInstance().finish();

		System.exit(0);

	}
}