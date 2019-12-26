package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.MyOrderDetailsActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.MyOrderResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.Utils;

public class MyOrderAdapter extends BaseAdapter {

    private Context mContext;
    private boolean isCurrentOrder;
    List<MyOrderResponse.AllOrder> mOrderList = new ArrayList<>();


    public MyOrderAdapter(Context mContext, boolean isCurrentOrder) {
        this.mContext = mContext;
        this.isCurrentOrder = isCurrentOrder;
    }

    public void addAll(List<MyOrderResponse.AllOrder> mTempOrderList) {
        if (mOrderList != null && mOrderList.size() > 0) {
            mOrderList.clear();
        }
        if (mTempOrderList != null && mTempOrderList.size() > 0) {
            mOrderList.addAll(mTempOrderList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public MyOrderResponse.AllOrder getItem(int i) {
        return mOrderList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_my_order, null);
            ViewHolder holder = new ViewHolder();
            holder.tvStatus = view.findViewById(R.id.tvStatus);
            holder.tvOrderTitle = view.findViewById(R.id.tvOrderTitle);
            holder.tvOrderId = view.findViewById(R.id.tvOrderId);
            holder.tvDateTitle = view.findViewById(R.id.tvDateTitle);
            holder.tvEstimatedDeliveryDate = view.findViewById(R.id.tvEstimatedDeliveryDate);
            holder.rvOrderImages = view.findViewById(R.id.rvOrderImages);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.rvOrderImages.setScaleX(-1f);
            holder.tvOrderTitle.setScaleX(-1f);
            holder.tvOrderId.setScaleX(-1f);
            holder.tvDateTitle.setScaleX(-1f);
            holder.tvEstimatedDeliveryDate.setScaleX(-1f);
            holder.tvStatus.setScaleX(-1f);
            holder.tvStatus.setGravity(Gravity.RIGHT);
            ((LinearLayoutManager) holder.rvOrderImages.getLayoutManager()).setReverseLayout(true);
            holder.tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_left, 0, 0, 0);
        } else {
            holder.rvOrderImages.setScaleX(1f);
            holder.tvOrderTitle.setScaleX(1f);
            holder.tvOrderId.setScaleX(1f);
            holder.tvDateTitle.setScaleX(1f);
            holder.tvEstimatedDeliveryDate.setScaleX(1f);
            holder.tvEstimatedDeliveryDate.setScaleX(1f);
            holder.tvStatus.setGravity(Gravity.LEFT);
            ((LinearLayoutManager) holder.rvOrderImages.getLayoutManager()).setReverseLayout(false);
            holder.tvStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_right, 0);
        }

        final MyOrderResponse.AllOrder mOrder = mOrderList.get(i);
        if (mOrder != null) {
            holder.tvStatus.setText("" + mOrder.status);
            holder.tvOrderId.setText("#" + mOrder.orderId);
            holder.tvEstimatedDeliveryDate.setText("" + Utils.getOrderDate(mOrder.date));
            if (isCurrentOrder) {
                holder.tvDateTitle.setText(mContext.getString(R.string.estimated_delivery));
            } else {
                holder.tvDateTitle.setText(mContext.getString(R.string.shipping_date));
            }

            final View finalView = view;
            OrderImagesAdapter mOrderImagesAdapter = new OrderImagesAdapter(mContext, new OrderImagesAdapter.OnImageClickListener() {
                @Override
                public void onImageClick() {
                    finalView.performClick();
                }
            });
            mOrderImagesAdapter.addAll(mOrder.items);
            holder.rvOrderImages.setAdapter(mOrderImagesAdapter);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BNDL_ORDER_ID, mOrder.orderId);
                    bundle.putBoolean(Constant.BNDL_IS_CURRENT_ORDER, isCurrentOrder);
                    IntentHandler.startActivity(mContext, MyOrderDetailsActivity.class, bundle);
                }
            });

        }


        return view;
    }

    private class ViewHolder {
        TextView tvStatus, tvOrderTitle, tvOrderId, tvDateTitle, tvEstimatedDeliveryDate;
        RecyclerView rvOrderImages;
    }

}
