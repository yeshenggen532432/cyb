package com.qwb.view.company.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 搜索公司
 */
public class SearchCompanyBean extends BaseBean {

    private List<Company> companys;

    public List<Company> getCompanys() {
        return companys;
    }

    public void setCompanys(List<Company> companys) {
        this.companys = companys;
    }

    public class Company{
        private int deptId;//公司id
        private String deptNm;//公司名称

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }

        public String getDeptNm() {
            return deptNm;
        }

        public void setDeptNm(String deptNm) {
            this.deptNm = deptNm;
        }
    }
}
