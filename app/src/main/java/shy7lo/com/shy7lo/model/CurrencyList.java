package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JITEN-PC on 27-12-2016.
 */

public class CurrencyList {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public Currencies currencies;

    public class Currencies {

        @SerializedName("exchange_rates")
        @Expose
        private List<ExchangeRate> exchangeRates = null;

        public List<ExchangeRate> getExchangeRates() {
            return exchangeRates;
        }

        public void setExchangeRates(List<ExchangeRate> exchangeRates) {
            this.exchangeRates = exchangeRates;
        }
    }

    public class ExchangeRate {

        @SerializedName("currency_to")
        @Expose
        private String currencyTo;
        @SerializedName("rate")
        @Expose
        private float rate;

        public String getCurrencyTo() {
            return currencyTo;
        }

        public void setCurrencyTo(String currencyTo) {
            this.currencyTo = currencyTo;
        }

        public float getRate() {
            return rate;
        }

        public void setRate(float rate) {
            this.rate = rate;
        }

    }

}
