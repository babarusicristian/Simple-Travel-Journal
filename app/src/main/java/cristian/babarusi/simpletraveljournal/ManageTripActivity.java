package cristian.babarusi.simpletraveljournal;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import cristian.babarusi.simpletraveljournal.recyclerViewSources.DestinationsAdapter;
import cristian.babarusi.simpletraveljournal.utils.KeyboardUtils;
import cristian.babarusi.simpletraveljournal.utils.Logging;
import cristian.babarusi.simpletraveljournal.utils.Snack;

public class ManageTripActivity extends AppCompatActivity {

    public static final int GALLERY_CODE = 101;
    private static final int CAMERA_CODE = 202;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    byte[] dataByteGallery;
    byte[] dataByteCamera;
    private Bitmap mBitmapGallery;
    private Bitmap mBitmapCamera;
    private ConstraintLayout mConstraintLayoutMyView; //help on lose focus
    private RadioButton mRadioButtonCityBreak;
    private RadioButton mRadioButtonSeaSide;
    private RadioButton mRadioButtonMountains;
    private SeekBar mSeekBarPrice;
    private Button mButtonStartDate;
    private Button mButtonEndDate;
    private RatingBar mRatingBarRating;
    private Button mButtonSelectGalleryPhoto;
    private Button mButtonSelectTakePicture;
    private Button mButtonSave;
    private EditText mEditTextTripName;
    private EditText mEditTextDestination;
    private ProgressBar mProgressBar;
    private TextView mTextViewPrice;
    private TextView mTextViewSetPrice;

    private TextView mTextViewStatusGallery;
    private TextView mTextViewStatusCamera;

    //for custom the date
    private int mYearStart, mMonthStart, mDayStart;
    private int mYearEnd, mMonthEnd, mDayEnd;

    private static final String MONTH_JAN = "Jan";
    private static final String MONTH_FEB = "Feb";
    private static final String MONTH_MAR = "Mar";
    private static final String MONTH_APR = "Apr";
    private static final String MONTH_MAY = "May";
    private static final String MONTH_JUN = "Jun";
    private static final String MONTH_JUL = "Jul";
    private static final String MONTH_AUG = "Aug";
    private static final String MONTH_SEP = "Sep";
    private static final String MONTH_OCT = "Oct";
    private static final String MONTH_NOV = "Nov";
    private static final String MONTH_DEC = "Dec";

    //firebase storage
    private FirebaseStorage mStorage;

    //for firebase user
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsernameMail;

    //for firestore (cloud database)
    FirebaseFirestore db;

    //for firebase use
    private static final String ANONYMOUS = "anonymus :D";

    //for store datas
    private String mTripName;
    private String mDestination;
    private double mRating;

    //for use on DB read and write
    private static final String DB_TRIP_NAME = "db_tripName";
    private static final String DB_DESTINATION = "db_destination";
    private static final String DB_TRIP_TYPE = "db_tripType";
    private static final String DB_PRICE_EURO = "db_priceEuro";
    private static final String DB_START_DATE = "db_startDate";
    private static final String DB_END_DATE = "db_endDate";
    private static final String DB_RATING = "db_rating";
    private static final String DB_URL_IMAGE = "db_urlImage";
    private static final String DB_FAVORITE = "db_favorite";
    private static final String DB_START_DATE_MILISEC = "db_startDateMilisec";
    private static final String DB_FILE_REFERECNE = "db_fileReference";
    private static final String DB_REF_IDENTITY = "db_refIdentity";

    private static final String CITY_BREAK = "cityBreak";
    private static final String SEA_SIDE = "seaSide";
    private static final String MOUNTAINS = "mountains";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trip);

        //back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        retriveFirebaseUserMail();
        initView();

        //initialize firebase storage
        mStorage = FirebaseStorage.getInstance();


        //receiving data from recycler view (trip list)
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mEditTextTripName.setText(bundle.getString(DestinationsAdapter.KEY_TITLE));
            mEditTextDestination.setText(bundle.getString(DestinationsAdapter.KEY_SUBTITLE));
        }

        mRadioButtonCityBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();
            }
        });

        mRadioButtonSeaSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();
            }
        });

        mRadioButtonMountains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();
            }
        });

        mTextViewSetPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();
                dialogSetPrice();
            }
        });

        mSeekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTextViewPrice.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                loseEditTextsFocus();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mButtonStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();

                // to select current date in dialog picker
                final Calendar c = Calendar.getInstance();
                mYearStart = c.get(Calendar.YEAR);
                mMonthStart = c.get(Calendar.MONTH);
                mDayStart = c.get(Calendar.DAY_OF_MONTH);

                //to mentain current selection
                mentainDateStartSelection();

                DatePickerDialog datePickerDialog = new DatePickerDialog(ManageTripActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String textMonth = "";
                                String textDay;

                                if (dayOfMonth < 10) {
                                    textDay = "0" + dayOfMonth;
                                } else {
                                    textDay = "" + dayOfMonth;
                                }

                                //for use words on months
                                textMonth = getWordsOnMonth(monthOfYear, textMonth);

                                //checking dates: start
                                mDayStart = dayOfMonth;
                                mMonthStart = monthOfYear;
                                mYearStart = year;

                                //date verification (START)
                                if (mDayEnd != 0 && mMonthEnd != 0  && mYearEnd != 0) {
                                    if (mYearStart > mYearEnd) {
                                        displayStartDateError();
                                        return;
                                    }
                                    if (mMonthStart > mMonthEnd && mYearStart == mYearEnd) {
                                        displayStartDateError();
                                        return;
                                    }
                                    if (mDayStart > mDayEnd && mMonthStart == mMonthEnd && mYearStart == mYearEnd) {
                                        displayStartDateError();
                                        return;
                                    }
                                }

                                //do not format (it causes BUG)
                                mButtonStartDate.setText(textDay + "/" + textMonth + "/" + year);
                            }
                        }, mYearStart, mMonthStart, mDayStart);
                datePickerDialog.show();
            }
        });

        mButtonEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();

                // to select current date in dialog picker
                final Calendar c = Calendar.getInstance();
                mYearEnd = c.get(Calendar.YEAR);
                mMonthEnd = c.get(Calendar.MONTH);
                mDayEnd = c.get(Calendar.DAY_OF_MONTH);

                //to mentain current selection
                mentainDateEndSelection();

                DatePickerDialog datePickerDialog = new DatePickerDialog(ManageTripActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String textMonth = "";
                                String textDay;

                                if (dayOfMonth < 10) {
                                    textDay = "0" + dayOfMonth;
                                } else {
                                    textDay = "" + dayOfMonth;
                                }

                                //for use words on months
                                textMonth = getWordsOnMonth(monthOfYear, textMonth);

                                //checking dates: end
                                mDayEnd = dayOfMonth;
                                mMonthEnd = monthOfYear;
                                mYearEnd = year;

                                //date verification (END)
                                if (mDayStart != 0 && mMonthStart != 0  && mYearStart != 0) {
                                    if (mYearStart > mYearEnd) {
                                        displayEndDateError();
                                        return;
                                    }
                                    if (mMonthStart > mMonthEnd && mYearStart == mYearEnd) {
                                        displayEndDateError();
                                        return;
                                    }
                                    if (mDayStart > mDayEnd && mMonthStart == mMonthEnd && mYearStart == mYearEnd) {
                                        displayEndDateError();
                                        return;
                                    }
                                }

                                //do not format (it causes BUG)
                                mButtonEndDate.setText(textDay + "/" + textMonth + "/" + year);

                                //date end to mDate2 - for compare
                                SimpleDateFormat sdf = new SimpleDateFormat("d/m/yyyy");
                            }
                        }, mYearEnd, mMonthEnd, mDayEnd);
                datePickerDialog.show();
            }
        });

        mRatingBarRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                loseEditTextsFocus();
            }
        });

        mButtonSelectGalleryPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();

                if (ContextCompat.checkSelfPermission(ManageTripActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    if (mButtonSelectGalleryPhoto.getText().toString().equals(getString(R.string.select))) {

                        //open gallery
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, GALLERY_CODE);
                        return;
                    }

                    if (mButtonSelectGalleryPhoto.getText().toString().equals(getString(R.string.remove))) {

                        mBitmapGallery = null;

                        //restore gallery opt
                        mTextViewStatusGallery.setText(getString(R.string.no_image_file_selected));
                        mTextViewStatusGallery.setTextColor(Color.parseColor("#990000")); //dark red
                        mButtonSelectGalleryPhoto.setText(getString(R.string.select));
                        mButtonSelectTakePicture.setEnabled(true);
                        mTextViewStatusCamera.setVisibility(View.VISIBLE);

                        Toast.makeText(ManageTripActivity.this,
                                getString(R.string.image_file_removed), Toast.LENGTH_SHORT).show();
                        mButtonSelectGalleryPhoto.setText(R.string.select);
                    }

                } else {
                    //open specific fragment to ask for permission
                    ActivityCompat.requestPermissions(ManageTripActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_CODE);
                }
            }
        });

        mButtonSelectTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();

                if (mButtonSelectTakePicture.getText().toString().equals(getString(R.string.select))) {
                    //camera permission
                    if (ContextCompat.checkSelfPermission(ManageTripActivity.this,
                            Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        //open camera
                        dispatchTakePictureIntent();

                    } else {
                        //open specific fragment to ask for permission
                        ActivityCompat.requestPermissions(ManageTripActivity.this,
                                new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
                    }
                    return;
                }

                if (mButtonSelectTakePicture.getText().toString().equals(getString(R.string.remove))) {
                    mBitmapCamera = null;

                    //restore camera opt
                    mTextViewStatusCamera.setText(getString(R.string.no_photo_taken));
                    mTextViewStatusCamera.setTextColor(Color.parseColor("#990000")); //dark red
                    mButtonSelectTakePicture.setText(getString(R.string.select));
                    mButtonSelectGalleryPhoto.setEnabled(true);
                    mTextViewStatusGallery.setVisibility(View.VISIBLE);

                    Toast.makeText(ManageTripActivity.this, getString(R.string.photo_taken_removed),
                            Toast.LENGTH_SHORT).show();
                    mButtonSelectTakePicture.setText(R.string.select);
                }

            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();
                //TODO save button click

                // verifyAllBeforeSave
                String tripName = mEditTextTripName.getText().toString().trim();
                String destination = mEditTextDestination.getText().toString().trim();
                String price = mTextViewPrice.getText().toString().trim();
                String startDate = mButtonStartDate.getText().toString().toUpperCase();
                String endDate = mButtonEndDate.getText().toString().toUpperCase();
                double rating = mRatingBarRating.getRating();

                //for trip name
                if (tripName.isEmpty()) {
                    mEditTextTripName.requestFocus();
                    mEditTextTripName.setError("Trip name is missing");
                    return;
                }
                //for destination
                if (destination.isEmpty()) {
                    mEditTextDestination.requestFocus();
                    mEditTextDestination.setError("Destination is missing");
                    return;
                }
                //for radio buttons trip type
                if (!mRadioButtonCityBreak.isChecked()
                        && !mRadioButtonSeaSide.isChecked()
                        && !mRadioButtonMountains.isChecked()) {
                    Snack.bar(v, "Trip type is missing");
                    return;
                }
                //for price
                if (price.equals("0")) {
                    Snack.bar(v, "Price is missing");
                    return;
                }
                //for start and end date
                if (startDate.equals("DD/MM/YYYY")) {
                    Snack.bar(v, "Start date is missing");
                    return;
                }
                if (endDate.equals("DD/MM/YYYY")) {
                    Snack.bar(v, "End date is missing");
                    return;
                }
                //for rating
                if(rating == 0) {
                    Snack.bar(v, "Rating is missing");
                }


                //PORNESTE-LE
                //blockAllActivityProgressBar();
//
//                uploadGalleryImageToStorage();
//                uploadCameraImageToStorage();

            }
        });

        //TODO de sters metoda asta cu LONG click
        mButtonSave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //TODO delete from storage
                //storage reference for be DELETED
                String refFromDB = "ebf46ec0-ce64-48a6-adce-1220b37aacc4.jpeg"; //ce este salvat in baza de date
                String referencePath = mUsernameMail + "/" + refFromDB; //reformam path care a fost salvat la upload
                StorageReference storageReference = mStorage.getReference(referencePath);

                // Delete the file
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        Toast.makeText(ManageTripActivity.this, "File deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                        Logging.show(ManageTripActivity.this, "Delete error occured:" + exception.toString());
                    }
                });

                return true;
            }
        });
    }

    private void mentainDateStartSelection() {
        if (!mButtonStartDate.getText().toString().equals("DD/MM/YYYY")){

            String textButton = mButtonStartDate.getText().toString().trim();
            String[] arrOfStr = textButton.split("/");

            mYearStart = Integer.parseInt(arrOfStr[2]);
            String tempVal = arrOfStr[1];
            switch (tempVal) {
                case MONTH_JAN :
                    mMonthStart = 0;
                    break;
                case MONTH_FEB :
                    mMonthStart = 1;
                    break;
                case MONTH_MAR :
                    mMonthStart = 2;
                    break;
                case MONTH_APR :
                    mMonthStart = 3;
                    break;
                case MONTH_MAY :
                    mMonthStart = 4;
                    break;
                case MONTH_JUN :
                    mMonthStart = 5;
                    break;
                case MONTH_JUL :
                    mMonthStart = 6;
                    break;
                case MONTH_AUG :
                    mMonthStart = 7;
                    break;
                case MONTH_SEP :
                    mMonthStart = 8;
                    break;
                case MONTH_OCT :
                    mMonthStart = 9;
                    break;
                case MONTH_NOV :
                    mMonthStart = 10;
                    break;
                case MONTH_DEC :
                    mMonthStart = 11;
                    break;
            }

            String tempDay = arrOfStr[0];
            if (tempDay.startsWith("0")) {
                mDayStart = Integer.parseInt(tempDay.substring(1));
            } else {
                mDayStart = Integer.parseInt(arrOfStr[0]);
            }
        }
    }

    private void mentainDateEndSelection() {
        if (!mButtonEndDate.getText().toString().equals("DD/MM/YYYY")){

            String textButton = mButtonEndDate.getText().toString().trim();
            String[] arrOfStr = textButton.split("/");

            mYearEnd = Integer.parseInt(arrOfStr[2]);
            String tempVal = arrOfStr[1];
            switch (tempVal) {
                case MONTH_JAN :
                    mMonthEnd = 0;
                    break;
                case MONTH_FEB :
                    mMonthEnd = 1;
                    break;
                case MONTH_MAR :
                    mMonthEnd = 2;
                    break;
                case MONTH_APR :
                    mMonthEnd = 3;
                    break;
                case MONTH_MAY :
                    mMonthEnd = 4;
                    break;
                case MONTH_JUN :
                    mMonthEnd = 5;
                    break;
                case MONTH_JUL :
                    mMonthEnd = 6;
                    break;
                case MONTH_AUG :
                    mMonthEnd = 7;
                    break;
                case MONTH_SEP :
                    mMonthEnd = 8;
                    break;
                case MONTH_OCT :
                    mMonthEnd = 9;
                    break;
                case MONTH_NOV :
                    mMonthEnd = 10;
                    break;
                case MONTH_DEC :
                    mMonthEnd = 11;
                    break;
            }

            String tempDay = arrOfStr[0];
            if (tempDay.startsWith("0")) {
                mDayEnd = Integer.parseInt(tempDay.substring(1));
            } else {
                mDayEnd = Integer.parseInt(arrOfStr[0]);
            }
        }
    }

    private void initView() {
        mConstraintLayoutMyView = findViewById(R.id.my_view);
        mRadioButtonCityBreak = findViewById(R.id.radio_button_city_break);
        mRadioButtonSeaSide = findViewById(R.id.radio_button_sea_side);
        mRadioButtonMountains = findViewById(R.id.radio_button_mountains);
        mSeekBarPrice = findViewById(R.id.seek_bar_price);
        mButtonStartDate = findViewById(R.id.button_start_date);
        mButtonEndDate = findViewById(R.id.button_end_date);
        mRatingBarRating = findViewById(R.id.rating_bar_rating);
        mButtonSelectGalleryPhoto = findViewById(R.id.button_select_gallery_photo);
        mButtonSelectTakePicture = findViewById(R.id.button_select_take_picture);
        mButtonSave = findViewById(R.id.button_save);
        mEditTextTripName = findViewById(R.id.edit_text_trip_name);
        mEditTextDestination = findViewById(R.id.edit_text_destination);
        mProgressBar = findViewById(R.id.progressBar);
        mTextViewPrice = findViewById(R.id.text_view_price);
        mTextViewPrice.setText(String.valueOf(mSeekBarPrice.getProgress()));
        mTextViewSetPrice = findViewById(R.id.text_view_set_price);
        mTextViewStatusGallery = findViewById(R.id.text_view_status_gallery);
        mTextViewStatusCamera = findViewById(R.id.text_view_status_camera);
    }

    private void saveToFirestore() {

        //my DB object creation
        Map<String, Object> userDatas = new HashMap<>();
        userDatas.put(DB_TRIP_NAME, mEditTextTripName.getText().toString().trim()); //String
        userDatas.put(DB_DESTINATION, mEditTextDestination.getText().toString().trim()); //String
        if (mRadioButtonCityBreak.isChecked()) {
            userDatas.put(DB_TRIP_TYPE, CITY_BREAK); //String
        }
        else if (mRadioButtonSeaSide.isChecked()) {
            userDatas.put(DB_TRIP_TYPE, SEA_SIDE); //String
        }
        else if (mRadioButtonMountains.isChecked()) {
            userDatas.put(DB_TRIP_TYPE, MOUNTAINS); //String
        }
        userDatas.put(DB_PRICE_EURO, mTextViewPrice.getText().toString().trim()); //String
        userDatas.put(DB_START_DATE, mButtonStartDate.getText().toString().trim()); //String
        userDatas.put(DB_END_DATE, mButtonEndDate.getText().toString().trim()); //String
        //TODO de continuat objectul



        //firestore saving...
        db.collection(mUsernameMail)
                .add(userDatas)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ManageTripActivity.this, "Datas added succesfully", Toast.LENGTH_SHORT).show();
                        //here to do the final mark...activate FINISH on timer //or maybe NOT
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ManageTripActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("TAG:", "Error occurred while creating the File: " + ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        //int targetW = imageView.getWidth();
        //int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        //int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        //bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        mBitmapCamera = BitmapFactory.decodeFile(mCurrentPhotoPath);
        //to set to a imageview
        //imageView.setImageBitmap(bitmap);
    }

    private void displayStartDateError() {
        Toast.makeText(ManageTripActivity.this, getString(R.string.wrong_start_date),
                Toast.LENGTH_SHORT).show();
        mButtonStartDate.performClick();
    }

    private void displayEndDateError() {
        Toast.makeText(ManageTripActivity.this, getString(R.string.wrong_end_date),
                Toast.LENGTH_SHORT).show();
        mButtonEndDate.performClick();
    }

    private String getWordsOnMonth(int monthOfYear, String textMonth) {
        switch (monthOfYear) {
            case 0:
                textMonth = MONTH_JAN;
                break;
            case 1:
                textMonth = MONTH_FEB;
                break;
            case 2:
                textMonth = MONTH_MAR;
                break;
            case 3:
                textMonth = MONTH_APR;
                break;
            case 4:
                textMonth = MONTH_MAY;
                break;
            case 5:
                textMonth = MONTH_JUN;
                break;
            case 6:
                textMonth = MONTH_JUL;
                break;
            case 7:
                textMonth = MONTH_AUG;
                break;
            case 8:
                textMonth = MONTH_SEP;
                break;
            case 9:
                textMonth = MONTH_OCT;
                break;
            case 10:
                textMonth = MONTH_NOV;
                break;
            case 11:
                textMonth = MONTH_DEC;
                break;
        }
        return textMonth;
    }

    private void dialogSetPrice() {

        final Dialog theDialog = new Dialog(ManageTripActivity.this);
        theDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        theDialog.setContentView(R.layout.dialog_set_price);
        theDialog.setCancelable(false);

        TextView dialogButtonSet = theDialog.findViewById(R.id.button_dialog_set);
        TextView dialogButtonCancel = theDialog.findViewById(R.id.button_dialog_cancel);
        final EditText dialogEditText = theDialog.findViewById(R.id.edit_text_dialog_price);
        dialogEditText.setHint(getString(R.string.max_value) + " " + mSeekBarPrice.getMax());

        dialogButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxVal = mSeekBarPrice.getMax();

                //field verification
                String text = dialogEditText.getText().toString();

                if (text.isEmpty()) {
                    dialogEditText.setError(getString(R.string.value_is_missing));
                } else if (text.startsWith("0")){
                    dialogEditText.setError(getString(R.string.invalid_price));
                } else if (!text.startsWith("0") && Integer.valueOf(text) > maxVal) {
                    dialogEditText.setError(getString(R.string.out_of_range) + " (1 - " + maxVal + ")");
                } else {
                    mTextViewPrice.setText(text);
                    mSeekBarPrice.setProgress(Integer.valueOf(text));
                    theDialog.dismiss();
                }
            }
        });

        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theDialog.dismiss();
            }
        });

        theDialog.show();


    }

    private void loseEditTextsFocus() {
        KeyboardUtils.hideKeyboard(ManageTripActivity.this);
        mConstraintLayoutMyView.requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        //for CAMERA
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            galleryAddPic();
            setPic();

            //for remove photo from selection
            if (mBitmapCamera != null) {
                //set camera opt
                mTextViewStatusCamera.setText(getString(R.string.photo_taken));
                mTextViewStatusCamera.setTextColor(Color.parseColor("#006300")); //dark green
                mButtonSelectTakePicture.setText(getString(R.string.remove));
                mButtonSelectGalleryPhoto.setEnabled(false);
                mTextViewStatusGallery.setVisibility(View.INVISIBLE);
            }
            return;
        }

        //for GALLERY
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    //image selected is added to mBitmapGallery
                    mBitmapGallery = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                            contentURI);

                    //for remove image from selection
                    if (mBitmapGallery != null) {
                        //set gallery opt
                        mTextViewStatusGallery.setText(getString(R.string.image_file_selected));
                        mTextViewStatusGallery.setTextColor(Color.parseColor("#006300")); //dark
                        // green
                        mButtonSelectGalleryPhoto.setText(getString(R.string.remove));
                        mButtonSelectTakePicture.setEnabled(false);
                        mTextViewStatusCamera.setVisibility(View.INVISIBLE);
                    }
                    //mImageViewDisplayPicture.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ManageTripActivity.this,
                            getString(R.string.failed_to_load_image) + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadCameraImageToStorage() {

        if (mBitmapCamera != null) {

            //scaling the gallery image
            customScaleImageCamera();

            //convert my image to jpeg
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmapCamera.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dataByteCamera = baos.toByteArray();

            //for upload file (storage reference)
            String path = mUsernameMail + "/" + UUID.randomUUID() + ".jpeg";
            final StorageReference storageReference = mStorage.getReference(path);

            //uploading
            //dataByte = my image
            UploadTask uploadTask = storageReference.putBytes(dataByteCamera);
            uploadTask.addOnSuccessListener(ManageTripActivity.this,
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //to get download url
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri url = urlTask.getResult();
                            Logging.show(ManageTripActivity.this, "the url is: " + url);

                            String ref = storageReference.getName();
                            Logging.show(ManageTripActivity.this, "the ref is: " + ref);

                            //clear image from memory (prevent OutOfMemoryError crash BUG)
                            mBitmapCamera = null;
                        }
                    });
        }

    }

    private void uploadGalleryImageToStorage() {

        if (mBitmapGallery != null) {

            //scaling the gallery image
            customScaleImageGallery();

            //convert my image to jpeg
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmapGallery.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            dataByteGallery = baos.toByteArray();

            //for upload file (storage reference)
            String path = mUsernameMail + "/" + UUID.randomUUID() + ".jpeg";
            final StorageReference storageReference = mStorage.getReference(path);

            //uploading
            //dataByte = my image
            UploadTask uploadTask = storageReference.putBytes(dataByteGallery);
            uploadTask.addOnSuccessListener(ManageTripActivity.this,
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //to get download url
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri url = urlTask.getResult();
                    Logging.show(ManageTripActivity.this, "the url is: " + url);

                    String ref = storageReference.getName();
                    Logging.show(ManageTripActivity.this, "the ref is: " + ref);

                    //clear image from memory (prevent OutOfMemoryError crash BUG)
                    mBitmapGallery = null;
                }
            });
        }
    }

    private void customScaleImageGallery() {

        final double OPTIMUM_MAX_SIZE = 900.0;

        if (mBitmapGallery != null) {
            int myDstWidth = mBitmapGallery.getWidth();
            int myDstHeight = mBitmapGallery.getHeight();
            double temp;

            //verification
            //Log.e("TAG:" , "INITIAL img width:" + bitmap.getWidth());
            //Log.e("TAG:" , "INITIAL img height:" + bitmap.getHeight());

            int width = mBitmapGallery.getWidth();
            int height = mBitmapGallery.getHeight();

            if (height > width && height > OPTIMUM_MAX_SIZE) {
                    myDstHeight = (int) OPTIMUM_MAX_SIZE;

                    temp = height / OPTIMUM_MAX_SIZE;
                    double calc = width / temp;
                    width = (int) calc;
                    myDstWidth = width;

            } else if (width > height && width > OPTIMUM_MAX_SIZE) {

                myDstWidth = (int) OPTIMUM_MAX_SIZE;

                temp = width / OPTIMUM_MAX_SIZE;
                double calc = height / temp;
                height = (int) calc;
                myDstHeight = height;
            }

            mBitmapGallery = Bitmap.createScaledBitmap(mBitmapGallery, myDstWidth, myDstHeight, true);

            //verification
            //Log.e("TAG:" , "AFTER scale - img width:" + bitmap.getWidth());
            //Log.e("TAG:" , "AFTER scale - img height:" + bitmap.getHeight());
        }
    }

    private void customScaleImageCamera() {

        final double OPTIMUM_MAX_SIZE = 900.0;

        if (mBitmapCamera != null) {
            int myDstWidth = mBitmapCamera.getWidth();
            int myDstHeight = mBitmapCamera.getHeight();
            double temp;

            //verification
            //Log.e("TAG:" , "INITIAL img width:" + bitmap.getWidth());
            //Log.e("TAG:" , "INITIAL img height:" + bitmap.getHeight());

            int width = mBitmapCamera.getWidth();
            int height = mBitmapCamera.getHeight();

            if (height > width && height > OPTIMUM_MAX_SIZE) {
                myDstHeight = (int) OPTIMUM_MAX_SIZE;

                temp = height / OPTIMUM_MAX_SIZE;
                double calc = width / temp;
                width = (int) calc;
                myDstWidth = width;

            } else if (width > height && width > OPTIMUM_MAX_SIZE) {

                myDstWidth = (int) OPTIMUM_MAX_SIZE;

                temp = width / OPTIMUM_MAX_SIZE;
                double calc = height / temp;
                height = (int) calc;
                myDstHeight = height;
            }

            mBitmapCamera = Bitmap.createScaledBitmap(mBitmapCamera, myDstWidth, myDstHeight, true);

            //verification
            //Log.e("TAG:" , "AFTER scale - img width:" + bitmap.getWidth());
            //Log.e("TAG:" , "AFTER scale - img height:" + bitmap.getHeight());
        }
    }

    private void blockAllActivityProgressBar() {

        mProgressBar.setVisibility(View.VISIBLE);

        mEditTextTripName.setEnabled(false);
        mEditTextDestination.setEnabled(false);
        mRadioButtonCityBreak.setEnabled(false);
        mRadioButtonSeaSide.setEnabled(false);
        mRadioButtonMountains.setEnabled(false);
        mSeekBarPrice.setEnabled(false);
        mButtonStartDate.setEnabled(false);
        mButtonEndDate.setEnabled(false);
        mTextViewSetPrice.setEnabled(false);
        mRatingBarRating.setEnabled(false);
        mButtonSelectGalleryPhoto.setEnabled(false);
        mButtonSelectTakePicture.setEnabled(false);
        mButtonSave.setEnabled(false);
    }

    private void retriveFirebaseUserMail() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class)); //go to login activity
            finish();
        } else {
            mUsernameMail = mFirebaseUser.getEmail();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
