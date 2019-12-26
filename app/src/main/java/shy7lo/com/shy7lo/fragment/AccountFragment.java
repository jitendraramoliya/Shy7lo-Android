package shy7lo.com.shy7lo.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.AccountSettingActivity;
import shy7lo.com.shy7lo.AppInfoActivity;
import shy7lo.com.shy7lo.HomeActivity;
import shy7lo.com.shy7lo.InfoOptionActivity;
import shy7lo.com.shy7lo.MyAddressActivity;
import shy7lo.com.shy7lo.MyDetailsActivity;
import shy7lo.com.shy7lo.MyOrderActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.WebviewActivity;
import shy7lo.com.shy7lo.adapter.HelpAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.database.DBAdapter;
import shy7lo.com.shy7lo.model.CMSPage;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.pref.MyPref.CART_ITEM_COUNT;

/**
 * Created by Jiten on 26-06-2018.
 */

public class AccountFragment extends Fragment implements View.OnClickListener {

    public static String TAG_ACCOUNT_FRAGMENT = "AccountFragment";
    public static int RC_SELECT_LANGUAGE = 5010;
    public static int RC_MY_ORDER = 5011;

    View mView;

    @BindView(R.id.rltTopLayout)
    RelativeLayout rltTopLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
//    @BindView(R.id.tvComeOn)
//    TextView tvComeOn;
//    @BindView(R.id.tvUpdateDetails)
//    TextView tvUpdateDetails;
    @BindView(R.id.tvSignIn)
    TextView tvSignIn;
    @BindView(R.id.tvJoin)
    TextView tvJoin;
    @BindView(R.id.tvNeedHelp)
    TextView tvNeedHelp;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvNicName)
    TextView tvNicName;
    @BindView(R.id.tvMyOrders)
    TextView tvMyOrders;
    @BindView(R.id.tvMyDetails)
    TextView tvMyDetails;
    @BindView(R.id.tvAddressBook)
    TextView tvAddressBook;
    @BindView(R.id.tvNeedHelpOption)
    TextView tvNeedHelpOption;
    @BindView(R.id.tvSignOut)
    TextView tvSignOut;

    @BindView(R.id.ibMore)
    ImageButton ibMore;
    @BindView(R.id.ibSettings)
    ImageButton ibSettings;
    @BindView(R.id.lnrMyAccount)
    LinearLayout lnrMyAccount;
    @BindView(R.id.lnrAccountJoin)
    LinearLayout lnrAccountJoin;
    @BindView(R.id.lnrAccountContainer)
    LinearLayout lnrAccountContainer;


    DBAdapter dbAdapter;

    Animation animClose, animOpen;

    private static AccountFragment mAccountFragment;

    public static AccountFragment getInstance() {
        if (mAccountFragment == null) {
            mAccountFragment = new AccountFragment();
        }
        return mAccountFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_account, container, false);
        Utils.setLocale(getActivity());
        ButterKnife.bind(this, mView);
        InitializeControls();
        InitializeControlsAction();

        return mView;
    }

    private void InitializeControls() {

        animOpen = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_open);
        animClose = AnimationUtils.loadAnimation(getActivity(),
                R.anim.animation_dialog_close);

        setView();
    }

    private void InitializeControlsAction() {

        dbAdapter = new DBAdapter(getActivity());

        ibMore.setOnClickListener(this);
        ibSettings.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);
        tvJoin.setOnClickListener(this);
        tvNeedHelp.setOnClickListener(this);
        tvMyOrders.setOnClickListener(this);
        tvMyDetails.setOnClickListener(this);
        tvAddressBook.setOnClickListener(this);
        tvNeedHelpOption.setOnClickListener(this);
        tvSignOut.setOnClickListener(this);

        setLanguageView();

    }

    public void setView() {

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        String userId = MyPref.getPref(getActivity(), MyPref.USER_ID, "");
        LogUtils.e("", "setSignoutView::" + userId);

//        if (!TextUtils.isEmpty(userId)) {
        if (!TextUtils.isEmpty(userToken) && !TextUtils.isEmpty(userId)) {

            lnrMyAccount.setVisibility(View.VISIBLE);
            lnrAccountJoin.setVisibility(View.GONE);

            String mFirstName = MyPref.getPref(getActivity(), MyPref.USER_FIRSTNAME, "");
            String mLastName = MyPref.getPref(getActivity(), MyPref.USER_LASTNAME, "");

            tvName.setText(mFirstName + " " + mLastName);

            if (!TextUtils.isEmpty(mFirstName) && !TextUtils.isEmpty(mLastName)) {
                tvNicName.setText(mFirstName.charAt(0) + "" + mLastName.charAt(0));
            } else if (!TextUtils.isEmpty(mFirstName)) {
                tvNicName.setText(mFirstName.charAt(0));
            }

        } else {
            lnrMyAccount.setVisibility(View.GONE);
            lnrAccountJoin.setVisibility(View.VISIBLE);
        }
        if (getActivity() instanceof HomeActivity) {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.setSignoutView();
        }
    }

    private void setLanguageView() {

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            rltTopLayout.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            lnrMyAccount.setScaleX(-1f);
            tvName.setScaleX(-1f);
            tvNicName.setScaleX(-1f);
            lnrAccountContainer.setScaleX(-1f);

            lnrAccountContainer.setGravity(Gravity.RIGHT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvNeedHelp.getLayoutParams();
            params.gravity = Gravity.RIGHT;
            tvNeedHelp.setLayoutParams(params);

//            LinearLayout.LayoutParams paramsAccount = (LinearLayout.LayoutParams) lnrAccountContainer.getLayoutParams();
//            paramsAccount.gravity = Gravity.RIGHT;
//            lnrAccountContainer.setLayoutParams(paramsAccount);

        } else {
            rltTopLayout.setScaleX(1f);
            tvTitle.setScaleX(1f);
            lnrMyAccount.setScaleX(1f);
            tvName.setScaleX(1f);
            tvNicName.setScaleX(1f);
            lnrAccountContainer.setScaleX(1f);

            lnrAccountContainer.setGravity(Gravity.LEFT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvNeedHelp.getLayoutParams();
            params.gravity = Gravity.LEFT;
            tvNeedHelp.setLayoutParams(params);

//            LinearLayout.LayoutParams paramsAccount = (LinearLayout.LayoutParams) lnrAccountContainer.getLayoutParams();
//            paramsAccount.gravity = Gravity.LEFT;
//            lnrAccountContainer.setLayoutParams(paramsAccount);
        }

        tvTitle.setText(getString(R.string.account));
//        tvComeOn.setText(getString(R.string.come_on));
//        tvUpdateDetails.setText(getString(R.string.update_details));
        tvSignIn.setText(getString(R.string.signin));
        tvJoin.setText(getString(R.string.signup));
        tvNeedHelp.setText(getString(R.string.need_help));
        tvMyOrders.setText(getString(R.string.my_orders));
        tvMyDetails.setText(getString(R.string.my_details));
        tvAddressBook.setText(getString(R.string.address_book));
        tvNeedHelpOption.setText(getString(R.string.need_help));
        tvSignOut.setText(getString(R.string.sign_out));
    }


    @Override
    public void onClick(View view) {

        if (view == ibMore) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.openDrawer();
            }
        } else if (view == ibSettings) {

            IntentHandler.startActivity(getActivity(), AccountSettingActivity.class, RC_SELECT_LANGUAGE);

        } else if (view == tvSignIn) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.openSignIn();
            }
        } else if (view == tvJoin) {
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.openSignUp();
            }
        } else if (view == tvNeedHelp) {

            showHelpDialog();

        } else if (view == tvMyOrders) {
            IntentHandler.startActivity(getActivity(), MyOrderActivity.class, RC_MY_ORDER);
        } else if (view == tvMyDetails) {
            IntentHandler.startActivity(getActivity(), MyDetailsActivity.class);
        } else if (view == tvAddressBook) {
            IntentHandler.startActivity(getActivity(), MyAddressActivity.class);
        } else if (view == tvNeedHelpOption) {

            showHelpDialog();

        } else if (view == tvSignOut) {

            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_alert2);

            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
            TextView tvMsgText = (TextView) dialog.findViewById(R.id.tvMsgText);

            tvMsgText.setText("" + getString(R.string.msg_logout));

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    getGuestCartToken();

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                }
            });

            dialog.show();
        }
    }


    private void getGuestCartToken() {

//        if (!Utils.isInternetConnected(getActivity())) {
//            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
//                @Override
//                public void onRetryClicked(Dialog dialog) {
//                    if (Utils.isInternetConnected(getActivity())) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                        getGuestCartToken();
//                    }
//                }
//            });
//            return;
//        }
//
//        LogUtils.e(Shy7lo.TAG, "getGuestCartToken call");
//
//        ApiInterface apiService =
//                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
//        Call<JsonElement> call = apiService.getGuestCartToken(Shy7lo.mLangCode);
//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                LogUtils.e(Shy7lo.TAG, "response code:" + response.code());
//
//                if (response.isSuccessful()) {
//                    try {
//
//                        JSONObject jResponse = new JSONObject(response.body().toString());
//                        if (jResponse != null && jResponse.getString("success").equalsIgnoreCase("1")) {
//
//                            JSONObject jData = jResponse.getJSONObject("data");
//                            if (jData != null && jData.has("cart_id")) {
//                                String token = jData.getString("cart_id");
//                                LogUtils.e(Shy7lo.TAG, "response token:" + token);
//
//                                if (!TextUtils.isEmpty(token)) {
//                                    MyPref.setPref(getActivity(), MyPref.GUEST_CART_ID, token);
//                                }
//                            }

                            MyPref.setPref(getActivity(), MyPref.USER_CART_ID, "");
                            MyPref.setPref(getActivity(), CART_ITEM_COUNT, 0);
                            MyPref.setPref(getActivity(), MyPref.USER_ID, "");
                            MyPref.setPref(getActivity(), MyPref.USER_FIRSTNAME, "");
                            MyPref.setPref(getActivity(), MyPref.USER_LASTNAME, "");
                            MyPref.setPref(getActivity(), MyPref.USER_PHONE, "");
                            MyPref.setPref(getActivity(), MyPref.USER_ADDRESS, "");
                            MyPref.setPref(getActivity(), MyPref.USER_CITY, "");

//                            MyPref.setPref(getActivity(), MyPref.TOTAL_AMT_LIST, "");
                            dbAdapter.removeAllWishItem();
//                            dbAdapter.removeAllShoppingItem();

                            if (getActivity() instanceof HomeActivity) {
                                HomeActivity activity = (HomeActivity) getActivity();
                                activity.updateBadgetCount();
                                activity.updateWishListBadgetCount();
                            }

                            setView();
//                        } else if (jResponse != null && jResponse.getString("success").equals("2")) {
//                            Utils.showInitialScreen(getActivity());
//                            return;
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                System.out.println(t.getMessage());
//            }
//        });

    }

//    private void showHelpDialog() {
//
////        final Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogSlideAnim));
//        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setContentView(R.layout.dialog_need_help);
//
//        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
//        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
//        final TextView tvHelpFAQ = (TextView) dialog.findViewById(R.id.tvHelpFAQ);
//        final TextView tvInformation = (TextView) dialog.findViewById(R.id.tvInformation);
//        final TextView tvForCustomer = (TextView) dialog.findViewById(R.id.tvForCustomer);
//        final TextView tvAppInfo = (TextView) dialog.findViewById(R.id.tvAppInfo);
//        final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
//
//        rlMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        lnrContainer.startAnimation(animClose);
//                    }
//                }, 10);
//
//            }
//        });
//
//        tvHelpFAQ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Bundle bundle = new Bundle();
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/ar/faqs/");
//                } else {
//                    bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/en/faqs/");
//                }
//                bundle.putString(WebviewActivity.BNDL_TITLE, getString(R.string.help_faqs));
//                IntentHandler.startActivity(getActivity(), WebviewActivity.class, bundle);
//                rlMain.performClick();
//
//            }
//        });
//
//        tvInformation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString(InfoOptionActivity.BNDL_OPTION, InfoOptionActivity.BNDL_OPTION_INFORMATION);
//                IntentHandler.startActivity(getActivity(), InfoOptionActivity.class, bundle);
//                rlMain.performClick();
//            }
//        });
//
//        tvForCustomer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString(InfoOptionActivity.BNDL_OPTION, InfoOptionActivity.BNDL_OPTION_CUSTOMER);
//                IntentHandler.startActivity(getActivity(), InfoOptionActivity.class, bundle);
//                rlMain.performClick();
//            }
//        });
//
//        tvAppInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), AppInfoActivity.class));
//                rlMain.performClick();
//            }
//        });
//
//
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                rlMain.performClick();
//            }
//        });
//
//        animClose.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.dismiss();
//                            dialog.cancel();
//                        }
//                    }
//                }, 10);
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        lnrContainer.startAnimation(animOpen);
//        dialog.show();
//    }

    private void showHelpDialog() {

//        final Dialog dialog = new Dialog(new ContextThemeWrapper(getActivity(), R.style.DialogSlideAnim));
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_need_help_new);

        final RelativeLayout rlMain = (RelativeLayout) dialog.findViewById(R.id.rlMain);
        final LinearLayout lnrContainer = (LinearLayout) dialog.findViewById(R.id.lnrContainer);
        final ListView lvHelp = (ListView) dialog.findViewById(R.id.lvHelp);
        final TextView tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lnrContainer.startAnimation(animClose);
                    }
                }, 10);

            }
        });

        HelpAdapter mHelpAdapter = new HelpAdapter(getActivity(), false);
        lvHelp.setAdapter(mHelpAdapter);

        View mHelpFooter = LayoutInflater.from(getActivity()).inflate(R.layout.layout_footer_help, null);
        TextView tvLiveChat = mHelpFooter.findViewById(R.id.tvLiveChat);
        TextView tvAppInfo = mHelpFooter.findViewById(R.id.tvAppInfo);
        lvHelp.addFooterView(mHelpFooter);

        tvLiveChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
                String userId = MyPref.getPref(getActivity(), MyPref.USER_ID, "");

                if (!TextUtils.isEmpty(userToken) && !TextUtils.isEmpty(userId)) {

                    FreshchatUser freshUser = Freshchat.getInstance(getActivity()).getUser();

                    freshUser.setFirstName("" + MyPref.getPref(getActivity(), MyPref.USER_FIRSTNAME, ""));
                    freshUser.setLastName("" + MyPref.getPref(getActivity(), MyPref.USER_LASTNAME, ""));
                    freshUser.setEmail("" + MyPref.getPref(getActivity(), MyPref.USER_EMAIL, ""));
                    freshUser.setPhone("+" + (MyPref.getPref(getActivity(), MyPref.DEFAULT_COUNTRY_CODE, "").replace("+", "")), "" + MyPref.getPref(getActivity(), MyPref.USER_PHONE, ""));

                    Freshchat.getInstance(getActivity()).setUser(freshUser);

                }

                Freshchat.showConversations(getActivity());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlMain.performClick();
                    }
                }, 1000);


            }
        });

        tvAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AppInfoActivity.class));
                rlMain.performClick();
            }
        });

        lvHelp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CMSPage.Data mCMSPage = Shy7lo.mCMSPage.get(position);
                if (mCMSPage != null) {
                    if (mCMSPage.child != null) {

                        Bundle bundle = new Bundle();
                        bundle.putString(InfoOptionActivity.BNDL_TITLE, mCMSPage.title);
                        bundle.putString(InfoOptionActivity.BNDL_OPTION, new Gson().toJson(mCMSPage.child));
                        IntentHandler.startActivity(getActivity(), InfoOptionActivity.class, bundle);

                    } else {

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

                        Bundle bundle = new Bundle();
                        bundle.putString(WebviewActivity.BNDL_URL, mCMSPage.url);
                        bundle.putString(WebviewActivity.BNDL_TITLE, mCMSPage.title);
                        IntentHandler.startActivity(getActivity(), WebviewActivity.class, bundle);

                    }
                    rlMain.performClick();
                }
            }
        });

        mHelpAdapter.setData(Shy7lo.mCMSPage);

//        tvHelpFAQ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Bundle bundle = new Bundle();
//                if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
//                    bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/ar/faqs/");
//                } else {
//                    bundle.putString(WebviewActivity.BNDL_URL, RestClient.API_NEW_URL + "/en/faqs/");
//                }
//                bundle.putString(WebviewActivity.BNDL_TITLE, getString(R.string.help_faqs));
//                IntentHandler.startActivity(getActivity(), WebviewActivity.class, bundle);
//                rlMain.performClick();
//
//            }
//        });
//
//        tvInformation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString(InfoOptionActivity.BNDL_OPTION, InfoOptionActivity.BNDL_OPTION_INFORMATION);
//                IntentHandler.startActivity(getActivity(), InfoOptionActivity.class, bundle);
//                rlMain.performClick();
//            }
//        });
//
//        tvForCustomer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString(InfoOptionActivity.BNDL_OPTION, InfoOptionActivity.BNDL_OPTION_CUSTOMER);
//                IntentHandler.startActivity(getActivity(), InfoOptionActivity.class, bundle);
//                rlMain.performClick();
//            }
//        });
//
//        tvAppInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), AppInfoActivity.class));
//                rlMain.performClick();
//            }
//        });


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rlMain.performClick();
            }
        });

        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }
                }, 10);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        lnrContainer.startAnimation(animOpen);
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("", TAG_ACCOUNT_FRAGMENT + " requestCode::" + requestCode);

        if (requestCode == RC_SELECT_LANGUAGE) {
            setLanguageView();
            if (getActivity() instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) getActivity();
                activity.setDrawerGravity();
                activity.getCategoryList();
            }
        } else if (requestCode == AccountFragment.RC_MY_ORDER) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                boolean isShopMore = data.getBooleanExtra(Constant.BNDL_IS_SHOP_MORE, false);
                if (isShopMore) {
                    if (getActivity() instanceof HomeActivity) {
                        HomeActivity activity = (HomeActivity) getActivity();
                        activity.showContinueShopping();
                    }
                }
            }
        }
    }
}
