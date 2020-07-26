package com.qwb.view.company.model;

import com.qwb.view.base.model.BaseBean;

/**
 * 公司信息
 */
public class CompanyInfoBean extends BaseBean {

    private CompanyInfo data;

    public CompanyInfo getData() {
        return data;
    }

    public void setData(CompanyInfo data) {
        this.data = data;
    }

    public class CompanyInfo{
//        "id": 285,
//                "name": "企微宝666",
//                "industryId": "176055675691995136",
//                "categoryId": "176055680502861824",
//                "brand": "1231",
//                "leader": "123",
//                "tel": "123",
//                "email": "1231",
//                "employeeCount": 1,
//                "salesmanCount": 1,
//                "bizLicenseNumber": "1123123",
//                "bizLicensePic": "/upload/xmqwbwlkjyxgs285/logo/1569033093523.jpg"
        private int id;
        private String name;
        private String industryId;//行业id
        private String categoryId;//行业分类id
        private String brand;//品牌
        private String leader;//负责人
        private String tel;
        private String email;
        private String employeeCount;//员工人数
        private String salesmanCount;//业务员人数
        private String bizLicenseNumber;//营业执业号
        private String bizLicensePic;//图片

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIndustryId() {
            return industryId;
        }

        public void setIndustryId(String industryId) {
            this.industryId = industryId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getLeader() {
            return leader;
        }

        public void setLeader(String leader) {
            this.leader = leader;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmployeeCount() {
            return employeeCount;
        }

        public void setEmployeeCount(String employeeCount) {
            this.employeeCount = employeeCount;
        }

        public String getSalesmanCount() {
            return salesmanCount;
        }

        public void setSalesmanCount(String salesmanCount) {
            this.salesmanCount = salesmanCount;
        }

        public String getBizLicenseNumber() {
            return bizLicenseNumber;
        }

        public void setBizLicenseNumber(String bizLicenseNumber) {
            this.bizLicenseNumber = bizLicenseNumber;
        }

        public String getBizLicensePic() {
            return bizLicensePic;
        }

        public void setBizLicensePic(String bizLicensePic) {
            this.bizLicensePic = bizLicensePic;
        }
    }
}
