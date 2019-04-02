package cristian.babarusi.simpletraveljournal.recyclerViewSources;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import java.util.List;

import cristian.babarusi.simpletraveljournal.ManageTripActivity;
import cristian.babarusi.simpletraveljournal.R;

public class DestinationsAdapter extends RecyclerView.Adapter<DestinationsViewHolder> {

    public static final String KEY_TITLE = "keyTitle";
    public static final String KEY_SUBTITLE = "keySubtitle";
    private Context mContext;
    private List<Destinations> mListDestinations;

    private ImageView mImageViewDestinations;
    private TextView mTextViewEdit;
    private TextView mTextViewDelete;

    //constructor custom
    public DestinationsAdapter(Context context, List<Destinations> list) {
        mListDestinations = list;
        mContext = context;
    }

    //draw layout
    @NonNull
    @Override
    public DestinationsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trip_list_item,
                        viewGroup, false);

        //initViews
        mImageViewDestinations = itemView.findViewById(R.id.image_view_travel_list_picture);
        mTextViewEdit = itemView.findViewById(R.id.text_view_travel_list_edit);
        mTextViewDelete = itemView.findViewById(R.id.text_view_travel_list_delete);

        return new DestinationsViewHolder(itemView);
    }

    //populate layout with items
    @Override
    public void onBindViewHolder(@NonNull final DestinationsViewHolder destinationsViewHolder, int i) {

        final int position = i;
        Destinations currentDestination = mListDestinations.get(position);
        destinationsViewHolder.getTextViewTripNameTitle().setText(currentDestination.getTripNameTitle());
        destinationsViewHolder.getTextViewDestinationSubtitle().setText(currentDestination.getDestinationSubtitle());

        Glide.with(mContext).load(currentDestination.getPicture()).placeholder(R.drawable.ic_launcher_background)
                .into(destinationsViewHolder.getImageViewPicture());

        final String itemTitle = mListDestinations.get(i).getTripNameTitle();
        final String itemSubtitle = mListDestinations.get(i).getDestinationSubtitle();

        //TODO temporary clicks procedures

        //item list CLICK
        destinationsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, itemTitle + "\n" + itemSubtitle, Toast.LENGTH_SHORT).show();
            }
        });

        //item list long CLICK
        destinationsViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intentManageTrip = new Intent(mContext, ManageTripActivity.class);
                intentManageTrip.putExtra(KEY_TITLE, itemTitle);
                intentManageTrip.putExtra(KEY_SUBTITLE, itemSubtitle);
                mContext.startActivity(intentManageTrip);
                return true;
            }
        });

        //on image click
        mImageViewDestinations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked image at pos: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        //on edit CLICK
        mTextViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked Edit at pos: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        //on edit CLICK
        mTextViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked Delete at pos: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListDestinations.size();
    }
}
