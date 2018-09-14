package mygrocerylist.listandshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AddNewItem extends AppCompatActivity {

    EditText editText;
    Button button;
    ArrayList<String> stuff;
    Intent lateef;

    ArrayList<String> myList;
    ArrayList<Integer> theImage;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lateef = new Intent(this, MainActivity.class);

        setContentView(R.layout.additems);

        editText = findViewById(R.id.ItemNameText);
        button = findViewById(R.id.button); //adds to list

        hideKeyboard();

        stuff = MainActivity.getArrayList("Custom", this);

        myList = MainActivity.getArrayList("Names", this); //this is main menu items
        theImage = MainActivity.getArrayListInt("Image", this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stuff.add(editText.getText().toString());
                MainActivity.saveArrayList(stuff, "Custom", getApplicationContext());
                myList.add(editText.getText().toString());//gets the string part of the object and puts it into an arraylist
                theImage.add(0); //ditto but int
                MainActivity.saveArrayList(myList, "Names", getApplicationContext());
                MainActivity.saveArrayListInt(theImage, "Image", getApplicationContext());

                startActivity(lateef);

            }
        });




    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public void hideKeyboard()
    {


        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

}