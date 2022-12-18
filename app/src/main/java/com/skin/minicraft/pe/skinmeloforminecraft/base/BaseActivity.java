package com.skin.minicraft.pe.skinmeloforminecraft.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.github.javiersantos.piracychecker.PiracyChecker;
import com.github.javiersantos.piracychecker.enums.InstallerID;
import com.skin.minicraft.pe.skinmeloforminecraft.BuildConfig;
import com.skin.minicraft.pe.skinmeloforminecraft.R;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BaseActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    private MaxRewardedAd rewardedAd;
    private int           retryAttempt;

    /*
    ini Applovin
     */
    void createRewardedAd()
    {
        String idReward =  getResources().getString(R.string.applovin_reward);
        rewardedAd = MaxRewardedAd.getInstance( idReward, this );
        rewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onAdLoaded(final MaxAd maxAd)
            {
                // Rewarded ad is ready to be shown. rewardedAd.isReady() will now return 'true'

                // Reset retry attempt
                retryAttempt = 0;
            }

            @Override
            public void onAdLoadFailed(final String adUnitId, final MaxError error)
            {
                // Rewarded ad failed to load
                // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        rewardedAd.loadAd();
                    }
                }, delayMillis );
            }

            @Override
            public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error)
            {
                // Rewarded ad failed to display. We recommend loading the next ad
                rewardedAd.loadAd();
            }

            @Override
            public void onAdDisplayed(final MaxAd maxAd) {}

            @Override
            public void onAdClicked(final MaxAd maxAd) {}

            @Override
            public void onAdHidden(final MaxAd maxAd)
            {
                // rewarded ad is hidden. Pre-load the next ad
                rewardedAd.loadAd();
            }

            @Override
            public void onRewardedVideoStarted(final MaxAd maxAd) {}

            @Override
            public void onRewardedVideoCompleted(final MaxAd maxAd) {}

            @Override
            public void onUserRewarded(final MaxAd maxAd, final MaxReward maxReward)
            {
                // Rewarded ad was displayed and user should receive the reward
            }
        });

        rewardedAd.loadAd();
    }

    private void initApplovin() {
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.getInstance(this).getSettings().setTestDeviceAdvertisingIds(Arrays.asList(getString(R.string.add_test)));
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
                createRewardedAd();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initApplovin();
        askForPermission(Manifest.permission.INTERNET, 101);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            if (requestCode == 101)
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    public void showAds() {
        if ( !BuildConfig.DEBUG && rewardedAd != null && rewardedAd.isReady()) {
            rewardedAd.showAd();
        }
    }

    protected  void checkLicense(){
        if (BuildConfig.DEBUG){
            return;
        }
        String BASE_64_LICENSE_KEY = getString(R.string.key_play_store);
        new PiracyChecker(this)
                .enableGooglePlayLicensing(BASE_64_LICENSE_KEY)
                .enableUnauthorizedAppsCheck()
                .display(com.github.javiersantos.piracychecker.enums.Display.DIALOG)
                .enableInstallerId(InstallerID.GOOGLE_PLAY)
                .saveResultToSharedPreferences("my_app_preferences", "valid_license")
                .start();
    }

}
