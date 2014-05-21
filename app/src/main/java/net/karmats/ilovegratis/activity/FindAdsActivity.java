package net.karmats.ilovegratis.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.karmats.ilovegratis.ILoveGratisActivity;
import net.karmats.ilovegratis.R;
import net.karmats.ilovegratis.constant.ApplicationConstants;
import net.karmats.ilovegratis.constant.County;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FindAdsActivity extends ILoveGratisActivity {

    private static final String COUNTY_COLUMN = "county";
    private static final String ARROW_COLUMN = "arrow";

    private static final String STRING_ARROW = ">";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findads);

        final String[] countyNames = County.getCountyNames();

        ListView mainView = (ListView) findViewById(R.id.counties);

        // The county data that contains a county name and '>' arrow
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (String county : countyNames) {
            Map<String, String> counties = new HashMap<String, String>();
            counties.put(COUNTY_COLUMN, county);
            counties.put(ARROW_COLUMN, STRING_ARROW);
            data.add(counties);
        }
        mainView.setAdapter(new SimpleAdapter(this, data, R.drawable.countyrow, new String[] { COUNTY_COLUMN, ARROW_COLUMN }, new int[] {
                R.id.countyNameColumn, R.id.arrowColumn }));
        mainView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View view, int position, long id) {
                // Send the county as extra for ads activity
                Intent adsIntent = new Intent(FindAdsActivity.this, AdsActivity.class);
                adsIntent.putExtra(ApplicationConstants.COUNTY_ID, countyNames[position]);
                startActivity(adsIntent);
            }

        });
    }

}
