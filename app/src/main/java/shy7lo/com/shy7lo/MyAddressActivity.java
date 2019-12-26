package shy7lo.com.shy7lo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.adapter.MyAddressAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.ApiResponse;
import shy7lo.com.shy7lo.model.MyAddressResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.Utils;

public class MyAddressActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.lvAddress)
    ListView lvAddress;
    @BindView(R.id.lvAdditionalAddress)
    ListView lvAdditionalAddress;
    @BindView(R.id.tvAddAddress)
    TextView tvAddAddress;
    @BindView(R.id.tvNoAdditionalAddress)
    TextView tvNoAdditionalAddress;
    @BindView(R.id.tvNoAddress)
    TextView tvNoAddress;

    public List<MyAddressResponse.Address> mAddressList = null;
    public List<MyAddressResponse.Address> mMyAddressList = null;
    public List<MyAddressResponse.Address> mMyAdditionalAddressList = null;

    MyAddressAdapter mMyAddressAdapter, mMyAdditionalAddressAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_my_address);
        ButterKnife.bind(this);

        InitializeControls();
        InitializeControlsAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAddress();
    }

    private void InitializeControls() {

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            lvAddress.setScaleX(-1f);
            lvAdditionalAddress.setScaleX(-1f);
        } else {
            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
            lvAddress.setScaleX(1f);
            lvAdditionalAddress.setScaleX(1f);
        }

        mMyAddressAdapter = new MyAddressAdapter(getActivity());
        lvAddress.setAdapter(mMyAddressAdapter);

        mMyAdditionalAddressAdapter = new MyAddressAdapter(getActivity());
        lvAdditionalAddress.setAdapter(mMyAdditionalAddressAdapter);
    }

    private Context getActivity() {
        return MyAddressActivity.this;
    }

    private void InitializeControlsAction() {
        ibCancel.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ibCancel) {
            finish();
        } else if (view == tvAddAddress) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.BNDL_ADDRESS_SIZE, (mAddressList == null ? 0 : mAddressList.size()));
            bundle.putBoolean(Constant.BNDL_IS_FROM_SHIPMENT, false);
            IntentHandler.startActivity(getActivity(), AddAddressActivity.class, bundle);
        }
    }

    private void getAllAddress() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getAllAddress();
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(getActivity());
        ApiInterface serviceAPI =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        Call<MyAddressResponse> call = serviceAPI.getAllAddress(Shy7lo.mLangCode, "Bearer " + userToken);
        call.enqueue(new Callback<MyAddressResponse>() {
            @Override
            public void onResponse(Call<MyAddressResponse> call, Response<MyAddressResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    MyAddressResponse mMyAddressResponse = response.body();
                    if (mMyAddressResponse != null && mMyAddressResponse.success == 1) {
                        mAddressList = mMyAddressResponse.data;
//                        mMyAddressAdapter.addAll(mAddressList);

                        mMyAddressList = new ArrayList<>();
                        mMyAdditionalAddressList = new ArrayList<>();
                        for (int i = 0; i < mAddressList.size(); i++) {
                            if (mAddressList.get(i).defaultBilling || mAddressList.get(i).defaultShipping) {
                                mMyAddressList.add(mAddressList.get(i));
                            } else {
                                mMyAdditionalAddressList.add(mAddressList.get(i));
                            }
                        }

                        mMyAddressAdapter.addAll(mMyAddressList);
                        mMyAdditionalAddressAdapter.addAll(mMyAdditionalAddressList);

                        if (mAddressList != null && mAddressList.size() > 0) {
                            tvNoAddress.setVisibility(View.GONE);

                            if (mMyAdditionalAddressList != null && mMyAdditionalAddressList.size() > 0) {
                                tvNoAdditionalAddress.setVisibility(View.GONE);
                            } else {
                                tvNoAdditionalAddress.setVisibility(View.VISIBLE);
                            }

                        } else {
                            tvNoAddress.setVisibility(View.VISIBLE);
                        }

                    } else if (mMyAddressResponse != null && mMyAddressResponse.success == 0) {
                        Utils.showToast(getActivity(), "" + mMyAddressResponse.message);
                    } else {
                        Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
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
                }
            }

            @Override
            public void onFailure(Call<MyAddressResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });
    }

    public void deleteAddress(final MyAddressResponse.Address mAddress, final int position) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_alert2);

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        TextView tvMsgText = (TextView) dialog.findViewById(R.id.tvMsgText);

        tvMsgText.setText("" + getString(R.string.msg_delete_address));

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }


                if (!Utils.isInternetConnected(getActivity())) {
                    Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                        @Override
                        public void onRetryClicked(Dialog dialog) {
                            if (Utils.isInternetConnected(getActivity())) {
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                deleteAddress(mAddress, position);
                            }
                        }
                    });
                    return;
                }

                Utils.showProgressDialog(getActivity());
                ApiInterface serviceAPI =
                        RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

                String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
                Call<ApiResponse> call = serviceAPI.deleteAddress(Shy7lo.mLangCode, "Bearer " + userToken, "" + mAddress.id);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        Utils.closeProgressDialog();

                        if (response.isSuccessful()) {
                            ApiResponse mApiResponse = response.body();
                            if (mApiResponse != null && mApiResponse.success == 1) {

                                if (mMyAdditionalAddressList != null && mMyAdditionalAddressList.size() > 0) {
                                    mMyAdditionalAddressList.remove(position);
                                    mMyAdditionalAddressAdapter.addAll(mMyAdditionalAddressList);
                                }

                                if (mMyAdditionalAddressList != null && mMyAdditionalAddressList.size() > 0) {
                                    tvNoAdditionalAddress.setVisibility(View.GONE);
                                } else {
                                    tvNoAdditionalAddress.setVisibility(View.VISIBLE);
                                }

//                                if (mAddressList != null && mAddressList.size() > 0) {
//                                    tvNoAddress.setVisibility(View.GONE);
//                                } else {
//                                    tvNoAddress.setVisibility(View.VISIBLE);
//                                }

                                Utils.showToast(getActivity(), "" + mApiResponse.message);

                            } else if (mApiResponse != null && mApiResponse.success == 0) {
                                Utils.showToast(getActivity(), "" + mApiResponse.message);
                            } else {
                                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                            }
                        }else{
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
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Utils.closeProgressDialog();
                    }
                });

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
