package net.karmats.ilovegratis.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.karmats.ilovegratis.ILoveGratisActivity;
import net.karmats.ilovegratis.ILoveGratisTabs;
import net.karmats.ilovegratis.R;
import net.karmats.ilovegratis.constant.Category;
import net.karmats.ilovegratis.constant.County;
import net.karmats.ilovegratis.dto.AdUploadDto;
import net.karmats.ilovegratis.exception.AdNotUploadedException;
import net.karmats.ilovegratis.exception.ValidationException;
import net.karmats.ilovegratis.util.ILoveGratisUtil;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterAdActivity extends ILoveGratisActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;
    private static final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 1;

    // The visible image maximum size
    private static final long IMAGE_MAX_SIZE = 200;
    // Setting file max size to 3mb
    private static final long FILE_MAX_SIZE = 3000000;

    private static final int COUNTY_MENU_GROUP = 0;
    private static final int MUNICPALITY_MENU_GROUP = 1;
    private static final int CATEGORY_MENU_GROUP = 2;
    private static final int PICTURE_CHOOSER_MENU_GROUP = R.id.registerAdPictureChooserMenuGroup;

    private String county;
    private String municipality;
    private String categoryId;
    private Uri imageUri;

    private Button categoryButton;
    private Button placeButton;
    private ImageView pictureView;
    private TextView pictureInfo;

    private AdUploadDto adToPost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerad);

        // Input texts
        final EditText nameText = (EditText) findViewById(R.id.nameInput);
        final EditText emailText = (EditText) findViewById(R.id.emailInput);
        final EditText phoneText = (EditText) findViewById(R.id.phoneInput);
        final Button continueButton = (Button) findViewById(R.id.continueButton);
        final Button pictureButton = (Button) findViewById(R.id.pictureButton);
        final EditText titleText = (EditText) findViewById(R.id.titleInput);
        final EditText descriptionText = (EditText) findViewById(R.id.descriptionInput);
        // Button interactions
        categoryButton = (Button) findViewById(R.id.categoryButton);
        placeButton = (Button) findViewById(R.id.placeButton);
        pictureView = (ImageView) findViewById(R.id.registerAdPictureView);
        pictureInfo = (TextView) findViewById(R.id.registerAdPictureText);

        // Dialog that confirms the upload
        final Dialog confirmDialog = createConfirmDialog();

        continueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    // Create the ad
                    adToPost = createAd(nameText.getText().toString(), emailText.getText().toString(), 
                                        phoneText.getText().toString(), titleText.getText().toString(),
                                        descriptionText.getText().toString());
                    ILoveGratisUtil.validateAdUpload(adToPost);
                    // Start the confirm ad dialog and put the AdToUpload as an extra
                    confirmDialog.show();
                } catch (ValidationException ve) {
                    displayText(createValidationErrorString(ve.getValidationErrorIds()));
                }
            }
        });

        registerForContextMenu(placeButton);
        placeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openContextMenu(placeButton);

            }
        });

        registerForContextMenu(categoryButton);
        categoryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openContextMenu(categoryButton);

            }
        });

        registerForContextMenu(pictureButton);
        pictureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openContextMenu(pictureButton);
            }
        });

    }

    // Creates a confirm dialog
    private Dialog createConfirmDialog() {
        final Dialog confirmDialog = new Dialog(this);
        confirmDialog.setContentView(R.layout.confirmad);

        // Set dialog title
        confirmDialog.setTitle(getString(R.string.confirmAdTitle));

        // Set so you see the link
        TextView confirmInfo = (TextView) confirmDialog.findViewById(R.id.confirmAdConfirmationInfo);
        confirmInfo.setMovementMethod(LinkMovementMethod.getInstance());

        final EditText passwordText = (EditText) confirmDialog.findViewById(R.id.passwordInput);
        final EditText confirmPasswordText = (EditText) confirmDialog.findViewById(R.id.confirmPasswordInput);
        final Button submitButton = (Button) confirmDialog.findViewById(R.id.submitAdButton);

        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Validate that there is a password, and that the passwords equal each other
                String password = passwordText.getText().toString();
                String confirmPassword = confirmPasswordText.getText().toString();
                if (ILoveGratisUtil.stringIsEmpty(password) || password.length() < 4) {
                    displayText(getString(R.string.passworderror));
                    return;
                } else if (!password.equals(confirmPassword)) {
                    displayText(getString(R.string.passwordnotequalerror));
                    return;
                }
                adToPost.setPassword(password);
                confirmDialog.hide();
                new RegisterAdTask().execute(adToPost);
            }
        });

        return confirmDialog;
    }

    /**
     * Creates a context menu for place and category
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.placeButton) {
            // Create place context menu
            menu.setHeaderTitle(getString(R.string.registerAdChooseCounty));
            for (County c : County.values()) {
                SubMenu sm = menu.addSubMenu(COUNTY_MENU_GROUP, v.getId(), 0, c.getCountyName());
                for (String m : c.getMunicipalities()) {
                    sm.add(MUNICPALITY_MENU_GROUP, v.getId(), 0, m);
                }
            }
        } else if (v.getId() == R.id.categoryButton) {
            // Create category context menu
            for (Category c : Category.values()) {
                menu.add(CATEGORY_MENU_GROUP, c.getCategoryValue(), 0, c.getCategoryName());
            }
        } else if (v.getId() == R.id.pictureButton) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.picturecontextmenu, menu);
        }
    }

    /**
     * Place and category context menu
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getGroupId()) {
        case COUNTY_MENU_GROUP:
            county = item.getTitle().toString();
            placeButton.setText(item.getTitle());
            break;
        case MUNICPALITY_MENU_GROUP:
            municipality = item.getTitle().toString();
            placeButton.setText(placeButton.getText() + ", " + item.getTitle());
            break;
        case CATEGORY_MENU_GROUP:
            categoryId = String.valueOf(item.getItemId());
            categoryButton.setText(item.getTitle());
            break;
        case PICTURE_CHOOSER_MENU_GROUP:
            if (item.getItemId() == R.id.registerAdTakePicture) {
                try {
                    // create parameters for Intent with filename
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "ilovegratis-image");
                    // imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    // create new Intent
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } catch (Exception e) {
                    displayText(getString(R.string.couldnotstartcamera));
                }
            } else if (item.getItemId() == R.id.registerAdUploadByFile) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                                       SELECT_IMAGE_ACTIVITY_REQUEST_CODE);
            }
            break;
        default:
            break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE || requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE) {
                    imageUri = data.getData();
                }
                // Convert the image uri to a file
                File imgFile = convertImageUriToFile();
                // Check that the size of the file is lesser than the maximum allowed size
                if (imgFile != null && imgFile.length() <= FILE_MAX_SIZE) {
                    pictureView.setImageBitmap(decodeFile(imgFile));
                    pictureInfo.setVisibility(View.GONE);
                } else {
                    displayText(getString(R.string.imagetoolarge, ILoveGratisUtil.convertBytesToMB(FILE_MAX_SIZE),
                                          ILoveGratisUtil.convertBytesToMB(imgFile.length())));
                }
            }
        }
    }

    // Scales an image so it fits the image view. Need this to prevent OutOfMemoryError
    private Bitmap decodeFile(File imageFile) {
        try {
            // Decode image size
            BitmapFactory.Options oBefore = new BitmapFactory.Options();
            oBefore.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(imageFile), null, oBefore);
            int scale = 1;
            if (oBefore.outHeight > IMAGE_MAX_SIZE || oBefore.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(oBefore.outHeight, oBefore.outWidth)) / Math.log(0.5)));
            }

            // Decode with inSampleSize
            BitmapFactory.Options oAfter = new BitmapFactory.Options();
            oAfter.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(imageFile), null, oAfter);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    // Creates an ad based on user input
    private AdUploadDto createAd(String name, String email, String phoneNo, String title, String description) {
        AdUploadDto result = new AdUploadDto();
        result.setName(name);
        result.setEmail(email);
        result.setPhoneNumber(phoneNo);
        result.setCounty(county);
        result.setMunicipality(municipality);
        result.setTitle(title);
        result.setCategory(categoryId);
        result.setDescription(description);
        result.setImg(convertImageUriToFile());
        return result;
    }

    // Converts the user selected img uri to a file
    private File convertImageUriToFile() {
        if (imageUri == null || imageUri.toString().length() <= 0) {
            return null;
        }
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION };
            cursor = RegisterAdActivity.this.managedQuery(imageUri, proj, // Which columns to return
                                                          null, // WHERE clause; which rows to return (all rows)
                                                          null, // WHERE clause selection arguments (none)
                                                          null); // Order-by clause (ascending by name)
            int fileColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                return new File(cursor.getString(fileColumnIndex));
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Async task that posts an ad to ilovegratis.se
     * 
     * @author mats
     * 
     */
    protected class RegisterAdTask extends AsyncTask<AdUploadDto, Void, AdNotUploadedException> {

        public RegisterAdTask() {
        }

        @Override
        protected void onPreExecute() {
            showDialog(REGISTER_AD_TASK);
        }

        @Override
        protected AdNotUploadedException doInBackground(AdUploadDto... params) {
            try {
                getAdService().postAd(params[0]);
            } catch (AdNotUploadedException anue) {
                return anue;
            }
            return null;
        }

        @Override
        protected void onPostExecute(AdNotUploadedException result) {
            removeDialog(REGISTER_AD_TASK);
            if (result != null) {
                if (result.getErrorCode() > 0) {
                    displayText(getString(R.string.adcouldnotbepostedwitherrorcode, result.getErrorCode()));
                } else {
                    displayText(getString(R.string.adcouldnotbeposted, result.getCause().getClass().getSimpleName()));
                }
            } else {
                // Notify the user that all went well and go back to five latest
                displayText(getString(R.string.aduploadsuccess));
                // End the current tab activity and start a new one
                startActivity(new Intent(RegisterAdActivity.this, ILoveGratisTabs.class));
                finish();
            }
        }
    }

}
