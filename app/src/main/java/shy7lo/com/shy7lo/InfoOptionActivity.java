package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.adapter.InfoOptionAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.CMSPage;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 14-09-2017.
 */

public class InfoOptionActivity extends AppCompatActivity {

    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibCancel)
    ImageButton ibCancel;

    @BindView(R.id.lvOption)
    ListView lvOption;
    InfoOptionAdapter nInfoOptionAdapter;
    List<String> mOptionList;

    public static String BNDL_OPTION = "BNDL_OPTION";
    public static String BNDL_TITLE = "BNDL_TITLE";
    public static String BNDL_OPTION_INFORMATION = "BNDL_OPTION_INFORMATION";
    public static String BNDL_OPTION_CUSTOMER = "BNDL_OPTION_CUSTOMER";
    String mOption, mTitle;
    private List<CMSPage.Child> mHelpOtiopnList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_info_option);
        ButterKnife.bind(getActivity());
        InitilizeControls();
        InitilizeControlsAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Shy7lo.setActivityContext(getActivity());
    }

    private void InitilizeControls() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            mTitle = bundle.getString(BNDL_TITLE);
            mOption = bundle.getString(BNDL_OPTION);
            tvTitle.setText("" + mTitle);
            if (!TextUtils.isEmpty(mOption)) {
                Type mType = new TypeToken<List<CMSPage.Child>>() {
                }.getType();
                mHelpOtiopnList = new Gson().fromJson(mOption, mType);
            }
        }

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
        } else {
            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
        }


        nInfoOptionAdapter = new InfoOptionAdapter(getActivity(), mHelpOtiopnList);
        lvOption.setAdapter(nInfoOptionAdapter);

        lvOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!Utils.isInternetConnected(getActivity())) {
                    Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                        @Override
                        public void onRetryClicked(Dialog dialog) {
                            if (Utils.isInternetConnected(getActivity())) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        }
                    });
                    return;
                }

                CMSPage.Child mCMSChildPage = mHelpOtiopnList.get(position);
                if (mCMSChildPage != null) {
                    Bundle bundle = new Bundle();
                    if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                        bundle.putString(WebviewActivity.BNDL_URL, mCMSChildPage.urlAr);
                        bundle.putString(WebviewActivity.BNDL_TITLE, mCMSChildPage.titleAr);
                    }else{
                        bundle.putString(WebviewActivity.BNDL_URL, mCMSChildPage.url);
                        bundle.putString(WebviewActivity.BNDL_TITLE, mCMSChildPage.title);
                    }
                    IntentHandler.startActivity(getActivity(), WebviewActivity.class, bundle);
                }

            }
        });

//        mOptionList = new ArrayList<>();
//        if (mOption.equalsIgnoreCase(BNDL_OPTION_INFORMATION)) {
//            mOptionList = Arrays.asList(getResources().getStringArray(R.array.information_array));
//            tvTitle.setText(getResources().getText(R.string.information));
//        } else if (mOption.equalsIgnoreCase(BNDL_OPTION_CUSTOMER)) {
//            mOptionList = Arrays.asList(getResources().getStringArray(R.array.customer_service_array));
//            tvTitle.setText(getResources().getText(R.string.customer_service));
//        }

//        nInfoOptionAdapter = new InfoOptionAdapter(getActivity(), mOptionList);
//        lvOption.setAdapter(nInfoOptionAdapter);

//        lvOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                if (!Utils.isInternetConnected(getActivity())) {
//                    Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
//                        @Override
//                        public void onRetryClicked(Dialog dialog) {
//                            if (Utils.isInternetConnected(getActivity())) {
//                                if (dialog != null && dialog.isShowing()) {
//                                    dialog.dismiss();
//                                }
//                            }
//                        }
//                    });
//                    return;
//                }
//
//                if (mOption.equalsIgnoreCase(BNDL_OPTION_INFORMATION)) {
//
//                    Bundle bundle = new Bundle();
//
//                    if (position == 0) {
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/ar/about-us/");
//                        } else {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/en/about-us/");
//                        }
//
//                    } else if (position == 1) {
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/ar/security/");
//                        } else {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/en/security/");
//                        }
//                    } else if (position == 2) {
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/ar/affiliates/");
//                        } else {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/en/affiliates/");
//                        }
//                    }
//
//                    bundle.putString(WebviewActivity.BNDL_TITLE, mOptionList.get(position));
//                    IntentHandler.startActivity(getActivity(), WebviewActivity.class, bundle);
//
//                } else if (mOption.equalsIgnoreCase(BNDL_OPTION_CUSTOMER)) {
//
//                    Bundle bundle = new Bundle();
//
//                    if (position == 0) {
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/ar/return-policy/");
//                        } else {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/en/return-policy/");
//                        }
//                    } else if (position == 1) {
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/ar/shipping/");
//                        } else {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/en/shipping/");
//                        }
//                    } else if (position == 2) {
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/ar/size-guide/");
//                        } else {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/en/size-guide/");
//                        }
//                    } else if (position == 3) {
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/ar/payment-methods/");
//                        } else {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/en/payment-methods/");
//                        }
//                    } else if (position == 4) {
//                        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/ar/faqs/");
//                        } else {
//                            bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/en/faqs/");
//                        }
//                    }
//
//                    bundle.putString(WebviewActivity.BNDL_TITLE, mOptionList.get(position));
//                    IntentHandler.startActivity(getActivity(), WebviewActivity.class, bundle);
//
//                }
//
//            }
//        });

    }

    private void InitilizeControlsAction() {
        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Activity getActivity() {
        return InfoOptionActivity.this;
    }
}
