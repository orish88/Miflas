package share.fair.miflas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

import share.fair.miflas.AdminStuff.DatabaseAdministrator;

public class NamePickerActivity extends AppCompatActivity {

    public static String OriSharabi ="Ori Sharabi";
    public static String AveraTzagay="Avera Tzagay";
    public static String EliavShames ="Eliav Shames";
    public static String EliyaDahan = "Eliya Dahan";
    public static String DavidFreud= "David Freud";
    public static String AviKorzec = "Avi Korzec";
    public static String DanaYona ="Dana Yona";
    public static String TomerCohen ="Tomer Cohen";
    public static String AlexPinkas= "Alex Pinkas";
    public static String YanivAlter ="Yaniv Alter";
    public static String NairaMusilian ="Naira Musilian";
    public static String EhudKlein= "Ehud Klein";
    public static String LitalDavid ="Lital David";
    public static String NevoFishbine ="Nevo Fishbine";
    public static String TzlilAvraham= "Tzlil Avraham";
    public static String YacovPariente = "Yacov Pariente";
    public static String BaruchMekonen ="Baruch Mekonen";
    public static String IgorLapshun ="Igor Lapshun";
    public static String YaaraZohar ="Yaara Zohar";
    public static String YoniAsulin ="Yoni Asulin";

    DatabaseReference myFirebaseRef;

    TextView tvNamePickerHeadline;
    Button btBackToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_picker);
        myFirebaseRef = FirebaseDatabase.getInstance().getReference();

        //one time:
        DatabaseAdministrator.loadNameStringsToList();
        btBackToSignUp = (Button) findViewById(R.id.bt_cancel);
        btBackToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("notes", "Cancel pressed");
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_name_picker, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
