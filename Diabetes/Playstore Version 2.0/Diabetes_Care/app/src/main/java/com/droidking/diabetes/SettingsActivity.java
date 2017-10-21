package com.droidking.diabetes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {

    private AppCompatDelegate mDelegate;
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            getDelegate().installViewFactory();
            getDelegate().onCreate(savedInstanceState);

            setContentView(R.layout.activity_settings);
            Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(actionbar);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SettingsActivity.this.finish();
                }
            });
          //  getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
            addPreferencesFromResource(R.xml.pref_notification);
            checkValues();
        }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    private void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
        ActionBar actionBar = getDelegate().getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }


    public static class MyPreferenceFragment extends PreferenceFragment
        {
            @Override
            public void onCreate(final Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.pref_data_sync);
            }
        }

        private void checkValues()
        {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String strUserName = sharedPrefs.getString("username", "NA");
            boolean bAppUpdates = sharedPrefs.getBoolean("applicationUpdates",false);
            String downloadType = sharedPrefs.getString("downloadType","1");

            String msg = "Cur Values: ";
            msg += "\n userName = " + strUserName;
            msg += "\n bAppUpdates = " + bAppUpdates;
            msg += "\n downloadType = " + downloadType;

            Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
        }
    }