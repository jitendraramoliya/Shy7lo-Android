package shy7lo.com.shy7lo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.plugin.AdjustCriteo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.answers.AddToCartEvent;
import com.crashlytics.android.answers.Answers;
import com.daimajia.swipe.SwipeLayout;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.fragment.WishlistFragment;
import shy7lo.com.shy7lo.model.Wishlist;
import shy7lo.com.shy7lo.notification.NotificationUtils;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.CustomTypefaceSpan;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.wheel.WheelQtyPicker;
import shy7lo.com.shy7lo.wheel.WheelSizePicker;

import static shy7lo.com.shy7lo.utils.Constant.BNDL_IS_FROM_SEARCH;
import static shy7lo.com.shy7lo.utils.Constant.BNDL_SKU;

/**
 * Created by JITEN-PC on 15-02-2017.
 */

// offline wishlist
public class WishlistAdapter extends BaseAdapter {

    Context mContext;
    List<Wishlist.WishlistData> mValueList = new ArrayList<Wishlist.WishlistData>();
    WishlistFragment wishlistFragment;
    private String mCurrencyCode = "";
    private float mExchangeRate;
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DBAdapter dbAdapter;

    public WishlistAdapter(Context context, List<Wishlist.WishlistData> mValueList, DBAdapter dbAdapter, WishlistFragment wishlistFragment) {
        this.mContext = context;
        this.mValueList = mValueList;
        this.dbAdapter = dbAdapter;
        this.wishlistFragment = wishlistFragment;
//        if (context == null) {
//            this.mContext = Shy7lo.getAppContext();
//        }
        mCurrencyCode = MyPref.getPref(mContext, MyPref.DEFAULT_CURRENCY_CODE, mContext.getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(mContext, MyPref.DEFAULT_EXCHANGE_RATE, 1f);
    }

    public void refill(List<Wishlist.WishlistData> mWishlistDatas) {
        if (mValueList != null && mValueList.size() > 0) {
            mValueList.clear();
        }
        mValueList.addAll(mWishlistDatas);
        LogUtils.e("", "refill::" + mValueList.size());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        LogUtils.e("", "getCount::" + mValueList.size());
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


        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_wishlist, null);
            ViewHolder holder = new ViewHolder();
            holder.ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
            holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvAddToCart = (TextView) view.findViewById(R.id.tvAddToCart);
            holder.tvRemove = (TextView) view.findViewById(R.id.tvRemove);
            holder.tvOldPrice = (TextView) view.findViewById(R.id.tvOldPrice);
            holder.tvNewPrice = (TextView) view.findViewById(R.id.tvNewPrice);
            holder.tvSize = (TextView) view.findViewById(R.id.tvSize);
            holder.tvSizeValue = (TextView) view.findViewById(R.id.tvSizeValue);
            holder.tvQty = (TextView) view.findViewById(R.id.tvQty);
            holder.tvQtyValue = (TextView) view.findViewById(R.id.tvQtyValue);
            holder.lnrDelete = (LinearLayout) view.findViewById(R.id.lnrDelete);
            holder.lnrAddToCart = (LinearLayout) view.findViewById(R.id.lnrAddToCart);
            holder.lnrContent = (LinearLayout) view.findViewById(R.id.lnrContent);
            holder.swipeLayout = (SwipeLayout) view.findViewById(R.id.swipeLayout);


            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.lnrDelete);
                holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.lnrAddToCart);
            } else {
                holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.lnrAddToCart);
                holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.lnrDelete);
            }

            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();


        holder.swipeLayout.setTag(R.id.swipeLayout, position);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.lnrContent.setScaleX(-1f);
            holder.ivProductImage.setScaleX(-1f);
            holder.tvTitle.setScaleX(-1f);
            holder.tvName.setScaleX(-1f);
            holder.tvOldPrice.setScaleX(-1f);
            holder.tvNewPrice.setScaleX(-1f);
            holder.tvSize.setScaleX(-1f);
            holder.tvSizeValue.setScaleX(-1f);
            holder.tvQty.setScaleX(-1f);
            holder.tvQtyValue.setScaleX(-1f);

//            holder.lnrDelete.setScaleX(-1f);
//            holder.lnrAddToCart.setScaleX(-1f);

            holder.tvName.setGravity(Gravity.RIGHT);
            holder.tvTitle.setGravity(Gravity.RIGHT);

            holder.tvTitle.setTypeface(Shy7lo.DroidKufiBold);
            holder.tvOldPrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvNewPrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvName.setTypeface(Shy7lo.DroidKufiRegular);

            holder.tvSizeValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_down, 0, 0, 0);
            holder.tvQtyValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_down, 0, 0, 0);
        } else {
            holder.lnrContent.setScaleX(1f);
            holder.ivProductImage.setScaleX(1f);
            holder.tvTitle.setScaleX(1f);
            holder.tvName.setScaleX(1f);
            holder.tvOldPrice.setScaleX(1f);
            holder.tvNewPrice.setScaleX(1f);
            holder.tvSize.setScaleX(1f);
            holder.tvSizeValue.setScaleX(1f);
            holder.tvQty.setScaleX(1f);
            holder.tvQtyValue.setScaleX(1f);

//            holder.lnrDelete.setScaleX(1f);
//            holder.lnrAddToCart.setScaleX(1f);

            holder.tvName.setGravity(Gravity.LEFT);
            holder.tvTitle.setGravity(Gravity.LEFT);

            holder.tvTitle.setTypeface(Shy7lo.RalewayBold);
            holder.tvOldPrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvNewPrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvName.setTypeface(Shy7lo.RalewayRegular);

            holder.tvSizeValue.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_down, 0);
            holder.tvQtyValue.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_down, 0);
        }

        final Wishlist.WishlistData value = mValueList.get(position);

        if (value != null) {

            String mProductText = "";
            if (!TextUtils.isEmpty(value.getBrand()) && !value.getBrand().equalsIgnoreCase("null")) {
                mProductText = value.getBrand();
            } else {
                mProductText = "";
            }
            int brandLength = mProductText.length();
            if (mProductText.length() > 0) {
                mProductText = mProductText + " " + value.getName();
            } else {
                mProductText = value.getName();
            }
            SpannableStringBuilder sbBrand = new SpannableStringBuilder(mProductText);
            sbBrand.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, brandLength, 0);
            holder.tvName.setText(sbBrand);


//            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                if (value.getPrice() != null && !TextUtils.isEmpty(String.valueOf(value.getPrice()))) {
//                    holder.tvOldPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, value.getPrice()));
//                } else {
//                    holder.tvOldPrice.setText(mCurrencyCode + " " + value.getPrice());
//                }
//            } else {
            if (value.getPrice() != null && !TextUtils.isEmpty(String.valueOf(value.getPrice()))) {
//                holder.tvOldPrice.setText(Utils.getRealPrice(mExchangeRate, value.getPrice()) + " " + mCurrencyCode);
                SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, value.getPrice()));
                int start = mCurrencyCode.length() + 1;
                int end = mCurrencyCode.length() + 1 + Utils.getIntPrice(mExchangeRate, value.getPrice()).length();
                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                } else {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                holder.tvOldPrice.setText(sb);
            } else {
                holder.tvOldPrice.setText("" + value.getPrice() + " " + mCurrencyCode);
            }
//            }

            holder.tvSizeValue.setText("" + value.getSize());
            holder.tvSizeValue.setVisibility(View.GONE);
            holder.tvSize.setVisibility(View.GONE);

            holder.tvQty.setVisibility(View.GONE);
            holder.tvQtyValue.setVisibility(View.GONE);

            holder.tvNewPrice.setVisibility(View.GONE);
            holder.tvOldPrice.setPaintFlags(0);
            holder.tvOldPrice.setTextColor(mContext.getResources().getColor(R.color.black));


            try {
                if (value.getSpecial_price() > 0 && !TextUtils.isEmpty(String.valueOf(value.getSpecial_price()))) {

                    if (!TextUtils.isEmpty(value.getSpecial_from_date()) && !TextUtils.isEmpty(value.getSpecial_to_date())) {

                        Date fromDate = sdfDate.parse(value.getSpecial_from_date());
                        Date toDate = sdfDate.parse(value.getSpecial_to_date());
                        Date currentDate = new Date();

                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {
//                holder.tvNewPrice.setText(value.getSpecial_price() + " " + mCurrencyCode);
//                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    holder.tvNewPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, value.getSpecial_price()));
//                } else {
//                            holder.tvNewPrice.setText(Utils.getRealPrice(mExchangeRate, value.getSpecial_price()) + " " + mCurrencyCode);

                            SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, value.getSpecial_price()));
                            int start = mCurrencyCode.length() + 1;
                            int end = mCurrencyCode.length() + 1 + Utils.getIntPrice(mExchangeRate, value.getSpecial_price()).length();
                            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            } else {
                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            }
                            holder.tvNewPrice.setText(sb);
                            holder.tvNewPrice.setVisibility(View.VISIBLE);

//                            if (!TextUtils.isEmpty("" + value.getPrice())) {
//                                holder.tvOldPrice.setText(Utils.getIntPrice(mExchangeRate, value.getPrice()) + "");
//                            } else {
//                                holder.tvOldPrice.setText(value.getPrice() + " ");
//                            }
                            holder.tvOldPrice.setTextColor(mContext.getResources().getColor(R.color.gray_66));
                            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                }
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (value.getTypeId().equalsIgnoreCase("configurable")) {

                try {

//                    List<Wishlist.Option> option;

                    if (!TextUtils.isEmpty(value.getDescription()) && !value.getDescription().equalsIgnoreCase("null")) {

                        Wishlist.Option mOption = null;

                        Wishlist.ConfigurableAttributes mConfigurableAttributes = new Gson().fromJson(value.getDescription(), Wishlist.ConfigurableAttributes.class);
                        if (mConfigurableAttributes != null && mConfigurableAttributes.attributeDetails != null && mConfigurableAttributes.attributeDetails.size() > 0) {
                            for (int i = 0; i < mConfigurableAttributes.attributeDetails.size(); i++) {
                                Wishlist.AttributeDetail mAttributeDetail = mConfigurableAttributes.attributeDetails.get(i);
//                        if (mConfigurableAttributes != null && mConfigurableAttributes.attributeDetails != null) {
//                                Wishlist.AttributeDetail mAttributeDetail = mConfigurableAttributes.attributeDetails;
                                LogUtils.e("", "mAttributeDetail.attributeCode::" + mAttributeDetail.attributeCode);
                                if (mAttributeDetail != null && mAttributeDetail.attributeCode.contains("size") && mAttributeDetail.attributeCode != null && mAttributeDetail.option.size() > 0) {
//                                    option = mAttributeDetail.option;
                                    mOption = mAttributeDetail.option.get(0);
                                    if (mAttributeDetail.option != null && mAttributeDetail.option.size() > 0 && !TextUtils.isEmpty(value.getSize())) {
                                        for (int j = 0; j < mAttributeDetail.option.size(); j++) {
                                            LogUtils.e("", "value.getSize()::" + value.getSize() + " mAttributeDetail.option.get(j).defaultLabel::" + mAttributeDetail.option.get(j).defaultLabel + " Result:" + mAttributeDetail.option.get(j).defaultLabel.equalsIgnoreCase(value.getSize()));
                                            LogUtils.e("", "value.getSize()::" + value.getSize() + " mAttributeDetail.option.get(j).label::" + mAttributeDetail.option.get(j).label + " Result:" + mAttributeDetail.option.get(j).label.equalsIgnoreCase(value.getSize()));
                                            if (mAttributeDetail.option.get(j).defaultLabel.equalsIgnoreCase(value.getSize())
                                                    || mAttributeDetail.option.get(j).label.equalsIgnoreCase(value.getSize())) {
                                                mOption = mAttributeDetail.option.get(j);
                                            }
                                        }
                                    }


                                }
                            }
                        }

                        if (mOption != null) {
                            if (mOption != null) {
                                holder.tvSizeValue.setText("" + mOption.label);
                                holder.tvSizeValue.setVisibility(View.VISIBLE);
                                holder.tvSize.setVisibility(View.VISIBLE);

                                if (mOption.isInStock) {
                                    holder.tvQtyValue.setText("1");
                                    holder.tvQty.setVisibility(View.VISIBLE);
                                    holder.tvQtyValue.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                LogUtils.e("", "value.getStockStatus()::" + value.getStockStatus());
                if (!TextUtils.isEmpty(value.getStockStatus()) && value.getStockStatus().equalsIgnoreCase("1")) {
                    if (value.getQty() > 0) {
                        holder.tvQtyValue.setText("" + value.getQty());
                    } else {
                        holder.tvQtyValue.setText("1");
                    }

                    holder.tvQty.setVisibility(View.VISIBLE);
                    holder.tvQtyValue.setVisibility(View.VISIBLE);
                }
            }

            holder.tvQtyValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.tvQty.performClick();
                }
            });

            holder.tvQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showQtyDialog(holder, value, position);
                }
            });

            holder.tvSizeValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.tvSize.performClick();
                }
            });

            holder.tvSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!TextUtils.isEmpty(value.getDescription()) && !value.getDescription().equalsIgnoreCase("null")) {
                        showSizeDialog(holder, value, position);
                    }
                }
            });

            Glide.with(mContext)
                    .load(value.getThumbNail())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.ivProductImage);


            holder.lnrAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.tvAddToCart.performClick();
                }
            });

            holder.tvAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProductToCart(holder, value, position);
                }
            });


            holder.lnrDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.tvRemove.performClick();
                }
            });

            holder.tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
                    if (TextUtils.isEmpty(userToken) && dbAdapter.isGuestWishItem(value.getSku())) {
                        dbAdapter.removeWishItem(value.getSku());
                    } else {
                        dbAdapter.updateSoftDeleteWishItem(value.getSku(), "1");
                    }
                    wishlistFragment.getWishListFromDB();
                    closeSwipe(holder.swipeLayout);
//                    if (!TextUtils.isEmpty(value.getIsGuest()) && value.getIsGuest().equalsIgnoreCase("0")) {
//                        Utils.deleteProduct(mContext, dbAdapter, value.getId(), value.getSku());
//                    }


                }
            });

            holder.lnrContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle b = new Bundle();
                    b.putString(BNDL_SKU, "" + value.getSku());
                    b.putBoolean(BNDL_IS_FROM_SEARCH, false);

                    HomeActivity activity = (HomeActivity) mContext;
                    activity.loadProductDetails(b);
                }
            });

        }


        return view;
    }

    private class ViewHolder {
        ImageView ivProductImage;
        TextView tvTitle, tvName, tvAddToCart, tvRemove, tvOldPrice, tvNewPrice, tvSize, tvSizeValue, tvQty, tvQtyValue;
        LinearLayout lnrDelete, lnrAddToCart, lnrContent;
        SwipeLayout swipeLayout;
    }

    private void showQtyDialog(final ViewHolder holder, final Wishlist.WishlistData value, final int position) {

        final Animation animClose, animOpen;
        animOpen = AnimationUtils.loadAnimation(mContext,
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(mContext,
                R.anim.animation_dialog_close);

//        final Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogSlideAnim));
        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_qty_list);

        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final TextView tvQuantity = (TextView) dialog.findViewById(R.id.tvQuantity);
        final TextView tvDone = (TextView) dialog.findViewById(R.id.tvDone);

        final WheelQtyPicker pickerQty = (WheelQtyPicker) dialog.findViewById(R.id.pickerQty);
        final List<String> data = new ArrayList<>();
        int mItemPosition = 0;

        if (value.getTypeId().equalsIgnoreCase("configurable")) {

            try {

                if (!TextUtils.isEmpty(value.getDescription()) && !value.getDescription().equalsIgnoreCase("null")) {

                    Wishlist.ConfigurableAttributes mConfigurableAttributes = new Gson().fromJson(value.getDescription(), Wishlist.ConfigurableAttributes.class);
                    if (mConfigurableAttributes != null && mConfigurableAttributes.attributeDetails != null && mConfigurableAttributes.attributeDetails.size() > 0) {
                        for (int i = 0; i < mConfigurableAttributes.attributeDetails.size(); i++) {
                            Wishlist.AttributeDetail mAttributeDetail = mConfigurableAttributes.attributeDetails.get(i);
                            if (mAttributeDetail != null && mAttributeDetail.attributeCode.contains("size") && mAttributeDetail.attributeCode != null && mAttributeDetail.option.size() > 0) {
                                Wishlist.Option mOption = mAttributeDetail.option.get(0);

                                if (mOption != null) {
                                    for (int j = 0; j < mOption.qty; j++) {
                                        int qty = j + 1;
                                        data.add("" + qty);
//                                        if (qty == value.getQty()) {
//                                            mItemPosition = j;
//                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            for (int i = 0; i < value.getStockQty(); i++) {
                int qty = i + 1;
                data.add("" + qty);
//                if (qty == value.getQty()) {
//                    mItemPosition = i;
//                }
            }
        }


        pickerQty.setItems(data);
        pickerQty.setSelectedItemPosition(mItemPosition);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrContainer.setScaleX(-1f);
            tvQuantity.setScaleX(-1f);
            pickerQty.setScaleX(-1f);
            tvDone.setScaleX(-1f);
        } else {
            lnrContainer.setScaleX(1f);
            tvQuantity.setScaleX(1f);
            pickerQty.setScaleX(1f);
            tvDone.setScaleX(1f);
        }

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lnrContainer.startAnimation(animClose);
                    }
                }, 10);

            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int qty = Integer.parseInt(pickerQty.getCurrentQty());
                if (qty != value.getQty()) {
                    holder.tvQtyValue.setText("" + qty);
//                    setQuantitylist(holder, cartItem, qty, position);
                }

                rlMain.performClick();
            }
        });

        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }
                }, 10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        lnrContainer.startAnimation(animOpen);
        dialog.show();
    }

    int mItemPosition = 0;
    String mSelectedName = "";

    private void showSizeDialog(final ViewHolder holder, final Wishlist.WishlistData value, final int position) {

        mItemPosition = 0;
        mSelectedName = "";

        final Animation animClose, animOpen;
        animOpen = AnimationUtils.loadAnimation(mContext,
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(mContext,
                R.anim.animation_dialog_close);

//        final Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogSlideAnim));
        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_size_list);

        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final TextView tvSelectSize = (TextView) dialog.findViewById(R.id.tvSelectSize);
        final TextView tvDone = (TextView) dialog.findViewById(R.id.tvDone);

        tvSelectSize.setText("" + mContext.getString(R.string.sizes_table));

        final WheelSizePicker pickerSize = (WheelSizePicker) dialog.findViewById(R.id.pickerSize);


        if (value.getTypeId().equalsIgnoreCase("configurable")) {
            List<Wishlist.Option> option = null;
            try {

                Wishlist.ConfigurableAttributes mConfigurableAttributes = new Gson().fromJson(value.getDescription(), Wishlist.ConfigurableAttributes.class);


                if (mConfigurableAttributes != null && mConfigurableAttributes.attributeDetails != null && mConfigurableAttributes.attributeDetails.size() > 0) {
                    for (int i = 0; i < mConfigurableAttributes.attributeDetails.size(); i++) {
                        Wishlist.AttributeDetail mAttributeDetail = mConfigurableAttributes.attributeDetails.get(i);
                        if (mAttributeDetail != null && mAttributeDetail.attributeCode != null && mAttributeDetail.attributeCode.contains("size") && mAttributeDetail.option.size() > 0) {
                            option = mAttributeDetail.option;
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (option != null && option.size() > 0) {
                pickerSize.updateWishListSize(option);
                pickerSize.updateIndex(mItemPosition);
            }

        }


        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrContainer.setScaleX(-1f);
            tvSelectSize.setScaleX(-1f);
            pickerSize.setScaleX(-1f);
            tvDone.setScaleX(-1f);
        } else {
            lnrContainer.setScaleX(1f);
            tvSelectSize.setScaleX(1f);
            pickerSize.setScaleX(1f);
            tvDone.setScaleX(1f);
        }

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lnrContainer.startAnimation(animClose);
                    }
                }, 10);

            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mItemPosition = pickerSize.getCurrentItemPosition();
                mSelectedName = pickerSize.getCurrentSize();

                value.setSize(mSelectedName);
                LogUtils.e("", "tvDone mSelectedName " + mSelectedName + " value.getSize()::" + value.getSize());
                dbAdapter.updateItemSize(value.getSku(), mSelectedName);

                holder.tvSizeValue.setText("" + mSelectedName);

                rlMain.performClick();
            }
        });

        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }
                }, 10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        lnrContainer.startAnimation(animOpen);
        dialog.show();
    }

    private float mFinalRate;

    private void addProductToCart(final ViewHolder holder, final Wishlist.WishlistData value, final int position) {

        if (value.getTypeId().equalsIgnoreCase("configurable") && TextUtils.isEmpty(holder.tvSizeValue.getText().toString())) {
            Utils.showToast(getActivity(), "" + getActivity().getString(R.string.select_size));
            return;
        }

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        addProductToCart(holder, value, position);
                    }
                }
            });
            return;
        }

        LogUtils.e("", "getGuestCartToken call");
        Utils.showProgressDialog(getActivity());
        try {

            if (value.getPrice() > 0) {
                mFinalRate = value.getPrice();
            }

            try {
                if (value.getSpecial_price() > 0 && !TextUtils.isEmpty(String.valueOf(value.getSpecial_price()))) {

                    if (!TextUtils.isEmpty(value.getSpecial_from_date()) && !TextUtils.isEmpty(value.getSpecial_to_date())) {

                        Date fromDate = sdfDate.parse(value.getSpecial_from_date());
                        Date toDate = sdfDate.parse(value.getSpecial_to_date());
                        Date currentDate = new Date();

                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {

                            mFinalRate = value.getSpecial_price();

                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String mOptionId = "", mOptionValue = "";
            if (value.getTypeId().equalsIgnoreCase("configurable") && !TextUtils.isEmpty(value.getSize())) {
                if (!TextUtils.isEmpty(value.getDescription()) && !value.getDescription().equalsIgnoreCase("null")) {

                    Wishlist.ConfigurableAttributes mConfigurableAttributes = new Gson().fromJson(value.getDescription(), Wishlist.ConfigurableAttributes.class);
                    if (mConfigurableAttributes != null && mConfigurableAttributes.attributeDetails != null && mConfigurableAttributes.attributeDetails.size() > 0) {
                        for (int i = 0; i < mConfigurableAttributes.attributeDetails.size(); i++) {
                            Wishlist.AttributeDetail mAttributeDetail = mConfigurableAttributes.attributeDetails.get(i);
                            LogUtils.e("", "mAttributeDetail.attributeCode::" + mAttributeDetail.attributeCode);
                            mOptionId = mAttributeDetail.attributeId;
                            if (mAttributeDetail != null && mAttributeDetail.attributeCode.contains("size") && mAttributeDetail.attributeCode != null && mAttributeDetail.option.size() > 0) {

                                if (mAttributeDetail.option != null && mAttributeDetail.option.size() > 0 && !TextUtils.isEmpty(value.getSize())) {
                                    for (int j = 0; j < mAttributeDetail.option.size(); j++) {
                                        if (mAttributeDetail.option.get(j).defaultLabel.equalsIgnoreCase(value.getSize())
                                                || mAttributeDetail.option.get(j).label.equalsIgnoreCase(value.getSize())) {
                                            mOptionValue = mAttributeDetail.option.get(j).valueIndex;
                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }

            String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");

            JSONObject jsonParams = new JSONObject();

            SharedPreferences pref = getActivity().getSharedPreferences(NotificationUtils.SHARED_PREF, 0);

            if (!TextUtils.isEmpty(userToken)) {

                jsonParams.put("sku", "" + value.getSku());
                jsonParams.put("qty", "1");
                jsonParams.put("product_name", "" + value.getName());
                jsonParams.put("price", "" + value.getPrice());
                jsonParams.put("product_type", value.getTypeId());
                jsonParams.put("device_token", "" + pref.getString("regId", ""));
//                jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
                jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));


                if (value.getTypeId().equalsIgnoreCase("configurable")) {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("option_id", mOptionId);
                    jsonObject.put("option_value", mOptionValue);
                    jsonArray.put(jsonObject);
                    jsonParams.put("configurable_item_options", jsonArray);
                }

            } else {

                String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");

//                jsonParams.put("sku", "" + testSku);
                jsonParams.put("sku", "" + value.getSku());
                jsonParams.put("qty", "1");
                jsonParams.put("product_name", "" + value.getName());
                jsonParams.put("price", "" + value.getPrice());
                jsonParams.put("product_type", value.getTypeId());
                jsonParams.put("cart_id", "" + guestToken);
                jsonParams.put("device_token", "" + pref.getString("regId", ""));
//                jsonParams.put("device_token", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
                jsonParams.put("subcribe_id", "" + MyPref.getPref(getActivity(), MyPref.ONE_SIGNAL_USER_ID, ""));

                if (value.getTypeId().equalsIgnoreCase("configurable")) {
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("option_id", mOptionId);
                    jsonObject.put("option_value", mOptionValue);
                    jsonArray.put(jsonObject);
                    jsonParams.put("configurable_item_options", jsonArray);
                }
            }

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());

            ApiInterface apiService =
                    RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

            Call<ResponseBody> call;
            if (!TextUtils.isEmpty(userToken)) {
                if (value.getTypeId().equalsIgnoreCase("configurable")) {
                    call = apiService.addUserProductConfigInCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
                } else {
                    call = apiService.addUserProductInCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
                }
            } else {
                if (value.getTypeId().equalsIgnoreCase("configurable")) {
                    call = apiService.addGuestProductConfigInCart(Shy7lo.mLangCode, body);
                } else {
                    call = apiService.addGuestProductInCart(Shy7lo.mLangCode, body);
                }
            }

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    LogUtils.e("", "response code:" + response.code());
                    Utils.closeProgressDialog();
                    if (response.isSuccessful()) {

                        int mCartItemCount = MyPref.getPref(getActivity(), MyPref.CART_ITEM_COUNT, 0);
                        mCartItemCount = mCartItemCount + 1;
                        MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, mCartItemCount);

                        Utils.showToast(getActivity(), getActivity().getString(R.string.msg_added_cart));

                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.updateBadgetCount();
                        }

//                        holder.swipeLayout.close

//                        showOrderMoreDialog();
                        try {
                            Answers.getInstance().logAddToCart(new AddToCartEvent()
                                    .putItemPrice(BigDecimal.valueOf(Utils.getAnswerPrice(mExchangeRate, mFinalRate)))
                                    .putCurrency(Currency.getInstance("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")))
                                    .putItemName("" + value.getName())
                                    .putItemType(value.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
                                    .putItemId("" + value.getSku()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        if (RestClient.isTuneEnable) {
//                            try {
//                                Tune tune = Tune.getInstance();
//                                List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
//                                TuneEventItem mTuneEventItem = new TuneEventItem("" + value.getName());
//                                mTuneEventItemsList.add(mTuneEventItem);
//                                tune.measureEvent(new TuneEvent(TuneEvent.ADD_TO_CART)
//                                        .withEventItems(mTuneEventItemsList)
//                                        .withQuantity(1)
//                                        .withContentId(value.getSku())
//                                        .withRevenue(0)
//                                        .withContentType(value.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                        .withCurrencyCode("" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }

                        try {
                            AdjustEvent event = new AdjustEvent("rd7dpc");
                            event.addPartnerParameter("Name", "" + "" + value.getName());
                            event.addPartnerParameter("Price", "" + "" + Utils.getAnswerPrice(mExchangeRate, mFinalRate));
                            event.addPartnerParameter("Currency", "" + "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, ""));
                            event.addPartnerParameter("Product Type", "" + (value.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple"));
                            event.addPartnerParameter("SKU", "" + "" + value.getSku());

                            //callback
                            event.addCallbackParameter("Name", "" + "" + value.getName());
                            event.addCallbackParameter("Price", "" + "" + Utils.getAnswerPrice(mExchangeRate, mFinalRate));
                            event.addCallbackParameter("Currency", "" + "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, ""));
                            event.addCallbackParameter("Product Type", "" + (value.getTypeId().equalsIgnoreCase("configurable") ? "Configurable" : "Simple"));
                            event.addCallbackParameter("SKU", "" + "" + value.getSku());

                            String mCriteo = MyPref.getPref(getActivity(), MyPref.APP_CRITEO, "");
                            if (TextUtils.isEmpty(mCriteo) || mCriteo.equalsIgnoreCase("1")) {
                                String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
                                AdjustCriteo.injectCustomerIdIntoCriteoEvents(TextUtils.isEmpty(userToken) ? userToken : MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
                            }

                            Adjust.trackEvent(event);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (RestClient.isFacebookLive) {
                            try {
                                // FB Log Add to cart
                                AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

                                Bundle parameters = new Bundle();
                                parameters.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, getActivity().getResources().getString(R.string.SAR)));
//                                parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "" + productDetails.getTypeId());
                                parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Product");
                                parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, "" + value.getSku());
                                parameters.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, value.getName());
                                parameters.putString(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, "1");

                                logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, Utils.getAnswerPrice(mExchangeRate, mFinalRate), parameters);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (getActivity() instanceof HomeActivity) {
                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.setDrawerSwipe(false);
                            activity.setFirebaseLog("Add_to_cart");
                        }

                        closeSwipe(holder.swipeLayout);

                        // for remove item from wishlist
                        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
                        if (TextUtils.isEmpty(userToken) && dbAdapter.isGuestWishItem(value.getSku())) {
                            dbAdapter.removeWishItem(value.getSku());
                        } else {
                            dbAdapter.updateSoftDeleteWishItem(value.getSku(), "1");
                        }
                        wishlistFragment.getWishListFromDB();

                    } else {

                        try {
                            JSONObject jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(mContext, "" + jResponse.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

//                        if (response.code() == 400) {
//                            Utils.showToast(getActivity(), response.code() + " " + getActivity().getString(R.string.msg_outof_order));
//                        } else {
//                            Utils.showAlertDialog(getActivity(), "" + response.code());
//                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println(t.getMessage());
                    Utils.closeProgressDialog();
                    Utils.showAlertDialog(getActivity(), "" + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Utils.showAlertDialog(getActivity(), "" + e.getMessage());
        }
    }

//    private void deleteProduct(final String item_id) {
//
//        if (!Utils.isInternetConnected(mContext)) {
//            Utils.showOfflineMsgDialog(mContext, new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(mContext)) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        deleteProduct(item_id);
//                    }
//                }
//            });
//            return;
//        }
//
//        Utils.showProgressDialog(mContext);
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//
//        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//
//        Map<String, Object> jsonParams = new ArrayMap<>();
//        jsonParams.put("item_id", "" + item_id);
//
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
//                (new JSONObject(jsonParams)).toString());
//
//        Call<JsonElement> callCode = apiService.deleteProductFromWishlist(Shy7lo.mLangCode, "Bearer " + userToken, body);
//
//        callCode.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        JSONObject jResponse = new JSONObject(response.body().toString());
//                        if (jResponse != null && jResponse.getString("success").equals("1")) {
//                            wishlistFragment.showWishList();
//                        } else if (jResponse != null && jResponse.getString("success").equals("0")) {
//                            Utils.closeProgressDialog();
//                            Utils.showToast(mContext, "" + jResponse.getString("message"));
//                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
//                            Utils.closeProgressDialog();
//                            Utils.showInitialScreen(mContext);
//                            return;
//                        } else {
//                            Utils.closeProgressDialog();
////                            Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Utils.closeProgressDialog();
//                        Utils.showAlertDialog(mContext);
////                        Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
//                    }
//                } else {
//                    Utils.closeProgressDialog();
//                    Utils.showAlertDialog(mContext);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
////                Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
//                Utils.closeProgressDialog();
//                Utils.showAlertDialog(mContext);
//            }
//        });
//    }

    private void closeSwipe(final SwipeLayout swipeLayout) {
        if (swipeLayout != null) {
            swipeLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.close();
                }
            }, 5);
        }
    }


    private Context getActivity() {
        return mContext;
    }

}


