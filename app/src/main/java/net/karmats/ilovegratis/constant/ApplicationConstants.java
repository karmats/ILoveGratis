package net.karmats.ilovegratis.constant;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ApplicationConstants {

    // DateFormats
    public static final SimpleDateFormat DATE_TIME_FORMAT_EN = new SimpleDateFormat("MMM d yyyy, HH:mm", Locale.ENGLISH);
    public static final SimpleDateFormat DATE_TIME_FORMAT_SV = new SimpleDateFormat("MMM d yyyy, HH:mm", new Locale("sv", "SE"));
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d yyyy");

    // Intent ids
    public static final String COUNTY_ID = "countyId";
    public static final String VIEW_AD_ID = "viewAdId";
    public static final String AD_TO_UPLOAD_ID = "adToUploadId";
    public static final String AD_ID = "adId";

    // URLs
    public static final String FIVE_LATEST_URL = "http://ilovegratis.se/rss.php";
    public static final String ADS_URL = "http://ilovegratis.se/lan.php?l=";
    public static final String ADS_PAGE_PARAMETER = "&page=";
    public static final String AD_DETAILS_URL = "http://ilovegratis.se/annons.php?id=";
    public static final String AD_IMG_SRC_START = "http://ilovegratis.se/";
    public static final String AD_UPLOAD_URL = "http://ilovegratis.se/new.php";
    public static final String MAIL_SEND_URL = "http://ilovegratis.se/mailadder.php";

    // Others
    public static final String IMG_THUMB_DEFAULT = "default_tumme";
    public static final String IMG_DEFAULT = "default_bild";

    public static final int ADS_PER_PAGE = 10;

}
