package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jiten on 22-10-2017.
 */

public class InitialData {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {

        @SerializedName("version")
        @Expose
        public String version;
        @SerializedName("currencies")
        @Expose
        public CurrencyList.Currencies currencies;
        @SerializedName("cart_id")
        @Expose
        public String cartId;

    }

//    public class Currencies {
//
//        @SerializedName("exchange_rates")
//        @Expose
//        public List<ExchangeRate> exchangeRates = null;
//
//        public List<ExchangeRate> getExchangeRates() {
//            return exchangeRates;
//        }
//
//        public void setExchangeRates(List<ExchangeRate> exchangeRates) {
//            this.exchangeRates = exchangeRates;
//        }
//
//    }
//
//    public class ExchangeRate {
//
//        @SerializedName("currency_to")
//        @Expose
//        private String currencyTo;
//        @SerializedName("rate")
//        @Expose
//        private float rate;
//
//        public String getCurrencyTo() {
//            return currencyTo;
//        }
//
//        public void setCurrencyTo(String currencyTo) {
//            this.currencyTo = currencyTo;
//        }
//
//        public float getRate() {
//            return rate;
//        }
//
//        public void setRate(float rate) {
//            this.rate = rate;
//        }
//
//    }

}
