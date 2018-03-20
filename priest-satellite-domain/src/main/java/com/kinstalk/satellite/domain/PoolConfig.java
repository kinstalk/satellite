package com.kinstalk.satellite.domain;

import java.io.Serializable;

/**
 * Created by zhangchuanqi on 16/7/8.
 */
public class PoolConfig implements Serializable{

    private Integer id;

    private String remainGroups;

    private String remainSize;

    private Integer groups;

    private String lastMaxNumber="0";

    private Integer size;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemainGroups() {
        return remainGroups;
    }

    public void setRemainGroups(String remainGroups) {
        this.remainGroups = remainGroups;
    }

    public String getRemainSize() {
        return remainSize;
    }

    public void setRemainSize(String remainSize) {
        this.remainSize = remainSize;
    }

    public Integer getGroups() {
        return groups;
    }

    public void setGroups(Integer groups) {
        this.groups = groups;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getLastMaxNumber() {
        return lastMaxNumber;
    }

    public void setLastMaxNumber(String lastMaxNumber) {
        this.lastMaxNumber = lastMaxNumber;
    }

    @Override
    public String toString() {
        return "PoolConfig{" +
                "id=" + id +
                ", remainGroups='" + remainGroups + '\'' +
                ", remainSize='" + remainSize + '\'' +
                ", groups=" + groups +
                ", lastMaxNumber='" + lastMaxNumber + '\'' +
                ", size=" + size +
                '}';
    }
}
