package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.SortingPojo;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;

public class DialogSortingAdapter extends BaseAdapter {

    private Context mContext;
    private OnSortClickListener onSortClickListener;
    List<SortingPojo.SortingData> mSortingDataList = new ArrayList<>();


    public DialogSortingAdapter(Context mContext, OnSortClickListener onSortClickListener) {
        this.mContext = mContext;
        this.onSortClickListener = onSortClickListener;
    }

    public interface OnSortClickListener {
        public void onSortClicked(SortingPojo.SortingData mSortingData);
    }

    public void setData(List<SortingPojo.SortingData> mTempCMSPageList) {

        if (mSortingDataList != null && mSortingDataList.size() > 0) {
            mSortingDataList.clear();
        }

        if (mTempCMSPageList != null && mTempCMSPageList.size() > 0) {
            mSortingDataList.addAll(mTempCMSPageList);
        }
        LogUtils.e("", "mSortingDataList::" + mSortingDataList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSortingDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mSortingDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_dialog_sorting, null);

            ViewHolder holder = new ViewHolder();
            holder.tvSort = (TextView) view.findViewById(R.id.tvSort);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        final SortingPojo.SortingData mSortingData = mSortingDataList.get(position);
        if (mSortingData != null) {
            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                holder.tvSort.setText("" + mSortingData.titleAr);
                holder.tvSort.setGravity(Gravity.RIGHT);
            } else {
                holder.tvSort.setText("" + mSortingData.titleEn);
                holder.tvSort.setGravity(Gravity.LEFT);
            }

            holder.tvSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            if (mSortingData.isSelected) {
                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    holder.tvSort.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_black, 0, 0, 0);
                } else {
                    holder.tvSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_black, 0);
                }
            }

            holder.tvSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < mSortingDataList.size(); i++) {
                        if (i == position) {
                            mSortingDataList.get(i).isSelected = true;
                        } else {
                            mSortingDataList.get(i).isSelected = false;
                        }
                    }
                    notifyDataSetChanged();

                    onSortClickListener.onSortClicked(mSortingData);
                }
            });

        }

        return view;
    }

    private class ViewHolder {
        TextView tvSort;
    }


}
