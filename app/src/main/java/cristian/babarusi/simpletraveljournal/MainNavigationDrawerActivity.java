package cristian.babarusi.simpletraveljournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cristian.babarusi.simpletraveljournal.fragments.TravelListFragment;
import cristian.babarusi.simpletraveljournal.utils.Logging;
import cristian.babarusi.simpletraveljournal.utils.Screen;

public class MainNavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    //for firebase use
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient; //for sign out
    private static final String ANONYMOUS = "anonymus :D";
    private String mUsername;
    private String mUserMail;
    private String mPhotoUrl;

    private Menu menu;

    private TextView mTextViewTitleUsername;
    private TextView mTextViewTitleUsernameMail;
    private ImageView mImageViewUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //menu item from navigation drawer
        menu = navigationView.getMenu();

        //default fragment load
        TravelListFragment travelListFragment = new TravelListFragment();
        addFragment(travelListFragment);

        initView();
        initFirebaseAndDisplayData();
        initGoogleClient();
    }

    private void initView() {
        //to recognize ids from header of navigation drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        mTextViewTitleUsername = header.findViewById(R.id.text_view_title_username);
        mTextViewTitleUsernameMail = header.findViewById(R.id.text_view_title_username_mail);
        mImageViewUserImage = header.findViewById(R.id.image_view_title_user_image);
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.container_frame_fragments, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //desabled meniu action bar
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar_main_navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            TravelListFragment travelListFragment = new TravelListFragment();
            addFragment(travelListFragment);
            Screen.hideNavigationBar(this);
        } else if (id == R.id.nav_favourite) {

        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_log_out) {
            logOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mUsername = ANONYMOUS;
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void initFirebaseAndDisplayData() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class)); //go to login activity
            finish();
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            mUserMail = mFirebaseUser.getEmail();
            mPhotoUrl = "" + mFirebaseUser.getPhotoUrl();

            //set username to the textview
            if (mUsername != null && !mUsername.isEmpty()) {
                mTextViewTitleUsername.setText("Welcome," + " " + mUsername);
                //and to logout menu item
                MenuItem logOutItem = menu.findItem(R.id.nav_log_out);
                logOutItem.setTitle("Logout, " + mUsername);
            }

            //set username EMAIL to the textview
            if (mUserMail != null && !mUserMail.isEmpty()) {
                mTextViewTitleUsernameMail.setText(mUserMail);
            }

            //display photo of the user to imageview
            if (mPhotoUrl != null && !mPhotoUrl.isEmpty()) {
                Glide.with(this).load(mPhotoUrl).apply(RequestOptions.circleCropTransform()).placeholder(R.mipmap.ic_launcher).into(mImageViewUserImage);
            }
            Logging.show(MainNavigationDrawerActivity.this , "url poza: " + mPhotoUrl);

        }
    }

    private void initGoogleClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                         this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(MainNavigationDrawerActivity.this, "Connection failed!", Toast.LENGTH_SHORT).show();
    }
}
