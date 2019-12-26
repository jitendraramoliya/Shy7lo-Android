package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jiten on 09-05-2018.
 */

public class SubCategoryList {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("parent_id")
    @Expose
    public Integer parentId;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("is_active")
    @Expose
    public Boolean isActive;
    @SerializedName("position")
    @Expose
    public Integer position;
    @SerializedName("level")
    @Expose
    public Integer level;
    @SerializedName("product_count")
    @Expose
    public Integer productCount;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @SerializedName("children_data")
    @Expose
    public List<CategoryData> childrenData = null;

    public class CategoryData {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("parent_id")
        @Expose
        public Integer parentId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("is_active")
        @Expose
        public Boolean isActive;
        @SerializedName("position")
        @Expose
        public Integer position;
        @SerializedName("level")
        @Expose
        public Integer level;
        @SerializedName("product_count")
        @Expose
        public Integer productCount;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        @SerializedName("children_data")
        @Expose
        public List<CategoryDataInner> childrenDataInner = null;
//        public List<CategoryData> childrenData = null;
    }

    public class CategoryDataInner {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("parent_id")
        @Expose
        public Integer parentId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("is_active")
        @Expose
        public Boolean isActive;
        @SerializedName("position")
        @Expose
        public Integer position;
        @SerializedName("level")
        @Expose
        public Integer level;
        @SerializedName("product_count")
        @Expose
        public Integer productCount;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

    }
}
