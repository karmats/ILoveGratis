package net.karmats.ilovegratis.adapter;

import java.util.List;

import net.karmats.ilovegratis.R;
import net.karmats.ilovegratis.dto.AdDto;
import net.karmats.ilovegratis.util.ILoveGratisUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdsAdapter extends BaseAdapter {

    private List<AdDto> ads;
    private Context context;
    private ActivityName activityName;

    /**
     * Creates a new instance of {@link ListAdsAdapter}
     * 
     * @param context
     *            The context
     * @param textViewResourceId
     *            The rows text view resource id
     * @param ads
     *            The ads to have in this adapter
     * @param activityName
     *            The activity name enum
     */
    public ListAdsAdapter(Context context, int textViewResourceId, List<AdDto> ads, ActivityName activityName) {
        this.ads = ads;
        this.context = context;
        this.activityName = activityName;
    }

    public void refreshAds(List<AdDto> ads) {
        switch (activityName) {
        case ADS:
            if (this.ads == null) {
                this.ads = ads;
            } else {
                this.ads.addAll(ads);
            }
            break;
        case FIVE_LATEST:
            this.ads = ads;
            break;
        default:
            break;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ads.size();
    }

    @Override
    public Object getItem(int position) {
        return ads.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.drawable.adrow, null);
        }
        AdDto ad = ads.get(position);
        if (ad != null) {
            TextView adTitle = (TextView) v.findViewById(R.id.adTitle);
            TextView adDate = (TextView) v.findViewById(R.id.adDate);
            TextView adCountyOrCategory = (TextView) v.findViewById(R.id.adCountyOrCategory);
            ImageView adImg = (ImageView) v.findViewById(R.id.adImg);
            adTitle.setText(ad.getTitle());
            adDate.setText(ILoveGratisUtil.getDateAsReadableString(ad.getDateUploaded(), context.getString(R.string.today),
                                                                   context.getString(R.string.yesterday)));
            if (activityName.equals(ActivityName.FIVE_LATEST)) {
                adCountyOrCategory.setText(ad.getCounty());
            } else {
                adCountyOrCategory.setText(ad.getCategory());
            }
            if (ad.getImgThumb() == null) {
                ad.setImgThumb(ILoveGratisUtil.getImageBitmap(ad.getImgThumbSrc()));
            }
            adImg.setImageBitmap(ad.getImgThumb());
        }
        return v;
    }

    public enum ActivityName {
        FIVE_LATEST, ADS;
    }

}
