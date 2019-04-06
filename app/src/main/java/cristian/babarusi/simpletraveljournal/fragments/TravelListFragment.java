package cristian.babarusi.simpletraveljournal.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import cristian.babarusi.simpletraveljournal.R;
import cristian.babarusi.simpletraveljournal.recyclerViewSources.FirestoreTripAdapter;
import cristian.babarusi.simpletraveljournal.recyclerViewSources.OnTripSelectedListener;
import cristian.babarusi.simpletraveljournal.recyclerViewSources.TripModel;
import cristian.babarusi.simpletraveljournal.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravelListFragment extends Fragment implements OnTripSelectedListener<DocumentSnapshot> {

    //for firestore (cloud database)
    private FirebaseFirestore db;
    private CollectionReference mUserReference;
    private FirestoreTripAdapter mAdapter;

    //for firebase use
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsernameMail;

    public TravelListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_main_navigation_drawer, container, false);

        retriveUserMail();
        //initialize firestore DB
        db = FirebaseFirestore.getInstance();
        mUserReference = db.collection(mUsernameMail);
        setUpRecyclerView(view);

        return view;
    }

    private void setUpRecyclerView(View view) {
        Query query = mUserReference.orderBy(Constants.DB_RATING, Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<TripModel> options = new FirestoreRecyclerOptions.Builder<TripModel>()
                .setQuery(query, TripModel.class).build();
        mAdapter = new FirestoreTripAdapter(query,this,getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_travel);
        //se poate si cu this(daca nu ai fragment)
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
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

    private void retriveUserMail() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser != null) {
            mUsernameMail = mFirebaseUser.getEmail();
        }
    }

    @Override
    public void onTripSelected(DocumentSnapshot trip) {
        Toast.makeText(getActivity(),"Click pressed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTripLongPressed(DocumentSnapshot trip) {
        Toast.makeText(getActivity(),"Long click pressed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onIconPressed(DocumentSnapshot trip, ImageButton imageButton) {

    }

    @Override
    public void onDeletePressed(DocumentSnapshot trip, ImageButton imageButton) {

    }

    @Override
    public void onDeleteLongPressed(DocumentSnapshot trip, ImageButton imageButton) {

    }
}
