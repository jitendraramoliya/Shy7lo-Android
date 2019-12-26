package shy7lo.com.shy7lo.wheel;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

public class WheelQtyPicker extends WheelPicker {

    private int defaultIndex;

    private OnSizeSelectedListener onSizeSelectedListener;
    private OnSizeScrollListener onSizeScrollListener;

    Adapter adapter;

    public WheelQtyPicker(Context context) {
        this(context, null);
    }

    public WheelQtyPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.adapter = new Adapter();
        setAdapter(adapter);

        updateQty();

        updateDefaultQty();
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (null != onSizeScrollListener) {
            final String itemText = (String) item;
            onSizeScrollListener.onSizeScroll(this, position, itemText);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
        if (null != onSizeSelectedListener) {
            final String itemText = (String) item;
            onSizeSelectedListener.onSizeSelected(this, position, itemText);
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

    private void updateQty() {
        final List<String> data = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            data.add("" + (i + 1));
        }
        defaultIndex = 0;

        adapter.setData(data);
    }

    public void setItems(List<String> data) {
        defaultIndex = 0;
        adapter.setData(data);
    }


    public void setOnSizeSelectedListener(OnSizeSelectedListener onSizeSelectedListener) {
        this.onSizeSelectedListener = onSizeSelectedListener;
    }

    public void setOnSizeScrollListener(OnSizeScrollListener onSizeScrollListener) {
        this.onSizeScrollListener = onSizeScrollListener;
    }

    private void updateDefaultQty() {
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultDayIndex() {
        return defaultIndex;
    }

    public String getCurrentQty() {
        return adapter.getItemText(getCurrentItemPosition());
    }

    public interface OnSizeSelectedListener {
        void onSizeSelected(WheelQtyPicker picker, int position, String name);
    }

    public interface OnSizeScrollListener {
        void onSizeScroll(WheelQtyPicker picker, int position, String name);
    }
}