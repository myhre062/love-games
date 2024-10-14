package com.example.lovegames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.lovegames.compose.MainScreen
import com.example.lovegames.compose.NavigationComponent
import com.example.lovegames.ui.theme.LoveGamesTheme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Mobile Ads SDK]
        lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@MainActivity)
        }

        // Load the interstitial ad
//        loadInterstitialAd()

        enableEdgeToEdge()
        setContent {
            LoveGamesTheme {
                val navController = rememberNavController()
                NavigationComponent(
                    navController = navController,
                    showInterstitialAd = { showInterstitialAd() }
                )
            }
        }
    }

    // Function to load the interstitial ad
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            }
        )
    }

    // Function to show the interstitial ad when it's available
    fun showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
            // Reload the next ad after showing the current one
            loadInterstitialAd()
        } else {
            // Optionally, handle the case when the ad is not loaded (e.g., show a message)
            println("Interstitial ad was not ready.")
        }
    }
}


@Preview
@Composable
fun DefaultPreview() {
    LoveGamesTheme {
        MainScreen(navController = rememberNavController())
    }
}