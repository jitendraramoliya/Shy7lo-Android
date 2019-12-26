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
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.pref.MyPref;

public class HomeCategoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<AppInit.BaseScreen> mBaseScreenList = new ArrayList<>();

    public HomeCategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void addAll(List<AppInit.BaseScreen> mTempBaseScreenList) {
        if (mBaseScreenList != null && mBaseScreenList.size() > 0) {
            mBaseScreenList.clear();
        }

        if (mTempBaseScreenList != null && mTempBaseScreenList.size() > 0) {
            mBaseScreenList.addAll(mTempBaseScreenList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mBaseScreenList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBaseScreenList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_category, null);
            ViewHolder holder = new ViewHolder();
            holder.tvCategory = view.findViewById(R.id.tvCategory);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        AppInit.BaseScreen mCategory = mBaseScreenList.get(i);
        if (mCategory != null) {
            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                holder.tvCategory.setText("" + mCategory.titleAr);
            } else {
                holder.tvCategory.setText("" + mCategory.titleEn);
            }

        }

        return view;
    }

    private class ViewHolder {
        TextView tvCategory;
    }


}
