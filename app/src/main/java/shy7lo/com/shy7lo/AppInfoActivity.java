package shy7lo.com.shy7lo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.Maintenance;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

import static shy7lo.com.shy7lo.pref.MyPref.getPref;

/**
 * Created by Jiten on 27-07-2017.
 */

public class AppInfoActivity extends AppCompatActivity {

    public static final String TAG = "AppInfoFragment";

    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvNameTitle)
    TextView tvNameTitle;
    @BindView(R.id.tvNameInfo)
    TextView tvNameInfo;
    @BindView(R.id.tvVersionTitle)
    TextView tvVersionTitle;
    @BindView(R.id.tvVersionInfo)
    TextView tvVersionInfo;
    @BindView(R.id.tvVersionCodeTitle)
    TextView tvVersionCodeTitle;
    @BindView(R.id.tvVersionCodeInfo)
    TextView tvVersionCodeInfo;
    @BindView(R.id.tvEnvironmentTitle)
    TextView tvEnvironmentTitle;
    @BindView(R.id.tvEnvironmentInfo)
    TextView tvEnvironmentInfo;
    @BindView(R.id.tvBuildTitle)
    TextView tvBuildTitle;
    @BindView(R.id.tvBuildInfo)
    TextView tvBuildInfo;
    @BindView(R.id.tvUpgrade)
    TextView tvUpgrade;
    @BindView(R.id.lnrMainContainer)
    LinearLayout lnrMainContainer;

    @BindView(R.id.ibCancel)
    ImageButton ibCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_appinfo);
        Utils.setLocale(getActivity());
        ButterKnife.bind(getActivity());

        InitializeControls();
        checkForMaintenance();
    }

    private Activity getActivity() {
        return AppInfoActivity.this;
    }

    private void InitializeControls() {

        if (getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            tvTitle.setBackgroundResource(R.drawable.ic_top_logo_ar);
            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            lnrMainContainer.setScaleX(-1f);
            tvNameTitle.setScaleX(-1f);
            tvNameInfo.setScaleX(-1f);
            tvVersionTitle.setScaleX(-1f);
            tvVersionInfo.setScaleX(-1f);
            tvVersionCodeTitle.setScaleX(-1f);
            tvVersionCodeInfo.setScaleX(-1f);
            tvEnvironmentTitle.setScaleX(-1f);
            tvEnvironmentInfo.setScaleX(-1f);
            tvBuildTitle.setScaleX(-1f);
            tvBuildInfo.setScaleX(-1f);
            tvUpgrade.setScaleX(-1f);


            tvNameInfo.setGravity(Gravity.LEFT);
            tvVersionInfo.setGravity(Gravity.LEFT);
            tvVersionCodeInfo.setGravity(Gravity.LEFT);
            tvEnvironmentInfo.setGravity(Gravity.LEFT);
            tvBuildInfo.setGravity(Gravity.LEFT);
        } else {
            tvTitle.setBackgroundResource(R.drawable.ic_top_logo);
            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
            lnrMainContainer.setScaleX(1f);
            tvNameTitle.setScaleX(1f);
            tvNameInfo.setScaleX(1f);
            tvVersionTitle.setScaleX(1f);
            tvVersionInfo.setScaleX(1f);
            tvVersionCodeTitle.setScaleX(1f);
            tvVersionCodeInfo.setScaleX(1f);
            tvEnvironmentTitle.setScaleX(1f);
            tvEnvironmentInfo.setScaleX(1f);
            tvBuildTitle.setScaleX(1f);
            tvBuildInfo.setScaleX(1f);
            tvUpgrade.setScaleX(1f);

            tvNameInfo.setGravity(Gravity.RIGHT);
            tvVersionInfo.setGravity(Gravity.RIGHT);
            tvVersionCodeInfo.setGravity(Gravity.RIGHT);
            tvEnvironmentInfo.setGravity(Gravity.RIGHT);
            tvBuildInfo.setGravity(Gravity.RIGHT);


        }

        tvVersionInfo.setText("" + BuildConfig.VERSION_NAME);
        tvVersionCodeInfo.setText("" + BuildConfig.VERSION_CODE);

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }

            }
        });

        checkForUpgrade();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Shy7lo.setActivityContext(getActivity());
    }

    private void checkForUpgrade() {

        LogUtils.e("", "checkForUpgrade call");

        String mAppVersion = getPref(getActivity(), MyPref.APP_VERSION, "");
        String mVersionName = BuildConfig.VERSION_NAME;
        LogUtils.e("", "mAppVersion::" + mAppVersion);
        LogUtils.e("", "mVersionName::" + mVersionName);
        if (!TextUtils.isEmpty(mAppVersion) && !TextUtils.isEmpty(mVersionName) && !mAppVersion.equals(mVersionName)) {
            tvUpgrade.setVisibility(View.VISIBLE);
        } else {
            tvUpgrade.setVisibility(View.GONE);
        }


    }

    private void checkForMaintenance() {

        ApiInterface apiService =
                RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<Maintenance> callCode = apiService.getMaintenanceStatus(Shy7lo.mLangCode);
        callCode.enqueue(new Callback<Maintenance>() {
            @Override
            public void onResponse(Call<Maintenance> call, Response<Maintenance> response) {
                if (response.isSuccessful()) {
                    Maintenance maintenance = response.body();
                    if (maintenance != null && maintenance.success.equalsIgnoreCase("1")) {

                    } else if (maintenance != null && maintenance.success.equalsIgnoreCase("2")) {
                        Utils.showInitialScreen(getActivity());
                    }

                }
            }

            @Override
            public void onFailure(Call<Maintenance> call, Throwable t) {
            }
        });

    }

}
