package bhh.youtube.channel.fragment.navigation;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import bhh.youtube.channel.BuildConfig;
import bhh.youtube.channel.R;
import bhh.youtube.channel.VideoList;
import bhh.youtube.channel.fragment.FragmentAction;
import bhh.youtube.channel.fragment.FragmentYoutubeList;
import bhh.youtube.channel.fragment.FragmentDrama;
import bhh.youtube.channel.fragment.FragmentMusical;
import bhh.youtube.channel.fragment.FragmentThriller;

/**
 * @author msahakyan
 */

public class FragmentNavigationManager implements NavigationManager {

    private static FragmentNavigationManager sInstance;

    private FragmentManager mFragmentManager;
    private VideoList mActivity;

    public static FragmentNavigationManager obtain(VideoList activity) {
        if (sInstance == null) {
            sInstance = new FragmentNavigationManager();
        }
        sInstance.configure(activity);
        return sInstance;
    }

    private void configure(VideoList activity) {
        mActivity = activity;
        mFragmentManager = mActivity.getSupportFragmentManager();
    }

    @Override
    public void showFragmentAction(String title) {
        showFragment(FragmentAction.newInstance(title), false);
    }

    @Override
    public void showFragmentYoutubeList(int title) {
        showFragment(FragmentYoutubeList.newInstance(title), false);
    }

    @Override
    public void showFragmentDrama(int title) {
        showFragment(FragmentDrama.newInstance(title), false);
    }

    @Override
    public void showFragmentMusical(String title) {
        showFragment(FragmentMusical.newInstance(title), false);
    }

    @Override
    public void showFragmentThriller(String title) {
        showFragment(FragmentThriller.newInstance(title), false);
    }

    private void showFragment(Fragment fragment, boolean allowStateLoss) {
        FragmentManager fm = mFragmentManager;

        @SuppressLint("CommitTransaction")
        FragmentTransaction ft = fm.beginTransaction()
            .replace(R.id.container, fragment);

        ft.addToBackStack(null);

        if (allowStateLoss || !BuildConfig.DEBUG) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }

        fm.executePendingTransactions();
    }
}
