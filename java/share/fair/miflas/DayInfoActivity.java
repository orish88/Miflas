package share.fair.miflas;

//import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import android.view.View.OnClickListener;

public class DayInfoActivity extends AppCompatActivity implements OnClickListener {


    //todo: make a globsl map,save the data lisetener snapshot in there, on data change. and that's it,
    //todo: no other data changes.
    int year;
    int month;
    int dayOfMonth;
    TextView tvDayTitle;
    DatabaseReference myFirebaseRef;
    FirebaseAuth myFirebaseAuth;
    String myName;
    ArrayList<String> arrListWeeklyDayNames = new ArrayList<String>();
    ArrayList<String> arrListWeekendDayNames;
    int dayOfWeek;
    String dateToCheckStr;
    String myMail;

    HashMap<String,Object> dayMap = new HashMap<String,Object>();

    Button btReplace1;
    Button btReplace2;
    Button btReplace3;
    Button btReplace4;
    Button btReplace5;
    TextView tvReplacement1;
    TextView tvReplacement2;
    TextView tvReplacement3;
    TextView tvReplacement4;
    TextView tvReplacement5;

    TextView tvDuty1;
    TextView tvDuty2;
    TextView tvDuty3;
    TextView tvDuty4;
    TextView tvDuty5;

    Switch switchRequesrReplacment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("notes", "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_info);

        myFirebaseRef = FirebaseDatabase.getInstance().getReference();
        myFirebaseAuth = FirebaseAuth.getInstance();

        year = this.getIntent().getIntExtra("year",0);
        month = this.getIntent().getIntExtra("month",0);
        dayOfMonth = this.getIntent().getIntExtra("dayOfMonth",0);
        tvDayTitle =(TextView) findViewById(R.id.tv_day_title);
        Date date = getDateFromInfo(year, month, dayOfMonth);
        Log.d("notes", "date: " + date.toString());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String dateStr =new SimpleDateFormat("EE  dd/MM/yyyy").format(date);
        dateToCheckStr =new SimpleDateFormat("dd MM yy").format(date);

        btReplace1 = (Button) findViewById(R.id.bt_replace1);
        btReplace2 = (Button) findViewById(R.id.bt_replace2);
        btReplace3 = (Button) findViewById(R.id.bt_replace3);
        btReplace4 = (Button) findViewById(R.id.bt_replace4);
        btReplace5 = (Button) findViewById(R.id.bt_replace5);

        btReplace1.setOnClickListener(this);
        btReplace2.setOnClickListener(this);
        btReplace3.setOnClickListener(this);
        btReplace4.setOnClickListener(this);
        btReplace5.setOnClickListener(this);


        tvReplacement1 = (TextView) findViewById(R.id.tv_replacement1);
        tvReplacement2 = (TextView) findViewById(R.id.tv_replacement2);
        tvReplacement3 = (TextView) findViewById(R.id.tv_replacement3);
        tvReplacement4 = (TextView) findViewById(R.id.tv_replacement4);
        tvReplacement5 = (TextView) findViewById(R.id.tv_replacement5);

        tvDuty1 = (TextView) findViewById(R.id.tv_duty1);
        tvDuty2 = (TextView) findViewById(R.id.tv_duty2);
        tvDuty3 = (TextView) findViewById(R.id.tv_duty3);
        tvDuty4 = (TextView) findViewById(R.id.tv_duty4);
        tvDuty5 = (TextView) findViewById(R.id.tv_duty5);

        switchRequesrReplacment = (Switch) findViewById(R.id.switch_request_replacement);

        tvDayTitle.setText("People on duty:\n" + dateStr);

        myMail= myFirebaseAuth.getCurrentUser().getEmail();

        //set the listeners:

        setMyNameListener();
        setWeekDayListener(dayOfWeek);

        setMapDataChangedListener();

        Log.d("notes","weekly names: "+arrListWeeklyDayNames);

        //Put the duty on screen:
        //check if weekly
        if(1 <= dayOfWeek && dayOfWeek <= 4){
            ScreenWeeklyNames();
        }

        Log.d("notes", "weekend date to check: " + dateToCheckStr);
        //check if weekend(or weekly with replacements)
//        ScreenWeekendNames();

        //put the replacements on screen and paint orange/green if needed. also:determine switch start
//        ScreenReplacements(dateToCheckStr);

        setTheSwitch(dateToCheckStr);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day_info, menu);
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
    public static java.util.Date getDateFromInfo(int year,int month, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTime();
    }
    /**
     * update the names of duty to names
     */
    private void putOnDuty(ArrayList<String> names){
        Log.d("notes","PutOnDuty");

        TextView tv= (TextView) findViewById(R.id.tv_duty1);
        int i=1;
        for(String name : names){
//            if(dayMap != null && dayMap.containsKey(name) && dayMap.get(name) != null && dayMap.get(name).equals(MainActivity.REQUESTED)){
//                changeColor(name,MainActivity.REQUEST_COLOR);
//            }            if(dayMap != null && dayMap.containsKey(name) && dayMap.get(name) != null && dayMap.get(name).equals(MainActivity.REQUESTED)){
//                changeColor(name,MainActivity.REQUEST_COLOR);
//            }
            switch(i++){
                case(1):
                    tv= (TextView) findViewById(R.id.tv_duty1);
                    break;
                case(2):
                    tv= (TextView) findViewById(R.id.tv_duty2);
                    break;
                case(3):
                    tv= (TextView) findViewById(R.id.tv_duty3);
                    break;
                case(4):
                    tv= (TextView) findViewById(R.id.tv_duty4);
                    break;
                case(5):
                    tv= (TextView) findViewById(R.id.tv_duty5);
                    break;
            }
            tv.setText(name);

        }
    }
    private void ScreenMap(){
        Log.d("notes","Screen map");
        if(dayMap == null){
            return;
        }

        TextView tvDuty= (TextView) findViewById(R.id.tv_duty1);
        TextView tvReplacement= (TextView) findViewById(R.id.tv_replacement1);
        Button bt_replace_undo = (Button) findViewById(R.id.bt_replace1);

        int i=1;
        for(String name : getKeysFromMap(dayMap)){
            if(dayMap != null && dayMap.containsKey(name) && dayMap.get(name) != null && dayMap.get(name).equals(MainActivity.REQUESTED)){
                changeColor(name,MainActivity.REQUEST_COLOR);
            }
            switch(i++){
                case(1):
                    tvDuty= (TextView) findViewById(R.id.tv_duty1);
                    tvReplacement= (TextView) findViewById(R.id.tv_replacement1);
                    bt_replace_undo = (Button) findViewById(R.id.bt_replace1);
                    break;
                case(2):
                    tvDuty= (TextView) findViewById(R.id.tv_duty2);
                    tvReplacement= (TextView) findViewById(R.id.tv_replacement2);
                    bt_replace_undo = (Button) findViewById(R.id.bt_replace2);
                    break;
                case(3):
                    tvDuty= (TextView) findViewById(R.id.tv_duty3);
                    tvReplacement= (TextView) findViewById(R.id.tv_replacement3);
                    bt_replace_undo = (Button) findViewById(R.id.bt_replace3);
                    break;
                case(4):
                    tvDuty= (TextView) findViewById(R.id.tv_duty4);
                    tvReplacement= (TextView) findViewById(R.id.tv_replacement4);
                    bt_replace_undo = (Button) findViewById(R.id.bt_replace4);
                    break;
                case(5):
                    tvDuty= (TextView) findViewById(R.id.tv_duty5);
                    tvReplacement= (TextView) findViewById(R.id.tv_replacement5);
                    bt_replace_undo = (Button) findViewById(R.id.bt_replace5);
                    break;
            }
            tvDuty.setText(name);
            String replaceText= dayMap.get(name).toString();
            tvReplacement.setText(replaceText);
            //replace/undo:
            if(replaceText.equals(myName)){
                bt_replace_undo.setText(MainActivity.UNDO_BT);
            }else{
                bt_replace_undo.setText(MainActivity.REPLACE_BT);
            }
            //paint:
            if(replaceText.equals(MainActivity.REQUESTED)){
                changeColor(name, MainActivity.REQUEST_COLOR);
                if(name.equals(myName)){
                    switchRequesrReplacment.setChecked(true);
                    Toast.makeText(getApplicationContext(), "You have requested to be replaced.", Toast.LENGTH_SHORT).show();
                }
            }else
            if(!replaceText.equals(MainActivity.NO_PERSON)){
                changeColor(name,MainActivity.REPLACED_COLOR);
                if(name.equals(myName)){
                    switchRequesrReplacment.setChecked(true);
                    Toast.makeText(getApplicationContext(), "You are being replaced by: "+replaceText, Toast.LENGTH_SHORT).show();
                }
            }else{
                changeColor(name,MainActivity.REGULAR_COLOR);

            }

        }
    }
    /**
     * change the textViews which equal name into color
     * @param name
     * @param color
     */
    public void changeColor(String name,int color){
        Log.d("notes","change color: "+name);
        //determine the colors
        color= getResources().getColor(color);

        if( name.equals(tvDuty1.getText().toString()) ){
            tvDuty1.setTextColor(color);
            btReplace1.setTextColor(color);
            tvReplacement1.setTextColor(color);
        }
        if( name.equals(tvDuty2.getText().toString()) ){
            tvDuty2.setTextColor(color);
            btReplace2.setTextColor(color);
            tvReplacement2.setTextColor(color);
        }
        if( name.equals(tvDuty3.getText().toString()) ){
            tvDuty3.setTextColor(color);
            btReplace3.setTextColor(color);
            tvReplacement3.setTextColor(color);
        }
        if( name.equals(tvDuty4.getText().toString()) ){
            tvDuty4.setTextColor(color);
            btReplace4.setTextColor(color);
            tvReplacement4.setTextColor(color);
        }
        if( name.equals(tvDuty5.getText().toString()) ){
            tvDuty5.setTextColor(color);
            btReplace5.setTextColor(color);
            tvReplacement5.setTextColor(color);
        }

        if( name.equals(tvReplacement1.getText().toString()) ){
            btReplace1.setTextColor(color);
            tvReplacement1.setTextColor(color);
        }
        if( name.equals(tvReplacement2.getText().toString()) ){
            btReplace2.setTextColor(color);
            tvReplacement2.setTextColor(color);
        }
        if( name.equals(tvReplacement3.getText().toString()) ){
            btReplace3.setTextColor(color);
            tvReplacement3.setTextColor(color);
        }
        if( name.equals(tvReplacement4.getText().toString()) ){
            btReplace4.setTextColor(color);
            tvReplacement4.setTextColor(color);
        }
        if( name.equals(tvReplacement5.getText().toString()) ){
            btReplace5.setTextColor(color);
            tvReplacement5.setTextColor(color);
        }
    }
    public void putOnReplacements(HashMap<String,String> repMap){
        Log.d("notes","putOnReplacements");
        if(repMap == null){
            return;
        }
        String dutyName1 = tvDuty1.getText().toString();
        if(repMap.containsKey(dutyName1)){
            String repStr = repMap.get(dutyName1);
            // if requested then paint in orange(no need?)
            tvReplacement1.setText(repStr);
        }
        String dutyName2 = tvDuty2.getText().toString();
        if(repMap.containsKey(dutyName2)){
            String repStr = repMap.get(dutyName2);
            // if requested then paint in orange(no need?)
            tvReplacement1.setText(repStr);
        }
        String dutyName3 = tvDuty3.getText().toString();
        if(repMap.containsKey(dutyName3)){
            String repStr = repMap.get(dutyName1);
            // if requested then paint in orange(no need?)
            tvReplacement1.setText(repStr);
        }
        String dutyName4 = tvDuty4.getText().toString();
        if(repMap.containsKey(dutyName4)){
            String repStr = repMap.get(dutyName4);
            // if requested then paint in orange(no need?)
            tvReplacement1.setText(repStr);
        }
        String dutyName5 = tvDuty5.getText().toString();
        if(  !( dutyName5.equals("---") ) && repMap.containsKey(dutyName5)){
            String repStr = repMap.get(dutyName5);
            // if requested then paint in orange(no need?)
            tvReplacement1.setText(repStr);
        }
    }
    /**
     * put the names of the people on duty at this day of week on screen

     */
    public  void ScreenWeeklyNames() {
        Log.d("notes","ScreenWeekkyNames");

        Log.d("notes", "weekly names: " + arrListWeeklyDayNames);
        putOnDuty(arrListWeeklyDayNames);
    }
    public void setTheSwitch(final String dateToCheckStr){
        Log.d("notes","SetTheSwitch");

        switchRequesrReplacment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //case checked on:
                if (isChecked) {
                    //assuming day map already initiated:
                    HashMap tmpMap = new HashMap<String, String>();
                    Log.d("notes","show ny name in start of checked: "+myName);
                    if (dayMap == null && arrListWeeklyDayNames != null) {
//                        tmpMap = MainActivity.addWeekendToFirebase(dateToCheckStr, arrListWeeklyDayNames);
                        for(String name:arrListWeeklyDayNames){
                            tmpMap.put(name,MainActivity.NO_PERSON);
                        }
                        Log.d("notes", "NOT: entered initializing map in switch");
                    } else {
                        tmpMap = (HashMap<String, Object>) dayMap;
                    }
//                    Log.d("notes","show day map in start of checked,before initialization: "+dayMap.toString());

                    if (!tmpMap.containsKey(myName)) {
                        Toast.makeText(getApplicationContext(), "You are not on duty on this day.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("notes","show ny name in checked: "+myName);
                    Log.d("notes","show temp map in checked: " + tmpMap.toString());
//                    Log.d("notes","show day map in checked: "+dayMap.toString());


                    if (tmpMap.get(myName).equals(MainActivity.NO_PERSON)) {
                        tmpMap.put(myName, MainActivity.REQUESTED);
//                        changeColor(myName, MainActivity.REQUEST_COLOR);
//                        tvDuty5.setTextColor(getResources().getColor(R.color.red));
                    } else if (tmpMap.get(myName).equals(MainActivity.REQUESTED)) {
                        Toast.makeText(getApplicationContext(), "You have ALREADY requested to be replaced.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You are ALREADY being replaced by: " + tmpMap.get(myName), Toast.LENGTH_SHORT).show();
                    }
                    //update the dayMap on firebase:
                    updateDayMap(dateToCheckStr, tmpMap);
                } else { //case checked off:
                    HashMap tmpMap = new HashMap<String, String>();
                    if (dayMap == null){
                        tmpMap = MainActivity.addWeekendToFirebase(dateToCheckStr, arrListWeeklyDayNames);
                        Log.d("notes", "entered initialzing map in switch unchecked");
                    } else {
                        tmpMap = (HashMap<String, Object>) dayMap;
                    }
                    if (!tmpMap.containsKey(myName)) {
                        Toast.makeText(getApplicationContext(), "You are not on duty on this day.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("notes","show temp map in unchecked: "+tmpMap.toString());
                    Log.d("notes","show day map in unchecked: "+dayMap.toString());

                    if (tmpMap.get(myName).equals(MainActivity.NO_PERSON)) {
                        Log.d("notes","show ny name in unchecked: "+myName);
                        Log.d("notes","show ny name's val in unchecked: "+tmpMap.get(myName));


                        Toast.makeText(getApplicationContext(), "You haven't even requested to be replaced...", Toast.LENGTH_SHORT).show();
                    } else if (tmpMap.get(myName).equals(MainActivity.REQUESTED)) {
                        tmpMap.put(myName, MainActivity.NO_PERSON);
                        changeColor(myName, MainActivity.REVOKED_COLOR);
                        Toast.makeText(getApplicationContext(), "Your request has been REVOKED.", Toast.LENGTH_SHORT).show();
                    } else { //case you revoke a request already answered
                        Toast.makeText(getApplicationContext(), "You are ALREADY being replaced by: " + tmpMap.get(myName), Toast.LENGTH_SHORT).show();
                        switchRequesrReplacment.setChecked(true);
                        //todo: add the option to still revoke the request, even if u were already replaced
                    }
                    updateDayMap(dateToCheckStr, tmpMap);
                }
            }
        });
    }
    private ArrayList<String> getKeysFromMap(HashMap<String,Object> map){
        ArrayList<String> arr = new ArrayList<String>();
        for(String key:map.keySet()){
            arr.add(key);
        }
        return arr;
    }
    private void updateDayMap(String dateToCheckStr,HashMap<String,Object> map){
        Log.d("notes", "UpdateDateMap");
        myFirebaseRef.child("schedule").child("weekends").child(dateToCheckStr).child(MainActivity.DAY_MAP).setValue(map);
    }
    private void setMapDataChangedListener(){
        Log.d("notes", "SetMapDataChangeListener");
        myFirebaseRef.child("schedule").child("weekends").child(dateToCheckStr).child(MainActivity.DAY_MAP).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.d("notes", "---------called map listener----------");
                Log.d("notes", "dayMap VALUE: " + dataSnapshot.getValue());
                dayMap = (HashMap<String, Object>) dataSnapshot.getValue();
                if (dayMap != null) {
                    Log.d("notes", "day map: " + dayMap.toString());
                    ScreenMap();
                }
                else
                    //Put the duty on screen:
                    //check if weekly
                    if (1 <= dayOfWeek && dayOfWeek <= 4) {
                        ScreenWeeklyNames();
                    }else{
                        Toast.makeText(getApplicationContext(),"This day's schedule is yet to be determined.",Toast.LENGTH_SHORT).show();
                    }
                Log.d("notes", "weekend date to check: " + dateToCheckStr);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("notes", "failed to aquire day map");
            }
        });
    }
    private void setWeekDayListener(int numOfDay){
        Log.d("notes","SetWeekDayListener");

        myFirebaseRef.child("schedule").child("weekly").child(""+numOfDay).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.d("notes", "weekly list data is: " + dataSnapshot.getValue());
                arrListWeeklyDayNames = (ArrayList) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("notes","failed to get weeklu list from FB");

            }
        });
    }
    private void setMyNameListener(){
        Log.d("notes", "SetMyNameListener");

        myFirebaseRef.child(SignUpActivity.USERS).child(SignUpActivity.MAIL).child(SignUpActivity.vanishDots(myMail)).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    myName = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("notes","failed to get username from mail");
            }
        });
    }


    @Override
    public void onClick(View v) {
        Log.d("notes","day info on Click: ");
        HashMap<String,Object> tmpMap = new HashMap<String,Object>();
        if(dayMap == null){
            for(String name:arrListWeeklyDayNames){
                tmpMap.put(name,MainActivity.NO_PERSON);
            }
            updateDayMap(dateToCheckStr,tmpMap);
        }else{
            tmpMap = dayMap;
        }
        //check invalid states of replacement:

        //uopn replace click, change the replacement value both in the cloud
        // and on the screen. aslo, turn the buttin into an undo button
        //todo: make ScreenMaps implement undo button


        //todo: button undo- only if ur the relpacer!
        switch(v.getId()){
            case(R.id.bt_replace1):
                if(btReplace1.getText().toString().equalsIgnoreCase(MainActivity.REPLACE_BT)){
                    if(whoIsOnDuty(tmpMap).contains(myName)){
                        Toast.makeText(getApplicationContext(),"You can't replace because you are ALREADY on duty.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String replaced = tvDuty1.getText().toString();
                    if(replaced.equals(MainActivity.NO_PERSON)){
                        Toast.makeText(getApplicationContext(),"You can't replace no one... why would you even try?",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tmpMap.put(replaced,myName);
                    updateDayMap(dateToCheckStr,tmpMap);
                }else{ //case of undo, return to requested
                    String replaced = tvDuty1.getText().toString();
                    if(!myName.equals(tmpMap.get(replaced))){
                        Toast.makeText(getApplicationContext(),"You can't undo this replacement because its not you.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tmpMap.put(replaced,MainActivity.REQUESTED);
                    updateDayMap(dateToCheckStr,tmpMap);
                }
                break;
            case(R.id.bt_replace2):
                if(btReplace2.getText().toString().equalsIgnoreCase(MainActivity.REPLACE_BT)){
                    if(whoIsOnDuty(tmpMap).contains(myName)){
                        Toast.makeText(getApplicationContext(),"You can't replace because you are ALREADY on duty.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String replaced = tvDuty2.getText().toString();
                    if(replaced.equals(MainActivity.NO_PERSON)){
                        Toast.makeText(getApplicationContext(),"You can't replace no one... why would you even try?",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tmpMap.put(replaced,myName);
                    updateDayMap(dateToCheckStr,tmpMap);
                }else{ //case of undo, return to requested
                    String replaced = tvDuty2.getText().toString();
                    if(!myName.equals(tmpMap.get(replaced))){
                        Toast.makeText(getApplicationContext(),"You can't undo this replacement because its not you.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tmpMap.put(replaced,MainActivity.REQUESTED);
                    updateDayMap(dateToCheckStr,tmpMap);
                }
                break;
            case(R.id.bt_replace3):
                if(btReplace3.getText().toString().equalsIgnoreCase(MainActivity.REPLACE_BT)){
                    if(whoIsOnDuty(tmpMap).contains(myName)){
                        Toast.makeText(getApplicationContext(),"You can't replace because you are ALREADY on duty.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String replaced = tvDuty3.getText().toString();
                    if(replaced.equals(MainActivity.NO_PERSON)){
                        Toast.makeText(getApplicationContext(),"You can't replace no one... why would you even try?",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tmpMap.put(replaced,myName);
                    updateDayMap(dateToCheckStr,tmpMap);
                }else{ //case of undo, return to requested
                    String replaced = tvDuty3.getText().toString();
                    if(!myName.equals(tmpMap.get(replaced))){
                        Toast.makeText(getApplicationContext(),"You can't undo this replacement because its not you.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tmpMap.put(replaced,MainActivity.REQUESTED);
                    updateDayMap(dateToCheckStr,tmpMap);
                }
                break;
            case(R.id.bt_replace4):
                if(btReplace4.getText().toString().equalsIgnoreCase(MainActivity.REPLACE_BT)){
                    if(whoIsOnDuty(tmpMap).contains(myName)){
                        Toast.makeText(getApplicationContext(),"You can't replace because you are ALREADY on duty.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String replaced = tvDuty4.getText().toString();
                    if(replaced.equals(MainActivity.NO_PERSON)){
                        Toast.makeText(getApplicationContext(),"You can't replace no one... why would you even try?",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tmpMap.put(replaced,myName);
                    updateDayMap(dateToCheckStr,tmpMap);
                }else{ //case of undo, return to requested
                    String replaced = tvDuty4.getText().toString();
                    if(!myName.equals(tmpMap.get(replaced))){
                        Toast.makeText(getApplicationContext(),"You can't undo this replacement because its not you.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tmpMap.put(replaced,MainActivity.REQUESTED);
                    updateDayMap(dateToCheckStr,tmpMap);
                }
                break;
            case(R.id.bt_replace5):
                if(btReplace5.getText().toString().equalsIgnoreCase(MainActivity.REPLACE_BT)){
                    if(whoIsOnDuty(tmpMap).contains(myName)){
                        Toast.makeText(getApplicationContext(),"You can't replace because you are ALREADY on duty.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String replaced = tvDuty5.getText().toString();
                    if(replaced.equals(MainActivity.NO_PERSON)){
                        Toast.makeText(getApplicationContext(),"You can't replace no one... why would you even try?",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tmpMap.put(replaced,myName);
                    updateDayMap(dateToCheckStr,tmpMap);
                }else{ //case of undo, return to requested
                    String replaced = tvDuty5.getText().toString();
                    if(!myName.equals(tmpMap.get(replaced))){
                        Toast.makeText(getApplicationContext(),"You can't undo this replacement because its not you.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tmpMap.put(replaced,MainActivity.REQUESTED);
                    updateDayMap(dateToCheckStr,tmpMap);
                }
                break;
        }
    }
    public ArrayList<String> whoIsOnDuty(HashMap<String,Object> map){
        ArrayList<String> dutyList = new ArrayList<String>();
        for(String name: getKeysFromMap(map)){
            if(map.get(name).equals(MainActivity.REQUESTED)||map.get(name).equals(MainActivity.NO_PERSON)){
                dutyList.add(name);
            }else{
                dutyList.add(map.get(name).toString());
            }
        }
        return dutyList;
    }
}
