package cristian.babarusi.simpletraveljournal.recyclerViewSources;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import cristian.babarusi.simpletraveljournal.R;

public class TripAdapter extends FirestoreRecyclerAdapter<TripModel, TripAdapter.TripHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public TripAdapter(@NonNull FirestoreRecyclerOptions<TripModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TripHolder holder, int position,
                                    @NonNull TripModel model) {

        //here we pass datas from the model
        holder.mTextViewTripName.setText(model.getTripName());
        holder.mTextViewDestination.setText(model.getDestination());
        holder.mTextViewRating.setText(String.valueOf(model.getRating()));
        holder.mUrl = model.getImageUrl();
        holder.mRef = model.getFileReference(); //for later use

        //img trip
//        Glide.with((Fragment)this).load(holder.mUrl).placeholder(R.drawable.ic_launcher_background)
//                .into(holder.mImageViewTripImage);
    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trip_list_item, viewGroup, false);
        return new TripHolder(view);
    }

    class TripHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTripName;
        private TextView mTextViewDestination;
        private TextView mTextViewRating;
        private ImageView mImageViewTripImage;
        private ImageView mImageViewFavIcon;

        private String mUrl;
        private String mRef;

        public TripHolder(@NonNull View itemView) {
            super(itemView);

            //the initializations
            mTextViewTripName = itemView.findViewById(R.id.text_view_item_trip_name);
            mTextViewDestination = itemView.findViewById(R.id.text_view_item_destination);
            mTextViewRating = itemView.findViewById(R.id.text_view_item_rating);
            mImageViewTripImage = itemView.findViewById(R.id.image_view_item_image);
            mImageViewFavIcon = itemView.findViewById(R.id.image_view_item_favicon);
        }
    }

}
