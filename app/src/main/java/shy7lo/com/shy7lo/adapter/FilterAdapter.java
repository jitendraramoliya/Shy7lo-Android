package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.SortingList;
import shy7lo.com.shy7lo.pref.MyPref;

/**
 * Created by JITEN-PC on 15-02-2017.
 */

public class FilterAdapter extends BaseAdapter {

    Context mContext;
    List<SortingList.FilterData> mValueList;

    public FilterAdapter(Context mContext, List<SortingList.FilterData> mValueList) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_filter, null);
            holder = new ViewHolder();
            holder.tvFilterTitle = (TextView) view.findViewById(R.id.tvFilterTitle);
            holder.tvFilterName = (TextView) view.findViewById(R.id.tvFilterName);
            holder.lnrRowLayout = (LinearLayout) view.findViewById(R.id.lnrRowLayout);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SortingList.FilterData value = mValueList.get(position);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.lnrRowLayout.setScaleX(-1f);
            holder.tvFilterTitle.setScaleX(-1f);
            holder.tvFilterName.setScaleX(-1f);
            holder.tvFilterTitle.setGravity(Gravity.RIGHT);
            holder.tvFilterName.setGravity(Gravity.RIGHT);
//            holder.tvFilterName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dot_fill, 0);
        } else {
            holder.lnrRowLayout.setScaleX(1f);
            holder.tvFilterTitle.setScaleX(1f);
            holder.tvFilterName.setScaleX(1f);
            holder.tvFilterTitle.setGravity(Gravity.LEFT);
            holder.tvFilterName.setGravity(Gravity.LEFT);
//            holder.tvFilterName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dot_fill, 0, 0, 0);
        }

        if (value != null) {
            holder.tvFilterTitle.setText(value.getLabel());
            if (value.isFilterSelected()) {
                if (!TextUtils.isEmpty(value.getFilterValue())){
                    if (value.getFilterValue().contains(",")){
                        int size = value.getFilterValue().split(",").length;
                        holder.tvFilterName.setText(""+size);
                    }else{
                        holder.tvFilterName.setText(value.getFilterValue());
                    }
                }

            } else {
                if (value.getLabel().equalsIgnoreCase(""+mContext.getResources().getString(R.string.category))) {
                    if (!TextUtils.isEmpty(value.getFilterValue())){
                        if (value.getFilterValue().contains(",")){
                            int size = value.getFilterValue().split(",").length;
                            holder.tvFilterName.setText(""+size);
                        }else{
                            holder.tvFilterName.setText(value.getFilterValue());
                        }
                    }
                } else {
                    holder.tvFilterName.setText("" + mContext.getString(R.string.all));
                }
            }


        }


        return view;
    }

    private class ViewHolder {
        TextView tvFilterTitle, tvFilterName;
        LinearLayout lnrRowLayout;
    }


}
