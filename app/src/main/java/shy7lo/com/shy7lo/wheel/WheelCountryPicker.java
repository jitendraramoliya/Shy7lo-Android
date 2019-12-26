package shy7lo.com.shy7lo.wheel;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.pref.MyPref;

public class WheelCountryPicker extends WheelPicker {

    private int defaultIndex;

    private OnCountrySelectedListener onCountrySelectedListener;
    private OnCountryScrollListener onCountryScrollListener;

    WheelPicker.Adapter adapter;

    public WheelCountryPicker(Context context) {
        this(context, null);
    }

    public WheelCountryPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.adapter = new Adapter();
        setAdapter(adapter);

        updateCountry();

        updateDefaultCountry();
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (null != onCountryScrollListener) {
            final String itemText = (String) item;
            onCountryScrollListener.onCountryScroll(this, position, itemText);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        if (null != onCountrySelectedListener) {
            final String itemText = (String) item;
            onCountrySelectedListener.onCountrySelected(this, position, itemText);
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

    private void updateCountry() {
        final List<String> data = new ArrayList<>();

        String[] mCountryArray = getResources().getStringArray(R.array.country_array);
        for (int i = 0; i < mCountryArray.length; i++) {
            data.add(mCountryArray[i]);
        }
        defaultIndex = 0;

        adapter.setData(data);
    }

    public void updateCountryList(List<AppInit.Country> mCountryList) {
        if (mCountryList != null && mCountryList.size() > 0) {
            final List<String> data = new ArrayList<>();
            for (int i = 0; i < mCountryList.size(); i++) {
                if (MyPref.getPref(getContext(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_Arabic).equals(MyPref.LANGUAGE_Arabic)) {
                    data.add(mCountryList.get(i).fullNameArabic);
                }else{
                    data.add(mCountryList.get(i).fullNameEnglish);
                }
            }

            adapter.setData(data);
        }
    }


    public void setOnCountrySelectedListener(OnCountrySelectedListener onCountrySelectedListener) {
        this.onCountrySelectedListener = onCountrySelectedListener;
    }

    public void setOnCountryScrollListener(OnCountryScrollListener onCountryScrollListener) {
        this.onCountryScrollListener = onCountryScrollListener;
    }

    private void updateDefaultCountry() {
        setSelectedItemPosition(defaultIndex);
    }

    public void updateCountry(int positon) {
        defaultIndex = positon;
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultDayIndex() {
        return defaultIndex;
    }

    public String getCurrentCountry() {
        return adapter.getItemText(getCurrentItemPosition());
    }

    public interface OnCountrySelectedListener {
        void onCountrySelected(WheelCountryPicker picker, int position, String name);
    }

    public interface OnCountryScrollListener {
        void onCountryScroll(WheelCountryPicker picker, int position, String name);
    }
}