package org.csix.android;

import android.app.Application;
import android.content.Intent;

public class CSixApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, EventIntentService.class));
        startService(new Intent(this, GroupIntentService.class));
        startService(new Intent(this, AboutIntentService.class));
    }
}
