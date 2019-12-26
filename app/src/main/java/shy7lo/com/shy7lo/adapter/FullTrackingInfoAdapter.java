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
import shy7lo.com.shy7lo.model.MyOrderDetailsResponse;
import shy7lo.com.shy7lo.pref.MyPref;

public class FullTrackingInfoAdapter extends BaseAdapter {

    private Context mContext;
    List<MyOrderDetailsResponse.FullTrackingInfo> fullTrackingInfo = new ArrayList<>();

    public FullTrackingInfoAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(List<MyOrderDetailsResponse.FullTrackingInfo> tempFullTrackingInfo) {
        if (fullTrackingInfo != null && fullTrackingInfo.size() > 0) {
            fullTrackingInfo.clear();
        }

        if (tempFullTrackingInfo != null && tempFullTrackingInfo.size() > 0) {
            fullTrackingInfo.addAll(tempFullTrackingInfo);
        }
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return fullTrackingInfo.size();
    }

    @Override
    public Object getItem(int i) {
        return fullTrackingInfo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_full_track, null);
            ViewHolder holder = new ViewHolder();
            holder.tvStatus = view.findViewById(R.id.tvStatus);
            holder.tvDate = view.findViewById(R.id.tvDate);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();


        final MyOrderDetailsResponse.FullTrackingInfo value = fullTrackingInfo.get(i);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvStatus.setScaleX(-1f);
            holder.tvDate.setScaleX(-1f);
            holder.tvStatus.setGravity(Gravity.RIGHT);
        } else {
            holder.tvDate.setScaleX(1f);
            holder.tvStatus.setScaleX(1f);
            holder.tvStatus.setGravity(Gravity.LEFT);
        }

        if (value != null) {
            holder.tvStatus.setText("" + value.status);
            holder.tvDate.setText("" + value.createdAt);
        }

        return view;
    }

    private class ViewHolder {
        TextView tvStatus, tvDate;
    }


}
