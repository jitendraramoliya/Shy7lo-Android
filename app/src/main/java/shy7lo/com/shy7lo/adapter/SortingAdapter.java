package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.SortingList;
import shy7lo.com.shy7lo.pref.MyPref;

import static shy7lo.com.shy7lo.R.id.ivDone;

/**
 * Created by JITEN-PC on 15-02-2017.
 */

public class SortingAdapter extends BaseAdapter {

    Context mContext;
    List<SortingList.SortingData> mValueList;

    public SortingAdapter(Context mContext, List<SortingList.SortingData> mValueList) {
        this.mContext = mContext;
        this.mValueList = mValueList;
    }

    @Override
    public int getCount() {
        return mValueList.size();
    }

    @Override
    public Object getItem(int i) {
        return mValueList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_sorting, null);
            holder = new ViewHolder();
            holder.ivSortingImg = (ImageView) view.findViewById(R.id.ivSortingImg);
            holder.ivDone = (ImageView) view.findViewById(ivDone);
            holder.tvSortingTitle = (TextView) view.findViewById(R.id.tvSortingTitle);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvSortingTitle.setScaleX(-1f);
            holder.ivSortingImg.setScaleX(-1f);
            holder.ivDone.setScaleX(-1f);
            holder.tvSortingTitle.setGravity(Gravity.RIGHT);
        } else {
            holder.tvSortingTitle.setScaleX(1f);
            holder.ivSortingImg.setScaleX(1f);
            holder.ivDone.setScaleX(1f);
            holder.tvSortingTitle.setGravity(Gravity.LEFT);
        }

        SortingList.SortingData value = mValueList.get(position);

        if (value != null) {
            holder.tvSortingTitle.setText(value.getLabel());

            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_English)) {
                if (value.getCode().equalsIgnoreCase("position")) {
                    holder.tvSortingTitle.setText("Position");
                } else if (value.getCode().equalsIgnoreCase("price")) {
                    if (!TextUtils.isEmpty(value.getDirection()) && value.getDirection().equalsIgnoreCase("ASC")) {
                        holder.tvSortingTitle.setText("Price (Low to High)");
                    } else if (!TextUtils.isEmpty(value.getDirection()) && value.getDirection().equalsIgnoreCase("DESC")) {
                        holder.tvSortingTitle.setText("Price (High to Low)");
                    }
                }

            } else {
                if (value.getCode().equalsIgnoreCase("name")) {
                    holder.tvSortingTitle.setText("اسم المنتج");
                } else if (value.getCode().equalsIgnoreCase("bag_size")) {
                    holder.tvSortingTitle.setText("فرعية");
                } else if (value.getCode().equalsIgnoreCase("created_at")) {
                    holder.tvSortingTitle.setText("وصل حديثًا");
                } else if (value.getCode().equalsIgnoreCase("saving")) {
                    holder.tvSortingTitle.setText("تخفيضات");
                } else if (value.getCode().equalsIgnoreCase("bestsellers")) {
                    holder.tvSortingTitle.setText("الأفضل مبيعًا");
                } else if (value.getCode().equalsIgnoreCase("most_viewed")) {
                    holder.tvSortingTitle.setText("الأكثر شهرة");
                } else if (value.getCode().equalsIgnoreCase("price")) {
                    if (!TextUtils.isEmpty(value.getDirection()) && value.getDirection().equalsIgnoreCase("ASC")) {
                        holder.tvSortingTitle.setText("السعر (الأقل)");
                    } else if (!TextUtils.isEmpty(value.getDirection()) && value.getDirection().equalsIgnoreCase("DESC")) {
                        holder.tvSortingTitle.setText("السعر (الأعلى)");
                    }
                }
            }

            if (value.isStatus()) {
                holder.ivDone.setVisibility(View.VISIBLE);
            } else {
                holder.ivDone.setVisibility(View.INVISIBLE);
            }
        }


        return view;
    }

    private class ViewHolder {
        ImageView ivSortingImg, ivDone;
        TextView tvSortingTitle;
    }
}
