package com.example.coppyfolder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SercureReceive extends BroadcastReceiver {
    private final String ACTION_START_ACTIVITY="android.intent.action.startInAppActivity";
    @Override
    public void onReceive(Context context, Intent intent) {
        String ACTION_INTENT= intent.getAction();
        if(ACTION_INTENT.equals(ACTION_START_ACTIVITY)){
            Intent intent1= new Intent(context,inAppActivity.class);
            context.startActivity(intent1);
        }
    }
}
