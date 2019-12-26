package shy7lo.com.shy7lo.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import shy7lo.com.shy7lo.AddAddressActivity;
import shy7lo.com.shy7lo.MyAddressActivity;
import shy7lo.com.shy7lo.R;
import shy7lo.com.shy7lo.model.MyAddressResponse;
import shy7lo.com.shy7lo.pref.MyPref;
import shy7lo.com.shy7lo.utils.Constant;
import shy7lo.com.shy7lo.utils.IntentHandler;

public class MyAddressAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyAddressResponse.Address> mMyAddressList = new ArrayList<>();

    public MyAddressAdapter(Context mContext) {
        this.mContext = mContext;
    }


    public void addAll(List<MyAddressResponse.Address> mTempMyAddressList) {
        if (mMyAddressList != null && mMyAddressList.size() > 0) {
            mMyAddressList.clear();
        }

        if (mTempMyAddressList != null && mTempMyAddressList.size() > 0) {
            mMyAddressList.addAll(mTempMyAddressList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMyAddressList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMyAddressList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_my_address, null);
            ViewHolder holder = new ViewHolder();
            holder.tvDefaultInfo = view.findViewById(R.id.tvDefaultInfo);
            holder.tvName = view.findViewById(R.id.tvName);
            holder.tvAddress = view.findViewById(R.id.tvAddress);
            holder.tvCity = view.findViewById(R.id.tvCity);
            holder.tvCountry = view.findViewById(R.id.tvCountry);
            holder.tvTelephone = view.findViewById(R.id.tvTelephone);
            holder.tvEdit = view.findViewById(R.id.tvEdit);
            holder.tvDeleteAddress = view.findViewById(R.id.tvDeleteAddress);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        final MyAddressResponse.Address mAddress = mMyAddressList.get(i);

        holder.tvEdit.setPaintFlags(holder.tvEdit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tvDeleteAddress.setPaintFlags(holder.tvDeleteAddress.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (MyPref.getPref(getActivity(), MyPref.DEFAULT_LANGUAGE, "").equals(MyPref.LANGUAGE_Arabic)) {
            holder.tvDefaultInfo.setScaleX(-1);
            holder.tvName.setScaleX(-1);
            holder.tvAddress.setScaleX(-1);
            holder.tvCity.setScaleX(-1);
            holder.tvCountry.setScaleX(-1);
            holder.tvTelephone.setScaleX(-1);
            holder.tvEdit.setScaleX(-1);
            holder.tvDeleteAddress.setScaleX(-1);

            holder.tvDefaultInfo.setGravity(Gravity.RIGHT);
            holder.tvName.setGravity(Gravity.RIGHT);
            holder.tvAddress.setGravity(Gravity.RIGHT);
            holder.tvCity.setGravity(Gravity.RIGHT);
            holder.tvCountry.setGravity(Gravity.RIGHT);
            holder.tvTelephone.setGravity(Gravity.RIGHT);
        } else {
            holder.tvDefaultInfo.setScaleX(1);
            holder.tvName.setScaleX(1);
            holder.tvAddress.setScaleX(1);
            holder.tvCity.setScaleX(1);
            holder.tvCountry.setScaleX(1);
            holder.tvTelephone.setScaleX(1);
            holder.tvEdit.setScaleX(1);
            holder.tvDeleteAddress.setScaleX(1);

            holder.tvDefaultInfo.setGravity(Gravity.LEFT);
            holder.tvName.setGravity(Gravity.LEFT);
            holder.tvAddress.setGravity(Gravity.LEFT);
            holder.tvCity.setGravity(Gravity.LEFT);
            holder.tvCountry.setGravity(Gravity.LEFT);
            holder.tvTelephone.setGravity(Gravity.LEFT);
        }

        if (mAddress != null) {

            holder.tvName.setText(mAddress.firstname + " " + mAddress.lastname);
//            if (!TextUtils.isEmpty(mAddress.postcode)) {
//                holder.tvCity.setText(mAddress.city + ", " + mAddress.postcode);
//            } else {
                holder.tvCity.setText(mAddress.city);
//            }
            holder.tvCountry.setText(mAddress.countryId);
            holder.tvTelephone.setText(mContext.getString(R.string.mobile_no)+ ". " + mAddress.telephone);

            if (mAddress.street != null && mAddress.street.size() > 0) {
                holder.tvAddress.setVisibility(View.VISIBLE);
                holder.tvAddress.setText(mAddress.street.get(0));
            } else {
                holder.tvAddress.setVisibility(View.GONE);
            }

            if (mAddress.countryId.equalsIgnoreCase("OM")) {
                holder.tvCountry.setText("Oman");
            } else if (mAddress.countryId.equalsIgnoreCase("SA")) {
                holder.tvCountry.setText("Saudi Arabia");
            } else if (mAddress.countryId.equalsIgnoreCase("AE")) {
                holder.tvCountry.setText("UAE");
            } else if (mAddress.countryId.equalsIgnoreCase("QA")) {
                holder.tvCountry.setText("Qatar");
            } else if (mAddress.countryId.equalsIgnoreCase("KW")) {
                holder.tvCountry.setText("Kuwait");
            } else if (mAddress.countryId.equalsIgnoreCase("BH")) {
                holder.tvCountry.setText("Bahrain");
            }

            if (mAddress.defaultShipping || mAddress.defaultBilling) {
                holder.tvDefaultInfo.setVisibility(View.VISIBLE);
                holder.tvDeleteAddress.setVisibility(View.GONE);

                String mDefaultInfo = "";
                if (mAddress.defaultShipping) {
                    mDefaultInfo = mContext.getString(R.string.default_shiping_address);
                    if (mAddress.defaultBilling) {
                        mDefaultInfo = mDefaultInfo + "\n" + mContext.getString(R.string.billing_address);
                    }
                } else if (mAddress.defaultBilling) {
                    mDefaultInfo = mContext.getString(R.string.billing_address);
                    if (mAddress.defaultShipping) {
                        mDefaultInfo = mDefaultInfo + "\n" + mContext.getString(R.string.default_shiping_address);
                    }
                }

                holder.tvDefaultInfo.setText(mDefaultInfo);

            } else {
                holder.tvDefaultInfo.setVisibility(View.INVISIBLE);
                holder.tvDeleteAddress.setVisibility(View.VISIBLE);
            }
        }

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString(Constant.BNDL_ADDRESS, new Gson().toJson(mAddress));
                bundle.putInt(Constant.BNDL_ADDRESS_SIZE, mMyAddressList.size());
                IntentHandler.startActivity(getActivity(), AddAddressActivity.class, bundle);
            }
        });

        holder.tvDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyAddressActivity) mContext).deleteAddress(mAddress, i);
            }
        });

        return view;
    }

    private class ViewHolder {

        TextView tvDefaultInfo, tvName, tvAddress, tvCity, tvCountry, tvTelephone, tvEdit, tvDeleteAddress;

    }


    private Context getActivity() {
        return mContext;
    }

}
