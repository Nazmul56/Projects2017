package com.droidking.diabetes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Patient_informationActivity extends AppCompatActivity {
    Spinner unit_spinner;
    EditText et_age;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String AGE = "key_age";
    String UNIT = "key_unit";
    int unit_id = 0;

    public static final String PATIENTPREFERENCES = "PatientPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_information2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Patient_informationActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        et_age = (EditText) findViewById(R.id.activity_hello_age);
        Button saveButton = (Button) findViewById(R.id.bsave);
        read_age();
        pref = getApplicationContext().getSharedPreferences(PATIENTPREFERENCES, MODE_PRIVATE);
        editor = pref.edit();
        /***
         *  editor.putBoolean("key_name1", true);           // Saving boolean - true/false
         editor.putInt("key_name2", "int value");        // Saving integer
         editor.putFloat("key_name3", "float value");    // Saving float
         editor.putLong("key_name4", "long value");      // Saving long
         editor.putString("key_name5", "string value");
         */
        unit_spinner = (Spinner) findViewById(R.id.unitsp);
        List<String> categories = new ArrayList<String>();
        categories.add("mmole/dL");   // 0
        categories.add("mg/dL");        // 1

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        unit_spinner.setAdapter(dataAdapter);
        //Set Selected Top
        read_unit();
        unit_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
               // String measured = adapter.getItemAtPosition(position).toString();
                unit_id = position;//Integer.parseInt(measured);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int age_value = Integer.parseInt(et_age.getText().toString());
                editor.putInt(AGE, age_value);
                editor.putInt(UNIT, unit_id);
                editor.commit();
            }
        });
    }
    public void read_age()
    {
        SharedPreferences sharedPref = getSharedPreferences(PATIENTPREFERENCES, Context.MODE_PRIVATE);
        int age_read = sharedPref.getInt(AGE,0);
        et_age.setText(age_read+"");
    }
    public void read_unit()
    {
        SharedPreferences sharedPref = getSharedPreferences(PATIENTPREFERENCES, Context.MODE_PRIVATE);
        int unit_read = sharedPref.getInt(UNIT,0);
        unit_spinner.setSelection(unit_read);
    }

}