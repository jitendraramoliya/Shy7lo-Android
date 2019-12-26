//package shy7lo.com.shy7lo.model;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.util.List;
//
///**
// * Created by Jiten on 29-03-2018.
// */
//
//public class HomeTabList {
//
//
//    @SerializedName("success")
//    @Expose
//    public Boolean success;
//    @SerializedName("status")
//    @Expose
//    public Integer status;
//    @SerializedName("data")
//    @Expose
//    public Data data;
//
//    public class Data {
//
//        @SerializedName("base_screens")
//        @Expose
//        public List<BaseScreen> baseScreens = null;
//        @SerializedName("child_screens")
//        @Expose
//        public List<ChildScreen> childScreens = null;
//
//    }
//
//    public class BaseScreen {
//
//        @SerializedName("title_en")
//        @Expose
//        public String titleEn;
//        @SerializedName("title_ar")
//        @Expose
//        public String titleAr;
//        @SerializedName("url")
//        @Expose
//        public String url;
//
//    }
//
//    public class ChildScreen {
//
//        @SerializedName("title_en")
//        @Expose
//        public String titleEn;
//        @SerializedName("title_ar")
//        @Expose
//        public String titleAr;
//        @SerializedName("url")
//        @Expose
//        public String url;
//
//    }
//}
