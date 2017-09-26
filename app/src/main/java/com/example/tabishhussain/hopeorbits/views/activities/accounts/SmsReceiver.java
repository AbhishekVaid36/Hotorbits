package com.example.tabishhussain.hopeorbits.views.activities.accounts;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.tabishhussain.hopeorbits.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swarajpal on 19-04-2016.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;
    Context c;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        c = context;
        Bundle data = intent.getExtras();
        checkAndRequestPermissions();

        Object[] pdus = (Object[]) data.get("pdus");

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            Toast.makeText(context, sender, Toast.LENGTH_LONG).show();
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

            } else {
                Toast.makeText(context, "Permision not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkAndRequestPermissions() {
        int recievesms = ContextCompat.checkSelfPermission(c, android.Manifest.permission.RECEIVE_SMS);
        int readsms = ContextCompat.checkSelfPermission(c, android.Manifest.permission.READ_SMS);
        int sendsms = ContextCompat.checkSelfPermission(c, android.Manifest.permission.SEND_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (recievesms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
        }
        if (readsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }
        if (sendsms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) c, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
