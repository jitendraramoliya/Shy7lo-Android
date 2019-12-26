package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.SubCategoryList;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.widget.CustExpListview;

/**
 * Created by Jiten on 06-09-2017.
 */

public class CategoryLevel extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<SubCategoryList> mCategorData;
    private ExpandableListView eListView;
    int mLastExpandedPosition = -1;
    private OnOptionClickListener onOptionClickListener;
    ArrayList<SecondLevelAdapter> mSubChildeAdapterList = new ArrayList<>();

    public interface OnOptionClickListener {
        public void onOptionClicked(int level, int id);
    }

    public CategoryLevel(Context mContext, ArrayList<SubCategoryList> mCategorData, ExpandableListView eListView, OnOptionClickListener onOptionClickListener) {
        this.mContext = mContext;
        this.mCategorData = mCategorData;
        this.eListView = eListView;
        this.onOptionClickListener = onOptionClickListener;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCategorData.get(groupPosition).childrenData.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {

        CategorySecondLevelAdapter mSecondLevelAdapter = new CategorySecondLevelAdapter(mContext, mCategorData.get(groupPosition).childrenData.get(childPosition), onOptionClickListener);

        int totalHeight = 0;
        View mGroupView = LayoutInflater.from(mContext).inflate(R.layout.expand_group_item_sub_products, null, false);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.expand_item_products, null);
        boolean isSelected = false;
        if (mView != null && mSecondLevelAdapter != null) {
            for (int i = 0; i < mSecondLevelAdapter.getChildrenCount(1); i++) {
                mView.measure(0, 0);
                totalHeight += mView.getMeasuredHeight();
                SubCategoryList.CategoryDataInner mSubChildData = (SubCategoryList.CategoryDataInner) mSecondLevelAdapter.getChild(1, i);
                if (mSubChildData != null && mSubChildData.isSelected() && !isSelected) {
                    isSelected = true;
                }
            }
        }
        mGroupView.measure(0, 0);
        totalHeight += mGroupView.getMeasuredHeight();

//        final CustExpListview mCustExpListview = new CustExpListview(mContext, totalHeight, isSelected);
        final CustExpListview mCustExpListview = new CustExpListview(mContext, totalHeight);
//        final ExpandableHeightListView mCustExpListview = new ExpandableHeightListView(mContext);
        mSecondLevelAdapter.setListView(mCustExpListview);
        mCustExpListview.setAdapter(mSecondLevelAdapter);
//        mCustExpListview.expandGroup(0);
        mCustExpListview.setDividerHeight(0);
        mCustExpListview.setDivider(null);
        mCustExpListview.setGroupIndicator(null);
//        mCustExpListview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                LogUtils.e("", "onGroupExpand groupPosition::" + groupPosition);
//            }
//        });
//
//        mCustExpListview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                LogUtils.e("", "onGroupCollapse groupPosition::" + groupPosition);
//            }
//        });

//        for (int i = 0; i < mCategorData.getChildrenData().get(groupPosition).getChildrenData().size(); i++) {
//            SubChildData mGroupData = (SubChildData) mCategorData.getChildrenData().get(groupPosition).getChildrenData().get(i);
//            if (mGroupData.getChildrenData() != null && mGroupData.getChildrenData().size() > 0) {
//                boolean isSelected = false;
//                for (int j = 0; i < mGroupData.getChildrenData().size(); i++) {
//                    if (mGroupData.getChildrenData().get(j).isSelected()) {
//                        isSelected = true;
//                        break;
//                    }
//                }
//                LogUtils.e("", "" + mGroupData.getId() + " isSelected::" + isSelected);
        if (isSelected) {
            mCustExpListview.expandGroup(0);
////            mCustExpListview.measure(0, 0);
//            ((Activity)mContext).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mCustExpListview.expandGroup(1, false);
//                }
//            });
//
        }
//            }

//        }


        return mCustExpListview;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mCategorData.get(groupPosition) == null){
            return 0;
        }

        if (mCategorData.get(groupPosition).childrenData == null){
            return 0;
        }

        return mCategorData.get(groupPosition).childrenData.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCategorData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mCategorData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expand_group_item_products, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        final SubCategoryList mGroupData = (SubCategoryList) getGroup(groupPosition);
        String groupName = mGroupData.name;
        holder.tvGroupName.setText(groupName);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvGroupName.setTypeface(Shy7lo.DroidKufiBold);
            holder.rlMain.setScaleX(-1f);
            holder.tvGroupName.setScaleX(-1f);
        } else {
            holder.tvGroupName.setTypeface(Shy7lo.RalewayBold);
            holder.rlMain.setScaleX(1f);
            holder.tvGroupName.setScaleX(1f);
        }

//        if (isExpanded) {
        if (mGroupData.isSelected()) {
            if (mGroupData.childrenData == null || mGroupData.childrenData.size() == 0) {
                holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_done_big));
                holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.sb_selection));
                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    holder.ibIndicator.setScaleX(-1f);
                } else {
                    holder.ibIndicator.setScaleX(1f);
                }
            } else {
                holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_up_black_big));
                holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.black));
            }

        } else {
            if (mGroupData.childrenData == null || mGroupData.childrenData.size() == 0) {
                holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_right_black_big));
                holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.black));
            } else {
                if (isExpanded) {
                    holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_down_big));
                } else {
                    holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_right_black_big));
                }
                holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.black));
            }

        }

        boolean isSelected = false;
        if (mGroupData.childrenData != null && mGroupData.childrenData.size() > 0) {
            for (int i = 0; i < mGroupData.childrenData.size(); i++) {
                SubCategoryList.CategoryData mSubChildData = mGroupData.childrenData.get(i);
                if (mSubChildData.isSelected()) {
                    isSelected = true;
                    break;
                }
                if (mSubChildData.childrenDataInner != null && mSubChildData.childrenDataInner.size() > 0) {
                    for (int j = 0; j < mSubChildData.childrenDataInner.size(); j++) {
                        SubCategoryList.CategoryDataInner mChildData = mSubChildData.childrenDataInner.get(j);
                        if (mChildData.isSelected()) {
                            isSelected = true;
                            break;
                        }
                    }
                }

            }
        }
        if (isSelected) {
            eListView.expandGroup(groupPosition);
            mLastExpandedPosition = groupPosition;
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGroupData.childrenData == null || mGroupData.childrenData.size() == 0) {
                    onOptionClickListener.onOptionClicked(1, mGroupData.id);
//                    mGroupData.setSelected(true);
                }

//                if (mGroupData.isSelected()) {
//                    eListView.expandGroup(groupPosition);
//                }else{
//                    eListView.expandGroup(groupPosition);
//                }

                if (eListView.isGroupExpanded(groupPosition)) {
                    eListView.collapseGroup(groupPosition);
                } else {
                    eListView.expandGroup(groupPosition);
                    if (mLastExpandedPosition != -1
                            && groupPosition != mLastExpandedPosition) {
                        eListView.collapseGroup(mLastExpandedPosition);
                    }
                    mLastExpandedPosition = groupPosition;
                }

//                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        @BindView(R.id.tvGroupName)
        TextView tvGroupName;
        @BindView(R.id.ibIndicator)
        ImageView ibIndicator;
        @BindView(R.id.rlMain)
        RelativeLayout rlMain;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
