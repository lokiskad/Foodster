package com.example.brian.foodsterredesign1;

import android.util.Log;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Brian on 16.11.2017.
 */

public class StaticElements {
    public static final String FRIENDLY_MSG_LENGTH = "friendly_msg_length";

    private static final String TAG = "MyFMService";

    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
    }
    private static final String FRIENDLY_ENGAGE_TOPIC = "friendly_engage";

    /**
     * The Application's current Instance ID token is no longer valid and thus a new one must be requested.
     */
    public void onTokenRefresh() {
    }
}
