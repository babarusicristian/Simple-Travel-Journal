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

import java.util.ArrayList;
import java.util.List;

import cristian.babarusi.simpletraveljournal.ManageTripActivity;
import cristian.babarusi.simpletraveljournal.R;
import cristian.babarusi.simpletraveljournal.recyclerViewSources.Destinations;
import cristian.babarusi.simpletraveljournal.recyclerViewSources.DestinationsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravelListFragment extends Fragment {

    private RecyclerView mRecyclerViewTravel;
    private FloatingActionButton mFloatingActionButtonAdd;
    private FragmentActivity fragmentActivity;

    public TravelListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel_list, container, false);

        initView(view);

        mFloatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentManageTrip = new Intent(fragmentActivity, ManageTripActivity.class);
                startActivity(intentManageTrip);
            }
        });

        return view;
    }

    private void initView(View view) {
        mRecyclerViewTravel = view.findViewById(R.id.recycler_view_travel);
        fragmentActivity = getActivity();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(fragmentActivity);
        mRecyclerViewTravel.setLayoutManager(layoutManager);
        DestinationsAdapter destinationsAdapter = new DestinationsAdapter(fragmentActivity,
                getDestinationsSource());
        mRecyclerViewTravel.setAdapter(destinationsAdapter);
        mFloatingActionButtonAdd = view.findViewById(R.id.floating_action_button_add);
    }

    private List<Destinations> getDestinationsSource() {
        List<Destinations> destinationsList = new ArrayList<>();
        Destinations dest;

        //TODO to use FireBase

        dest = new Destinations();
        dest.setTripNameTitle("Holiday 2017");
        dest.setDestinationSubtitle("Islands");
        dest.setPicture(R.drawable.islands);
        destinationsList.add(dest);

        dest = new Destinations();
        dest.setTripNameTitle("Fall 2017");
        dest.setDestinationSubtitle("Rome");
        dest.setPicture(R.drawable.rome);
        destinationsList.add(dest);

        dest = new Destinations();
        dest.setTripNameTitle("Summer 2017");
        dest.setDestinationSubtitle("London");
        dest.setPicture(R.drawable.london);
        destinationsList.add(dest);

        dest = new Destinations();
        dest.setTripNameTitle("Winter 2017");
        dest.setDestinationSubtitle("Paris");
        dest.setPicture(R.drawable.paris);
        destinationsList.add(dest);

        dest = new Destinations();
        dest.setTripNameTitle("Spring 2018");
        dest.setDestinationSubtitle("San Francisco");
        dest.setPicture(R.drawable.san_francisco);
        destinationsList.add(dest);

        dest = new Destinations();
        dest.setTripNameTitle("Summer 2018");
        dest.setDestinationSubtitle("Greece");
        dest.setPicture(R.drawable.greece);
        destinationsList.add(dest);

        dest = new Destinations();
        dest.setTripNameTitle("Summer 2018");
        dest.setDestinationSubtitle("Cairo");
        dest.setPicture(R.drawable.cairo);
        destinationsList.add(dest);

        return destinationsList;
    }
}
