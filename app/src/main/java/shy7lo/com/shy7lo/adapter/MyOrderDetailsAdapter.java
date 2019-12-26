package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.MyOrderDetailsResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.CustomTypefaceSpan;
import shy7lo.com.shy7lo.utils.Utils;

public class MyOrderDetailsAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyOrderDetailsResponse.Item> mOrderDetailsList = new ArrayList<>();
    private String mCurrencyCode = "";
    private float mExchangeRate;

    public MyOrderDetailsAdapter(Context mContext) {
        this.mContext = mContext;
        mCurrencyCode = MyPref.getPref(mContext, MyPref.DEFAULT_CURRENCY_CODE, mContext.getResources().getString(R.string.SAR));
        mExchangeRate = MyPref.getPref(mContext, MyPref.DEFAULT_EXCHANGE_RATE, 1f);
    }

    public void addAll(List<MyOrderDetailsResponse.Item> mTempOrderDetailsList) {
        if (mOrderDetailsList != null && mOrderDetailsList.size() > 0) {
            mOrderDetailsList.clear();
        }

        if (mTempOrderDetailsList != null && mTempOrderDetailsList.size() > 0) {
            mOrderDetailsList.addAll(mTempOrderDetailsList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mOrderDetailsList.size();
    }

    @Override
    public Object getItem(int i) {
        return mOrderDetailsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_order_details, null);
            ViewHolder holder = new ViewHolder();
            holder.ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
            holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvOldPrice = (TextView) view.findViewById(R.id.tvOldPrice);
            holder.tvNewPrice = (TextView) view.findViewById(R.id.tvNewPrice);
            holder.tvSize = (TextView) view.findViewById(R.id.tvSize);
            holder.tvSizeValue = (TextView) view.findViewById(R.id.tvSizeValue);
            holder.tvQty = (TextView) view.findViewById(R.id.tvQty);
            holder.tvQtyValue = (TextView) view.findViewById(R.id.tvQtyValue);

            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.ivProductImage.setScaleX(-1f);
            holder.tvTitle.setScaleX(-1f);
            holder.tvName.setScaleX(-1f);
            holder.tvOldPrice.setScaleX(-1f);
            holder.tvNewPrice.setScaleX(-1f);
            holder.tvSize.setScaleX(-1f);
            holder.tvSizeValue.setScaleX(-1f);
            holder.tvQty.setScaleX(-1f);
            holder.tvQtyValue.setScaleX(-1f);

            holder.tvTitle.setTypeface(Shy7lo.DroidKufiBold);
            holder.tvOldPrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvNewPrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvName.setTypeface(Shy7lo.DroidKufiRegular);

        } else {
            holder.ivProductImage.setScaleX(1f);
            holder.tvTitle.setScaleX(1f);
            holder.tvName.setScaleX(1f);
            holder.tvOldPrice.setScaleX(1f);
            holder.tvNewPrice.setScaleX(1f);
            holder.tvSize.setScaleX(1f);
            holder.tvSizeValue.setScaleX(1f);
            holder.tvQty.setScaleX(1f);
            holder.tvQtyValue.setScaleX(1f);

            holder.tvTitle.setTypeface(Shy7lo.RalewayBold);
            holder.tvOldPrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvNewPrice.setTypeface(Shy7lo.DroidKufiRegular);
            holder.tvName.setTypeface(Shy7lo.RalewayRegular);
        }

        final MyOrderDetailsResponse.Item value = mOrderDetailsList.get(position);

        if (value != null) {

            String mProductText = "";
            if (!TextUtils.isEmpty(value.brand) && !value.brand.equalsIgnoreCase("null")) {
                mProductText = value.brand;
            } else {
                mProductText = "";
            }
            int brandLength = mProductText.length();
            if (mProductText.length() > 0) {
                mProductText = mProductText + " " + value.name;
            } else {
                mProductText = value.name;
            }
            SpannableStringBuilder sbBrand = new SpannableStringBuilder(mProductText);
            sbBrand.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, brandLength, 0);
            holder.tvName.setText(sbBrand);

//            holder.tvQty.setVisibility(View.VISIBLE);
//            holder.tvQtyValue.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(value.productType) && value.productType.equalsIgnoreCase("configurable")){
                holder.tvSize.setVisibility(View.VISIBLE);
                holder.tvSizeValue.setVisibility(View.VISIBLE);

                holder.tvQtyValue.setText(""+value.qtyOrdered);
                holder.tvSizeValue.setText(""+value.label);
            }else{
                holder.tvSize.setVisibility(View.INVISIBLE);
                holder.tvSizeValue.setVisibility(View.INVISIBLE);

                holder.tvQtyValue.setText(""+value.qtyOrdered);
            }

            if (!TextUtils.isEmpty(String.valueOf(value.price))) {
                SpannableStringBuilder sb = new SpannableStringBuilder(mCurrencyCode + " " + Utils.getIntPrice(mExchangeRate, value.price));
                int start = mCurrencyCode.length() + 1;
                int end = mCurrencyCode.length() + 1 + Utils.getIntPrice(mExchangeRate, value.price).length();
                if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.DroidKufiBold), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                } else {
                    sb.setSpan(new CustomTypefaceSpan("", Shy7lo.RalewayBold), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                }
                holder.tvOldPrice.setText(sb);
            } else {
                holder.tvOldPrice.setText("" + value.price + " " + mCurrencyCode);
            }

            Glide.with(mContext)
                    .load(value.image)
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
        TextView tvTitle, tvName, tvOldPrice, tvNewPrice, tvSize, tvSizeValue, tvQty, tvQtyValue;
    }


}
