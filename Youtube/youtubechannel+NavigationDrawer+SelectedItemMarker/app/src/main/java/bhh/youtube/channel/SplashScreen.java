package bhh.youtube.channel;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends Activity {

    ImageView img;
    TextView txtwelcome;
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 3000; // 3 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        img = (ImageView)findViewById(R.id.imageView);
        txtwelcome = (TextView)findViewById(R.id.textView);

        animation();


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                if (!mIsBackButtonPressed) {
                    Intent i = new Intent(SplashScreen.this, VideoList.class);
                    finish();
                    startActivity(i);
                    overridePendingTransition(0,0);


                }

            }

        }, SPLASH_DURATION); // time in milliseconds (1 second = 1000
        // milliseconds) until the run() method will
        // be called
    }
    @Override
    public void onBackPressed() {

        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();

    }

    private void animation() {

        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(img, "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(1200);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(img, "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(1200);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(img, "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(1200);
        ObjectAnimator textanimation = ObjectAnimator.ofFloat(txtwelcome, "alpha", 0.0F, 1.0F);
        textanimation.setStartDelay(1700);
        textanimation.setDuration(500);
        textanimation.start();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation).with(textanimation);
        animatorSet.setStartDelay(500);
        animatorSet.start();

    }

}
