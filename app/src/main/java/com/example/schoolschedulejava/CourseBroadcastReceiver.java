package com.example.schoolschedulejava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CourseBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent broadcastIntent = new Intent(context, CourseNotifyService.class);
        context.startService(broadcastIntent);
    }
}
