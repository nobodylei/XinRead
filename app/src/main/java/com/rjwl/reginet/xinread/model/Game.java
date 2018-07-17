package com.rjwl.reginet.xinread.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/18.
 */

public class Game implements Serializable{
    private int titleId;
    private String text;
    private String A;
    private String B;
    private String C;
    private String tru;

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getTru() {
        return tru;
    }

    public void setTru(String tru) {
        this.tru = tru;
    }
}
