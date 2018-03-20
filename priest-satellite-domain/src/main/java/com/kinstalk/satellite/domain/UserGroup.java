package com.kinstalk.satellite.domain;

/**
 * @table userGroup
 */
public class UserGroup {
    private static final long serialVersionUID = 1L;


    private Long id;

    private String userId;//userId

    private Long groupId;//groupId


    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}