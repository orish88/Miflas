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
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginInActivity extends AppCompatActivity {


    EditText etEmail;
    EditText etPassword;
    Button btLogin;
    Button btBack;
    DatabaseReference myFirebaseRef;
    FirebaseAuth myFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_dialog);

        Firebase.setAndroidContext(this);
        myFirebaseRef = FirebaseDatabase.getInstance().getReference();
        myFirebaseAuth = FirebaseAuth.getInstance();

        etEmail =(EditText) findViewById(R.id.et_email_login);
        etPassword= (EditText) findViewById(R.id.et_password_login);
        btLogin = (Button) findViewById(R.id.bt_login);
        btBack = (Button) findViewById(R.id.bt_back);



        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputEmail = etEmail.getText().toString();
                final String inputPassword = etPassword.getText().toString();

                if (!(inputEmail.isEmpty() || inputPassword.isEmpty() || !SignUpActivity.isValidEmailAddress(inputEmail) ) ) {//todo: check validation of email

                    //Log in:
                    myFirebaseAuth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("notes", "User: " + inputEmail + " has logged in");
                                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);

                                //update the Shared Preferences that you are logged in:
                                SharedPreferences settings = getSharedPreferences(SignUpActivity.PREFS_NAME, 0); // 0 - for private mode
                                final SharedPreferences.Editor editor = settings.edit();
                                //Set "hasLoggedIn" to true
                                editor.putBoolean(SignUpActivity.HAS_LOGGED_IN, true);
                                myFirebaseRef.child(SignUpActivity.USERS).child(SignUpActivity.MAIL).child(SignUpActivity.vanishDots(inputEmail)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot == null) {
                                            Log.d("notes", "null datasnapshot");
                                            return;
                                        }
                                        editor.putString(SignUpActivity.NAME_OF_USER, dataSnapshot.getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                editor.putString(SignUpActivity.MAIL_OF_USER, inputEmail);
                                // Commit the edits!
                                editor.commit();
                                startActivity(mainActivity);
                                finish();
                            } else {
                                Log.d("notes", "user " + inputEmail + " failed to log in");
                                Toast.makeText(getApplicationContext(), "Wrong email and or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    }
                }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        if(id == R.id.action_sign_up){
            myFirebaseAuth.signOut();
            //update the Shared Preferences that you are logged in:
            SharedPreferences settings = getSharedPreferences(SignUpActivity.PREFS_NAME, 0); // 0 - for private mode
            final SharedPreferences.Editor editor = settings.edit();
            //Set "hasLoggedIn" to true
            editor.putBoolean(SignUpActivity.HAS_LOGGED_IN, false);
            editor.putString(SignUpActivity.NAME_OF_USER, "no one");
            editor.putString(SignUpActivity.MAIL_OF_USER, "no one");

            editor.commit();
            Intent SignInAct = new Intent(getApplicationContext(),SignUpActivity.class);
            startActivity(SignInAct);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
