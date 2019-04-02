package cristian.babarusi.simpletraveljournal.recyclerViewSources;

public class Destinations {

    private String mTripNameTitle;
    private String mDestinationSubtitle;
    private int mPicture;
    private double mRating;

    //TODO complete rest of datas fields from DB (rating, favorite boolean, id, reference... etc

    public String getTripNameTitle() {
        return mTripNameTitle;
    }

    public void setTripNameTitle(String tripNameTitle) {
        mTripNameTitle = tripNameTitle;
    }

    public String getDestinationSubtitle() {
        return mDestinationSubtitle;
    }

    public void setDestinationSubtitle(String destinationSubtitle) {
        mDestinationSubtitle = destinationSubtitle;
    }

    public int getPicture() {
        return mPicture;
    }

    public void setPicture(int picture) {
        mPicture = picture;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }
}
