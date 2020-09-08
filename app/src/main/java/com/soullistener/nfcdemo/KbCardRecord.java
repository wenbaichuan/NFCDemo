package com.soullistener.nfcdemo;

public class KbCardRecord {

    private String userName;

    private Long recordDatetime;

    private int type;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getRecordDatetime() {
        return recordDatetime;
    }

    public void setRecordDatetime(Long recordDatetime) {
        this.recordDatetime = recordDatetime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
