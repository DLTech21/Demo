package com.baidu.push.example;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.crashlytics.android.Crashlytics;

public class PushDemoActivity extends AppActivity implements View.OnClickListener {

	private static final String TAG = PushDemoActivity.class.getSimpleName();
	RelativeLayout mainLayout = null;
	int akBtnId = 0;
	int initBtnId = 0;
	int richBtnId = 0;
	int setTagBtnId = 0;
	int delTagBtnId = 0;
	int clearLogBtnId = 0;
	Button initButton = null;
	Button initWithApiKey = null;
	Button displayRichMedia = null;
	Button setTags = null;
	Button delTags = null;
	Button clearLog = null;
	TextView logText = null;
	ScrollView scrollView = null;
	public static int initialCnt = 0;
	private boolean isLogin = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		Utils.logStringCache = Utils.getLogText(getApplicationContext());
		
		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		
		setContentView(resource.getIdentifier("main", "layout", pkgName));
		akBtnId = resource.getIdentifier("btn_initAK", "id", pkgName);
		initBtnId = resource.getIdentifier("btn_init", "id", pkgName);
		richBtnId = resource.getIdentifier("btn_rich", "id", pkgName);
		setTagBtnId = resource.getIdentifier("btn_setTags", "id", pkgName);
		delTagBtnId = resource.getIdentifier("btn_delTags", "id", pkgName);
		clearLogBtnId = resource.getIdentifier("btn_clear_log", "id", pkgName);
		
		initWithApiKey = (Button) findViewById(akBtnId);
		initButton = (Button) findViewById(initBtnId);
		displayRichMedia = (Button) findViewById(richBtnId);
		setTags = (Button) findViewById(setTagBtnId);
		delTags = (Button) findViewById(delTagBtnId);
		clearLog = (Button) findViewById(clearLogBtnId);
		
		logText = (TextView) findViewById(resource.getIdentifier("text_log", "id", pkgName));
		scrollView = (ScrollView) findViewById(resource.getIdentifier("stroll_text", "id", pkgName));
		
		
		initWithApiKey.setOnClickListener(this);
		initButton.setOnClickListener(this);
		setTags.setOnClickListener(this);
		delTags.setOnClickListener(this);
		displayRichMedia.setOnClickListener(this);
		clearLog.setOnClickListener(this);
		
//		if (!Utils.hasBind(getApplicationContext())) {
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY, 
					Utils.getMetaValue(PushDemoActivity.this, "api_key"));
			PushManager.enableLbs(getApplicationContext());
//		}
//		
//        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
//        		getApplicationContext(),
//        		resource.getIdentifier("notification_custom_builder", "layout", pkgName), 
//        		resource.getIdentifier("notification_icon", "id", pkgName), 
//        		resource.getIdentifier("notification_title", "id", pkgName), 
//        		resource.getIdentifier("notification_text", "id", pkgName));
//        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
//        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
//        cBuilder.setLayoutDrawable(resource.getIdentifier("simple_notification_icon", "drawable", pkgName));
//		PushManager.setNotificationBuilder(this, 1, cBuilder);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == akBtnId) {
			initWithApiKey();
		} else if (v.getId() == initBtnId) {
			initWithBaiduAccount();
		} else if (v.getId() == richBtnId) {
			openRichMediaList();
		} else if (v.getId() == setTagBtnId) {
			setTags();
		} else if (v.getId() == delTagBtnId) {
			deleteTags();
		} else if (v.getId() == clearLogBtnId) {
			Utils.logStringCache = "";
			Utils.setLogText(getApplicationContext(), Utils.logStringCache);
			updateDisplay();
		} else  {
			
		}
		
	}
	
	private void openRichMediaList() {
		Intent sendIntent = new Intent();
		sendIntent.setClassName(getBaseContext(), "com.baidu.android.pushservice.richmedia.MediaListActivity");
		sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PushDemoActivity.this.startActivity(sendIntent);
	}

	private void deleteTags() {
		LinearLayout layout = new LinearLayout(PushDemoActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);

		final EditText textviewGid = new EditText(PushDemoActivity.this);
		textviewGid.setHint("????????????????????????");
		layout.addView(textviewGid);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				PushDemoActivity.this);
		builder.setView(layout);
		builder.setPositiveButton("?????",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						List<String> tags = Utils.getTagsList(textviewGid.getText().toString());
						PushManager.delTags(getApplicationContext(), tags);
					}
				});
		builder.show();
	}

	private void setTags() {
		LinearLayout layout = new LinearLayout(PushDemoActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);

		final EditText textviewGid = new EditText(PushDemoActivity.this);
		textviewGid.setHint("????????????????????????");
		layout.addView(textviewGid);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				PushDemoActivity.this);
		builder.setView(layout);
		builder.setPositiveButton("??????",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,	int which) {
						// Push: ????tag???��??
						List<String> tags = Utils.getTagsList(textviewGid.getText().toString());
						PushManager.setTags(getApplicationContext(), tags);
					}

				});
		builder.show();
	}

	private void initWithApiKey() {
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, 
				Utils.getMetaValue(PushDemoActivity.this, "api_key"));
	}

	private void initWithBaiduAccount() {
		if (isLogin) {
			CookieSyncManager.createInstance(getApplicationContext());
			CookieManager.getInstance().removeAllCookie();
			CookieSyncManager.getInstance().sync();

			isLogin = false;
			initButton.setText("?????????????Channel");
		}
		Intent intent = new Intent(PushDemoActivity.this,
				LoginActivity.class);
		startActivity(intent);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		updateDisplay();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		String action = intent.getAction();

		if (Utils.ACTION_LOGIN.equals(action)) {
			String accessToken = intent
					.getStringExtra(Utils.EXTRA_ACCESS_TOKEN);
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_ACCESS_TOKEN, accessToken);
			
			isLogin = true;
			initButton.setText("???????");
		}
			
		updateDisplay();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		Utils.setLogText(getApplicationContext(), Utils.logStringCache);
		super.onDestroy();
	}

	private void updateDisplay() {
		Log.d(TAG, "updateDisplay, logText:" + logText + " cache: " + Utils.logStringCache);
		if (logText != null) {
			logText.setText(Utils.logStringCache);
		}
		if (scrollView != null) {
			scrollView.fullScroll(ScrollView.FOCUS_DOWN);
		}
	}

}
