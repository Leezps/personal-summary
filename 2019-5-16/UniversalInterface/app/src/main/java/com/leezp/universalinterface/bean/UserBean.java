package com.leezp.universalinterface.bean;

public class UserBean {
    private String mUserName;
    private String mPassWord;

    public UserBean(String mUserName, String mPassWord) {
        this.mUserName = mUserName;
        this.mPassWord = mPassWord;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmPassWord() {
        return mPassWord;
    }

    public void setmPassWord(String mPassWord) {
        this.mPassWord = mPassWord;
    }
}
