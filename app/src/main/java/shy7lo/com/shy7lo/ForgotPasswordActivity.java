package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by JITEN-PC on 13-05-2017.
 */

public class ForgotPasswordActivity extends Activity {

    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvRequestPasswordTitle)
    TextView tvRequestPasswordTitle;
    @BindView(R.id.tvRequestPassword)
    TextView tvRequestPassword;
    @BindView(R.id.etEmail)
    TextView etEmail;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_forgot_password);
        Utils.setLocale(getActivity());
        ButterKnife.bind(getActivity());
        InitializeControls();
        InitializeControlsAction();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        mFirebaseAnalytics.setMinimumSessionDuration(500);
        mFirebaseAnalytics.setSessionTimeoutDuration(1000000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Shy7lo.setActivityContext(getActivity());
//        if (BuildConfig.DEBUG) {
//            mFirebaseAnalytics.setAnalyticsCollectionEnabled(false);
//        } else {
            mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
//        }
        mFirebaseAnalytics.logEvent("Forgot_Password", new Bundle());
    }

    private void InitializeControls() {

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);

            etEmail.setGravity(Gravity.RIGHT);
            tvRequestPasswordTitle.setGravity(Gravity.RIGHT);
        } else {
//            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);
            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);

            etEmail.setGravity(Gravity.LEFT);
            tvRequestPasswordTitle.setGravity(Gravity.LEFT);
        }

    }

    private void InitializeControlsAction() {

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvRequestPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etEmail.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_email_address));
                    return;
                }

                if (!Utils.isValidEmail(etEmail.getText().toString())) {
                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_email_not_valid));
                    return;
                }

                forgotPassword();

            }
        });

    }

    private void forgotPassword() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        forgotPassword();
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(getActivity());

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("email", "" + etEmail.getText().toString());

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString());

        Call<JsonElement> callCode = apiService.forgotPassword(Shy7lo.mLangCode, body);

        callCode.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Utils.closeProgressDialog();
                try {
                    if (response.isSuccessful()) {


                        JSONObject jResponse = new JSONObject(response.body().toString());
                        if (jResponse != null && jResponse.getString("success").equals("1")) {

                            etEmail.setText("");

                            Utils.showToast(getActivity(), "" + getString(R.string.msg_password_reset));

                            finish();

                        } else if (jResponse != null && jResponse.getString("success").equals("0")) {
                            Utils.showToast(getActivity(), "" + jResponse.getString("message"));
                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            return;
                        } else {
                            Utils.showToast(getActivity(), "" + getString(R.string.msg_something_wrong));
                        }


                    } else {
                        if (String.valueOf(response.code()).startsWith("5")) {
                            Utils.showAlertDialog(getActivity(), "" + response.code());
                        } else {

                            try {
                                JSONObject jResponse = new JSONObject(response.errorBody().string());
                                if (jResponse != null && jResponse.getString("success").equals("0")) {
                                    Utils.showToast(getActivity(), "" + jResponse.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
//                        Utils.showAlertDialog(getActivity(), "" + response.code());
//                        JSONObject jResponse = new JSONObject(response.errorBody().string());
//                        if (jResponse != null && jResponse.getString("success").equals("0")) {
//                            Utils.showToast(getActivity(), "" + jResponse.getString("message"));
//                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showAlertDialog(getActivity(), "" + response.code());
                }


            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Utils.closeProgressDialog();
                Utils.showAlertDialog(getActivity(), "" + t.getMessage());
//                Utils.showToast(getActivity(), "" + getString(R.string.msg_something_wrong));
            }
        });

    }

    private Activity getActivity() {
        return ForgotPasswordActivity.this;
    }
}
