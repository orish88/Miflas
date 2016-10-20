package share.fair.miflas;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    DatabaseReference myFirebaseRef = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth myFirebaseAuth = FirebaseAuth.getInstance();
    private static final String TAG = "notes";
    static String refreshedToken;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        myFirebaseRef =  FirebaseDatabase.getInstance().getReference();

        myFirebaseRef.child(SignUpActivity.USERS).child(SignUpActivity.MAIL).
                child(SignUpActivity.vanishDots(myFirebaseAuth.getCurrentUser().getEmail())).setValue(refreshedToken);
        if(MainActivity.myName != null){
            Log.d("notes", "myName: "+MainActivity.myName);
            myFirebaseRef.child(SignUpActivity.USERS).child(SignUpActivity.NAMES).
                    child(MainActivity.myName).setValue(refreshedToken);
        }
        Log.d("notes", "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
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
        // Add custom implementation, as needed.
    }
}