package com.kinstalk.satellite.domain;

/**
 * @table  groupMenu
 */
public class GroupMenu {
	private static final long serialVersionUID = 1L;

    private Long id;
	
    private Long groupId;//groupId
    
    private Long menuId;//menuId
    

	
	public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
	public Long getMenuId() {
        return this.menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}