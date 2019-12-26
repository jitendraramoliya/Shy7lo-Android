package shy7lo.com.shy7lo.wheel;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.R;

public class WheelLanguagePicker extends WheelPicker {

    private int defaultIndex;

    private OnLanguageSelectedListener onLanguageSelectedListener;
    private OnLanguageScrollListener onLanguageScrollListener;

    Adapter adapter;

    public WheelLanguagePicker(Context context) {
        this(context, null);
    }

    public WheelLanguagePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.adapter = new Adapter();
        setAdapter(adapter);

        updateLanguage();

        updateDefaultLanguage();
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (null != onLanguageScrollListener) {
            final String itemText = (String) item;
            onLanguageScrollListener.onCountryScroll(this, position, itemText);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        if (null != onLanguageSelectedListener) {
            final String itemText = (String) item;
            onLanguageSelectedListener.onCountrySelected(this, position, itemText);
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

    private void updateLanguage() {
        final List<String> data = new ArrayList<>();

        String[] mLanguageArray = getResources().getStringArray(R.array.language_array);
        for (int i = 0; i < mLanguageArray.length; i++) {
            data.add(mLanguageArray[i]);
        }
        defaultIndex = 0;

        adapter.setData(data);
    }


    public void setOnLanguageSelectedListener(OnLanguageSelectedListener onLanguageSelectedListener) {
        this.onLanguageSelectedListener = onLanguageSelectedListener;
    }

    public void setOnLanguageScrollListener(OnLanguageScrollListener onLanguageScrollListener) {
        this.onLanguageScrollListener = onLanguageScrollListener;
    }

    private void updateDefaultLanguage() {
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultDayIndex() {
        return defaultIndex;
    }

    public String getCurrentCountry() {
        return adapter.getItemText(getCurrentItemPosition());
    }

    public interface OnLanguageSelectedListener {
        void onCountrySelected(WheelLanguagePicker picker, int position, String name);
    }

    public interface OnLanguageScrollListener {
        void onCountryScroll(WheelLanguagePicker picker, int position, String name);
    }
}