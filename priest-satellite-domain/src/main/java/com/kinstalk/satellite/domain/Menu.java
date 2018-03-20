package com.kinstalk.satellite.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @table  menu
 */
public class Menu  {
	private static final long serialVersionUID = 1L;

	
    private Long id;//id
    
    private String name;//菜单名称
    
    private String url;//url
    
    private Long typeId;//子系统
    
    private Long parentId;//父节点
    
    private Long level;//层级
    
    private Long orderId;//排序ID

    private Integer isChecked;//只用作显示不更新。

    private List<Menu> children = new ArrayList<Menu>();//子节点菜单。

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
	public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
	public Long getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }
	public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
	public Long getLevel() {
        return this.level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }
	public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }


}