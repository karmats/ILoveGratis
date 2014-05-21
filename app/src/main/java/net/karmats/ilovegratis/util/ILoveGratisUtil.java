package net.karmats.ilovegratis.util;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.karmats.ilovegratis.R;
import net.karmats.ilovegratis.constant.ApplicationConstants;
import net.karmats.ilovegratis.dto.AdUploadDto;
import net.karmats.ilovegratis.dto.MailDto;
import net.karmats.ilovegratis.exception.ValidationException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ILoveGratisUtil {

    /**
     * The default thumb image bitmap. Is set in TabActivity(The first that is started)
     */
    public static Bitmap DEFAULT_THUMB;

    /**
     * The default image bitmap. Is set in TabActivity.
     */
    public static Bitmap DEFAULT_IMAGE;

    private static String EMAIL_PATTERN = "[a-zA-Z0-9_.+\\-]+@[a-zA-Z0-9_.+\\-]+\\.[a-zA-Z]{2,9}";

    /**
     * Validates an {@link AdUploadDto}
     * 
     * @param ad
     *            The ad to validate
     * @throws ValidationException
     *             that contains the validation errors.
     */
    public static void validateAdUpload(AdUploadDto ad) throws ValidationException {
        List<Integer> validationErrorIds = new ArrayList<Integer>();
        // Name
        if (ad.getName().length() < 3 || ad.getName().length() > 20) {
            validationErrorIds.add(R.string.nameerror);
        }
        // Email
        Matcher emailMatcher = Pattern.compile(EMAIL_PATTERN).matcher(ad.getEmail());
        if (!emailMatcher.matches()) {
            validationErrorIds.add(R.string.emailerror);
        }
        // Place
        if (stringIsEmpty(ad.getCounty()) || stringIsEmpty(ad.getMunicipality())) {
            validationErrorIds.add(R.string.countymunicipalityerror);
        }
        // Title
        if (ad.getTitle().length() < 3) {
            validationErrorIds.add(R.string.titleerror);
        }
        // Category
        if (stringIsEmpty(ad.getCategory())) {
            validationErrorIds.add(R.string.categoryerror);
        }
        if (ad.getDescription().length() < 20) {
            validationErrorIds.add(R.string.descriptionerror);
        }

        // If the validation error ids isn't empty, throw exception
        if (!validationErrorIds.isEmpty()) {
            throw new ValidationException(validationErrorIds);
        }
    }

    public static void validateMailToSend(MailDto mail) throws ValidationException {
        List<Integer> validationErrorIds = new ArrayList<Integer>();
        // Name
        if (mail.getName().length() < 3 || mail.getName().length() > 20) {
            validationErrorIds.add(R.string.nameerror);
        }
        // Email
        Matcher emailMatcher = Pattern.compile(EMAIL_PATTERN).matcher(mail.getEmailAddress());
        if (!emailMatcher.matches()) {
            validationErrorIds.add(R.string.emailerror);
        }
        // Message
        if (mail.getMessage().length() < 10) {
            validationErrorIds.add(R.string.messageerror);
        }

        if (!validationErrorIds.isEmpty()) {
            throw new ValidationException(validationErrorIds);
        }

    }

    /**
     * @return True if the string is null or the string length equals 0.
     */
    public static boolean stringIsEmpty(String string) {
        return string == null || string.length() <= 0;
    }

    /**
     * @return True if the string isn't null and the string length is larger than 0
     */
    public static boolean stringIsNotEmpty(String string) {
        return string != null && string.length() > 0;
    }

    /**
     * Get a {@link Bitmap} based on an url.
     * 
     * @param url
     *            The url where the image is located.
     * @return A bitmap image
     */
    public static Bitmap getImageBitmap(String url) {
        if (url.contains(ApplicationConstants.IMG_DEFAULT)) {
            return DEFAULT_IMAGE;
        } else if (url.contains(ApplicationConstants.IMG_THUMB_DEFAULT)) {
            return DEFAULT_THUMB;
        }
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            bm = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            return bm;
        }
        return bm;
    }

    /**
     * Convert an img url to its thumb img url
     * 
     * @param url
     *            The img url
     * @return The img thumb url
     */
    public static String convertImgUrlToImgThumbUrl(String url) {
        if (url.contains(ApplicationConstants.IMG_DEFAULT)) {
            return ApplicationConstants.IMG_THUMB_DEFAULT;
        }
        return url.replace("annons", "anth");
    }

    /**
     * Convert a thumb img url to its img url
     * 
     * @param url
     *            The thumb img url
     * @return The img url
     */
    public static String convertImgThumbUrlToImgUrl(String url) {
        if (url.contains(ApplicationConstants.IMG_THUMB_DEFAULT)) {
            return ApplicationConstants.IMG_DEFAULT;
        }
        return url.replace("anth", "annons");
    }

    /**
     * Gets a readable string out of a date
     * 
     * @param date
     *            The date to get the readable string out of
     * @param today
     *            A string representation of the word today
     * @param yesterday
     *            A string representation of the word yesterday
     * @return A string representation of the date
     */
    public static String getDateAsReadableString(Date date, String today, String yesterday) {
        if (date == null) {
            return "";
        }
        Calendar now = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int cDate = c.get(Calendar.DAY_OF_YEAR);
        int nowDate = now.get(Calendar.DAY_OF_YEAR);
        // Number format for having atleast 2 digits on the time
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(2);
        nf.setGroupingUsed(false);
        // Today
        if (nowDate - cDate == 0) {
            return today + " " + nf.format(c.get(Calendar.HOUR_OF_DAY)) + ":" + nf.format(c.get(Calendar.MINUTE));
        } else if (nowDate - cDate == 1) { // Tomorrow
            return yesterday + " " + nf.format(c.get(Calendar.HOUR_OF_DAY)) + ":" + nf.format(c.get(Calendar.MINUTE));
        }
        return ApplicationConstants.DATE_FORMAT.format(date);
    }

    /**
     * Converts a length of bytes to MB.
     * 
     * @param bytes
     *            The bytes to convert
     * @return A string representation of the MB
     */
    public static String convertBytesToMB(long bytes) {
        double result = (double) bytes / 1000 / 1000;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        return nf.format(result) + " MB";
    }

}
