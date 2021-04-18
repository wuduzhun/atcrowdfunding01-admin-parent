package com.atguigu.crowd.entity;

import java.util.List;
import java.util.Map;

public class Student
{
    private Integer stuId;
    private String name;
    private Integer age;
    private Address address;
    private List<Subject> subjectList;
    private Map<String,String> map;

    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuId=" + stuId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", subjectList=" + subjectList +
                ", map=" + map +
                '}';
    }

    public Integer getStuId() {
        return stuId;
    }

    public void setStuId(Integer stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Student(Integer stuId, String name, Integer age, Address address, List<Subject> subjectList, Map<String, String> map) {
        this.stuId = stuId;
        this.name = name;
        this.age = age;
        this.address = address;
        this.subjectList = subjectList;
        this.map = map;
    }
}
