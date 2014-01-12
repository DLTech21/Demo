package com.baidu.push.example;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;

/**
 * @author Donal Tong
 * @project FansCam
 * @created 2013-9-17
 * Copyright (c) 2013年 Donal Tong. All rights reserved.
 */
public class BaseActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(null);
		AppManager.getAppManager().addActivity(this);
	}

	protected void onDestroy() {
		super.onDestroy();
	}
	
//	public abstract void ButtonClick(View v);
}
