package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.fragment.SubCategoryForProductFragment;
import shy7lo.com.shy7lo.model.CategoryProductList;
import shy7lo.com.shy7lo.pref.MyPref;

/**
 * Created by JITEN-PC on 15-02-2017.
 */

public class SubCategoryProductAdapter extends BaseAdapter {

    Context mContext;
    List<CategoryProductList.CategoryChild> mValueList = new ArrayList<>();
    private SubCategoryForProductFragment subCategoryForProductFragment;

    public SubCategoryProductAdapter(Context mContext, SubCategoryForProductFragment subCategoryForProductFragment) {
        this.mContext = mContext;
        this.subCategoryForProductFragment = subCategoryForProductFragment;
    }

    public void addAll(List<CategoryProductList.CategoryChild> mTempValueList) {
        mValueList.clear();
        mValueList.addAll(mTempValueList);
        notifyDataSetChanged();
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
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_sub_category_product, null);
            holder = new ViewHolder();
            holder.ivCategory = (ImageView) view.findViewById(R.id.ivCategory);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvName.setScaleX(-1f);
            holder.ivCategory.setScaleX(-1f);
            holder.tvName.setGravity(Gravity.RIGHT);
        } else {
            holder.tvName.setScaleX(1f);
            holder.ivCategory.setScaleX(1f);
            holder.tvName.setGravity(Gravity.LEFT);
        }

        final CategoryProductList.CategoryChild value = mValueList.get(position);

        if (value != null) {

            Picasso.with(mContext).load(value.thumb).into(holder.ivCategory);
            holder.tvName.setText(""+value.name);

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String mSearchCriteria = "category_id="+value.id+"&sortby=created_at&direction=DESC&brand=null";

                subCategoryForProductFragment.loadBundleItemWithID(mSearchCriteria);

//                Bundle bundle = new Bundle();
//                bundle.putString(ProductsItemsFragment.BK_PRODUCT_ITEMS, mSearchCriteria);
//                bundle.putString(ProductsItemsFragment.BNDL_CATEGORY, "Women");
//                bundle.putBoolean(ProductsItemsFragment.IS_FROM_BANNER, true);
//
//                HomeActivity activity = (HomeActivity) mContext;
//                if (activity != null) {
//                    activity.loadBannerItems(bundle);
//                }
            }
        });


        return view;
    }

    private class ViewHolder {
        ImageView ivCategory;
        TextView tvName;
    }
}
