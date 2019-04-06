package cristian.babarusi.simpletraveljournal.recyclerViewSources;

import android.widget.ImageButton;

public interface OnTripSelectedListener<T> {

    void onTripSelected(T trip);

    void onTripLongPressed(T trip);

    void onIconPressed(T trip, ImageButton imageButton);

    void onDeletePressed(T trip, ImageButton imageButton);

    void onDeleteLongPressed(T trip, ImageButton imageButton);

}