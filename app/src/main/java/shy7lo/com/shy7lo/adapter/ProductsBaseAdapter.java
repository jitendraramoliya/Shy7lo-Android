package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.grantland.widget.AutofitTextView;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.model.ProductList;
import shy7lo.com.shy7lo.model.Wishlist;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.sync.WishListSync;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.utils.Constant.BNDL_IS_FROM_SEARCH;
import static shy7lo.com.shy7lo.utils.Constant.BNDL_SKU;

/**
 * Created by JITEN-PC on 20-05-2017.
 */

public class ProductsBaseAdapter extends BaseAdapter {

    private Context mContext;
    private List<ProductList.ProductInfo> prodItemList = new ArrayList<>();
    private int layout;
    GridView gvProductItems;
    private String mCurrencyCode = "";
    private float mExchangeRate;
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int TwentyDp;
    DBAdapter dbAdapter;

    public ProductsBaseAdapter(Context mContext, int layout, List<ProductList.ProductInfo> prodItemList, GridView gvProductItems) {
        this.mContext = mContext;
//        this.prodItemList = prodItemList;
        this.prodItemList.addAll(prodItemList);
        this.layout = layout;
        this.gvProductItems = gvProductItems;
        mCurrencyCode = MyPref.getPref(mContext, MyPref.DEFAULT_CURRENCY_CODE, mContext.getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(mContext, MyPref.DEFAULT_EXCHANGE_RATE, 1f);
        TwentyDp = (int) Utils.pxFromDp(mContext, 20);
        dbAdapter = new DBAdapter(mContext);
    }

    public void changeData(List<ProductList.ProductInfo> prodItemList) {
        LogUtils.e("", "before::" + prodItemList.size());
        if (this.prodItemList != null && this.prodItemList.size() > 0) {
            this.prodItemList.clear();
        }
        LogUtils.e("", "after::" + prodItemList.size());
        this.prodItemList.addAll(prodItemList);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return prodItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return prodItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_products, null);
            ViewHolder holder = new ViewHolder();
            holder.ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
            holder.ivPreloaderProductImage = (ImageView) view.findViewById(R.id.ivPreloaderProductImage);
            holder.rlImageContainer = (RelativeLayout) view.findViewById(R.id.rlImageContainer);
            holder.tvProductName = (TextView) view.findViewById(R.id.tvProductName);
            holder.tvProductCategory = (AutofitTextView) view.findViewById(R.id.tvProductCategory);
            holder.tvProductOldPrice = (TextView) view.findViewById(R.id.tvProductOldPrice);
            holder.tvProductNewPrice = (TextView) view.findViewById(R.id.tvProductNewPrice);
            holder.tvOffer = (TextView) view.findViewById(R.id.tvOffer);
            holder.tvWishlist = (TextView) view.findViewById(R.id.tvWishlist);
//            holder.mProgressBar = (ProgressBar) view.findViewById(R.id.mProgressBar);
            holder.mAVProgressbar = (AVLoadingIndicatorView) view.findViewById(R.id.mAVProgressbar);
            holder.ivTagImage = (ImageView) view.findViewById(R.id.ivTagImage);
            holder.tvTagImage = (TextView) view.findViewById(R.id.tvTagImage);
//            holder.mAutoFitLayout = (AutofitLayout) view.findViewById(R.id.mAutoFitLayout);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        if (gvProductItems.getNumColumns() == 1) {
            holder.rlImageContainer.setPadding(0, 0, 0, 0);
        } else if (gvProductItems.getNumColumns() == 2) {
            if (position % 2 == 0) {
                holder.rlImageContainer.setPadding(10, 0, 0, 0);
            } else {
                holder.rlImageContainer.setPadding(0, 0, 10, 0);
            }
        }

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.ivProductImage.setScaleX(-1f);
            holder.ivPreloaderProductImage.setScaleX(-1f);
            holder.tvProductName.setScaleX(-1f);
            holder.tvProductCategory.setScaleX(-1f);
            holder.tvProductOldPrice.setScaleX(-1f);
            holder.tvProductNewPrice.setScaleX(-1f);
            holder.tvProductCategory.setGravity(Gravity.RIGHT);
            holder.ivTagImage.setScaleX(-1f);
            holder.tvTagImage.setScaleX(-1f);
            holder.tvOffer.setScaleX(-1f);
        } else {
            holder.ivProductImage.setScaleX(1f);
            holder.ivPreloaderProductImage.setScaleX(1f);
            holder.tvProductName.setScaleX(1f);
            holder.tvProductCategory.setScaleX(1f);
            holder.tvProductOldPrice.setScaleX(1f);
            holder.tvProductNewPrice.setScaleX(1f);
            holder.tvProductCategory.setGravity(Gravity.LEFT);
            holder.ivTagImage.setScaleX(1f);
            holder.tvTagImage.setScaleX(1f);
            holder.tvOffer.setScaleX(1f);
        }

        if (gvProductItems.getNumColumns() == 2) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ivProductImage.getLayoutParams();
            params.height = (int) mContext.getResources().getDimension(R.dimen.grid_item_hieght);
            holder.ivProductImage.setLayoutParams(params);
            holder.ivPreloaderProductImage.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ivProductImage.getLayoutParams();
            params.height = (int) mContext.getResources().getDimension(R.dimen.list_item_hieght);
            holder.ivProductImage.setLayoutParams(params);
            holder.ivPreloaderProductImage.setLayoutParams(params);
        }

        final ProductList.ProductInfo productItem = prodItemList.get(position);

        if (dbAdapter.isContainInWishListWithoutSoftDelete(productItem.getSku())) {
            holder.tvWishlist.setBackgroundResource(R.drawable.ic_top_wishlist_selected);
        } else {
            holder.tvWishlist.setBackgroundResource(R.drawable.ic_wishlist_product);
        }

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvProductName.setTypeface(Shy7lo.SFUIDisplayRegular);
            holder.tvProductCategory.setTypeface(Shy7lo.SFUIDisplayRegular);
            holder.tvProductOldPrice.setTypeface(Shy7lo.SFUIDisplayBold);
            holder.tvProductNewPrice.setTypeface(Shy7lo.SFUIDisplayBold);
        } else {
            holder.tvProductName.setTypeface(Shy7lo.SFUIDisplayRegular);
            holder.tvProductCategory.setTypeface(Shy7lo.SFUIDisplayRegular);
            holder.tvProductOldPrice.setTypeface(Shy7lo.SFUIDisplayBold);
            holder.tvProductNewPrice.setTypeface(Shy7lo.SFUIDisplayBold);
        }

        String mProductText = "";

        if (!TextUtils.isEmpty(productItem.getBrand()) && !productItem.getBrand().equalsIgnoreCase("null")) {
//            holder.tvProductName.setText("" + productItem.getBrand());
            mProductText = productItem.getBrand();
        } else {
            mProductText = "";
//            holder.tvProductName.setText("");
        }
        int brandLength = mProductText.length();

        if (mProductText.length() > 0) {
            mProductText = mProductText + " " + productItem.getName();
        } else {
            mProductText = productItem.getName();
        }

        SpannableStringBuilder sbBrand = new SpannableStringBuilder(mProductText);
        sbBrand.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, brandLength, 0);
        holder.tvProductName.setText(sbBrand);
//        holder.tvProductCategory.setText("" + productItem.getName());

        try {

            if (productItem.isTag()) {
                if (productItem.getTagStatus() != null) {
                    if (!TextUtils.isEmpty(productItem.getTagStatus().mode) && productItem.getTagStatus().mode.equalsIgnoreCase("text")) {
                        holder.tvTagImage.setVisibility(View.VISIBLE);
                        holder.ivTagImage.setVisibility(View.INVISIBLE);

                        holder.tvTagImage.setBackgroundColor(Color.parseColor(productItem.getTagStatus().bg_color));
                        holder.tvTagImage.setTextColor(Color.parseColor(productItem.getTagStatus().color));

                        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                            holder.tvTagImage.setText("" + productItem.getTagStatus().ar);
                        } else {
                            holder.tvTagImage.setText("" + productItem.getTagStatus().en);
                        }
                    } else {
                        holder.tvTagImage.setVisibility(View.INVISIBLE);
                        holder.ivTagImage.setVisibility(View.VISIBLE);

                        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                            Picasso.with(mContext).load(productItem.getTagStatus().url_ar).into(holder.ivTagImage);
                        } else {
                            Picasso.with(mContext).load(productItem.getTagStatus().url_en).into(holder.ivTagImage);
                        }
                    }
                } else {
                    holder.ivTagImage.setVisibility(View.INVISIBLE);
                    holder.tvTagImage.setVisibility(View.INVISIBLE);
                }

            } else {
                holder.ivTagImage.setVisibility(View.INVISIBLE);
                holder.tvTagImage.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
                if (TextUtils.isEmpty(userToken)) {
                    Utils.showToast(mContext, "" + mContext.getString(R.string.msg_need_login));
                } else {
                    if (dbAdapter.isContainInWishListWithoutSoftDelete(productItem.getSku())) {

                        if (TextUtils.isEmpty(userToken) && dbAdapter.isGuestWishItem(productItem.getSku())) {
                            dbAdapter.removeWishItem(productItem.getSku());
                        } else {
                            dbAdapter.updateSoftDeleteWishItem(productItem.getSku(), "1");
                        }
                        if (mContext instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) mContext;
                            activity.showWishListMsg(mContext.getResources().getString(R.string.msg_item_removed));
                        }

                        notifyDataSetChanged();

                    } else {

                        Wishlist.WishlistData mWishItem = new Wishlist().new WishlistData();
                        mWishItem.setId(productItem.getEntityId());
                        mWishItem.setName(productItem.getName());
//                    mWishItem.setDescription(productItem.getShortDescription());
                        mWishItem.setDescription("");
                        mWishItem.setSku(productItem.getSku());
                        mWishItem.setProductId(productItem.getEntityId());
                        mWishItem.setTypeId(productItem.getTypeId());
                        mWishItem.setQty(1);
                        if (!TextUtils.isEmpty("" + productItem.getPrice())) {
                            mWishItem.setPrice(productItem.getPrice());
                        }
                        if (!TextUtils.isEmpty("" + productItem.getSpecialPrice())) {
                            mWishItem.setSpecial_price(productItem.getSpecialPrice());
                        }
                        mWishItem.setSpecial_from_date(productItem.getSpecial_from_date());
                        mWishItem.setSpecial_to_date(productItem.getSpecial_to_date());
                        mWishItem.setThumbNail(productItem.getThumbNail());
                        mWishItem.setBrand(productItem.getBrand());
                        mWishItem.setRating(0);
                        mWishItem.setStockQty(1);
                        mWishItem.setStockStatus("1");


                        if (!TextUtils.isEmpty(userToken)) {
                            mWishItem.setIsGuest("0");
                            mWishItem.setToken(userToken);

                        } else {
                            mWishItem.setIsGuest("1");
                            String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
                            mWishItem.setToken(guestToken);
                        }

                        if (Utils.isInternetConnected(mContext)) {
                            mWishItem.setIs_on_server("0");
                        } else {
                            mWishItem.setIs_on_server("0");
                        }

                        mWishItem.setSoft_delete("0");

                        dbAdapter.addWishItem(mWishItem);

                        if (productItem.getTypeId().equalsIgnoreCase("configurable")) {
                            WishListSync mWishListSync = new WishListSync(mContext);
                            mWishListSync.syncWishList(mContext, dbAdapter);
                        }

                        if (mContext instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) mContext;
                            activity.showWishListMsg(mContext.getResources().getString(R.string.msg_item_saved));
                        }
                        notifyDataSetChanged();
                    }

                }

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof HomeActivity) {

                    Bundle b = new Bundle();
                    b.putString(BNDL_SKU, "" + productItem.getSku());
                    b.putBoolean(BNDL_IS_FROM_SEARCH, false);

                    HomeActivity activity = (HomeActivity) mContext;
                    activity.loadProductDetails(b);
                }
            }
        });


//        LogUtils.e("", position + " loader show");
//        holder.mAVProgressbar.show();
//        holder.mAVProgressbar.smoothToShow();
//        holder.mProgressBar.setVisibility(View.VISIBLE);
        holder.ivPreloaderProductImage.setVisibility(View.VISIBLE);
        Glide.with(mContext)
                .load(productItem.getThumbNail())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.ivPreloaderProductImage.setVisibility(View.GONE);
//                        holder.mProgressBar.setVisibility(View.GONE);
//                        LogUtils.e("", position + " onLoadFailed loader hide");
//                        holder.mAVProgressbar.hide();
//                        holder.mAVProgressbar.smoothToHide();
//                        holder.mAVProgressbar.show();
//                        holder.mAVProgressbar.smoothToShow();

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.ivPreloaderProductImage.setVisibility(View.GONE);
//                        holder.mProgressBar.setVisibility(View.GONE);
//                        LogUtils.e("", position + " onResourceReady loader hide");
//                        holder.mAVProgressbar.hide();
//                        holder.mAVProgressbar.smoothToHide();
                        return false;
                    }
                })
                .into(holder.ivProductImage);

        holder.tvProductNewPrice.setVisibility(View.GONE);
        holder.tvProductOldPrice.setPaintFlags(0);
        holder.tvProductOldPrice.setTextColor(mContext.getResources().getColor(R.color.black));
        holder.tvOffer.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(String.valueOf(productItem.getPrice()))) {
//            holder.tvProductOldPrice.setText(Utils.getIntPrice(mExchangeRate, productItem.getPrice()) + " " + mCurrencyCode);
            SpannableStringBuilder sb = new SpannableStringBuilder(Utils.getIntPrice(mExchangeRate, productItem.getPrice()) + " " + mCurrencyCode);
//            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, productItem.getPrice()).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            } else {
//                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, productItem.getPrice()).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            }
            holder.tvProductOldPrice.setText(sb);
        }

        try {
            if (productItem.getSpecialPrice() > 0) {

                if (!TextUtils.isEmpty(productItem.getSpecial_from_date()) && !TextUtils.isEmpty(productItem.getSpecial_to_date())) {

                    Date fromDate = sdfDate.parse(productItem.getSpecial_from_date());
                    Date toDate = sdfDate.parse(productItem.getSpecial_to_date());
                    Date currentDate = new Date();

                    if (currentDate.after(fromDate) && currentDate.before(toDate)) {

//            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                holder.tvProductNewPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, productItem.getSpecialPrice()));
//            } else {
//                        holder.tvProductNewPrice.setText(Utils.getIntPrice(mExchangeRate, productItem.getSpecialPrice()) + " " + mCurrencyCode);
////            }
//                        holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                        holder.tvProductNewPrice.setVisibility(View.VISIBLE);

                        SpannableStringBuilder sb = new SpannableStringBuilder(Utils.getIntPrice(mExchangeRate, productItem.getSpecialPrice()) + " " + mCurrencyCode);
//                        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, productItem.getSpecialPrice()).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                        } else {
//                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, productItem.getSpecialPrice()).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                        }
                        holder.tvProductNewPrice.setText(sb);
                        holder.tvProductNewPrice.setVisibility(View.VISIBLE);
                        if (productItem.getPercentOff() > 0) {
                            holder.tvOffer.setVisibility(View.VISIBLE);
                            holder.tvOffer.setText((int) productItem.getPercentOff() + "% "+mContext.getString(R.string.off));
                        }


//                        if (!TextUtils.isEmpty(String.valueOf(productItem.getPrice()))) {
//                            holder.tvProductOldPrice.setText(Utils.getIntPrice(mExchangeRate, productItem.getPrice()) + "");
//                        }
//                        holder.tvProductOldPrice.setTextColor(mContext.getResources().getColor(R.color.gray_66));
                        holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }

                }

            } else {
                holder.tvProductNewPrice.setVisibility(View.GONE);
//                holder.mAutoFitLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            holder.tvProductOldPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, productItem.getPrice()));
//        } else {
//        holder.tvProductOldPrice.setText(Utils.getIntPrice(mExchangeRate, productItem.getPrice()) + " " + mCurrencyCode);
//        }

        return view;
    }

    private class ViewHolder {
        public ImageView ivProductImage, ivPreloaderProductImage, ivTagImage;
        public RelativeLayout rlImageContainer;
        public TextView tvProductName, tvOffer, tvProductOldPrice, tvProductNewPrice, tvWishlist, tvTagImage;
        private AutofitTextView tvProductCategory;
        //        public AutofitLayout mAutoFitLayout;
        //        private ProgressBar mProgressBar;
        private AVLoadingIndicatorView mAVProgressbar;
    }
}
