package cristian.babarusi.simpletraveljournal.recyclerViewSources;

import android.content.Context;

public class TripModel {

    //used to represent fields from the recyclerView item
    private String mTripName;
    private String mDestination;
    private String mImageUrl;
    private String mFileReference;
    private float mRating;
    private boolean mFlagFav;

    public TripModel() {
        //need to be this empty constructor
    }

    //my custom constructor to initialize variables
    public TripModel(Context context, String tripName, String destination, String imageUrl, float rating, boolean flagFav) {
        this.mTripName = tripName;
        this.mDestination = destination;
        this.mImageUrl = imageUrl;
        this.mRating = rating;
        this.mFlagFav = flagFav;
    }

    public String getTripName() {
        return mTripName;
    }

    public String getDestination() {
        return mDestination;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public float getRating() {
        return mRating;
    }

    public boolean isFlagFav() {
        return mFlagFav;
    }

    public String getFileReference() {
        return mFileReference;
    }
}
