package cristian.babarusi.simpletraveljournal;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Calendar;

import cristian.babarusi.simpletraveljournal.recyclerViewSources.DestinationsAdapter;
import cristian.babarusi.simpletraveljournal.utils.KeyboardUtils;
import cristian.babarusi.simpletraveljournal.utils.Screen;

public class ManageTripActivity extends AppCompatActivity {

    private static final int REQ_CODE = 200;

    private ConstraintLayout mConstraintLayoutMyView;
    private RadioButton mRadioButtonCityBreak;
    private RadioButton mRadioButtonSeaSide;
    private RadioButton mRadioButtonMountains;
    private SeekBar mSeekBarPriceEuro;
    private Button mButtonStartDate;
    private Button mButtonEndDate;
    private RatingBar mRatingBarRating;
    private Button mButtonSelectGalleryPhoto;
    private Button mButtonSelectTakePicture;
    private Button mButtonSave;
    private EditText mEditTextTripName;
    private EditText mEditTextDestination;

    private int mYearStart, mMonthStart, mDayStart;
    private int mYearEnd, mMonthEnd, mDayEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trip);

        //back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initView();
        Screen.hideNavigationBar(this);

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

        mSeekBarPriceEuro.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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
            }
        });

        mButtonSelectTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();

                //camera permission
                if (ContextCompat.checkSelfPermission(ManageTripActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ManageTripActivity.this, "permission: true", Toast.LENGTH_SHORT).show();

                } else {
                    //open specific fragment to ask for permission
                    ActivityCompat.requestPermissions(ManageTripActivity.this, new String[] {Manifest.permission.CAMERA}, REQ_CODE);
                    //my msg
                    Toast.makeText(ManageTripActivity.this, "permission: false", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loseEditTextsFocus();
            }
        });
    }

    private void initView() {
        mConstraintLayoutMyView = findViewById(R.id.my_view);
        mRadioButtonCityBreak = findViewById(R.id.radio_button_city_break);
        mRadioButtonSeaSide = findViewById(R.id.radio_button_sea_side);
        mRadioButtonMountains = findViewById(R.id.radio_button_mountains);
        mSeekBarPriceEuro = findViewById(R.id.seek_bar_price_euro);
        mButtonStartDate = findViewById(R.id.button_start_date);
        mButtonEndDate = findViewById(R.id.button_end_date);
        mRatingBarRating = findViewById(R.id.rating_bar_rating);
        mButtonSelectGalleryPhoto = findViewById(R.id.button_select_gallery_photo);
        mButtonSelectTakePicture = findViewById(R.id.button_select_take_picture);
        mButtonSave = findViewById(R.id.button_save);
        mEditTextTripName = findViewById(R.id.edit_text_trip_name);
        mEditTextDestination = findViewById(R.id.edit_text_destination);
    }

    private void loseEditTextsFocus() {
        KeyboardUtils.hideKeyboard(ManageTripActivity.this);
        mConstraintLayoutMyView.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Screen.hideNavigationBar(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

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
