package com.qwb.view.company.model;

import com.qwb.view.base.model.XbaseBean;

import java.util.List;

/**
 * 平台业务员
 */
public class CompanySalesmanBean extends XbaseBean {

    private List<CompanySalesman> data;

    public List<CompanySalesman> getData() {
        return data;
    }

    public void setData(List<CompanySalesman> data) {
        this.data = data;
    }

    public class CompanySalesman {
        private Integer id;
        private String name;
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }



}
