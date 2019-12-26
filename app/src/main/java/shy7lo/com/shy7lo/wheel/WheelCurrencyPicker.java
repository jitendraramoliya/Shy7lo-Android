package shy7lo.com.shy7lo.wheel;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.R;

public class WheelCurrencyPicker extends WheelPicker {

    private int defaultIndex;

    private OnCurrencySelectedListener onCurrencySelectedListener;
    private OnCurrencyScrollListener onCurrencyScrollListener;

    Adapter adapter;

    public WheelCurrencyPicker(Context context) {
        this(context, null);
    }

    public WheelCurrencyPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.adapter = new Adapter();
        setAdapter(adapter);

        updateCurrency();

        updateDefaultCurrency();
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (null != onCurrencyScrollListener) {
            final String itemText = (String) item;
            onCurrencyScrollListener.onCountryScroll(this, position, itemText);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        if (null != onCurrencySelectedListener) {
            final String itemText = (String) item;
            onCurrencySelectedListener.onCountrySelected(this, position, itemText);
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

    private void updateCurrency() {
        final List<String> data = new ArrayList<>();

        String[] mCurrencyArray = getResources().getStringArray(R.array.currency_array);
        for (int i = 0; i < mCurrencyArray.length; i++) {
            data.add(mCurrencyArray[i]);
        }
        defaultIndex = 0;

        adapter.setData(data);
    }


    public void setOnCurrencySelectedListener(OnCurrencySelectedListener onCurrencySelectedListener) {
        this.onCurrencySelectedListener = onCurrencySelectedListener;
    }

    public void setOnCurrencyScrollListener(OnCurrencyScrollListener onCurrencyScrollListener) {
        this.onCurrencyScrollListener = onCurrencyScrollListener;
    }

    private void updateDefaultCurrency() {
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultDayIndex() {
        return defaultIndex;
    }

    public String getCurrentCountry() {
        return adapter.getItemText(getCurrentItemPosition());
    }

    public interface OnCurrencySelectedListener {
        void onCountrySelected(WheelCurrencyPicker picker, int position, String name);
    }

    public interface OnCurrencyScrollListener {
        void onCountryScroll(WheelCurrencyPicker picker, int position, String name);
    }
}