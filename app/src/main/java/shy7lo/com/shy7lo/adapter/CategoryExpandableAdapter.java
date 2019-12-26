package shy7lo.com.shy7lo.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.CategoryList;
import shy7lo.com.shy7lo.model.SubChildData;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.widget.CategoryExpandableListView;

/**
 * Created by JITEN-PC on 13-09-2016.
 */
public class CategoryExpandableAdapter extends CategoryExpandableListView.CategoryExpandableAdapter{

    private Activity mContext;
    private CategoryList.MainCategoryData mCategorData;

    public CategoryExpandableAdapter(Activity mContext, CategoryList.MainCategoryData mCategorData) {
        this.mContext = mContext;
        this.mCategorData = mCategorData;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expand_item_products, null);
            holder = new ChildViewHolder();
            holder.tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }


        final SubChildData mSubChildData = (SubChildData) getChild(groupPosition, childPosition);
        final String filterItem = mSubChildData.getName();

        holder.tvCategory.setText(filterItem);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvCategory.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvCategory.setScaleX(-1f);
        } else {
            holder.tvCategory.setTypeface(Shy7lo.RalewayRegular);
            holder.tvCategory.setScaleX(1f);
        }

        return convertView;
    }

    class ChildViewHolder {
        TextView tvCategory;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return mCategorData.getChildrenData().get(groupPosition).getChildrenData().size();

    }

    @Override
    public int getGroupCount() {
        return mCategorData.getChildrenData().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCategorData.getChildrenData().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCategorData.getChildrenData().get(groupPosition).getChildrenData().get(childPosition);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expand_group_item_products, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        CategoryList.ChildData mGroupData = (CategoryList.ChildData) getGroup(groupPosition);
        String groupName = mGroupData.getName();
        holder.tvGroupName.setText(groupName);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvGroupName.setTypeface(Shy7lo.DroidKufiBold);
            holder.tvGroupName.setScaleX(-1f);
        } else {
            holder.tvGroupName.setTypeface(Shy7lo.RalewayBold);
            holder.tvGroupName.setScaleX(1f);
        }

        if (isExpanded) {
            holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_up));
            holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.sb_selection));
        } else {
            holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_down));
            holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.black));
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
        @BindView(R.id.ibIndicator)
        ImageView ibIndicator;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
