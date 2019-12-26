package shy7lo.com.shy7lo.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.SortingList;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.widget.FilterExpandableListView;

/**
 * Created by JITEN-PC on 13-09-2016.
 */
public class FilterExpandableAdapter extends FilterExpandableListView.FilterExpandableAdapter {

    private Activity mContext;
    private Map<String, List<String>> categoryCollections;
    private List<String> categoryList;
    private List<SortingList.FilterData> mFilterDataList;

//    public CategoryExpandableAdapter(Activity mContext, List<String> categoryList,
//                                   Map<String, List<String>> categoryCollections) {
//        this.mContext = mContext;
//        this.categoryCollections = categoryCollections;
//        this.categoryList = categoryList;
//    }

    public FilterExpandableAdapter(Activity mContext, List<SortingList.FilterData> mFilterDataList) {
        this.mContext = mContext;
        this.mFilterDataList = mFilterDataList;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expand_item_filters, null);
            holder = new ChildViewHolder();
            holder.tvFilterItem = (TextView) convertView.findViewById(R.id.tvFilterItem);
            holder.ivFilterItem = (ImageView) convertView.findViewById(R.id.ivFilterItem);
            holder.ivFilterRightItem = (ImageView) convertView.findViewById(R.id.ivFilterRightItem);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }


        final SortingList.Option mSortingOption = (SortingList.Option) getChild(groupPosition, childPosition);
        final String filterItem = mSortingOption.getLabel();
//        final String filterItem = (String) getChild(groupPosition, childPosition);

        holder.tvFilterItem.setText(filterItem);

        if (mSortingOption.isStatus()) {
            holder.ivFilterItem.setBackgroundResource(R.drawable.ic_dot_fill);
            holder.ivFilterRightItem.setBackgroundResource(R.drawable.ic_dot_fill);

        } else {
            holder.ivFilterItem.setBackgroundResource(R.drawable.ic_dot_blank);
            holder.ivFilterRightItem.setBackgroundResource(R.drawable.ic_dot_blank);
        }

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvFilterItem.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvFilterItem.setGravity(Gravity.RIGHT);
            holder.ivFilterRightItem.setVisibility(View.VISIBLE);
            holder.ivFilterItem.setVisibility(View.GONE);
        } else {
            holder.tvFilterItem.setTypeface(Shy7lo.RalewayRegular);
//            holder.tvFilterItem.setScaleX(1f);
            holder.tvFilterItem.setGravity(Gravity.LEFT);
            holder.ivFilterRightItem.setVisibility(View.GONE);
            holder.ivFilterItem.setVisibility(View.VISIBLE);

        }

        return convertView;
    }

    class ChildViewHolder {
        TextView tvFilterItem;
        ImageView ivFilterItem, ivFilterRightItem;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return mFilterDataList.get(groupPosition).getOptions().size();
//        return categoryCollections.get(categoryList.get(groupPosition)).size();

    }

    @Override
    public int getGroupCount() {
        return mFilterDataList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mFilterDataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mFilterDataList.get(groupPosition).getOptions().get(childPosition);
//        return categoryCollections.get(categoryList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {

        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expand_group_item_filters, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        SortingList.FilterData mFilterData = (SortingList.FilterData) getGroup(groupPosition);
        String groupName = mFilterData.getLabel();
//        String groupName = (String) getGroup(groupPosition);
        holder.tvGroupName.setText(groupName);

        if (mFilterData.isFilterSelected()) {
            holder.tvGroupSelectedItem.setText(mFilterData.getFilterValue());
        } else {
            holder.tvGroupSelectedItem.setText("" + mContext.getString(R.string.all));
        }


        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            if (isExpanded) {
                holder.tvGroupName.setTypeface(Shy7lo.DroidKufiBold);
            } else {
                holder.tvGroupName.setTypeface(Shy7lo.DroidKufiRegular);
            }
            holder.lnrBottom.setScaleX(-1f);
            holder.tvGroupSelectedItem.setScaleX(-1f);
            holder.tvGroupName.setGravity(Gravity.RIGHT);
            holder.tvGroupSelectedItem.setGravity(Gravity.RIGHT);
        } else {
            if (isExpanded) {
                holder.tvGroupName.setTypeface(Shy7lo.RalewayBold);
            } else {
                holder.tvGroupName.setTypeface(Shy7lo.RalewayRegular);
            }
            holder.lnrBottom.setScaleX(1f);
            holder.tvGroupSelectedItem.setScaleX(1f);
            holder.tvGroupName.setGravity(Gravity.LEFT);
            holder.tvGroupSelectedItem.setGravity(Gravity.LEFT);
        }

        if (isExpanded) {
//            holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_up));
            holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.lnrBottom.setVisibility(View.GONE);
        } else {
//            holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_down));
            holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.lnrBottom.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    static class ViewHolder {
        @BindView(R.id.tvGroupName)
        TextView tvGroupName;
        @BindView(R.id.tvGroupSelectedItem)
        TextView tvGroupSelectedItem;
        @BindView(R.id.ibIndicator)
        ImageView ibIndicator;
        @BindView(R.id.lnrBottom)
        LinearLayout lnrBottom;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
