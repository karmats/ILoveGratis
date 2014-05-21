package net.karmats.ilovegratis.activity;

import java.util.ArrayList;
import java.util.List;

import net.karmats.ilovegratis.ILoveGratisActivity;
import net.karmats.ilovegratis.ILoveGratisApplication;
import net.karmats.ilovegratis.R;
import net.karmats.ilovegratis.adapter.ListAdsAdapter;
import net.karmats.ilovegratis.adapter.ListAdsAdapter.ActivityName;
import net.karmats.ilovegratis.dto.AdDto;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class FiveLatestActivity extends ILoveGratisActivity {

    private ListView mainView;
    private ListAdsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fivelatest);

        // Put admob ad on view
        createAdView((LinearLayout) findViewById(R.id.fiveLatestBottomLayout));

        adapter = new ListAdsAdapter(this, R.drawable.adrow, new ArrayList<AdDto>(), ActivityName.FIVE_LATEST);
        mainView = (ListView) findViewById(R.id.fiveLatestList);
        mainView.setAdapter(adapter);
        mainView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View view, int position, long id) {
                // Get the details for this ad as an async task
                AdDto ad = (AdDto) adapter.getItem(position);
                new FetchAdDetailsTask().execute(ad);
            }
        });

        List<AdDto> ads = ((ILoveGratisApplication) getApplication()).getFiveLatestAds();
        if (ads == null || ads.isEmpty()) {
            fetchAds();
        } else {
            adapter.refreshAds(ads);
            // Set ads in application to null, so the activity will get the ads from the server
            ((ILoveGratisApplication) getApplication()).setFiveLatestAds(null);
        }
    }

    private void fetchAds() {
        FetchAdsTask ffla = new FetchAdsTask(adapter);
        ffla.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fivelatestmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.fiveLatestMenuRefresh:
            fetchAds();
            return true;
        default:
            return false;
        }
    }

    /**
     * Async task that fires off when fetching ads from ilovegratis.se
     * 
     * @author mats
     * 
     */
    private class FetchAdsTask extends AsyncTask<Void, Void, List<AdDto>> {
        private ListAdsAdapter adapter;

        public FetchAdsTask(ListAdsAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            showDialog(FETCH_ADS_TASK);
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
            removeDialog(FETCH_ADS_TASK);
            if (ads == null) { // An exception was thrown
                displayText(getString(R.string.adsexception));
                return;
            } else if (ads.size() != 5) { // There was a problem getting all the ads
                displayText(getString(R.string.alladscouldnotbeparsed));
            }
            adapter.refreshAds(ads);
        }

    }
}
