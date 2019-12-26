package shy7lo.com.shy7lo.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.CMSPage;
import shy7lo.com.shy7lo.pref.MyPref;

/**
 * Created by JITEN-PC on 15-02-2017.
 */

public class InfoOptionAdapter extends BaseAdapter {

//    Activity activity;
//    List<String> mOptionList;
//
//    public InfoOptionAdapter(Activity activity, List<String> mOptionList) {
//        this.activity = activity;
//        this.mOptionList = mOptionList;
//    }
//
//    @Override
//    public int getCount() {
//        return mOptionList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return mOptionList.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//
//    @Override
//    public View getView(final int position, View view, ViewGroup viewGroup) {
//
//        ViewHolder holder;
//
//        if (view == null) {
//            view = LayoutInflater.from(activity).inflate(R.layout.list_item_info_option, null);
//            holder = new ViewHolder();
//            holder.lnrOption = (LinearLayout) view.findViewById(R.id.lnrOption);
//            holder.tvOptionTitle = (TextView) view.findViewById(R.id.tvOptionTitle);
//            view.setTag(holder);
//        } else {
//            holder = (ViewHolder) view.getTag();
//        }
//
//        final String mOption = mOptionList.get(position);
//
//        if (MyPref.getPref(activity, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            holder.lnrOption.setScaleX(-1f);
//            holder.tvOptionTitle.setScaleX(-1f);
//            holder.tvOptionTitle.setGravity(Gravity.RIGHT);
//        } else {
//            holder.lnrOption.setScaleX(1f);
//            holder.tvOptionTitle.setScaleX(1f);
//            holder.tvOptionTitle.setGravity(Gravity.LEFT);
//        }
//
//        holder.tvOptionTitle.setText("" + mOption);
//
//        return view;
//    }
//
//    private class ViewHolder {
//        TextView tvOptionTitle;
//        LinearLayout lnrOption;
//    }

    Activity activity;
    List<CMSPage.Child> mOptionList;

    public InfoOptionAdapter(Activity activity, List<CMSPage.Child> mOptionList) {
        this.activity = activity;
        this.mOptionList = mOptionList;
    }

    @Override
    public int getCount() {
        return mOptionList.size();
    }

    @Override
    public Object getItem(int i) {
        return mOptionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.list_item_info_option, null);
            holder = new ViewHolder();
            holder.lnrOption = (LinearLayout) view.findViewById(R.id.lnrOption);
            holder.tvOptionTitle = (TextView) view.findViewById(R.id.tvOptionTitle);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final CMSPage.Child mOption = mOptionList.get(position);

        if (MyPref.getPref(activity, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.lnrOption.setScaleX(-1f);
            holder.tvOptionTitle.setScaleX(-1f);
            holder.tvOptionTitle.setGravity(Gravity.RIGHT);

            holder.tvOptionTitle.setText("" + mOption.titleAr);
        } else {
            holder.lnrOption.setScaleX(1f);
            holder.tvOptionTitle.setScaleX(1f);
            holder.tvOptionTitle.setGravity(Gravity.LEFT);

            holder.tvOptionTitle.setText("" + mOption.title);
        }



        return view;
    }

    private class ViewHolder {
        TextView tvOptionTitle;
        LinearLayout lnrOption;
    }
}
