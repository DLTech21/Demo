package com.baidu.push.example;

import java.util.ArrayList;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SendSmsService extends IntentService {
	String SENT_SMS_ACTION = "SENT_SMS_ACTION";  
	public static String			UPLOAD_CLIENT = "pw.sms.service";
	private static final String ACTION_START_PAY = UPLOAD_CLIENT + ".START.PAY";
	private static final String	ACTION_STOP = UPLOAD_CLIENT + ".STOP";
	private static final String	ACTION_KEEPALIVE = UPLOAD_CLIENT + ".KEEP_ALIVE";
	
	private String smsMember ;
	private String smsBody;
	private SmsManager sManage; 
	public SendSmsService() {
		super(UPLOAD_CLIENT);
	}
	
	
	public static void actionStartPAY(Context ctx, String smsMember, String smsBody) {
		try{
			Intent i = new Intent(ctx, SendSmsService.class);
			i.setAction(ACTION_START_PAY);
			i.putExtra("smsMember", smsMember);
			i.putExtra("smsBody", smsBody);
			ctx.startService(i);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void actionStop(Context ctx) {
		try {
			Intent i = new Intent(ctx, SendSmsService.class);
			i.setAction(ACTION_STOP);
			ctx.stopService(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction().equals(ACTION_START_PAY) == true) {
				smsMember = intent.getStringExtra("smsMember");
				smsBody = intent.getStringExtra("smsBody");
				sManage = SmsManager.getDefault();  
				sendSMS();
		}
	}

	private void sendSMS() {
		try {
	            String number = smsMember;
	            Intent sentIntent = new Intent(SENT_SMS_ACTION); 
	        	PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, sentIntent,  0); 
	            sManage.sendTextMessage(number, null, smsBody, sentPI, null);  
		} catch (Exception e ) {
		}
	}
	
}
