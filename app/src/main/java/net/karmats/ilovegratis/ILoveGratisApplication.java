package net.karmats.ilovegratis;

import java.util.List;

import net.karmats.ilovegratis.dto.AdDto;
import android.app.Application;

/**
 * Applicaton class
 */
public class ILoveGratisApplication extends Application {

    private List<AdDto> fiveLatestAds;

    public List<AdDto> getFiveLatestAds() {
        return fiveLatestAds;
    }

    public void setFiveLatestAds(List<AdDto> fiveLatestAds) {
        this.fiveLatestAds = fiveLatestAds;
    }

}
