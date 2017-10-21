package com.droidkings.socialmedia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //Firebase
    Firebase mRootRef;
    ArrayList<String> mMessages = new ArrayList<>();

    //UI
    TextView mTextView ;
    ListView mListView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        mRootRef = new Firebase("https://socialmedia-c2250.firebaseio.com");
        mTextView = (TextView) findViewById(R.id.textView);
        mListView = (ListView) findViewById(R.id.listView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Firebase messagesRef  =  mRootRef.child("messages");
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Map<String , String> map = dataSnapshot.getValue(Map.class);
                String hello = map.get("hello");
                String world = map.get("world");
                // mTextView.setText(hello+ " " +world );
                Log.d("E_VALUE",hello + " " + world);
                
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
