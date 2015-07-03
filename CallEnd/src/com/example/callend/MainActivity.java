package com.example.callend;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.android.internal.telephony.ITelephony;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MainActivity extends Activity
{
	// ��¼�������ļ���  
    ArrayList<String> blockList = new ArrayList<String>();  
    // �����绰����������  
    TelephonyManager tManager;  
    // ��������ͨ��״̬�ļ�����  
    CustomPhoneCallListener cpListener;  
  
    public class CustomPhoneCallListener extends PhoneStateListener
    {  
        @Override  
        public void onCallStateChanged(int state, String incomingNumber) 
        {  
            switch (state)
            {  
            case TelephonyManager.CALL_STATE_IDLE:  
                break;  
            case TelephonyManager.CALL_STATE_OFFHOOK:  
                break;  
            case TelephonyManager.CALL_STATE_RINGING:  
            	Log.v("jeden", "���绰��");
                // ����ú������ں�����  
                if (isBlock(incomingNumber))
                {  
                    try 
                    {  
                        Method method = Class.forName(  
                                "android.os.ServiceManager").getMethod(  
                                "getService", String.class);  
                        // ��ȡԶ��TELEPHONY_SERVICE��IBinder����Ĵ���  
                        IBinder binder = (IBinder) method.invoke(null,  
                                new Object[] { TELEPHONY_SERVICE });  
                        // ��IBinder����Ĵ���ת��ΪITelephony����  
                        ITelephony telephony = ITelephony.Stub  
                                .asInterface(binder);  
                        // �Ҷϵ绰  
                        telephony.endCall(); 
                        Log.v("jeden", "���سɹ�:"+incomingNumber);
                    } 
                    catch (Exception e) 
                    {  
                        e.printStackTrace();  
                    }  
  
                }  
                break;  
            }  
            super.onCallStateChanged(state, incomingNumber);  
        }  
    }  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// ��ʼ������������  
        initBlockList();  
        // ��ȡϵͳ��TelephonyManager������  
        tManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);  
        cpListener = new CustomPhoneCallListener();  
        // ͨ��TelephonyManager����ͨ��״̬�ĸı�  
        tManager.listen(cpListener, PhoneStateListener.LISTEN_CALL_STATE); 
	}
	
	/** 
     * ��ʼ������������ 
     */  
    private void initBlockList()
    {  
        blockList.add("18321380268");  
        blockList.add("15555215554");  
        blockList.add("15680768383");  
        blockList.add("15680768284");  
        blockList.add("15680768385");  
        blockList.add("15680768386");  
        blockList.add("15680768387");  
        blockList.add("15680768388");  
        blockList.add("15680768389");  
    }  
    
	/** 
     * �ж�ĳ���绰�����Ƿ��ں�����֮�� 
     *  
     * @param phone 
     *            ������� 
     * @return 
     */  
    public boolean isBlock(String phone)
    {  
        for (String s1 : blockList) 
        {  
            if (s1.equals(phone))
            {  
                return true;  
            }  
        }  
        return false;  
    } 
}
