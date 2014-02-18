package net.karmats.ilovegratis.activity;

import net.karmats.ilovegratis.ILoveGratisActivity;
import net.karmats.ilovegratis.R;
import net.karmats.ilovegratis.constant.ApplicationConstants;
import net.karmats.ilovegratis.dto.AdDto;
import net.karmats.ilovegratis.util.ILoveGratisUtil;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ViewAdActivity extends ILoveGratisActivity {

    private boolean adHasPhoneNumber;
    private AdDto ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewad);

        // Put admob ad on view
        createAdView((LinearLayout) findViewById(R.id.viewAdTopLayout));

        // Get the ad to view from the previous intent
        ad = (AdDto) getIntent().getExtras().get(ApplicationConstants.VIEW_AD_ID);

        // Get the image bitmap for this ad
        Bitmap image = ILoveGratisUtil.getImageBitmap(ILoveGratisUtil.convertImgThumbUrlToImgUrl(ad.getImgThumbSrc()));
        ImageView iv = (ImageView) findViewById(R.id.viewAdImage);
        iv.setImageBitmap(image);

        // Fill the view with values
        TextView headerView = (TextView) findViewById(R.id.viewAdHeader);
        headerView.setText(ad.getTitle());

        TextView descriptionView = (TextView) findViewById(R.id.viewAdDescription);
        descriptionView.setText(ad.getDescription());

        TextView uploadedByView = (TextView) findViewById(R.id.viewAdUploadedBy);
        uploadedByView.setText(ad.getName());

        TextView whereView = (TextView) findViewById(R.id.viewAdWhere);
        String whereText = ad.getCounty();
        if (ILoveGratisUtil.stringIsNotEmpty(ad.getMunicipality())) {
            whereText += ", " + ad.getMunicipality();
        }
        whereView.setText(whereText);

        TextView uploadedDateView = (TextView) findViewById(R.id.viewAdUploadedDate);
        uploadedDateView.setText(ILoveGratisUtil.getDateAsReadableString(ad.getDateUploaded(), getString(R.string.today), getString(R.string.yesterday)));

        TableRow phoneRow = (TableRow) findViewById(R.id.viewAdPhoneRow);
        TextView phoneNrView = (TextView) findViewById(R.id.viewAdPhoneNr);

        // If the ad has a phone number, enable the 'Ring' the menu item so the person can call the ad
        adHasPhoneNumber = ILoveGratisUtil.stringIsNotEmpty(ad.getPhoneNumber());
        if (adHasPhoneNumber) {
            phoneNrView.setText(ad.getPhoneNumber());
        } else {
            // If the ad doesn't have a phone number, don't display it
            phoneRow.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.viewAdMenuCall:
            try {
                // Call advertiser
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ad.getPhoneNumber()));
                startActivity(intent);
                return true;
            } catch (Exception e) {
                displayText(getString(R.string.callcouldnotbemade, ad.getPhoneNumber()));
                return false;
            }
        case R.id.viewAdMenuSendMail:
            // Send mail to advertiser
            Intent sendMailIntent = new Intent(ViewAdActivity.this, SendMailActivity.class);
            sendMailIntent.putExtra(ApplicationConstants.AD_ID, ad.getId());
            startActivity(sendMailIntent);
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewadmenu, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.getItemId() == R.id.viewAdMenuCall) {
                menuItem.setEnabled(adHasPhoneNumber);
            } else if (menuItem.getItemId() == R.id.viewAdMenuSendMail) {
                menuItem.setEnabled(ad.getId() != null);
            }
        }
        return true;
    }

}
