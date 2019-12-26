// @author Bhavya Mehta
package shy7lo.com.shy7lo.sidebar;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import shy7lo.com.shy7lo.BrandOptionActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.SortingList;
import shy7lo.com.shy7lo.pref.MyPref;

public class SectionHeaderAdapter extends BaseAdapter implements OnScrollListener, Filterable {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SECTION = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SECTION + 1;

    ArrayList<Integer> mListSectionPos;
    ArrayList<SortingList.Option> mOptionListItems;
    private OnBrandOptionSelectListener mOnBrandOptionSelectListener;

    Context mContext;

    public SectionHeaderAdapter(Context context, ArrayList<SortingList.Option> mOptionListItems, ArrayList<Integer> listSectionPos, OnBrandOptionSelectListener mOnBrandOptionSelectListener) {
        this.mContext = context;
        this.mOptionListItems = mOptionListItems;
        this.mListSectionPos = listSectionPos;
        this.mOnBrandOptionSelectListener = mOnBrandOptionSelectListener;
    }

    public interface OnBrandOptionSelectListener {
        public void onBrandOptionSelected(boolean isOptionSelected, int position);
    }

    @Override
    public int getCount() {
        return mOptionListItems.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return !mListSectionPos.contains(position);
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return mListSectionPos.contains(position) ? TYPE_SECTION : TYPE_ITEM;
    }

    @Override
    public Object getItem(int position) {
        return mOptionListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mOptionListItems.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int type = getItemViewType(position);
        final SortingList.Option mOption = mOptionListItems.get(position);

        switch (type) {
            case TYPE_ITEM:
                if (convertView == null) {
                    ViewBrandHolder brandHolder = new ViewBrandHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_brand, null);
                    brandHolder.tvBrandTitle = (TextView) convertView.findViewById(R.id.tvBrandTitle);
                    brandHolder.ivDone = (ImageView) convertView.findViewById(R.id.ivDone);
                    convertView.setTag(brandHolder);
                }

                ViewBrandHolder brandHolder = (ViewBrandHolder) convertView.getTag();

                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    brandHolder.tvBrandTitle.setScaleX(-1f);
                    brandHolder.ivDone.setScaleX(-1f);
                    brandHolder.tvBrandTitle.setGravity(Gravity.RIGHT);

                } else {
                    brandHolder.tvBrandTitle.setScaleX(1f);
                    brandHolder.ivDone.setScaleX(1f);
                    brandHolder.tvBrandTitle.setGravity(Gravity.LEFT);
                }

                brandHolder.tvBrandTitle.setText(mOption.getLabel());

                if (mOption.isStatus()) {
                    brandHolder.ivDone.setVisibility(View.VISIBLE);
//                    brandHolder.ivDone.setBackgroundResource(R.drawable.ic_dot_fill);
                } else {
                    brandHolder.ivDone.setVisibility(View.INVISIBLE);
//                    brandHolder.ivDone.setBackgroundResource(R.drawable.ic_dot_blank);
                }

                break;
            case TYPE_SECTION:

                if (convertView == null) {
                    ViewHolder holder = new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.sidebar_section_row_view, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.row_title);
                    convertView.setTag(holder);
                }

                ViewHolder holder = (ViewHolder) convertView.getTag();

                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    holder.textView.setScaleX(-1f);
                    holder.textView.setGravity(Gravity.RIGHT);
                } else {
                    holder.textView.setScaleX(1f);
                    holder.textView.setGravity(Gravity.LEFT);
                }


                holder.textView.setText(mOption.getLabel().toString());
                break;
        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type == TYPE_ITEM) {

                    boolean status = !mOptionListItems.get(position).isStatus();
                    mOptionListItems.get(position).setStatus(status);

                    boolean isSelected = false;
                    for (int i = 0; i < mOptionListItems.size(); i++) {
                        if (mOptionListItems.get(i).isStatus()) {
                            isSelected = true;
                            break;
                        }
                    }
                    mOnBrandOptionSelectListener.onBrandOptionSelected(isSelected, position);
//                    mOnBrandOptionSelectListener.onBrandOptionSelected(status, position);
//                    for (int i = 0; i < mOptionListItems.size(); i++) {
//                        mOptionListItems.get(i).setStatus(false);
//                    }



                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    public class ViewBrandHolder {
        TextView tvBrandTitle;
        ImageView ivDone;
    }

    public class ViewHolder {
        public TextView textView;
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof SectionHeaderListView) {
//            ((SectionHeaderListView) view).configureHeaderView(firstVisibleItem);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public Filter getFilter() {
        return ((BrandOptionActivity) mContext).new ListFilter();
    }

    public String[] getSelectedValue() {

        StringBuilder sbValue = new StringBuilder();
        StringBuilder sbId = new StringBuilder();
        if (mOptionListItems != null && mOptionListItems.size() > 0) {
            for (int i = 0; i < mOptionListItems.size(); i++) {
                if (mOptionListItems.get(i).isStatus()) {
                    if (sbValue.length() > 0) {
                        sbValue.append("," + mOptionListItems.get(i).getLabel());
                        sbId.append("," + mOptionListItems.get(i).getId());

                    } else {
                        sbValue.append(mOptionListItems.get(i).getLabel());
                        sbId.append(mOptionListItems.get(i).getId());
                    }

                }
            }
        }

        return new String[]{sbValue.toString(), sbId.toString()};
    }

    public void clear() {
        if (mOptionListItems != null && mOptionListItems.size() > 0) {
            for (int i = 0; i < mOptionListItems.size(); i++) {
                mOptionListItems.get(i).setStatus(false);
            }
        }
    }


}
