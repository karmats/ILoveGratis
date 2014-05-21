package net.karmats.ilovegratis;

import java.util.List;

import net.karmats.ilovegratis.activity.ViewAdActivity;
import net.karmats.ilovegratis.constant.ApplicationConstants;
import net.karmats.ilovegratis.dto.AdDto;
import net.karmats.ilovegratis.service.AdService;
import net.karmats.ilovegratis.service.AdServiceImpl;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Toast;

public class ILoveGratisActivity extends Activity {

    private static final String AD_UNIT_ID = "a14d7def41ea393";

    protected static final int FETCH_ADS_TASK = 1;
    protected static final int REGISTER_AD_TASK = 2;
    protected static final int FETCH_AD_DETAILS_TASK = 3;
    protected static final int SEND_MAIL_TASK = 4;

    protected ProgressDialog progressDialog;

    /**
     * @return The {@link AdService}
     */
    protected AdService getAdService() {
        // return new AdServiceMock();
        return AdServiceImpl.getInstance();
    }

    /**
     * Toasts a text.
     * 
     * @param textToDislplay
     *            The text to display
     */
    protected void displayText(String textToDislplay) {
        Toast toast = Toast.makeText(this, textToDislplay, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    /**
     * Creates a validation error string.
     * 
     * @param validationErrorIds
     *            The resource error ids.
     * @return A string representation of the validation errors
     */
    protected String createValidationErrorString(List<Integer> validationErrorIds) {
        StringBuilder sb = new StringBuilder();
        for (Integer validationErrorId : validationErrorIds) {
            sb.append("* ").append(getString(validationErrorId)).append("\n");
        }
        return sb.toString();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        progressDialog = new ProgressDialog(this);
        switch (id) {
        case FETCH_ADS_TASK:
            progressDialog.setMessage(getString(R.string.fiveLatestProgress));
            break;
        case FETCH_AD_DETAILS_TASK:
            progressDialog.setMessage(getString(R.string.viewAdProgress));
            break;
        case REGISTER_AD_TASK:
            progressDialog.setMessage(getString(R.string.confirmAdProgress));
            break;
        case SEND_MAIL_TASK:
            progressDialog.setMessage(getString(R.string.sendMailProgress));
            break;
        default:
            return super.onCreateDialog(id);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * Creates an AdView under the defined parent.
     * 
     * @param parent
     *            The parent view
     */
    protected void createAdView(ViewGroup parent) {
        // Create the adView
//        AdView adView = new AdView(this, AdSize.BANNER, AD_UNIT_ID);
//        // Add the adView to parent
//        parent.addView(adView);
//        // Initiate a generic request to load it with an ad
//        AdRequest request = new AdRequest();
//        adView.loadAd(request);
    }

    /**
     * Async task that fires off when fetching ad details from ilovegratis.se
     * 
     * @author mats
     * 
     */
    protected class FetchAdDetailsTask extends AsyncTask<AdDto, Void, AdDto> {

        public FetchAdDetailsTask() {
        }

        @Override
        protected void onPreExecute() {
            showDialog(FETCH_AD_DETAILS_TASK);
        }

        @Override
        protected AdDto doInBackground(AdDto... params) {
            try {
                return params[0];//getAdService().getAdDetailsForAd(params[0]);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(AdDto adDetails) {
            removeDialog(FETCH_AD_DETAILS_TASK);
            if (adDetails == null) { // Exception has been thrown
                displayText(getString(R.string.adnotfound));
                return;
            } else {
                // Set the imgThumb to null since it's not serializeable
                adDetails.setImgThumb(null);
                // Start ViewAdActivity
                Intent viewAdIntent = new Intent(ILoveGratisActivity.this, ViewAdActivity.class);
                viewAdIntent.putExtra(ApplicationConstants.VIEW_AD_ID, adDetails);
                startActivity(viewAdIntent);
            }
        }

    }

}
