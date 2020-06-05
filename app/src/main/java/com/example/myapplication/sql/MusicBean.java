package com.example.myapplication.sql;

/*
 * Package    :com.example.myapplication.sql
 * ClassName  :MusicBean
 * Description:music data
 * Data       :2020/5/25 14:22
 */
public class MusicBean {
    private int id;
    private int type;
    private String path;
    private String name;

    public MusicBean() {
    }

    public MusicBean(int id, int type, String path, String name) {
        this.id = id;
        this.type = type;
        this.path = path;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MusicBean{" +
                "id=" + id +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
