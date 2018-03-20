package com.kinstalk.satellite.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @table group
 */
public class Group {
    private static final long serialVersionUID = 1L;


    private Long id;//id

    private String name;//组名称

    private Long orderId;//排序Id

    private int status;

    private Integer isChecked = 0;//只用作显示不更新。默认0，选择是1。

    private List<Menu> menuList = new ArrayList<Menu>();//群组下面的菜单。

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }
}