package net.karmats.ilovegratis.activity;

import net.karmats.ilovegratis.ILoveGratisActivity;
import net.karmats.ilovegratis.R;
import net.karmats.ilovegratis.constant.ApplicationConstants;
import net.karmats.ilovegratis.dto.MailDto;
import net.karmats.ilovegratis.exception.ValidationException;
import net.karmats.ilovegratis.util.ILoveGratisUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SendMailActivity extends ILoveGratisActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendmail);

        // Put admob ad on view
        createAdView((LinearLayout) findViewById(R.id.sendMailTopLayout));

        // Get the ad id
        final Long adId = getIntent().getLongExtra(ApplicationConstants.AD_ID, 0L);

        final EditText nameInput = (EditText) findViewById(R.id.sendMailNameInput);
        final EditText emailInput = (EditText) findViewById(R.id.sendMailEmailInput);
        final EditText messageInput = (EditText) findViewById(R.id.sendMailMessageInput);

        Button sendMailButton = (Button) findViewById(R.id.sendMailSendButton);
        sendMailButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MailDto mail = createMailDto(adId, nameInput.getText().toString(), emailInput.getText().toString(), messageInput.getText().toString());
                try {
                    ILoveGratisUtil.validateMailToSend(mail);
                    new SendMailTask().execute(mail);
                } catch (ValidationException ve) {
                    displayText(createValidationErrorString(ve.getValidationErrorIds()));
                }
            }
        });
    }

    // Creates a mail dto based on the input
    private MailDto createMailDto(Long adId, String name, String email, String message) {
        MailDto mail = new MailDto();
        mail.setAdId(adId);
        mail.setName(name);
        mail.setEmailAddress(email);
        mail.setMessage(message);
        return mail;
    }

    /**
     * Async task that sends an email to an advertiser for an ad
     * 
     * @author mats
     * 
     */
    protected class SendMailTask extends AsyncTask<MailDto, Void, Exception> {

        public SendMailTask() {
        }

        @Override
        protected void onPreExecute() {
            showDialog(SEND_MAIL_TASK);
        }

        @Override
        protected Exception doInBackground(MailDto... params) {
            try {
                getAdService().sendMail(params[0]);
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            removeDialog(SEND_MAIL_TASK);
            if (result != null) {
                displayText(result.getMessage());
            } else {
                // Notify the user that all went well and go back to five latest
                displayText(getString(R.string.mailsentsuccess));
                // End this activity
                finish();
            }
        }
    }

}
