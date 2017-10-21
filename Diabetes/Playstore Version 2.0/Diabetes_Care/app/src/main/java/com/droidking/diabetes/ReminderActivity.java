package com.droidking.diabetes;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {
    AlarmManager first_am,second_am,third_am,fourth_am,  fifth_am,sixth_am;  //This should be Global Variable
    static int FIRSTALARM = 1 ,SECONDALARM = 2, THIRDALARM = 3, FOURTHALARM =4, FIFTHALARM = 5,SIXTHALARM = 6;
    int dialog_no = 1; // Default Value
    PendingIntent pendingIntent;///Global variable
    TextView first_switchStatus, second_switchStatus,third_switchStatus,fourth_switchStatus , fifth_switchStatus, sixth_switchStatus;
    TextView first_time,second_time,third_time, fourth_time,fifth_time,sixth_time;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String first_Switch_Mode =  "first_switch_mode";
    public static final String second_Switch_Mode = "second_switch_mode";
    public static final String third_Switch_Mode = "third_switch_mode";
    public static final String fourth_Switch_Mode = "fourth_switch_mode";
    public static final String fifth_Switch_Mode = "fifth_switch_mode";
    public static final String sixth_Switch_Mode = "sixth_switch_mode";

    public static final String first_Hour = "first_hourKey";
    public static final String second_Hour = "second_hourKey";
    public static final String third_Hour = "third_hourKey";
    public static final String fourth_Hour = "fourth_hourKey";
    public static final String fifth_Hour = "fifth_hourKey";
    public static final String sixth_Hour = "sixth_hourKey";

    public static final String first_Minute = "first_minutesKey";
    public static final String second_Minute = "second_minutesKey";
    public static final String third_Minute = "third_minutesKey";
    public static final String fourth_Minute = "fourth_minutesKey";
    public static final String fifth_Minute = "fifth_minutesKey";
    public static final String sixth_Minute = "sixth_minutesKey";

    SharedPreferences sharedpreferences;
    Switch firstSwitch,secondSwitch,thirdSwitch, fourthSwitch , fifthSwitch, sixthSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
      //  this.overridePendingTransition(R.anim.righttoleft, R.anim.slide_in_left);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirstAlarmList();
        SecondAlarmList();
        ThirdAlarmList();
        FourthAlarmList();
        FifthAlarmList();
        SixthAlarmList();
    }


    public void FirstAlarmList(){
        ImageView set = (ImageView) findViewById(R.id.first_imageview);
        first_time = (TextView) findViewById(R.id.first_alarmTimetv);
        firstSwitch = (Switch) findViewById(R.id.first_switchview);
        firstSwitch.setChecked(read_sharedprefarance(FIRSTALARM));
        read_time(first_Hour, first_Minute,FIRSTALARM);
        /**Edit the shared preference*/
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        first_switchStatus = (TextView) findViewById(R.id.first_statustv);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(881);
            }
        });

        firstSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(isChecked){


                    SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    int hour = sharedPref.getInt(first_Hour,0);
                    int minute = sharedPref.getInt(first_Minute,0);
                    notification(hour,minute,FIRSTALARM);
                    editor.putBoolean(first_Switch_Mode, true);
                    first_switchStatus.setText("ON");
                }else{
                    editor.putBoolean(first_Switch_Mode,false);
                    if (first_am!= null) {
                        first_am.cancel(pendingIntent);//Cancel the pre set Alarm From Alarm manager
                        pendingIntent.cancel();//Release The panding intent
                    }
                    first_switchStatus.setText("OFF");
                }
                editor.commit();
            }
        });

        //check the current state before we display the screen
        if(firstSwitch.isChecked()){
            first_switchStatus.setText("ON");
        }
        else {
            first_switchStatus.setText("OFF");
        }


    }
    public void SecondAlarmList(){

        ImageView set = (ImageView) findViewById(R.id.second_imageview);
        second_time = (TextView) findViewById(R.id.second_alarmTimetv);
        secondSwitch = (Switch) findViewById(R.id.second_switchview);
        secondSwitch.setChecked(read_sharedprefarance(SECONDALARM));
        read_time(second_Hour, second_Minute,SECONDALARM);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        second_switchStatus = (TextView) findViewById(R.id.second_statustv);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(882);

            }
        });
        secondSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(isChecked){
                    SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    int hour = sharedPref.getInt(second_Hour,0);
                    int minute = sharedPref.getInt(second_Minute,0);
                    notification(hour,minute,SECONDALARM);
                    editor.putBoolean(second_Switch_Mode, true);
                    second_switchStatus.setText("ON");
                }else{
                    editor.putBoolean(second_Switch_Mode,false);
                    if (second_am!= null) {
                        second_am.cancel(pendingIntent);//Cancel the pre set Alarm From Alarm manager
                        pendingIntent.cancel();//Release The panding intent
                    }
                    second_switchStatus.setText("OFF");
                }
                editor.commit();
            }
        });
        if(secondSwitch.isChecked()){
            second_switchStatus.setText("ON");
        }
        else {
            second_switchStatus.setText("OFF");
        }
    }
    public void ThirdAlarmList(){
        ImageView set = (ImageView) findViewById(R.id.third_imageview);
        third_time = (TextView) findViewById(R.id.third_alarmTimetv);
        thirdSwitch = (Switch) findViewById(R.id.third_switchview);
        thirdSwitch.setChecked(read_sharedprefarance(THIRDALARM));
        read_time(third_Hour, third_Minute,THIRDALARM);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        third_switchStatus = (TextView) findViewById(R.id.third_statustv);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(883);

            }
        });
        thirdSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(isChecked){
                    SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    int hour = sharedPref.getInt(third_Hour,0);
                    int minute = sharedPref.getInt(third_Minute,0);
                    notification(hour,minute,THIRDALARM);
                    editor.putBoolean(third_Switch_Mode, true);
                    third_switchStatus.setText("ON");
                }else{
                    editor.putBoolean(third_Switch_Mode,false);
                    if (third_am!= null) {
                        third_am.cancel(pendingIntent);//Cancel the pre set Alarm From Alarm manager
                        pendingIntent.cancel();//Release The panding intent
                    }
                    third_switchStatus.setText("OFF");
                }
                editor.commit();
            }
        });
        if(thirdSwitch.isChecked()){
            third_switchStatus.setText("ON");
        }
        else {
            third_switchStatus.setText("OFF");
        }
    }

    public void FourthAlarmList(){
        ImageView set = (ImageView) findViewById(R.id.fourth_imageview);
        fourth_time = (TextView) findViewById(R.id.fourth_alarmTimetv);
        fourthSwitch = (Switch) findViewById(R.id.fourth_switchview);
        fourthSwitch.setChecked(read_sharedprefarance(FOURTHALARM));
        read_time(fourth_Hour, fourth_Minute,FOURTHALARM);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        fourth_switchStatus = (TextView) findViewById(R.id.fourth_statustv);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(884);

            }
        });
        fourthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(isChecked){
                    SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    int hour = sharedPref.getInt(fourth_Hour,0);
                    int minute = sharedPref.getInt(fourth_Minute,0);
                    notification(hour,minute,FOURTHALARM);
                    editor.putBoolean(fourth_Switch_Mode, true);
                    fourth_switchStatus.setText("ON");
                }else{
                    editor.putBoolean(fourth_Switch_Mode,false);
                    if (fourth_am!= null) {
                        fourth_am.cancel(pendingIntent);//Cancel the pre set Alarm From Alarm manager
                        pendingIntent.cancel();//Release The panding intent
                    }
                    fourth_switchStatus.setText("OFF");
                }
                editor.commit();
            }
        });
        if(fourthSwitch.isChecked()){
            fourth_switchStatus.setText("ON");
        }
        else {
            fourth_switchStatus.setText("OFF");
        }
    }
    public void FifthAlarmList(){
        ImageView set = (ImageView) findViewById(R.id.fifth_imageview);
        fifth_time = (TextView) findViewById(R.id.fifth_alarmTimetv);
        fifthSwitch = (Switch) findViewById(R.id.fifth_switchview);
        fifthSwitch.setChecked(read_sharedprefarance(FIFTHALARM));
        read_time(fifth_Hour, fifth_Minute,FIFTHALARM);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        fifth_switchStatus = (TextView) findViewById(R.id.fifth_statustv);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(885);

            }
        });
        fifthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(isChecked){
                    SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    int hour = sharedPref.getInt(fifth_Hour,0);
                    int minute = sharedPref.getInt(fifth_Minute,0);
                    notification(hour,minute,FIFTHALARM);
                    editor.putBoolean(fifth_Switch_Mode, true);
                    fifth_switchStatus.setText("ON");
                }else{
                    editor.putBoolean(fifth_Switch_Mode,false);
                    if (fifth_am!= null) {
                        fifth_am.cancel(pendingIntent);//Cancel the pre set Alarm From Alarm manager
                        pendingIntent.cancel();//Release The panding intent
                    }
                    fifth_switchStatus.setText("OFF");
                }
                editor.commit();
            }
        });
        if(fifthSwitch.isChecked()){
            fifth_switchStatus.setText("ON");
        }
        else {
            fifth_switchStatus.setText("OFF");

        }
    }
    public void SixthAlarmList(){
        ImageView set = (ImageView) findViewById(R.id.sixth_imageview);
        sixth_time = (TextView) findViewById(R.id.sixth_alarmTimetv);
        sixthSwitch = (Switch) findViewById(R.id.sixth_switchview);
        sixthSwitch.setChecked(read_sharedprefarance(SIXTHALARM));
        read_time(sixth_Hour, sixth_Minute,SIXTHALARM);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sixth_switchStatus = (TextView) findViewById(R.id.sixth_statustv);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(886);

            }
        });
        sixthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(isChecked){
                    SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                    int hour = sharedPref.getInt(sixth_Hour,0);
                    int minute = sharedPref.getInt(sixth_Minute,0);
                    notification(hour,minute,SIXTHALARM);
                    editor.putBoolean(sixth_Switch_Mode, true);
                    sixth_switchStatus.setText("ON");
                }else{
                    editor.putBoolean(sixth_Switch_Mode,false);
                    if (sixth_am!= null) {
                        sixth_am.cancel(pendingIntent);//Cancel the pre set Alarm From Alarm manager
                        pendingIntent.cancel();//Release The panding intent
                    }
                    sixth_switchStatus.setText("OFF");
                }
                editor.commit();
            }
        });
        if(sixthSwitch.isChecked()){
            sixth_switchStatus.setText("ON");
        }
        else {
            sixth_switchStatus.setText("OFF");
        }
    }



    public boolean read_sharedprefarance(int id){
        SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);\
        boolean mode = false;
        switch(id)
        {
            case 1:
                mode = sharedPref.getBoolean(first_Switch_Mode,false);//For first alarm
                break;
            case 2:
                mode = sharedPref.getBoolean(second_Switch_Mode,false);//For Second alarm
                break;
            case 3:
                mode = sharedPref.getBoolean(third_Switch_Mode,false);
                break;
            case 4:
                mode = sharedPref.getBoolean(fourth_Switch_Mode,false);
                break;
            case 5:
                mode = sharedPref.getBoolean(fifth_Switch_Mode,false);
                break;
            case 6:
                mode = sharedPref.getBoolean(sixth_Switch_Mode,false);
                break;
            default:
                mode = false;

        }

        return mode;
    }
    public void read_time(String Hour, String Minute, int i)
    {
        SharedPreferences sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        int hour = sharedPref.getInt(Hour,0);
        int minute = sharedPref.getInt(Minute,0);
        String time = showTime(hour, minute);
        switch(i)
        {
            case 1:
                first_time.setText(time);
                break;
            case 2:
                second_time.setText(time);
                break;
            case 3:
                third_time.setText(time);
                break;
            case 4:
                fourth_time.setText(time);
                break;
            case 5:
                fifth_time.setText(time);
                break;
            case 6:
                sixth_time.setText(time);
                break;
            default:

        }
    }
    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            //  notification(hourOfDay, minute);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            switch(dialog_no)
            {
                case 1:
                    editor.putInt(first_Hour, hourOfDay);
                    editor.putInt(first_Minute,minute);
                    editor.commit();
                    read_time(first_Hour, first_Minute,dialog_no);
                    //notification(hourOfDay,minute,FIRSTALARM);
                    break;
                case 2:
                    editor.putInt(second_Hour, hourOfDay);
                    editor.putInt(second_Minute,minute);
                    editor.commit();
                    read_time(second_Hour, second_Minute,dialog_no);
                  //  notification(hourOfDay,minute,SECONDALARM);
                    break;
                case 3:
                    editor.putInt(third_Hour, hourOfDay);
                    editor.putInt(third_Minute,minute);
                    editor.commit();
                    read_time(third_Hour, third_Minute,dialog_no);
                   // notification(hourOfDay,minute,THIRDALARM);
                    break;
                case 4:
                    editor.putInt(fourth_Hour, hourOfDay);
                    editor.putInt(fourth_Minute,minute);
                    editor.commit();
                    read_time(fourth_Hour, fourth_Minute,dialog_no);
                    // notification(hourOfDay,minute,THIRDALARM);
                    break;
                case 5:
                    editor.putInt(fifth_Hour, hourOfDay);
                    editor.putInt(fifth_Minute,minute);
                    editor.commit();
                    read_time(fifth_Hour, fifth_Minute,dialog_no);
                    // notification(hourOfDay,minute,THIRDALARM);
                    break;
                case 6:
                    editor.putInt(sixth_Hour, hourOfDay);
                    editor.putInt(sixth_Minute,minute);
                    editor.commit();
                    read_time(sixth_Hour, sixth_Minute,dialog_no);
                    // notification(hourOfDay,minute,THIRDALARM);
                    break;

                default:

            }



        }

    };

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        // showDate(year, month + 1, day);
        Calendar calendar;
        calendar = Calendar.getInstance();
        if (id == 881){
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            dialog_no = FIRSTALARM;
            return new TimePickerDialog(this, myTimeListener, hour, min, false);
        }else if(id == 882) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            dialog_no = SECONDALARM;
            return new TimePickerDialog(this, myTimeListener, hour, min, false);
        }else if(id == 883) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            dialog_no = THIRDALARM;
            return new TimePickerDialog(this, myTimeListener, hour, min, false);
        }else if(id == 884) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            dialog_no = FOURTHALARM;
            return new TimePickerDialog(this, myTimeListener, hour, min, false);
        }else if(id == 885) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            dialog_no = FIFTHALARM;
            return new TimePickerDialog(this, myTimeListener, hour, min, false);
        }else if(id == 886) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            dialog_no = SIXTHALARM;
            return new TimePickerDialog(this, myTimeListener, hour, min, false);
        }
        else {
            return null;
        }
    }
    private void notification(int h ,int m ,int number ){
        //Locale alocal = new Locale

        Calendar calendar = Calendar.getInstance();
        // we can set time by open date and time picker dialog
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE,m);
        calendar.set(Calendar.SECOND, 0);

        Toast.makeText(this,  showTime(h,m), Toast.LENGTH_SHORT).show();


        Intent intent1 = new Intent(ReminderActivity.this, MyReceiver.class);
        pendingIntent = PendingIntent.getService( this,0,intent1,
                pendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent = PendingIntent.getBroadcast(
                  this,0,intent1,
                pendingIntent.FLAG_UPDATE_CURRENT

        );
        if(pendingIntent == null) {
            int iki = 10;
        }

        switch(number)
        {
            case 1:
                first_am = (AlarmManager) ReminderActivity.this
                        .getSystemService(ReminderActivity.this.ALARM_SERVICE);

                first_am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                break;
            case 2:
                second_am = (AlarmManager) ReminderActivity.this
                        .getSystemService(ReminderActivity.this.ALARM_SERVICE);

                second_am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                //      alarmCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                break;
            case 3:
                third_am = (AlarmManager) ReminderActivity.this
                        .getSystemService(ReminderActivity.this.ALARM_SERVICE);

                third_am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                //      alarmCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                break;
            case 4:
                fourth_am = (AlarmManager) ReminderActivity.this
                        .getSystemService(ReminderActivity.this.ALARM_SERVICE);

                fourth_am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                //      alarmCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                break;
            case 5:
                fifth_am = (AlarmManager) ReminderActivity.this
                        .getSystemService(ReminderActivity.this.ALARM_SERVICE);

                fifth_am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                //      alarmCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                break;
            case 6:
                sixth_am = (AlarmManager) ReminderActivity.this
                        .getSystemService(ReminderActivity.this.ALARM_SERVICE);

                sixth_am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
                //      alarmCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                break;
            default:

        }

    }

    public String showTime(int hour, int min) {
        String format,minuteString;
        if (hour == 0) {
            hour += 12;
            format = "AM";
        }
        else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        if(min<10)
        {
            minuteString ="0"+min;
        }
        else
        {
            minuteString=""+min;
        }
        return hour+":"+minuteString+" "+format;
    }
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
        }

        return super.onOptionsItemSelected(item);
    }

}
