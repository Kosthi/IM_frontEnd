package com.kosthi.wechatclient.Model.Data;

public class friendData {
    private String head;
    private String account;

    public friendData(String head, String account) {
        this.head = head;
        this.account = account;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
