package com.jeden.tel;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;

public class TelReceive extends BroadcastReceiver {

	private static boolean incomingFlag = false;
  
	private static String incoming_number = null;
  
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		//如果是拨打电话
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
        {                        
                incomingFlag = false;
                String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER); 
                Tool.BfLog("打了电话："+phoneNumber);
    	}
        else
    	{                        
            //如果是来电
            TelephonyManager tm = 
                (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);                        
            
            switch (tm.getCallState()) 
            {
	            case TelephonyManager.CALL_STATE_RINGING:
	                    incomingFlag = true;//标识当前是来电
	                    incoming_number = intent.getStringExtra("incoming_number");
	                    Tool.BfLog("打进来了电话："+incoming_number);
	                    SharedPreferences sp = context.getSharedPreferences("jeden_tel",
				    			MainActivity.MODE_PRIVATE);
	                    String tels = sp.getString("tels", null);
	                    if(tels != null)
	                    {
	                    	String telstemp[] = tels.split(",");
	                    	for(String temp:telstemp)
	                    	{
			                    if(temp.contains(incoming_number))  
			                    {
		                            stop(context, incoming_number);//拦截来电
		                            String hestory = sp.getString("hestory", null);
		                            if(hestory == null)
		                            {
		                            	hestory = "";
		                            }
		                            
		                            hestory = hestory + "\n 拦截到了电话：" + incoming_number;
		                            sp.edit().putString("hestory", hestory).commit();
		                            //abortBroadcast();//截断广播
			                    }
	                    		
	                    	}
	                    }
	                    break;
	            case TelephonyManager.CALL_STATE_OFFHOOK:                                
	                    if(incomingFlag)
	                    {
	                    	
	                    }
	                    break;
	            
	            case TelephonyManager.CALL_STATE_IDLE:                                
	                    if(incomingFlag)
	                    {       
	                    	
	                    }
	                    break;
            } 
        }
	}
	public void stop(Context context ,String incoming_number) 
	{ 
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);//静音处理
        ITelephony iTelephony = getITelephony(context); //获取电话接口
        try 
        {
        	iTelephony.endCall();//结束电话                    
        } catch (RemoteException e) 
        {                                
        	e.printStackTrace();
        }                    
        //再恢复正常铃声    
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);      
	}
	private static ITelephony getITelephony(Context context) 
	{
		ITelephony iTelephony = null;
		TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		try 
		{
			Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);
		    getITelephonyMethod.setAccessible(true);
		    iTelephony = (ITelephony) getITelephonyMethod.invoke(telephonyMgr, (Object[]) null);
		} catch (Exception e) 
		{
		      e.printStackTrace();
	    }
	    return iTelephony;
	}
}
