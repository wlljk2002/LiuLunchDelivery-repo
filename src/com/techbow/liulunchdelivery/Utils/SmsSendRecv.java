package com.techbow.liulunchdelivery.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.techbow.liulunchdelivery.ActivityRegister;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SmsSendRecv {
	/** 发送与接收的广播 **/
    private static String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private static String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    private static Uri SMS_INBOX = Uri.parse("content://sms/");
    private boolean isRegistered = false;
    private Context context;
    private BroadcastReceiver sendMessage;
    private BroadcastReceiver receiver;
    private Handler smsHandler;
    private SmsObserver smsObserver;
    
    public SmsSendRecv(Context context) {
    	super();
    	this.context = context;
    	sendMessage = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 判断短信是否发送成功
                switch (getResultCode()) {
                case Activity.RESULT_OK:
                    //Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "Get ID code fail, please check if the phone number is correct...", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        };
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 表示对方成功收到短信
                //Toast.makeText(context, "对方接收成功", Toast.LENGTH_LONG).show();
            	Toast.makeText(context, "ID code has been requested, please check coming message...",  Toast.LENGTH_LONG).show();
            }
        };
        smsHandler = new MyHandler();
    }
    
    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	Log.w("sms", "contentobserver handler");
        }
    }

    /**
     * 实现发送短信
     * @param context 
     * @param text 短信的内容
     * @param phoneNumber 手机号码
     */
    public void sendMessage(String text, String phoneNumber) {
    	if (!isRegistered) {
	        context.registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
	        context.registerReceiver(receiver, new IntentFilter(DELIVERED_SMS_ACTION));
	        smsObserver = new SmsObserver(context, smsHandler);
	        context.getContentResolver().registerContentObserver(SMS_INBOX, true, smsObserver);
	        isRegistered = true;
    	}
        
        // create the sentIntent parameter  
        Intent sentIntent = new Intent(SENT_SMS_ACTION);  
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent,0);
        // create the deilverIntent parameter  
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);  
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,deliverIntent, 0); 
        
        SmsManager smsManager = SmsManager.getDefault();
        //如果字数超过5,需拆分成多条短信发送
        if (text.length() > 70 ) {
            ArrayList<String> msgs = smsManager.divideMessage(text);
            for (String msg : msgs) {
                smsManager.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);                        
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI);
        }
    }

    public void deregisterAllReceiver() {
    	if (isRegistered) {
    		isRegistered = false;
    		context.unregisterReceiver(sendMessage);
    		context.unregisterReceiver(receiver);
    		context.getContentResolver().unregisterContentObserver(smsObserver);
    		Log.w("sms", "deregister done");
    	}
    }
    
    private class SmsObserver extends ContentObserver {
        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // 每当有新短信到来时，使用我们获取短消息的方法
            getSmsFromPhone();
        }
    }
    private void getSmsFromPhone() {
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] { "body","address","person"};// "_id", "address",
                                                        // "person",, "date",
                                                        // "type
        String where = " date >  "
                + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur)
            return;
        if (cur.moveToNext()) {
            //String number = cur.getString(cur.getColumnIndex("address"));// 手机号
            //String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));
            String code = body.substring(8, 12);
            
            // 这里我是要获取自己短信服务号码中的验证码~~
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]{4}");
            Matcher matcher = pattern.matcher(code);//String body="测试验证码2346ds";
            if (matcher.find()) {
            	((ActivityRegister)context).editCode.setText(code);
            }
        }
    }

}
