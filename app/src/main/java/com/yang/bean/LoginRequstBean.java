package com.yang.bean;

import com.yang.utils.JwtUtil;

public class LoginRequstBean {

    public LoginRequstBean() {
    }

    public LoginRequstBean(String mobile, String password) {
        this.mobile = mobile;
        setPassword(password);
    }

    private String mobile;
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = JwtUtil.encrypt(new Password(password));
    }
}
