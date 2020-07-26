package com.qwb.view.company.model;

import com.qwb.view.base.model.BaseBean;

import java.util.List;

/**
 * 公司所属行业
 */
public class CompanyIndustryBean extends BaseBean {

    private List<CompanyIndustry> data;

    public List<CompanyIndustry> getData() {
        return data;
    }

    public void setData(List<CompanyIndustry> data) {
        this.data = data;
    }

    public class CompanyIndustry{
//      "id": "176055676530855936",
//              "no": "058",
//              "parentId": "-1",
//              "name": "电信和其他信息传输服务业",
//              "code": "60",
//              "delFlag": 0,
//              "sort": 1
        private String id;
        private String no;
        private String parentId;
        private String name;
        private String code;
        private int delFlag;
        private int sort;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }
}
