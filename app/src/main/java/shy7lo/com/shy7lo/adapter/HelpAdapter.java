package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.CMSPage;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;

public class HelpAdapter extends BaseAdapter {

    private Context mContext;
    private boolean isSideBarHelp;
    List<CMSPage.Data> mCMSPageList = new ArrayList<>();


    public HelpAdapter(Context mContext, boolean isSideBarHelp) {
        this.mContext = mContext;
        this.isSideBarHelp = isSideBarHelp;
    }

    public void setData(List<CMSPage.Data> mTempCMSPageList) {

        if (mCMSPageList != null && mCMSPageList.size() > 0) {
            mCMSPageList.clear();
        }

        if (mTempCMSPageList != null && mTempCMSPageList.size() > 0) {
            mCMSPageList.addAll(mTempCMSPageList);
        }
        LogUtils.e("", "mSortingDataList::" + mCMSPageList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCMSPageList.size();
    }

    @Override
    public Object getItem(int i) {
        return mCMSPageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            if (isSideBarHelp) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_sidebar_help, null);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_help, null);
            }

            ViewHolder holder = new ViewHolder();
            holder.tvAppInfo = (TextView) view.findViewById(R.id.tvAppInfo);
            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        CMSPage.Data mCMSPage = mCMSPageList.get(position);
        if (mCMSPage != null) {
            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                holder.tvAppInfo.setText("" + mCMSPage.titleAr);
            } else {
                holder.tvAppInfo.setText("" + mCMSPage.title);
            }
        }

        return view;
    }

    private class ViewHolder {
        TextView tvAppInfo;
    }


}
