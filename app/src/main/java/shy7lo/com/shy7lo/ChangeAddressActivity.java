package shy7lo.com.shy7lo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.adapter.AddressAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.MyAddressResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.IntentHandler;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

public class ChangeAddressActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDone)
    TextView tvDone;
    @BindView(R.id.tvAddAddress)
    TextView tvAddAddress;
    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.lvAddress)
    ListView lvAddress;

    List<MyAddressResponse.Address> mAddressList;
    MyAddressResponse.Address mAddress;

    AddressAdapter mMyAddressAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        ButterKnife.bind(this);

        InitializeControls();
        InitializeControlsAction();
    }

    private void InitializeControls() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constant.BNDL_ADDRESS)) {
            mAddress = new Gson().fromJson(bundle.getString(Constant.BNDL_ADDRESS), MyAddressResponse.Address.class);
        }

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            tvDone.setScaleX(-1f);
            lvAddress.setScaleX(-1f);
        } else {
            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
            tvDone.setScaleX(1f);
            lvAddress.setScaleX(1f);
        }

        mMyAddressAdapter = new AddressAdapter(getActivity());
        lvAddress.setAdapter(mMyAddressAdapter);

    }

    private Context getActivity() {
        return ChangeAddressActivity.this;
    }

    private void InitializeControlsAction() {

        ibBack.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAddress();
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
                        int mPosition = 0;
                        for (int j = 0; j < mAddressList.size(); j++) {
                            LogUtils.e("", mAddressList.get(j).id + " " + mAddress.id + " " + (mAddressList.get(j).id.equals(mAddress.id)));
                            if (mAddressList.get(j).id.equals(mAddress.id)) {
                                mAddressList.get(j).isSelected = true;
                                mPosition = j;
                            } else {
                                mAddressList.get(j).isSelected = false;
                            }
                        }

                        mMyAddressAdapter.addAll(mAddressList);
                        lvAddress.setSelection(mPosition);

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

    @Override
    public void onClick(View view) {
        if (view == tvDone) {
            if (mMyAddressAdapter != null) {
                MyAddressResponse.Address mAddress = mMyAddressAdapter.getSelectedAddress();
                if (mAddress != null) {
                    Intent back = new Intent();
                    back.putExtra("id", "" + mAddress.id);
                    back.putExtra("firstname", "" + mAddress.firstname);
                    back.putExtra("lastname", "" + mAddress.lastname);
                    if (mAddress.street != null && mAddress.street.size() > 0) {
                        back.putExtra("street", "" + mAddress.street.get(0));
                    } else {
                        back.putExtra("street", "");
                    }

                    back.putExtra("city", "" + mAddress.city);
//                    back.putExtra("postcode", "" + mAddress.postcode);
                    back.putExtra("country_id", "" + mAddress.countryId);
                    back.putExtra("default_shipping", "" + mAddress.defaultShipping);
                    back.putExtra("default_billing", "" + mAddress.defaultBilling);
                    back.putExtra("telephone", mAddress.telephone.trim());
                    setResult(RESULT_OK, back);
                    finish();
                } else {
                    Utils.showToast(getActivity(), getString(R.string.select_address));
                }
            }
        } else if (view == tvAddAddress) {

            Bundle bundle = new Bundle();
            bundle.putInt(Constant.BNDL_ADDRESS_SIZE, mAddressList.size());
            IntentHandler.startActivity(getActivity(), AddAddressActivity.class, bundle);

        } else if (view == ibBack) {
            finish();
        }
    }

    public void setAddress(MyAddressResponse.Address address) {
        mAddress = address;
    }
}
