package com.kinstalk.satellite.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangchuanqi on 16/4/14.
 */
public class RunScriptTimerDetail implements Serializable{

    private Long id;

    private Long timerId;

    private Long startTime;

    private Long endTime;

    private String  resultFilePath;

    private String successRate;

    private Long createTime;

    private int agentId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimerId() {
        return timerId;
    }

    public void setTimerId(Long timerId) {
        this.timerId = timerId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath;
    }

    public String getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(String successRate) {
        this.successRate = successRate;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return "RunScriptTimerDetail{" +
                "id=" + id +
                ", timerId=" + timerId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", resultFilePath='" + resultFilePath + '\'' +
                ", successRate=" + successRate +
                ", createTime=" + createTime +
                '}';
    }

	// 内部类,Json转换用.
	static class Menu implements Serializable {
	
		private Long id;
	
		private String name;// 菜单名
	
		private String url;// 链接
	
		private Long menuTypeId;// 系统分类
	
		// 父节点 子菜单通过parentId 获得
		private Long parentId;
	
		private Long level;// 新增level字段 判断层级关系
	
		private String menuRole;// 针对菜单的权限配置，里面用正则表示，
	
		private Long orderId;// 排序id
	
		public Long getId() {
			return id;
		}
	
		public void setId(Long id) {
			this.id = id;
		}
	
		public String getName() {
			return name;
		}
	
		public void setName(String name) {
			this.name = name;
		}
	
		public String getUrl() {
			return url;
		}
	
		public void setUrl(String url) {
			this.url = url;
		}
	
		public Long getMenuTypeId() {
			return menuTypeId;
		}
	
		public void setMenuTypeId(Long menuTypeId) {
			this.menuTypeId = menuTypeId;
		}
	
		public Long getParentId() {
			return parentId;
		}
	
		public void setParentId(Long parentId) {
			this.parentId = parentId;
		}
	
		public Long getLevel() {
			return level;
		}
	
		public void setLevel(Long level) {
			this.level = level;
		}
	
		public String getMenuRole() {
			return menuRole;
		}
	
		public void setMenuRole(String menuRole) {
			this.menuRole = menuRole;
		}
	
		public Long getOrderId() {
			return orderId;
		}
	
		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}
	
		@Override
		public String toString() {
			return "UserMenu [id=" + id + ", name=" + name + ", url=" + url + ", menuTypeId=" + menuTypeId
					+ ", parentId=" + parentId + ", level=" + level + ", menuRole=" + menuRole + ", orderId=" + orderId + "]";
		}
	
	}

	// 内部类,Json转换用.
	static class MenuType implements Serializable {
	
		private String name;// 名称
	
		private String url;// 链接
	
		public String getName() {
			return name;
		}
	
		public void setName(String name) {
			this.name = name;
		}
	
		public String getUrl() {
			return url;
		}
	
		public void setUrl(String url) {
			this.url = url;
		}
	
		@Override
		public String toString() {
			return "MenuType [name=" + name + ", url=" + url + "]";
		}
	
	}

	/**
	 * 转换json类型
	 */
	public static class UserMenuJson implements Serializable {
	
		// 用户名称
		private String userName;
		// 返回菜单权限
		private List<com.kinstalk.satellite.domain.Menu> menuList;
		// 所有菜单权限
		private List<com.kinstalk.satellite.domain.Menu> allMenuList;
		// 返回系统类型
		private List<MenuType> menuTypeList;
	
		public String getUserName() {
			return userName;
		}
	
		public void setUserName(String userName) {
			this.userName = userName;
		}
	
		public List<com.kinstalk.satellite.domain.Menu> getMenuList() {
			return menuList;
		}
	
		public void setMenuList(List<com.kinstalk.satellite.domain.Menu> menuList) {
			this.menuList = menuList;
		}
	
		public List<MenuType> getMenuTypeList() {
			return menuTypeList;
		}
	
		public void setMenuTypeList(List<MenuType> menuTypeList) {
			this.menuTypeList = menuTypeList;
		}
	
		public List<com.kinstalk.satellite.domain.Menu> getAllMenuList() {
			return allMenuList;
		}
	
		public void setAllMenuList(List<com.kinstalk.satellite.domain.Menu> allMenuList) {
			this.allMenuList = allMenuList;
		}
	
	}
}
