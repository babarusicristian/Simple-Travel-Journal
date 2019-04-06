package cristian.babarusi.simpletraveljournal.recyclerViewSources;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.Query;
import cristian.babarusi.simpletraveljournal.R;

public class FirestoreTripAdapter extends FirestoreAdapter<TripHolder> {

    private Context mContext;
    private OnTripSelectedListener mListener;

    public FirestoreTripAdapter(Query query, OnTripSelectedListener listener, Context context) {
        super(query);
        mListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trip_list_item,
                viewGroup, false);
        return new TripHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripHolder tripsViewHolder, final int i) {


        tripsViewHolder.mTextViewTripName.setText(getSnapshot(i).getString("db_tripName"));
        tripsViewHolder.mTextViewDestination.setText(getSnapshot(i).getString("db_destination"));
        Double tempRating = getSnapshot(i).getDouble("db_rating");

        tripsViewHolder.mTextViewRating.setText(String.valueOf(tempRating));




        // Click listener
        tripsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onTripSelected(getSnapshot(i));
                }
            }
        });
        tripsViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mListener != null) {
                    mListener.onTripLongPressed(getSnapshot(i));
                }
                return true;
            }
        });


    }


}
