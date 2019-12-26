package shy7lo.com.shy7lo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.MyDetailsResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.DateFormate;
import shy7lo.com.shy7lo.utils.Utils;

public class MyDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDone)
    TextView tvDone;
    @BindView(R.id.tvDateOfBirthTitle)
    TextView tvDateOfBirthTitle;
    @BindView(R.id.tvPrefferedShopTitle)
    TextView tvPrefferedShopTitle;
    @BindView(R.id.tvDateBirth)
    TextView tvDateBirth;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
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
    @BindView(R.id.tvGoShy7loTest)
    TextView tvGoShy7loTest;

    Calendar mCalNow;
    int startYear, startMonth, startDay;
    DatePickerDialog mDatePickerDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_my_details);
        ButterKnife.bind(this);

        InitializeControls();
        InitializeControlsAction();
        getMyDetails();
    }

    private void InitializeControls() {

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            rltTitle.setScaleX(-1);
            tvTitle.setScaleX(-1);
            tvDone.setScaleX(-1);
            lnrWomen.setScaleX(-1);
            tvWomen.setScaleX(-1);
            lnrMen.setScaleX(-1);
            tvMen.setScaleX(-1);

            etFirstName.setGravity(Gravity.RIGHT);
            etLastName.setGravity(Gravity.RIGHT);
            tvDateBirth.setGravity(Gravity.RIGHT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvDateOfBirthTitle.getLayoutParams();
            params.gravity = Gravity.RIGHT;
            tvDateOfBirthTitle.setLayoutParams(params);
            tvPrefferedShopTitle.setLayoutParams(params);

            tvDateBirth.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_details_arrow_left, 0, 0, 0);
        } else {
            rltTitle.setScaleX(1);
            tvTitle.setScaleX(1);
            tvDone.setScaleX(1);
            lnrWomen.setScaleX(1);
            tvWomen.setScaleX(1);
            lnrMen.setScaleX(1);
            tvMen.setScaleX(1);

            etFirstName.setGravity(Gravity.LEFT);
            etLastName.setGravity(Gravity.LEFT);
            tvDateBirth.setGravity(Gravity.LEFT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvDateOfBirthTitle.getLayoutParams();
            params.gravity = Gravity.LEFT;
            tvDateOfBirthTitle.setLayoutParams(params);
            tvPrefferedShopTitle.setLayoutParams(params);

            tvDateBirth.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_details_arrow_right, 0);
        }

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
    }

    private void getMyDetails() {
        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getMyDetails();
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(getActivity());
        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        Call<MyDetailsResponse> productDetailsCall = serviceAPI.getMyDetailList(Shy7lo.mLangCode, "Bearer " + userToken);
        productDetailsCall.enqueue(new Callback<MyDetailsResponse>() {
            @Override
            public void onResponse(Call<MyDetailsResponse> call, Response<MyDetailsResponse> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    MyDetailsResponse mMyDetailsResponse = response.body();
                    if (mMyDetailsResponse != null && mMyDetailsResponse.success == 1) {

                        MyDetailsResponse.Details mDetails = mMyDetailsResponse.details;
                        if (mDetails != null) {
                            if (!TextUtils.isEmpty(mDetails.firstname)) {
                                etFirstName.setText("" + mDetails.firstname);
                            }

                            if (!TextUtils.isEmpty(mDetails.lastname)) {
                                etLastName.setText("" + mDetails.lastname);
                            }

                            if (!TextUtils.isEmpty(mDetails.lastname)) {
                                etLastName.setText("" + mDetails.lastname);
                            }

                            if (!TextUtils.isEmpty(mDetails.gender) && (mDetails.gender.equalsIgnoreCase("male") || mDetails.gender.equalsIgnoreCase("رجل"))) {
                                cbWomen.setChecked(false);
                                cbMen.setChecked(true);
                            } else if (!TextUtils.isEmpty(mDetails.gender) && (mDetails.gender.equalsIgnoreCase("female") || mDetails.gender.equalsIgnoreCase("امرأة"))) {
                                cbWomen.setChecked(true);
                                cbMen.setChecked(false);
                            }

                            if (!TextUtils.isEmpty(mDetails.dob)) {
                                tvDateBirth.setText("" + mDetails.dob);
                                Date mDate = null;
                                try {
                                    mDate = DateFormate.sdfGender.parse(mDetails.dob);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (mDate != null) {

                                    startYear = mCalNow.get(Calendar.YEAR);
                                    startMonth = mCalNow.get(Calendar.MONTH);
                                    startDay = mCalNow.get(Calendar.DAY_OF_MONTH);
                                    mCalNow.setTime(mDate);
                                    mDatePickerDialog.updateDate(startYear, startMonth, startDay);
                                }
                            }
                        }


                    } else if (mMyDetailsResponse != null && mMyDetailsResponse.success == 0) {
                        Utils.showToast(getActivity(), "" + mMyDetailsResponse.message);
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
            public void onFailure(Call<MyDetailsResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });
    }

    private Context getActivity() {
        return MyDetailsActivity.this;
    }

    private void InitializeControlsAction() {
        ibCancel.setOnClickListener(this);
        lnrWomen.setOnClickListener(this);
        lnrMen.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        tvDateBirth.setOnClickListener(this);
        tvGoShy7loTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == ibCancel) {
            finish();
        } else if (view == lnrWomen) {
            cbWomen.setChecked(true);
            cbMen.setChecked(false);
        } else if (view == lnrMen) {
            cbWomen.setChecked(false);
            cbMen.setChecked(true);
        } else if (view == tvDateBirth) {
            mDatePickerDialog.show();
        } else if (view == tvGoShy7loTest) {

        } else if (view == tvDone) {
            if (isValidField()) {
                updateDetails();
            }
        }
    }

    private boolean isValidField() {

        if (TextUtils.isEmpty(etFirstName.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.msg_firstname));
            return false;
        } else if (TextUtils.isEmpty(etLastName.getText().toString())) {
            Utils.showToast(getActivity(), "" + getString(R.string.msg_lastname));
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

    private void updateDetails() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        updateDetails();
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(getActivity());
        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        Map<String, Object> jsonParams = new ArrayMap<>();

        String gender = "";
        if (cbMen.isChecked()) {
            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                gender = "male";
            } else {
                gender = "رجل";
            }
        } else if (cbWomen.isChecked()) {
            if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
                gender = "female";
            } else {
                gender = "امرأة";
            }
        }

        String dob = "";
        if (!tvDateBirth.getText().toString().equalsIgnoreCase(getString(R.string.date_birth)) && tvDateBirth.getText().toString().contains("-")) {
            dob = tvDateBirth.getText().toString();
        }

        jsonParams.put("firstname", "" + etFirstName.getText().toString());
        jsonParams.put("lastname", "" + etLastName.getText().toString());
        jsonParams.put("gender", "" + gender);
        jsonParams.put("dob", "" + dob);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        Call<MyDetailsResponse> updateDetailsCall = serviceAPI.getUpdateDetailList(Shy7lo.mLangCode, "Bearer " + userToken, body);
        updateDetailsCall.enqueue(new Callback<MyDetailsResponse>() {
            @Override
            public void onResponse(Call<MyDetailsResponse> call, Response<MyDetailsResponse> response) {
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    MyDetailsResponse mMyDetailsResponse = response.body();
                    if (mMyDetailsResponse != null && mMyDetailsResponse.success == 1) {
                        Utils.showToast(getActivity(), "" + mMyDetailsResponse.message);
                        finish();
                    } else if (mMyDetailsResponse != null && mMyDetailsResponse.success == 0) {
                        Utils.showToast(getActivity(), "" + mMyDetailsResponse.message);
                    } else {
                        Utils.showToast(getActivity(), "" + getString(R.string.msg_something_wrong));
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
            public void onFailure(Call<MyDetailsResponse> call, Throwable t) {
                Utils.closeProgressDialog();
            }
        });

    }
}
