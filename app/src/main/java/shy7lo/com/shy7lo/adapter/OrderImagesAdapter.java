package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.MyOrderResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.LogUtils;

/**
 * Created by Jiten on 09-04-2018.
 */

public class OrderImagesAdapter extends RecyclerView.Adapter<OrderImagesAdapter.OrderViewHolder> {

    Context mContext;
    private OnImageClickListener onImageClickListener;
    List<MyOrderResponse.Item> mOrderListing = new ArrayList<>();

    public interface OnImageClickListener{
        public void onImageClick();
    }

    public OrderImagesAdapter(Context mContext, OnImageClickListener onImageClickListener) {
        this.mContext = mContext;
        this.onImageClickListener = onImageClickListener;
    }


    public void addAll(List<MyOrderResponse.Item> mTempOrderListing) {
        if (mOrderListing != null && mOrderListing.size() > 0) {
            mOrderListing.clear();
        }
        mOrderListing.addAll(mTempOrderListing);
        notifyDataSetChanged();
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderViewHolder holder, final int position) {
        LogUtils.e("", "onBindViewHolder::" + position);

        if (MyPref.getPref(mContext, MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.ivProductImage.setScaleX(-1f);
        } else {
            holder.ivProductImage.setScaleX(1f);
        }

        final MyOrderResponse.Item productItem = mOrderListing.get(position);

        Picasso.with(mContext).load(productItem.image).into(holder.ivProductImage);

        holder.ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageClickListener.onImageClick();
            }
        });


    }

    @Override
    public int getItemCount() {
        LogUtils.e("", "mOrderListing::" + mOrderListing.size());
        return mOrderListing.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProductImage;

        public OrderViewHolder(View view) {
            super(view);
            ivProductImage = (ImageView) view.findViewById(R.id.ivProductImage);
        }
    }

}
