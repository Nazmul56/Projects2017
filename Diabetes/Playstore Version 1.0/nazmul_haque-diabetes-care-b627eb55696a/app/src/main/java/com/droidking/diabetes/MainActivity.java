package com.droidking.diabetes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.droidking.diabetes.Excel.Team;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<Team> teams = new ArrayList<Team>();
    public static ListView listview;
    public static Button btnDownload;
    // public static final MediaType FORM_DATA_TYPE
    //        = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    /**
     * AsyncTask Variable
     */
   /* public static final String URL="https://docs.google.com/forms/d/13aS0Z652osolwweX3uxgW6352cUfczPZSs-dMOYfgfI/formResponse";
    //input element ids found from the live form page
    public static final String BEat_KEY="entry.1248647360";
    public static final String AEat_KEY="entry.206365149";
    public static final String BP_Systol_KEY = "entry.1229261796";
    public static final String BP_Dyastol_KEY = "entry.1323596523";
    */
    private static Context context;
    private static EditText Date;
    private static EditText Time;
    private static EditText Value;
    private static EditText Unit;
    private static EditText Measured;
    private static TextView Req_Unit;
    private static TextView Dialog_Date;
    private static TextView Dialog_Time;
    private static EditText Dialog_Value;
    private TextView dialogCancelButton;
    private TextView dialogAddButton;

    Spinner spCountries;
    public static Spinner Graph_Range;
    String  measured;
    public static PieChart mChart;
    private static FloatingActionButton fab;
    TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_timeline_black_24dp,
            R.drawable.ic_assignment_black_24dp,
            R.drawable.ic_extension_black_24dp
    };
    //History Fragment
    private static RecyclerView recyclerView;
    private static List<Movie> movieList = new ArrayList<>();
    private static MoviesAdapter mAdapter;
    /**
     * Diabetes Database KEYS
     */
    public static final String KEY_ROWID = "Row_ID";
    public static final String KEY_DATE = "Date";
    public static final String KEY_TIME = "Time";
    public static final String KEY_GLUCOSE_LEVEL = "Glucose_Level";
    public static final String KEY_UNIT = "Unit";
    public static final String KEY_STATUS = "Status";
    private Dialog addDialog;
    private Dialog timePicker;
    private Dialog datePicker;
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three from link
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        context = this;
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        context = getApplicationContext();
        //Tab Layout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void refreshFragment(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();
    }
    public void showAddDialog() {
        addDialog = new Dialog(MainActivity.this, R.style.AppTheme);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(addDialog.getWindow().getAttributes());
        addDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        addDialog.setContentView(R.layout.dialog_add);
        addDialog.getWindow().setAttributes(lp);
        addDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        addDialog.getWindow().setDimAmount(0.5f);
        addDialog.show();

        //spinnerReadingType = (LabelledSpinner) addDialog.findViewById(R.id.dialog_add_reading_type);
     //  spinnerReadingType.setItemsArray(R.array.dialog_add_measured_list);

        spCountries = (Spinner) addDialog.findViewById(R.id.spCountries);


       spCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                measured = adapter.getItemAtPosition(position).toString();
                // Showing selected spinner item

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        dialogCancelButton = (TextView) addDialog.findViewById(R.id.dialog_add_cancel);
        dialogAddButton = (TextView) addDialog.findViewById(R.id.dialog_add_add);
        dialogCancelButton.setOnClickListener(
                new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addDialog.dismiss();
            }
        });
        Dialog_Date = (TextView) addDialog.findViewById(R.id.dialog_add_date);
        Dialog_Time = (TextView) addDialog.findViewById(R.id.dialog_add_time);
        Dialog_Value = (EditText) addDialog.findViewById(R.id.dialog_add_concentration);
      //Unit = (EditText) findViewById(R.id.di);
        long current_time = System.currentTimeMillis();
        String current_date;
        SimpleDateFormat sdf_cd = new SimpleDateFormat("dd-MMM-yyyy");
        current_date = sdf_cd.format(current_time);
        Dialog_Date.setText(current_date);
      //SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String dateString = sdf.format(current_time);
        Dialog_Time.setText(dateString);
        Dialog_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        Dialog_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        dialogAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date, time, value, unit ;
                date = Dialog_Date.getText().toString();
                time = Dialog_Time.getText().toString();
                value = Dialog_Value.getText().toString();
                unit = "mmol/L ";                               /*** Unit*/

                //Make sure all the fields are filled with values
                if (TextUtils.isEmpty(date) ||
                        TextUtils.isEmpty(time) ||
                        TextUtils.isEmpty(value) ||
                        TextUtils.isEmpty(measured)) {
                    Toast.makeText(context, "Please Enter All Value.", Toast.LENGTH_LONG).show();
                    return;
                }
                //Update In DataBase
                DataBaseHelper info = new DataBaseHelper(context);
                try {
                    info.open();
                    info.add_Data(date, time, value, unit, measured);
                    info.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                addDialog.dismiss();
                refreshFragment();

            }

        });
    }
    public void showTimePickerDialog() {
        showDialog(888);
    }
    public void showDatePickerDialog() {
        showDialog(999);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        // showDate(year, month + 1, day);
        Calendar calendar;
        calendar = Calendar.getInstance();
        if (id == 999) {
            int year, month, day;
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, myDateListener, year, month, day);
        } else if (id == 888){
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(this, myTimeListener, hour, min, false);
        }else
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);

        }
    };
    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showTime(hourOfDay,minute);
        }
    };
    private void showDate(int year, int month, int day) {

       String Months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        Dialog_Date.setText(new StringBuilder().append(day).append("-")
                .append(Months[month-1]).append("-").append(year));
    }
    public void showTime(int hour, int min) {
        String format;
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
        Dialog_Time.setText(new StringBuilder().append(hour).append(":").append(min)
                .append(" ").append(format));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            Intent i = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {

                View rootView2 = inflater.inflate(R.layout.fragment_overview, container, false);
                LineChart chart = (LineChart) rootView2.findViewById(R.id.chart);

                mChart = (PieChart) rootView2.findViewById(R.id.pi_chart1);

                Graph_Range = (Spinner) rootView2.findViewById(R.id.chart_spinner);


                Graph_Range.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapter, View v,
                                               int position, long id) {
                        // On selecting a spinner item
                        String measured = adapter.getItemAtPosition(position).toString();
                        // Showing selected spinner item
                      //  Toast.makeText(getContext(),
                        //        "Measured at : " + measured, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                Legend legend = chart.getLegend();
                XAxis xAxis = chart.getXAxis();
                xAxis.setDrawGridLines(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(getResources().getColor(R.color.glucosio_text_light));
                xAxis.setAvoidFirstLastClipping(true);
                YAxis leftAxis = chart.getAxisLeft();



                //leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines

               /*leftAxis.addLimitLine(ll1);
               leftAxis.addLimitLine(ll2);
               leftAxis.addLimitLine(ll3);
               leftAxis.addLimitLine(ll4);
               */

                leftAxis.setTextColor(getResources().getColor(R.color.glucosio_text_light));
                leftAxis.setStartAtZero(false);
                //leftAxis.setYOffset(20f);
                leftAxis.disableGridDashedLine();
                leftAxis.setDrawGridLines(false);
                leftAxis.setValueFormatter(new AddUnitFormatter());


                // limit lines are drawn behind data (and not on top)
                leftAxis.setDrawLimitLinesBehindData(true);

                chart.getAxisRight().setEnabled(false);
                //Unit


                //Draw Grid
                chart.setBackgroundColor(Color.parseColor("#FFFFFF"));
                chart.setDescription("");
                chart.setGridBackgroundColor(Color.parseColor("#FFFFFF"));


                // setData();
                ArrayList<String> xVals = new ArrayList<String>();
                ArrayList<Integer> colors = new ArrayList<>();
                ArrayList<Entry> yVals = new ArrayList<Entry>();
                //Set Data Value /


                Movie movie;
                DataBaseHelper info = new DataBaseHelper(context);

                try {
                    info.open();

                    Cursor myCursor = info.getRecyclerData_table();

                    int iDate = myCursor.getColumnIndex(KEY_DATE);
                    int iTime = myCursor.getColumnIndex(KEY_TIME);

                    int iGlucose_Level = myCursor.getColumnIndex(KEY_GLUCOSE_LEVEL);
                    int iUnit = myCursor.getColumnIndex(KEY_UNIT);
                    int iMeasured = myCursor.getColumnIndex(KEY_STATUS);

                    int i =0 ;
                    for (myCursor.moveToFirst(); !myCursor.isAfterLast(); myCursor.moveToNext()) {

                        //movie = new Movie(myCursor.getString(iDate) + " " + myCursor.getString(iTime), myCursor.getString(iMeasured), myCursor.getString(iGlucose_Level) + myCursor.getString(iUnit));

                        xVals.add( /*myCursor.getString(iDate)+*/" "+myCursor.getString(iTime));
                        float val = Float.parseFloat(myCursor.getString(iGlucose_Level)) ;
                        yVals.add(new Entry(val, i++));
                    }

                    info.close();
             /*
               Movie movie = new Movie("7/21/2016 12:00 am", "After Breakfast", "8.6dmol/L");
               movieList.add(movie);

               movie = new Movie("7/21/2016 1:00 pm", "Before Lunch", "8.0dmol/L");
               movieList.add(movie);
               movie = new Movie("7/21/2016 3:00 pm ", "After Lunch", "9.10dmol/L");
               movieList.add(movie);*/


                } catch (SQLException e) {
                    e.printStackTrace();
                }


                /*for (int i = 0; i < 15; i++) {
                    xVals.add("" + i);
                    float val = i;
                    yVals.add(new Entry(val, i));
                }*/

                LineDataSet set1 = new LineDataSet(yVals, "");
                // set the line to be drawn like this "- - - - - -"
                set1.setColor(getResources().getColor(R.color.glucosio_pink));
                set1.setCircleColors(colors);
                set1.setLineWidth(0f);
                set1.setCircleSize(2.8f);
                set1.setDrawCircleHole(false);
                set1.disableDashedLine();
                set1.setFillAlpha(255);
                set1.setDrawFilled(true);
                set1.setValueTextSize(0);
                set1.setValueTextColor(Color.parseColor("#FFFFFF"));
                set1.setFillColor(Color.parseColor("#FCE2EA"));
                colors.add(getResources().getColor(R.color.glucosio_reading_ok));
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    set1.setDrawFilled(false);
                    set1.setLineWidth(3f);
                    set1.setCircleSize(4.5f);
                    set1.setDrawCircleHole(true);

                }

                ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                dataSets.add(set1); // add the datasets

                LineData data = new LineData(xVals, dataSets);

                chart.setData(data);
                chart.setPinchZoom(true);
                chart.setHardwareAccelerationEnabled(true);
                chart.animateY(1000, Easing.EasingOption.EaseOutCubic);

                legend.setEnabled(false);

   /*
                // x-axis limit line
                LimitLine ll1 = new LimitLine(15f, "Upper Limit");
                ll1.setLineWidth(4f);
                ll1.enableDashedLine(10f, 10f, 0f);
                ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                ll1.setTextSize(10f);


                LimitLine ll2 = new LimitLine(5f, "Lower Limit");
                ll2.setLineWidth(4f);
                ll2.enableDashedLine(10f, 10f, 0f);
                ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                ll2.setTextSize(10f);

                leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                leftAxis.addLimitLine(ll1);
                leftAxis.addLimitLine(ll2);
    */
                DataBaseHelper info1 = new DataBaseHelper(getContext());
                try {
                    info1.open();
                   // String value = info1.getData();
                    info1.close();
                    TextView tv = (TextView) rootView2.findViewById(R.id.item_history_reading);
                   // tv.setText(value);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                mChart.setUsePercentValues(true);
                mChart.setDescription("");
                mChart.setExtraOffsets(5, 10, 5, 5);
                mChart.setDragDecelerationFrictionCoef(0.95f);

                //mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
             //   mChart.setCenterText(generateCenterSpannableText());
                mChart.setCenterText("65%");
                mChart.setDrawHoleEnabled(true);
                mChart.setHoleColor(Color.WHITE);

                mChart.setTransparentCircleColor(Color.WHITE);
                mChart.setTransparentCircleAlpha(110);

                mChart.setHoleRadius(58f);
                mChart.setTransparentCircleRadius(61f);

                mChart.setDrawCenterText(true);

                mChart.setRotationAngle(0);
                // enable rotation of the chart by touch
                mChart.setRotationEnabled(true);
                mChart.setHighlightPerTapEnabled(true);
               // mChart.setOnChartValueSelectedListener(getContext());

                setData(3, 100);

                mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                // mChart.spin(2000, 0, 360);

                Legend l = mChart.getLegend();
                l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
                l.setXEntrySpace(7f);
                l.setYEntrySpace(0f);
                l.setYOffset(0f);

                return rootView2;

            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {

                /**   View rootView2 = inflater.inflate(R.layout.fragment_list, container, false);

                 listview = (ListView) rootView2.findViewById(R.id.listview);
                 btnDownload = (Button) rootView2.findViewById(R.id.btnDownload);
                 //ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                 // NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                 btnDownload.setEnabled(true);
                 */

                View rootView2 = inflater.inflate(R.layout.fragment_history, container, false);
                recyclerView = (RecyclerView) rootView2.findViewById(R.id.fragment_history_recycler_view);
                mAdapter = new MoviesAdapter(movieList);

                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(super.getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Movie movie = movieList.get(position);
                        Toast.makeText(getContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Movie movie = movieList.get(position);
                        Toast.makeText(getContext(), movie.getTitle() + " Long Click", Toast.LENGTH_SHORT).show();
                    }
                }));

                movieList.clear();
                Movie movie;
                DataBaseHelper info = new DataBaseHelper(context);

                try {
                    info.open();

                    Cursor myCursor = info.getRecyclerData_table();

                    int iDate = myCursor.getColumnIndex(KEY_DATE);
                    int iTime = myCursor.getColumnIndex(KEY_TIME);

                    int iGlucose_Level = myCursor.getColumnIndex(KEY_GLUCOSE_LEVEL);
                    int iUnit = myCursor.getColumnIndex(KEY_UNIT);
                    int iMeasured = myCursor.getColumnIndex(KEY_STATUS);

                    for (myCursor.moveToLast(); !myCursor.isBeforeFirst(); myCursor.moveToPrevious()) {

                        movie = new Movie(myCursor.getString(iDate) + " " + myCursor.getString(iTime), myCursor.getString(iMeasured), myCursor.getString(iGlucose_Level) + myCursor.getString(iUnit));

                        movieList.add(movie);
                    }

                    info.close();
             /*
               Movie movie = new Movie("7/21/2016 12:00 am", "After Breakfast", "8.6dmol/L");
               movieList.add(movie);

               movie = new Movie("7/21/2016 1:00 pm", "Before Lunch", "8.0dmol/L");
               movieList.add(movie);
               movie = new Movie("7/21/2016 3:00 pm ", "After Lunch", "9.10dmol/L");
               movieList.add(movie);*/


                } catch (SQLException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();

                return rootView2;
            } else {

                //fab.hide();
                View rootView1 = inflater.inflate(R.layout.fragment_add, container, false);


                Button sendButton = (Button) rootView1.findViewById(R.id.bSubmit);
                Date = (EditText) rootView1.findViewById(R.id.etDate);
                Time = (EditText) rootView1.findViewById(R.id.etTime);
                Value = (EditText) rootView1.findViewById(R.id.etGLucoseLevel);
                Unit = (EditText) rootView1.findViewById(R.id.etUnit);
                Measured = (EditText) rootView1.findViewById(R.id.etStatus);
                Req_Unit =(TextView) rootView1.findViewById(R.id.tvRequiredUnit);

                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date, time, value, unit, measured;
                        date = Date.getText().toString();
                        time = Time.getText().toString();
                        value = Value.getText().toString();
                        unit = Unit.getText().toString();
                        measured = Measured.getText().toString();

                        //Make sure all the fields are filled with values
                        if (TextUtils.isEmpty(date) ||
                                TextUtils.isEmpty(time) ||
                                TextUtils.isEmpty(value) ||
                                TextUtils.isEmpty(unit) ||
                                TextUtils.isEmpty(measured)) {
                            Toast.makeText(context, "Every fields are mandatory.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Float currentBG, targetBG, carbIntake, carbInsulinRatio, corrFactor,req;
                        int result;
                       currentBG  = Float.parseFloat(date);
                        targetBG = Float.parseFloat(time);
                        carbIntake = Float.parseFloat(value);
                        carbInsulinRatio = Float.parseFloat(unit);
                        corrFactor = Float.parseFloat(measured);
                        //Update In DataBase

                      /*  DataBaseHelper info = new DataBaseHelper(context);
                        try {
                            info.open();
                            info.add_Data(date, time, value, unit, measured);
                            info.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }*/



                        req = (carbIntake/carbInsulinRatio);
                        result = req.intValue();
                        req = (currentBG-targetBG)/corrFactor;
                        result = result+ req.intValue() ;
                        Req_Unit.setText(result+"");
                        /*Date.setText("");
                        Time.setText("");
                        Value.setText("");
                        Unit.setText("");
                        Measured.setText("");*/

                    }
                });


                /**  Button sendButton = (Button)rootView1.findViewById(R.id.bSubmit);
                 BEatEditText = (EditText)rootView1.findViewById(R.id.etDate);
                 AEatEditText = (EditText)rootView1.findViewById(R.id.etSugarLevel);
                 BP_SystolEditText = (EditText) rootView1.findViewById(R.id.etBPSystol);
                 BP_DyastolEditText = (EditText) rootView1.findViewById(R.id.etBPDiaystol);

                 sendButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                //Make sure all the fields are filled with values
                if (TextUtils.isEmpty(BEatEditText.getText().toString()) &&
                TextUtils.isEmpty(AEatEditText.getText().toString()) &&
                TextUtils.isEmpty(BP_SystolEditText.getText().toString()) &&
                TextUtils.isEmpty(BP_DyastolEditText.getText().toString())) {
                Toast.makeText(context, "Any fields are mandatory.", Toast.LENGTH_LONG).show();
                return;
                }
                //Create an object for PostDataTask AsyncTask
                PostDataTask postDataTask = new PostDataTask();

                //execute asynctask
                postDataTask.execute(URL, BEatEditText.getText().toString(),
                AEatEditText.getText().toString(),
                BP_SystolEditText.getText().toString(),
                BP_DyastolEditText.getText().toString());
                }
                });*/
                return rootView1;
            }


        }

        private void setData(int count, float range) {

            float mult = range;

            ArrayList<Entry> yVals1 = new ArrayList<Entry>();

            // IMPORTANT: In a PieChart, no values (Entry) should have the same
            // xIndex (even if from different DataSets), since no values can be
            // drawn above each other.
            for (int i = 0; i < count + 1; i++) {
                yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
            }

            ArrayList<String> xVals = new ArrayList<String>();
            String[] mParties = new String[] {
                    "Breakfast", "Lunch", "Dinner", "Other", "Party E", "Party F", "Party G", "Party H",
                    "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
                    "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
                    "Party Y", "Party Z"
            };
            for (int i = 0; i < count + 1; i++)
                xVals.add(mParties[i % mParties.length]);

            PieDataSet dataSet = new PieDataSet(yVals1, "progress");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);

            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());

            dataSet.setColors(colors);
            //dataSet.setSelectionShift(0f);

            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            //data.setValueTypeface(tf);
            mChart.setData(data);

            // undo all highlights
            mChart.highlightValues(null);

            mChart.invalidate();
        }



        private SpannableString generateCenterSpannableText() {

            SpannableString s = new SpannableString("65%\n");
            s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
            s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
          //  s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
            //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
           // s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
           // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
            return s;
        }

        @Override
        public void onResume() {
            super.onResume();

            android.app.Fragment currentFragment = getActivity().getFragmentManager().findFragmentById(R.id.container);
            /*int tabNumber = getArguments().getInt(ARG_SECTION_NUMBER) ;
            if(tabNumber == 3)
            {
                fab.hide();
            }
            else
            {
                fab.show();
            }*/
           // Toast.makeText(getContext(), "Tab Changed to: "+tabNumber , Toast.LENGTH_SHORT).show();
            // Reload current fragment
           // Toast.makeText(getContext(), "Resumed", Toast.LENGTH_LONG).show();

        }

    }


    @SuppressWarnings("StatementWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }
        } else if (id == R.id.nav_gallery) {

            Intent j = new Intent(MainActivity.this, Patient_informationActivity.class);
            startActivity(j);

        } else if (id == R.id.nav_manage) {
            Intent j = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(j);
        } else if(id == R.id.nav_reminder)
        {
            Intent i = new Intent(MainActivity.this, ReminderActivity.class);
            startActivity(i);
        }
        else if (id == R.id.about) {
            Intent i = new Intent(MainActivity.this, About.class);
            startActivity(i);

        } else if (id == R.id.licence) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";//"OverView";
                case 1:
                    return "";//"History";
                case 2:
                    return "";//"Reports";

            }
            return null;
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

  /*  private static class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String BEat = contactData[1];
            String AEat = contactData[2];
            String BPSystol = contactData[3];
            String BPDystol = contactData[4];
            String postBody="";
            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = BEat_KEY+"=" + URLEncoder.encode(BEat, "UTF-8") +
                        "&" + AEat_KEY + "=" + URLEncoder.encode(AEat,"UTF-8")+
                        "&" + BP_Systol_KEY + "=" + URLEncoder.encode(BPSystol,"UTF-8") +
                "&" + BP_Dyastol_KEY + "=" + URLEncoder.encode(BPDystol,"UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result=false;
            }
            try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            }catch (IOException exception){
                result=false;
            }
            return result;
        }
        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            Toast.makeText(context, result ? "Message successfully sent!" : "There was some error in sending message. Please try again after some time.", Toast.LENGTH_LONG).show();
        }
    }*/
}
