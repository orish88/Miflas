package share.fair.miflas;

/**
 * Created by Ori on 7/1/2016.
 */

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nir on 19/02/2016.
 */
public class PushService {
    private static String APP_KEY = "AIzaSyCatjL_JhH-OTGFcfSLJkSEPCLpW_-fNmY";
    private static Context context;
    public static void init(Context other){
        context = other;
    }
// feaAGyRiguk:APA91bEc5nVLbdayHfRU543NrAM8JxQTuISsyS-_yuDSb2eqik5CTfCOKAjGnoj9c6Aa0bSjPEci59PA0ULr6yopiOohdC_TM0CcsAW9bM59SamuLxcLDGvCd7q1oUuXIOvA4oe9h2n6
    public static void sendPushNotification(final List<String> subscribers, final JSONObject content){
        for( String subscriber : subscribers){
            sendOnePush(subscriber,content);
        }
    }

    public static void sendOnePush(final String subscriberId, final JSONObject content){
        String url = "https://fcm.googleapis.com/fcm/send";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.w("custom", "can't get response");
                        Log.w("custom", error);
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("to",subscriberId);
                    jsonObject.put("data",content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + APP_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);
    }

    public static interface SubscribersCallback {
        public void processSubscribers(List<String> subscribers);
    }
}
