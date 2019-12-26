package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JITEN-PC on 28-12-2016.
 */

public class SortingList implements Serializable {

    @SerializedName("success")
    @Expose
    public String success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Data data;

    public class Data implements Serializable {

        @SerializedName("layeredData")
        @Expose
        private List<FilterData> filterData = null;
        @SerializedName("sortingData")
        @Expose
        private List<SortingData> sortingData = null;

        public List<FilterData> getFilterData() {
            return filterData;
        }

        public void setFilterData(List<FilterData> filterData) {
            this.filterData = filterData;
        }

        public List<SortingData> getSortingData() {
            return sortingData;
        }

        public void setSortingData(List<SortingData> sortingData) {
            this.sortingData = sortingData;
        }

    }

    public class FilterData implements Serializable {

        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("code")
        @Expose
        private String code;

        private boolean isFilterSelected;
        private String filterValue;
        private String filterId;

        @SerializedName("options")
        @Expose
        private List<Option> options = null;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<Option> getOptions() {
            return options;
        }

        public void setOptions(List<Option> options) {
            this.options = options;
        }

        public boolean isFilterSelected() {
            return isFilterSelected;
        }

        public void setFilterSelected(boolean filterSelected) {
            isFilterSelected = filterSelected;
        }

        public String getFilterValue() {
            return filterValue;
        }

        public void setFilterValue(String filterValue) {
            this.filterValue = filterValue;
        }

        public String getFilterId() {
            return filterId;
        }

        public void setFilterId(String filterId) {
            this.filterId = filterId;
        }
    }

    public class Option implements Serializable {

        @SerializedName("label")
        @Expose
        private String label;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("count")
        @Expose
        private String count;
        @SerializedName("min")
        @Expose
        public int minPrice;
        @SerializedName("max")
        @Expose
        public int maxPrice;
        @SerializedName("step_size")
        @Expose
        public int stepSize;

        public boolean status;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }


    public class SortingData implements Serializable {

        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("label")
        @Expose
        private String label;
        private String direction;
        public boolean status;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }


}
