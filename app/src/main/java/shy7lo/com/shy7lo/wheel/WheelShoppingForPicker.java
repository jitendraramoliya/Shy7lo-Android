package shy7lo.com.shy7lo.wheel;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.model.AppInit;
import shy7lo.com.shy7lo.pref.MyPref;

public class WheelShoppingForPicker extends WheelPicker {

    private int defaultIndex;

    Adapter adapter;

    public WheelShoppingForPicker(Context context) {
        this(context, null);
    }

    public WheelShoppingForPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.adapter = new Adapter();
        setAdapter(adapter);

//        updateShoppingFor();

//        updateDefautShoppingFor();
    }

    @Override
    protected void onItemSelected(int position, Object item) {
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {
    }

    @Override
    protected String getFormattedValue(Object value) {
        return (String) value;
    }

    @Override
    public int getDefaultItemPosition() {
        return defaultIndex;
    }

//    private void updateShoppingFor() {
//        final List<String> data = new ArrayList<>();
//
//        String[] mShoppingForArray = getResources().getStringArray(R.array.shopping_for_array);
//        for (int i = 0; i < mShoppingForArray.length; i++) {
//            data.add(mShoppingForArray[i]);
//        }
//        defaultIndex = 0;
//
//        adapter.setData(data);
//    }

    public void updateShoppingFor(List<AppInit.BaseScreen> mBaseScreenList) {
        final List<String> data = new ArrayList<>();

        for (int i = 0; i < mBaseScreenList.size(); i++) {
            if (MyPref.getPref(getContext(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                data.add(mBaseScreenList.get(i).titleAr);
            } else {
                data.add(mBaseScreenList.get(i).titleEn);
            }
        }

        defaultIndex = 0;

        adapter.setData(data);

        updateDefautShoppingFor();
    }

    private void updateDefautShoppingFor() {
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultDayIndex() {
        return defaultIndex;
    }

    public String getCurrentCountry() {
        return adapter.getItemText(getCurrentItemPosition());
    }

    public interface OnShoppingForSelectedListener {
        void onCountrySelected(WheelShoppingForPicker picker, int position, String name);
    }

    public interface OnShoppingForScrollListener {
        void onCountryScroll(WheelShoppingForPicker picker, int position, String name);
    }
}