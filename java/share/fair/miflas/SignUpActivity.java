package share.fair.miflas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.client.AuthData;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String HAS_LOGGED_IN = "hasLoggedIn";
    public static final String NAME_OF_USER = "nameOfUser";
    public static final String MAIL_OF_USER = "mailOfUser";
    public static final String NO_ONE = "no one";
    public static final String USERS ="users";
    public static final String MAIL ="mail";
    public static final String NAMES ="names";


    private static final int NAME_PICKER_REQ_CODE = 1;
    DatabaseReference myFirebaseRef;
    FirebaseAuth myFirebaseAuth;


    //    RadioGroup rgSMiflasNameSelection;
    Button btNameDialog;
    EditText etEmail;
    EditText etPassword;
    Button btSignUp;
    String pickedName;
    TextView tvPickedName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_login);

        SharedPreferences settings = getSharedPreferences(SignUpActivity.PREFS_NAME, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean(HAS_LOGGED_IN, false);
        if(hasLoggedIn) {
            //Go directly to main activity
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
        }
        myFirebaseRef = FirebaseDatabase.getInstance().getReference();
        myFirebaseAuth = FirebaseAuth.getInstance();
        etEmail = (EditText) findViewById(R.id.et_email_login);
        etPassword = (EditText) findViewById(R.id.et_password);
        btSignUp = (Button) findViewById(R.id.bt_sign_up);
        tvPickedName =(TextView) findViewById(R.id.tv_picked_name);
        btNameDialog =(Button) findViewById(R.id.bt_choose_name_dialog);
        pickedName="";

        btNameDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent namePicker = new Intent(getApplicationContext(),NamePickerActivity.class);
                startActivityForResult(namePicker,NAME_PICKER_REQ_CODE);

            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("notes","pressed sign up pn:"+pickedName);
                final String inputEmail = etEmail.getText().toString();
                //todo: check if email is already on users/mail in the DB
                final String inputPassword = etPassword.getText().toString();

                if (!(inputEmail.isEmpty() || inputPassword.isEmpty() || pickedName.isEmpty())) {//todo: check validation of email
                    if (!(isValidEmailAddress(inputEmail))) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid email adress", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("notes","pressed sign up2");

                    myFirebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("notes", "succesfully signed up user: " + inputEmail);
//                                MiflasUser newUser = new MiflasUser(result.get("uid").toString(), pickedName, inputEmail);
                                myFirebaseRef.child(USERS).child(MAIL).child(vanishDots(inputEmail)).setValue(pickedName); //todo: miflas user?
                                myFirebaseRef.child(USERS).child(NAMES).child(pickedName).setValue(inputEmail);
                                myFirebaseAuth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("notes", "User: " + inputEmail + " has logged in");
                                            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);

                                            //update the Shared Preferences that you are logged in:
                                            SharedPreferences settings = getSharedPreferences(SignUpActivity.PREFS_NAME, 0); // 0 - for private mode
                                            SharedPreferences.Editor editor = settings.edit();
                                            //Set "hasLoggedIn" to true
                                            editor.putBoolean(HAS_LOGGED_IN, true);
                                            editor.putString(NAME_OF_USER, pickedName);
                                            editor.putString(MAIL_OF_USER, inputEmail);
                                            // Commit the edits!
                                            editor.commit();
                                            startActivity(mainActivity);
                                            finish();
                                        } else {
                                            Log.d("notes", "user " + inputEmail + " failed to log in");
                                        }
                                    }
                                });
                            } else {
                                Log.d("notes", "user " + inputEmail + " failed to sign up");
                                Toast.makeText(getApplicationContext(),"Failed to sign up.",Toast.LENGTH_SHORT).show();
                                if(inputPassword.length()<6){
                                    Toast.makeText(getApplicationContext(),"Password is too short(6+ characters).",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
//                    myFirebaseRef.createUser(inputEmail, inputPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
//                        @Override
//                        public void onSuccess(Map<String, Object> result) {
//                            System.out.println("Successfully created user account with uid: " + result.get("uid"));
//                            Log.d("notes", "Successfully created user account with uid: " + result.get("uid"));
//
////                            myFirebaseRef.child("loginMails").child(inputEmail).setValue(pickedName);
//                            myFirebaseRef.child("users").child(result.get("uid").toString()).child("name").setValue(pickedName);
//                            MiflasUser newUser = new MiflasUser(result.get("uid").toString(), pickedName, inputEmail);
//                            myFirebaseRef.child("users").child(result.get("uid").toString()).child("miflasUser").setValue(newUser);
//
//                            //Log in:
//                            myFirebaseRef.authWithPassword(inputEmail, inputPassword, new Firebase.AuthResultHandler() {
//                                @Override
//                                public void onAuthenticated(AuthData authData) {
//                                    Log.d("notes", "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
//                                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
//
//                                    //update the Shared Preferences that you are logged in:
//                                    SharedPreferences settings = getSharedPreferences(SignUpActivity.PREFS_NAME, 0); // 0 - for private mode
//                                    SharedPreferences.Editor editor = settings.edit();
//                                    //Set "hasLoggedIn" to true
//                                    editor.putBoolean("hasLoggedIn", true);
//                                    editor.putString("nameOfUser", pickedName);
//
//                                    // Commit the edits!
//                                    editor.commit();
//
//                                    // [START subscribe_topics]
////                                    FirebaseMessaging.getInstance().subscribeToTopic("news");
////                                    Log.d("notes", "Subscribed to news topic");
//                                    // [END subscribe_topics]
//
//                                    startActivity(mainActivity);
//                                    finish();
//                                }
//
//                                @Override
//                                public void onAuthenticationError(FirebaseError firebaseError) {
//                                    // there was an error
//                                }
//                            });
//
//
//                        }
//
//                        @Override
//                        public void onError(FirebaseError firebaseError) {
//                            // there was an error
//                            Log.d("notes", "sign in error: " + firebaseError.getMessage().toString());
//                        }
//                    });
//                    Log.d("notes", "after sign in");
//
//
//                }
//            }
//        });
                }else{
                    Toast.makeText(getApplicationContext(),"Pick your name, and fill an email and a password.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("notes", "pressed sign up3");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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
            editor.putBoolean(HAS_LOGGED_IN, false);
            editor.putString(NAME_OF_USER, NO_ONE);
            editor.putString(MAIL_OF_USER, NO_ONE);
            editor.commit();
            Intent logInAct = new Intent(getApplicationContext(),LoginInActivity.class);
            startActivity(logInAct);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }






//        for (int row = 0; row < 1; row++) {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NAME_PICKER_REQ_CODE){
            if(resultCode == RESULT_OK){
                pickedName= data.getStringExtra("name");
                Log.d("notes","picked name in login: "+pickedName);
                tvPickedName.setText(pickedName);

            }


    }
//            RadioGroup ll = new RadioGroup(this);
//            ll.setOrientation(LinearLayout.HORIZONTAL);
//
//            for (int i = 1; i <= number; i++) {
//                RadioButton rdbtn = new RadioButton(this);
//                rdbtn.setId((row * 2) + i);
//                rdbtn.setText("Radio " + rdbtn.getId());
//                ll.addView(rdbtn);
//            }
//            ((ViewGroup) findViewById(R.id.radiogroup)).addView(ll);
//        }

    }
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public static String vanishDots(String str){
        return str.replace(".","");
    }

}
