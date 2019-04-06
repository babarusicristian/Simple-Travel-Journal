package cristian.babarusi.simpletraveljournal.recyclerViewSources;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cristian.babarusi.simpletraveljournal.R;

class TripHolder extends RecyclerView.ViewHolder {

    public TextView mTextViewTripName;
    public TextView mTextViewDestination;
    public TextView mTextViewRating;
    public ImageView mImageViewTripImage;
    public ImageView mImageViewFavIcon;

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