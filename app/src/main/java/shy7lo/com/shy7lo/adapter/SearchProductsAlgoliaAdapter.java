package shy7lo.com.shy7lo.adapter;

import android.content.Context;
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
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.model.SearchAlgoriaProductList;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.utils.Constant.BNDL_IS_FROM_SEARCH;
import static shy7lo.com.shy7lo.utils.Constant.BNDL_SKU;

/**
 * Created by JITEN-PC on 20-05-2017.
 */

public class SearchProductsAlgoliaAdapter extends BaseAdapter {

    private Context mContext;
    private List<SearchAlgoriaProductList.Hit> prodItemList = new ArrayList<>();
    private int layout;
    GridView gvProductItems;
    private String mCurrencyCode = "";
    private float mExchangeRate;
    DBAdapter dbAdapter;


    public SearchProductsAlgoliaAdapter(Context mContext, int layout, List<SearchAlgoriaProductList.Hit> prodItemList, GridView gvProductItems) {
        this.mContext = mContext;
//        this.prodItemList = prodItemList;
        this.prodItemList.addAll(prodItemList);
        this.layout = layout;
        this.gvProductItems = gvProductItems;
        mCurrencyCode = MyPref.getPref(mContext, MyPref.DEFAULT_CURRENCY_CODE, mContext.getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(mContext, MyPref.DEFAULT_EXCHANGE_RATE, 1f);
        dbAdapter = new DBAdapter(mContext);
    }

    public void changeData(List<SearchAlgoriaProductList.Hit> prodItemList) {
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
    public View getView(int position, View view, ViewGroup viewGroup) {

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
            holder.tvWishlist = (TextView) view.findViewById(R.id.tvWishlist);
            holder.tvOffer = (TextView) view.findViewById(R.id.tvOffer);
            holder.mAVProgressbar = (AVLoadingIndicatorView) view.findViewById(R.id.mAVProgressbar);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        holder.tvOffer.setVisibility(View.GONE);

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

        if (gvProductItems.getNumColumns() == 1) {
            holder.rlImageContainer.setPadding(0, 0, 0, 0);
        } else if (gvProductItems.getNumColumns() == 2) {
            if (position % 2 == 0) {
                holder.rlImageContainer.setPadding(10, 0, 0, 0);
            } else {
                holder.rlImageContainer.setPadding(0, 0, 10, 0);
            }
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

        try {

            final SearchAlgoriaProductList.Hit productItem = prodItemList.get(position);

            if (productItem != null) {
//        holder.tvProductName.setText("" + productItem.getBrand());
//                holder.tvProductCategory.setText("" + productItem.name);
//                holder.tvProductName.setText("" + productItem.brand);
                String mProductText = "";
                if (!TextUtils.isEmpty(productItem.brand) && !productItem.brand.equalsIgnoreCase("null")) {
                    mProductText = productItem.brand;
                } else {
                    mProductText = "";
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

//                if (dbAdapter.isContainInWishList(productItem.sku)) {
//                    holder.tvWishlist.setBackgroundResource(R.drawable.ic_top_wishlist_selected);
//                } else {
//                    holder.tvWishlist.setBackgroundResource(R.drawable.ic_wishlist_product);
//                }

                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    holder.tvProductName.setTypeface(Shy7lo.DroidKufiRegular);
                    holder.tvProductCategory.setTypeface(Shy7lo.DroidKufiRegular);
                    holder.tvProductOldPrice.setTypeface(Shy7lo.DroidKufiRegular);
                    holder.tvProductNewPrice.setTypeface(Shy7lo.DroidKufiRegular);
                    holder.tvProductCategory.setGravity(Gravity.RIGHT);
                } else {
                    holder.tvProductName.setTypeface(Shy7lo.DroidKufiRegular);
                    holder.tvProductCategory.setTypeface(Shy7lo.RalewayRegular);
                    holder.tvProductOldPrice.setTypeface(Shy7lo.RalewayRegular);
                    holder.tvProductNewPrice.setTypeface(Shy7lo.RalewayRegular);
                    holder.tvProductCategory.setGravity(Gravity.LEFT);
                }


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mContext instanceof HomeActivity) {

                            Bundle b = new Bundle();
                            b.putString(BNDL_SKU, "" + productItem.objectID);
                            b.putBoolean(BNDL_IS_FROM_SEARCH, true);

                            HomeActivity activity = (HomeActivity) mContext;
                            activity.loadProductDetails(b);
                        }
                    }
                });


//                        holder.mProgressBar.setVisibility(View.VISIBLE);
//                holder.mAVProgressbar.smoothToShow();
                holder.ivPreloaderProductImage.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(productItem.imageUrl)
                        .apply(new RequestOptions().placeholder(R.drawable.bg_preloader).error(R.drawable.bg_preloader))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                        holder.mProgressBar.setVisibility(View.GONE);
//                                holder.mAVProgressbar.smoothToHide();
                                holder.ivPreloaderProductImage.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                        holder.mProgressBar.setVisibility(View.GONE);
//                                holder.mAVProgressbar.smoothToHide();
                                holder.ivPreloaderProductImage.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.ivProductImage);

                // for special price
                holder.tvProductNewPrice.setVisibility(View.GONE);
                holder.tvProductOldPrice.setPaintFlags(0);
                holder.tvProductOldPrice.setTextColor(mContext.getResources().getColor(R.color.black));

//        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////            holder.tvProductOldPrice.setText(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, productItem.price.sAR.origionalPrice));
//            SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, productItem.price.sAR.origionalPrice));
//            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), mCurrencyCode.length(), mCurrencyCode.length()+1+Utils.getIntPrice(mExchangeRate, productItem.price.sAR.origionalPrice).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            } else {
//                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mCurrencyCode.length(), mCurrencyCode.length()+1+Utils.getIntPrice(mExchangeRate, productItem.price.sAR.origionalPrice).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            }
//            holder.tvProductOldPrice.setText(sb);
//        } else {

                if (!TextUtils.isEmpty("" + productItem.price.sAR.origionalPrice)) {
                    SpannableStringBuilder sb = new SpannableStringBuilder(Utils.getIntPrice(mExchangeRate, productItem.price.sAR.origionalPrice) + " " + mCurrencyCode);
//                    if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                        sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, productItem.price.sAR.origionalPrice).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                    } else {
//                        sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, productItem.price.sAR.origionalPrice).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                    }
                    holder.tvProductOldPrice.setText(sb);
//            holder.tvProductOldPrice.setText(Utils.getIntPrice(mExchangeRate, productItem.price.sAR.origionalPrice) + " " + mCurrencyCode);
                }
//        }

                LogUtils.e("", "specialPrice::" + productItem.price.sAR.specialPrice);
                if (!TextUtils.isEmpty("" + productItem.price.sAR.specialPrice) && !TextUtils.equals("" + productItem.price.sAR.specialPrice, "null")  && productItem.price.sAR.specialPrice > 0) {
//            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////                holder.tvProductNewPrice.setText(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, Float.parseFloat(productItem.price.sAR.specialPrice)));
//                SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, Float.parseFloat(productItem.price.sAR.specialPrice)));
//                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), mCurrencyCode.length(), mCurrencyCode.length()+1+Utils.getIntPrice(mExchangeRate, Float.parseFloat(productItem.price.sAR.specialPrice)).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                } else {
//                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mCurrencyCode.length(), mCurrencyCode.length()+1+Utils.getIntPrice(mExchangeRate, Float.parseFloat(productItem.price.sAR.specialPrice)).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                }
//                holder.tvProductNewPrice.setText(sb);
//            } else {
                    if (!TextUtils.isEmpty("" + productItem.price.sAR.origionalPrice)) {
//                holder.tvProductNewPrice.setText(Utils.getIntPrice(mExchangeRate, Float.parseFloat(productItem.price.sAR.specialPrice)) + " " + mCurrencyCode);
                        SpannableStringBuilder sb = new SpannableStringBuilder(Utils.getIntPrice(mExchangeRate, productItem.price.sAR.specialPrice) + " " + mCurrencyCode);
//                        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), 0, Utils.getIntPrice(mExchangeRate, Float.parseFloat(productItem.price.sAR.specialPrice)).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                        } else {
//                            sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), 0, Utils.getIntPrice(mExchangeRate, Float.parseFloat(productItem.price.sAR.specialPrice)).length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                        }
                        holder.tvProductNewPrice.setText(sb);
                    }
//            }

                    holder.tvProductNewPrice.setVisibility(View.VISIBLE);

//                    holder.tvProductOldPrice.setText(Utils.getIntPrice(mExchangeRate, productItem.price.sAR.origionalPrice) + "");
//                    holder.tvProductOldPrice.setTextColor(mContext.getResources().getColor(R.color.gray_66));
                    holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.tvProductNewPrice.setVisibility(View.GONE);
                }

                holder.tvWishlist.setVisibility(View.GONE);

//                holder.tvWishlist.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//
//                        if (dbAdapter.isContainInWishList(productItem.getSku())) {
//
//                            if (TextUtils.isEmpty(userToken) && dbAdapter.isGuestWishItem(productItem.getSku())) {
//                                dbAdapter.removeWishItem(productItem.getSku());
//                            } else {
//                                dbAdapter.updateSoftDeleteWishItem(productItem.getSku(), "1");
//                            }
//                            if (mContext instanceof HomeActivity) {
//                                HomeActivity activity = (HomeActivity) mContext;
//                                activity.showWishListMsg(mContext.getResources().getString(R.string.msg_item_removed));
//                            }
//                        } else {
//
//                            Wishlist.WishlistData mWishItem = new Wishlist().new WishlistData();
//                            mWishItem.setId(productItem.getEntityId());
//                            mWishItem.setName(productItem.getName());
//                            mWishItem.setDescription(productItem.getShortDescription());
//                            mWishItem.setSku(productItem.getSku());
//                            mWishItem.setProductId(productItem.getEntityId());
//                            mWishItem.setTypeId(productItem.getTypeId());
//                            mWishItem.setQty(1);
//                            if (!TextUtils.isEmpty("" + productItem.getPrice())) {
//                                mWishItem.setPrice(productItem.getPrice());
//                            }
//                            if (!TextUtils.isEmpty("" + productItem.getSpecialPrice())) {
//                                mWishItem.setSpecial_price(productItem.getSpecialPrice());
//                            }
//                            mWishItem.setSpecial_from_date(productItem.getSpecial_from_date());
//                            mWishItem.setSpecial_to_date(productItem.getSpecial_to_date());
//                            mWishItem.setThumbNail(productItem.getThumbNail());
//                            mWishItem.setBrand(productItem.getBrand());
//                            mWishItem.setRating(0);
//
//
//                            if (!TextUtils.isEmpty(userToken)) {
//                                mWishItem.setIsGuest("0");
//                                mWishItem.setToken(userToken);
//
//                            } else {
//                                mWishItem.setIsGuest("1");
//                                String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
//                                mWishItem.setToken(guestToken);
//                            }
//
//                            if (Utils.isInternetConnected(mContext)) {
//                                mWishItem.setIs_on_server("0");
//                            } else {
//                                mWishItem.setIs_on_server("0");
//                            }
//
//                            mWishItem.setSoft_delete("0");
//
//                            dbAdapter.addWishItem(mWishItem);
//
//                            if (mContext instanceof HomeActivity) {
//                                HomeActivity activity = (HomeActivity) mContext;
//                                activity.showWishListMsg(mContext.getResources().getString(R.string.msg_item_saved));
//                            }
//                        }
//                        notifyDataSetChanged();
//
//                    }
//                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    private class ViewHolder {
        public ImageView ivProductImage, ivPreloaderProductImage;
        public RelativeLayout rlImageContainer;
        public TextView tvProductName, tvProductOldPrice, tvProductNewPrice, tvWishlist, tvOffer;
        public AutofitTextView tvProductCategory;
        private AVLoadingIndicatorView mAVProgressbar;
    }
}
