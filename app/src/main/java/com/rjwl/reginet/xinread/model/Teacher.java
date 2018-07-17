package com.rjwl.reginet.xinread.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/8.
 * 老师对象
 */

public class Teacher implements Serializable{
    private String name;
    private String gender;
    private int id;
    private String schoolname;

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
