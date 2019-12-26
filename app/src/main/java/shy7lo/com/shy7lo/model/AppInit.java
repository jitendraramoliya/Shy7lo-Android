package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jiten on 05-04-2018.
 */

public class AppInit {


    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("api_token")
    @Expose
    public String apiToken;
    @SerializedName("landing_screens")
    @Expose
    public LandingScreens landingScreens;
    @SerializedName("integrations")
    @Expose
    public Integrations integrations;
    @SerializedName("stores")
    @Expose
    public List<Store> stores = null;
    @SerializedName("currencies")
    @Expose
    public Currencies currencies;
    @SerializedName("countries")
    @Expose
    public List<Country> countries = null;
    @SerializedName("coupon_code")
    @Expose
    public CouponCode couponCode;

//    public class Data {
//
//        @SerializedName("api_token")
//        @Expose
//        public String apiToken;
//        @SerializedName("integrations")
//        @Expose
//        public Integrations integrations;
//
//    }

    public class CouponCode{
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("url_en")
        @Expose
        public String url_en;
        @SerializedName("url_ar")
        @Expose
        public String url_ar;
    }

    public class Integrations {

        @SerializedName("tune")
        @Expose
        public String tune;
        @SerializedName("adjust")
        @Expose
        public String adjust;
        @SerializedName("one_sginal")
        @Expose
        public String oneSginal;
        @SerializedName("fabric")
        @Expose
        public String fabric;
        @SerializedName("criteo")
        @Expose
        public String criteo;

    }

    public class LandingScreens {

        @SerializedName("base_screens")
        @Expose
        public List<BaseScreen> baseScreens = null;
        @SerializedName("child_screens")
        @Expose
        public List<ChildScreen> childScreens = null;

    }

    public class BaseScreen {

        @SerializedName("title_en")
        @Expose
        public String titleEn;
        @SerializedName("title_ar")
        @Expose
        public String titleAr;
        @SerializedName("mode")
        @Expose
        public String mode;
        @SerializedName("attribute")
        @Expose
        public Attribute attribute;


    }

    public class Attribute{

        @SerializedName("cat_id")
        @Expose
        public String cat_id;
        @SerializedName("url_en")
        @Expose
        public String url;
        @SerializedName("url_ar")
        @Expose
        public String urlAr;
        @SerializedName("query")
        @Expose
        public String query;
    }

    public class ChildScreen {

        @SerializedName("title_en")
        @Expose
        public String titleEn;
        @SerializedName("title_ar")
        @Expose
        public String titleAr;
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("url_ar")
        @Expose
        public String urlAr;
    }

    public class Country {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("two_letter_abbreviation")
        @Expose
        public String twoLetterAbbreviation;
        @SerializedName("three_letter_abbreviation")
        @Expose
        public String threeLetterAbbreviation;
        @SerializedName("full_name_locale")
        @Expose
        public String fullNameArabic;
        @SerializedName("full_name_english")
        @Expose
        public String fullNameEnglish;
        @SerializedName("currency_en")
        @Expose
        public String currencyEn;
        @SerializedName("currency_ar")
        @Expose
        public String currencyAr;
        @SerializedName("exchange_rate")
        @Expose
        public float exchangeRate;

    }

    public class Currencies {


        @SerializedName("base_currency_code")
        @Expose
        public String baseCurrencyCode;
        @SerializedName("base_currency_symbol")
        @Expose
        public String baseCurrencySymbol;
        @SerializedName("default_display_currency_code")
        @Expose
        public String defaultDisplayCurrencyCode;
        @SerializedName("default_display_currency_symbol")
        @Expose
        public String defaultDisplayCurrencySymbol;
        @SerializedName("available_currency_codes")
        @Expose
        public List<String> availableCurrencyCodes = null;
        @SerializedName("exchange_rates")
        @Expose
        public List<ExchangeRate> exchangeRates = null;

    }

    public class ExchangeRate {


        @SerializedName("currency_to")
        @Expose
        public String currencyTo;
        @SerializedName("rate")
        @Expose
        public Float rate;

    }

    public class Store {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("website_id")
        @Expose
        public Integer websiteId;
        @SerializedName("locale")
        @Expose
        public String locale;
        @SerializedName("base_currency_code")
        @Expose
        public String baseCurrencyCode;
        @SerializedName("default_display_currency_code")
        @Expose
        public String defaultDisplayCurrencyCode;
        @SerializedName("timezone")
        @Expose
        public String timezone;
        @SerializedName("weight_unit")
        @Expose
        public String weightUnit;
        @SerializedName("base_url")
        @Expose
        public String baseUrl;
        @SerializedName("base_link_url")
        @Expose
        public String baseLinkUrl;
        @SerializedName("base_static_url")
        @Expose
        public String baseStaticUrl;
        @SerializedName("base_media_url")
        @Expose
        public String baseMediaUrl;
        @SerializedName("secure_base_url")
        @Expose
        public String secureBaseUrl;
        @SerializedName("secure_base_link_url")
        @Expose
        public String secureBaseLinkUrl;
        @SerializedName("secure_base_static_url")
        @Expose
        public String secureBaseStaticUrl;
        @SerializedName("secure_base_media_url")
        @Expose
        public String secureBaseMediaUrl;

    }

}
