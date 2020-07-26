package com.lljjcoder.style.cityjd;

public class JDCityConfig {

    /**
     * 默认显示的城市数据，只包含省市区名称
     * 定义显示省市区三种显示状态
     * PRO:只显示省份的一级选择器
     * PRO_CITY:显示省份和城市二级联动的选择器
     * PRO_CITY_DIS:显示省份和城市和县区三级联动的选择器
     */
    public enum ShowType {
        PRO_CITY, PRO_CITY_DIS
    }

    private ShowType showType = ShowType.PRO_CITY_DIS;
    public Builder builder;

    public ShowType getShowType() {
        return showType;
    }

    public String getProvince() {
        return builder.province;
    }

    public String getCity() {
        return builder.city;
    }
    public String getArea() {
        return builder.area;
    }

    public void setShowType(ShowType showType) {
        this.showType = showType;
    }

    public JDCityConfig(Builder builder) {
        this.builder = builder;
        this.showType = builder.showType;
    }


    public static class Builder {

        public Builder() {

        }

        public ShowType showType = ShowType.PRO_CITY_DIS;
        public String province, city, area;

        /**
         * 显示省市区三级联动的显示状态
         * PRO_CITY:显示省份和城市二级联动的选择器
         * PRO_CITY_DIS:显示省份和城市和县区三级联动的选择器
         */
        public Builder setJDCityShowType(ShowType showType) {
            this.showType = showType;
            return this;
        }

        /**
         * 省
         */
        public Builder setProvince(String province) {
            this.province = province;
            return this;
        }

        /**
         * 市
         */
        public Builder setCity(String city) {
            this.city = city;
            return this;
        }
        /**
         * 区
         */
        public Builder setArea(String area) {
            this.area = area;
            return this;
        }

        public JDCityConfig build() {
            JDCityConfig config = new JDCityConfig(this);
            return config;
        }
    }
}
