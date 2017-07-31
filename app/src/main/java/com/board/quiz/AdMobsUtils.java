package com.board.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

@SuppressWarnings("ALL")
public class AdMobsUtils {
    private Context ctx;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    public AdMobsUtils(Context ctx) {
        this.ctx = ctx;
        initInterstitialAd();
    }

    // Show Banner Ads
    public void showBannerAd(AdView mAdView) {
        this.mAdView = mAdView;
        mAdView.loadAd(getTestAdRequest());
        mAdView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                Log.e("Banner", "onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                Log.e("Banner", "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("Banner", "onAdFailedToLoad>>" + errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                Log.e("Banner", "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                Log.e("Banner", "onAdOpened");
            }
        });
    }

    @NonNull
    private AdRequest getAdRequest() {
        // For Production Purpose
        return new AdRequest.Builder().build();
    }

    private AdRequest getTestAdRequest() {
        // For Testing Purpose
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID and replace here
                .addTestDevice("BA4B473C2A586E01BDC375AA6AC98A7D")
                .build();
        return adRequest;
    }

    // Initialize InterstitialAd
    private void initInterstitialAd() {
        mInterstitialAd = new InterstitialAd(ctx);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(ctx.getString(R.string.interstitial_full_screen));
        loadInterstitialAds();
    }

    // Show Interstitial Ads
    public void showInterstitial() {
        // Show Ads
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            loadInterstitialAds();
        }
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                Log.e("Interstitial", "onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                Log.e("Interstitial", "onAdClosed");
                // Load the next interstitial.
                loadInterstitialAds();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("Interstitial", "onAdFailedToLoad>>" + errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                Log.e("Interstitial", "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                Log.e("Interstitial", "onAdOpened");
            }
        });
    }

    // Load Interstitial Ads
    private void loadInterstitialAds() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            mInterstitialAd.loadAd(getTestAdRequest());
        }
    }

    public AdView getAdView() {
        return mAdView;
    }

    public InterstitialAd getInterstitialAd() {
        return mInterstitialAd;
    }
}