package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.grantland.widget.AutofitTextView;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.ProductDetails;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by JITEN-PC on 15-02-2017.
 */

public class ProductSizeAdapter extends BaseAdapter {

    Context mContext;
    List<ProductDetails.AttributeOption> mValueList;
    OnSizeSelectListener onSizeSelectListener;
    private boolean isUsLable = false;
    private String mLocalLable = "";
    private int TwentyDp;

    public ProductSizeAdapter(Context mContext, List<ProductDetails.AttributeOption> mValueList, OnSizeSelectListener onSizeSelectListener) {
        this.mContext = mContext;
        this.mValueList = mValueList;
        this.onSizeSelectListener = onSizeSelectListener;
        TwentyDp = (int) Utils.pxFromDp(mContext, 20);
    }

    public void setLableLocal(boolean isUsLable) {
        this.isUsLable = isUsLable;
    }

    public void setLableLocal(String mLocalLable) {
        this.mLocalLable = mLocalLable;
        LogUtils.e("", "mLocalLable::" + mLocalLable);
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

    public interface OnSizeSelectListener {
        public void onSizeSelect(String lable, int position, boolean isUsLable);
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {


        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.gv_item_size, null);
            ViewHolder holder = new ViewHolder();
            holder.btnItemSize = (RelativeLayout) view.findViewById(R.id.btnItemSize);
//            holder.ivSize = (Button) view.findViewById(R.id.ivSize);
//            holder.tvSize = (TextView) view.findViewById(R.id.tvSize);
//            holder.tvSize = (AutoResizeTextView) view.findViewById(R.id.tvSize);
            holder.tvSize = (AutofitTextView) view.findViewById(R.id.tvSize);
            view.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) view.getTag();


        final ProductDetails.AttributeOption value = mValueList.get(position);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvSize.setScaleX(-1f);
        } else {
            holder.tvSize.setScaleX(1f);
        }

        if (value != null) {

//            String lableGson = new Gson().toJson(value);
            String lableGson = value.data;
            LogUtils.e("", "lableGson::" + lableGson);
            if (!TextUtils.isEmpty(lableGson)) {
                try {
                    JSONObject jsonObject = new JSONObject(lableGson);
                    LogUtils.e("", "mLocalLable::" + mLocalLable);
                    if (jsonObject != null && jsonObject.has(mLocalLable)) {
                        holder.tvSize.setText(jsonObject.getString(mLocalLable));
                    } else {
                        holder.tvSize.setText(value.getDefaultLabel());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    holder.tvSize.setText(value.getDefaultLabel());
                }
            } else {
                holder.tvSize.setText(value.getDefaultLabel());
            }

//            if (isUsLable) {
//                holder.tvSize.setText(value.getUsLabel());
////            holder.tvSize.setTextSize(TwentyDp);
//                LogUtils.e("", "value.getUsLabel()::" + value.getUsLabel());
//            } else {
//                holder.tvSize.setText(value.getDefaultLabel());
////            holder.tvSize.setTextSize(TwentyDp);
//                LogUtils.e("", "value.getDefaultLabel()::" + value.getDefaultLabel());
//            }

            LogUtils.e("", "adapter " + position + " " + value.getSelected());
//            if (value.getSelected()) {
////                holder.ivSize.setBackgroundResource(R.drawable.rounded_dudhiya_shape);
////                if( android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
////                    holder.ivSize.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.rounded_dudhiya_shape) );
////                } else {
////                    holder.ivSize.setBackground( mContext.getResources().getDrawable(R.drawable.rounded_dudhiya_shape));
////                }
//                holder.ivSize.setBackgroundResource(R.drawable.rounded_dudhiya_shape);
//                holder.tvSize.setTextColor(Color.WHITE);
//            } else {
////                holder.ivSize.setBackgroundResource(R.drawable.bg_btn_size);
////                if( android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
////                    holder.ivSize.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.bg_btn_size) );
////                } else {
////                    holder.ivSize.setBackground( mContext.getResources().getDrawable(R.drawable.bg_btn_size));
////                }
////                holder.tvSize.setTextColor(mContext.getResources().getColorStateList(R.color.size_text_selector));
//                holder.ivSize.setBackgroundResource(R.drawable.rounded_dudhiya_border);
//                holder.tvSize.setTextColor(mContext.getResources().getColor(R.color.sb_selection));
//            }

//            holder.btnItemSize.setSelected(value.getSelected());
        }

        holder.tvSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int j = 0; j < mValueList.size(); j++) {
                    mValueList.get(j).setSelected(false);
                }
                mValueList.get(position).setSelected(true);
//                onSizeSelectListener.onSizeSelect(isUsLable ? value.getUsLabel() : value.getDefaultLabel(), position, isUsLable);
                onSizeSelectListener.onSizeSelect(holder.tvSize.getText().toString(), position, isUsLable);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private class ViewHolder {
        RelativeLayout btnItemSize;
        //        Button ivSize;
//        AutoResizeTextView tvSize;
        AutofitTextView tvSize;
//        TextView tvSize;
    }
}
