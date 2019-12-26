package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import shy7lo.com.shy7lo.model.SimilarProducts;
import shy7lo.com.shy7lo.model.Wishlist;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 09-04-2018.
 */

public class SimilarProductAdapter extends RecyclerView.Adapter<SimilarProductAdapter.ProductViewHolder> {

    Context mContext;
    List<SimilarProducts.ProductListing> mSimilarProductListing = new ArrayList<>();
    DBAdapter dbAdapter;
    private OnSimilarItemClickListener onSimilarItemClickListener;
    private String mCurrencyCode = "";
    private float mExchangeRate;
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //    public SimilarProductAdapter(Context mContext, List<SimilarProducts.ProductListing> mOrderListing) {
    public SimilarProductAdapter(Context mContext, OnSimilarItemClickListener onSimilarItemClickListener) {
        this.mContext = mContext;
        this.onSimilarItemClickListener = onSimilarItemClickListener;
//        this.mOrderListing = mOrderListing;
        dbAdapter = new DBAdapter(mContext);
        mCurrencyCode = MyPref.getPref(mContext, MyPref.DEFAULT_CURRENCY_CODE, mContext.getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(mContext, MyPref.DEFAULT_EXCHANGE_RATE, 1f);
    }

    public interface OnSimilarItemClickListener {
        void onItemClickListener(int position, SimilarProducts.ProductListing mProductItem);
    }

    public void addAll(List<SimilarProducts.ProductListing> mTempSimilarProductListing) {
        if (mSimilarProductListing != null && mSimilarProductListing.size() > 0) {
            mSimilarProductListing.clear();
        }
        mSimilarProductListing.addAll(mTempSimilarProductListing);
        notifyDataSetChanged();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_item_products, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        LogUtils.e("", "onBindViewHolder::" + position);

        holder.rlImageContainer.setPadding(0, 0, 0, 0);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.ivProductImage.setScaleX(-1f);
            holder.ivPreloaderProductImage.setScaleX(-1f);
            holder.tvProductName.setScaleX(-1f);
            holder.tvProductCategory.setScaleX(-1f);
            holder.tvProductOldPrice.setScaleX(-1f);
            holder.tvProductNewPrice.setScaleX(-1f);
            holder.tvProductCategory.setGravity(Gravity.RIGHT);
        } else {
            holder.ivProductImage.setScaleX(1f);
            holder.ivPreloaderProductImage.setScaleX(1f);
            holder.tvProductName.setScaleX(1f);
            holder.tvProductCategory.setScaleX(1f);
            holder.tvProductOldPrice.setScaleX(1f);
            holder.tvProductNewPrice.setScaleX(1f);
            holder.tvProductCategory.setGravity(Gravity.LEFT);
        }

        final SimilarProducts.ProductListing productItem = mSimilarProductListing.get(position);

        if (dbAdapter.isContainInWishList(productItem.sku)) {
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

        if (!TextUtils.isEmpty(productItem.brand) && !productItem.brand.equalsIgnoreCase("null")) {
//            holder.tvProductName.setText("" + productItem.getBrand());
            mProductText = productItem.brand;
        } else {
            mProductText = "";
//            holder.tvProductName.setText("");
        }
        int brandLength = mProductText.length();

        if (mProductText.length() > 0) {
            mProductText = mProductText + " " + productItem.name;
        } else {
            mProductText = productItem.name;
        }

        SpannableStringBuilder sbBrand = new SpannableStringBuilder(mProductText);
        sbBrand.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, brandLength, 0);
        holder.tvProductName.setText(sbBrand);
//        holder.tvProductCategory.setText("" + productItem.getName());

        holder.tvWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");

                if (dbAdapter.isContainInWishList(productItem.sku)) {

                    if (TextUtils.isEmpty(userToken) && dbAdapter.isGuestWishItem(productItem.sku)) {
                        dbAdapter.removeWishItem(productItem.sku);
                    } else {
                        dbAdapter.updateSoftDeleteWishItem(productItem.sku, "1");
                    }
                    if (mContext instanceof HomeActivity) {
                        HomeActivity activity = (HomeActivity) mContext;
                        activity.showWishListMsg(mContext.getResources().getString(R.string.msg_item_removed));
                    }
                } else {

                    Wishlist.WishlistData mWishItem = new Wishlist().new WishlistData();
                    mWishItem.setId(productItem.entityId);
                    mWishItem.setName(productItem.name);
//                    mWishItem.setDescription(productItem.name);
                    mWishItem.setDescription("");
                    mWishItem.setSku(productItem.sku);
                    mWishItem.setProductId(productItem.entityId);
                    mWishItem.setTypeId(productItem.typeId);
                    mWishItem.setQty(1);
                    if (!TextUtils.isEmpty("" + productItem.price)) {
                        mWishItem.setPrice(productItem.price);
                    }
                    if (!TextUtils.isEmpty("" + productItem.specialPrice)) {
                        mWishItem.setSpecial_price(productItem.specialPrice);
                    }
                    mWishItem.setSpecial_from_date(productItem.specialFromDate);
                    mWishItem.setSpecial_to_date(productItem.specialToDate);
                    mWishItem.setThumbNail(productItem.image);
                    mWishItem.setBrand(productItem.brand);
                    mWishItem.setRating(0);
                    mWishItem.setStockStatus("1");
                    mWishItem.setStockQty(1);


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

                    if (mContext instanceof HomeActivity) {
                        HomeActivity activity = (HomeActivity) mContext;
                        activity.showWishListMsg(mContext.getResources().getString(R.string.msg_item_saved));
                    }
                }
                notifyDataSetChanged();

            }
        });

        holder.lnrMainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof HomeActivity) {

                    onSimilarItemClickListener.onItemClickListener(position, productItem);

//                    Bundle b = new Bundle();
//                    b.putString(BNDL_SKU, "" + productItem.getSku());
//                    b.putBoolean(BNDL_IS_FROM_SEARCH, false);
//
//                    HomeActivity activity = (HomeActivity) mContext;
//                    activity.loadProductDetails(b);
                }
            }
        });


//        LogUtils.e("", position + " loader show");
//        holder.mAVProgressbar.show();
//        holder.mAVProgressbar.smoothToShow();
//        holder.mProgressBar.setVisibility(View.VISIBLE);
        holder.ivPreloaderProductImage.setVisibility(View.VISIBLE);
        Glide.with(mContext)
                .load(productItem.image)
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

        if (!TextUtils.isEmpty(String.valueOf(productItem.price))) {
//            holder.tvProductOldPrice.setText(Utils.getIntPrice(mExchangeRate, productItem.price) + " " + mCurrencyCode);
            SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode+" "+Utils.getIntPrice(mExchangeRate, productItem.price) );
//            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, productItem.price).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            } else {
//                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, productItem.price).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            }
            holder.tvProductOldPrice.setText(sb);
        }

        try {
            if (productItem.specialPrice > 0) {

                if (!TextUtils.isEmpty(productItem.specialFromDate) && !TextUtils.isEmpty(productItem.specialToDate)) {

                    Date fromDate = sdfDate.parse(productItem.specialFromDate);
                    Date toDate = sdfDate.parse(productItem.specialToDate);
                    Date currentDate = new Date();

                    if (currentDate.after(fromDate) && currentDate.before(toDate)) {

//            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                holder.tvProductNewPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, productItem.specialPrice));
//            } else {
//                        holder.tvProductNewPrice.setText(Utils.getIntPrice(mExchangeRate, productItem.specialPrice) + " " + mCurrencyCode);
////            }
//                        holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                        holder.tvProductNewPrice.setVisibility(View.VISIBLE);

                        SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode+" "+Utils.getIntPrice(mExchangeRate, productItem.specialPrice));
//                        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, productItem.specialPrice).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                        } else {
//                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, productItem.specialPrice).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                        }
                        holder.tvProductNewPrice.setText(sb);
                        holder.tvProductNewPrice.setVisibility(View.VISIBLE);

//                        if (!TextUtils.isEmpty(String.valueOf(productItem.price))) {
//                            holder.tvProductOldPrice.setText(Utils.getIntPrice(mExchangeRate, productItem.price) + "");
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

    }

    @Override
    public int getItemCount() {
        LogUtils.e("", "mOrderListing::" + mSimilarProductListing.size());
        return mSimilarProductListing.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProductImage, ivPreloaderProductImage;
        public RelativeLayout rlImageContainer;
        public LinearLayout lnrMainContainer;
        public TextView tvProductName, tvProductOldPrice, tvProductNewPrice, tvWishlist;
        private AutofitTextView tvProductCategory;
        private AVLoadingIndicatorView mAVProgressbar;

        public ProductViewHolder(View view) {
            super(view);
            ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
            ivPreloaderProductImage = (ImageView) view.findViewById(R.id.ivPreloaderProductImage);
            rlImageContainer = (RelativeLayout) view.findViewById(R.id.rlImageContainer);
            lnrMainContainer = (LinearLayout) view.findViewById(R.id.lnrMainContainer);
            tvProductName = (TextView) view.findViewById(R.id.tvProductName);
            tvProductCategory = (AutofitTextView) view.findViewById(R.id.tvProductCategory);
            tvProductOldPrice = (TextView) view.findViewById(R.id.tvProductOldPrice);
            tvProductNewPrice = (TextView) view.findViewById(R.id.tvProductNewPrice);
            tvWishlist = (TextView) view.findViewById(R.id.tvWishlist);
            mAVProgressbar = (AVLoadingIndicatorView) view.findViewById(R.id.mAVProgressbar);
        }
    }

}
