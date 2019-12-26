package shy7lo.com.shy7lo.adapter;

import android.content.Context;
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
import shy7lo.com.shy7lo.utils.LogUtils;

import static shy7lo.com.shy7lo.R.id.tvBrandTitle;

/**
 * Created by JITEN-PC on 15-02-2017.
 */

public class FilterOptionAdapter extends BaseAdapter {

    Context mContext;
    List<SortingList.Option> mValueList;
    boolean isMultiSelected;
    //    List<SortingList.Option> mTempValueList = new ArrayList<>();
    private OnFilterOptionSelectListener mOnFilterOptionSelectListener;

    public FilterOptionAdapter(Context mContext, List<SortingList.Option> mValueList, boolean isMultiSelected, OnFilterOptionSelectListener mOnFilterOptionSelectListener) {
        this.mContext = mContext;
        this.mValueList = mValueList;
        this.isMultiSelected = isMultiSelected;
//        this.mTempValueList.addAll(mOptionList);
        this.mOnFilterOptionSelectListener = mOnFilterOptionSelectListener;
    }

    public interface OnFilterOptionSelectListener {
        public void onFilterOptionSelected(boolean isOptionSelected, int position);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_sort, null);
            holder = new ViewHolder();
            holder.tvBrandTitle = (TextView) view.findViewById(tvBrandTitle);
            holder.ivDone = (ImageView) view.findViewById(R.id.ivDone);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final SortingList.Option value = mValueList.get(position);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvBrandTitle.setScaleX(-1f);
            holder.ivDone.setScaleX(-1f);
            holder.tvBrandTitle.setGravity(Gravity.RIGHT);

        } else {
            holder.tvBrandTitle.setScaleX(1f);
            holder.ivDone.setScaleX(1f);
            holder.tvBrandTitle.setGravity(Gravity.LEFT);
        }

        if (value != null) {
            holder.tvBrandTitle.setText(value.getLabel());
            LogUtils.e("", position + " value.getStatus()::" + value.isStatus());

            if (value.isStatus()) {
                holder.ivDone.setVisibility(View.VISIBLE);
//                holder.ivDone.setBackgroundResource(R.drawable.ic_dot_fill);
            } else {
                holder.ivDone.setVisibility(View.INVISIBLE);
//                holder.ivDone.setBackgroundResource(R.drawable.ic_dot_blank);
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                boolean status = !mTempValueList.get(position).isStatus();
////                mOptionList.get(position).setStatus(status);
//
//                for (int i = 0; i < mTempValueList.size(); i++) {
//                    mTempValueList.get(i).setStatus(false);
//                    if (mTempValueList.get(i).getLabel().equalsIgnoreCase(value.getLabel())) {
//                        mTempValueList.get(position).setStatus(status);
////                        mOptionList.get(position).setStatus(status);
//                    }
//                }

                boolean status = !mValueList.get(position).isStatus();
//                if (position == 0 && !status) {
//                    return;
//                } else {
////                    LogUtils.e("", position + " status::" + status);
////                    if (!status) {
////                        mOptionList.get(0).setStatus(true);
////                        mTempValueList.get(0).setStatus(true);
////
////                    }
////                    LogUtils.e("", position + " isStatus::" + mOptionList.get(0).isStatus());
////                    LogUtils.e("", position + " mTempValueList isStatus::" + mTempValueList.get(0).isStatus());
//                }

//                mOnFilterOptionSelectListener.onFilterOptionSelected(status, position);

                if (!isMultiSelected) {
                    for (int i = 0; i < mValueList.size(); i++) {
                        mValueList.get(i).setStatus(false);
                    }
                    mValueList.get(position).setStatus(status);
                    mOnFilterOptionSelectListener.onFilterOptionSelected(status, position);
                } else {
                    mValueList.get(position).setStatus(status);
                    boolean isSelected = false;
                    for (int i = 0; i < mValueList.size(); i++) {
                        if (mValueList.get(i).isStatus()) {
                            isSelected = true;
                            break;
                        }
                    }
                    mOnFilterOptionSelectListener.onFilterOptionSelected(isSelected, position);
                }



//                for (int i = 0; i < mTempValueList.size(); i++) {
//                    mTempValueList.get(i).setStatus(false);
//                    if (mTempValueList.get(i).getLabel().equalsIgnoreCase(value.getLabel())) {
//                        mTempValueList.get(i).setStatus(status);
////                        mOptionList.get(position).setStatus(status);
//                    }
//                }

//                if (!status && position != 0) {
//                    mOptionList.get(0).setStatus(true);
//                    mTempValueList.get(0).setStatus(true);
//
//                }


                notifyDataSetChanged();
            }
        });


        return view;
    }

//    public void filter(String charText) {
//
//        charText = charText.toLowerCase(Locale.getDefault());
//        mOptionList.clear();
//
//        if (charText.length() == 0) {
//            mOptionList.addAll(mTempValueList);
//        } else {
//
//            for (SortingList.Option value : mTempValueList) {
//                if (charText.length() != 0 && value.getLabel().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    mOptionList.add(value);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    public String[] getSelectedValue() {

        StringBuilder sbValue = new StringBuilder();
        StringBuilder sbId = new StringBuilder();
        if (mValueList != null && mValueList.size() > 0) {
            for (int i = 0; i < mValueList.size(); i++) {
                if (mValueList.get(i).isStatus()) {
                    if (sbValue.length() > 0) {
                        sbValue.append("," + mValueList.get(i).getLabel());
                        sbId.append("," + mValueList.get(i).getId());

                    } else {
                        sbValue.append(mValueList.get(i).getLabel());
                        sbId.append(mValueList.get(i).getId());
                    }

                }
            }
        }

        return new String[]{sbValue.toString(), sbId.toString()};
    }

//    public void clear() {
//        if (mTempValueList != null && mTempValueList.size() > 0) {
//            for (int i = 0; i < mTempValueList.size(); i++) {
//                mTempValueList.get(i).setStatus(false);
//            }
//            mOptionList.clear();
//            mOptionList.addAll(mTempValueList);
//        }
//    }

    public void clear() {
        if (mValueList != null && mValueList.size() > 0) {
            for (int i = 0; i < mValueList.size(); i++) {
                mValueList.get(i).setStatus(false);
            }
        }
    }


    private class ViewHolder {
        TextView tvBrandTitle;
        ImageView ivDone;
    }
}
