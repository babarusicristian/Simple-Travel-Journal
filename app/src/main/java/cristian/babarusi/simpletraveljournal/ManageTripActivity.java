package cristian.babarusi.simpletraveljournal;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;
import cristian.babarusi.simpletraveljournal.recyclerViewSources.DestinationsAdapter;
import cristian.babarusi.simpletraveljournal.utils.KeyboardUtils;
import cristian.babarusi.simpletraveljournal.utils.Logging;

public class ManageTripActivity extends AppCompatActivity {

    public static final int GALLERY_CODE = 101;
    private static final int CAMERA_CODE = 202;
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

    //firebase storage
    private FirebaseStorage mStorage;

    //for firebase user
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsernameMail;

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

//                                //for use months with numbers
//                                //cause is from 0 - 11
//                                if ((monthOfYear + 1) < 10) {
//                                    textMonth = "0" + (monthOfYear + 1);
//                                } else {
//                                    textMonth = "" + (monthOfYear + 1);
//                                }

                                //for use words on months
                                switch (monthOfYear) {
                                    case 0:
                                        textMonth = "Jan";
                                        break;
                                    case 1:
                                        textMonth = "Feb";
                                        break;
                                    case 2:
                                        textMonth = "Mar";
                                        break;
                                    case 3:
                                        textMonth = "Apr";
                                        break;
                                    case 4:
                                        textMonth = "May";
                                        break;
                                    case 5:
                                        textMonth = "Jun";
                                        break;
                                    case 6:
                                        textMonth = "Jul";
                                        break;
                                    case 7:
                                        textMonth = "Aug";
                                        break;
                                    case 8:
                                        textMonth = "Sep";
                                        break;
                                    case 9:
                                        textMonth = "Oct";
                                        break;
                                    case 10:
                                        textMonth = "Nov";
                                        break;
                                    case 11:
                                        textMonth = "Dec";
                                        break;
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

//                                //for use months with numbers
//                                //cause is from 0 - 11
//                                if ((monthOfYear + 1) < 10) {
//                                    textMonth = "0" + (monthOfYear + 1);
//                                } else {
//                                    textMonth = "" + (monthOfYear + 1);
//                                }

                                //for use words on months
                                switch (monthOfYear) {
                                    case 0:
                                        textMonth = "Jan";
                                        break;
                                    case 1:
                                        textMonth = "Feb";
                                        break;
                                    case 2:
                                        textMonth = "Mar";
                                        break;
                                    case 3:
                                        textMonth = "Apr";
                                        break;
                                    case 4:
                                        textMonth = "May";
                                        break;
                                    case 5:
                                        textMonth = "Jun";
                                        break;
                                    case 6:
                                        textMonth = "Jul";
                                        break;
                                    case 7:
                                        textMonth = "Aug";
                                        break;
                                    case 8:
                                        textMonth = "Sep";
                                        break;
                                    case 9:
                                        textMonth = "Oct";
                                        break;
                                    case 10:
                                        textMonth = "Nov";
                                        break;
                                    case 11:
                                        textMonth = "Dec";
                                        break;
                                }
                                //do not format (it causes BUG)
                                mButtonEndDate.setText(textDay + "/" + textMonth + "/" + year);

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
                        Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intentTakePicture.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intentTakePicture, CAMERA_CODE);
                        }
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

                    Toast.makeText(ManageTripActivity.this, "Photo taken removed",
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
                blockAllActivityProgressBar();
                uploadGalleryImageToStorage();
            }
        });

        //TODO de sters metoda asta cu LONG click
        mButtonSave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

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

    private void dialogSetPrice() {

        final Dialog theDialog = new Dialog(ManageTripActivity.this);
        theDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        theDialog.setContentView(R.layout.dialog_set_price);
        theDialog.setCancelable(false);

        Button dialogButtonSet = theDialog.findViewById(R.id.button_dialog_set);
        Button dialogButtonCancel = theDialog.findViewById(R.id.button_dialog_cancel);
        EditText dialogEditText = theDialog.findViewById(R.id.edit_text_dialog_price);
        dialogEditText.setHint(getString(R.string.max_value) + " " + mSeekBarPrice.getMax());

        dialogButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mBitmapCamera = (Bitmap) extras.get("data");
            //mImageViewDisplayPicture.setImageBitmap(imageBitmap);

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
                            "Failed! to load image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
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
