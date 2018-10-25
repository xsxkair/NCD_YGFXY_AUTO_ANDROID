package com.ncd.xsx.ncd_ygfxy.Databases.Entity;

import com.j256.ormlite.field.DatabaseField;

public class Patient extends BaseEntity {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    private Integer id;

    //病人住院号或者门诊号等唯一编号
    @DatabaseField(canBeNull = false, unique = true)
    private String sickid;

    //病人姓名
    @DatabaseField(canBeNull = false)
    private String name;

    //病人性别
    @DatabaseField(canBeNull = true)
    private Boolean sex;                        //true - man, false - woman

    //病人年龄
    @DatabaseField(canBeNull = true)
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSickid() {
        return sickid;
    }

    public void setSickid(String sickid) {
        this.sickid = sickid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", sickid='" + sickid + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }
}
