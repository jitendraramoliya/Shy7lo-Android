package shy7lo.com.shy7lo.wheel;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.model.CityResponse;
import shy7lo.com.shy7lo.model.RegionsResponse;
import shy7lo.com.shy7lo.pref.MyPref;

public class WheelStringPicker extends WheelPicker {

    private int defaultIndex;

    private OnSelectedListener onSelectedListener;
    private OnScrollListener onScrollListener;

    WheelPicker.Adapter adapter;

    public WheelStringPicker(Context context) {
        this(context, null);
    }

    public WheelStringPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.adapter = new Adapter();
        setAdapter(adapter);

//        updateData();

        updateDefaultDataIndex();
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (null != onScrollListener) {
            final String itemText = (String) item;
            onScrollListener.onScroll(this, position, itemText);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        if (null != onSelectedListener) {
            final String itemText = (String) item;
            onSelectedListener.onSelected(this, position, itemText);
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

//    private void updateData() {
//        final List<String> data = new ArrayList<>();
//
////        String[] mCountryArray = getResources().getStringArray(R.array.country_array);
////        for (int i = 0; i < mCountryArray.length; i++) {
////            data.add(mCountryArray[i]);
////        }
//        defaultIndex = 0;
//
//        adapter.setData(data);
//    }

    public void updateRegionDataList(List<RegionsResponse.Region> mDataList) {
        if (mDataList != null && mDataList.size() > 0) {
            final List<String> data = new ArrayList<>();
            for (int i = 0; i < mDataList.size(); i++) {

                if (MyPref.getPref(getContext(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    data.add("" + mDataList.get(i).region);
                } else {
                    data.add("" + mDataList.get(i).regionEn);
                }
            }
            adapter.setData(data);
        }
    }

    public void updateCityDataList(List<CityResponse.City> mDataList) {
        if (mDataList != null && mDataList.size() > 0) {
            final List<String> data = new ArrayList<>();
            for (int i = 0; i < mDataList.size(); i++) {
                if (MyPref.getPref(getContext(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                    data.add("" + mDataList.get(i).city);
                } else {
                    data.add("" + mDataList.get(i).cityEn);
                }
            }
            adapter.setData(data);
        }
    }


    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    private void updateDefaultDataIndex() {
        setSelectedItemPosition(defaultIndex);
    }

    public void updateData(int positon) {
        defaultIndex = positon;
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultDayIndex() {
        return defaultIndex;
    }

    public String getCurrentCountry() {
        return adapter.getItemText(getCurrentItemPosition());
    }


    public interface OnSelectedListener {
        void onSelected(WheelStringPicker picker, int position, String name);
    }

    public interface OnScrollListener {
        void onScroll(WheelStringPicker picker, int position, String name);
    }
}