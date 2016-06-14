package com.coder.fouryear.framwork;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coder.Constants;

public class BaseFragment extends Fragment {
	private static final String TAG = "==BaseFragment==";
	private String type;

	public static BaseFragment newInstance(Context context, String tag) {
		BaseFragment f = null;
		if(TextUtils.equals(tag, Constants.FRAGMENT_FLAG_FLEAMARKET)){
			f = new FleaMarketFragment();
		}else if(TextUtils.equals(tag, Constants.FRAGMENT_FLAG_CAMPUS)){
			f = new FleaMarketFragment();
		}else if(TextUtils.equals(tag, Constants.FRAGMENT_FLAG_LOSTFOUND)){
			f = new FleaMarketFragment();
		}else{
			f = new BaseFragment();
		}
//		Bundle args = new Bundle();
//		args.putString("tag", tag);
//		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e(TAG, "onAttach-----");

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "onCreate------");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView------");		
		return 	super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(TAG, "onActivityCreated-------");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e(TAG, "onStart----->");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "onresume---->");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(TAG, "onpause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e(TAG, "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(TAG, "ondestoryView");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "ondestory");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "onDetach------");
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
