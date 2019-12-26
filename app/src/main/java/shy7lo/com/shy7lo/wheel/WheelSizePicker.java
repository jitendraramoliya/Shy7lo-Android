package shy7lo.com.shy7lo.wheel;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.model.ProductDetails;
import shy7lo.com.shy7lo.model.Wishlist;
import shy7lo.com.shy7lo.utils.LogUtils;

public class WheelSizePicker extends WheelPicker {

    private int defaultIndex;

    private OnSizeSelectedListener onSizeSelectedListener;
    private OnSizeScrollListener onSizeScrollListener;

    Adapter adapter;

    public WheelSizePicker(Context context) {
        this(context, null);
    }

    public WheelSizePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.adapter = new Adapter();
        setAdapter(adapter);

//        updateSize();

//        updateDefaultSize();
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (null != onSizeScrollListener) {
            final String itemText = (String) item;
            onSizeScrollListener.onCountryScroll(this, position, itemText);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        if (null != onSizeSelectedListener) {
            final String itemText = (String) item;
            onSizeSelectedListener.onCountrySelected(this, position, itemText);
        }
    }

    @Override
    protected String getFormattedValue(Object value) {
        return (String) value;
    }

    @Override
    public int getDefaultItemPosition() {
        return defaultIndex;
    }

//    private void updateSize() {
//        final List<String> data = new ArrayList<>();
//
//        String[] mSizeArray = getResources().getStringArray(R.array.country_array);
//        for (int i = 0; i < mSizeArray.length; i++) {
//            data.add(mSizeArray[i]);
//        }
//        defaultIndex = 0;
//
//        adapter.setData(data);
//    }

    String mLocalLable = "";

    public void updateLocalLable(String mLocalLable){
        this.mLocalLable  = mLocalLable;
    }

    public void updateSize(List<ProductDetails.AttributeOption> mAttributeOptionList) {
        final List<String> data = new ArrayList<>();

        for (int i = 0; i < mAttributeOptionList.size(); i++) {
            ProductDetails.AttributeOption value = mAttributeOptionList.get(i);

            String lable = "";
            String lableGson = value.data;
            LogUtils.e("", "lableGson::" + lableGson);
            if (!TextUtils.isEmpty(lableGson)) {
                try {
                    JSONObject jsonObject = new JSONObject(lableGson);
                    LogUtils.e("", "mLocalLable::" + mLocalLable);
                    if (jsonObject != null && jsonObject.has(mLocalLable)) {
                        lable = jsonObject.getString(mLocalLable);
                    } else {
                        lable = value.getDefaultLabel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    lable = value.getDefaultLabel();
                }
            } else {
                lable = value.getDefaultLabel();
            }
            data.add(lable);
//            data.add(value.getDefaultLabel());
        }


        defaultIndex = 0;
        adapter.setData(data);
        updateDefaultSize();
    }

    public void updateWishListSize(List<Wishlist.Option> mAttributeOptionList) {
        final List<String> data = new ArrayList<>();

        for (int i = 0; i < mAttributeOptionList.size(); i++) {
            Wishlist.Option value = mAttributeOptionList.get(i);

//            String lable = "";
//            String lableGson = value.data;
//            LogUtils.e("", "lableGson::" + lableGson);
//            if (!TextUtils.isEmpty(lableGson)) {
//                try {
//                    JSONObject jsonObject = new JSONObject(lableGson);
//                    LogUtils.e("", "mLocalLable::" + mLocalLable);
//                    if (jsonObject != null && jsonObject.has(mLocalLable)) {
//                        lable = jsonObject.getString(mLocalLable);
//                    } else {
//                        lable = value.getDefaultLabel();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    lable = value.getDefaultLabel();
//                }
//            } else {
//                lable = value.getDefaultLabel();
//            }
//            data.add(lable);
            data.add(value.label);
        }


        defaultIndex = 0;
        adapter.setData(data);
        updateDefaultSize();
    }

    public void updateSettinsSize(List<String> data) {

        defaultIndex = 0;
        adapter.setData(data);
        updateDefaultSize();
    }

    public void updateIndex(int index) {
        defaultIndex = index;
        setSelectedItemPosition(defaultIndex);
    }


    public void setOnSizeSelectedListener(OnSizeSelectedListener onSizeSelectedListener) {
        this.onSizeSelectedListener = onSizeSelectedListener;
    }

    public void setOnSizeScrollListener(OnSizeScrollListener onSizeScrollListener) {
        this.onSizeScrollListener = onSizeScrollListener;
    }

    private void updateDefaultSize() {
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultDayIndex() {
        return defaultIndex;
    }

    public String getCurrentSize() {
        return adapter.getItemText(getCurrentItemPosition());
    }

    public interface OnSizeSelectedListener {
        void onCountrySelected(WheelSizePicker picker, int position, String name);
    }

    public interface OnSizeScrollListener {
        void onCountryScroll(WheelSizePicker picker, int position, String name);
    }
}