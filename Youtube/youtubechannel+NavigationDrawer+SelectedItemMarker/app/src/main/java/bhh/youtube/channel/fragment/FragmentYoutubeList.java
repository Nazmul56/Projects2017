package bhh.youtube.channel.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bhh.youtube.channel.ConnectionDetector;
import bhh.youtube.channel.DataManager;
import bhh.youtube.channel.FeedImageView;
import bhh.youtube.channel.GoogleProgress;
import bhh.youtube.channel.LruBitmapCache;
import bhh.youtube.channel.R;
import bhh.youtube.channel.YouTubePlayerActivity;
import bhh.youtube.channel.pojo.VideoPojo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment} factory method to
 * create an instance of this fragment.
 */
public class FragmentYoutubeList extends Fragment {

    private static final String KEY_MOVIE_TITLE = "key_title";
   RequestQueue mRequestQueue;
    ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;
    ImageLoader imageLoader;
    ListView lvvideos;
    String CHANNEL_ID;
    String YOUTUBE_URL = "";
    String NEXT_PAGE_TOKEN = "",PREV_PAGE_TOKEN="";
    ProgressDialog progress;
    int total = 0;
    ArrayList<VideoPojo> videolist = new ArrayList<VideoPojo>();
    Custom_Adapter_fragment adapter;
    boolean loadmore = false;
    TextView txtfooter;
    private AdView adView;
    private static final String AD_UNIT_ID = DataManager.ADMOB_BANNER;
    private InterstitialAd interstitial;
    ConnectionDetector connectionDetector;
    /**Firebase Variables*/
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private static final String YOUTUBE_API_KEY = "youtube_api_key";
    private static final String ANDROID_YOUTUBE_API_KEY = "android_youtube_api_key";

    // private TextView mWelcomeTextView;
    /** Data Manager Variabels*/
    String[] typenames, typeids,types;
    List<String> typelist = new ArrayList<String>();

    /**Expendable List Variables*/

    List<String> ChildList;
    public static Context contextmain;

    int chaild_ID ;

    public FragmentYoutubeList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment FragmentYoutubeList.
     */
    public static FragmentYoutubeList newInstance(int child) {
        FragmentYoutubeList FragmentYoutubeList = new FragmentYoutubeList();
        Bundle args = new Bundle();
        args.putInt(KEY_MOVIE_TITLE, child);
       // contextmain = context ;
        FragmentYoutubeList.setArguments(args);

        return FragmentYoutubeList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contextmain = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comedy, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvvideos = (ListView) view.findViewById(R.id.lvvideostest);
        txtfooter = (TextView) view.findViewById(R.id.txtfooter);
        txtfooter.setVisibility(View.GONE);

        chaild_ID = getArguments().getInt(KEY_MOVIE_TITLE);
        /**Data Manager Initialization*/
        types = getResources().getStringArray(R.array.type);
        typenames = getResources().getStringArray(R.array.channel_name);
        typeids = getResources().getStringArray(R.array.channel_ID);
        typelist = Arrays.asList(typeids);
        DataManager.selectedchannelid = typelist.get(chaild_ID).toString();
        DataManager.channelname = typenames[chaild_ID];
        DataManager.type=types[chaild_ID];
        CHANNEL_ID = DataManager.selectedchannelid;
        connectionDetector = new ConnectionDetector(getContext());
        /**Firebase Initialized Here */ // If we initialized this variables after here our app will go to loadVideos Without The API Key Which Cause Null Pointer Exception
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        fetchData();


       CallLoadVideo();

        lvvideos.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (lvvideos.getLastVisiblePosition() >= lvvideos.getCount() - 1) {
                        if (connectionDetector.isConnectingToInternet()) {
                            if (DataManager.type.equals("channel")) {
                                if (loadmore) {
                                    new loadvideos().execute();
                                    txtfooter.setText(" Loading more videos...");
                                    txtfooter.setVisibility(View.VISIBLE);
                                } else {
                                    txtfooter.setText("No More Videos");
                                    txtfooter.setVisibility(View.GONE);
                                }
                            } else if (DataManager.type.equals("playlist")) {
                                if (loadmore) {
                                    Log.d("load", "Loading More Videos---");
                                    new loadplaylistvideos().execute();
                                    txtfooter.setText(" Loading more videos...");
                                    txtfooter.setVisibility(View.VISIBLE);
                                } else {
                                    txtfooter.setText("No More Videos");
                                    txtfooter.setVisibility(View.GONE);
                                }
                            }
                        }
                    }else{
                        /** No Internet*/
                        txtfooter.setText("Wait ");
                        txtfooter.setVisibility(View.VISIBLE);
                    }
                }


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });

        lvvideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                DataManager.selectedvideoid = videolist.get(position).getVideoid();
                Intent i = new Intent(getActivity(), YouTubePlayerActivity.class);
                i.putExtra("video_id",DataManager.selectedvideoid);
                startActivity(i);
                //overridePendingTransition(0, 0);
            }
        });

    }

    public void CallLoadVideo()
    {

        if(connectionDetector.isConnectingToInternet()) {
            if (DataManager.type.equals("channel")) {
                Log.d("Call channel video", "call channel");

                new loadvideos().execute();
            } else if (DataManager.type.equals("playlist")) {
                Log.d("Call playlist video", "call playlist");
                new loadplaylistvideos().execute();
            }
        }
        else{
            nointernet();
        }
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
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Fetch Succeeded\n"+ mFirebaseRemoteConfig.getString(ANDROID_YOUTUBE_API_KEY),
                                    Toast.LENGTH_SHORT).show();
                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(getActivity(), "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //displayWelcomeMessage();
                    }
                });
        // [END fetch_config_with_callback]
    }


    private class loadvideos extends AsyncTask<Void, Void, Void> {
        boolean isconnect = false;

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request.
            if (!loadmore)
            {
                progress = GoogleProgress.Progressshow(getContext());
                progress.show();
            }
        }

        protected Void doInBackground(Void... unused) {
            try
            {
                if(!connectionDetector.isConnectingToInternet())
                {
                    nointernet();
                }
                else
                {
                    HttpClient client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(),
                            15000);
                    HttpConnectionParams.setSoTimeout(client.getParams(), 15000);

                    if (!loadmore)
                    {
                        YOUTUBE_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId="
                                + CHANNEL_ID
                                + "&type=video"
                                + "&maxResults="
                                + DataManager.maxResults + "&key=" + mFirebaseRemoteConfig.getString(ANDROID_YOUTUBE_API_KEY) + "&order=date";  // USE  Remote Config API Key
                    }
                    else
                    {
                        YOUTUBE_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&pageToken="
                                + NEXT_PAGE_TOKEN
                                + "&channelId="
                                + CHANNEL_ID
                                + "&type=video"
                                + "&maxResults="
                                + DataManager.maxResults
                                + "&key="
                                + mFirebaseRemoteConfig.getString(ANDROID_YOUTUBE_API_KEY) + "&order=date";   // USE  Remote Config API Key
                    }


                    HttpUriRequest request = new HttpGet(YOUTUBE_URL);

                    HttpResponse response = client.execute(request);

                    InputStream atomInputStream = response.getEntity().getContent();

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            atomInputStream));

                    String line;
                    String str = "";
                    while ((line = in.readLine()) != null) {
                        str += line;
                    }

                    System.out.println("url---" + YOUTUBE_URL);

                    JSONObject json = new JSONObject(str);
                    JSONArray items = json.getJSONArray("items");

                    total = json.getJSONObject("pageInfo").getInt("totalResults");
                    if (total > 10)
                    {
                        if(json.has("nextPageToken"))
                        {
                            loadmore = true;
                            NEXT_PAGE_TOKEN = json.getString("nextPageToken");

                        }
                    }

                    for (int i = 0; i < items.length(); i++) {

                        VideoPojo video = new VideoPojo();
                        JSONObject youtubeObject = items.getJSONObject(i).getJSONObject("snippet");


                        if (items.getJSONObject(i).getJSONObject("id").getString("videoId") != null) {

                            video.setVideoid(items.getJSONObject(i).getJSONObject("id").getString("videoId"));
                            video.setTitle(youtubeObject.getString("title"));
                            video.setThumbnail(youtubeObject.getJSONObject("thumbnails").getJSONObject("high").getString("url"));

                            videolist.add(video);

                        }

                    }

                    isconnect = true;
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                isconnect = false;
                System.out.println("1exception---" + e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                isconnect = false;
                System.out.println("2exception---" + e.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                System.out.println("3exception---" + e.getMessage());
                e.printStackTrace();
            }

            return (null);
        }

        protected void onPostExecute(Void unused) {
            // Closing progress dialog.
            progress.dismiss();
            if (isconnect) {
                if (videolist.size() > 0) {
                  //  displayInterstitial();

                    adapter = new Custom_Adapter_fragment(getContext());
                    lvvideos.setAdapter(adapter);
                   // getSupportActionBar().setTitle(typenames[1]);

                    if (loadmore)
                        lvvideos.setSelection(((videolist.size() - DataManager.maxResults)-1));
                    else
                        lvvideos.setSelection(0);


                    if (total > videolist.size()) {
                        loadmore = true;
                    }else
                    {
                        loadmore = false;
                    }
                }
            }
            else
            {
                nointernet();
            }
        }
        @Override
        protected void onCancelled() {
            // Log.d( String.format("mAsyncTask - onCancelled: isCancelled = %b, FlagCancelled = %b", this.isCancelled(), FlagCancelled ));
            super.onCancelled();
        }
    }
    private class loadplaylistvideos extends AsyncTask<Void, Void, Void> {
        boolean isconnect = false;

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request.
            if (!loadmore)
            {
                progress = GoogleProgress.Progressshow(getContext());
                progress.show();
            }
        }

        protected Void doInBackground(Void... unused) {

            try
            {
                if(!connectionDetector.isConnectingToInternet())
                {
                    nointernet();
                }
                else
                {

                    HttpClient client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(),
                            15000);
                    HttpConnectionParams.setSoTimeout(client.getParams(), 15000);

                    if (!loadmore)
                    {
                        YOUTUBE_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="
                                + CHANNEL_ID
                                + "&maxResults="
                                + DataManager.maxResults + "&key=" + mFirebaseRemoteConfig.getString(ANDROID_YOUTUBE_API_KEY)+"&order=date";  // USE  Remote Config API Key
                    }
                    else
                    {
                        YOUTUBE_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&pageToken="
                                + NEXT_PAGE_TOKEN
                                + "&playlistId="
                                + CHANNEL_ID
                                + "&maxResults="
                                + DataManager.maxResults
                                + "&key="
                                + mFirebaseRemoteConfig.getString(ANDROID_YOUTUBE_API_KEY)+"&order=date";    // USE  Remote Config API Key
                    }

                    HttpUriRequest request = new HttpGet(YOUTUBE_URL);

                    HttpResponse response = client.execute(request);

                    InputStream atomInputStream = response.getEntity().getContent();

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            atomInputStream));

                    String line;
                    String str = "";
                    while ((line = in.readLine()) != null) {
                        str += line;
                    }

                    Log.d("url---","URL" + YOUTUBE_URL);

                    JSONObject json = new JSONObject(str);
                    JSONArray items = json.getJSONArray("items");

                    Log.d("JSONARRAY---","JAONARRAY DATA " + items);

                    total = json.getJSONObject("pageInfo").getInt("totalResults");
                    Log.d("Total---","Total DATA " + total);
                    if (total > 10)
                    {

                        if(json.has("nextPageToken"))
                        {
                            loadmore = true;
                            NEXT_PAGE_TOKEN = json.getString("nextPageToken");

                        }

                    }

                    for (int i = 0; i < items.length(); i++)
                    {

                        VideoPojo video = new VideoPojo();
                        JSONObject youtubeObject = items.getJSONObject(i).getJSONObject("snippet");

                        Log.d("VIDEO ID---","VIDEO ID" + youtubeObject.getJSONObject("resourceId").getString("videoId"));
                        if (youtubeObject.getJSONObject("resourceId").getString("videoId") != null) {

                            video.setVideoid(youtubeObject.getJSONObject("resourceId").getString("videoId"));
                            video.setTitle(youtubeObject.getString("title"));
                            video.setThumbnail(youtubeObject.getJSONObject("thumbnails").getJSONObject("high").getString("url"));

                            videolist.add(video);

                        }

                    }

                    isconnect = true;
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                isconnect = false;
                System.out.println(" 1exception---" + e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                isconnect = false;
                System.out.println("2 exception---" + e.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                System.out.println("3 exception---" + e.getMessage());
                e.printStackTrace();
                //isconnect = false;
            }

            return (null);
        }

        protected void onPostExecute(Void unused) {
            // Closing progress dialog.
            progress.dismiss();
            if (isconnect)
            {
                if (videolist.size() > 0)
                {

                    Log.d("Video/list Size:-----","VideoListSize:---"+videolist.size());
                   // displayInterstitial();
                    adapter = new Custom_Adapter_fragment(getContext());
                    lvvideos.setAdapter(adapter);

                    if (loadmore)
                        lvvideos.setSelection(((videolist.size() - DataManager.maxResults)-1));
                    else
                        lvvideos.setSelection(0);


                    if (total > videolist.size())
                    {
                        loadmore = true;
                    }
                    else
                    {
                        loadmore = false;
                    }
                }
            }
            else
            {
                nointernet();
            }
        }
    }


    public void nointernet()
    {
        new AlertDialog.Builder(getContext())
                .setTitle("Connection Error")
                .setMessage("Try Again")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1)
                            {
                                CallLoadVideo();
                                arg0.dismiss();
                                arg0.cancel();
                            }
                        }).create().show();
    }
    public class Custom_Adapter_fragment extends BaseAdapter {

        private LayoutInflater mInflater;

        public Custom_Adapter_fragment(Context c) {
            mInflater = LayoutInflater.from(c);
            imageLoader = getImageLoader(c);
        }

        @Override
        public int getCount() {
            return videolist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.row_video_list, null);

                holder = new ViewHolder();

                holder.txttitle = (TextView) convertView
                        .findViewById(R.id.txttitle);

                holder.img = (FeedImageView) convertView.findViewById(R.id.img);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txttitle.setText("" + videolist.get(position).getTitle());

            holder.img.setImageUrl(videolist.get(position).getThumbnail(),
                    imageLoader);

            return convertView;
        }

        class ViewHolder {
            TextView txttitle;
            FeedImageView img;

        }

    }
    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader(Context context) {
        getRequestQueue(context);
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }


}
