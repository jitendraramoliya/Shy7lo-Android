package shy7lo.com.shy7lo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import shy7lo.com.shy7lo.adapter.FullTrackingInfoAdapter;
import shy7lo.com.shy7lo.model.MyOrderDetailsResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.Utils;

public class MyOrderTrackingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rltTitle)
    RelativeLayout rltTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvStatusValue)
    TextView tvStatusValue;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.ibCancel)
    ImageButton ibCancel;
    @BindView(R.id.lnrContainer)
    LinearLayout lnrContainer;
    @BindView(R.id.lnrFullTrackInfo)
    LinearLayout lnrFullTrackInfo;
    @BindView(R.id.lvFullTrackingInfo)
    ListView lvFullTrackingInfo;

    FullTrackingInfoAdapter mFullTrackingInfoAdapter;

    MyOrderDetailsResponse.Data mData;
    MyOrderDetailsResponse.TrackingInfo mTrackingInfo;
    MyOrderDetailsResponse.FullTrackingInfo mFullTrackingInfo;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_order_tracking);
        ButterKnife.bind(this);

        InitializeControls();
        InitializeControlsAction();
    }

    private void InitializeControls() {
        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

            rltTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
            lnrContainer.setScaleX(-1f);

            tvStatusValue.setScaleX(-1f);
            tvDate.setScaleX(-1f);

        } else {

            rltTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
            lnrContainer.setScaleX(1f);

            tvStatusValue.setScaleX(1f);
            tvDate.setScaleX(1f);

        }

        mFullTrackingInfoAdapter = new FullTrackingInfoAdapter(getActivity());
        lvFullTrackingInfo.setAdapter(mFullTrackingInfoAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mData = new Gson().fromJson(bundle.getString(Constant.BNDL_ORDER_TRACKING), MyOrderDetailsResponse.Data.class);
            if (mData != null) {
                if (mData.trackingInfo != null && mData.trackingInfo.size() > 0) {
                    mTrackingInfo = mData.trackingInfo.get(mData.trackingInfo.size() - 1);
                }

                if (mData.fullTrackingInfo != null && mData.fullTrackingInfo.size() > 0) {
                    lnrFullTrackInfo.setVisibility(View.VISIBLE);
                    mFullTrackingInfoAdapter.addAll(mData.fullTrackingInfo);
                } else {
                    lnrFullTrackInfo.setVisibility(View.GONE);
                }


            }
        }

    }

    private void InitializeControlsAction() {
        ibCancel.setOnClickListener(this);
    }

    private Context getActivity() {
        return MyOrderTrackingActivity.this;
    }

    @Override
    public void onClick(View view) {
        if (view == ibCancel) {
            finish();
        }
    }
}
