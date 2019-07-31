package com.gant.secondhandsm.domain;

/**
 * Author: 甘明波
 * 2019-07-23
 */
public class TranHistory {
    private String id;
    private String createBy;
    private String createTime;
    private String expectedDate;
    private String money;
    private String stage;
    private String tranId;

    public String getId() {
        return id;
    }

    public TranHistory setId(String id) {
        this.id = id;
        return this;
    }

    public String getCreateBy() {
        return createBy;
    }

    public TranHistory setCreateBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public TranHistory setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public TranHistory setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
        return this;
    }

    public String getMoney() {
        return money;
    }

    public TranHistory setMoney(String money) {
        this.money = money;
        return this;
    }

    public String getStage() {
        return stage;
    }

    public TranHistory setStage(String stage) {
        this.stage = stage;
        return this;
    }

    public String getTranId() {
        return tranId;
    }

    public TranHistory setTranId(String tranId) {
        this.tranId = tranId;
        return this;
    }

    @Override
    public String toString() {
        return "TranHistory{" +
                "id='" + id + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime='" + createTime + '\'' +
                ", expectedDate='" + expectedDate + '\'' +
                ", money='" + money + '\'' +
                ", stage='" + stage + '\'' +
                ", tranId='" + tranId + '\'' +
                '}';
    }
}
