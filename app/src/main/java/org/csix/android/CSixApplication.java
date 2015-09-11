package org.csix.android;

import android.app.Application;
import android.content.Intent;

import org.csix.android.services.AboutIntentService;
import org.csix.android.services.EventIntentService;
import org.csix.android.services.GroupIntentService;

public class CSixApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, EventIntentService.class));
        startService(new Intent(this, GroupIntentService.class));
        startService(new Intent(this, AboutIntentService.class));
    }
}
