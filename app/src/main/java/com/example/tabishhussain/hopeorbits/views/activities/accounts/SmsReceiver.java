package com.example.tabishhussain.hopeorbits.views.activities.accounts;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.example.tabishhussain.hopeorbits.R;

/**
 * Created by swarajpal on 19-04-2016.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.
            if (sender.equalsIgnoreCase("DM-088689")) {
                String messageBody = smsMessage.getMessageBody();
                String otp = messageBody.substring(messageBody.length() - 6, messageBody.length());
                //Pass on the text to our listener.
                try {
                    // get running process
                    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
                    String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
                    PackageManager pm = context.getPackageManager();
                    PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
                    String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();
                    if (foregroundTaskAppName.equalsIgnoreCase(context.getString(R.string.app_name))) {
                        mListener.messageReceived(otp);
                    }

                } catch (Exception e) {

                }

            }

        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
