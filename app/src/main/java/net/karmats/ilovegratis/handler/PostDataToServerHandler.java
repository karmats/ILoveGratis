package net.karmats.ilovegratis.handler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import net.karmats.ilovegratis.constant.ApplicationConstants;
import net.karmats.ilovegratis.dto.AdUploadDto;
import net.karmats.ilovegratis.dto.MailDto;
import net.karmats.ilovegratis.exception.AdNotUploadedException;

/**
 * Class that posts an ad to be available at ilovegratis.se.
 * 
 * @author mats
 * 
 */
public class PostDataToServerHandler {

    private static final String ENCODING = "UTF-8";

    private static String CRLF = "\r\n";
    private static final String BOUNDARY = "---------------------------168072824752491622650073";
    private static final String CONTENT_DISPOSITION_FORM_DATA = "--" + BOUNDARY + CRLF + "Content-Disposition: form-data; name=";

    private static final String NAME_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"namn\"";
    private static final String EMAIL_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"emai1\"";
    private static final String PASSWORD_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"pass\"";
    private static final String PHONE_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"telefon\"";
    private static final String COUNTY_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"countyid\"";
    private static final String MUNICIPALITY_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"cityid\"";
    private static final String TITLE_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"rubrik\"";
    private static final String CATEGORY_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"Kategori\"";
    private static final String DESCRIPTION_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"besk\"";
    private static final String GUEST_ID_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"guest\"";
    private static final String CODE_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"dacode\"";
    private static final String IMG_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"ufile\"";
    private static final String SUBMIT_PARAMETER = CONTENT_DISPOSITION_FORM_DATA + "\"Submit\"";

    private static final String MAIL_NAME_PARAMETER = "&namn=";
    private static final String MAIL_EMAIL_PARAMETER = "&din_epost=";
    private static final String MAIL_AD_ID_PARAMETER = "epost=";
    private static final String MAIL_MESSAGE_PARAMETER = "&medd=";
    private static final String MAIL_SUBMIT_PARAMETER = "&submit=";
    private static final String MAIL_GUEST_ID_PARAMETER = "&guest=";
    private static final String MAIL_CODE_PARAMETER = "&dacode=";

    private static final String IMG_TYPE = "image/";

    /**
     * Uses Http POST to upload an {@link AdUploadDto} to ilovegratis.se
     * 
     * @param adToPost
     *            The ad to post to the server
     * @throws AdNotUploadedException
     *             If the ad couldn't be uploaded.
     */
    public void postAd(AdUploadDto adToPost) throws AdNotUploadedException {
        HttpURLConnection urlc = null;
        try {
            urlc = (HttpURLConnection) new URL(ApplicationConstants.AD_UPLOAD_URL).openConnection();
            try {
                urlc.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new AdNotUploadedException("Shouldn't happen: HttpURLConnection doesn't support POST??", e);
            }
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            urlc.setUseCaches(false);
            urlc.setAllowUserInteraction(false);
            urlc.setRequestProperty("Content-type", "multipart/form-data; boundary=" + BOUNDARY);
            urlc.setRequestProperty("Connection", "Keep-Alive");
            OutputStream out = urlc.getOutputStream();
            try {
                // Write the ad data to the stream
                DataOutputStream dos = new DataOutputStream(out);
                writeDataToStream(adToPost, dos);
                dos.close();
            } catch (IOException e) {
                throw new AdNotUploadedException("IOException while posting data", e);
            } finally {
                if (out != null)
                    out.close();
            }
            int responseCode = urlc.getResponseCode();
            if (responseCode != 200) {
                throw new AdNotUploadedException("The ad couldn't be uploaded, failed with response code " + responseCode, responseCode);
            }
        } catch (IOException e) {
            throw new AdNotUploadedException("Connection error (is server running at " + ApplicationConstants.AD_UPLOAD_URL + " ?): ", e);
        } finally {
            // Reset the image
            adToPost.setImg(null);
            if (urlc != null)
                urlc.disconnect();
        }
    }

    /**
     * Posts a {@link MailDto} to the server.
     * 
     * @param mailToPost
     *            The mail to send
     * @throws Exception
     *             If the mail couldn't be sent
     */
    public void postMail(MailDto mailToPost) throws Exception {
        DataOutputStream dos = null;
        HttpURLConnection urlc = null;
        try {
            URL url = new URL(ApplicationConstants.MAIL_SEND_URL);
            urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestMethod("POST");
            urlc.setDoOutput(true);
            urlc.setUseCaches(false);
            // Request properties
            urlc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlc.setRequestProperty("Connection", "keep-alive");
            // Send post output
            dos = new DataOutputStream(urlc.getOutputStream());
            // The string to write to the server
            String content = createPostMailContent(mailToPost);
            // Write the post as bytes
            dos.write(content.getBytes(ENCODING));
            int responseCode = urlc.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Failed to send mail reason: " + responseCode);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (dos != null) {
                dos.close();
            }
            if (urlc != null) {
                urlc.disconnect();
            }
        }
    }

    // Creates the data to write to the stream
    private String createPostMailContent(MailDto mailToPost) {
        StringBuilder sb = new StringBuilder();
        sb.append(MAIL_AD_ID_PARAMETER + URLEncoder.encode(String.valueOf(mailToPost.getAdId())));
        sb.append(MAIL_EMAIL_PARAMETER + URLEncoder.encode(mailToPost.getEmailAddress()));
        sb.append(MAIL_NAME_PARAMETER + URLEncoder.encode(mailToPost.getName()));
        sb.append(MAIL_MESSAGE_PARAMETER + URLEncoder.encode(mailToPost.getMessage()));
        // These two has to be the same
        sb.append(MAIL_GUEST_ID_PARAMETER + URLEncoder.encode("2222"));
        sb.append(MAIL_CODE_PARAMETER + URLEncoder.encode("2222"));
        sb.append(MAIL_SUBMIT_PARAMETER + URLEncoder.encode("Skicka Mail"));
        return sb.toString();
    }

    // Writes the byte data to a stream
    private void writeDataToStream(AdUploadDto adToPost, DataOutputStream dos) throws IOException {
        dos.write(getFormDataBytes(NAME_PARAMETER, adToPost.getName()));
        dos.write(getFormDataBytes(EMAIL_PARAMETER, adToPost.getEmail()));
        dos.write(getFormDataBytes(PASSWORD_PARAMETER, adToPost.getPassword()));
        dos.write(getFormDataBytes(PHONE_PARAMETER, adToPost.getPhoneNumber()));
        dos.write(getFormDataBytes(COUNTY_PARAMETER, adToPost.getCounty()));
        dos.write(getFormDataBytes(MUNICIPALITY_PARAMETER, adToPost.getMunicipality()));
        dos.write(getFormDataBytes(TITLE_PARAMETER, adToPost.getTitle()));
        dos.write(getFormDataBytes(CATEGORY_PARAMETER, adToPost.getCategory()));
        dos.write(getFormDataBytes(DESCRIPTION_PARAMETER, adToPost.getDescription()));
        dos.write(getFormDataBytes(GUEST_ID_PARAMETER, "222"));
        dos.write(getFormDataBytes(CODE_PARAMETER, "222"));

        File adImg = adToPost.getImg();
        if (adImg != null) {

            // Img file
            String fileImgType = IMG_TYPE + "jpg";
            String[] fileNameArray = adImg.getName().split("\\.");
            if (fileNameArray.length > 1) {
                // The last string in the fileNameArray is the file type
                fileImgType = IMG_TYPE + fileNameArray[fileNameArray.length - 1].toLowerCase();
            }
            dos.write((IMG_PARAMETER + "; filename=\"" + adImg.getName() + "\"" + CRLF + "Content-Type: " + fileImgType + CRLF + CRLF).getBytes(ENCODING));

            FileInputStream fis = new FileInputStream(adToPost.getImg());
            // create a buffer of maximum size
            int bytesAvailable = fis.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            // read file and write it into form...
            int bytesRead = fis.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
            }
            fis.close();

            // closing CRLF
            dos.write((CRLF).getBytes(ENCODING));
        } else {
            // ilovegratis.se needs this if no image is attached, don't ask me why.
            dos.write((IMG_PARAMETER + "; filename=\"\"" + CRLF + "Content-Type: application/octet-stream" + CRLF + CRLF).getBytes(ENCODING));
        }

        dos.write(getFormDataBytes(SUBMIT_PARAMETER, "Lägg in annons"));
        dos.write(("--" + BOUNDARY + "--").getBytes(ENCODING));
        dos.write(CRLF.getBytes(ENCODING));
    }

    private byte[] getFormDataBytes(String name, String value) throws UnsupportedEncodingException {
        String byteArrayString = name + CRLF + CRLF + (value == null ? "" : value) + CRLF;
        return byteArrayString.getBytes(ENCODING);
    }

}
