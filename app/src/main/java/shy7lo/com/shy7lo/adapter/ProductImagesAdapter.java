package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.ProductDetails;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.widget.ParallaxScrollView;
import shy7lo.com.shy7lo.widget.TouchImageView;

/**
 * Created by JITEN-PC on 14-02-2017.
 */

public class ProductImagesAdapter extends PagerAdapter {

    Context mContext;
    List<ProductDetails.Image_> imageList;
    private ParallaxScrollView pscScroll;

    public ProductImagesAdapter(Context mContext, List<ProductDetails.Image_> imageList, ParallaxScrollView pscScroll) {
        this.mContext = mContext;
        this.imageList = imageList;
        this.pscScroll = pscScroll;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.vp_item_product_image, container, false);

        final TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.ivProductImage);
//        final ImageView imageView = (ImageView) itemView.findViewById(R.id.ivProductImage);
        final AVLoadingIndicatorView mAVProgressbar = (AVLoadingIndicatorView) itemView.findViewById(R.id.mAVProgressbar);
        mAVProgressbar.smoothToShow();
//        final String imageUrl = RestClient.IMAGE_URL + imageList.get(position).getUrl();
        final String imageUrl = imageList.get(position).getUrl();
        LogUtils.e("", "imageUrl::" + imageUrl);

        Glide.with(mContext)
                .asBitmap()
                .load(imageUrl)
                .apply(new RequestOptions().override(1080, 1080))
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(Bitmap drawable, Transition<? super Bitmap> transition) {
                        super.onResourceReady(drawable, transition);
                        mAVProgressbar.smoothToHide();
                        imageView.setImageBitmap(drawable);
                        imageView.setZoom(1);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        mAVProgressbar.smoothToHide();
                    }
                });

//        Glide.with(mContext).load(imageUrl).into(new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//
//                mAVProgressbar.smoothToHide();
//                imageView.setImageDrawable(resource);
//            }
//
//            @Override
//            public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                super.onLoadFailed(errorDrawable);
//                mAVProgressbar.smoothToHide();
//            }
//        });
//        Glide.with(mContext)
//                .load(new File(imageUrl))
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        mAVProgressbar.smoothToHide();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        mAVProgressbar.smoothToHide();
//                        return false;
//                    }
//                })
//                .into(imageView);

//
//        Glide.with(mContext)
//                .load(imageUrl)
//                .into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                        LogUtils.e("", "onResourceReady " + imageUrl);
//                        imageView.setImageDrawable(resource);
//                        mAVProgressbar.smoothToHide();
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                        LogUtils.e("", "onLoadFailed " + imageUrl);
//                        super.onLoadFailed(errorDrawable);
////                        Glide.with(mContext)
////                                .load(imageUrl).into(imageView);
//                        mAVProgressbar.smoothToHide();
//                    }
//                });


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        pscScroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    case MotionEvent.ACTION_UP:
                        pscScroll.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        pscScroll.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

//                .into(imageView);

//        ViewTreeObserver vto = imageView.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
//
//                Picasso.with(mContext).load(imageUrl).resize((int) (Utils.getScreenResolution(mContext) * 0.6)
//                        , (int) (mContext.getResources().getDimension(R.dimen.vp_hieght) * .7)).onlyScaleDown().into(imageView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        mAVProgressbar.smoothToHide();
//                    }
//
//                    @Override
//                    public void onError() {
//                        mAVProgressbar.smoothToHide();
//                    }
//                });
//
////                mProgressBar.setVisibility(View.VISIBLE);
////                Picasso.with(mContext).load(imageUrl).resize((int) (imageView.getMeasuredWidth() * 0.6)
////                        , (int) (imageView.getMeasuredHeight() * .7)).onlyScaleDown().into(imageView, new Callback() {
////                    @Override
////                    public void onSuccess() {
//////                        mProgressBar.setVisibility(View.GONE);
////                    }
////
////                    @Override
////                    public void onError() {
//////                        mProgressBar.setVisibility(View.GONE);
////                    }
////                });
////                Picasso.with(mContext).load(imageUrl).into(imageView, new Callback() {
////                    @Override
////                    public void onSuccess() {
//////                        mProgressBar.setVisibility(View.GONE);
////                    }
////
////                    @Override
////                    public void onError() {
//////                        mProgressBar.setVisibility(View.GONE);
////                    }
////                });
//                return true;
//            }
//        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof RelativeLayout) {
            container.removeView((RelativeLayout) object);
        } else if (object instanceof LinearLayout) {
            container.removeView((LinearLayout) object);
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }
}
