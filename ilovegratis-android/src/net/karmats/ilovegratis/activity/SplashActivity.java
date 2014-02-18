package net.karmats.ilovegratis.activity;

import java.util.List;

import net.karmats.ilovegratis.ILoveGratisActivity;
import net.karmats.ilovegratis.ILoveGratisApplication;
import net.karmats.ilovegratis.ILoveGratisTabs;
import net.karmats.ilovegratis.R;
import net.karmats.ilovegratis.dto.AdDto;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class SplashActivity extends ILoveGratisActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new FetchFiveLatestAdsTask().execute();

    }

    public void exitSplash() {
        startActivity(new Intent(this, ILoveGratisTabs.class));
        finish();
    }

    /**
     * Async task that fires off when fetching ads from ilovegratis.se
     * 
     * @author mats
     * 
     */
    private class FetchFiveLatestAdsTask extends AsyncTask<Void, Void, List<AdDto>> {

        public FetchFiveLatestAdsTask() {
        }

        @Override
        protected List<AdDto> doInBackground(Void... params) {
            try {
                return getAdService().getFiveLatestAds();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<AdDto> ads) {
            if (ads == null) { // An exception was thrown
                displayText(getString(R.string.adsexception));
            } else if (ads.size() != 5) { // There was a problem getting all the ads
                displayText(getString(R.string.alladscouldnotbeparsed));
            }
            // Set the five latest ads the application context
            ((ILoveGratisApplication) getApplication()).setFiveLatestAds(ads);
            exitSplash();
        }

    }
}
