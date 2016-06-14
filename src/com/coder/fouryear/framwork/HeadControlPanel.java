package com.coder.fouryear.framwork;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.Constants;
import com.coder.fouryear.R;

public class HeadControlPanel extends RelativeLayout {

	private Context mContext;
	private TextView mMidleTitle;
	private TextView mRightTitle;
	private Button mRightBtn;
	private Button mBackBtn;
	private static final float middle_title_size = 20f;
	private static final float right_title_size = 17f;
	private static final int default_background_color = Color.rgb(23, 124, 202);
	private static HeadControlPanel INSTANCE = null;

	public HeadControlPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	
	@Override
	protected void onFinishInflate() {
		mMidleTitle = (TextView) findViewById(R.id.midle_title);
		mRightTitle = (TextView) findViewById(R.id.right_title);
		LayoutInflater factory=LayoutInflater.from(getContext());
		//mSearchBox = (EditText) netelementsLayout.findViewById(R.id.search_box);
		mBackBtn = (Button) findViewById(R.id.back_button);
		mRightBtn = (Button) findViewById(R.id.right_button);
		setBackgroundColor(default_background_color);
	}

	public void initHeadPanel() {
		if (mMidleTitle != null) {
			setMiddleTitle(Constants.FRAGMENT_FLAG_FLEAMARKET);
		}
	}

	public void setMiddleTitle(String s) {
		mMidleTitle.setText(s);
		mMidleTitle.setTextSize(middle_title_size);
		if (TextUtils.equals(s, Constants.FRAGMENT_FLAG_FLEAMARKET)) {
			mRightBtn.setVisibility(View.VISIBLE);
			mBackBtn.setVisibility(View.GONE);
			mRightTitle.setVisibility(View.GONE);
		} else if (TextUtils.equals(s, Constants.FRAGMENT_FLAG_CAMPUS)) {
			mRightBtn.setVisibility(View.GONE);
			mBackBtn.setVisibility(View.GONE);
			mRightTitle.setVisibility(View.VISIBLE);
			setRightTitle(Constants.FRAGMENT_FLAG_SEARCH);
		} else if (TextUtils.equals(s, Constants.FRAGMENT_FLAG_LOSTFOUND)) {
			mRightBtn.setVisibility(View.GONE);
			mBackBtn.setVisibility(View.GONE);
			mRightTitle.setVisibility(View.GONE);
		}else {
			mRightBtn.setVisibility(View.GONE);
			mBackBtn.setVisibility(View.GONE);
			mRightTitle.setVisibility(View.VISIBLE);
			setRightTitle(Constants.FRAGMENT_FLAG_SEARCH);
		}
	}
	
	public void setRightTitle(String s) {
		mRightTitle.setText(s);
		mRightTitle.setTextSize(right_title_size);
		mRightTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if("查询".equals(mRightTitle.getText())){
					//mSearchBox.setVisibility(View.VISIBLE);
					mRightTitle.setText("取消");
				}else if("取消".equals(mRightTitle.getText())){
					//mSearchBox.setVisibility(View.GONE);
					mRightTitle.setText("查询");
				}
			}
		});
	}

}