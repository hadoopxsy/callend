package com.example.callend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver
{
	private static final String ACTION_SMS_RECEIVE = "android.provider.Telephony.SMS_RECEIVED"; 
	
	@Override
	public void onReceive(Context arg0, Intent intent) 
	{
		String actionName = intent.getAction();
		if (actionName.equals(ACTION_SMS_RECEIVE))
		{
			StringBuffer SMSAddress = new StringBuffer();
			StringBuffer SMSContent = new StringBuffer();
			
			Bundle bundle = intent.getExtras();
			if (bundle != null) 
			{
				Object[] myOBJpdus = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[myOBJpdus.length];
				for (int i = 0; i < myOBJpdus.length; i++) 
				{
					messages[i] = SmsMessage
							.createFromPdu((byte[]) myOBJpdus[i]);
				}
				for (SmsMessage message : messages) 
		        {
		            SMSAddress.append(message  
		                    .getDisplayOriginatingAddress());  
		            SMSContent.append(message.getDisplayMessageBody());  
		            Log.v("jeden", "�յ��Ķ��ţ���"+"���ź��룺" + SMSAddress + "\n�������ݣ�"  
		                    + SMSContent);

	            	abortBroadcast();
		         }
			}
		}
	}
}
