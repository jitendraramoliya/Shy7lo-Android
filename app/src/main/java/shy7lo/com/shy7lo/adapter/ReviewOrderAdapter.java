package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.ShoppingBag;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.CustomTypefaceSpan;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.R.id.tvTitle;

/**
 * Created by JITEN-PC on 17-03-2017.
 */

public class ReviewOrderAdapter extends BaseAdapter {

//    Context mContext;
//    List<ReviewOrder.Item> mOrderItemList;
//    private String mCurrencyCode = "";
//    private float mExchangeRate;
//    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    public ReviewOrderAdapter(Context mContext, List<ReviewOrder.Item> mOrderItemList) {
//        this.mContext = mContext;
//        this.mOrderItemList = mOrderItemList;
//        mCurrencyCode = MyPref.getPref(mContext, MyPref.DEFAULT_CURRENCY_CODE, mContext.getResources().getString(R.string.SAR));
//        mExchangeRate = MyPref.getPref(mContext, MyPref.DEFAULT_EXCHANGE_RATE, 1f);
//    }
//
//    @Override
//    public int getCount() {
//        return mOrderItemList.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return mOrderItemList.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int position, View view, ViewGroup viewGroup) {
//
//        if (view == null) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_review_order, null);
//            ReviewOrderAdapter.ViewHolder holder = new ReviewOrderAdapter.ViewHolder();
//            holder.ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
//            holder.tvTitle = (TextView) view.findViewById(tvTitle);
//            holder.tvName = (TextView) view.findViewById(R.id.tvName);
//            holder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
//            holder.tvOldPrice = (TextView) view.findViewById(R.id.tvOldPrice);
//            holder.tvSpecialPrice = (TextView) view.findViewById(R.id.tvSpecialPrice);
//            holder.tvSize = (TextView) view.findViewById(R.id.tvSize);
//            holder.tvQty = (TextView) view.findViewById(R.id.tvQty);
//            view.setTag(holder);
//        }
//
//        final ReviewOrderAdapter.ViewHolder holder = (ReviewOrderAdapter.ViewHolder) view.getTag();
//
//        ReviewOrder.Item mReviewOrder = mOrderItemList.get(position);
//
//        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            holder.ivProductImage.setScaleX(-1f);
//            holder.tvTitle.setScaleX(-1f);
//            holder.tvName.setScaleX(-1f);
//            holder.tvPrice.setScaleX(-1f);
//            holder.tvOldPrice.setScaleX(-1f);
//            holder.tvSpecialPrice.setScaleX(-1f);
//            holder.tvSize.setScaleX(-1f);
//            holder.tvQty.setScaleX(-1f);
//
//            holder.tvTitle.setGravity(Gravity.RIGHT);
//            holder.tvName.setGravity(Gravity.RIGHT);
//        } else {
//            holder.ivProductImage.setScaleX(1f);
//            holder.tvTitle.setScaleX(1f);
//            holder.tvName.setScaleX(1f);
//            holder.tvPrice.setScaleX(1f);
//            holder.tvOldPrice.setScaleX(1f);
//            holder.tvSpecialPrice.setScaleX(1f);
//            holder.tvSize.setScaleX(1f);
//            holder.tvQty.setScaleX(1f);
//            holder.tvTitle.setGravity(Gravity.LEFT);
//            holder.tvName.setGravity(Gravity.LEFT);
//        }
//
//        if (mReviewOrder != null) {
//
//            holder.tvTitle.setText("" + mReviewOrder.getName());
//            if (!TextUtils.isEmpty(mReviewOrder.getBrand()) && !mReviewOrder.getBrand().equalsIgnoreCase("null") ) {
//                holder.tvName.setText("" + mReviewOrder.getBrand());
//            } else {
//                holder.tvName.setText("");
//            }
//
////            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
////                holder.tvPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, mReviewOrder.getPrice()) + " :" + mContext.getResources().getString(R.string.price));
////            } else {
//            holder.tvPrice.setText(mContext.getResources().getString(R.string.price) + ": ");
//
//            holder.tvSpecialPrice.setVisibility(View.GONE);
//            holder.tvOldPrice.setPaintFlags(0);
//
//            holder.tvOldPrice.setText(Utils.getRealPrice(mExchangeRate, mReviewOrder.getPrice()) + " " + mCurrencyCode);
//
//            try {
//                if (mReviewOrder.getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(mReviewOrder.getSpecial_price()))) {
//
//                    if (!TextUtils.isEmpty(mReviewOrder.getSpecial_from_date()) && !TextUtils.isEmpty(mReviewOrder.getSpecial_to_date())) {
//
//                        Date fromDate = sdfDate.parse(mReviewOrder.getSpecial_from_date());
//                        Date toDate = sdfDate.parse(mReviewOrder.getSpecial_to_date());
//                        Date currentDate = new Date();
//
//                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {
//                            holder.tvSpecialPrice.setText(Utils.getRealPrice(mExchangeRate, mReviewOrder.getSpecial_price()) + " " + mCurrencyCode);
//                            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                            holder.tvSpecialPrice.setVisibility(View.VISIBLE);
////                            holder.tvOldPrice.setTextColor(mContext.getResources().getColor(R.color.gray_cc));
//                        }
//
//                    }
//
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
////            }
//            holder.tvQty.setText(mContext.getResources().getString(R.string.quantity) + ": " + mReviewOrder.getQty());
//
//            if (mReviewOrder.getProductType().equalsIgnoreCase("configurable")) {
//                holder.tvSize.setVisibility(View.VISIBLE);
//                List<ReviewOrder.ConfigureOption> mConfigureOptionList = mReviewOrder.getConfigureOptionList();
//                if (mConfigureOptionList != null && mConfigureOptionList.size() > 0) {
//                    for (int i = 0; i < mConfigureOptionList.size(); i++) {
//                        if (mConfigureOptionList.get(i).getOption_label().contains("Size")) {
//                            holder.tvSize.setText("" + mContext.getResources().getString(R.string.size)
//                                    + ": " + mConfigureOptionList.get(i).getOption_value());
//                        }
//                    }
//
//                }
//            } else {
//                holder.tvSize.setVisibility(View.GONE);
//            }
//
////            final String imageUrl = RestClient.IMAGE_URL + mReviewOrder.getImageFIle();
////            final String imageUrl = mReviewOrder.getImageFIle();
//            final String imageUrl = mReviewOrder.getImage();
//            LogUtils.e("", "imageUrl::" + imageUrl);
//
//            Glide.with(mContext)
//                    .load(imageUrl)
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            return false;
//                        }
//                    })
//                    .into(holder.ivProductImage);
//
////            ViewTreeObserver vto = holder.ivProductImage.getViewTreeObserver();
////            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
////                public boolean onPreDraw() {
////                    holder.ivProductImage.getViewTreeObserver().removeOnPreDrawListener(this);
//////                    Picasso.with(mContext).load(imageUrl)
//////                            .resize(holder.ivProductImage.getMeasuredWidth(), holder.ivProductImage.getMeasuredHeight())
//////                            .onlyScaleDown().into(holder.ivProductImage);
////                    Picasso.with(mContext).load(imageUrl)
////                            .into(holder.ivProductImage);
////                    return true;
////                }
////            });
//        }
//
//        return view;
//    }
//
//    private class ViewHolder {
//        ImageView ivProductImage;
//        TextView tvTitle, tvName, tvPrice, tvQty, tvSize, tvOldPrice, tvSpecialPrice;
//    }

    Context mContext;
    List<ShoppingBag.Item> mOrderItemList;
    private String mCurrencyCode = "";
    private float mExchangeRate;
    private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ReviewOrderAdapter(Context mContext, List<ShoppingBag.Item> mOrderItemList) {
        this.mContext = mContext;
        this.mOrderItemList = mOrderItemList;
        mCurrencyCode = MyPref.getPref(mContext, MyPref.DEFAULT_CURRENCY_CODE, mContext.getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(mContext, MyPref.DEFAULT_EXCHANGE_RATE, 1f);
    }

    @Override
    public int getCount() {
        return mOrderItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return mOrderItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_review_order, null);
            ReviewOrderAdapter.ViewHolder holder = new ReviewOrderAdapter.ViewHolder();
            holder.ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
            holder.tvTitle = (TextView) view.findViewById(tvTitle);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvOldPrice = (TextView) view.findViewById(R.id.tvOldPrice);
            holder.tvSpecialPrice = (TextView) view.findViewById(R.id.tvSpecialPrice);
            holder.tvSize = (TextView) view.findViewById(R.id.tvSize);
            holder.tvSizeTitle = (TextView) view.findViewById(R.id.tvSizeTitle);
            holder.tvQty = (TextView) view.findViewById(R.id.tvQty);
            holder.tvQtyTitle = (TextView) view.findViewById(R.id.tvQtyTitle);
            view.setTag(holder);
        }

        final ReviewOrderAdapter.ViewHolder holder = (ReviewOrderAdapter.ViewHolder) view.getTag();

        ShoppingBag.Item mReviewOrder = mOrderItemList.get(position);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.ivProductImage.setScaleX(-1f);
            holder.tvTitle.setScaleX(-1f);
            holder.tvName.setScaleX(-1f);
            holder.tvOldPrice.setScaleX(-1f);
            holder.tvSpecialPrice.setScaleX(-1f);
            holder.tvSize.setScaleX(-1f);
            holder.tvSizeTitle.setScaleX(-1f);
            holder.tvQty.setScaleX(-1f);
            holder.tvQtyTitle.setScaleX(-1f);

            holder.tvTitle.setGravity(Gravity.RIGHT);
            holder.tvName.setGravity(Gravity.RIGHT);
        } else {
            holder.ivProductImage.setScaleX(1f);
            holder.tvTitle.setScaleX(1f);
            holder.tvName.setScaleX(1f);
            holder.tvOldPrice.setScaleX(1f);
            holder.tvSpecialPrice.setScaleX(1f);
            holder.tvSize.setScaleX(1f);
            holder.tvSizeTitle.setScaleX(1f);
            holder.tvQty.setScaleX(1f);
            holder.tvQtyTitle.setScaleX(1f);
            holder.tvTitle.setGravity(Gravity.LEFT);
            holder.tvName.setGravity(Gravity.LEFT);
        }

        if (mReviewOrder != null) {

            String mProductText = "";
            if (!TextUtils.isEmpty(mReviewOrder.getBrand()) && !mReviewOrder.getBrand().equalsIgnoreCase("null")) {
                mProductText = mReviewOrder.getBrand();
            } else {
                mProductText = "";
            }
            int brandLength = mProductText.length();
            if (mProductText.length() > 0) {
                mProductText = mProductText + " " + mReviewOrder.getName();
            } else {
                mProductText = mReviewOrder.getName();
            }
            SpannableStringBuilder sbBrand = new SpannableStringBuilder(mProductText);
            sbBrand.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, brandLength, 0);
            holder.tvName.setText(sbBrand);

//            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                holder.tvPrice.setText(mCurrencyCode + " " + Utils.getRealPrice(mExchangeRate, mReviewOrder.getPrice()) + " :" + mContext.getResources().getString(R.string.price));
//            } else {
//            holder.tvPrice.setText(mContext.getResources().getString(R.string.price) + ": ");
            if (mReviewOrder.getPrice() != null && !TextUtils.isEmpty(String.valueOf(mReviewOrder.getPrice()))) {
//                holder.tvPrice.setText(Utils.getIntPrice(mExchangeRate, cartItem.getPrice()) + " " + mCurrencyCode);

                SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, mReviewOrder.getPrice()));
                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), mCurrencyCode.length(), mCurrencyCode.length() + Utils.getIntPrice(mExchangeRate, mReviewOrder.getPrice()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                } else {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mCurrencyCode.length(), mCurrencyCode.length() + Utils.getIntPrice(mExchangeRate, mReviewOrder.getPrice()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                holder.tvOldPrice.setText(sb);
            } else {
                SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + mReviewOrder.getPrice());
                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), mCurrencyCode.length(), mCurrencyCode.length() + String.valueOf(mReviewOrder.getPrice()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                } else {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mCurrencyCode.length(), mCurrencyCode.length() + String.valueOf(mReviewOrder.getPrice()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                holder.tvOldPrice.setText(sb);
            }

            holder.tvSpecialPrice.setVisibility(View.GONE);
            holder.tvOldPrice.setPaintFlags(0);
            holder.tvOldPrice.setTextColor(mContext.getResources().getColor(R.color.black));
//            holder.tvOldPrice.setText(Utils.getRealPrice(mExchangeRate, mReviewOrder.getPrice()) + " " + mCurrencyCode);


            try {
                if (mReviewOrder.getSpecial_price() != null && !TextUtils.isEmpty(String.valueOf(mReviewOrder.getSpecial_price()))) {

                    if (!TextUtils.isEmpty(mReviewOrder.getSpecial_from_date()) && !TextUtils.isEmpty(mReviewOrder.getSpecial_to_date())) {

                        Date fromDate = sdfDate.parse(mReviewOrder.getSpecial_from_date());
                        Date toDate = sdfDate.parse(mReviewOrder.getSpecial_to_date());
                        Date currentDate = new Date();

                        if (currentDate.after(fromDate) && currentDate.before(toDate)) {

                            SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, mReviewOrder.getSpecial_price()));
                            if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), mCurrencyCode.length() + 1, mCurrencyCode.length() + Utils.getIntPrice(mExchangeRate, mReviewOrder.getSpecial_price()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            } else {
                                sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), mCurrencyCode.length() + 1, mCurrencyCode.length() + Utils.getIntPrice(mExchangeRate, mReviewOrder.getSpecial_price()).length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            }
                            holder.tvSpecialPrice.setText(sb);
                            holder.tvSpecialPrice.setVisibility(View.VISIBLE);

                            holder.tvOldPrice.setTextColor(mContext.getResources().getColor(R.color.gray_66));
                            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

//            }
            holder.tvQty.setText("" + mReviewOrder.getQty());

            if (mReviewOrder.getProductType().equalsIgnoreCase("configurable")) {
                holder.tvSize.setVisibility(View.VISIBLE);
                holder.tvSizeTitle.setVisibility(View.VISIBLE);
                List<ShoppingBag.ConfigureOption> mConfigureOptionList = mReviewOrder.getConfigureOptionList();
                if (mConfigureOptionList != null && mConfigureOptionList.size() > 0) {
                    for (int i = 0; i < mConfigureOptionList.size(); i++) {
                        if (mConfigureOptionList.get(i).getOption_label().contains("Size")) {
                            holder.tvSize.setText("" + mConfigureOptionList.get(i).getOption_value());
                        }
                    }

                }
            } else {
                holder.tvSize.setVisibility(View.GONE);
                holder.tvSizeTitle.setVisibility(View.GONE);
            }

            final String imageUrl = mReviewOrder.getImageFIle();
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

        }

        return view;
    }

    private class ViewHolder {
        ImageView ivProductImage;
        TextView tvTitle, tvName, tvQtyTitle, tvQty, tvSizeTitle, tvSize, tvOldPrice, tvSpecialPrice;
    }
}
