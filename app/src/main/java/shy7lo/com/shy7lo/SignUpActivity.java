package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.plugin.AdjustCriteo;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.Calendar;
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
import shy7lo.com.shy7lo.utils.DateFormate;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 27-03-2018.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTitleSecondnary)
    TextView tvTitleSecondnary;
    @BindView(R.id.tvGoogleLogin)
    TextView tvGoogleLogin;
    @BindView(R.id.tvTwitterLogin)
    TextView tvTwitterLogin;
    @BindView(R.id.tvInstaLogin)
    TextView tvInstaLogin;
    @BindView(R.id.tvFacebookLogin)
    TextView tvFacebookLogin;
    @BindView(R.id.tvErrName)
    TextView tvErrName;
    @BindView(R.id.tvErrEmail)
    TextView tvErrEmail;
    @BindView(R.id.tvErrPassword)
    TextView tvErrPassword;
    @BindView(R.id.tvSignUp)
    TextView tvSignUp;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etTelephone)
    EditText etTelephone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.lnrWomen)
    LinearLayout lnrWomen;
    @BindView(R.id.cbWomen)
    CheckBox cbWomen;
    @BindView(R.id.tvWomen)
    TextView tvWomen;
    @BindView(R.id.lnrMen)
    LinearLayout lnrMen;
    @BindView(R.id.cbMen)
    CheckBox cbMen;
    @BindView(R.id.tvMen)
    TextView tvMen;
    @BindView(R.id.tvDateOfBirthTitle)
    TextView tvDateOfBirthTitle;
    @BindView(R.id.tvDateBirth)
    TextView tvDateBirth;

    Calendar mCalNow;
    int startYear, startMonth, startDay;
    DatePickerDialog mDatePickerDialog;

    private Activity getActivity() {
        return SignUpActivity.this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Utils.setLocale(this);
        ButterKnife.bind(getActivity());

        InitializeConrols();
        InitializeConrolsAction();
    }

    private void InitializeConrols() {

        mCalNow = Calendar.getInstance();
        startYear = mCalNow.get(Calendar.YEAR);
        startMonth = mCalNow.get(Calendar.MONTH);
        startDay = mCalNow.get(Calendar.DAY_OF_MONTH);

        mDatePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                startYear = year;
                startMonth = month;
                startDay = day;
                mCalNow.set(Calendar.YEAR, startYear);
                mCalNow.set(Calendar.MONTH, startMonth);
                mCalNow.set(Calendar.DAY_OF_MONTH, startDay);
                tvDateBirth.setText("" + DateFormate.sdfGender.format(mCalNow.getTime()));
            }
        }, startYear, startMonth, startDay);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            etEmail.setScaleX(-1);
//            etPassword.setScaleX(-1);
//            lnrSocial.setScaleX(-1);
//            tvTwitterLogin.setScaleX(-1);
//            tvInstaLogin.setScaleX(-1);
//            tvFacebookLogin.setScaleX(-1);
            lnrWomen.setScaleX(-1);
            tvWomen.setScaleX(-1);
            lnrMen.setScaleX(-1);
            tvMen.setScaleX(-1);

            etFirstName.setGravity(Gravity.RIGHT);
            etLastName.setGravity(Gravity.RIGHT);
            etEmail.setGravity(Gravity.RIGHT);
            etPassword.setGravity(Gravity.RIGHT);
            etTelephone.setGravity(Gravity.RIGHT);
            tvDateBirth.setGravity(Gravity.RIGHT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvDateOfBirthTitle.getLayoutParams();
            params.gravity = Gravity.RIGHT;
            tvDateOfBirthTitle.setLayoutParams(params);

            tvDateBirth.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_left, 0, 0, 0);
        } else {
//            etEmail.setScaleX(1);
//            etPassword.setScaleX(1);
//            lnrSocial.setScaleX(1);
//            tvTwitterLogin.setScaleX(1);
//            tvInstaLogin.setScaleX(1);
//            tvFacebookLogin.setScaleX(1);
            lnrWomen.setScaleX(1);
            tvWomen.setScaleX(1);
            lnrMen.setScaleX(1);
            tvMen.setScaleX(1);

            etFirstName.setGravity(Gravity.LEFT);
            etLastName.setGravity(Gravity.LEFT);
            etEmail.setGravity(Gravity.LEFT);
            etPassword.setGravity(Gravity.LEFT);
            etTelephone.setGravity(Gravity.LEFT);
            tvDateBirth.setGravity(Gravity.LEFT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvDateOfBirthTitle.getLayoutParams();
            params.gravity = Gravity.LEFT;
            tvDateOfBirthTitle.setLayoutParams(params);

            tvDateBirth.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_right, 0);
        }


    }

    private void InitializeConrolsAction() {

        ibCancel.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvGoogleLogin.setOnClickListener(this);
        tvTwitterLogin.setOnClickListener(this);
        tvInstaLogin.setOnClickListener(this);
        tvFacebookLogin.setOnClickListener(this);
        lnrWomen.setOnClickListener(this);
        lnrMen.setOnClickListener(this);
        tvDateBirth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == tvSignUp) {
            if (isValidField()) {
                signInUser();
            }
        } else if (view == ibCancel) {
            finish();
        } else if (view == tvLogin) {
            IntentHandler.startActivity(getActivity(), SignInActivity.class);
            finish();
        } else if (view == tvGoogleLogin) {

        } else if (view == tvTwitterLogin) {

        } else if (view == tvInstaLogin) {

        } else if (view == tvFacebookLogin) {

        } else if (view == lnrWomen) {
            cbWomen.setChecked(true);
            cbMen.setChecked(false);
        } else if (view == lnrMen) {
            cbWomen.setChecked(false);
            cbMen.setChecked(true);
        } else if (view == tvDateBirth) {
            mDatePickerDialog.show();
        }

    }

    private boolean isValidField() {

        if (TextUtils.isEmpty(etFirstName.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.msg_firstname));
            return false;
        } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.msg_lastname));
            return false;
        } else if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_email_address));
            return false;
        } else if (!Utils.isValidEmail(etEmail.getText().toString().trim())) {
            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_email_not_valid));
            return false;
        } else if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_password));
            return false;
        } else if (etPassword.getText().toString().length() < 8) {
            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_password_min_char));
            return false;
        }
//        else if (!Utils.isValidPassword(etPassword.getText().toString())) {
//            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_password_valid_char));
//            return false;
//        }
        else if (TextUtils.isEmpty(etTelephone.getText().toString().trim())) {
            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_telephone));
            return false;
        } else if (TextUtils.isEmpty(tvDateBirth.getText().toString()) && tvDateBirth.getText().toString().equalsIgnoreCase("" + getString(R.string.date_birth))) {
            Utils.showToast(getActivity(), "" + getString(R.string.msg_select_dob));
            return false;
        } else if (!cbMen.isChecked() && !cbWomen.isChecked()) {
            Utils.showToast(getActivity(), "" + getString(R.string.msg_select_gender));
            return false;
        }

        return true;
    }

    String gender = "";

    private void signInUser() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        signInUser();
                    }
                }
            });
            return;
        }


        if (cbMen.isChecked()) {
            gender = "male";
        } else if (cbWomen.isChecked()) {
            gender = "female";
        }

        String dob = "";
        if (!tvDateBirth.getText().toString().equalsIgnoreCase(getString(R.string.date_birth)) && tvDateBirth.getText().toString().contains("-")) {
            dob = tvDateBirth.getText().toString();
        }

        Utils.showProgressDialog(getActivity());
        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Map<String, Object> jsonParams = new android.support.v4.util.ArrayMap<>();
        jsonParams.put("firstname", etFirstName.getText().toString());
        jsonParams.put("lastname", etLastName.getText().toString());
        jsonParams.put("email", etEmail.getText().toString());
        jsonParams.put("password", etPassword.getText().toString());
        jsonParams.put("telephone", etTelephone.getText().toString());
        jsonParams.put("subscribe", false);
        jsonParams.put("gender", "" + gender);
        jsonParams.put("dob", "" + dob);
        String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
        jsonParams.put("cart_id", "" + guestToken);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        Call<JsonElement> productListCall = serviceAPI.getSignUp(Shy7lo.mLangCode, body);


        productListCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Utils.closeProgressDialog();
                try {
                    if (response.isSuccessful()) {
                        JsonElement jsonElement = response.body();

                        JSONObject jResponse = new JSONObject(jsonElement.toString());
                        if (jResponse != null && jResponse.getString("success").equals("1")) {

                            JSONObject jData = jResponse.getJSONObject("data");
                            if (jData != null && jData.has("token")) {
                                String userToken = jData.getString("token");
                                if (!TextUtils.isEmpty(userToken)) {
                                    MyPref.setPref(getActivity(), MyPref.USER_CART_ID, "" + userToken);
                                } else {
                                    MyPref.setPref(getActivity(), MyPref.USER_CART_ID, "");
                                }

                                MyPref.setPref(getActivity(), MyPref.GUEST_CART_ID, "");

                                int mCartCount = jData.getJSONObject("customerDetails").getInt("cart_count");
                                MyPref.setPref(getActivity(), MyPref.CART_ITEM_COUNT, mCartCount);

                                String userID = jData.getJSONObject("customerDetails").getString("id");
                                String email = jData.getJSONObject("customerDetails").getString("email");
                                String firstname = jData.getJSONObject("customerDetails").getString("firstname");
                                String lastname = jData.getJSONObject("customerDetails").getString("lastname");

                                // fresh chat
                                FreshchatUser freshUser = Freshchat.getInstance(getActivity()).getUser();
                                freshUser.setFirstName("" + firstname);
                                freshUser.setLastName("" + lastname);
                                freshUser.setEmail("" + email);
                                freshUser.setPhone("+" + (MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "").replace("+", "")), "" + MyPref.getPref(getActivity(), MyPref.USER_PHONE, ""));
                                Freshchat.getInstance(getActivity()).setUser(freshUser);

//                                if (RestClient.isTuneEnable) {
//                                    try {
//
//                                        Tune tune = Tune.getInstance();
//                                        tune.setUserEmail("" + email);
//                                        tune.setUserName("" + firstname + " " + lastname);
//                                        tune.setUserId("" + userID);
//                                        tune.measureEvent(TuneEvent.REGISTRATION);
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }

                                if (RestClient.isFacebookLive) {
                                    try {
                                        // FB Log Registration
                                        AppEventsLogger logger = AppEventsLogger.newLogger(getActivity());

                                        Bundle parameters = new Bundle();
                                        parameters.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, "" + MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, MyPref.LANGUAGE_Arabic));

                                        logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, parameters);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                try {
                                    AdjustEvent event = new AdjustEvent("3n05qy");
                                    event.addPartnerParameter("User ID", userID);
                                    event.addPartnerParameter("Name", firstname.trim() + "" + lastname.trim());
                                    event.addPartnerParameter("Email", "" + "" + email.trim());
//                                    event.addPartnerParameter("Country", "" + "" + mCountrty);
                                    event.addPartnerParameter("Gender", "" + "" + gender);
                                    event.addPartnerParameter("Mobile No", "" + "" + etTelephone.getText().toString().trim());

                                    //callback
                                    event.addCallbackParameter("User ID", userID);
                                    event.addCallbackParameter("Name", firstname.trim() + "" + lastname.trim());
                                    event.addCallbackParameter("Email", "" + "" + email.trim());
//                                    event.addCallbackParameter("Country", "" + "" + mCountrty);
                                    event.addCallbackParameter("Gender", "" + "" + gender);
                                    event.addCallbackParameter("Mobile No", "" + "" + etTelephone.getText().toString().trim());

                                    String mCriteo = MyPref.getPref(getActivity(), MyPref.APP_CRITEO, "");
                                    if (TextUtils.isEmpty(mCriteo) || mCriteo.equalsIgnoreCase("1")) {
                                        String userToken1 = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
                                        AdjustCriteo.injectCustomerIdIntoCriteoEvents(TextUtils.isEmpty(userToken1) ? userToken1 : MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, ""));
                                    }

                                    Adjust.trackEvent(event);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (getActivity() instanceof HomeActivity) {
                                    HomeActivity activity = (HomeActivity) getActivity();
                                    activity.updateBadgetCount();
                                    activity.updateWishListBadgetCount();
                                }

                                MyPref.setPref(getActivity(), MyPref.USER_ID, userID);
                                MyPref.setPref(getActivity(), MyPref.USER_EMAIL, email);
                                MyPref.setPref(getActivity(), MyPref.USER_PWD, etPassword.getText().toString());
                                MyPref.setPref(getActivity(), MyPref.USER_FIRSTNAME, firstname);
                                MyPref.setPref(getActivity(), MyPref.USER_LASTNAME, lastname);

                                try {

                                    if (jData.getJSONObject("customerDetails").has("addresses")
                                            && jData.getJSONObject("customerDetails").getJSONArray("addresses") != null
                                            && jData.getJSONObject("customerDetails").getJSONArray("addresses").length() > 0) {
                                        JSONObject jAddress = jData.getJSONObject("customerDetails").getJSONArray("addresses").getJSONObject(0);
                                        if (jAddress != null && jAddress.length() > 0) {
                                            String telephone = jAddress.getString("telephone");
                                            String city = jAddress.getString("city");
                                            String address = jAddress.getJSONArray("street").getString(0);
                                            LogUtils.e("", "telephone::" + telephone);
                                            if (!TextUtils.isEmpty(telephone) && telephone.contains(" ")) {
                                                telephone = telephone.split(" ")[1];
                                                LogUtils.e("", "after telephone::" + telephone);
                                            }


                                            MyPref.setPref(getActivity(), MyPref.USER_PHONE, telephone);
                                            MyPref.setPref(getActivity(), MyPref.USER_ADDRESS, address);
                                            MyPref.setPref(getActivity(), MyPref.USER_CITY, city);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            int mCountryPosition = 1;
                            String mCountryCode = "+966";
                            String mCountryId = "SA";
                            float mExchangeRate = 1f;
                            String mCurrencyCode = "SAR";
                            String mCurrencyArCode = "ر.س";

                            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                                MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mCurrencyArCode);
                            } else {
                                MyPref.setPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "" + mCurrencyCode);
                            }
                            LogUtils.e("", "DEFAULT_CURRENCY_CODE::" + MyPref.getPref(getActivity(), MyPref.DEFAULT_CURRENCY_CODE, "SAR"));

                            MyPref.setPref(getActivity(), MyPref.DEFAULT_ENGLISH_CURRENCY_CODE, "" + mCurrencyCode);
                            MyPref.setPref(getActivity(), MyPref.DEFAULT_ARABIC_CURRENCY_CODE, "" + mCurrencyArCode);
                            MyPref.setPref(getActivity(), MyPref.DEFAULT_EXCHANGE_RATE, mExchangeRate);
                            MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_POSITION, mCountryPosition);
                            MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "" + mCountryCode);
                            MyPref.setPref(getActivity(), MyPref.DEFAULT_COUNTRY_ID, "" + mCountryId);

                            MyPref.setPref(getActivity(), MyPref.DEFAULT_CATEGORY_POSITION, 0);

                            MyPref.setPref(getActivity(), MyPref.IS_FIRST_TIME, false);

                            sendBroadcast(new Intent(LandingLanguageActivity.FILTER_FINISH));

                            IntentHandler.startActivity(getActivity(), HomeActivity.class);
//                            IntentHandler.startActivity(getActivity(), InitialScreenActivity.class);
                            finish();


                        } else if (jResponse != null && jResponse.getString("success").equals("0")) {
                            Utils.showToast(getActivity(), response.code() + " " + jResponse.getString("message"));
                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            return;
                        } else {
                            Utils.showToast(getActivity(), response.code() + " " + getResources().getString(R.string.msg_something_wrong));
                        }


                        LogUtils.e("", "Response is great::" + jsonElement);
                    } else {
                        if (String.valueOf(response.code()).startsWith("5")) {
                            Utils.showAlertDialog(getActivity(), "" + response.code());
                        } else {
                            JSONObject jResponse = new JSONObject(response.errorBody().string());
                            if (jResponse != null && jResponse.getString("success").equals("0")) {
                                Utils.showToast(getActivity(), "" + jResponse.getString("message"));
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showAlertDialog(getActivity(), "" + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                System.out.println(t.getMessage());
                Utils.closeProgressDialog();
                Utils.showAlertDialog(getActivity(), "" + t.getMessage());
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
            }
        });

    }
}
