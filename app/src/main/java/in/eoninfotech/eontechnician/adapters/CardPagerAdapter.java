package in.eoninfotech.eontechnician;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import in.eoninfotech.eontechnician.responses.TechnicianMonthDetail;
import in.eoninfotech.eontechnician.responses.TechnicianMonthResponse;
import in.eoninfotech.eontechnician.helper.K;
import retrofit2.Callback;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private float mBaseElevation;
    private  ArrayList<TechnicianMonthDetail> techList = null;

    public CardPagerAdapter(ArrayList<TechnicianMonthDetail> techList, Callback<TechnicianMonthResponse> callback) {

        this.techList = techList;
        mViews = new ArrayList<>();
    }
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
       // return mViews.get(position);
        return null;
    }

    @Override
    public int getCount() {
        return techList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragment_adapter, container, false);
        container.addView(view);
        bind(techList.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
//        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//        mViews.set(position, null);
    }

    private void bind(TechnicianMonthDetail item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        CircleImageView ivProfile = (CircleImageView)view.findViewById(R.id.ivProfile);
        titleTextView.setText(item.getMonth()+" "+item.getYear());
        contentTextView.setText(item.getName());

        String ImageUri = K.Url.IMAGE_URL+item.getImage();
        ImageUtils.glideImage(ivProfile, ImageUri,R.drawable.user);

    }

}
