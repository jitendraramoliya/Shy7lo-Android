package shy7lo.com.shy7lo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shy7lo.com.shy7lo.adapter.ParentLevel;
import shy7lo.com.shy7lo.application.Shy7lo;
import shy7lo.com.shy7lo.fragment.ProductsItemsFragment;
import shy7lo.com.shy7lo.model.CategoryList;
import shy7lo.com.shy7lo.model.SubChildData;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.rest.ApiInterface;
import shy7lo.com.shy7lo.rest.RestClient;
import shy7lo.com.shy7lo.utils.LogUtils;
import shy7lo.com.shy7lo.utils.Utils;

/**
 * Created by Jiten on 29-08-2017.
 */

public class CategoryOptionActivity extends AppCompatActivity {

    //    CategoryExpandableAdapter expWomenListAdapter, expMenListAdapter, expKidListAdapter;
    ParentLevel mGeneralParentLevel, mWomenParentLevel, mMenParentLevel, mKidParentLevel;

    //    @BindView(R.id.aelvWomen)
//    CategoryExpandableListView aelvWomen;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.elvGeneral)
    ExpandableListView elvGeneral;
    @BindView(R.id.elvWomen)
    ExpandableListView elvWomen;
    @BindView(R.id.elvMen)
    ExpandableListView elvMen;
    @BindView(R.id.elvKid)
    ExpandableListView elvKid;
    @BindView(R.id.lnrTitle)
    RelativeLayout lnrTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    //    @BindView(R.id.aelvMen)
//    CategoryExpandableListView aelvMen;
//    @BindView(R.id.aelvKid)
//    CategoryExpandableListView aelvKid;
    @BindView(R.id.stlCategory)
    SegmentTabLayout stlCategory;

    int mLastWomenExpandedPosition = -1, mLastMenExpandedPosition = -1, mLastKidExpandedPosition = -1;


    CategoryList mCategoryList;
    CategoryList.MainCategoryData mGeneralCategoryData, mWomenCategoryData, mMenCategoryData, mKidCategoryData;

    String[] mCategoryArray; // = new String[]{"Women", "Men", "Kid"};
    private boolean isGeneralOptionSelected = false, isWomenOptionSelected = false, isMenOptionSelected = false, isKidOptionSelected = false;

    private enum CategoryType {
        GENERAL, WOMEN, MEN, KID
    }

    String category_id = "", main_category_id = "", category_name = "", selected_category_id = "", selected_category_name = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setMint(this.getApplication());
        setContentView(R.layout.activity_category_option_list);
        ButterKnife.bind(getActivity());
        InitilizeControls();

    }

    private void InitilizeControls() {

        mCategoryArray = getResources().getStringArray(R.array.product_tab_array);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            category_id = b.getString(FilterNewActivity.BNDL_CATEGORY_ID, category_id);
            main_category_id = b.getString(FilterNewActivity.BNDL_MAIN_CATEGORY_ID, main_category_id);
            category_name = b.getString(FilterNewActivity.BNDL_CATEGORY_NAME, category_name);
            String categoryList = b.getString(FilterNewActivity.BNDL_CATEGORY_LIST, "");
            LogUtils.e("", "category_id::" + category_id);
            LogUtils.e("", "main_category_id::" + main_category_id);
            LogUtils.e("", "category_name::" + category_name);
//            LogUtils.e("", "categoryList::" + categoryList);
            if (!TextUtils.isEmpty(categoryList)) {
                mCategoryList = new Gson().fromJson(categoryList, CategoryList.class);
            }

        }

        tvTitle.setTypeface(Shy7lo.TahomaBold);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            lnrTitle.setScaleX(-1f);
            tvTitle.setScaleX(-1f);
//            elvWomen.setScaleX(-1f);
//            elvMen.setScaleX(-1f);
//            elvKid.setScaleX(-1f);
//            tvTitle.setGravity(Gravity.RIGHT);
        } else {
            lnrTitle.setScaleX(1f);
            tvTitle.setScaleX(1f);
//            elvWomen.setScaleX(1f);
//            elvMen.setScaleX(1f);
//            elvKid.setScaleX(1f);
//            tvTitle.setGravity(Gravity.LEFT);
        }

        stlCategory.setTabData(mCategoryArray);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                stlCategory.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//            } else {
            stlCategory.setScaleX(-1f);

            for (int i = 0; i < mCategoryArray.length; i++) {
//                TextView mTab1 = (TextView) (((RelativeLayout) ((LinearLayout) stlCategory.getChildAt(0)).getChildAt(i)).getChildAt(1));
//                LogUtils.e("", i + " text::" + mTab1.getText().toString());
//                mTab1.setScaleX(-1f);
                ((RelativeLayout) ((LinearLayout) stlCategory.getChildAt(0)).getChildAt(i)).setScaleX(-1f);
            }
//            }
        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                stlCategory.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//            } else {
            stlCategory.setScaleX(1f);
            for (int i = 0; i < mCategoryArray.length; i++) {
//                TextView mTab1 = (TextView) (((RelativeLayout) ((LinearLayout) stlCategory.getChildAt(0)).getChildAt(i)).getChildAt(1));
//                mTab1.setScaleX(1f);
                ((RelativeLayout) ((LinearLayout) stlCategory.getChildAt(0)).getChildAt(i)).setScaleX(1f);
            }
//            }
        }

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent back = new Intent();
                Bundle bundle = new Bundle();
                LogUtils.e("", "back selected_category_id::" + selected_category_id);
                LogUtils.e("", "back selected_category_name::" + selected_category_name);
                if (!TextUtils.isEmpty(selected_category_id)) {
                    bundle.putString(FilterNewActivity.BNDL_CATEGORY_ID, "" + selected_category_id);
                    if (!selected_category_id.equalsIgnoreCase(category_id)) {
                        bundle.putBoolean(FilterNewActivity.BNDL_CATEGORY_ID_CHANGED, true);
                    } else {
                        bundle.putBoolean(FilterNewActivity.BNDL_CATEGORY_ID_CHANGED, false);
                    }
                } else {
                    bundle.putString(FilterNewActivity.BNDL_CATEGORY_ID, "" + category_id);
                    bundle.putBoolean(FilterNewActivity.BNDL_CATEGORY_ID_CHANGED, false);

                }
                bundle.putString(FilterNewActivity.BNDL_SELECTED_CATEGORY_NAME, selected_category_name);
                back.putExtras(bundle);
                setResult(RESULT_OK, back);
                finish();
            }
        });
        stlCategory.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Log.e("dsf", "position::" + position);
                if (position == 2) {
                    showCategory(CategoryType.KID);
                } else if (position == 1) {
                    showCategory(CategoryType.MEN);
                } else {
                    showCategory(CategoryType.WOMEN);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        Shy7lo.setActivityContext(getActivity());
        if (mCategoryList == null) {
            getCategoryList();
        } else {
            showCategoryList();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showCategory(CategoryType mCategoryType) {
        switch (mCategoryType) {
            case GENERAL:
//                aelvWomen.setVisibility(View.VISIBLE);
                elvGeneral.setVisibility(View.VISIBLE);
                elvWomen.setVisibility(View.GONE);
//                aelvMen.setVisibility(View.GONE);
                elvMen.setVisibility(View.GONE);
//                aelvKid.setVisibility(View.GONE);
                elvKid.setVisibility(View.GONE);
                break;
            case WOMEN:
//                aelvWomen.setVisibility(View.VISIBLE);
                elvGeneral.setVisibility(View.GONE);
                elvWomen.setVisibility(View.VISIBLE);
//                aelvMen.setVisibility(View.GONE);
                elvMen.setVisibility(View.GONE);
//                aelvKid.setVisibility(View.GONE);
                elvKid.setVisibility(View.GONE);
                break;
            case MEN:
//                aelvWomen.setVisibility(View.GONE);
                elvGeneral.setVisibility(View.GONE);
                elvWomen.setVisibility(View.GONE);
//                aelvMen.setVisibility(View.VISIBLE);
                elvMen.setVisibility(View.VISIBLE);
//                aelvKid.setVisibility(View.GONE);
                elvKid.setVisibility(View.GONE);
                break;
            case KID:
//                aelvWomen.setVisibility(View.GONE);
                elvGeneral.setVisibility(View.GONE);
                elvWomen.setVisibility(View.GONE);
//                aelvMen.setVisibility(View.GONE);
                elvMen.setVisibility(View.GONE);
//                aelvKid.setVisibility(View.VISIBLE);
                elvKid.setVisibility(View.VISIBLE);
                break;
        }
    }

    private Activity getActivity() {
        return CategoryOptionActivity.this;
    }

    public void showCategoryList() {

        if (mCategoryList != null && mCategoryList.success.equals("1")) {

            if (mCategoryList.data != null && mCategoryList.data.getChildrenData() != null && mCategoryList.data.getChildrenData().size() > 0) {


                for (CategoryList.MainCategoryData mainCategoryData : mCategoryList.data.getChildrenData()) {

                    if (main_category_id.equalsIgnoreCase("" + mainCategoryData.getId())) {
                        mGeneralCategoryData = mainCategoryData;

                        CategoryList.ChildData mChildData = new CategoryList().new ChildData();
                        mChildData.setId(mGeneralCategoryData.getId());
                        mChildData.setName(getResources().getString(R.string.all) + " " + mGeneralCategoryData.getName());
                        mChildData.setChildrenData(new ArrayList<SubChildData>());
                        mGeneralCategoryData.getChildrenData().add(0, mChildData);


                        for (int i = 0; i < mGeneralCategoryData.getChildrenData().size(); i++) {

                            CategoryList.ChildData childData = mGeneralCategoryData.getChildrenData().get(i);
//                            LogUtils.e("", "single mProductName: " + childData.getName() + " size::" + childData.getChildrenData().size());
                            if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {

                                for (int j = 0; j < childData.getChildrenData().size(); j++) {

                                    SubChildData subChildData = childData.getChildrenData().get(j);
                                    if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {


                                        for (int k = 0; k < subChildData.getChildrenData().size(); k++) {

                                            SubChildData.ChildData mChildData2 = subChildData.getChildrenData().get(k);
                                            if (mChildData2.getId().toString().equalsIgnoreCase(category_id)) {
                                                mChildData2.setSelected(true);
                                                isGeneralOptionSelected = true;
                                                selected_category_name = mGeneralCategoryData.getName() + "->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChildData2.getName();
                                            } else {
                                                mChildData2.setSelected(false);
                                            }

                                            if (k == subChildData.getChildrenData().size() - 1) {
                                                SubChildData.ChildData mChildData1 = new SubChildData().new ChildData();
                                                mChildData1.setId(subChildData.getId());
                                                mChildData1.setName(getResources().getString(R.string.all) + " " + subChildData.getName());
                                                LogUtils.e("", "Id::" + subChildData.getId() + " " + category_id);
                                                if (subChildData.getId().toString().equalsIgnoreCase(category_id)) {
                                                    mChildData1.setSelected(true);
                                                    isGeneralOptionSelected = true;
                                                    selected_category_name = mGeneralCategoryData.getName() + "->" + childData.getName().trim() + "->" + subChildData.getName();
                                                } else {
                                                    mChildData1.setSelected(false);
//                                                isWomenOptionSelected = false;
                                                }

                                                subChildData.getChildrenData().add(0, mChildData1);
                                                break;
                                            }

                                        }
                                    } else {
                                        if (subChildData.getId().toString().equalsIgnoreCase(category_id)) {
                                            subChildData.setSelected(true);
                                            isGeneralOptionSelected = true;
                                            selected_category_name = mGeneralCategoryData.getName() + "->" + childData.getName().trim() + "->" + subChildData.getName();
                                        } else {
                                            subChildData.setSelected(false);
                                        }
                                    }

                                    if (j == childData.getChildrenData().size() - 1) {
                                        SubChildData mSubChildData = new SubChildData();
                                        mSubChildData.setId(childData.getId());
                                        mSubChildData.setName(getResources().getString(R.string.all) + " " + childData.getName());
                                        mSubChildData.setChildrenData(new ArrayList<SubChildData.ChildData>());
                                        if (childData.getId().toString().equalsIgnoreCase(category_id)) {
                                            mSubChildData.setSelected(true);
                                            isGeneralOptionSelected = true;
                                            selected_category_name = mGeneralCategoryData.getName() + "->" + childData.getName();
                                        } else {
                                            mSubChildData.setSelected(false);
                                        }
                                        childData.getChildrenData().add(0, mSubChildData);
                                        break;
                                    }
                                }
                            } else {

                                if (childData.getId().toString().equalsIgnoreCase(category_id)) {
                                    childData.setSelected(true);
                                    isGeneralOptionSelected = true;

                                    if (i == 0) {
                                        selected_category_name = mGeneralCategoryData.getName() + "->All";
                                    } else {
                                        selected_category_name = mGeneralCategoryData.getName() + "->" + childData.getName();
                                    }
                                } else {
                                    childData.setSelected(false);
                                }
                            }

                        }


                    } else {

//                        if (mainCategoryData.getId() == 143) {
//                            mWomenCategoryData = mainCategoryData;
//
//                            CategoryList.ChildData mChildData = new CategoryList().new ChildData();
//                            mChildData.setId(mWomenCategoryData.getId());
//                            mChildData.setName(getResources().getString(R.string.all) + " " + mWomenCategoryData.getName());
//                            mChildData.setChildrenData(new ArrayList<SubChildData>());
//                            mWomenCategoryData.getChildrenData().add(0, mChildData);
//
//
//                            for (int i = 0; i < mWomenCategoryData.getChildrenData().size(); i++) {
//
//                                CategoryList.ChildData childData = mWomenCategoryData.getChildrenData().get(i);
////                            LogUtils.e("", "single mProductName: " + childData.getName() + " size::" + childData.getChildrenData().size());
//                                if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
//
//                                    for (int j = 0; j < childData.getChildrenData().size(); j++) {
//
//                                        SubChildData subChildData = childData.getChildrenData().get(j);
//                                        if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
//
//
//                                            for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//
//                                                SubChildData.ChildData mChildData2 = subChildData.getChildrenData().get(k);
//                                                if (mChildData2.getId().toString().equalsIgnoreCase(category_id)) {
//                                                    mChildData2.setSelected(true);
//                                                    isWomenOptionSelected = true;
//                                                    selected_category_name = "Women->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChildData2.getName();
//                                                } else {
//                                                    mChildData2.setSelected(false);
//                                                }
//
//                                                if (k == subChildData.getChildrenData().size() - 1) {
//                                                    SubChildData.ChildData mChildData1 = new SubChildData().new ChildData();
//                                                    mChildData1.setId(subChildData.getId());
//                                                    mChildData1.setName(getResources().getString(R.string.all) + " " + subChildData.getName());
//                                                    LogUtils.e("", "Id::" + subChildData.getId() + " " + category_id);
//                                                    if (subChildData.getId().toString().equalsIgnoreCase(category_id)) {
//                                                        mChildData1.setSelected(true);
//                                                        isWomenOptionSelected = true;
//                                                        selected_category_name = "Women->" + childData.getName().trim() + "->" + subChildData.getName();
//                                                    } else {
//                                                        mChildData1.setSelected(false);
////                                                isWomenOptionSelected = false;
//                                                    }
//
//                                                    subChildData.getChildrenData().add(0, mChildData1);
//                                                    break;
//                                                }
//
//                                            }
//                                        } else {
//                                            if (subChildData.getId().toString().equalsIgnoreCase(category_id)) {
//                                                subChildData.setSelected(true);
//                                                isWomenOptionSelected = true;
//                                                selected_category_name = "Women->" + childData.getName().trim() + "->" + subChildData.getName();
//                                            } else {
//                                                subChildData.setSelected(false);
//                                            }
//                                        }
//
//                                        if (j == childData.getChildrenData().size() - 1) {
//                                            SubChildData mSubChildData = new SubChildData();
//                                            mSubChildData.setId(childData.getId());
//                                            mSubChildData.setName(getResources().getString(R.string.all) + " " + childData.getName());
//                                            mSubChildData.setChildrenData(new ArrayList<SubChildData.ChildData>());
//                                            if (childData.getId().toString().equalsIgnoreCase(category_id)) {
//                                                mSubChildData.setSelected(true);
//                                                isWomenOptionSelected = true;
//                                                selected_category_name = "Women->" + childData.getName();
//                                            } else {
//                                                mSubChildData.setSelected(false);
//                                            }
//                                            childData.getChildrenData().add(0, mSubChildData);
//                                            break;
//                                        }
//                                    }
//                                } else {
//
//                                    if (childData.getId().toString().equalsIgnoreCase(category_id)) {
//                                        childData.setSelected(true);
//                                        isWomenOptionSelected = true;
//
//                                        if (i == 0) {
//                                            selected_category_name = "Women->All";
//                                        } else {
//                                            selected_category_name = "Women->" + childData.getName();
//                                        }
//                                    } else {
//                                        childData.setSelected(false);
//                                    }
//                                }
//
//                            }
//
//
//                        } else if (mainCategoryData.getId() == 144) {
//                            mMenCategoryData = mainCategoryData;
//
//                            CategoryList.ChildData mChildData = new CategoryList().new ChildData();
//                            mChildData.setId(mMenCategoryData.getId());
//                            mChildData.setName(getResources().getString(R.string.all) + " " + mMenCategoryData.getName());
//                            mChildData.setChildrenData(new ArrayList<SubChildData>());
//                            mMenCategoryData.getChildrenData().add(0, mChildData);
//
//                            for (int i = 0; i < mMenCategoryData.getChildrenData().size(); i++) {
//
//                                CategoryList.ChildData childData = mMenCategoryData.getChildrenData().get(i);
//                                if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
//                                    for (int j = 0; j < childData.getChildrenData().size(); j++) {
//
//                                        SubChildData subChildData = childData.getChildrenData().get(j);
//                                        if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
//
//
//                                            for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//
//                                                SubChildData.ChildData mChildData2 = subChildData.getChildrenData().get(k);
//                                                if (mChildData2.getId().toString().equalsIgnoreCase(category_id)) {
//                                                    mChildData2.setSelected(true);
//                                                    isMenOptionSelected = true;
//                                                    selected_category_name = "Men->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChildData2.getName();
//                                                } else {
//                                                    mChildData2.setSelected(false);
//                                                }
//
//                                                if (k == subChildData.getChildrenData().size() - 1) {
//                                                    SubChildData.ChildData mChildData1 = new SubChildData().new ChildData();
//                                                    mChildData1.setId(subChildData.getId());
//                                                    mChildData1.setName(getResources().getString(R.string.all) + " " + subChildData.getName());
//                                                    if (subChildData.getId().toString().equalsIgnoreCase(category_id)) {
//                                                        mChildData1.setSelected(true);
//                                                        isMenOptionSelected = true;
//                                                        selected_category_name = "Men->" + childData.getName() + "->" + subChildData.getName();
//                                                    } else {
//                                                        mChildData1.setSelected(false);
//                                                    }
//                                                    subChildData.getChildrenData().add(0, mChildData1);
//                                                    break;
//                                                }
//                                            }
//
//                                        } else {
//                                            if (subChildData.getId().toString().equalsIgnoreCase(category_id)) {
//                                                subChildData.setSelected(true);
//                                                isMenOptionSelected = true;
//                                                selected_category_name = "Men->" + childData.getName().trim() + "->" + subChildData.getName();
//                                            } else {
//                                                subChildData.setSelected(false);
//                                            }
//                                        }
//
//                                        if (j == childData.getChildrenData().size() - 1) {
//                                            SubChildData mSubChildData = new SubChildData();
//                                            mSubChildData.setId(childData.getId());
//                                            mSubChildData.setName(getResources().getString(R.string.all) + " " + childData.getName());
//                                            mSubChildData.setChildrenData(new ArrayList<SubChildData.ChildData>());
//                                            if (childData.getId().toString().equalsIgnoreCase(category_id)) {
//                                                mSubChildData.setSelected(true);
//                                                isMenOptionSelected = true;
//                                                selected_category_name = "Men->" + childData.getName();
//                                            } else {
//                                                mSubChildData.setSelected(false);
//                                            }
//                                            childData.getChildrenData().add(0, mSubChildData);
//                                            break;
//                                        }
//                                    }
//                                } else {
//                                    if (childData.getId().toString().equalsIgnoreCase(category_id)) {
//                                        childData.setSelected(true);
//                                        isMenOptionSelected = true;
//
//                                        if (i == 0) {
//                                            selected_category_name = "Men->All";
//                                        } else {
//                                            selected_category_name = "Men->" + childData.getName();
//                                        }
//                                    } else {
//                                        childData.setSelected(false);
//                                    }
//                                }
//
//                            }
//                        } else if (mainCategoryData.getId() == 145) {
//                            mKidCategoryData = mainCategoryData;
//
//                            CategoryList.ChildData mChildData = new CategoryList().new ChildData();
//                            mChildData.setId(mKidCategoryData.getId());
//                            mChildData.setName(getResources().getString(R.string.all) + " " + mKidCategoryData.getName());
//                            mChildData.setChildrenData(new ArrayList<SubChildData>());
//                            mKidCategoryData.getChildrenData().add(0, mChildData);
//
//
//                            for (int i = 0; i < mKidCategoryData.getChildrenData().size(); i++) {
//
//                                CategoryList.ChildData childData = mKidCategoryData.getChildrenData().get(i);
//                                if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
//                                    for (int j = 0; j < childData.getChildrenData().size(); j++) {
//
//                                        SubChildData subChildData = childData.getChildrenData().get(j);
//                                        if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
//                                            for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//
//                                                SubChildData.ChildData mChildData2 = subChildData.getChildrenData().get(k);
//                                                if (mChildData2.getId().toString().equalsIgnoreCase(category_id)) {
//                                                    mChildData2.setSelected(true);
//                                                    isKidOptionSelected = true;
//                                                    selected_category_name = "Kids->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChildData2.getName();
//                                                } else {
//                                                    mChildData2.setSelected(false);
//                                                }
//
//                                                if (k == subChildData.getChildrenData().size() - 1) {
//                                                    SubChildData.ChildData mChildData1 = new SubChildData().new ChildData();
//                                                    mChildData1.setId(subChildData.getId());
//                                                    mChildData1.setName(getResources().getString(R.string.all) + " " + subChildData.getName());
//                                                    if (subChildData.getId().toString().equalsIgnoreCase(category_id)) {
//                                                        mChildData1.setSelected(true);
//                                                        isKidOptionSelected = true;
//                                                        selected_category_name = "Kids->" + subChildData.getName() + "->" + mChildData1.getName();
//                                                    } else {
//                                                        mChildData1.setSelected(false);
//                                                    }
//                                                    subChildData.getChildrenData().add(0, mChildData1);
//                                                    break;
//                                                }
//                                            }
//                                        } else {
//                                            if (subChildData.getId().toString().equalsIgnoreCase(category_id)) {
//                                                subChildData.setSelected(true);
//                                                isKidOptionSelected = true;
//                                                selected_category_name = "Kids->" + childData.getName().trim() + "->" + subChildData.getName();
//                                            } else {
//                                                subChildData.setSelected(false);
//                                            }
//                                        }
//
//                                        if (j == childData.getChildrenData().size() - 1) {
//                                            SubChildData mSubChildData = new SubChildData();
//                                            mSubChildData.setId(childData.getId());
//                                            mSubChildData.setName(getResources().getString(R.string.all) + " " + childData.getName());
//                                            mSubChildData.setChildrenData(new ArrayList<SubChildData.ChildData>());
//                                            if (childData.getId().toString().equalsIgnoreCase(category_id)) {
//                                                mSubChildData.setSelected(true);
//                                                isKidOptionSelected = true;
//                                                selected_category_name = "Kids->" + childData.getName();
//                                            } else {
//                                                mSubChildData.setSelected(false);
//                                            }
//                                            childData.getChildrenData().add(0, mSubChildData);
//                                            break;
//                                        }
//                                    }
//                                } else {
//                                    if (childData.getId().toString().equalsIgnoreCase(category_id)) {
//                                        childData.setSelected(true);
//                                        isKidOptionSelected = true;
//
//                                        if (i == 0) {
//                                            selected_category_name = "Kids->All";
//                                        } else {
//                                            selected_category_name = "Kids->" + childData.getName();
//                                        }
//                                    } else {
//                                        childData.setSelected(false);
//                                    }
//                                }
//                            }
//                        }
                    }
                }
            }

            LogUtils.e("", "isGeneralOptionSelected::" + isGeneralOptionSelected);
            LogUtils.e("", "isWomenOptionSelected::" + isWomenOptionSelected);
            LogUtils.e("", "isMenOptionSelected::" + isMenOptionSelected);
            LogUtils.e("", "isKidOptionSelected::" + isKidOptionSelected);

//            if (isWomenOptionSelected) {
//                stlCategory.setCurrentTab(0);
//                showCategory(CategoryType.WOMEN);
//            }
//            if (isMenOptionSelected) {
//                stlCategory.setCurrentTab(1);
//                showCategory(CategoryType.MEN);
//            }
//            if (isKidOptionSelected) {
//                stlCategory.setCurrentTab(2);
//                showCategory(CategoryType.KID);
//            }

            if (isGeneralOptionSelected) {
                showCategory(CategoryType.GENERAL);
            }

            setView();
        } else if (mCategoryList != null && mCategoryList.success.equals("0")) {
            Utils.showToast(getActivity(), "" + mCategoryList.message);
        } else {
            Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
        }

    }

    private void getCategoryList() {

        if (!Utils.isInternetConnected(getActivity())) {
            Utils.showOfflineMsgDialog(getActivity(), new Utils.OnOfflineRetryDialogClick() {
                @Override
                public void onRetryClicked(Dialog dialog) {
                    if (Utils.isInternetConnected(getActivity())) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        getCategoryList();
                    }
                }
            });
            return;
        }

        LogUtils.e("", "getCategoryList call");
//        Utils.showProgressDialog(getActivity());

//        ApiInterface apiService =
//                RestClient.getIndexClient();
        ApiInterface serviceAPI = RestClient.getDynamicClient(RestClient.API2_SHYLABS_API_URL, true);
        Call<CategoryList> call = serviceAPI.getCategoriesList(Shy7lo.mLangCode);
        call.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {

                LogUtils.e("", "response code:" + response.code());
                Utils.closeProgressDialog();
                if (response.isSuccessful()) {
                    try {

                        mCategoryList = response.body();
                        if (mCategoryList != null && mCategoryList.success.equals("2")) {
                            Utils.showInitialScreen(getActivity());
                            return;
                        }
                        showCategoryList();

                    } catch (Exception e) {
                        e.printStackTrace();
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
//                    Utils.showAlertDialog(getActivity(), "" + response.code());
//                    Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
                }
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {
                System.out.println(t.getMessage());
                Utils.closeProgressDialog();
                Utils.showAlertDialog(getActivity(), "" + t.getMessage());
//                Utils.showToast(getActivity(), "" + getResources().getString(R.string.msg_something_wrong));
            }
        });
    }

    private void setView() {

//        expWomenListAdapter = new CategoryExpandableAdapter(getActivity(), mWomenCategoryData);
//        aelvWomen.setAdapter(expWomenListAdapter);

//        expMenListAdapter = new CategoryExpandableAdapter(getActivity(), mMenCategoryData);
//        aelvMen.setAdapter(expMenListAdapter);
//
//        expKidListAdapter = new CategoryExpandableAdapter(getActivity(), mKidCategoryData);
//        aelvKid.setAdapter(expKidListAdapter);

        mGeneralParentLevel = new ParentLevel(getActivity(), mGeneralCategoryData, elvGeneral, new ParentLevel.OnOptionClickListener() {
            @Override
            public void onOptionClicked(int level, int id) {
                LogUtils.e("", "level::" + level + " id::" + id);

                selected_category_id = "" + id;

                for (int i = 0; i < mGeneralCategoryData.getChildrenData().size(); i++) {
                    CategoryList.ChildData childData = mGeneralCategoryData.getChildrenData().get(i);
                    if (childData.getId() == id && level == 1) {
                        childData.setSelected(true);
                        if (i == 0) {
                            selected_category_name = mGeneralCategoryData.getName() + "->All";
                        } else {
                            selected_category_name = mGeneralCategoryData.getName() + "->" + childData.getName();
                        }
                        ProductsItemsFragment.mMainCateName = "" + mGeneralCategoryData.getName();
                        ibBack.performClick();

                    } else {
                        childData.setSelected(false);
                    }

                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                        for (int j = 0; j < childData.getChildrenData().size(); j++) {

                            SubChildData subChildData = childData.getChildrenData().get(j);
                            if (subChildData.getId() == id && level == 2) {
                                subChildData.setSelected(true);

                                if (j == 0) {
                                    selected_category_name = mGeneralCategoryData.getName() + "->" + childData.getName().replaceFirst("All ", "").trim();
                                } else {
                                    selected_category_name = mGeneralCategoryData.getName() + "->" + childData.getName().trim() + "->" + subChildData.getName();
                                }
                                ProductsItemsFragment.mMainCateName = "" + mGeneralCategoryData.getName();
                                ibBack.performClick();

                            } else {
                                subChildData.setSelected(false);
                            }

                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
//                                    LogUtils.e("", "isParent::" + isParent + " id::" + id + " mChileData.getId()::" + mChileData.getId());
                                    if (mChileData.getId() == id && level == 3) {
                                        mChileData.setSelected(true);
                                        if (k == 0) {
                                            selected_category_name = mGeneralCategoryData.getName() + "->" + childData.getName().trim() + "->" + mChileData.getName().replace("All ", "");
                                        } else {
                                            selected_category_name = mGeneralCategoryData.getName() + "->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
                                        }
                                        ProductsItemsFragment.mMainCateName = "" + mGeneralCategoryData.getName();
                                        ibBack.performClick();

                                    } else {
                                        mChileData.setSelected(false);
                                    }
                                }
                            }
                        }
                    }
                }

//                for (int i = 0; i < mWomenCategoryData.getChildrenData().size(); i++) {
//                    CategoryList.ChildData childData = mWomenCategoryData.getChildrenData().get(i);
//                    LogUtils.e("", "Main getId:" + childData.getId() + " isSelected:" + childData.isSelected());
//
//                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
//                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
//                            SubChildData subChildData = childData.getChildrenData().get(j);
//                            LogUtils.e("", "Sub InfoChild getId:" + subChildData.getId() + " isSelected:" + subChildData.isSelected());
//
//                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
//                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
//                                    LogUtils.e("", "Sub InfoChild InfoChild getId:" + mChileData.getId() + " isSelected:" + mChileData.isSelected());
//                                }
//                            }
//                        }
//                    }
//
//                }

                if (level == 1 || level == 2) {
                    mGeneralParentLevel.notifyDataSetChanged();
                }

//                LogUtils.e("", "isWomenOptionSelected::" + isWomenOptionSelected);
//                LogUtils.e("", "isMenOptionSelected::" + isMenOptionSelected);
//                LogUtils.e("", "isKidOptionSelected::" + isKidOptionSelected);

                isGeneralOptionSelected = true;

//                if (isMenOptionSelected) {
//                    clearMenSelection();
//                }
//
//                if (isKidOptionSelected) {
//                    clearKidSelection();
//                }


            }
        });
        elvGeneral.setAdapter(mGeneralParentLevel);

//        mWomenParentLevel = new ParentLevel(getActivity(), mWomenCategoryData, elvWomen, new ParentLevel.OnOptionClickListener() {
//            @Override
//            public void onOptionClicked(int level, int id) {
//                LogUtils.e("", "level::" + level + " id::" + id);
//
//                selected_category_id = "" + id;
//
//                for (int i = 0; i < mWomenCategoryData.getChildrenData().size(); i++) {
//                    CategoryList.ChildData childData = mWomenCategoryData.getChildrenData().get(i);
//                    if (childData.getId() == id && level == 1) {
//                        childData.setSelected(true);
//                        if (i == 0) {
//                            selected_category_name = "Women->All";
//                        } else {
//                            selected_category_name = "Women->" + childData.getName();
//                        }
//                        ProductsItemsFragment.mMainCateName = "women";
//                        ibBack.performClick();
//
//                    } else {
//                        childData.setSelected(false);
//                    }
//
//                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
//                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
//
//                            SubChildData subChildData = childData.getChildrenData().get(j);
//                            if (subChildData.getId() == id && level == 2) {
//                                subChildData.setSelected(true);
//
//                                if (j == 0) {
//                                    selected_category_name = "Women->" + childData.getName().replaceFirst("All ", "").trim();
//                                } else {
//                                    selected_category_name = "Women->" + childData.getName().trim() + "->" + subChildData.getName();
//                                }
//                                ProductsItemsFragment.mMainCateName = "women";
//                                ibBack.performClick();
//
//                            } else {
//                                subChildData.setSelected(false);
//                            }
//
//                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
//                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
////                                    LogUtils.e("", "isParent::" + isParent + " id::" + id + " mChileData.getId()::" + mChileData.getId());
//                                    if (mChileData.getId() == id && level == 3) {
//                                        mChileData.setSelected(true);
//                                        if (k == 0) {
//                                            selected_category_name = "Women->" + childData.getName().trim() + "->" + mChileData.getName().replace("All ", "");
//                                        } else {
//                                            selected_category_name = "Women->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
//                                        }
//                                        ProductsItemsFragment.mMainCateName = "women";
//                                        ibBack.performClick();
//
//                                    } else {
//                                        mChileData.setSelected(false);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
////                for (int i = 0; i < mWomenCategoryData.getChildrenData().size(); i++) {
////                    CategoryList.ChildData childData = mWomenCategoryData.getChildrenData().get(i);
////                    LogUtils.e("", "Main getId:" + childData.getId() + " isSelected:" + childData.isSelected());
////
////                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
////                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
////                            SubChildData subChildData = childData.getChildrenData().get(j);
////                            LogUtils.e("", "Sub InfoChild getId:" + subChildData.getId() + " isSelected:" + subChildData.isSelected());
////
////                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
////                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
////                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
////                                    LogUtils.e("", "Sub InfoChild InfoChild getId:" + mChileData.getId() + " isSelected:" + mChileData.isSelected());
////                                }
////                            }
////                        }
////                    }
////
////                }
//
//                if (level == 1 || level == 2) {
//                    mWomenParentLevel.notifyDataSetChanged();
//                }
//
////                LogUtils.e("", "isWomenOptionSelected::" + isWomenOptionSelected);
////                LogUtils.e("", "isMenOptionSelected::" + isMenOptionSelected);
////                LogUtils.e("", "isKidOptionSelected::" + isKidOptionSelected);
//
//                isWomenOptionSelected = true;
//
//                if (isMenOptionSelected) {
//                    clearMenSelection();
//                }
//
//                if (isKidOptionSelected) {
//                    clearKidSelection();
//                }
//
//
//            }
//        });
//        elvWomen.setAdapter(mWomenParentLevel);
//
//        mMenParentLevel = new ParentLevel(getActivity(), mMenCategoryData, elvMen, new ParentLevel.OnOptionClickListener() {
//            @Override
//            public void onOptionClicked(int level, int id) {
//                LogUtils.e("", "level::" + level + " id::" + id);
//
//                selected_category_id = "" + id;
//
//                for (int i = 0; i < mMenCategoryData.getChildrenData().size(); i++) {
//                    CategoryList.ChildData childData = mMenCategoryData.getChildrenData().get(i);
//                    if (childData.getId() == id && level == 1) {
//                        childData.setSelected(true);
//                        if (i == 0) {
//                            selected_category_name = "Men->All";
//                        } else {
//                            selected_category_name = "Men->" + childData.getName();
//                        }
//                        ProductsItemsFragment.mMainCateName = "men";
//                        ibBack.performClick();
//
//                    } else {
//                        childData.setSelected(false);
//                    }
//
//                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
//                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
//
//                            SubChildData subChildData = childData.getChildrenData().get(j);
//                            if (subChildData.getId() == id && level == 2) {
//                                subChildData.setSelected(true);
//                                if (j == 0) {
//                                    selected_category_name = "Men->" + childData.getName().replaceFirst("All ", "").trim();
//                                } else {
//                                    selected_category_name = "Men->" + childData.getName().trim() + "->" + subChildData.getName();
//                                }
//                                ProductsItemsFragment.mMainCateName = "men";
//                                ibBack.performClick();
//
//                            } else {
//                                subChildData.setSelected(false);
//                            }
//
//                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
//                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
////                                    LogUtils.e("", "isParent::" + isParent + " id::" + id + " mChileData.getId()::" + mChileData.getId());
//                                    if (mChileData.getId() == id && level == 3) {
//                                        mChileData.setSelected(true);
//                                        if (k == 0) {
//                                            selected_category_name = "Men->" + childData.getName().trim() + "->" + mChileData.getName().replaceFirst("All ", "").trim();
//                                        } else {
//                                            selected_category_name = "Men->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
//                                        }
//                                        ibBack.performClick();
//                                        ProductsItemsFragment.mMainCateName = "men";
//                                    } else {
//                                        mChileData.setSelected(false);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
////                for (int i = 0; i < mMenCategoryData.getChildrenData().size(); i++) {
////                    CategoryList.ChildData childData = mMenCategoryData.getChildrenData().get(i);
////                    LogUtils.e("", "Main getId:" + childData.getId() + " isSelected:" + childData.isSelected());
////
////                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
////                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
////                            SubChildData subChildData = childData.getChildrenData().get(j);
////                            LogUtils.e("", "Sub InfoChild getId:" + subChildData.getId() + " isSelected:" + subChildData.isSelected());
////
////                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
////                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
////                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
////                                    LogUtils.e("", "Sub InfoChild InfoChild getId:" + mChileData.getId() + " isSelected:" + mChileData.isSelected());
////                                }
////                            }
////                        }
////                    }
////
////                }
//
////                if (level == 1 || level == 2) {
//                mMenParentLevel.notifyDataSetChanged();
////                }
//
//                LogUtils.e("", "isWomenOptionSelected::" + isWomenOptionSelected);
//                LogUtils.e("", "isMenOptionSelected::" + isMenOptionSelected);
//                LogUtils.e("", "isKidOptionSelected::" + isKidOptionSelected);
//
//                isMenOptionSelected = true;
//
//                if (isWomenOptionSelected) {
//                    clearWomenSelection();
//                }
//
//                if (isKidOptionSelected) {
//                    clearKidSelection();
//                }
//            }
//        });
//        elvMen.setAdapter(mMenParentLevel);
//
//        mKidParentLevel = new ParentLevel(getActivity(), mKidCategoryData, elvKid, new ParentLevel.OnOptionClickListener() {
//            @Override
//            public void onOptionClicked(int level, int id) {
//                LogUtils.e("", "level::" + level + " id::" + id);
//
//                selected_category_id = "" + id;
//
//                for (int i = 0; i < mKidCategoryData.getChildrenData().size(); i++) {
//                    CategoryList.ChildData childData = mKidCategoryData.getChildrenData().get(i);
//                    if (childData.getId() == id && level == 1) {
//                        childData.setSelected(true);
//                        if (i == 0) {
//                            selected_category_name = "Kids->All";
//                        } else {
//                            selected_category_name = "Kids->" + childData.getName();
//                        }
//                        ProductsItemsFragment.mMainCateName = "Kids";
//                        ibBack.performClick();
//
//                    } else {
//                        childData.setSelected(false);
//                    }
//
//                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
//                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
//
//                            SubChildData subChildData = childData.getChildrenData().get(j);
//                            if (subChildData.getId() == id && level == 2) {
//                                subChildData.setSelected(true);
//                                if (j == 0) {
//                                    selected_category_name = "Kids->" + childData.getName().replaceFirst("All ", "").trim();
//                                } else {
//                                    selected_category_name = "Kids->" + childData.getName().trim() + "->" + subChildData.getName();
//                                }
//                                ProductsItemsFragment.mMainCateName = "Kids";
//                                ibBack.performClick();
//
//                            } else {
//                                subChildData.setSelected(false);
//                            }
//
//                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
//                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
//                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
////                                    LogUtils.e("", "isParent::" + isParent + " id::" + id + " mChileData.getId()::" + mChileData.getId());
//                                    if (mChileData.getId() == id && level == 3) {
//                                        mChileData.setSelected(true);
//                                        if (k == 0) {
//                                            selected_category_name = "Kids->" + childData.getName().trim() + "->" + mChileData.getName().replaceFirst("All ", "").trim();
//                                        } else {
//                                            selected_category_name = "Kids->" + childData.getName().trim() + "->" + subChildData.getName().trim() + "->" + mChileData.getName();
//                                        }
//                                        ProductsItemsFragment.mMainCateName = "Kids";
//                                        ibBack.performClick();
//
//                                    } else {
//                                        mChileData.setSelected(false);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
////                for (int i = 0; i < mKidCategoryData.getChildrenData().size(); i++) {
////                    CategoryList.ChildData childData = mKidCategoryData.getChildrenData().get(i);
////                    LogUtils.e("", "Main getId:" + childData.getId() + " isSelected:" + childData.isSelected());
////
////                    if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
////                        for (int j = 0; j < childData.getChildrenData().size(); j++) {
////                            SubChildData subChildData = childData.getChildrenData().get(j);
////                            LogUtils.e("", "Sub InfoChild getId:" + subChildData.getId() + " isSelected:" + subChildData.isSelected());
////
////                            if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
////                                for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
////                                    SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
////                                    LogUtils.e("", "Sub InfoChild InfoChild getId:" + mChileData.getId() + " isSelected:" + mChileData.isSelected());
////                                }
////                            }
////                        }
////                    }
////
////                }
//
////                if (level == 1 || level == 2) {
//                mKidParentLevel.notifyDataSetChanged();
////                }
//
//                LogUtils.e("", "isWomenOptionSelected::" + isWomenOptionSelected);
//                LogUtils.e("", "isMenOptionSelected::" + isMenOptionSelected);
//                LogUtils.e("", "isKidOptionSelected::" + isKidOptionSelected);
//
//                isKidOptionSelected = true;
//
//                if (isWomenOptionSelected) {
//                    clearWomenSelection();
//                }
//
//                if (isMenOptionSelected) {
//                    clearMenSelection();
//                }
//            }
//        });
//        elvKid.setAdapter(mKidParentLevel);


    }

    public void cleaGeneralSelection() {

        for (int i = 0; i < mGeneralCategoryData.getChildrenData().size(); i++) {
            CategoryList.ChildData childData = mGeneralCategoryData.getChildrenData().get(i);
            childData.setSelected(false);

            if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                for (int j = 0; j < childData.getChildrenData().size(); j++) {

                    SubChildData subChildData = childData.getChildrenData().get(j);
                    subChildData.setSelected(false);

                    if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                        for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                            SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
                            mChileData.setSelected(false);
                        }
                    }
                }
            }
        }

        isGeneralOptionSelected = false;

        if (mGeneralParentLevel != null) {
            mGeneralParentLevel.notifyDataSetChanged();
        }

    }

    public void clearWomenSelection() {

        for (int i = 0; i < mWomenCategoryData.getChildrenData().size(); i++) {
            CategoryList.ChildData childData = mWomenCategoryData.getChildrenData().get(i);
            childData.setSelected(false);

            if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                for (int j = 0; j < childData.getChildrenData().size(); j++) {

                    SubChildData subChildData = childData.getChildrenData().get(j);
                    subChildData.setSelected(false);

                    if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                        for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                            SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
                            mChileData.setSelected(false);
                        }
                    }
                }
            }
        }

        isWomenOptionSelected = false;

        if (mWomenParentLevel != null) {
            mWomenParentLevel.notifyDataSetChanged();
        }

    }

    public void clearMenSelection() {
        for (int i = 0; i < mMenCategoryData.getChildrenData().size(); i++) {
            CategoryList.ChildData childData = mMenCategoryData.getChildrenData().get(i);
            childData.setSelected(false);

            if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                for (int j = 0; j < childData.getChildrenData().size(); j++) {

                    SubChildData subChildData = childData.getChildrenData().get(j);
                    subChildData.setSelected(false);

                    if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                        for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                            SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
                            mChileData.setSelected(false);
                        }
                    }
                }
            }
        }

        isMenOptionSelected = false;

        if (mMenParentLevel != null) {
            mMenParentLevel.notifyDataSetChanged();
        }

    }

    public void clearKidSelection() {
        for (int i = 0; i < mKidCategoryData.getChildrenData().size(); i++) {
            CategoryList.ChildData childData = mKidCategoryData.getChildrenData().get(i);
            childData.setSelected(false);

            if (childData.getChildrenData() != null && childData.getChildrenData().size() > 0) {
                for (int j = 0; j < childData.getChildrenData().size(); j++) {

                    SubChildData subChildData = childData.getChildrenData().get(j);
                    subChildData.setSelected(false);

                    if (subChildData.getChildrenData() != null && subChildData.getChildrenData().size() > 0) {
                        for (int k = 0; k < subChildData.getChildrenData().size(); k++) {
                            SubChildData.ChildData mChileData = subChildData.getChildrenData().get(k);
                            mChileData.setSelected(false);
                        }
                    }
                }
            }
        }

        isKidOptionSelected = false;

        if (mKidParentLevel != null) {
            mKidParentLevel.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        ibBack.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MaintenanceScreenActivity.RC_MAINTANANCE_CODE && requestCode == RESULT_OK
                && data != null && data.getExtras() != null && data.getExtras().containsKey(MaintenanceScreenActivity.IS_RETURN)
                && data.getExtras().getBoolean(MaintenanceScreenActivity.IS_RETURN)) {
            getCategoryList();
        }
    }
}
