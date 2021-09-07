
package com.planb.test;



import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;


class TestExample {

    Integer exampleId;
    String exampleName;
    String exampleType;

    LocalDate exampleDate;

    Date exampleTime;


    @TableField(exist = false)
    String subName;

    public Integer getExampleId() {
        return exampleId;
    }

    public void setExampleId(Integer exampleId) {
        this.exampleId = exampleId;
    }

    public String getExampleName() {
        return exampleName;
    }

    public void setExampleName(String exampleName) {
        this.exampleName = exampleName;
    }

    public String getExampleType() {
        return exampleType;
    }

    public void setExampleType(String exampleType) {
        this.exampleType = exampleType;
    }

    public LocalDate getExampleDate() {
        return exampleDate;
    }

    public void setExampleDate(LocalDate exampleDate) {
        this.exampleDate = exampleDate;
    }

    public Date getExampleTime() {
        return exampleTime;
    }

    public void setExampleTime(Date exampleTime) {
        this.exampleTime = exampleTime;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }
}