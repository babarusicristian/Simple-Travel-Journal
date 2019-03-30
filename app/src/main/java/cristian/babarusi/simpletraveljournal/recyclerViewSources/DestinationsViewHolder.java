package cristian.babarusi.simpletraveljournal.recyclerViewSources;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cristian.babarusi.simpletraveljournal.R;

public class DestinationsViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextViewTripNameTitle;
    private TextView mTextViewDestinationSubtitle;
    private ImageView mImageViewPicture;

    public DestinationsViewHolder(@NonNull View itemView) {
        super(itemView);

        mTextViewTripNameTitle = itemView.findViewById(R.id.text_view_trip_name_title);
        mTextViewDestinationSubtitle = itemView.findViewById(R.id.text_view_destination_subtitle);
        mImageViewPicture = itemView.findViewById(R.id.image_view_travel_list_picture);
    }

    public TextView getTextViewTripNameTitle() {
        return mTextViewTripNameTitle;
    }

    public void setTextViewTripNameTitle(TextView textViewTripNameTitle) {
        mTextViewTripNameTitle = textViewTripNameTitle;
    }

    public TextView getTextViewDestinationSubtitle() {
        return mTextViewDestinationSubtitle;
    }

    public void setTextViewDestinationSubtitle(TextView textViewDestinationSubtitle) {
        mTextViewDestinationSubtitle = textViewDestinationSubtitle;
    }

    public ImageView getImageViewPicture() {
        return mImageViewPicture;
    }

    public void setImageViewPicture(ImageView imageViewPicture) {
        mImageViewPicture = imageViewPicture;
    }
}
