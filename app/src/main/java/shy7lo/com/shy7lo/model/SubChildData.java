package shy7lo.com.shy7lo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JITEN-PC on 27-12-2016.
 */
public class SubChildData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("parent_id")
    @Expose
    private Integer parentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("product_count")
    @Expose
    private Integer productCount;
    @SerializedName("children_data")
    @Expose
    private List<ChildData> childrenData = null;

    private boolean isSelected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public List<ChildData> getChildrenData() {
        return childrenData;
    }

    public void setChildrenData(List<ChildData> childrenData) {
        this.childrenData = childrenData;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public class ChildData {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("parent_id")
        @Expose
        private Integer parentId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("is_active")
        @Expose
        private Boolean isActive;
        @SerializedName("position")
        @Expose
        private Integer position;
        @SerializedName("level")
        @Expose
        private Integer level;
        @SerializedName("product_count")
        @Expose
        private Integer productCount;
        @SerializedName("children_data")
        @Expose
        private List<Object> childrenData = null;

        private boolean isSelected;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getParentId() {
            return parentId;
        }

        public void setParentId(Integer parentId) {
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public Integer getProductCount() {
            return productCount;
        }

        public void setProductCount(Integer productCount) {
            this.productCount = productCount;
        }

        public List<Object> getChildrenData() {
            return childrenData;
        }

        public void setChildrenData(List<Object> childrenData) {
            this.childrenData = childrenData;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

}
