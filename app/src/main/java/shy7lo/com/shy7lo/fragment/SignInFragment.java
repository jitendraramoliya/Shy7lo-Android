package shy7lo.com.shy7lo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.plugin.AdjustCriteo;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatUser;
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
import shy7lo.com.shy7lo.ForgotPasswordActivity;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.sync.WishListSync;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

public class SignInFragment extends Fragment implements View.OnClickListener {

    public static String TAG_SIGNIN_FRAGMENT = "SignInFragment";

    View mView;
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
    @BindView(R.id.tvErrEmail)
    TextView tvErrEmail;
    @BindView(R.id.tvErrPassword)
    TextView tvErrPassword;
    @BindView(R.id.tvSignUp)
    TextView tvSignUp;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.lnrSocial)
    LinearLayout lnrSocial;

    DBAdapter dbAdapter;

    private static SignInFragment mSignInFragment;

    public static Fragment getInstance() {
//        if (mSignInFragment == null) {
        mSignInFragment = new SignInFragment();
//        }
        return mSignInFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_signin, container, false);
        ButterKnife.bind(this, mView);

        InitializeControls();
        InitializeControlsAction();

        return mView;
    }

    private void InitializeControls() {

        dbAdapter = new DBAdapter(getActivity());

        ibCancel.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        tvTitleSecondnary.setVisibility(View.VISIBLE);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//            etEmail.setScaleX(-1);
//            etPassword.setScaleX(-1);
//            lnrSocial.setScaleX(-1);
//            tvTwitterLogin.setScaleX(-1);
//            tvInstaLogin.setScaleX(-1);
//            tvFacebookLogin.setScaleX(-1);

            etEmail.setGravity(Gravity.RIGHT);
            etPassword.setGravity(Gravity.RIGHT);
        } else {
//            etEmail.setScaleX(1);
//            etPassword.setScaleX(1);
//            lnrSocial.setScaleX(1);
//            tvTwitterLogin.setScaleX(1);
//            tvInstaLogin.setScaleX(1);
//            tvFacebookLogin.setScaleX(1);

            etEmail.setGravity(Gravity.LEFT);
            etPassword.setGravity(Gravity.LEFT);
        }

    }

    private void InitializeControlsAction() {

        ibCancel.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvGoogleLogin.setOnClickListener(this);
        tvTwitterLogin.setOnClickListener(this);
        tvInstaLogin.setOnClickListener(this);
        tvFacebookLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == tvSignUp) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.openSignUp();
            }
        } else if (view == tvLogin) {
            if (isValidField()) {
                signInUser(etEmail.getText().toString(), etPassword.getText().toString());
            }
        } else if (view == tvForgotPassword) {
            IntentHandler.startActivity(getActivity(), ForgotPasswordActivity.class);
        } else if (view == tvGoogleLogin) {

        } else if (view == tvTwitterLogin) {

        } else if (view == tvInstaLogin) {

        } else if (view == tvFacebookLogin) {

        }
    }

    private void signInUser(final String email, final String password) {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        signInUser(email, password);
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(getActivity());
        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Map<String, Object> jsonParams = new android.support.v4.util.ArrayMap<>();
        jsonParams.put("email", email);
        jsonParams.put("password", password);
        String guestToken = MyPref.getPref(getActivity(), MyPref.GUEST_CART_ID, "");
        jsonParams.put("cart_id", "" + guestToken);

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        Call<JsonElement> productListCall = serviceAPI.getSignIn(Shy7lo.mLangCode, body);


        productListCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Utils.closeProgressDialog();
                try {
                    if (response.isSuccessful()) {
                        JsonElement jsonElement = response.body();

//                        {"success":1,"message":"login successfully","data":{"token":"mf9t3vhky64tigqsyxp084gbnvi1rfk7","customerDetails":
//                            {"id":62558,"group_id":1,"default_billing":"27485","default_shipping":"27485","created_at":"2017-06-04 13:30:27",
//                                    "updated_at":"2017-07-20 12:29:56","created_in":"English","email":"jiten@gmail.com","firstname":"ndns","lastname":"ndnd",
//                                    "store_id":2,"website_id":2,"addresses":[{"id":27485,"customer_id":62558,"region":{"region_code":null,"region":null,"region_id":0},
//                                "region_id":0,"country_id":"AE","street":["bxbd"],"telephone":"+971 95959","city":"ndnd","firstname":"ndns","lastname":"ndnd",
//                                        "default_shipping":true,"default_billing":true}],"disable_auto_group_change":0,"cart_count":2}}}


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

                                // for offline uncoment this
                                WishListSync mWishListSync = new WishListSync(getActivity());
                                mWishListSync.syncWishList(getActivity(), dbAdapter);

//                                ShoppingBagSync mShoppingBagSync = new ShoppingBagSync(getActivity());
//                                mShoppingBagSync.syncShoppingBagList(getActivity(), dbAdapter);

//                                ArrayList<Wishlist.WishlistData> alWishList = dbAdapter.getGeustWishList();
//                                if (alWishList != null && alWishList.size() > 0) {
//                                    new UploadGeustWishItem(alWishList).execute();
//                                }
//                                if (RestClient.isTuneEnable) {
//                                    try {
//
//                                        Tune tune = Tune.getInstance();
//                                        tune.setUserEmail("" + email);
//                                        tune.setUserName("" + firstname + " " + lastname);
//                                        tune.setUserId("" + userID);
//                                        tune.measureEvent(TuneEvent.LOGIN);
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }


                                try {
                                    AdjustEvent event = new AdjustEvent("ymp0xp");
                                    event.addPartnerParameter("User ID", "" + "" + userID);
                                    event.addPartnerParameter("Email", "" + "" + email);
                                    event.addPartnerParameter("Name", firstname + " " + lastname);
                                    //Callback
                                    event.addCallbackParameter("User ID", "" + "" + userID);
                                    event.addCallbackParameter("Email", "" + "" + email);
                                    event.addCallbackParameter("Name", firstname + " " + lastname);

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
                                MyPref.setPref(getActivity(), MyPref.USER_PWD, password);
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


                            Bundle bundle = new Bundle();

                            HomeActivity activity = (HomeActivity) getActivity();
                            activity.loadAccountWithClearStack(bundle);


                        } else if (jResponse != null && jResponse.getString("success").equals("0")) {
                            Utils.showToast(getActivity(), response.code() + "" + jResponse.getString("message"));
                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.loadShoppingCartsWithClearStack();
                            }
                            return;
                        } else {
                            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                        }


                        LogUtils.e(TAG_SIGNIN_FRAGMENT, "Response is great::" + jsonElement);
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


    private boolean isValidField() {

        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
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

        return true;
    }
}
