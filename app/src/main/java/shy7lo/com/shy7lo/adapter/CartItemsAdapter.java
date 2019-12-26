package shy7lo.com.shy7lo.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.plugin.AdjustCriteo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.daimajia.swipe.SwipeLayout;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import shy7lo.com.shy7lo.fragment.ShoppingBagsFragment;
import shy7lo.com.shy7lo.model.ShoppingBag;
import shy7lo.com.shy7lo.model.Wishlist;
import shy7lo.com.shy7lo.notification.NotificationUtils;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.CustomTypefaceSpan;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.wheel.WheelQtyPicker;

import static shy7lo.com.shy7lo.fragment.ShoppingBagsFragment.TAG_SHOPPING_BAGS_FRAGMENT;
import static shy7lo.com.shy7lo.utils.Constant.BNDL_IS_FROM_SEARCH;
import static shy7lo.com.shy7lo.utils.Constant.BNDL_SKU;

/**
 * Created by JITEN-PC on 16-02-2017.
 */

public class CartItemsAdapter extends BaseAdapter {

    Context mContext;
    DBAdapter dbAdapter;
    List<ShoppingBag.Item> mCartItemsList;
    ShoppingBagsFragment shoppingBagsFragment;

    private String mCurrencyCode = "";
    private float mExchangeRate;
    private OnPlusMinusListener mOnPlusMinusListener;
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //    private String mExtraItem = "";
    private boolean isExtraItemFound = false;
    private int mScreenWidth = 720;
    private HorizontalScrollView hsvLastView;

    public CartItemsAdapter(Context mContext, DBAdapter dbAdapter, List<ShoppingBag.Item> mCartItemsList,
                            ShoppingBagsFragment shoppingBagsFragment, OnPlusMinusListener mOnPlusMinusListener) {
        this.mContext = mContext;
        this.mCartItemsList = mCartItemsList;
        this.dbAdapter = dbAdapter;
        this.shoppingBagsFragment = shoppingBagsFragment;
        this.mOnPlusMinusListener = mOnPlusMinusListener;
        mCurrencyCode = MyPref.getPref(mContext, MyPref.DEFAULT_CURRENCY_CODE, mContext.getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(mContext, MyPref.DEFAULT_EXCHANGE_RATE, 1f);

        mScreenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        LogUtils.e("", "mScreenWidth");

        checkForCartQtyUpdate();

    }

    private void checkForCartQtyUpdate() {
        if (mCartItemsList != null && mCartItemsList.size() > 0) {
            ArrayList<String> alExtra = new ArrayList<>();
            for (int i = 0; i < mCartItemsList.size(); i++) {
                ShoppingBag.Item item = mCartItemsList.get(i);
                if (!TextUtils.isEmpty(item.getStockStatus()) && item.getStockStatus().equalsIgnoreCase("0")) {
                    alExtra.add("" + item.getItemId());
                    isExtraItemFound = true;
                    break;
                } else if (!TextUtils.isEmpty(item.getStockStatus()) && item.getStockStatus().equalsIgnoreCase("1")) {
                    if (item.getStockQty() < item.getQty()) {
                        alExtra.add("" + item.getItemId());
                        isExtraItemFound = true;
                        break;
                    }
                }
            }
            LogUtils.e("", "isExtraItemFound::" + isExtraItemFound);
            if (isExtraItemFound) {
                showUpgradeDialog();
            }
        }
    }

    public void refill(List<ShoppingBag.Item> carItemList) {

        if (mCartItemsList != null && mCartItemsList.size() > 0) {
            mCartItemsList.clear();
        }
        mCartItemsList.addAll(carItemList);
        notifyDataSetChanged();
        LogUtils.e("", mCartItemsList.size() + " notifyDataSetChanged mCartItemsList::" + mCartItemsList);

        checkForCartQtyUpdate();
    }

    public interface OnPlusMinusListener {
        void onPlusClick(String sku, int position, int quantity);

        void onMinusClick(String sku, int position, int quantity);

        void onQtyClick(String sku, int position, int quantity);
    }

    @Override
    public int getCount() {
        return mCartItemsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mCartItemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LogUtils.e("", "getView call");


        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_cart_new, null);
            ViewHolder holder = new ViewHolder();
            holder.ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
            holder.tvSize = (TextView) view.findViewById(R.id.tvSize);
            holder.tvSizeValue = (TextView) view.findViewById(R.id.tvSizeValue);
            holder.tvSaveLater = (TextView) view.findViewById(R.id.tvSaveLater);
            holder.tvRemove = (TextView) view.findViewById(R.id.tvRemove);
            holder.tvDelete = (TextView) view.findViewById(R.id.tvDelete);
            holder.tbDetails = (TableRow) view.findViewById(R.id.tbDetails);
            holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            holder.tvNewPrice = (TextView) view.findViewById(R.id.tvNewPrice);
            holder.tvExclusivePrice = (TextView) view.findViewById(R.id.tvExclusivePrice);
            holder.tvQty = (TextView) view.findViewById(R.id.tvQty);
            holder.tvQtyValue = (TextView) view.findViewById(R.id.tvQtyValue);
            holder.tvMsg = (TextView) view.findViewById(R.id.tvMsg);
            holder.lnrMainView = (LinearLayout) view.findViewById(R.id.lnrMainView);
//            holder.hsvMainView = (HorizontalScrollView) view.findViewById(R.id.hsvMainView);
            holder.lnrDelete = (LinearLayout) view.findViewById(R.id.lnrDelete);
            holder.lnrSaveForLater = (LinearLayout) view.findViewById(R.id.lnrSaveForLater);
            holder.swipeLayout = (SwipeLayout) view.findViewById(R.id.swipeLayout);

            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.lnrSaveForLater);
                holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.lnrDelete);
            } else {
                holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.lnrDelete);
                holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.lnrSaveForLater);
            }


            view.setTag(holder);
        }


        final ViewHolder holder = (ViewHolder) view.getTag();

        holder.swipeLayout.setTag(R.id.swipeLayout, position);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.lnrMainView.getLayoutParams();
        params.width = mScreenWidth;
        holder.lnrMainView.setLayoutParams(params);

//        holder.hsvMainView.scrollTo(0, 0);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

//            holder.tvAddToCart.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cart_save, 0);
//            holder.tvRemove.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cart_remove, 0);

            holder.tvTitle.setGravity(Gravity.RIGHT);
            holder.tvName.setGravity(Gravity.RIGHT);
            holder.tvMsg.setGravity(Gravity.RIGHT);

            holder.lnrMainView.setScaleX(-1f);
            holder.ivProductImage.setScaleX(-1f);
            holder.tvQty.setScaleX(-1f);
            holder.tvQtyValue.setScaleX(-1f);
            holder.tvSize.setScaleX(-1f);
            holder.tvSizeValue.setScaleX(-1f);
            holder.tvTitle.setScaleX(-1f);
            holder.tvName.setScaleX(-1f);
            holder.tvPrice.setScaleX(-1f);
            holder.tvNewPrice.setScaleX(-1f);
            holder.tvExclusivePrice.setScaleX(-1f);
            holder.tvMsg.setScaleX(-1f);
//            holder.tvAddToCart.setScaleX(-1f);
//            holder.tvRemove.setScaleX(-1f);
//            holder.lnrDelete.setScaleX(-1f);
//            holder.lnrSaveForLater.setScaleX(-1f);

            holder.tvTitle.setTypeface(Shy7lo.DroidKufiBold);
            holder.tvName.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvSize.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvSizeValue.setTypeface(Shy7lo.DroidKufiBold);
            holder.tvPrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvNewPrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvExclusivePrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvSaveLater.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvRemove.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvQty.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvQtyValue.setTypeface(Shy7lo.DroidKufiBold);
            holder.tvMsg.setTypeface(Shy7lo.DroidKufiRegular);

            holder.tvQtyValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_down, 0, 0, 0);
            holder.tvSizeValue.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_down, 0, 0, 0);

//            holder.lnrDelete.setTypeface(Shy7lo.DroidKufiRegular);
//            holder.lnrAddToCart.setTypeface(Shy7lo.DroidKufiRegular);
        } else {

//            holder.tvAddToCart.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cart_save, 0, 0, 0);
//            holder.tvRemove.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cart_remove, 0, 0, 0);

            holder.tvTitle.setGravity(Gravity.LEFT);
            holder.tvName.setGravity(Gravity.LEFT);
            holder.tvMsg.setGravity(Gravity.LEFT);

            holder.lnrMainView.setScaleX(1f);
            holder.ivProductImage.setScaleX(1f);
            holder.tvQty.setScaleX(1f);
            holder.tvQtyValue.setScaleX(1f);
            holder.tvSize.setScaleX(1f);
            holder.tvSizeValue.setScaleX(1f);
            holder.tvTitle.setScaleX(1f);
            holder.tvName.setScaleX(1f);
            holder.tvPrice.setScaleX(1f);
            holder.tvNewPrice.setScaleX(1f);
            holder.tvExclusivePrice.setScaleX(1f);
            holder.tvMsg.setScaleX(1f);
//            holder.tvAddToCart.setScaleX(1f);
//            holder.tvRemove.setScaleX(1f);
//            holder.lnrDelete.setScaleX(1f);
//            holder.lnrSaveForLater.setScaleX(1f);

            holder.tvTitle.setTypeface(Shy7lo.RalewayBold);
            holder.tvName.setTypeface(Shy7lo.RalewayRegular);
            holder.tvSize.setTypeface(Shy7lo.RalewayRegular);
            holder.tvSizeValue.setTypeface(Shy7lo.RalewayBold);
            holder.tvPrice.setTypeface(Shy7lo.RalewayRegular);
            holder.tvNewPrice.setTypeface(Shy7lo.RalewayRegular);
            holder.tvExclusivePrice.setTypeface(Shy7lo.RalewayRegular);
            holder.tvSaveLater.setTypeface(Shy7lo.RalewayRegular);
            holder.tvRemove.setTypeface(Shy7lo.RalewayRegular);
            holder.tvQty.setTypeface(Shy7lo.RalewayRegular);
            holder.tvQtyValue.setTypeface(Shy7lo.RalewayBold);
            holder.tvMsg.setTypeface(Shy7lo.RalewayRegular);

            holder.tvQtyValue.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_down, 0);
            holder.tvSizeValue.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_down, 0);
//            holder.lnrDelete.setTypeface(Shy7lo.RalewayRegular);
//            holder.lnrAddToCart.setTypeface(Shy7lo.RalewayRegular);

        }

        holder.tvSizeValue.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        final ShoppingBag.Item cartItem = mCartItemsList.get(position);

        if (cartItem != null) {

            if (cartItem.isWishListItem()) {
                holder.tvSaveLater.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_wishlist_red, 0, 0);
            } else {
                holder.tvSaveLater.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_wishlist_white, 0, 0);
            }

            String mProductText = "";
            if (!TextUtils.isEmpty(cartItem.getBrand()) && !cartItem.getBrand().equalsIgnoreCase("null")) {
                mProductText = cartItem.getBrand();
            } else {
                mProductText = "";
            }
            int brandLength = mProductText.length();
            if (mProductText.length() > 0) {
                mProductText = mProductText + " " + cartItem.getName();
            } else {
                mProductText = cartItem.getName();
            }
            SpannableStringBuilder sbBrand = new SpannableStringBuilder(mProductText);
            sbBrand.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, brandLength, 0);
            holder.tvName.setText(sbBrand);

            holder.tvQtyValue.setText("" + cartItem.getQty());

            if (cartItem.getCustom_msg() != null) {
                if (!TextUtils.isEmpty(cartItem.getCustom_msg().getMsg())) {
                    holder.tvMsg.setText(cartItem.getCustom_msg().getMsg());
                    holder.tvMsg.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(cartItem.getCustom_msg().getColor())) {
                        try {
                            LogUtils.e("", "getColor::" + cartItem.getCustom_msg().getColor());
                            holder.tvMsg.setTextColor(Color.parseColor("" + cartItem.getCustom_msg().getColor()));
                        } catch (Exception e) {
                            try {
                                String color = cartItem.getCustom_msg().getColor();
                                if (color.length() > 6) {
                                    color = "#" + color.substring((color.length() - 6));
                                    LogUtils.e("", "color::" + color + " original" + cartItem.getCustom_msg().getColor());
                                    holder.tvMsg.setTextColor(Color.parseColor(color));
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            e.printStackTrace();
                        }
                    }
                } else {
                    holder.tvMsg.setVisibility(View.GONE);
                }
            } else {
                holder.tvMsg.setVisibility(View.GONE);
            }


//            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                if (cartItem.getPrice() != null && !TextUtils.isEmpty(String.valueOf(cartItem.getPrice()))) {
//                    holder.tvPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, cartItem.getPrice()));
//                } else {
//                    holder.tvPrice.setText(mCurrencyCode + " " + cartItem.getPrice());
//                }
//            } else {
//            if (!TextUtils.isEmpty("" + cartItem.getPrice_excl_tax())) {
//                holder.tvExclusivePrice.setText(mContext.getResources().getString(R.string.exclusive_tax) + " " + (int) cartItem.getPrice_excl_tax() + " " + mCurrencyCode);
//                holder.tvExclusivePrice.setVisibility(View.VISIBLE);
//            } else {
//                holder.tvExclusivePrice.setVisibility(View.GONE);
//            }
            LogUtils.e("", "cartItem.getPrice()::" + cartItem.getPrice());
            if (cartItem.getPrice() != null && !TextUtils.isEmpty(String.valueOf(cartItem.getPrice()))) {
//                holder.tvPrice.setText(Utils.getIntPrice(mExchangeRate, cartItem.getPrice()) + " " + mCurrencyCode);

                SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, cartItem.getPrice()));
                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), mCurrencyCode.length(), mCurrencyCode.length() + Utils.getIntPrice(mExchangeRate, cartItem.getPrice()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                } else {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mCurrencyCode.length(), mCurrencyCode.length() + Utils.getIntPrice(mExchangeRate, cartItem.getPrice()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                holder.tvPrice.setText(sb);
            } else {
//                holder.tvPrice.setText("" + cartItem.getPrice() + " " + mCurrencyCode);

                SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + cartItem.getPrice());
                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), mCurrencyCode.length(), mCurrencyCode.length() + String.valueOf(cartItem.getPrice()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                } else {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mCurrencyCode.length(), mCurrencyCode.length() + String.valueOf(cartItem.getPrice()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                holder.tvPrice.setText(sb);
            }
//            }

            holder.tvNewPrice.setVisibility(View.GONE);
            holder.tvPrice.setPaintFlags(0);
//            holder.tvPrice.setPadding(0, 0, 0, 0);
            holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.black));
            try {
                if (cartItem.getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(cartItem.getSpecial_price()))) {

                    if (!TextUtils.isEmpty(cartItem.getSpecial_from_date()) && !TextUtils.isEmpty(cartItem.getSpecial_to_date())) {

                        Date fromDate = sdfDate.parse(cartItem.getSpecial_from_date());
                        Date toDate = sdfDate.parse(cartItem.getSpecial_to_date());
                        Date currentDate = new Date();

                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {

//                holder.tvNewPrice.setText(value.getSpecial_price() + " " + mCurrencyCode);
//                            holder.tvNewPrice.setText(Utils.getIntPrice(mExchangeRate, cartItem.getSpecial_price()) + " " + mCurrencyCode);
                            SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, cartItem.getSpecial_price()));
                            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), mCurrencyCode.length() + 1, mCurrencyCode.length() + Utils.getIntPrice(mExchangeRate, cartItem.getSpecial_price()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            } else {
                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mCurrencyCode.length() + 1, mCurrencyCode.length() + Utils.getIntPrice(mExchangeRate, cartItem.getSpecial_price()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            }
                            holder.tvNewPrice.setText(sb);
                            holder.tvNewPrice.setVisibility(View.VISIBLE);

//                            if (cartItem.getPrice() != null && !TextUtils.isEmpty(String.valueOf(cartItem.getPrice()))) {
//                                holder.tvPrice.setText(Utils.getIntPrice(mExchangeRate, cartItem.getPrice()) + "");
//                            } else {
//                                holder.tvPrice.setText("" + cartItem.getPrice() + " ");
//                            }
                            holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.gray_66));
                            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


//            if (cartItem.getProductType().equalsIgnoreCase("configurable") && cartItem.getOption_lable().contains("Size")) {
            if (cartItem.getProductType().equalsIgnoreCase("configurable")) {
                holder.tvSize.setVisibility(View.VISIBLE);
                holder.tvSizeValue.setVisibility(View.VISIBLE);
                List<ShoppingBag.ConfigureOption> mConfigureOptionList = cartItem.getConfigureOptionList();
                if (mConfigureOptionList != null && mConfigureOptionList.size() > 0) {
                    for (int i = 0; i < mConfigureOptionList.size(); i++) {
                        if (mConfigureOptionList.get(i).getOption_label().contains("Size")) {
//                        if (cartItem.getOption_lable().contains("Size")) {
//                            holder.tvSize.setText("" + mContext.getResources().getString(R.string.size)
//                                    + ":" + mConfigureOptionList.get(i).getOption_value());
//                            cartItem.setOption_value(mConfigureOptionList.get(i).getOption_value());

//                            SpannableStringBuilder sb = new SpannableStringBuilder(mContext.getResources().getString(R.string.size)
//                                    + " : " + mConfigureOptionList.get(i).getOption_value());
////                SpannableStringBuilder sb = new SpannableStringBuilder(mContext.getResources().getString(R.string.size)
////                        + " : " + cartItem.getOption_value());
//                            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), mContext.getResources().getString(R.string.size).length() + 1,
//                                        sb.length(),
//                                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                            } else {
//                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mContext.getResources().getString(R.string.size).length() + 1,
//                                        sb.length(),
//                                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//                            }
//
//                            holder.tvSizeValue.setText(sb);
                            holder.tvSizeValue.setText(mConfigureOptionList.get(i).getOption_value());
                        }
                    }

                }else{
                    holder.tvSize.setVisibility(View.INVISIBLE);
                    holder.tvSizeValue.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.tvSize.setVisibility(View.INVISIBLE);
                holder.tvSizeValue.setVisibility(View.INVISIBLE);
            }


//            final String imageUrl = RestClient.IMAGE_URL + cartItem.getImageFIle();
            final String imageUrl = cartItem.getImageFIle();
            LogUtils.e("", "imageUrl::" + imageUrl);

            Glide.with(mContext)
                    .load(imageUrl)
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

            holder.tbDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    holder.hsvMainView.scrollTo(0, 0);

                    String sku = (!TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getParent_sku() : cartItem.getSku());

                    LogUtils.e("", "sku::" + sku);

                    Bundle b = new Bundle();
                    b.putString(BNDL_SKU, "" + sku);
                    b.putBoolean(BNDL_IS_FROM_SEARCH, false);

                    HomeActivity activity = (HomeActivity) mContext;
                    activity.loadProductDetails(b);
                }
            });


//            holder.btnMinus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String qty = holder.tvQty.getText().toString();
//                    if (!TextUtils.isEmpty(qty) && TextUtils.isDigitsOnly(qty)) {
//                        int quantity = Integer.parseInt(qty);
//                        if (quantity > 1) {
//                            quantity = quantity - 1;
//                            setQuantitylist(holder, cartItem, quantity, position, "minus");
//
////                        holder.tvQty.setText("" + quantity);
////                        cartItem.setQty(quantity);
////                        mOnPlusMinusListener.onMinusClick(cartItem.getSku(), position, quantity);
//
//                        }
//                    }
//                }
//            });
//
//            holder.btnPlus.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String qty = holder.tvQty.getText().toString();
//                    if (!TextUtils.isEmpty(qty) && TextUtils.isDigitsOnly(qty)) {
//                        int quantity = Integer.parseInt(qty);
//                        quantity = quantity + 1;
//                        setQuantitylist(holder, cartItem, quantity, position, "plus");
//
////                    holder.tvQty.setText("" + quantity);
////                    cartItem.setQty(quantity);
////                    mOnPlusMinusListener.onPlusClick(cartItem.getSku(), position, quantity);
//                    }
//                }
//            });

            holder.tvSaveLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    closeSwipe(holder.swipeLayout);

                    String sku = (!TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getParent_sku() : cartItem.getSku());
                    String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
                    if (TextUtils.isEmpty(userToken)) {
                        Utils.showToast(mContext, "" + mContext.getString(R.string.msg_need_login));
                    } else {
                        if (cartItem.isWishListItem()) {

                            cartItem.setWishListItem(false);
                            for (int i = 0; i < mCartItemsList.size(); i++) {
                                ShoppingBag.Item mLocalCartItem = mCartItemsList.get(i);
                                if (mLocalCartItem != null) {
                                    String mLocalsku = (!TextUtils.isEmpty(mLocalCartItem.getParent_sku()) ? mLocalCartItem.getParent_sku() : mLocalCartItem.getSku());
                                    if (mLocalsku.equals(sku)) {
                                        mLocalCartItem.setWishListItem(false);
                                    }
                                }
                            }

//                            String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
                            if (TextUtils.isEmpty(userToken) && dbAdapter.isGuestWishItem(sku)) {
                                dbAdapter.removeWishItem(sku);
                            } else {
                                dbAdapter.updateSoftDeleteWishItem(sku, "1");
                            }

//                        Utils.showToast(mContext, "" + mContext.getResources().getString(R.string.product_removed));
//                        holder.tvSaveLater.setBackgroundResource(R.drawable.ic_cart_save);
                            holder.tvSaveLater.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_wishlist_white, 0, 0);

                            if (mContext instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) mContext;
                                activity.showWishListMsg(mContext.getResources().getString(R.string.msg_item_removed));
                            }


                        } else {

                            cartItem.setWishListItem(true);
                            for (int i = 0; i < mCartItemsList.size(); i++) {
                                ShoppingBag.Item mLocalCartItem = mCartItemsList.get(i);
                                if (mLocalCartItem != null) {
                                    String mLocalsku = (!TextUtils.isEmpty(mLocalCartItem.getParent_sku()) ? mLocalCartItem.getParent_sku() : mLocalCartItem.getSku());
                                    if (mLocalsku.equals(sku)) {
                                        mLocalCartItem.setWishListItem(true);
                                    }
                                }
                            }
//                            String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");

                            Wishlist.WishlistData mWishItem = new Wishlist().new WishlistData();
                            mWishItem.setId("" + cartItem.getItemId());
                            mWishItem.setName(cartItem.getName());
                            mWishItem.setDescription("");
                            mWishItem.setSku(sku);
                            mWishItem.setProductId("" + cartItem.getItemId());
                            mWishItem.setTypeId(cartItem.getProductType());
                            mWishItem.setQty(cartItem.getQty());
                            if (cartItem.getPrice() != null && !TextUtils.isEmpty(String.valueOf(cartItem.getPrice()))) {
                                mWishItem.setPrice(cartItem.getPrice());
                            }
                            if (cartItem.getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(cartItem.getSpecial_price()))) {
                                mWishItem.setSpecial_price(cartItem.getSpecial_price());
                            }
                            mWishItem.setSpecial_from_date(cartItem.getSpecial_from_date());
                            mWishItem.setSpecial_to_date(cartItem.getSpecial_to_date());
                            mWishItem.setThumbNail(imageUrl);
                            mWishItem.setBrand(cartItem.getBrand());
                            mWishItem.setRating(0);
                            mWishItem.setStockStatus(cartItem.getStockStatus());
                            mWishItem.setStockQty(cartItem.getStockQty());

                            if (!TextUtils.isEmpty(userToken)) {
                                mWishItem.setIsGuest("0");
                                mWishItem.setToken(userToken);

                            } else {
                                mWishItem.setIsGuest("1");
                                String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
                                mWishItem.setToken(guestToken);
                            }

                            mWishItem.setIs_on_server("0");
                            mWishItem.setSoft_delete("0");


                            String size = "";
                            if (cartItem.getProductType().equalsIgnoreCase("configurable")) {
                                List<ShoppingBag.ConfigureOption> mConfigureOptionList = cartItem.getConfigureOptionList();
                                if (mConfigureOptionList != null && mConfigureOptionList.size() > 0) {
                                    for (int i = 0; i < mConfigureOptionList.size(); i++) {
                                        if (mConfigureOptionList.get(i).getOption_label().contains("Size")) {
                                            size = mConfigureOptionList.get(i).getOption_value();
                                        }
                                    }
                                }
                            }

                            mWishItem.setSize(size);

                            if (dbAdapter.isContainInWishList(sku)) {
                                dbAdapter.updateSoftDeleteWishItem(sku, "0");
                            } else {
                                dbAdapter.addWishItem(mWishItem);
                            }
//                    tbWishlist.setOnCheckedChangeListener(null);
//                    tbWishlist.setEnabled(false);

//                        Utils.showToast(mContext, "" + mContext.getResources().getString(R.string.msg_add_wishlist));
                            if (mContext instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) mContext;
                                activity.showWishListMsg(mContext.getResources().getString(R.string.msg_item_saved));
                            }

//                        holder.tvSaveLater.setBackgroundResource(R.drawable.ic_cart_save_select);
                            holder.tvSaveLater.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_wishlist_red, 0, 0);

                            holder.lnrDelete.performClick();

                            try {
                                AdjustEvent event = new AdjustEvent("wxnr5r");
                                event.addPartnerParameter("Name", "" + "" + cartItem.getName());
                                if (cartItem.getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(cartItem.getSpecial_price()))) {
                                    event.addPartnerParameter("Price", "" + "" + cartItem.getSpecial_price());
                                } else {
                                    event.addPartnerParameter("Price", "" + "" + cartItem.getPrice());
                                }
                                event.addPartnerParameter("Currency", "" + "" + MyPref.getPref(mContext, MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, ""));
                                event.addPartnerParameter("Product Type", "" + cartItem.getProductType());
                                event.addPartnerParameter("SKU", "" + "" + sku);

                                //Callback
                                event.addCallbackParameter("Name", "" + "" + cartItem.getName());
                                if (cartItem.getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(cartItem.getSpecial_price()))) {
                                    event.addCallbackParameter("Price", "" + "" + cartItem.getSpecial_price());
                                } else {
                                    event.addCallbackParameter("Price", "" + "" + cartItem.getPrice());
                                }
                                event.addCallbackParameter("Currency", "" + "" + MyPref.getPref(mContext, MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, ""));
                                event.addCallbackParameter("Product Type", "" + cartItem.getProductType());
                                event.addCallbackParameter("SKU", "" + "" + sku);

                                String mCriteo = MyPref.getPref(mContext, MyPref.APP_CRITEO, "");
                                if (TextUtils.isEmpty(mCriteo) || mCriteo.equalsIgnoreCase("1")) {
                                    AdjustCriteo.injectCustomerIdIntoCriteoEvents(TextUtils.isEmpty(userToken) ? userToken : MyPref.getPref(mContext, MyPref.GUEST_CART_ID, ""));
                                }

                                Adjust.trackEvent(event);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        notifyDataSetChanged();
                        if (mContext instanceof HomeActivity) {
                            ((HomeActivity) mContext).updateWishListBadgetCount();
                        }
                    }

//                String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//                if (!TextUtils.isEmpty(userToken)) {
//
//                    String sku = (!TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getParent_sku() : cartItem.getSku());
//
//                    LogUtils.e("", "sku::" + sku);
//
//                    addProducToWishlist("" + sku, position, cartItem);
//                } else {
//                    Utils.showToast(mContext, "" + mContext.getString(R.string.msg_need_login));
//                }


                }
            });

            holder.lnrSaveForLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    holder.hsvMainView.scrollTo(0, 0);

                    holder.tvSaveLater.performClick();
                }
            });

            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.tvRemove.performClick();
                }
            });

            holder.lnrDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCartItemsList.remove(position);
                    notifyDataSetChanged();
//                    holder.hsvMainView.scrollTo(0, 0);
                    if (mCartItemsList != null && mCartItemsList.size() == 0) {
                        shoppingBagsFragment.showEmptyScreen();
                    }
                    removeItemFromCart("" + cartItem.getItemId(), position);

//                    dbAdapter.softDeleteShoppingItem(TextUtils.isEmpty(cartItem.getParent_sku()) ?
//                            cartItem.getSku() : cartItem.getParent_sku(), cartItem.getProductType(), cartItem.getOption_value());
//                    LogUtils.e("", "cartItem getItemId :" + cartItem.getItemId());
//                    LogUtils.e("", "cartItem getIs_on_server :" + cartItem.getIs_on_server());
//                    if (cartItem.getIs_on_server().equalsIgnoreCase("1")) {
//                        removeItemFromCart(cartItem, position);
//                    }
////                    if (dbAdapter.isShoppingItemOnServer(TextUtils.isEmpty(cartItem.getParent_sku()) ?
////                            cartItem.getSku() : cartItem.getParent_sku(), cartItem.getProductType(), cartItem.getOption_value())) {
////
////                        removeItemFromCart(cartItem, position);
////                    } else {
////                        dbAdapter.deleteShoppingItem(TextUtils.isEmpty(cartItem.getParent_sku()) ?
////                                cartItem.getSku() : cartItem.getParent_sku(), cartItem.getProductType(), cartItem.getOption_value());
////                    }
//
//                    shoppingBagsFragment.getShoppingListFromDB();

                }
            });

            holder.tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View tvview) {

                    closeSwipe(holder.swipeLayout);

                    TranslateAnimation animate = new TranslateAnimation(0, -holder.lnrMainView.getWidth(), 0, 0);
                    animate.setDuration(500);
                    animate.setFillAfter(true);
                    holder.lnrMainView.startAnimation(animate);
                    animate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            holder.lnrMainView.clearAnimation();
//                            mCartItemsList.remove(position);
//                            notifyDataSetChanged();
//                            if (mCartItemsList != null && mCartItemsList.size() == 0) {
//                                shoppingBagsFragment.showEmptyScreen();
//                            }
//                            removeItemFromCart("" + cartItem.getItemId(), position);
                            holder.lnrDelete.performClick();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
//
                }
            });

            holder.tvQtyValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.tvQty.performClick();
                }
            });

            holder.tvQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showQtyDialog(holder, cartItem, position);
                }
            });

        }

        return view;
    }

    private class ViewHolder {
        ImageView ivProductImage;
        TableRow tbDetails;
        LinearLayout lnrDelete, lnrSaveForLater;
        TextView tvTitle, tvName, tvPrice, tvNewPrice, tvQty, tvQtyValue, tvSize, tvSizeValue, tvSaveLater, tvRemove, tvDelete, tvMsg, tvExclusivePrice;
        LinearLayout lnrMainView;
        //        HorizontalScrollView hsvMainView;
        SwipeLayout swipeLayout;
    }

//    private void removeItemFromCart(final ShoppingBag.Item cartItem, final int position) {
//
//        if (!Utils.isInternetConnected(mContext)) {
//            return;
//        }
//
//        String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
//        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//        Call<ResponseBody> call;
//
//        SharedPreferences pref = mContext.getSharedPreferences(NotificationUtils.SHARED_PREF, 0);
//
//        if (!TextUtils.isEmpty(userToken)) {
//
//            JSONObject jsonParams = new JSONObject();
//            try {
//                jsonParams.put("item_id", "" + cartItem.getItemId());
//                jsonParams.put("device_token", "" + pref.getString("regId", ""));
//                jsonParams.put("subcribe_id", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_USER_ID, ""));
////                jsonParams.put("device_token", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//            call = apiService.deleteItemFromUserCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
//        } else {
//
//            JSONObject jsonParams = new JSONObject();
//            try {
//                jsonParams.put("cart_id", "" + guestToken);
//                jsonParams.put("item_id", "" + cartItem.getItemId());
//                jsonParams.put("device_token", "" + pref.getString("regId", ""));
//                jsonParams.put("subcribe_id", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_USER_ID, ""));
////                jsonParams.put("device_token", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//            call = apiService.deleteItemFromGuestCart(Shy7lo.mLangCode, body);
//        }
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "response code:" + response.code());
//
//                if (response.isSuccessful()) {
//                    dbAdapter.deleteShoppingItem(TextUtils.isEmpty(cartItem.getParent_sku()) ?
//                            cartItem.getSku() : cartItem.getParent_sku(), cartItem.getProductType(), cartItem.getOption_value());
//                    shoppingBagsFragment.getTotalAmount();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                System.out.println(t.getMessage());
//            }
//        });
//    }

    private void removeItemFromCart(final String itemId, final int position) {

        if (!Utils.isInternetConnected(mContext)) {
            Utils.showOfflineMsgDialog(mContext, new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(mContext)) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        removeItemFromCart(itemId, position);
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(mContext);

        String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<ResponseBody> call;

        SharedPreferences pref = mContext.getSharedPreferences(NotificationUtils.SHARED_PREF, 0);

        if (!TextUtils.isEmpty(userToken)) {

            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("item_id", "" + itemId);
                jsonParams.put("device_token", "" + pref.getString("regId", ""));
                jsonParams.put("subcribe_id", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_USER_ID, ""));
//                jsonParams.put("device_token", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());

            call = apiService.deleteItemFromUserCart(Shy7lo.mLangCode, "Bearer " + userToken, body);
        } else {

            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("cart_id", "" + guestToken);
                jsonParams.put("item_id", "" + itemId);
                jsonParams.put("device_token", "" + pref.getString("regId", ""));
                jsonParams.put("subcribe_id", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_USER_ID, ""));
//                jsonParams.put("device_token", "" + MyPref.getPref(mContext, MyPref.ONE_SIGNAL_PUSH_TOKEN, ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());

            call = apiService.deleteItemFromGuestCart(Shy7lo.mLangCode, body, guestToken);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "response code:" + response.code());

                if (response.isSuccessful()) {
                    try {

                        shoppingBagsFragment.getShoppingBag();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.closeProgressDialog();
                    }

                } else {
                    Utils.closeProgressDialog();
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(mContext, "" + response.code());
                    } else {

                        try {
                            JSONObject jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(mContext, "" + jResponse.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                    Utils.showAlertDialog(mContext, "" + response.code());
//                    if (response.code() == 404) {
//                        Utils.showToast(mContext, "Item not available in cart");
//                    } else {
//                        Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
//                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
                Utils.closeProgressDialog();
                Utils.showAlertDialog(mContext, "" + t.getMessage());
            }
        });
    }

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

//    private void addProducToWishlist(final String sku, final int position, final ShoppingBag.Item cartItem) {
//
//        if (!Utils.isInternetConnected(mContext)) {
//            Utils.showOfflineMsgDialog(mContext, new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(mContext)) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        addProducToWishlist(sku, position, cartItem);
//                    }
//                }
//            });
//            return;
//        }
//
//        Utils.showProgressDialog(mContext);
//
//        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//
//        JSONObject jsonParams = new JSONObject();
//        try {
//            jsonParams.put("sku", "" + sku);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//        Call<JsonElement> call = apiService.addProductToWishlist(Shy7lo.mLangCode, "Bearer " + userToken, body);
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                if (response.isSuccessful()) {
//                    if (RestClient.isTuneEnable) {
//                        try {
//                            Tune tune = Tune.getInstance();
//                            List<TuneEventItem> mTuneEventItemsList = new ArrayList<TuneEventItem>();
//                            TuneEventItem mTuneEventItem = new TuneEventItem("" + cartItem.getName());
//                            mTuneEventItemsList.add(mTuneEventItem);
//                            tune.measureEvent(new TuneEvent(TuneEvent.ADD_TO_WISHLIST)
//                                    .withEventItems(mTuneEventItemsList)
//                                    .withQuantity(1)
//                                    .withContentId("" + sku)
//                                    .withRevenue(0)
//                                    .withContentType(cartItem.getProductType().equalsIgnoreCase("configurable") ? "Configurable" : "Simple")
//                                    .withCurrencyCode("" + MyPref.getPref(mContext, MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "")));
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    Utils.showToast(mContext, "" + mContext.getString(R.string.msg_add_wishlist));
//                } else {
//                    Utils.showAlertDialog(mContext);
//                }
//                Utils.closeProgressDialog();
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Utils.closeProgressDialog();
//                Utils.showAlertDialog(mContext);
////                Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
//            }
//        });
//    }

//    private void setQuantitylist(final ViewHolder holder, final ShoppingBag.Item cartItem, final int quantity, final int position, final String type) {
//
//        if (type.equalsIgnoreCase("minus")) {
//
//            holder.tvQty.setText("" + quantity);
//            cartItem.setQty(quantity);
//            mOnPlusMinusListener.onMinusClick(cartItem.getSku(), position, quantity);
//
//
//        } else if (type.equalsIgnoreCase("plus")) {
//
//            holder.tvQty.setText("" + quantity);
//            cartItem.setQty(quantity);
//            mOnPlusMinusListener.onPlusClick(cartItem.getSku(), position, quantity);
//        }
//        dbAdapter.updateQtyShoppingItem(quantity, TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getSku() : cartItem.getParent_sku(),
//                cartItem.getProductType(), cartItem.getOption_value());
//
//        if (!Utils.isInternetConnected(mContext)) {
//            return;
//        }
//
//        String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
//        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//
//        Call<JsonElement> call;
//        if (!TextUtils.isEmpty(userToken)) {
//
//            JSONObject jsonParams = new JSONObject();
//            try {
//                jsonParams.put("item_id", "" + cartItem.getItemId());
//                jsonParams.put("sku", "" + cartItem.getSku());
//                jsonParams.put("qty", "" + quantity);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//            call = apiService.setUserQuantityItem(Shy7lo.mLangCode, "Bearer " + userToken, body);
//        } else {
//
//            JSONObject jsonParams = new JSONObject();
//            try {
//                jsonParams.put("cart_id", "" + guestToken);
//                jsonParams.put("item_id", "" + cartItem.getItemId());
//                jsonParams.put("sku", "" + cartItem.getSku());
//                jsonParams.put("qty", "" + quantity);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//            call = apiService.setGuestQuantityItem(Shy7lo.mLangCode, body);
//        }
//
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                try {
//                    if (response.isSuccessful()) {
//
//                        JSONObject jResponse = new JSONObject(response.body().toString());
//
//                        if (jResponse != null && jResponse.getString("success").equals("1")) {
//
//                        } else if (jResponse != null && jResponse.getString("success").equals("0")) {
//
////                            Utils.showToast(mContext, "" + mContext.getString(R.string.msg_max_quantity));
//                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
////                            Utils.showInitialScreen(mContext);
////                            return;
//                        } else {
////                            Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
//                        }
//                        if (jResponse != null && (jResponse.getString("success").equals("0") || jResponse.getString("success").equals("2"))) {
//                            if (type.equalsIgnoreCase("minus")) {
//                                int previousQuantity = quantity + 1;
//                                holder.tvQty.setText("" + previousQuantity);
//                                cartItem.setQty(previousQuantity);
//                                dbAdapter.updateQtyShoppingItem(previousQuantity, TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getSku() : cartItem.getParent_sku(),
//                                        cartItem.getProductType(), cartItem.getOption_value());
//
//                            } else if (type.equalsIgnoreCase("plus")) {
//                                int previousQuantity = quantity - 1;
//                                holder.tvQty.setText("" + previousQuantity);
//                                cartItem.setQty(previousQuantity);
//                                dbAdapter.updateQtyShoppingItem(previousQuantity, TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getSku() : cartItem.getParent_sku(),
//                                        cartItem.getProductType(), cartItem.getOption_value());
//                            }
//                        }
//
//
//                    } else {
//
//                        if (response.code() == 400) {
//                            if (type.equalsIgnoreCase("minus")) {
//                                int previousQuantity = quantity + 1;
//                                holder.tvQty.setText("" + previousQuantity);
//                                cartItem.setQty(previousQuantity);
//                                dbAdapter.updateQtyShoppingItem(previousQuantity, TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getSku() : cartItem.getParent_sku(),
//                                        cartItem.getProductType(), cartItem.getOption_value());
//
//                            } else if (type.equalsIgnoreCase("plus")) {
//                                int previousQuantity = quantity - 1;
//                                holder.tvQty.setText("" + previousQuantity);
//                                cartItem.setQty(previousQuantity);
//                                dbAdapter.updateQtyShoppingItem(previousQuantity, TextUtils.isEmpty(cartItem.getParent_sku()) ? cartItem.getSku() : cartItem.getParent_sku(),
//                                        cartItem.getProductType(), cartItem.getOption_value());
//                            }
//                            Utils.showToast(mContext, "" + mContext.getString(R.string.msg_max_quantity));
////                            JSONObject jResponse = new JSONObject(response.errorBody().string());
////                            if (jResponse != null && jResponse.getString("success").equals("0")) {
////                                Utils.showToast(mContext, "" + mContext.getString(R.string.msg_max_quantity));
////                            }
//                        } else {
////                            Utils.showAlertDialog(mContext);
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//
//            }
//        });
//    }

    private void showQtyDialog(final ViewHolder holder, final ShoppingBag.Item cartItem, final int position) {

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
        for (int i = 0; i < cartItem.getStockQty(); i++) {
            int qty = i + 1;
            data.add("" + qty);
            if (qty == cartItem.getQty()) {
                mItemPosition = i;
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
                if (qty != cartItem.getQty()) {
                    setQuantitylist(holder, cartItem, qty, position);
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

    private void setQuantitylist(final ViewHolder holder, final ShoppingBag.Item cartItem,
                                 final int quantity, final int position) {

        if (!Utils.isInternetConnected(mContext)) {
            Utils.showOfflineMsgDialog(mContext, new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(mContext)) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        setQuantitylist(holder, cartItem, quantity, position);
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(mContext);

        String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Call<JsonElement> call;
        if (!TextUtils.isEmpty(userToken)) {

            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("item_id", "" + cartItem.getItemId());
                jsonParams.put("sku", "" + cartItem.getSku());
                jsonParams.put("qty", "" + quantity);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());

            call = apiService.setUserQuantityItem(Shy7lo.mLangCode, "Bearer " + userToken, body);
        } else {

            JSONObject jsonParams = new JSONObject();
            try {
//                jsonParams.put("cart_id", "" + guestToken);
                jsonParams.put("item_id", "" + cartItem.getItemId());
                jsonParams.put("sku", "" + cartItem.getSku());
                jsonParams.put("qty", "" + quantity);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());

            call = apiService.setGuestQuantityItem(Shy7lo.mLangCode, body, guestToken);
        }

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Utils.closeProgressDialog();

                try {
                    if (response.isSuccessful()) {

                        JSONObject jResponse = new JSONObject(response.body().toString());

                        if (jResponse != null && jResponse.getString("success").equals("1")) {

                            holder.tvQtyValue.setText("" + quantity);
                            cartItem.setQty(quantity);
                            mOnPlusMinusListener.onQtyClick(cartItem.getSku(), position, quantity);

                        } else if (jResponse != null && jResponse.getString("success").equals("0")) {
                            Utils.showToast(mContext, "" + mContext.getString(R.string.msg_max_quantity));
                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
                            Utils.showInitialScreen(mContext);
                            return;
                        } else {
                            Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
                        }


                    } else {

                        if (String.valueOf(response.code()).startsWith("5")) {
                            Utils.showAlertDialog(mContext, "" + response.code());
                        } else {

                            try {
                                JSONObject jResponse = new JSONObject(response.errorBody().string());
                                if (jResponse != null && jResponse.getString("success").equals("0")) {
                                    Utils.showToast(mContext, "" + jResponse.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

//                        if (response.code() == 400) {
//                            JSONObject jResponse = new JSONObject(response.errorBody().string());
//                            if (jResponse != null && jResponse.getString("success").equals("0")) {
//                                Utils.showToast(mContext, response.code() + " " + mContext.getString(R.string.msg_max_quantity));
//                            }
//                        } else {
//                            Utils.showAlertDialog(mContext, "" + response.code());
//                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showAlertDialog(mContext, "" + response.code());
//                    Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
                }


            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Utils.closeProgressDialog();
                Utils.showAlertDialog(mContext, "" + t.getMessage());
//                Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
            }
        });
    }

//    private void setQuantitylist(final ViewHolder holder, final ShoppingBag.Item cartItem,
//                                 final int quantity, final int position, final String type) {
//
//        if (!Utils.isInternetConnected(mContext)) {
//            Utils.showOfflineMsgDialog(mContext, new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(mContext)) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        setQuantitylist(holder, cartItem, quantity, position, type);
//                    }
//                }
//            });
//            return;
//        }
//
//        Utils.showProgressDialog(mContext);
//
//        String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
//        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API_SHYLABS_URL, true);
//
//        Call<JsonElement> call;
//        if (!TextUtils.isEmpty(userToken)) {
//
//            JSONObject jsonParams = new JSONObject();
//            try {
//                jsonParams.put("item_id", "" + cartItem.getItemId());
//                jsonParams.put("sku", "" + cartItem.getSku());
//                jsonParams.put("qty", "" + quantity);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//            call = apiService.setUserQuantityItem(Shy7lo.mLangCode, "Bearer " + userToken, body);
//        } else {
//
//            JSONObject jsonParams = new JSONObject();
//            try {
//                jsonParams.put("cart_id", "" + guestToken);
//                jsonParams.put("item_id", "" + cartItem.getItemId());
//                jsonParams.put("sku", "" + cartItem.getSku());
//                jsonParams.put("qty", "" + quantity);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams.toString());
//
//            call = apiService.setGuestQuantityItem(Shy7lo.mLangCode, body);
//        }
//
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                Utils.closeProgressDialog();
//
//                try {
//                    if (response.isSuccessful()) {
//
//                        JSONObject jResponse = new JSONObject(response.body().toString());
//
//                        if (jResponse != null && jResponse.getString("success").equals("1")) {
//
//                            if (type.equalsIgnoreCase("minus")) {
//
//                                holder.tvQtyValue.setText("" + quantity);
//                                cartItem.setQty(quantity);
//                                mOnPlusMinusListener.onMinusClick(cartItem.getSku(), position, quantity);
//
//                            } else if (type.equalsIgnoreCase("plus")) {
//
//                                holder.tvQtyValue.setText("" + quantity);
//                                cartItem.setQty(quantity);
//                                mOnPlusMinusListener.onPlusClick(cartItem.getSku(), position, quantity);
//
//                            }
//
//                        } else if (jResponse != null && jResponse.getString("success").equals("0")) {
//                            Utils.showToast(mContext, "" + mContext.getString(R.string.msg_max_quantity));
//                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
//                            Utils.showInitialScreen(mContext);
//                            return;
//                        } else {
//                            Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
//                        }
//
//
//                    } else {
//
//                        if (response.code() == 400) {
//                            JSONObject jResponse = new JSONObject(response.errorBody().string());
//                            if (jResponse != null && jResponse.getString("success").equals("0")) {
//                                Utils.showToast(mContext, "" + mContext.getString(R.string.msg_max_quantity));
//                            }
//                        } else {
//                            Utils.showAlertDialog(mContext);
//                        }
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Utils.showAlertDialog(mContext);
////                    Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                Utils.closeProgressDialog();
//                Utils.showAlertDialog(mContext);
////                Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
//            }
//        });
//    }

    public void showUpgradeDialog() {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_extra_items);


        Button btnRemove = (Button) dialog.findViewById(R.id.btnRemove);
        TextView tvExtraText = (TextView) dialog.findViewById(R.id.tvExtraText);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

                removeUnavailableItemFromCart();

            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent keyEvent) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        dialog.show();

    }

    private void removeUnavailableItemFromCart() {

        if (!Utils.isInternetConnected(mContext)) {
            Utils.showOfflineMsgDialog(mContext, new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(mContext)) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        removeUnavailableItemFromCart();
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(mContext);

        String guestToken = MyPref.getPref(mContext, MyPref.GUEST_CART_ID, "");
        String userToken = MyPref.getPref(mContext, MyPref.USER_CART_ID, "");

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<ResponseBody> call;

        SharedPreferences pref = mContext.getSharedPreferences(NotificationUtils.SHARED_PREF, 0);

        if (!TextUtils.isEmpty(userToken)) {

            call = apiService.removeUnavailalbeItemsUser(Shy7lo.mLangCode, "Bearer " + userToken);
        } else {
            call = apiService.removeUnavailalbeItemsGuest(Shy7lo.mLangCode, guestToken);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                LogUtils.e(TAG_SHOPPING_BAGS_FRAGMENT, "response code:" + response.code());

                if (response.isSuccessful()) {
                    try {

//                        shoppingBagsFragment.getShoppingBag();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.closeProgressDialog();
                        Utils.showAlertDialog(mContext, "" + response.code());
                    }

                } else {
                    Utils.closeProgressDialog();
//                    Utils.showAlertDialog(mContext, "" + response.code());
//                    Utils.showToast(mContext, "" + mContext.getString(R.string.msg_something_wrong));
                    if (String.valueOf(response.code()).startsWith("5")) {
                        Utils.showAlertDialog(mContext, "" + response.code());
                    } else {

                        try {
                            JSONObject jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(mContext, "" + jResponse.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
                Utils.closeProgressDialog();
                Utils.showAlertDialog(mContext, "" + t.getMessage());
            }
        });
    }
}

