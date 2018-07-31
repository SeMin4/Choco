package com.example.xptmx.myapp1;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class CustomFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public static String refreshedToken;
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Sever_trans.getInstance().setDevice_id(CustomFirebaseInstanceIdService.refreshedToken);
        Sever_trans.getInstance().setUsername(CustomFirebaseInstanceIdService.refreshedToken);
        sendRegistrationToServer(refreshedToken);
//        getApplicationContext().sendBroadcast(new Intent("ABCDEFG"));


    }
    // [END refresh_token]


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        //FCM 토큰 갱신
    }
}
