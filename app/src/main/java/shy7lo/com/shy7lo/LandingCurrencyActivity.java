package shy7lo.com.shy7lo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;
import shy7lo.com.shy7lo.wheel.WheelCurrencyPicker;

/**
 * Created by JitenRamen on 20-08-2016.
 */
public class LandingCurrencyActivity extends Activity implements View.OnClickListener {

    private final String TAG = "LandingCurrencyActivity";

    @BindView(R.id.tvSkip)
    TextView tvSkip;
    @BindView(R.id.tvGenuine)
    TextView tvDaysReturn;
    @BindView(R.id.tvHighQuality)
    TextView tvOptionalCashReturn;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.tvShoppingFor)
    TextView tvChooseCurrency;
    @BindView(R.id.pickerShoppingFor)
    WheelCurrencyPicker pickerCurrency;

    private int mCountryPosition = 0;
    private float mExchangeRate = 1f;
    private String mCurrencyCode = "SAR";

    private Activity getActivity() {
        return LandingCurrencyActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_landing_currency);
        ButterKnife.bind(getActivity());

        InitializeConrols();
        InitializeConrolsAction();

    }

    private void InitializeConrols() {

        tvSkip.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvDaysReturn.setTypeface(Shy7lo.SFUIDisplayBold);
        tvOptionalCashReturn.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvChooseCurrency.setTypeface(Shy7lo.SFUIDisplayRegular);
        tvNext.setTypeface(Shy7lo.TahomaBold);

        mCountryPosition = MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, 1) - 1;

        pickerCurrency.setSelectedItemPosition(mCountryPosition);

        pickerCurrency.setOnCurrencySelectedListener(new WheelCurrencyPicker.OnCurrencySelectedListener() {
            @Override
            public void onCountrySelected(WheelCurrencyPicker picker, int position, String name) {
                setCurrency(position);
            }
        });

        pickerCurrency.setOnCurrencyScrollListener(new WheelCurrencyPicker.OnCurrencyScrollListener() {
            @Override
            public void onCountryScroll(WheelCurrencyPicker picker, int position, String name) {
                setCurrency(position);
            }
        });

    }

    private void setCurrency(int position) {
        switch (position) {
            case 0:
                mCurrencyCode = "SAR";
                mExchangeRate = 1f;
                break;
            case 1:
                mCurrencyCode = "AED";
                mExchangeRate = 0.9789f;
                break;
            case 2:
                mCurrencyCode = "QAR";
                mExchangeRate = 0.971f;
                break;
            case 3:
                mCurrencyCode = "KWD";
                mExchangeRate = 0.0809f;
                break;
            case 4:
                mCurrencyCode = "OMR";
                mExchangeRate = 0.1026f;
                break;
            case 5:
                mCurrencyCode = "BHD";
                mExchangeRate = 0.1005f;
                break;
        }
    }

    private void InitializeConrolsAction() {
        tvSkip.setOnClickListener(this);
        tvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == tvNext) {

            String mCode = "";
            if (mCurrencyCode.equalsIgnoreCase("SAR")) {
                mCode = getResources().getString(R.string.SAR);
            } else if (mCurrencyCode.equalsIgnoreCase("AED")) {
                mCode = getResources().getString(R.string.AED);
            } else if (mCurrencyCode.equalsIgnoreCase("QAR")) {
                mCode = getResources().getString(R.string.QAR);
            } else if (mCurrencyCode.equalsIgnoreCase("OMR")) {
                mCode = getResources().getString(R.string.OMR);
            } else if (mCurrencyCode.equalsIgnoreCase("KWD")) {
                mCode = getResources().getString(R.string.KWD);
            } else if (mCurrencyCode.equalsIgnoreCase("BHD")) {
                mCode = getResources().getString(R.string.BHD);
            }

            LogUtils.e("", "mCurrencyCode::" + mCurrencyCode + " mCode::" + mCode);

            MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mCode);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "" + mCurrencyCode);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, mExchangeRate);

            IntentHandler.startActivity(getActivity(), LandingShoppingForActivity.class);

        } else if (v == tvSkip) {

            mExchangeRate = 1f;
            mCurrencyCode = "SAR";

            String mCode = getResources().getString(R.string.SAR);

            MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mCode);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "" + mCurrencyCode);
            MyPref.setPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, mExchangeRate);

            IntentHandler.startActivity(getActivity(), LandingShoppingForActivity.class);
        }
    }

}
