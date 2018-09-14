package mygrocerylist.listandshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.StringTokenizer;

import mygrocerylist.listandshop.support.SwipeController;
import mygrocerylist.listandshop.support.SwipeControllerActions;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv; //the list
    LinearLayoutManager llm; //make the list linear
    RecyclerViewClass adapter2; //adapt the list
    Button button; //submit
    FloatingActionButton addItem; //button on bottom
    Intent ganesh; //to change activities
    Intent darambar;
    Intent shalet;
    ArrayList<String> myList;
    ArrayList<Integer> amount;
    SwipeController swipeController = null;

    AdView mAdView;

    boolean alarm;

    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //main content
        setContentView(mygrocerylist.listandshop.R.layout.thelist);

        MobileAds.initialize(this, "ca-app-pub-2016427388447019~9138576272");

        mAdView = findViewById(mygrocerylist.listandshop.R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ganesh = new Intent(this, AddNewItem.class); //intent to start addnewitem
        darambar = new Intent(this, listSearch.class);

        setRecyclerViewMain(); //RV, and FAB Actions
    }




    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    public void setRecyclerViewMain(){

        myList = getArrayList("Names",this);
        amount = getArrayListInt("Image",this); //get the lists from sharedprefs
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);


        //https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
        rv = findViewById(mygrocerylist.listandshop.R.id.recyclerView); //self
        llm = new LinearLayoutManager(this);
        rv.setHasFixedSize(true); //rv can't change size
        rv.setLayoutManager(llm); //sets rv like listview

        RecyclerView.ItemAnimator animator = rv.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) { //disable flashing when tapping
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        adapter2 = new RecyclerViewClass(this, myList, amount); //set the recyclerviewclass
        rv.setAdapter(adapter2); //links adapter to rv
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                adapter2.Remove(MainActivity.this, position);  //removes when swiping
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rv); //attach swiping to rv
        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        addItem = findViewById(mygrocerylist.listandshop.R.id.floatingActionButton);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(darambar);
            }
        });


    }
 ///GET ARRAYLIST FROM SHAREDPREFS
    public static ArrayList<String> getArrayList(String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        if (gson.fromJson(json, type) == null)
        { return new ArrayList<>(); }
        else
        {
            return gson.fromJson(json, type);}

    }

    public static void saveArrayList(ArrayList<String> list, String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }


    public static ArrayList<Integer> getArrayListInt(String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String s = prefs.getString(key, "");
        StringTokenizer st = new StringTokenizer(s, ",");
        ArrayList<Integer> result = new ArrayList();
        while (st.hasMoreTokens()) {
            result.add(Integer.parseInt(st.nextToken()));
        }
        if (result.isEmpty()){
            return new ArrayList<>();}
            else
            return result;

    }

    public static ArrayList<Integer> getArrayListIntFirstTime(String key, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String s = prefs.getString(key, "");
        StringTokenizer st = new StringTokenizer(s, ",");
        ArrayList<Integer> result = new ArrayList();
        while (st.hasMoreTokens()) {
            result.add(Integer.parseInt(st.nextToken()));
        }
        if (result.isEmpty()){
            ArrayList<Integer> init = new ArrayList<>();
            for (int i = 1; i <= 500; i++)
            {
                init.add(1);
            }
            return init;
        }

        else
            return result;

    }


    public static void saveArrayListInt(ArrayList<Integer> list, String key, Context context)
        {
            String s = "";
            for (Integer i : list) {
                s += i + ",";
            }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, s);
            editor.apply();
    }

    public static boolean storeArray(Boolean[] array, Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("theState" +"_size", array.length);

        for(int i=0;i<array.length;i++)
            editor.putBoolean("theState" + "_" + i, array[i]);

        return editor.commit();
    }

    public static Boolean[] loadArray( Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt("theState" + "_size", 0);
        Boolean array[] = new Boolean[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getBoolean("theState" + "_" + i, false);

        if (size == 0){
            Log.d("size", String.valueOf(size));
            Boolean [] bob = new Boolean[9999];
            Arrays.fill(bob, Boolean.FALSE);
            return bob;
        }
        else
            return array;
    }

}
