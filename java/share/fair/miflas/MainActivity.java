package share.fair.miflas;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    public static final String DAY_MAP ="dayMap";
    public static final String SCHEDULE ="schedule";
    public static final String WEEKENDS ="weekends";
    public static final String WEEKLY ="weekly";


    public static final String NO_PERSON ="---";
    public static final String REQUESTED ="requested";

    public static final String REPLACE_BT ="REPLACE";
    public static final String UNDO_BT ="UNDO";





    public static final int REQUEST_COLOR = R.color.orange;
    public static final int REPLACED_COLOR = R.color.green;
    public static final int REVOKED_COLOR = R.color.darkpurple;
    public static final int REGULAR_COLOR = R.color.blue;

//    public static final String firebaseURL = "https://miflas.firebaseio.com/";

    public static final String firebaseURLNEW= "https://miflas-a92a9.firebaseio.com/\"";

    DatabaseReference myFirebaseRef;
    FirebaseAuth myFirebaseAuth;
    TextView tvTitle;
    EditText etName;
    CalendarView calView;
    public static String myName;
//    String myEmail;
//    MiflasUser myUser;
    String myMail;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Firebase.setAndroidContext(this);
        myFirebaseRef = FirebaseDatabase.getInstance().getReference();
        myFirebaseAuth = FirebaseAuth.getInstance();
        Log.d("notes", "initialized ref to firebase: logged USER IS: "+myFirebaseAuth.getCurrentUser().getEmail().toString());
        tvTitle = (TextView) findViewById(R.id.tvTitle);

//        myMail = myFirebaseRef.getAuth().getUid().toString();
        myMail = myFirebaseAuth.getCurrentUser().getEmail();
//        loadWeekendSchedule();
//        loadWeeklySchedule();

        myFirebaseRef.child(SignUpActivity.USERS).child(SignUpActivity.MAIL).child(SignUpActivity.vanishDots(myMail)).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.d("notes", "user val is: " + dataSnapshot.getValue());

//                myUser = new MiflasUser((HashMap) snapshot.getValue());
//                myEmail = myUser.getEmail();
                myName = dataSnapshot.getValue().toString();

                Log.d("notes", "inside id: " + myMail + " name: " + myName + " email: " + myMail);
                tvTitle.setText("Welcome, " + myName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("notes", "The read failed: " + databaseError.getMessage());
            }
        });

        etName = (EditText) findViewById(R.id.etName);
        calView = (CalendarView) findViewById(R.id.calendarView);

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Log.d("notes", "y: " + year + " m: " + month + " d: " + dayOfMonth);
//                Toast.makeText(getApplicationContext(), "Go  into day info:" + "y: " + year + " m: " + month + " d: " + dayOfMonth, Toast.LENGTH_LONG).show();
                Intent dayInfo = new Intent(getApplicationContext(), DayInfoActivity.class);
                dayInfo.putExtra("year", year);
                dayInfo.putExtra("month", month);
                dayInfo.putExtra("dayOfMonth",dayOfMonth);

                startActivity(dayInfo);
            }
        });
        Button subscribeButton = (Button) findViewById(R.id.subscribeButton);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RemoteMessage rm = new RemoteMessage();
//                PushService.sendPushNotification();
                String SENDER_ID ="feaAGyRiguk:APA91bEc5nVLbdayHfRU543NrAM8JxQTuISsyS-_yuDSb2eqik5CTfCOKAjGnoj9c6Aa0bSjPEci59PA0ULr6yopiOohdC_TM0CcsAW9bM59SamuLxcLDGvCd7q1oUuXIOvA4oe9h2n6" ;
                FirebaseMessaging.getInstance().send(
                        new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                                .setMessageId("id1")
                                .addData("key", "value")
                                .build());
                Log.d("notes","subsribe presesd,after rm sent");

            }
        });

        Button logTokenButton = (Button) findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("notes", "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
//                new MyFirebaseMessagingService().sendNotificationPublic("You shall not pass!");
            }
        });
    }
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else
        if(id == R.id.action_log_in){
            myFirebaseAuth.signOut();
            //update the Shared Preferences that you are logged in:
            SharedPreferences settings = getSharedPreferences(SignUpActivity.PREFS_NAME, 0); // 0 - for private mode
            final SharedPreferences.Editor editor = settings.edit();
            //Set "hasLoggedIn" to true
            editor.putBoolean(SignUpActivity.HAS_LOGGED_IN, false);
            editor.putString(SignUpActivity.NAME_OF_USER, "no one");
            editor.putString(SignUpActivity.MAIL_OF_USER, "no one");
            editor.commit();
           Intent logInAct = new Intent(getApplicationContext(),LoginInActivity.class);
            startActivity(logInAct);
            finish();
        }else
        if(id == R.id.action_log_out){
            //log out
            myFirebaseAuth.signOut();
            SharedPreferences settings = getSharedPreferences(SignUpActivity.PREFS_NAME, 0); // 0 - for private mode
            final SharedPreferences.Editor editor = settings.edit();
            //Set "hasLoggedIn" to true
            editor.putBoolean(SignUpActivity.HAS_LOGGED_IN, false);
            editor.putString(SignUpActivity.NAME_OF_USER, "no one");
            editor.putString(SignUpActivity.MAIL_OF_USER, "no one");
            editor.commit();
            Intent logInAct = new Intent(getApplicationContext(),LoginInActivity.class); // change to signIn?
            startActivity(logInAct);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadWeeklySchedule(){

//        Collections.addAll(MiflasNames, "Ori Sharabi", "Avera Tzagay", "Eliav Shames", "Eliya Dahan",
//                "David Freud", "Avi Korzec", "Dana Yona"
//                , "Tomer Cohen", "Alex Pinkas", "Yaniv Alter", "Naira Musilian", "Ehud Klein",
//                "Lital David", "Nevo Fishbine", "Tzlil Avraham",
//                "Yacov Pariente", "Baruch Mekonen", "Igor Lapshun",
//                "Yaara Zohar", "Yoni Asulin");

        ArrayList<String> day1 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.BaruchMekonen, NamePickerActivity.IgorLapshun, NamePickerActivity.YanivAlter,
                        NamePickerActivity.YacovPariente,NamePickerActivity.TzlilAvraham));

        myFirebaseRef.child("schedule").child("weekly").child("1").setValue(day1);

        ArrayList<String> day2 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.DavidFreud, NamePickerActivity.EhudKlein, NamePickerActivity.TomerCohen, NamePickerActivity.EliavShames,
                        NamePickerActivity.YoniAsulin));

        myFirebaseRef.child("schedule").child("weekly").child("2").setValue(day2);

        ArrayList<String> day3 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.AlexPinkas, NamePickerActivity.DanaYona, NamePickerActivity.LitalDavid, NamePickerActivity.AviKorzec
                        ,NamePickerActivity.YaaraZohar));

        myFirebaseRef.child("schedule").child("weekly").child("3").setValue(day3);

        ArrayList<String> day4 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.AveraTzagay, NamePickerActivity.NairaMusilian, NamePickerActivity.NevoFishbine, NamePickerActivity.EliyaDahan,
                        NamePickerActivity.OriSharabi));

        myFirebaseRef.child("schedule").child("weekly").child("4").setValue(day4);

    }
//    private void loadWeekendSchedule(){
//
//        myFirebaseRef.child("try2").setValue("hellooooo");
//        HashMap<String,Object> map= new HashMap<String,Object>();
////        map.put("str1", "stringgg");
//
//        //28-30.4 hol
//
//
//        ArrayList<String> weekend28_4_16 = new ArrayList<String>(
//                Arrays.asList(NamePickerActivity.NevoFishbine, NamePickerActivity.TzlilAvraham,NamePickerActivity.YacovPariente, NamePickerActivity.EliyaDahan, "---"));
//
//        myFirebaseRef.child("scedule").child("weekends").child("28 04 16").child("duty").setValue(weekend28_4_16);
//        myFirebaseRef.child("scedule").child("weekends").child("28 04 16").child("actual").setValue(weekend28_4_16);
//
//        myFirebaseRef.child("scedule").child("weekends").child("28 04 16").child("replacements").setValue(map,new Firebase.CompletionListener() {
//            @Override
//            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
//                if(firebaseError!= null){
//                Log.d("notes","FB2 ERROR: "+firebaseError.getMessage());
//                }
//            }
//        });
//
//        myFirebaseRef.child("scedule").child("weekends").child("29 04 16").child("duty").setValue(weekend28_4_16);
//        myFirebaseRef.child("scedule").child("weekends").child("29 04 16").child("actual").setValue(weekend28_4_16);
//        myFirebaseRef.child("scedule").child("weekends").child("29 04 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("30 04 16").child("duty").setValue(weekend28_4_16);
//        myFirebaseRef.child("scedule").child("weekends").child("30 04 16").child("actual").setValue(weekend28_4_16);
//        myFirebaseRef.child("scedule").child("weekends").child("30 04 16").child("replacements").setValue(new HashMap<String,String>());
//
//
//        //1-3.5 hol
//        ArrayList<String> weekend1_5_16 = new ArrayList<String>(
//                Arrays.asList(NamePickerActivity.BaruchMekonen, NamePickerActivity.AlexPinkas,NamePickerActivity.IgorLapshun, NamePickerActivity.YacovPariente, "---"));
//
//
//
//        myFirebaseRef.child("scedule").child("weekends").child("01 05 16").child("duty").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("01 05 16").child("actual").setValue(weekend1_5_16);
//
//
//        //tried the semaphore to fix the fact
//        myFirebaseRef.child("scedule").child("weekends").child("01 05 16").child("replacements").setValue(new HashMap<String, String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("02 05 16").child("duty").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("02 05 16").child("actual").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("02 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("03 05 16").child("duty").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("03 05 16").child("actual").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("03 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        //10-14.5.16 hol
//        ArrayList<String> weekend10_5_16 = new ArrayList<String>(
//                Arrays.asList(NamePickerActivity.BaruchMekonen, NamePickerActivity.YoniAsulin,NamePickerActivity.YaaraZohar, NamePickerActivity.EhudKlein, "---"));
//
//        myFirebaseRef.child("scedule").child("weekends").child("10 05 16").child("duty").setValue(weekend10_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("10 05 16").child("actual").setValue(weekend10_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("10 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("11 05 16").child("duty").setValue(weekend10_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("11 05 16").child("actual").setValue(weekend10_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("11 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("12 05 16").child("duty").setValue(weekend10_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("12 05 16").child("actual").setValue(weekend10_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("12 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("13 05 16").child("duty").setValue(weekend10_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("13 05 16").child("actual").setValue(weekend10_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("13 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("14 05 16").child("duty").setValue(weekend10_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("14 05 16").child("actual").setValue(weekend10_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("14 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//
//        //5-6.6.16 hol
//        ArrayList<String> weekend5_6_16 = new ArrayList<String>(
//                Arrays.asList(NamePickerActivity.TomerCohen, NamePickerActivity.DanaYona,NamePickerActivity.EliavShames, NamePickerActivity.TzlilAvraham, "---"));
//
//        myFirebaseRef.child("scedule").child("weekends").child("05 06 16").child("duty").setValue(weekend5_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("05 06 16").child("actual").setValue(weekend5_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("05 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("06 06 16").child("duty").setValue(weekend5_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("06 06 16").child("actual").setValue(weekend5_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("06 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//
//        //9-12.6.16 hol
//        ArrayList<String> weekend9_6_16 = new ArrayList<String>(
//                Arrays.asList(NamePickerActivity.IgorLapshun, NamePickerActivity.YaaraZohar,NamePickerActivity.YoniAsulin, NamePickerActivity.DavidFreud, "---"));
//
//        myFirebaseRef.child("scedule").child("weekends").child("09 06 16").child("duty").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("09 06 16").child("actual").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("09 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("10 06 16").child("duty").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("10 06 16").child("actual").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("10 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//
//        myFirebaseRef.child("scedule").child("weekends").child("11 06 16").child("duty").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("11 06 16").child("actual").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("11 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//
//        myFirebaseRef.child("scedule").child("weekends").child("12 06 16").child("duty").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("12 06 16").child("actual").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("12 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("13 06 16").child("duty").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("13 06 16").child("actual").setValue(weekend1_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("13 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//        //5-7.5
//        ArrayList<String> weekend5_5_16 = new ArrayList<String>(
//                Arrays.asList(NamePickerActivity.YanivAlter, NamePickerActivity.EliyaDahan,NamePickerActivity.YacovPariente, NamePickerActivity.DavidFreud, "---"));
//
//        myFirebaseRef.child("scedule").child("weekends").child("05 05 16").child("duty").setValue(weekend5_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("05 05 16").child("actual").setValue(weekend5_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("05 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("06 05 16").child("duty").setValue(weekend5_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("06 05 16").child("actual").setValue(weekend5_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("06 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("07 05 16").child("duty").setValue(weekend5_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("07 05 16").child("actual").setValue(weekend5_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("07 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        //19-21.5
//        ArrayList<String> weekend19_5_16 = new ArrayList<String>(
//                Arrays.asList(NamePickerActivity.AviKorzec, NamePickerActivity.TomerCohen,NamePickerActivity.DanaYona, NamePickerActivity.OriSharabi, "---"));
//
//        myFirebaseRef.child("scedule").child("weekends").child("19 05 16").child("duty").setValue(weekend19_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("19 05 16").child("actual").setValue(weekend19_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("19 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("20 05 16").child("duty").setValue(weekend19_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("20 05 16").child("actual").setValue(weekend19_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("20 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("21 05 16").child("duty").setValue(weekend19_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("21 05 16").child("actual").setValue(weekend19_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("21 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        //26-28.5
//        ArrayList<String> weeken26_5_16 = new ArrayList<String>(
//                Arrays.asList(NamePickerActivity.TzlilAvraham, NamePickerActivity.YoniAsulin,NamePickerActivity.AlexPinkas, NamePickerActivity.NairaMusilian, "---"));
//
//        myFirebaseRef.child("scedule").child("weekends").child("26 05 16").child("duty").setValue(weeken26_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("26 05 16").child("actual").setValue(weeken26_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("26 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//
//        myFirebaseRef.child("scedule").child("weekends").child("27 05 16").child("duty").setValue(weeken26_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("27 05 16").child("actual").setValue(weeken26_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("27 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("28 05 16").child("duty").setValue(weeken26_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("28 05 16").child("actual").setValue(weeken26_5_16);
//        myFirebaseRef.child("scedule").child("weekends").child("28 05 16").child("replacements").setValue(new HashMap<String,String>());
//
//
//        //2-4.6.16
//        ArrayList<String> weekend2_6_16 = new ArrayList<String>(
//                Arrays.asList(NamePickerActivity.EhudKlein, NamePickerActivity.YaaraZohar,NamePickerActivity.EliavShames, NamePickerActivity.NevoFishbine, "---"));
//
//        myFirebaseRef.child("scedule").child("weekends").child("02 06 16").child("duty").setValue(weekend2_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("02 06 16").child("actual").setValue(weekend2_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("02 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("03 06 16").child("duty").setValue(weekend2_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("03 06 16").child("actual").setValue(weekend2_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("03 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("04 06 16").child("duty").setValue(weekend2_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("04 06 16").child("actual").setValue(weekend2_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("04 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//
//        //16-18.6
//        ArrayList<String> weekend16_6_16 = new ArrayList<String>(
//                Arrays.asList(NamePickerActivity.BaruchMekonen, NamePickerActivity.LitalDavid,NamePickerActivity.AveraTzagay, NamePickerActivity.IgorLapshun, "---"));
//
//        myFirebaseRef.child("scedule").child("weekends").child("16 06 16").child("duty").setValue(weekend16_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("16 06 16").child("actual").setValue(weekend16_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("16 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("17 06 16").child("duty").setValue(weekend16_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("17 06 16").child("actual").setValue(weekend16_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("17 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//        myFirebaseRef.child("scedule").child("weekends").child("18 06 16").child("duty").setValue(weekend16_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("18 06 16").child("actual").setValue(weekend16_6_16);
//        myFirebaseRef.child("scedule").child("weekends").child("18 06 16").child("replacements").setValue(new HashMap<String,String>());
//
//    }
//

    private void loadWeekendSchedule(){

        //28-30.4 hol

        ArrayList<String> weekend28_4_16 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.NevoFishbine, NamePickerActivity.TzlilAvraham,NamePickerActivity.YacovPariente, NamePickerActivity.EliyaDahan, "---"));
        addWeekendToFirebase("28 04 16",weekend28_4_16);
        addWeekendToFirebase("29 04 16",weekend28_4_16);
        addWeekendToFirebase("30 04 16",weekend28_4_16);


        //1-3.5 hol
        ArrayList<String> weekend1_5_16 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.BaruchMekonen, NamePickerActivity.AlexPinkas,NamePickerActivity.IgorLapshun, NamePickerActivity.YacovPariente, "---"));

        addWeekendToFirebase("01 05 16",weekend1_5_16);
        addWeekendToFirebase("02 05 16",weekend1_5_16);
        addWeekendToFirebase("03 05 16",weekend1_5_16);

        //10-14.5.16 hol
        ArrayList<String> weekend10_5_16 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.BaruchMekonen, NamePickerActivity.YoniAsulin,NamePickerActivity.YaaraZohar, NamePickerActivity.EhudKlein, "---"));

        addWeekendToFirebase("10 05 16",weekend10_5_16);
        addWeekendToFirebase("11 05 16",weekend10_5_16);
        addWeekendToFirebase("12 05 16",weekend10_5_16);
        addWeekendToFirebase("13 05 16",weekend10_5_16);
        addWeekendToFirebase("14 05 16",weekend10_5_16);


        //5-6.6.16 hol
        ArrayList<String> weekend5_6_16 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.TomerCohen, NamePickerActivity.DanaYona,NamePickerActivity.EliavShames, NamePickerActivity.TzlilAvraham, "---"));

        addWeekendToFirebase("05 06 16",weekend5_6_16);
        addWeekendToFirebase("06 06 16",weekend5_6_16);


        //9-12.6.16 hol
        ArrayList<String> weekend9_6_16 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.IgorLapshun, NamePickerActivity.YaaraZohar,NamePickerActivity.YoniAsulin, NamePickerActivity.DavidFreud, "---"));
        addWeekendToFirebase("09 06 16",weekend9_6_16);
        addWeekendToFirebase("10 06 16",weekend9_6_16);
        addWeekendToFirebase("11 06 16",weekend9_6_16);
        addWeekendToFirebase("12 06 16",weekend9_6_16);

        //5-7.5
        ArrayList<String> weekend5_5_16 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.YanivAlter, NamePickerActivity.EliyaDahan,NamePickerActivity.YacovPariente, NamePickerActivity.DavidFreud, "---"));

        addWeekendToFirebase("05 05 16", weekend5_5_16);
        addWeekendToFirebase("06 05 16",weekend5_5_16);
        addWeekendToFirebase("07 05 16",weekend5_5_16);

        //19-21.5
        ArrayList<String> weekend19_5_16 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.AviKorzec, NamePickerActivity.TomerCohen,NamePickerActivity.DanaYona, NamePickerActivity.OriSharabi, "---"));

        addWeekendToFirebase("19 05 16", weekend19_5_16);
        addWeekendToFirebase("20 05 16",weekend19_5_16);
        addWeekendToFirebase("21 05 16",weekend19_5_16);

        //26-28.5
        ArrayList<String> weekend26_5_16 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.TzlilAvraham, NamePickerActivity.YoniAsulin,NamePickerActivity.AlexPinkas, NamePickerActivity.NairaMusilian, "---"));

        addWeekendToFirebase("26 05 16", weekend26_5_16);
        addWeekendToFirebase("27 05 16",weekend26_5_16);
        addWeekendToFirebase("28 05 16",weekend26_5_16);


        //2-4.6.16
        ArrayList<String> weekend2_6_16 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.EhudKlein, NamePickerActivity.YaaraZohar,NamePickerActivity.EliavShames, NamePickerActivity.NevoFishbine, "---"));

        addWeekendToFirebase("02 06 16",weekend2_6_16);
        addWeekendToFirebase("03 06 16", weekend2_6_16);
        addWeekendToFirebase("04 06 16",weekend2_6_16);


        //16-18.6
        ArrayList<String> weekend16_6_16 = new ArrayList<String>(
                Arrays.asList(NamePickerActivity.BaruchMekonen, NamePickerActivity.LitalDavid,NamePickerActivity.AveraTzagay, NamePickerActivity.IgorLapshun, "---"));

        addWeekendToFirebase("16 06 16",weekend16_6_16);
        addWeekendToFirebase("17 06 16",weekend16_6_16);
        addWeekendToFirebase("18 06 16",weekend16_6_16);

    }

    public static HashMap<String,Object> addWeekendToFirebase(String date, ArrayList<String> names){
        //todo: what if names is null
        Log.d("notes","--called add weekend--");

        HashMap<String,Object> tmpMap = new HashMap<String,Object>();
        for(String name:names){
            tmpMap.put(name,NO_PERSON);
        }

//        Firebase myFirebaseRef2 = new Firebase("https://miflas.firebaseio.com/");
        DatabaseReference myFirebaseRef2 = FirebaseDatabase.getInstance().getReference();

        myFirebaseRef2.child(MainActivity.SCHEDULE).child(MainActivity.WEEKENDS).child(date).child(MainActivity.DAY_MAP).setValue(tmpMap); //todo: delete

        return tmpMap;
    }
}














//********
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
////        DatabaseReference myRef = database.getReference("message");
////        myRef.setValue("Hello, World!!!");
//        database.getReference().child("try1").setValue("try1!");
//        Log.d("notes", "called the new inserting to firebase");












