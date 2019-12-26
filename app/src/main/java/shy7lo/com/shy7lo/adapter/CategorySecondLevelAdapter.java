package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class CategorySecondLevelAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private SubCategoryList.CategoryData mChildData;
    private CustExpListview mCustExpListview;
    private CategoryLevel.OnOptionClickListener onOptionClickListener;
    int mLastExpandedPosition = -1;

    public CategorySecondLevelAdapter(Context mContext, SubCategoryList.CategoryData mChildData, CategoryLevel.OnOptionClickListener onOptionClickListener) {
        this.mContext = mContext;
        this.mChildData = mChildData;
        this.onOptionClickListener = onOptionClickListener;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildData.childrenDataInner.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expand_item_products, null);
            holder = new ChildViewHolder();
            holder.tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
            holder.ibIndicator = (ImageView) convertView.findViewById(R.id.ibIndicator);
            holder.rlMain = (RelativeLayout) convertView.findViewById(R.id.rlMain);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }


        final SubCategoryList.CategoryDataInner mSubChildData = (SubCategoryList.CategoryDataInner) getChild(groupPosition, childPosition);
        final String filterItem = mSubChildData.name;

        holder.tvCategory.setText(filterItem);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvCategory.setTypeface(Shy7lo.DroidKufiRegular);
            holder.rlMain.setScaleX(-1f);
            holder.tvCategory.setScaleX(-1f);
            holder.tvCategory.setGravity(Gravity.RIGHT);
        } else {
            holder.tvCategory.setTypeface(Shy7lo.RalewayRegular);
            holder.rlMain.setScaleX(1f);
            holder.tvCategory.setScaleX(1f);
            holder.tvCategory.setGravity(Gravity.LEFT);
        }

        if (mSubChildData.isSelected()) {
            holder.ibIndicator.setVisibility(View.VISIBLE);
            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                holder.ibIndicator.setScaleX(-1f);
            } else {
                holder.ibIndicator.setScaleX(1f);
            }
            holder.tvCategory.setTextColor(mContext.getResources().getColor(R.color.sb_selection));
        } else {
            holder.ibIndicator.setVisibility(View.INVISIBLE);
            holder.tvCategory.setTextColor(mContext.getResources().getColor(R.color.gray_99));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!mSubChildData.isSelected()) {
                    onOptionClickListener.onOptionClicked(3, mSubChildData.id);
//                }
            }
        });

        return convertView;
    }

    public void setListView(CustExpListview mCustExpListview) {
        this.mCustExpListview = mCustExpListview;
    }

    class ChildViewHolder {
        TextView tvCategory;
        ImageView ibIndicator;
        RelativeLayout rlMain;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mChildData.childrenDataInner == null) {
            return 0;
        }
        return mChildData.childrenDataInner.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mChildData;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expand_group_item_sub_products, null, false);
            ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        final SubCategoryList.CategoryData mGroupData = (SubCategoryList.CategoryData) getGroup(groupPosition);
        String groupName = mGroupData.name;
        holder.tvGroupName.setText(groupName);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvGroupName.setTypeface(Shy7lo.DroidKufiRegular);
            holder.lnrMain.setScaleX(-1f);
            holder.tvGroupName.setScaleX(-1f);
        } else {
            holder.tvGroupName.setTypeface(Shy7lo.RalewayRegular);
            holder.lnrMain.setScaleX(1f);
            holder.tvGroupName.setScaleX(1f);
        }

//        if (isExpanded) {
        if (mGroupData.isSelected()) {
            if (mGroupData.childrenDataInner == null || mGroupData.childrenDataInner.size() == 0) {
                holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_done_big));
                holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.sb_selection));
                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    holder.ibIndicator.setScaleX(-1f);
                } else {
                    holder.ibIndicator.setScaleX(1f);
                }
                if (mCustExpListview != null) {
                    mCustExpListview.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                }
                holder.mLine.setVisibility(View.VISIBLE);
            } else {
//                holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_up_black_big));
                if (isExpanded) {
                    holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_down_big));
                } else {
                    holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_right_black_big));
                }
                holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.black));
                if (mCustExpListview != null) {
//                    mCustExpListview.setBackgroundColor(mContext.getResources().getColor(R.color.gray_f1));
                    mCustExpListview.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                }
                holder.mLine.setVisibility(View.VISIBLE);
//                holder.mLine.setVisibility(View.INVISIBLE);
            }

            holder.ibIndicator.setVisibility(View.VISIBLE);

        } else {
            holder.mLine.setVisibility(View.VISIBLE);
            if (mGroupData.childrenDataInner == null || mGroupData.childrenDataInner.size() == 0) {
                holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_right_black_big));
                holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.black));
                holder.ibIndicator.setVisibility(View.INVISIBLE);
            } else {
                holder.ibIndicator.setVisibility(View.VISIBLE);
                if (isExpanded) {
                    holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_down_big));
                } else {
                    holder.ibIndicator.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_right_black_big));
                }
                holder.tvGroupName.setTextColor(mContext.getResources().getColor(R.color.black));
            }
            if (mCustExpListview != null) {
                mCustExpListview.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
            }
        }

//        boolean isSelected = false;
//        if (mGroupData.getChildrenData() != null && mGroupData.getChildrenData().size() > 0) {
//            for (int i = 0; i < mGroupData.getChildrenData().size(); i++) {
//                SubChildData.ChildData mChildData = mGroupData.getChildrenData().get(i);
//                if (mChildData.isSelected()) {
//                    isSelected = true;
//                    break;
//                }
//
//            }
//        }
//
//        if (isSelected) {
//            CustExpListview mExpandableListView = (CustExpListview) parent;
//            mExpandableListView.expandGroup(groupPosition);
//        }


//        CustExpListview mExpandableListView = (CustExpListview) parent;
//        mExpandableListView.expandGroup(groupPosition);

//        if (mGroupData.getChildrenData() != null && mGroupData.getChildrenData().size() > 0) {
//            boolean isSelected = false;
//            for (int i = 0; i < mGroupData.getChildrenData().size(); i++) {
//                if (mGroupData.getChildrenData().get(i).isSelected()) {
//                    isSelected = true;
//                    break;
//                }
//            }
//            LogUtils.e("", "" + mGroupData.getId() + " isSelected::" + isSelected);
//            if (isSelected) {
//                CustExpListview mExpandableListView = (CustExpListview) parent;
//                mExpandableListView.expandGroup(groupPosition);
////                mCustExpListview.expandGroup(groupPosition);
//                mLastExpandedPosition = groupPosition;
//            }
//        }

        mCustExpListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (mGroupData.childrenDataInner == null || mGroupData.childrenDataInner.size() == 0) {
                    onOptionClickListener.onOptionClicked(2, mGroupData.id);
                }

                if (mCustExpListview.isGroupExpanded(groupPosition)) {
                    mCustExpListview.collapseGroup(groupPosition);
                } else {
                    mCustExpListview.expandGroup(groupPosition);
                    if (mLastExpandedPosition != -1
                            && groupPosition != mLastExpandedPosition) {
                        mCustExpListview.collapseGroup(mLastExpandedPosition);
                    }
                    mLastExpandedPosition = groupPosition;
                }

                return true;
            }
        });

//        mCustExpListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//
//                final SubChildData.ChildData mSubChildData = (SubChildData.ChildData) getChild(groupPosition, childPosition);
//                if (!mSubChildData.isSelected()) {
//                    onOptionClickListener.onOptionClicked(3, mSubChildData.getId());
////                    notifyDataSetChanged();
//                }
//
//                return true;
//            }
//        });

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

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
        @BindView(R.id.lnrMain)
        LinearLayout lnrMain;
        @BindView(R.id.mLine)
        View mLine;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
