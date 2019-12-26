package shy7lo.com.shy7lo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.adapter.MyOrderAdapter;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.model.MyOrderResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

public class HistoryOrderFragment extends Fragment {

    public static final String TAG = "HistoryOrderFragment";

    View mView;
    @BindView(R.id.tvOrderCount)
    TextView tvOrderCount;
    @BindView(R.id.lvHistoryOrder)
    ListView lvHistoryOrder;
    @BindView(R.id.lnrNoOrder)
    LinearLayout lnrNoOrder;

    MyOrderAdapter mMyOrderAdapter;
    List<MyOrderResponse.AllOrder> mHistoryOrderList = new ArrayList<>();

    int firstVisibleItemPosition = 0;

    private boolean isViewShown = false;
    private boolean isVisibleToUser = false;
    private int page_number = 1;
    ArrayList<String> mPageNumberList = new ArrayList<>();
    private boolean isLastPage = false;
    private boolean isLoading = false, isLoadingMore = false;
    public static int PAGE_SIZE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history_order, container, false);
        ButterKnife.bind(this, mView);
        Utils.setLocale(getActivity());
        LogUtils.e(TAG, "onCreateView");
        InitializeControls();

        return mView;
    }

    private void InitializeControls() {

        mMyOrderAdapter = new MyOrderAdapter(getActivity(), false);
        lvHistoryOrder.setAdapter(mMyOrderAdapter);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lvHistoryOrder.setScaleX(-1f);
        }else{
            lvHistoryOrder.setScaleX(1f);
        }

        lvHistoryOrder.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstVisibleItemPosition = firstVisibleItem;
                LogUtils.e("", "firstVisibleItem::" + firstVisibleItem);
                LogUtils.e("", "visibleItemCount::" + visibleItemCount);
                LogUtils.e("", "totalItemCount::" + totalItemCount);

                if (mHistoryOrderList.size() > 1) {

                    page_number = mHistoryOrderList.size() / 16;
                    if (page_number < 1) {
                        page_number = 1;
                    }
//                    page_number = (productItemList.size() / 16) - 1;
//                    if (page_number < 0) {
//                        page_number = 0;
//                    }
                }


//                int boundCount = (16 * (page_number - 1)) + 1;
////                LogUtils.e("", "firstVisibleItem::" + firstVisibleItem + " boundCount::" + boundCount + " page_number::" + page_number);
////                if (page_number > 1) {
//                if (page_number > 0) {
//                    if (firstVisibleItem >= boundCount && boundCount > 16) {
//                        if (!isLoading && !isLastPage) {
////                        if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {
//
//                            isLoading = true;
//                            page_number = page_number + 1;
////                            LogUtils.e("", "Load New more::" + page_number);
//                            getProductNextPageView(false, page_number);
////                        }
//                        }
//                    }
//                if (!isLoading && !isLastPage) {
                LogUtils.e("", "isLoadingMore:"+isLoadingMore+" isLastPage:"+isLastPage);
                if (!isLoadingMore && !isLastPage) {
                    if (firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > 0) {

                        isLoadingMore = true;
                        page_number = page_number + 1;
//                        LogUtils.e("", "Load more page_number::" + page_number);
                        getHistoryProductNextLoadMorePageView(page_number);
                    }
                }


            }
        });

        getHistoryOrderList();
//        lvHistoryOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//                if (mMyOrderAdapter != null) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Constant.BNDL_ORDER_ID, mMyOrderAdapter.getItem(position).orderId);
//                    IntentHandler.startActivity(getActivity(), MyOrderDetailsActivity.class, bundle);
//                }
//
//            }
//        });

    }

    private void getHistoryOrderList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getHistoryOrderList();
                    }
                }
            });
            return;
        }

        mPageNumberList = new ArrayList<>();

        if (mHistoryOrderList != null && mHistoryOrderList.size() > 0) {
            mHistoryOrderList.clear();
        }

        page_number = 1;

        Utils.showProgressDialog(getActivity());

        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        Call<MyOrderResponse> call = serviceAPI.getHistoryOrderList(Shy7lo.mLangCode, "Bearer " + userToken, page_number);
        call.enqueue(new Callback<MyOrderResponse>() {
            @Override
            public void onResponse(Call<MyOrderResponse> call, Response<MyOrderResponse> response) {
                Utils.closeProgressDialog();

                if (response.isSuccessful()) {
                    MyOrderResponse mMyOrderResponse = response.body();
                    if (mMyOrderResponse != null && mMyOrderResponse.success == 1) {
                        if (mMyOrderResponse.data != null) {

                            if (mPageNumberList != null && mPageNumberList.size() > 0) {
                                mPageNumberList.clear();
                                mPageNumberList.add("" + page_number);
                            }

                            List<MyOrderResponse.AllOrder> mOldOrders = mMyOrderResponse.data.currentOrders;
                            if (mOldOrders != null && mOldOrders.size() > 0) {
                                mHistoryOrderList.addAll(mOldOrders);
                            }

                            PAGE_SIZE = mMyOrderResponse.data.totalCount;
                            if (mMyOrderResponse.data.totalCount > 1){
                                tvOrderCount.setText(mMyOrderResponse.data.totalCount + " " + getString(R.string.orders));
                            }else{
                                tvOrderCount.setText(mMyOrderResponse.data.totalCount + " " + getString(R.string.order));
                            }

                            if (mHistoryOrderList.size() % 16 == 0 && mHistoryOrderList.size() < PAGE_SIZE) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                            }

                            setOrderList(mHistoryOrderList);
                        }
                    } else if (mMyOrderResponse != null && mMyOrderResponse.success == 0) {
                        Utils.showToast(getActivity(), "" + mMyOrderResponse.message);
                    } else {
                        Utils.showToast(getActivity(), getString(R.string.msg_something_wrong));
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
            public void onFailure(Call<MyOrderResponse> call, Throwable t) {
                Utils.closeProgressDialog();

                LogUtils.e("", "getMessage::" + t.getMessage());
            }
        });

    }

    private void getHistoryProductNextLoadMorePageView(final int page_number) {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getHistoryProductNextLoadMorePageView(page_number);
                    }
                }
            });
            return;
        }

        Utils.showProgressDialog(getActivity());

        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);

        String userToken = MyPref.getPref(getActivity(), MyPref.USER_CART_ID, "");
        Call<MyOrderResponse> call = serviceAPI.getHistoryOrderList(Shy7lo.mLangCode, "Bearer " + userToken, page_number);
        call.enqueue(new Callback<MyOrderResponse>() {
            @Override
            public void onResponse(Call<MyOrderResponse> call, Response<MyOrderResponse> response) {
                Utils.closeProgressDialog();

                isLoading = false;
                isLoadingMore = false;

                if (response.isSuccessful()) {
                    MyOrderResponse mMyOrderResponse = response.body();
                    if (mMyOrderResponse != null && mMyOrderResponse.success == 1) {
                        if (mMyOrderResponse.data != null) {

                            if (!mPageNumberList.contains("" + page_number)) {

                                List<MyOrderResponse.AllOrder> mOldOrders = mMyOrderResponse.data.currentOrders;
                                if (mOldOrders != null && mOldOrders.size() > 0) {
                                    mHistoryOrderList.addAll(mOldOrders);
                                }

                                mPageNumberList.add("" + page_number);
                            }

                            if (mHistoryOrderList.size() % 16 == 0 && mHistoryOrderList.size() < PAGE_SIZE) {
                                isLastPage = false;
                            } else {
                                isLastPage = true;
                            }

                            setOrderList(mHistoryOrderList);
                        }
                    } else if (mMyOrderResponse != null && mMyOrderResponse.success == 0) {
                        Utils.showToast(getActivity(), "" + mMyOrderResponse.message);
                    } else {
                        Utils.showToast(getActivity(), getString(R.string.msg_something_wrong));
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
            public void onFailure(Call<MyOrderResponse> call, Throwable t) {
                Utils.closeProgressDialog();
                isLoading = false;
                isLoadingMore = false;
                LogUtils.e("", "getMessage::" + t.getMessage());
            }
        });

    }

    public void setOrderList(List<MyOrderResponse.AllOrder> mHistoryOrderList) {
        if (mHistoryOrderList != null && mHistoryOrderList.size() > 0) {
            lnrNoOrder.setVisibility(View.GONE);
            tvOrderCount.setVisibility(View.VISIBLE);

            mMyOrderAdapter.addAll(mHistoryOrderList);

        } else {
            lnrNoOrder.setVisibility(View.VISIBLE);
            tvOrderCount.setVisibility(View.GONE);
        }
    }
}
