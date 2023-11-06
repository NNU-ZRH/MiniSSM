package com.atzrh.pojo;

/**
 * @author zrh
 * @version 1.0.0
 * @title User
 * @description <TODO description class purpose>
 * @create 2023/11/3 22:40
 **/
public class User {
    public User() {
    }

    public User(int id, String name, int year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    int id;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year=" + year +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    String name;
    int year;
}
