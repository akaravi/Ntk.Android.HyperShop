package ntk.android.academy.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import org.greenrobot.eventbus.EventBus;

import ntk.android.academy.event.EvDownload;

public class SeDownload extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(new EvDownload(true));
    }
}
