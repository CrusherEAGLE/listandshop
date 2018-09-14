package mygrocerylist.listandshop;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CalendarClass extends AppCompatActivity {

    String title; //name of fooditem
    Integer iconNo; //the icon to be affixed

    CalendarView simpleCalendarView;

    ImageView icon; //Icon
    TextView itemName; //textview
    Spinner spinnerTime;
    ArrayList<String> spinnerStuff;
    Integer pos;
    Button buttonDone;

    Boolean[] theState;

    TypedArray imgs; //the array where the list of pictures are

    Integer day;
    Integer month;
    Integer year;

    Intent baboosh;
    Integer timeofday;

    SimpleDateFormat sdf;

    long selectedDate;
    String todayday;


    RecyclerViewClass classy;
    BroadcastManager receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataStuff();

    }

    protected void DataStuff() {

        setContentStuff();


        getTimeAndDate();


        doneButton();




    }

    protected void setContentStuff() {
        setContentView(mygrocerylist.listandshop.R.layout.calendar);

        title = getIntent().getExtras().getString("Item");
        iconNo = getIntent().getExtras().getInt("Icon");
        pos = getIntent().getExtras().getInt("Pos");

        classy = new RecyclerViewClass(getApplicationContext());

        imgs = getResources().obtainTypedArray(mygrocerylist.listandshop.R.array.icons);

        baboosh = new Intent(getApplicationContext(), MainActivity.class);

        itemName = findViewById(mygrocerylist.listandshop.R.id.itemname);
        simpleCalendarView = findViewById(mygrocerylist.listandshop.R.id.calendarView);
        buttonDone = findViewById(mygrocerylist.listandshop.R.id.donebutton);
        icon = findViewById(mygrocerylist.listandshop.R.id.icon2);

        itemName.setText(title);
        icon.setImageResource(imgs.getResourceId(iconNo, -1));

    }


    protected void theAlarm() {

        AlarmManager alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //receiver = new BroadcastManager();
        //IntentFilter filter = new IntentFilter("ALARM");
        //registerReceiver(receiver, filter);

        Intent intent = new Intent(getApplicationContext(), BroadcastManager.class);
        intent.setAction("ALARM");
/*        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);*/
        //intent.setData(Uri.parse("http://"+ String.valueOf(iUniqueId)));
        intent.setData(Uri.parse("content://" + title));
        intent.putExtra("name", title);
        intent.putExtra("icon", iconNo);


        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 11);
        cal.set(Calendar.SECOND, 11);
        cal.set(Calendar.MILLISECOND, 11);



        PendingIntent operation = PendingIntent.getBroadcast(this ,0, intent, PendingIntent.FLAG_ONE_SHOT);

        alarms.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), operation);

    }








    protected void getTimeAndDate() {
/*        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        day = now.get(Calendar.DAY_OF_MONTH);*/

        simpleCalendarView.setMinDate(System.currentTimeMillis());

        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int y, int m, int d) {
                day = d;
                month = m;
                year = y;

                int actmonth = month + 1;

                if (actmonth <= 9){
                todayday = year + "/0" + actmonth + "/" + day;}

                else
                {
                    todayday = year + "/" + actmonth + "/" + day;}
            }
        });

    }

    protected void doneButton() {
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (year == null || day == null || month == null)
                    {Toast toast = Toast.makeText(getApplicationContext(), "Please pick a date other than today.", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else
                        {
                        theAlarm();
                            Toast toast = Toast.makeText(getApplicationContext(), "Reminder for " + title + " set! You will be reminded on " + todayday + " at 11 AM.", Toast.LENGTH_LONG);
                            toast.show();
                            theState = MainActivity.loadArray(getApplicationContext());
                            theState[pos] = true;
                            MainActivity.storeArray(theState, getApplicationContext());
                            startActivity(baboosh);

                    }




            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        try{
        unregisterReceiver(receiver);}
        catch(IllegalArgumentException e){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        try{
            unregisterReceiver(receiver);}
        catch(IllegalArgumentException e){

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try{
            unregisterReceiver(receiver);}
        catch(IllegalArgumentException e){

        }
    }
}
