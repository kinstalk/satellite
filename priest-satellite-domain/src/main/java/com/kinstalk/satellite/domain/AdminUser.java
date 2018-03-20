package com.kinstalk.satellite.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @table  adminUser
 */
public class AdminUser  {
	private static final long serialVersionUID = 1L;

	
    private String id;//id
    
    private String name;//姓名
    
    private String email;//email

    private String dn;//ldap 字段

    private String password;

    private List<UserGroup> userGroupList;//查询用户组。

    private List<Menu> menuList = new ArrayList<Menu>();//用户下面的菜单。

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<UserGroup> userGroupList) {
        this.userGroupList = userGroupList;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }
}