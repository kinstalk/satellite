package com.kinstalk.satellite.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @table  menuType
 */
public class MenuType {
	private static final long serialVersionUID = 1L;

    private Long id;//id
    
    private String name;//子系统名称

    private String hostUrl;//系统url。

    private Long orderId;//排序

    private List<Menu> menuList = new ArrayList<Menu>();//菜单列表


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

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }
}