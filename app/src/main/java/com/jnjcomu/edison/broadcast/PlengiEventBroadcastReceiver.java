package com.jnjcomu.edison.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PlengiEventBroadcastReceiver extends BroadcastReceiver {
    public static final String RECEIVER_ID = "com.jnjcomu.edison.cloud.response";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("place.name"), Toast.LENGTH_SHORT).show();
    }
}
