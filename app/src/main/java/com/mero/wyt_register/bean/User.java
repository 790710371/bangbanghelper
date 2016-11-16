package com.mero.wyt_register.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chenlei on 2016/11/15.
 */

public class User implements Parcelable {
    String wyt_user_name;//用户名

    public String getWyt_user_name() {
        return wyt_user_name;
    }

    public void setWyt_user_name(String wyt_user_name) {
        this.wyt_user_name = wyt_user_name;
    }

    public String getWyt_account() {
        return wyt_account;
    }

    public void setWyt_account(String wyt_account) {
        this.wyt_account = wyt_account;
    }

    public String getWyt_pwd() {
        return wyt_pwd;
    }

    public void setWyt_pwd(String wyt_pwd) {
        this.wyt_pwd = wyt_pwd;
    }

    String wyt_account;//账户名
    String wyt_pwd;//密码

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.wyt_user_name);
        dest.writeString(this.wyt_account);
        dest.writeString(this.wyt_pwd);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.wyt_user_name = in.readString();
        this.wyt_account = in.readString();
        this.wyt_pwd = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "wyt_user_name='" + wyt_user_name + '\'' +
                ", wyt_account='" + wyt_account + '\'' +
                ", wyt_pwd='" + wyt_pwd + '\'' +
                '}';
    }

}
