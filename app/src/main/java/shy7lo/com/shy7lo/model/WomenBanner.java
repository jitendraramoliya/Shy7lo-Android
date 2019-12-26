package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JITEN-PC on 27-12-2016.
 */

public class WomenBanner implements Serializable {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Data> data;

    public class Data {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("search_criteria")
        @Expose
        private String searchCriteria;
        @SerializedName("landing_screen_id")
        @Expose
        private Integer landingScreenId;
        @SerializedName("sort_by")
        @Expose
        private String sortBy;
        @SerializedName("sort_direction")
        @Expose
        private String sortDirection;
        @SerializedName("img")
        @Expose
        private String img;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSearchCriteria() {
            return searchCriteria;
        }

        public void setSearchCriteria(String searchCriteria) {
            this.searchCriteria = searchCriteria;
        }

        public Integer getLandingScreenId() {
            return landingScreenId;
        }

        public void setLandingScreenId(Integer landingScreenId) {
            this.landingScreenId = landingScreenId;
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = sortBy;
        }

        public String getSortDirection() {
            return sortDirection;
        }

        public void setSortDirection(String sortDirection) {
            this.sortDirection = sortDirection;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }

}
