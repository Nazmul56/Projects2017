package bhh.youtube.channel;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

/**
 * An abstract activity which deals with recovering from errors which may occur during API
 * initialization, but can be corrected through user action.
 */
public abstract class YouTubeFailureRecoveryActivity extends YouTubeBaseActivity implements
    YouTubePlayer.OnInitializedListener {
  /**Firebase Variables*/
  protected FirebaseRemoteConfig mFirebaseRemoteConfig;
  private static final String YOUTUBE_API_KEY = "youtube_api_key";
  private static final String ANDROID_YOUTUBE_API_KEY = "android_youtube_api_key";
  private static final int RECOVERY_DIALOG_REQUEST = 1;

  @Override
  public void onInitializationFailure(YouTubePlayer.Provider provider,
      YouTubeInitializationResult errorReason) {
    if (errorReason.isUserRecoverableError()) {
      errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
    } else {
      String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
      Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == RECOVERY_DIALOG_REQUEST) {
      // Retry initialization if user performed a recovery action
      /**Remote Config Initialization*/

      mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
      FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
              .setDeveloperModeEnabled(true)
              .build();
      mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);

      mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

      getYouTubePlayerProvider().initialize(mFirebaseRemoteConfig.getString(ANDROID_YOUTUBE_API_KEY), this);  // USE  Remote Config API Key
    }
  }

  protected abstract YouTubePlayer.Provider getYouTubePlayerProvider();

}
