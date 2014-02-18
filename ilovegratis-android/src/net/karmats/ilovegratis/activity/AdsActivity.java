package net.karmats.ilovegratis.activity;

import java.util.ArrayList;
import java.util.List;

import net.karmats.ilovegratis.ILoveGratisActivity;
import net.karmats.ilovegratis.R;
import net.karmats.ilovegratis.adapter.ListAdsAdapter;
import net.karmats.ilovegratis.adapter.ListAdsAdapter.ActivityName;
import net.karmats.ilovegratis.constant.ApplicationConstants;
import net.karmats.ilovegratis.dto.AdDto;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AdsActivity extends ILoveGratisActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ads);

        // Put admob ad on view
        createAdView((LinearLayout) findViewById(R.id.adsTopLayout));

        final String county = (String) getIntent().getExtras().get(ApplicationConstants.COUNTY_ID);

        // Sets the text 'Annonser i <county>'
        TextView header = (TextView) findViewById(R.id.adsHeader);
        header.setText(getString(R.string.listAdsHeader, county));

        final ListView mainView = (ListView) findViewById(R.id.ads);
        final ListAdsAdapter adapter = new ListAdsAdapter(this, R.drawable.adrow, new ArrayList<AdDto>(), ActivityName.ADS);
        final View footerView = getLayoutInflater().inflate(R.drawable.adsfooter, null);
        mainView.addFooterView(footerView);
        mainView.setAdapter(adapter);
        mainView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View view, int position, long id) {
                if (position >= adapter.getCount()) {
                    return;
                }
                // Get the details for this ad as an async task
                AdDto adDetails = (AdDto) adapter.getItem(position);
                new FetchAdDetailsTask().execute(adDetails);
            }
        });
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAds((adapter.getCount() / ApplicationConstants.ADS_PER_PAGE) + 1, adapter, county, footerView);
            }
        });
        fetchAds(1, adapter, county, footerView);
    }

    private void fetchAds(int page, ListAdsAdapter adapter, String county, View footer) {
        FetchAdsTask fa = new FetchAdsTask(adapter, county, footer);
        fa.execute(page);
    }

    /**
     * Async task that fires off when fetching ads from ilovegratis.se
     * 
     * @author mats
     * 
     */
    private class FetchAdsTask extends AsyncTask<Integer, Void, List<AdDto>> {
        private ListAdsAdapter adapter;
        private String county;
        private View footer;

        public FetchAdsTask(ListAdsAdapter adapter, String county, View footer) {
            this.adapter = adapter;
            this.county = county;
            this.footer = footer;
        }

        @Override
        protected void onPreExecute() {
            TextView footerText = (TextView) footer.findViewById(R.id.adsFooterText);
            footerText.setVisibility(View.GONE);
            ProgressBar footerProgress = (ProgressBar) footer.findViewById(R.id.listAdsFooterProgress);
            footerProgress.setVisibility(View.VISIBLE);
            footer.setClickable(false);
        }

        @Override
        protected List<AdDto> doInBackground(Integer... params) {
            try {
                return getAdService().getAdsForCounty(county, params[0]);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<AdDto> ads) {
            TextView footerText = (TextView) footer.findViewById(R.id.adsFooterText);
            footerText.setText(getString(R.string.listAdsFooterGetMore));
            footerText.setVisibility(View.VISIBLE);
            ProgressBar footerProgress = (ProgressBar) footer.findViewById(R.id.listAdsFooterProgress);
            footerProgress.setVisibility(View.GONE);
            if (ads == null) { // An exception was thrown
                displayText(getString(R.string.adsexception));
                return;
            }
            // If the ad result is less than ADS_PER_PAGE, we shouldn't be able to fetch any more ads
            if (ads.size() < ApplicationConstants.ADS_PER_PAGE) {
                footer.setVisibility(View.GONE);
            }
            footer.setClickable(true);
            adapter.refreshAds(ads);
        }

    }

}
