package com.coder.fouryear.framwork;

import java.util.ArrayList;
import java.util.List;


import com.coder.Constants;
import com.coder.fouryear.R;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class BottomControlPanel extends RelativeLayout implements View.OnClickListener {
	
	private static BottomControlPanel INSTANCE = null;
	private static Context context = null;
	private Context mContext;
	private static ImageText btnFleaMarket = null;
	private static ImageText btnCampusNews = null;
	private static ImageText btnLostFound = null;
	private int DEFALUT_BACKGROUND_COLOR = Color.rgb(243, 243, 243); // Color.rgb(192,192,192)
	private BottomPanelCallback mBottomCallback = null;
	private List<ImageText> viewList = new ArrayList<ImageText>();

	
	public interface BottomPanelCallback 
	{
		public void onBottomPanelClick(int itemId);
	}

	public BottomControlPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		this.context = context;
	}

	public static BottomControlPanel getInstance() { 
		
		if(INSTANCE == null){
			INSTANCE = new BottomControlPanel(context , null);
		}
		return INSTANCE;
	}
	
	@Override
	protected void onFinishInflate() {
		btnFleaMarket = (ImageText) findViewById(R.id.btn_fela_market);
		btnCampusNews = (ImageText) findViewById(R.id.btn_campus_news);
		btnLostFound = (ImageText) findViewById(R.id.btn_lost_found);
		setBackgroundColor(DEFALUT_BACKGROUND_COLOR);
		viewList.add(btnFleaMarket);
		viewList.add(btnLostFound);
		viewList.add(btnCampusNews);
	}

	public void initBottomPanel(String type,Boolean flag) {
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		
		
		if (btnFleaMarket != null) {
			btnFleaMarket.setImage(R.drawable.flea_market_unselected);
			btnFleaMarket.setText(Constants.FRAGMENT_FLAG_FLEAMARKET);
		}
		
		if (btnCampusNews != null) {
			btnCampusNews.setImage(R.drawable.campus_news_unselected);
			btnCampusNews.setText(Constants.FRAGMENT_FLAG_CAMPUS);
		}
		
		if (btnLostFound != null) {
			btnLostFound.setImage(R.drawable.lost_found_unselected);
			if(type!=null){	
				btnLostFound.setText(type);
			}else if(flag){
				btnLostFound.setText(Constants.FRAGMENT_FLAG_LOSTFOUND);
			}
		}
		//调整图标位置
		
		layoutItems();
		
		setBtnListener();
	}

	private void setBtnListener() {
		int num = this.getChildCount();
		for (int i = 0; i < num; i++) {
			View v = getChildAt(i);
			if (v != null) {
				v.setOnClickListener(this);
			}
		}
	}

	public void setBottomCallback(BottomPanelCallback bottomCallback) {
		mBottomCallback = bottomCallback;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id==R.id.btn_lost_found){
			initBottomPanel(null,false);
		}
		else
			initBottomPanel(null,true);
		int index = -1;
		switch (id) {
		case R.id.btn_fela_market:
			index = Constants.BTN_FLAG_FLEAMARKET;
			btnFleaMarket.setChecked(Constants.BTN_FLAG_FLEAMARKET);
			break;
		case R.id.btn_campus_news:
			index = Constants.BTN_FLAG_CAMPUS;
			btnCampusNews.setChecked(Constants.BTN_FLAG_CAMPUS);
			break;
		case R.id.btn_lost_found:
			index = Constants.BTN_FLAG_LOSTFOUND;
			btnLostFound.setChecked(Constants.BTN_FLAG_LOSTFOUND);
			break;
		default:
			break;
		}
		if (mBottomCallback != null) {
			mBottomCallback.onBottomPanelClick(index);
		}
	}

	
	public void onClick(int id,String type) {
		initBottomPanel(type,true);
		int index = -1;
		switch (id) {
		case R.id.btn_fela_market:
			index = Constants.BTN_FLAG_FLEAMARKET;
			btnFleaMarket.setChecked(Constants.BTN_FLAG_FLEAMARKET);
			break;
		case R.id.btn_campus_news:
			index = Constants.BTN_FLAG_CAMPUS;
			btnCampusNews.setChecked(Constants.BTN_FLAG_CAMPUS);
			break;
		case R.id.btn_lost_found:
			index = Constants.BTN_FLAG_LOSTFOUND;
			btnLostFound.setChecked(Constants.BTN_FLAG_LOSTFOUND);
			break;
		default:
			break;
		}
		if (mBottomCallback != null) {
			mBottomCallback.onBottomPanelClick(index);
		}
	}
	
	public void defaultBtnChecked() {
		if (btnFleaMarket != null) {
			btnFleaMarket.setChecked(Constants.BTN_FLAG_FLEAMARKET);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	//	layoutItems(left, top, right, bottom);
	}

	/**
	 * 最左边和最右边的view由母布局的padding进行控制位置。这里需对第2、3个view的位置重新设置
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	private void layoutItems() {
		int n = getChildCount();
		if (n == 0) {
			return;
		}
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		Log.i("==BottomControlPanel==", "paddingLeft = " + paddingLeft + " paddingRight = "
				+ paddingRight);		
		int allViewWidth = 64*n;
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		
		int blankWidth = (width - allViewWidth - paddingLeft - paddingRight) / (n - 1);
		Log.i("==BottomControlPanel==", "blankV = " + blankWidth);

		LayoutParams params1 = (LayoutParams) viewList.get(1).getLayoutParams();
		params1.leftMargin = blankWidth;
		viewList.get(1).setLayoutParams(params1);

		LayoutParams params2 = (LayoutParams) viewList.get(2).getLayoutParams();
		params2.leftMargin = blankWidth;
		viewList.get(2).setLayoutParams(params2);
	}

}