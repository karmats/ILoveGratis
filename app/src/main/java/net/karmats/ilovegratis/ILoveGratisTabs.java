package net.karmats.ilovegratis;

import net.karmats.ilovegratis.activity.FindAdsActivity;
import net.karmats.ilovegratis.activity.FiveLatestActivity;
import net.karmats.ilovegratis.activity.RegisterAdActivity;
import net.karmats.ilovegratis.util.ILoveGratisUtil;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

public class ILoveGratisTabs extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Set the default image and image thumb for ILoveGratisUtil
        ILoveGratisUtil.DEFAULT_THUMB = BitmapFactory.decodeResource(getResources(), R.drawable.default_tumme);
        ILoveGratisUtil.DEFAULT_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.default_bild);

        ViewGroup vg = (ViewGroup) findViewById(android.R.id.tabcontent);
        vg.setPadding(0, 0, 0, 0);

        Resources res = getResources(); // Resource object to get Drawables
        final TabHost tabHost = getTabHost(); // The activity TabHost
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        // Find ads activity
        intent = new Intent().setClass(this, FiveLatestActivity.class);
        spec = tabHost.newTabSpec("fiveLatestTab").setIndicator(res.getString(R.string.fiveLatestTabTitle), res.getDrawable(R.drawable.ic_tab_fivelatest))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Find ads activity
        intent = new Intent().setClass(this, FindAdsActivity.class);
        spec = tabHost.newTabSpec("findAdsTab").setIndicator(res.getString(R.string.findAdsTabTitle), res.getDrawable(R.drawable.ic_tab_find))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Register ad activity
        intent = new Intent().setClass(this, RegisterAdActivity.class);
        spec = tabHost.newTabSpec("registerAdTab").setIndicator(res.getString(R.string.registerAdTabTitle), res.getDrawable(R.drawable.ic_tab_register))
                      .setContent(intent);
        tabHost.addTab(spec);

        TabWidget tw = getTabWidget();

        for (int i = 0; i < tw.getChildCount(); i++) {
            View view = (View) tw.getChildAt(i);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.tabindicator));
        }
    }
}