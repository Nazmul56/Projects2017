package bhh.youtube.channel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import bhh.youtube.channel.adapter.CustomExpandableListAdapter;
import bhh.youtube.channel.datasource.ExpandableListDataSource;
import bhh.youtube.channel.fragment.navigation.FragmentNavigationManager;
import bhh.youtube.channel.fragment.navigation.NavigationManager;
public class VideoList extends ActionBarActivity {

    boolean loadmore = false;
    TextView txtfooter;
    private AdView adView;
    private static final String AD_UNIT_ID = DataManager.ADMOB_BANNER;
    private InterstitialAd interstitial;
	ConnectionDetector connectionDetector;

    private static final String YOUTUBE_API_KEY = "youtube_api_key";
    private static final String ANDROID_YOUTUBE_API_KEY = "android_youtube_api_key";
    /**Variables For Remote Config*/
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
   // private TextView mWelcomeTextView;
    /** Data Manager Variabels*/
    String[] typenames, typeids,types;
    List<String> typelist = new ArrayList<String>();

    /**Expendable List Variables*/

    List<String> ChildList;

    /**Navigation Drawer Variables*/
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    public String mActivityTitle;
    private String[] items, allchannel;

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private NavigationManager mNavigationManager;
    View previousSelectedItem;

    private Map<String, List<String>> mExpandableListData;

    public static int item_no = 2 ;
    public static int group_no = 1 ;
    public static int prev_group_no = 1 ;
    public static int prev_child_no = 1 ;
    /**Child Selected Marker */
    public static int MAX_GROUP = 100;
    public static int MAX_CHILD = 100;
    public static boolean selectedStatus[][] = new boolean[MAX_GROUP][MAX_CHILD];




	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist_main);
        /**Firebase Initialized Here */ // If we initialized this variables after here our app will go to loadVideos Without The API Key Which Cause Null Pointer Exception
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        fetchData();

        /**Data Manager Initialization*/
        types = getResources().getStringArray(R.array.type);
        typenames = getResources().getStringArray(R.array.channel_name);
        typeids = getResources().getStringArray(R.array.channel_ID);
        typelist = Arrays.asList(typeids);

        /** ---*/
        /**Navigation Drawer Initialization*/
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        mExpandableListView = (ExpandableListView) findViewById(R.id.navList);
        mNavigationManager = FragmentNavigationManager.obtain(VideoList.this);

        initItems();

        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.nav_header, null, false);
        mExpandableListView.addHeaderView(listHeaderView);

        mExpandableListData = ExpandableListDataSource.getData(this);
        mExpandableListTitle = new ArrayList(mExpandableListData.keySet());

        addDrawerItems();

        setupDrawer();

        if (savedInstanceState == null) {
            selectFirstItemAsDefault();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd(adRequest);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ad);
        ll.addView(adView);

        // Begin loading your interstitial.
        AdRequest adRequest1 = new AdRequest.Builder().build();
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(DataManager.ADMOB_INTERSTIAL);

        interstitial.loadAd(adRequest1);
        AdListener adListener = new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();

            }
        };

        interstitial.setAdListener(adListener);

    }
    /**
     * Fetch welcome message from server.
     */
    private void fetchData() {
      //  mWelcomeTextView.setText(mFirebaseRemoteConfig.getString(YOUTUBE_API_KEY));

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
        // fetched and cached config would be considered expired because it would have been fetched
        // more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
        // throttling is in progress. The default expiration duration is 43200 (12 hours).
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VideoList.this, "Fetch Succeeded\n"+ mFirebaseRemoteConfig.getString(ANDROID_YOUTUBE_API_KEY),
                                   Toast.LENGTH_SHORT).show();
                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(VideoList.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //displayWelcomeMessage();
                    }
                });
        // [END fetch_config_with_callback]
    }


    private void initItems() {
        items = getResources().getStringArray(R.array.group_name);
        allchannel =getResources().getStringArray(R.array.channel_name);
    }

    private void addDrawerItems() {
        mExpandableListAdapter = new CustomExpandableListAdapter(this, mExpandableListTitle, mExpandableListData);
        mExpandableListView.setAdapter(mExpandableListAdapter);
       // mExpandableListView.expandGroup(group_no);
        countItems(mExpandableListData);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
               // if(groupPosition == 3)
                getSupportActionBar().setTitle(mExpandableListTitle.get(groupPosition).toString());

            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                getSupportActionBar().setTitle(R.string.film_genres);

            }
        });


        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {


            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String selectedItem = ((List) (mExpandableListData.get(mExpandableListTitle.get(groupPosition))))
                        .get(childPosition).toString();
                   getSupportActionBar().setTitle(selectedItem);
                loadmore = false;
                selectedStatus[prev_group_no][prev_child_no] = false;
                selectedStatus[groupPosition][childPosition] = true;
                prev_group_no=groupPosition;
                prev_child_no=childPosition;

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                group_no= groupPosition;
                parent.setItemChecked(index, true);

                int item_no = 0 ;
                for ( String HoldItem : allchannel) {
                    if(allchannel[item_no].equals(selectedItem)){
                        View my = mExpandableListView.getChildAt(childPosition);

                        mNavigationManager.showFragmentYoutubeList(item_no);
                        displayInterstitial();
                        break;
                    }else
                    {
                        item_no++;
                    }
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);


                return false;
            }
        });
    }

    public int countItems(Map<String, List<String>>  yourMap){

        int counter = 0;

        for(int i =0 ; i < yourMap.size(); i ++) {
            int j = 0 ;
            for (String child : yourMap.get(mExpandableListTitle.get(i)) ) {

                selectedStatus[i][j] = false ;
                j++;

            }
        }
        return counter;
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.film_genres);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    private void selectFirstItemAsDefault() {
        if (mNavigationManager != null) {
            //String firstActionMovie = getResources().getStringArray(R.array.actionFilms)[0];
            mNavigationManager.showFragmentYoutubeList(0);
            //mNavigationManager.showFragmentAction(firstActionMovie);
            //getSupportActionBar().setTitle(firstActionMovie);
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void nointernet()
    {
        new AlertDialog.Builder(this)
                .setTitle("Connection Error")
                .setMessage("Try Again")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                if(DataManager.type.equals("channel"))
                                {
                                   // new loadvideos().execute();
                                    arg0.dismiss();
                                }
                                else
                                {
                                  //  new loadplaylistvideos().execute();
                                    arg0.dismiss();
                                }
                                arg0.cancel();
                            }
                        }).create().show();
    }
    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                startActivity(intent);
                            }
                        }).create().show();

    }
}
