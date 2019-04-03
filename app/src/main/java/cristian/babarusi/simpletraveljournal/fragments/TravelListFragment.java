package cristian.babarusi.simpletraveljournal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import cristian.babarusi.simpletraveljournal.ManageTripActivity;
import cristian.babarusi.simpletraveljournal.R;
import cristian.babarusi.simpletraveljournal.recyclerViewSources.TripAdapter;
import cristian.babarusi.simpletraveljournal.recyclerViewSources.TripModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravelListFragment extends Fragment {

    private FloatingActionButton mFloatingActionButtonAdd;
    private FragmentActivity fragmentActivity;

    //for firestore (cloud database)
    private FirebaseFirestore db;
    private CollectionReference mUserReference;
    private TripAdapter mAdapter;

    //for firebase use
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String mUsernameMail;

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
    private static final String DB_FILE_REFERENCE = "db_fileReference";
    private static final String DB_REF_IDENTITY = "db_refIdentity";

    private static final String CITY_BREAK = "cityBreak";
    private static final String SEA_SIDE = "seaSide";
    private static final String MOUNTAINS = "mountains";

    private static final String CAMERA_REF_IDENTITY = "CAMERA";
    private static final String GALLERY_REF_IDENTITY = "GALLERY";
    private static final String NONE_REF_IDENTITY = "NONE";

    public TravelListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel_list, container, false);

        initView(view);
        retriveUserMail();
        //initialize firestore DB
        db = FirebaseFirestore.getInstance();
        mUserReference = db.collection(mUsernameMail);

        setUpRecyclerView(view);

        mFloatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentManageTrip = new Intent(getActivity(), ManageTripActivity.class);
                startActivity(intentManageTrip);
            }
        });

        return view;
    }

    private void setUpRecyclerView(View view) {
        Query query = mUserReference.orderBy(DB_RATING, Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<TripModel> options = new FirestoreRecyclerOptions.Builder<TripModel>()
                .setQuery(query, TripModel.class).build();

        mAdapter = new TripAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_travel);
        recyclerView.setHasFixedSize(true);
        //se poate si cu this(daca nu ai fragment)
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void initView(View view) {
        mFloatingActionButtonAdd = view.findViewById(R.id.floating_action_button_add);
    }


    private void retriveUserMail() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser != null) {
            mUsernameMail = mFirebaseUser.getEmail();
        }
    }
}
